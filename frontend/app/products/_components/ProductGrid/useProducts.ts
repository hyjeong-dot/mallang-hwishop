import { useState, useEffect, useMemo } from 'react';

export interface ProductResponse {
    id: number;
    slug: string;
    korName: string;
    engName: string;
    description: string;
    price: number;
    categoryName: string;
    categoryIcon: string;
    imageSrc: string;
    isSoldOut: boolean;
}

export interface ProductListResponse {
    products: ProductResponse[];
    productCount: number;
}

interface UseProductsOptions {
    categoryId?: number | null;
    searchQuery?: string;
    page?: number;
    size?: number;
}

export function useProducts(options: UseProductsOptions = {}) {
    const { categoryId, searchQuery, page, size } = options;

    const [products, setProducts] = useState<ProductResponse[]>([]);
    const [totalProductCount, setTotalProductCount] = useState(0);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchProducts = async () => {
            setIsLoading(true);
            setError(null);

            const url = new URL('/api/products', window.location.origin);
            if (categoryId) url.searchParams.set('categoryId', categoryId.toString());
            if (searchQuery) url.searchParams.set('searchQuery', searchQuery);
            if (page !== undefined) url.searchParams.set('page', page.toString());
            if (size !== undefined) url.searchParams.set('size', size.toString());

            try {
                const response = await fetch(url.toString());
                
                if (response.status === 401) {
                    if (typeof window !== 'undefined') window.location.href = '/login';
                    return;
                }
                
                if (!response.ok) throw new Error('상품를 불러오는데 실패했습니다.');

                const data: ProductListResponse = await response.json();
                setProducts(data.products || []);
                setTotalProductCount(data.productCount || 0);
            } catch (err) {
                console.error('Fetch products error:', err);
                setError(err instanceof Error ? err.message : '알 수 없는 오류');
            } finally {
                setIsLoading(false);
            }
        };

        fetchProducts();
    }, [categoryId, searchQuery, page, size]);

    // 위 counts 로직은 필터링된 상품 기준이므로, 카테고리 필터 등에 쓸 때는 '필터 없는' useProducts를 별도로 부르는 것이 좋음 (Admin 방식)
    const productCounts = useMemo(() => {
        const counts: Record<string, number> = {};
        products.forEach((product) => {
            counts[product.categoryName] = (counts[product.categoryName] || 0) + 1;
        });
        return counts;
    }, [products]);

    return {
        products,
        isLoading,
        error,
        totalCount: totalProductCount,
        productCounts
    };
}
