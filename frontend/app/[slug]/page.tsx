'use client';

import { use } from 'react';
import ProductDetailHeader from './_components/ProductDetailHeader';
import ProductDetailImage from './_components/ProductDetailImage';
import ProductDetailInfo from './_components/ProductDetailInfo';
import ProductReviews from './_components/ProductReviews/ProductReviews';
import { useProductDetail } from './_components/ProductDetailInfo/useProductDetail';
import styles from './page.module.css';

export default function ProductDetailPage({ params }: { params: Promise<{ slug: string }> }) {
    const { slug } = use(params);

    const { product } = useProductDetail(slug);

    return (
        <div className={styles.page}>
            <ProductDetailHeader />

            <main className={styles.container}>
                <div className={styles.layout}>
                    <ProductDetailImage slug={slug} />

                    <div>
                        <ProductDetailInfo
                            slug={slug}
                        />
                        {product && <ProductReviews productId={product.id} />}
                    </div>
                </div>
            </main>
        </div>
    );
}
