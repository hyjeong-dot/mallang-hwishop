'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import toast from 'react-hot-toast';
import styles from '../page.module.css';
import Modal from '@/components/common/Modal/Modal';
import ProductDetailHeader from './ProductDetailHeader/ProductDetailHeader';
import ProductDetailImage from './ProductDetailImage/ProductDetailImage';
import ProductDetailInfo from './ProductDetailInfo/ProductDetailInfo';


interface AdminProductDetailContainerProps {
    id: number;
}

export default function AdminProductDetailContainer({ id }: AdminProductDetailContainerProps) {
    const router = useRouter();
    const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);

    const handleConfirmDelete = async () => {
        try {
            const response = await fetch(`/api/admin/products/${id}`, {
                method: 'DELETE',
            });
            
            if (response.status === 401) {
                if (typeof window !== 'undefined') window.location.href = '/login';
                return;
            }

            if (!response.ok) throw new Error('상품 삭제에 실패했습니다.');

            toast.success('상품가 삭제되었습니다.');
            router.push('/admin/products');
        } catch (error) {
            console.error('Failed to delete product:', error);
            toast.error('상품 삭제에 실패했습니다.');
            setIsDeleteModalOpen(false);
        }
    };

    return (
        <>
            <ProductDetailHeader
                id={id}
                onDelete={() => setIsDeleteModalOpen(true)}
            />

            <main className={styles.content}>
                {/* Left Column: Image & Nutrition */}
                <ProductDetailImage productId={id} />

                {/* Right Column: Info & Options */}
                <div className={styles.infoSection}>
                    <ProductDetailInfo id={id} />
                </div>
            </main>

            {/* Deletion Confirmation Modal */}
            <Modal
                isOpen={isDeleteModalOpen}
                onClose={() => setIsDeleteModalOpen(false)}
                title="상품 삭제"
                description="정말로 이 상품를 삭제하시겠습니까? 삭제된 상품는 복구할 수 없습니다."
                confirmText="삭제"
                variant="danger"
                onConfirm={handleConfirmDelete}
            />
        </>
    );
}
