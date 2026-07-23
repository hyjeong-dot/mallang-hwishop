import { useState, useEffect } from 'react';
import { ChevronLeft, ChevronRight, LayoutGrid, List } from 'lucide-react';
import Modal from '@/components/common/Modal/Modal';
import ProductCard from '../ProductCard/ProductCard';
import ProductTable from '../ProductTable/ProductTable';
import styles from './ProductGrid.module.css';
import { useProducts } from './useProducts';
import LoadingDitto from '@/components/common/LoadingDitto/LoadingDitto';

interface ProductGridProps {
    selectedCategory?: number | null;
    searchQuery?: string;
}

const ITEMS_PER_PAGE = 12;

export default function ProductGrid({ selectedCategory = null, searchQuery = '' }: ProductGridProps) {
    const [productToDelete, setProductToDelete] = useState<string | null>(null);
    const [currentPage, setCurrentPage] = useState(1);
    const [viewMode, setViewMode] = useState<'grid' | 'list'>('grid');

    const { products, toggleSoldOut, deleteProduct, isLoading } = useProducts({
        selectedCategory: selectedCategory || undefined,
        searchQuery: searchQuery || undefined,
        page: currentPage - 1,
        size: ITEMS_PER_PAGE
    });

    // Reset pagination when filter changes
    useEffect(() => {
        setCurrentPage(1);
    }, [selectedCategory, searchQuery]);

    // Calculate pagination
    const totalPages = Math.ceil(products.length / ITEMS_PER_PAGE);
    const startIndex = (currentPage - 1) * ITEMS_PER_PAGE;
    const currentProducts = products.slice(startIndex, startIndex + ITEMS_PER_PAGE);

    const handlePageChange = (page: number) => {
        setCurrentPage(page);
        window.scrollTo({ top: 0, behavior: 'smooth' });
    };

    const handleDeleteClick = (productId: string) => {
        setProductToDelete(productId);
    };

    const confirmDelete = () => {
        if (productToDelete) {
            deleteProduct(productToDelete);
            setProductToDelete(null);
        }
    };

    if (isLoading) {
        return <LoadingDitto message="상품 정보를 불러오는 중..." />;
    }

    return (
        <>
            {/* View Toggle */}
            <div className={styles.viewToggle}>
                <button
                    className={`${styles.toggleBtn} ${viewMode === 'grid' ? styles.active : ''}`}
                    onClick={() => setViewMode('grid')}
                    title="그리드 뷰"
                >
                    <LayoutGrid size={16} />
                    <span>Grid</span>
                </button>
                <button
                    className={`${styles.toggleBtn} ${viewMode === 'list' ? styles.active : ''}`}
                    onClick={() => setViewMode('list')}
                    title="리스트 뷰"
                >
                    <List size={16} />
                    <span>List</span>
                </button>
                <span className={styles.productCount}>{products.length}개 상품</span>
            </div>

            {/* Grid View */}
            {viewMode === 'grid' && (
                <section className={styles.productGrid} aria-label="Product list">
                    {currentProducts.map((product) => (
                        <ProductCard
                            key={product.id}
                            product={product}
                            onToggleSoldOut={toggleSoldOut}
                            onDelete={handleDeleteClick}
                        />
                    ))}
                </section>
            )}

            {/* List View */}
            {viewMode === 'list' && (
                <ProductTable
                    products={currentProducts}
                    onToggleSoldOut={toggleSoldOut}
                    onDelete={handleDeleteClick}
                />
            )}

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

            <Modal
                isOpen={!!productToDelete}
                onClose={() => setProductToDelete(null)}
                title="상품 삭제"
                description="정말로 이 상품를 삭제하시겠습니까? 삭제된 상품는 복구할 수 없습니다."
                confirmText="삭제"
                variant="danger"
                onConfirm={confirmDelete}
            />
        </>
    );
}