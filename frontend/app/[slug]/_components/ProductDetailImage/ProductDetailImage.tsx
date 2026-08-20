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
    const [isHovering, setIsHovering] = useState(false);
    const [mousePos, setMousePos] = useState({ x: 0, y: 0 });
    const [bgPos, setBgPos] = useState({ x: 0, y: 0 });

    const handleMouseMove = (e: React.MouseEvent<HTMLDivElement>) => {
        const { left, top, width, height } = e.currentTarget.getBoundingClientRect();
        
        // Mouse position relative to the container
        const x = e.clientX - left;
        const y = e.clientY - top;
        setMousePos({ x, y });

        // Calculate background position for the magnifier
        // For a zoom level of 2x, background size will be 200% 200%
        const bgX = (x / width) * 100;
        const bgY = (y / height) * 100;
        setBgPos({ x: bgX, y: bgY });
    };

    const handleMouseEnter = () => setIsHovering(true);
    const handleMouseLeave = () => setIsHovering(false);

    // 데이터 로딩 중일 때 표시
    if (isDataLoading) return <LoadingCharacter message="이미지를 불러오는 중..." />;

    const rawMainImage = images.length > 0 ? images[selectedIndex].srcUrl : product?.imageSrc;
    const mainImageSrc = getImageSrc(rawMainImage);

    return (
        <div className={styles.gallery}>
            {!isImageReady && <LoadingCharacter message="이미지를 준비하고 있어요... 🎀" />}
            <div 
                className={`${styles.mainImageWrapper} ${!isImageReady ? styles.hidden : styles.fadeIn}`}
                onMouseMove={handleMouseMove}
                onMouseEnter={handleMouseEnter}
                onMouseLeave={handleMouseLeave}
            >
                <Image
                    src={mainImageSrc}
                    alt={product?.korName || '상품 이미지'}
                    fill
                    className={styles.mainImage}
                    priority
                    onLoad={() => setIsImageReady(true)}
                    onError={() => setIsImageReady(true)}
                />
                {isHovering && !product?.isSoldOut && (
                    <div 
                        className={styles.magnifier}
                        style={{
                            left: `${mousePos.x}px`,
                            top: `${mousePos.y}px`,
                            backgroundImage: `url(${mainImageSrc})`,
                            backgroundPosition: `${bgPos.x}% ${bgPos.y}%`
                        }}
                    />
                )}
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
