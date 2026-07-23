'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import { useCategories } from '../../../_components/CategoryTabs/useCategories';
import { useProductDetail, ProductImageDetail, ProductOptionDetail } from '../../_components/ProductDetailInfo/useProductDetail';
import ProductForm, { ProductFormData, ImageItem, OptionFormData } from '../../../_components/ProductForm/ProductForm';
import { useProductSubmit } from '../../../_components/ProductForm/useProductSubmit';
import LoadingDitto from '@/components/common/LoadingDitto/LoadingDitto';
import { getImageSrc } from '@/lib/api';
import styles from '../page.module.css';

interface ProductEditContainerProps {
    productId: number;
}

export default function ProductEditContainer({ productId }: ProductEditContainerProps) {
    const router = useRouter();
    const { product, isLoading: isProductLoading } = useProductDetail(productId);
    const { categories } = useCategories();
    const { updateProduct, isSubmitting } = useProductSubmit();

    const [isInitialized, setIsInitialized] = useState(false);
    const [initialFormValues, setInitialFormValues] = useState<Partial<ProductFormData>>({});
    const [initialImages, setInitialImages] = useState<ImageItem[]>([]);
    const [initialOptions, setInitialOptions] = useState<OptionFormData[]>([]);

    useEffect(() => {
        if (product && categories.length > 0 && !isInitialized) {
            const matchedCategory = categories.find((cat) => cat.name === product.categoryName);

            setInitialFormValues({
                korName: product.korName,
                engName: product.engName,
                description: product.description || '',
                price: String(product.price),
                categoryId: matchedCategory ? String(matchedCategory.id) : '',
                isAvailable: product.isAvailable,
                isSoldOut: product.isSoldOut,
            });

            if (product.images && product.images.length > 0) {
                setInitialImages(product.images.map((img: ProductImageDetail) => ({
                    id: String(img.id),
                    url: getImageSrc(img.srcUrl),
                    isPrimary: img.sortOrder === 0
                })));
            } else if (product.imageSrc && product.imageSrc !== 'blank.png') {
                setInitialImages([{
                    id: 'original',
                    url: getImageSrc(product.imageSrc),
                    isPrimary: true
                }]);
            }

            // 옵션 데이터 매핑
            if (product.options && product.options.length > 0) {
                setInitialOptions(product.options.map((opt: ProductOptionDetail) => ({
                    id: String(opt.id),
                    name: opt.name,
                    type: opt.isMultiSelect ? 'checkbox' as const : 'radio' as const,
                    required: opt.isRequired,
                    items: opt.items.map(item => ({
                        id: String(item.id),
                        name: item.name,
                        priceDelta: String(item.priceDelta)
                    }))
                })));
            }

            setIsInitialized(true);
        }
    }, [product, categories, isInitialized]);

    const handleSubmit = async (formData: ProductFormData, images: ImageItem[], options: OptionFormData[]) => {
        try {
            await updateProduct(productId, formData, images, initialImages, options);
            router.push(`/admin/products/${productId}`);
        } catch (error) {
            console.error('Update failed:', error);
        }
    };

    const handleCancel = () => {
        router.push(`/admin/products/${productId}`);
    };

    if (isProductLoading) {
        return (
            <div className={styles.loadingWrapper}>
                <LoadingDitto message="상품 정보를 불러오는 중..." />
            </div>
        );
    }

    if (!product && !isProductLoading) {
        return (
            <div className={styles.errorWrapper}>
                <p>상품를 찾을 수 없습니다.</p>
                <button onClick={() => router.push('/admin/products')} className={styles.backBtn}>
                    목록으로 돌아가기
                </button>
            </div>
        );
    }

    return (
        <>
            {isInitialized && (
                <ProductForm
                    initialFormData={initialFormValues}
                    initialImages={initialImages}
                    initialOptions={initialOptions}
                    categories={categories}
                    onSubmit={handleSubmit}
                    onCancel={handleCancel}
                    isSubmitting={isSubmitting}
                    submitLabel="변경사항 저장"
                />
            )}
        </>
    );
}
