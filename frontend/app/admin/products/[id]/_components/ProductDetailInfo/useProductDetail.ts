import { useState, useEffect } from 'react';

export interface ProductImageDetail {
    id: number;
    srcUrl: string;
    sortOrder: number;
}

export interface OptionItemDetail {
    id: number;
    optionId: number;
    name: string;
    priceDelta: number;
    sortOrder: number;
}

export interface ProductOptionDetail {
    id: number;
    productId: number;
    name: string;
    isRequired: boolean;
    isMultiSelect: boolean;
    sortOrder: number;
    items: OptionItemDetail[];
}

export interface ProductDetail {
    id: number;
    korName: string;
    engName: string;
    description: string;
    price: number;
    discountPrice?: number;
    categoryName: string;
    imageSrc: string;
    images?: ProductImageDetail[];
    options?: ProductOptionDetail[];
    isAvailable: boolean;
    isSoldOut: boolean;
    sortOrder: number;
    createdAt: string;
    updatedAt: string;
}

export function useProductDetail(id: number) {
    const [product, setProduct] = useState<ProductDetail | null>(null);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        if (!id || isNaN(id)) {
            setIsLoading(false);
            return;
        }

        const fetchProduct = async () => {
            try {
                setIsLoading(true);
                const response = await fetch(`/api/admin/products/${id}`);

                if (response.status === 401) {
                    if (typeof window !== 'undefined') window.location.href = '/login';
                    return;
                }

                if (!response.ok) {
                    throw new Error('Failed to fetch product details');
                }

                const data: ProductDetail = await response.json();
                setProduct(data);
            } catch (err) {
                console.error('Error fetching product detail:', err);
                setError(err instanceof Error ? err.message : 'An unknown error occurred');
            } finally {
                setIsLoading(false);
            }
        };

        fetchProduct();
    }, [id]);

    return { product, isLoading, error };
}
