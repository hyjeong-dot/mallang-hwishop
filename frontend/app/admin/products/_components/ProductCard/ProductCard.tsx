'use client';

import Image from 'next/image';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { Edit2, Trash2, ImageIcon } from 'lucide-react';
import styles from './ProductCard.module.css';
import { ProductResponse } from '../ProductGrid/useProducts';
import { getImageSrc } from '@/lib/api';

interface ProductCardProps {
    product: ProductResponse | null;
    onToggleSoldOut: (productId: string) => void;
    onDelete: (productId: string) => void;
}

export default function ProductCard({ product, onToggleSoldOut, onDelete }: ProductCardProps) {
    const router = useRouter();

    if (!product) return null;

    const formatPrice = (price: number) => {
        return new Intl.NumberFormat('ko-KR').format(price);
    };

    const productIdStr = String(product.id);
    const imageSrc = getImageSrc(product.imageSrc);

    return (
        <div
            className={`${styles.card} ${product.isSoldOut ? styles.soldOut : ''}`}
            onClick={() => router.push(`/admin/products/${product.id}`)}
        >
            {/* Image */}
            <div className={styles.imageWrapper}>
                {product.imageSrc ? (
                    <Image
                        src={imageSrc}
                        alt={product.korName}
                        fill
                        className={styles.image}
                        sizes="(max-width: 768px) 100vw, (max-width: 1200px) 50vw, 33vw"
                    />
                ) : (
                    <div className={styles.noImage}>
                        <ImageIcon size={48} />
                    </div>
                )}

                {product.isSoldOut && (
                    <span className={styles.soldOutBadge}>품절</span>
                )}

                <span className={styles.categoryBadge}>
                    {product.categoryIcon} {product.categoryName}
                </span>
            </div>

            {/* Content */}
            <div className={styles.content}>
                <div className={styles.header}>
                    <div className={styles.names}>
                        <h3 className={styles.korName}>{product.korName}</h3>
                        <p className={styles.engName}>{product.engName}</p>
                    </div>
                    {product.discountPrice && product.discountPrice > 0 ? (
                        <div className={styles.priceContainer}>
                            <span className={styles.originalPrice}>₩{formatPrice(product.price)}</span>
                            <span className={styles.discountPrice}>₩{formatPrice(product.discountPrice)}</span>
                        </div>
                    ) : (
                        <span className={styles.price}>₩{formatPrice(product.price)}</span>
                    )}
                </div>

                <p className={styles.description}>{product.description}</p>

                {/* Footer */}
                <div className={styles.footer}>
                    <div className={styles.toggleWrapper}>
                        <span className={styles.toggleLabel}>품절</span>
                        <button
                            className={`${styles.toggle} ${product.isSoldOut ? styles.active : ''}`}
                            onClick={(e) => {
                                e.stopPropagation();
                                onToggleSoldOut(productIdStr);
                            }}
                            title={product.isSoldOut ? '품절 해제' : '품절 처리'}
                        >
                            <span className={styles.toggleKnob} />
                        </button>
                    </div>

                    <div className={styles.actions}>
                        <Link
                            href={`/admin/products/${product.id}`}
                            className={styles.actionButton}
                            title="상세 보기 / 수정"
                            onClick={(e) => e.stopPropagation()}
                        >
                            <Edit2 size={18} />
                        </Link>
                        <button
                            className={`${styles.actionButton} ${styles.delete}`}
                            onClick={(e) => {
                                e.stopPropagation();
                                onDelete(productIdStr);
                            }}
                            title="삭제"
                        >
                            <Trash2 size={18} />
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}
