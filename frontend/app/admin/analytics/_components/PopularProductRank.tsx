'use client';

import { PopularProduct } from './useAnalytics';
import styles from '../page.module.css';

interface PopularProductRankProps {
    products: PopularProduct[];
}

export default function PopularProductRank({ products }: PopularProductRankProps) {
    const maxQty = products[0]?.totalQuantity || 1;

    return (
        <section className={styles.chartSection}>
            <h2 className={styles.sectionTitle}>🏆 인기 상품 TOP 5</h2>
            <div className={styles.rankList}>
                {products.length === 0 ? (
                    <div className={styles.emptyRank}>아직 주문 데이터가 없어요.</div>
                ) : (
                    products.map((product, idx) => (
                        <div key={idx} className={styles.rankItem}>
                            <span className={styles.rankNum}>
                                {idx === 0 ? '🥇' : idx === 1 ? '🥈' : idx === 2 ? '🥉' : `${idx + 1}`}
                            </span>
                            <div className={styles.rankInfo}>
                                <span className={styles.rankName}>{product.productName}</span>
                                <div className={styles.rankBarOuter}>
                                    <div
                                        className={styles.rankBarInner}
                                        style={{ width: `${(product.totalQuantity / maxQty) * 100}%` }}
                                    />
                                </div>
                            </div>
                            <div className={styles.rankStats}>
                                <span className={styles.rankQty}>{product.totalQuantity}개</span>
                                <span className={styles.rankRev}>₩{product.totalRevenue.toLocaleString()}</span>
                            </div>
                        </div>
                    ))
                )}
            </div>
        </section>
    );
}
