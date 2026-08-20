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
    
    // Countdown state for UPCOMING products
    const [timeLeft, setTimeLeft] = useState<string>('');
    const [isLocallyOpen, setIsLocallyOpen] = useState(false);
    const isUpcoming = product.saleStatus === 'UPCOMING' && product.saleStartAt && !isLocallyOpen;

    useEffect(() => {
        if (!isUpcoming || !product.saleStartAt || isLocallyOpen) return;

        const targetTime = new Date(product.saleStartAt).getTime();

        const updateTimer = () => {
            const now = new Date().getTime();
            const diff = targetTime - now;

            if (diff <= 0) {
                setIsLocallyOpen(true);
                return;
            }

            const days = Math.floor(diff / (1000 * 60 * 60 * 24));
            const hours = Math.floor((diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
            const mins = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));
            const secs = Math.floor((diff % (1000 * 60)) / 1000);

            if (days > 0) {
                setTimeLeft(`D-${days} ${hours.toString().padStart(2, '0')}:${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`);
            } else {
                setTimeLeft(`${hours.toString().padStart(2, '0')}:${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`);
            }
        };

        updateTimer();
        const interval = setInterval(updateTimer, 1000);
        return () => clearInterval(interval);
    }, [product.saleStatus, product.saleStartAt, isLocallyOpen]);

    const formatSaleStart = (dateString?: string) => {
        if (!dateString) return '';
        const d = new Date(dateString);
        const m = d.getMonth() + 1;
        const day = d.getDate();
        const h = d.getHours();
        const isPM = h >= 12;
        const h12 = h % 12 || 12;
        return `${m}/${day} ${isPM ? '오후' : '오전'} ${h12}시`;
    };

    return (
        <Link href={`/${product.slug}`} className={`${styles.card} ${product.isSoldOut ? styles.soldOut : ''}`}>
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

                {product.isSoldOut && !isUpcoming && (
                    <div className={styles.soldOutOverlay}>
                        <span className={styles.soldOutBadge}>품절 😢</span>
                    </div>
                )}
                
                {isUpcoming && (
                    <div className={styles.upcomingOverlay}>
                        <span className={styles.upcomingBadge}>🔔 {formatSaleStart(product.saleStartAt)} 오픈</span>
                        <div className={styles.countdown}>{timeLeft}</div>
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
                    {product.discountPrice && product.discountPrice > 0 ? (
                        <div className={styles.discountContainer}>
                            <span className={styles.originalPrice}>
                                ₩{product.price?.toLocaleString()}
                            </span>
                            <span className={styles.discountPrice}>₩{product.discountPrice.toLocaleString()}</span>
                        </div>
                    ) : (
                        <span className={styles.price}>₩{product.price?.toLocaleString()}</span>
                    )}
                </div>
            </div>
        </Link>
    );
}
