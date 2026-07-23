import { useState, useEffect } from 'react';

export interface ProductImageResponse {
    id: number;
    productId: number;
    srcUrl: string;
    altText: string;
    sortOrder: number;
}

export interface ProductImageListResponse {
    images: ProductImageResponse[];
}

export function useProductImages(productId: number) {
    const [images, setImages] = useState<ProductImageResponse[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        if (!productId) return;

        const fetchImages = async () => {
            try {
                setIsLoading(true);
                const response = await fetch(`/api/admin/products/${productId}/product-images`);
                
                if (response.status === 401) {
                    if (typeof window !== 'undefined') window.location.href = '/login';
                    return;
                }
                
                if (!response.ok) {
                    throw new Error('Failed to fetch product images');
                }

                const data: ProductImageListResponse = await response.json();
                setImages(data.images);
            } catch (err) {
                console.error('Error fetching product images:', err);
                setError(err instanceof Error ? err.message : 'An unknown error occurred');
            } finally {
                setIsLoading(false);
            }
        };

        fetchImages();
    }, [productId]);

    return { images, isLoading, error };
}
