import { useState, useEffect } from 'react';

export interface ProductImageResult {
    id: number;
    productId: number;
    srcUrl: string;
    sortOrder: number;
}

export function useProductImages(id: number) {
    const [images, setImages] = useState<ProductImageResult[]>([]);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        if (!id) return;

        const fetchImages = async () => {
            setIsLoading(true);
            try {
                const response = await fetch(`/api/products/${id}/images`);

                if (response.status === 401) {
                    if (typeof window !== 'undefined') window.location.href = '/login';
                    return;
                }

                if (!response.ok) {
                    throw new Error('이미지를 가져오는데 실패했습니다.');
                }
                const data = await response.json();
                setImages(data.images || []);
            } catch (err) {
                console.error('Fetch product images error:', err);
            } finally {
                setIsLoading(false);
            }
        };

        fetchImages();
    }, [id]);

    return { images, isLoading };
}
