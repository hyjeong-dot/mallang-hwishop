'use client';

import { useState, useEffect } from 'react';
import Image from 'next/image';
import styles from './ProductDetailImage.module.css';
import { useProductImages } from './useProductImages';
import LoadingCharacter from '@/components/common/LoadingCharacter/LoadingCharacter';
import { useProductDetail } from '../ProductDetailInfo/useProductDetail';
import { getImageSrc } from '@/lib/api';

interface ProductDetailImageProps {
    slug: string;
}

export default function ProductDetailImage({ slug }: ProductDetailImageProps) {
    const { product } = useProductDetail(slug);
    const { images, isLoading: isDataLoading } = useProductImages(product?.id ?? 0);
    const [selectedIndex, setSelectedIndex] = useState(0);
    const [isImageReady, setIsImageReady] = useState(false);

    // 데이터 로딩 중일 때 표시
    if (isDataLoading) return <LoadingCharacter message="이미지를 불러오는 중..." />;

    const rawMainImage = images.length > 0 ? images[selectedIndex].srcUrl : product?.imageSrc;
    const mainImageSrc = getImageSrc(rawMainImage);

    return (
        <div className={styles.gallery}>
            {!isImageReady && <LoadingCharacter message="이미지를 준비하고 있어요... 💜" />}
            <div className={`${styles.mainImageWrapper} ${!isImageReady ? styles.hidden : styles.fadeIn}`}>
                <Image
                    src={mainImageSrc}
                    alt={product?.korName || '상품 이미지'}
                    fill
                    className={styles.mainImage}
                    priority
                    onLoad={() => setIsImageReady(true)}
                    onError={() => setIsImageReady(true)}
                />
                {product?.isSoldOut && (
                    <div className={styles.soldOutOverlay}>품절 😢</div>
                )}
            </div>
            {images.length > 1 && (
                <div className={styles.thumbnails}>
                    {images.map((img, index) => (
                        <div
                            key={img.id}
                            className={`${styles.thumbnail} ${selectedIndex === index ? styles.active : ''}`}
                            onClick={() => setSelectedIndex(index)}
                        >
                            <Image src={getImageSrc(img.srcUrl)} alt="추가 이미지" fill style={{ objectFit: 'cover' }} />
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}
