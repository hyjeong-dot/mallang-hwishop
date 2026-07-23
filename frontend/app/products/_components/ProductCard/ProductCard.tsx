import { useState, useEffect } from 'react';
import Image from 'next/image';
import Link from 'next/link';
import { ImageIcon } from 'lucide-react';
import styles from './ProductCard.module.css';
import { ProductResponse } from '../ProductGrid/useProducts';
import { getImageSrc } from '@/lib/api';

interface ProductCardProps {
    product: ProductResponse;
    onLoad?: () => void;
}

/**
 * 표준 상품 카드 컴포넌트
 * 백엔드 DTO(ProductResult, FavoriteProductResult 등)가 통일되어 이제 매우 단순한 구조를 유지합니다.
 */
export default function ProductCard({ product, onLoad }: ProductCardProps) {
    const imageSrc = getImageSrc(product.imageSrc);

    return (
        <Link href={`/products/${product.slug}`} className={`${styles.card} ${product.isSoldOut ? styles.soldOut : ''}`}>
            <div className={styles.imageWrapper}>
                <Image
                    src={imageSrc}
                    alt={product.korName}
                    fill
                    className={styles.image}
                    sizes="(max-width: 768px) 100vw, (max-width: 1024px) 50vw, 25vw"
                    onLoad={() => onLoad?.()}
                    onError={() => onLoad?.()}
                />

                {product.isSoldOut && (
                    <div className={styles.soldOutOverlay}>
                        <span className={styles.soldOutBadge}>품절 😢</span>
                    </div>
                )}

                <span className={styles.categoryBadge}>
                    {product.categoryIcon} {product.categoryName}
                </span>
            </div>

            <div className={styles.content}>
                <h3 className={styles.korName}>{product.korName}</h3>
                <p className={styles.engName}>{product.engName}</p>
                {product.description && (
                    <p className={styles.description}>{product.description}</p>
                )}
                <div className={styles.priceRow}>
                    <span className={styles.price}>₩{product.price?.toLocaleString()}</span>
                </div>
            </div>
        </Link>
    );
}
