'use client';

import { use } from 'react';
import styles from './page.module.css';
import AdminProductDetailContainer from './_components/AdminProductDetailContainer';

interface ProductDetailPageProps {
    params: Promise<{
        id: string;
    }>;
}

/**
 * 어드민 상품 상세 페이지
 * 상품 삭제 로직과 레이아웃 구성은 AdminProductDetailContainer로 분리되었습니다.
 */
export default function ProductDetailPage({ params }: ProductDetailPageProps) {
    const { id: idStr } = use(params);
    const id = Number(idStr);

    return (
        <div className={styles.container}>
            <AdminProductDetailContainer id={id} />
        </div>
    );
}
