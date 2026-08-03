import { useState } from 'react';
import toast from 'react-hot-toast';
import { ProductFormData, ImageItem, OptionFormData } from './ProductForm';

export function useProductSubmit() {
    const [isSubmitting, setIsSubmitting] = useState(false);

    const saveOptions = async (productId: number, options: OptionFormData[]) => {
        const payload = {
            options: options.map(opt => ({
                name: opt.name,
                type: opt.type,
                required: opt.required,
                items: opt.items.map(item => ({
                    name: item.name,
                    priceDelta: Number(item.priceDelta) || 0
                }))
            }))
        };

        const res = await fetch(`/api/admin/products/${productId}/options`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (res.status === 401 && typeof window !== 'undefined') {
            window.location.href = '/login';
            return;
        }

        if (!res.ok) {
            console.error('Failed to save options');
        }
    };

    const uploadImages = async (productId: number, images: ImageItem[]) => {
        let primaryImageId: number | null = null;

        for (const img of images) {
            let currentImageId: number | null = !isNaN(Number(img.id)) ? Number(img.id) : null;

            if (img.file) {
                const uploadFormData = new FormData();
                uploadFormData.append('file', img.file);

                try {
                    const uploadResponse = await fetch('/api/upload-file', {
                        method: 'POST',
                        body: uploadFormData,
                    });
                    
                    if (uploadResponse.status === 401 && typeof window !== 'undefined') {
                        window.location.href = '/login';
                        return;
                    }

                    if (uploadResponse.ok) {
                        const uploadData = await uploadResponse.json();
                        const srcUrl = uploadData.url;

                        const registerResponse = await fetch(`/api/admin/products/${productId}/product-images`, {
                            method: 'POST',
                            headers: { 'Content-Type': 'application/json' },
                            body: JSON.stringify({
                                srcUrl: srcUrl,
                                sortOrder: img.isPrimary ? 0 : 1,
                            }),
                        });
                        
                        if (registerResponse.status === 401 && typeof window !== 'undefined') {
                            window.location.href = '/login';
                            return;
                        }

                        if (registerResponse.ok) {
                            currentImageId = await registerResponse.json();
                        }
                    }
                } catch (error) {
                    console.error('Image upload failed:', error);
                }
            }

            if (img.isPrimary && currentImageId) {
                primaryImageId = currentImageId;
            }
        }

        if (primaryImageId) {
            const pResponse = await fetch(`/api/admin/products/${productId}/product-images/${primaryImageId}/primary`, {
                method: 'PATCH',
            });
            if (pResponse.status === 401 && typeof window !== 'undefined') {
                window.location.href = '/login';
                return;
            }
        }
    };

    const createProduct = async (formData: ProductFormData, images: ImageItem[], options: OptionFormData[]) => {
        setIsSubmitting(true);
        try {
            const response = await fetch('/api/admin/products', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    korName: formData.korName.trim(),
                    engName: formData.engName.trim(),
                    description: formData.description.trim(),
                    price: Number(formData.price),
                    categoryId: Number(formData.categoryId),
                    isAvailable: formData.isAvailable,
                    sortOrder: 0,
                    saleStartAt: formData.isReservation && formData.saleStartAt ? formData.saleStartAt + ':00' : null,
                    stock: formData.isUnlimitedStock ? null : Number(formData.stock),
                    maxPerOrder: 2,
                }),
            });
            
            if (response.status === 401 && typeof window !== 'undefined') {
                window.location.href = '/login';
                return;
            }

            if (!response.ok) throw new Error('Failed to create product');
            const productId = await response.json();

            await uploadImages(productId, images);

            // 옵션 저장
            if (options.length > 0) {
                await saveOptions(productId, options);
            }

            toast.success('새 상품가 등록되었습니다! 💜');
            return productId;
        } catch (error) {
            toast.error('상품 등록에 실패했습니다.');
            throw error;
        } finally {
            setIsSubmitting(false);
        }
    };

    const updateProduct = async (productId: number, formData: ProductFormData, images: ImageItem[], initialImages: ImageItem[], options?: OptionFormData[]) => {
        setIsSubmitting(true);
        try {
            const response = await fetch(`/api/admin/products/${productId}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    korName: formData.korName.trim(),
                    engName: formData.engName.trim(),
                    description: formData.description.trim(),
                    price: Number(formData.price),
                    categoryId: Number(formData.categoryId),
                    isAvailable: formData.isAvailable,
                    isSoldOut: formData.isSoldOut,
                    sortOrder: 0,
                    saleStartAt: formData.isReservation && formData.saleStartAt ? formData.saleStartAt + ':00' : null,
                    stock: formData.isUnlimitedStock ? null : Number(formData.stock),
                    maxPerOrder: 2,
                }),
            });
            
            if (response.status === 401 && typeof window !== 'undefined') {
                window.location.href = '/login';
                return;
            }

            if (!response.ok) throw new Error('Failed to update product');

            // Find deleted image IDs
            const initialIds = initialImages.map(img => img.id);
            const currentIds = images.map(img => img.id);
            const deletedIds = initialIds.filter(id => !currentIds.includes(id));

            for (const imageId of deletedIds) {
                if (!isNaN(Number(imageId))) {
                    const dResponse = await fetch(`/api/admin/products/product-images/${imageId}`, {
                        method: 'DELETE',
                    });
                    if (dResponse.status === 401 && typeof window !== 'undefined') {
                        window.location.href = '/login';
                        return;
                    }
                }
            }

            await uploadImages(productId, images);

            // 옵션 저장
            if (options) {
                await saveOptions(productId, options);
            }

            toast.success('상품 정보가 수정되었습니다! 💜');
            return true;
        } catch (error) {
            toast.error('상품 수정에 실패했습니다.');
            throw error;
        } finally {
            setIsSubmitting(false);
        }
    };

    return { createProduct, updateProduct, isSubmitting };
}
