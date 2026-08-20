import { useState, useEffect } from 'react';
import { ProductResponse } from '../../../_components/ProductGrid/useProducts';

export function useProductDetail(slug: string) {
    const [product, setProduct] = useState<ProductResponse | null>(null);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        if (!slug) return;

        const fetchProduct = async () => {
            setIsLoading(true);
            try {
                const response = await fetch(`/api/products/${slug}`);

                if (response.status === 401) {
                    if (typeof window !== 'undefined') window.location.href = '/login';
                    return;
                }

                if (!response.ok) {
                    throw new Error('상품 정보를 가져오는데 실패했습니다.');
                }
                const data = await response.json();
                setProduct(data);
            } catch (err) {
                console.error('Fetch product detail error:', err);
                setError(err instanceof Error ? err.message : '알 수 없는 오류');
            } finally {
                setIsLoading(false);
            }
        };

        fetchProduct();
    }, [slug]);

    return { product, isLoading, error };
}
