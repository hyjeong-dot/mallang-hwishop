import { useState, useEffect, useCallback } from 'react';
import { ChevronLeft, ChevronRight } from 'lucide-react';
import styles from './ProductGrid.module.css';
import ProductCard from '../ProductCard/ProductCard';
import { useProducts } from './useProducts';
import LoadingCharacter from '@/components/common/LoadingCharacter/LoadingCharacter';

interface ProductGridProps {
    selectedCategory: number | null;
    searchQuery: string;
}

const ITEMS_PER_PAGE = 8;

export default function ProductGrid({ selectedCategory, searchQuery }: ProductGridProps) {
    const [currentPage, setCurrentPage] = useState(1);
    const [loadedImagesCount, setLoadedImagesCount] = useState(0);
    const [isImagesReady, setIsImagesReady] = useState(false);

    const { products, isLoading, error } = useProducts({
        categoryId: selectedCategory,
        searchQuery: searchQuery
    });

    const totalPages = Math.ceil(products.length / ITEMS_PER_PAGE);
    const startIndex = (currentPage - 1) * ITEMS_PER_PAGE;
    const currentProducts = products.slice(startIndex, startIndex + ITEMS_PER_PAGE);

    const handlePageChange = (page: number) => {
        setCurrentPage(page);
        window.scrollTo({ top: 0, behavior: 'smooth' });
    };

    // 필터 변경 시 첫 페이지로 리셋
    useEffect(() => {
        setCurrentPage(1);
    }, [selectedCategory, searchQuery]);

    // 페이지나 필터 변경 시 이미지 로딩 상태 리셋
    useEffect(() => {
        setIsImagesReady(false);
        setLoadedImagesCount(0);
    }, [selectedCategory, searchQuery, currentPage]);

    useEffect(() => {
        if (!isLoading && currentProducts && currentProducts.length > 0) {
            if (loadedImagesCount >= currentProducts.length) {
                setIsImagesReady(true);
            }
        } else if (!isLoading && currentProducts && currentProducts.length === 0) {
            setIsImagesReady(true);
        }
    }, [loadedImagesCount, currentProducts, isLoading]);

    const handleImageLoad = useCallback(() => {
        setLoadedImagesCount(prev => prev + 1);
    }, []);

    // 데이터 로딩 중 (이미지 제외 데이터만)
    if (isLoading && !isImagesReady) {
        return <LoadingCharacter message="상품을 준비하고 있어요... 💖" />;
    }

    if (error) {
        return (
            <div className={styles.emptyWrapper}>
                <span className={styles.emptyEmoji}>😢</span>
                <p className={styles.emptyText}>상품를 불러오는데 문제가 생겼어요</p>
                <p className={styles.emptySubText}>{error}</p>
            </div>
        );
    }

    if (products.length === 0) {
        return (
            <div className={styles.emptyWrapper}>
                <span className={styles.emptyEmoji}>🔍</span>
                <p className={styles.emptyText}>해당 카테고리에 상품가 없어요</p>
                <p className={styles.emptySubText}>다른 카테고리를 선택해보세요!</p>
            </div>
        );
    }

    return (
        <section className={styles.gridSection}>
            {/* 이미지가 준비되지 않았을 때만 보여주는 오버레이 로더 */}
            {!isImagesReady && currentProducts.length > 0 && (
                <div className={styles.overlayLoader}>
                    <LoadingCharacter message="이미지를 선명하게 다듬고 있어요... 💖" />
                </div>
            )}

            <div
                key={currentPage}
                className={`${styles.grid} ${isImagesReady ? styles.fadeIn : styles.hidden}`}
            >
                {currentProducts.map((product) => (
                    <ProductCard
                        key={product.id}
                        product={product}
                        onLoad={handleImageLoad}
                    />
                ))}
            </div>

            {totalPages > 1 && (
                <div className={styles.pagination}>
                    <button
                        className={styles.pageButton}
                        onClick={() => handlePageChange(currentPage - 1)}
                        disabled={currentPage === 1}
                        aria-label="Previous page"
                    >
                        <ChevronLeft size={16} />
                    </button>

                    {Array.from({ length: totalPages }, (_, i) => i + 1).map((page) => (
                        <button
                            key={page}
                            className={`${styles.pageButton} ${currentPage === page ? styles.active : ''}`}
                            onClick={() => handlePageChange(page)}
                        >
                            {page}
                        </button>
                    ))}

                    <button
                        className={styles.pageButton}
                        onClick={() => handlePageChange(currentPage + 1)}
                        disabled={currentPage === totalPages}
                        aria-label="Next page"
                    >
                        <ChevronRight size={16} />
                    </button>
                </div>
            )}
        </section>
    );
}
