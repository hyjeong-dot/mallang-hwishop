import { useState, useEffect, useMemo } from 'react';
import toast from 'react-hot-toast';

export interface ProductResponse {
    id: number;
    korName: string;
    engName: string;
    description: string;
    price: number;
    discountPrice?: number;
    categoryName: string;
    categoryIcon: string;
    imageSrc: string;
    images?: any[];
    isAvailable: boolean;
    isSoldOut: boolean;
    saleStartAt?: string;
    saleStatus?: string;
    sortOrder: number;
    createdAt?: string;
    updatedAt?: string;
    instagramUrl?: string;
}

export interface ProductListResponse {
    products: ProductResponse[];
    productCount: number;
}

interface UseProductsOptions {
    selectedCategory?: number | null;
    searchQuery?: string;
    page?: number;
    size?: number;
}

export function useProducts(options: UseProductsOptions = {}) {
    const { selectedCategory, searchQuery, page, size } = options;

    const [products, setProducts] = useState<ProductResponse[]>([]);
    const [isLoading, setIsLoading] = useState(true);

    // Fetch products
    useEffect(() => {
        const fetchProducts = async () => {
            const url = new URL('/api/admin/products', window.location.origin);
            const params = url.searchParams;

            if (selectedCategory) params.set('categoryId', selectedCategory.toString());
            if (searchQuery) params.set('searchQuery', searchQuery);
            if (page !== undefined) params.set('page', page.toString());
            if (size !== undefined) params.set('size', size.toString());

            try {
                const response = await fetch(url.toString());
                
                if (response.status === 401) {
                    if (typeof window !== 'undefined') window.location.href = '/login';
                    return;
                }
                
                if (!response.ok) throw new Error('데이터를 불러오는데 실패했습니다.');

                const data: ProductListResponse = await response.json();
                setProducts(data.products || []);
            } catch (error) {
                console.error('Fetch error:', error);
                toast.error('상품 목록을 가져오는 중 오류가 발생했습니다.');
            } finally {
                setIsLoading(false);
            }
        };

        fetchProducts();
    }, [selectedCategory, searchQuery, page, size]);

    // Actions
    const toggleSoldOut = async (productId: string) => {
        // Optimistic update
        setProducts(prev => prev.map(product =>
            String(product.id) === productId ? { ...product, isSoldOut: !product.isSoldOut } : product
        ));

        try {
            const response = await fetch(`/api/admin/products/${productId}/sold-out`, { method: 'PATCH' });
            if (response.status === 401) {
                if (typeof window !== 'undefined') window.location.href = '/login';
                return;
            }
            if (!response.ok) {
                throw new Error('품절 상태 변경 실패');
            }
        } catch (error) {
            console.error('Toggle sold out error:', error);
            toast.error('품절 상태 변경에 실패했습니다.');
            // Revert optimistic update
            setProducts(prev => prev.map(product =>
                String(product.id) === productId ? { ...product, isSoldOut: !product.isSoldOut } : product
            ));
        }
    };

    const deleteProduct = async (productId: string) => {
        // Find product to delete for potential rollback
        const productToDelete = products.find(product => String(product.id) === productId);
        if (!productToDelete) return;

        // Optimistic update
        setProducts(prev => prev.filter(product => String(product.id) !== productId));

        try {
            const response = await fetch(`/api/admin/products/${productId}`, { method: 'DELETE' });
            if (response.status === 401) {
                if (typeof window !== 'undefined') window.location.href = '/login';
                return;
            }
            if (!response.ok) {
                throw new Error('상품 삭제 실패');
            }
            toast.success('상품가 성공적으로 삭제되었습니다.');
        } catch (error) {
            console.error('Delete product error:', error);
            toast.error('상품 삭제에 실패했습니다.');
            // Revert optimistic update (insert back)
            setProducts(prev => [...prev, productToDelete]);
        }
    };

    // Stats
    const stats = useMemo(() => ({
        total: products.length,
        available: products.filter(m => !m.isSoldOut).length,
        soldOut: products.filter(m => m.isSoldOut).length
    }), [products]);

    // Product counts by category - using categoryName since ID isn't in ProductResponse
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
        stats,
        productCounts,
        toggleSoldOut,
        deleteProduct
    };
}

