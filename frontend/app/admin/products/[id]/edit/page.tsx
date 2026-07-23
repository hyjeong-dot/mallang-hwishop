'use client';

import { use } from 'react';
import Link from 'next/link';
import { ArrowLeft } from 'lucide-react';
import ProductEditContainer from './_components/ProductEditContainer';
import styles from './page.module.css';

interface EditProductPageProps {
    params: Promise<{
        id: string;
    }>;
}

/**
 * 어드민 상품 수정 페이지
 * 상품 정보를 불러오고 폼을 초기화하는 로직은 ProductEditContainer로 분리되었습니다.
 */
export default function EditProductPage({ params }: EditProductPageProps) {
    const { id } = use(params);
    const productId = Number(id);

    return (
        <div className={styles.container}>
            {/* 뒤로 가기 버튼 */}
            <Link href={`/admin/products/${productId}`} className={styles.backButton}>
                <ArrowLeft size={20} />
                <span>상세 페이지로 돌아가기</span>
            </Link>

            {/* 헤더 섹션 */}
            <header className={styles.header}>
                <h1 className={styles.title}>상품 수정</h1>
                <p className={styles.subtitle}>상품 정보를 수정합니다</p>
            </header>

            {/* 수정 컨테이너 (비즈니스 로직 & 폼 렌더링) */}
            <ProductEditContainer productId={productId} />
        </div>
    );
}
