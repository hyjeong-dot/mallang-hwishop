'use client';

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import toast from 'react-hot-toast';
import { Heart, Share2, Info, ShoppingBag, Coins, Instagram } from 'lucide-react';
import styles from './ProductDetailInfo.module.css';
import { useProductDetail } from './useProductDetail';
import { useAuth } from '@/context/AuthContext';
import { useCart } from '@/context/CartContext';
import { fetchAPI } from '@/lib/api';
import Modal from '@/components/common/Modal/Modal';
import LoadingCharacter from '@/components/common/LoadingCharacter/LoadingCharacter';

interface ProductDetailInfoProps {
    slug: string;
}

export default function ProductDetailInfo({ slug }: ProductDetailInfoProps) {
    const { product, isLoading, error } = useProductDetail(slug);
    const { user } = useAuth();
    const { addItem, setCartOpen } = useCart();
    const router = useRouter();
    const [isLoginModalOpen, setIsLoginModalOpen] = useState(false);
    const [isOrderModalOpen, setIsOrderModalOpen] = useState(false);
    const [isCartConfirmModalOpen, setIsCartConfirmModalOpen] = useState(false);
    const [isLiked, setIsLiked] = useState(false);

    // Initial check on mount or when product/user changes
    useEffect(() => {
        if (user && product) {
            fetch(`${window.location.origin}/api/favorites/${product.id}/check`)
                .then(res => res.json())
                .then(data => {
                    // API가 { isFavorite: true } 또는 직접 true/false를 줄 수 있음을 대비
                    const liked = typeof data === 'boolean' ? data : !!data.isFavorite;
                    setIsLiked(liked);
                })
                .catch(err => console.error("Error checking favorite:", err));
        }
    }, [user, product]);

    const handleShare = () => {
        if (typeof window !== 'undefined') {
            navigator.clipboard.writeText(window.location.href);
            toast.success("주소가 복사되었어요! 🎀");
        }
    };

    const handleLike = async () => {
        if (!user) {
            setIsLoginModalOpen(true);
            return;
        }

        try {
            const result = await fetchAPI(`/favorites`, {
                method: 'POST',
                body: JSON.stringify({ productId: product?.id })
            });

            // 결과값 체크 (isFavorite 필드 혹은 불리언)
            const newLikedState = typeof result === 'boolean' ? result : !!result.isFavorite;
            setIsLiked(newLikedState);

            if (newLikedState) {
                toast.success("찜 목록에 담았어요! 마이페이지에서 확인해 보세요 🍮", {
                    icon: '🎀',
                });
            } else {
                toast("찜 목록에서 제외했습니다.", {
                    icon: '🤍',
                });
            }
        } catch (err) {
            console.error("Failed to toggle interest:", err);
            toast.error("처리 중 오류가 발생했습니다.");
        }
    };

    const formatPrice = (price: number) => {
        return new Intl.NumberFormat('ko-KR').format(price);
    };

    const handleAction = (action: () => void) => {
        if (!user) {
            setIsLoginModalOpen(true);
            return;
        }
        action();
    };

    // Countdown state for UPCOMING products
    const [timeLeft, setTimeLeft] = useState<string>('');
    const [isLocallyOpen, setIsLocallyOpen] = useState(false);
    
    // 원래 데이터상 UPCOMING이면서 예약 시간이 있을 때, 로컬에서 오픈되지 않은 경우에만 UPCOMING 배지 유지
    const isUpcoming = product?.saleStatus === 'UPCOMING' && !!product?.saleStartAt && !isLocallyOpen;

    useEffect(() => {
        // 이미 프론트엔드에서 오픈 처리되었거나 데이터 자체가 UPCOMING이 아니면 타이머 중단
        if (product?.saleStatus !== 'UPCOMING' || !product?.saleStartAt || isLocallyOpen) return;

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
    }, [product?.saleStatus, product?.saleStartAt, isLocallyOpen]);

    const formatSaleStart = (dateString?: string) => {
        if (!dateString) return '';
        const d = new Date(dateString);
        const m = d.getMonth() + 1;
        const day = d.getDate();
        const h = d.getHours();
        const isPM = h >= 12;
        const h12 = h % 12 || 12;
        return `${m}/${day} ${isPM ? '오후' : '오전'} ${h12}시 오픈`;
    };

    if (isLoading) return <LoadingCharacter message="정보를 불러오는 중..." />;
    if (error || !product) return null;

    return (
        <div className={styles.info}>
            <div className={styles.categoryInfo}>
                <span className={styles.categoryBadge}>
                    {product.categoryIcon} {product.categoryName}
                </span>
            </div>

            <div className={styles.titleRow}>
                <h1 className={styles.title}>{product.korName}</h1>
                <div className={styles.actionIcons}>
                    <button
                        className={`${styles.iconBtn} ${isLiked ? styles.liked : ''}`}
                        onClick={handleLike}
                        aria-label="좋아요"
                    >
                        <Heart size={22} fill={isLiked ? "currentColor" : "none"} />
                    </button>
                    <button
                        className={styles.iconBtn}
                        onClick={handleShare}
                        aria-label="공유하기"
                    >
                        <Share2 size={22} />
                    </button>
                </div>
            </div>
            <p className={styles.engTitle}>{product.engName}</p>

            <div className={styles.priceSection}>
                {product.discountPrice && product.discountPrice > 0 ? (
                    <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                        <span style={{ textDecoration: 'line-through', color: '#999', fontSize: '1.2rem' }}>₩{formatPrice(product.price)}</span>
                        <span className={styles.price} style={{ color: '#e53e3e' }}>₩{formatPrice(product.discountPrice)}</span>
                    </div>
                ) : (
                    <span className={styles.price}>₩{formatPrice(product.price)}</span>
                )}
                {(!product.discountPrice || product.discountPrice === 0) && (
                    <span className={styles.pointEarnBadge}>
                        <Coins size={14} />
                        구매 시 {formatPrice(Math.floor(product.price * 0.03))}원 적립
                    </span>
                )}
            </div>

            {isUpcoming && (
                <div className={styles.upcomingBanner}>
                    <span className={styles.upcomingTitle}>🔔 오픈 예정 ({formatSaleStart(product.saleStartAt)})</span>
                    <span className={styles.upcomingCountdown}>{timeLeft}</span>
                </div>
            )}

            <div className={styles.descSection}>
                <h3 className={styles.sectionTitle}><Info size={16} /> 상품 설명</h3>
                <p className={styles.description}>{product.description || '준비된 설명이 없습니다.'}</p>
                <a 
                    href="https://instagram.com/" 
                    target="_blank" 
                    rel="noopener noreferrer"
                    className={styles.instagramLink}
                >
                    <Instagram size={18} />
                    <span>인스타그램에서 더 많은 사진 보기</span>
                </a>
            </div>

            <div className={styles.commonInfoSection}>
                <img src="/images/common-info-placeholder.png" alt="공통 안내사항" className={styles.commonInfoImage} onError={(e) => { e.currentTarget.style.display = 'none'; }} />
            </div>

            <div className={styles.ctaRow}>
                <button
                    className={styles.cartBtn}
                    disabled={product.isSoldOut || isUpcoming}
                    onClick={() => {
                        addItem({
                            productId: product.id,
                            korName: product.korName,
                            engName: product.engName,
                            price: product.discountPrice && product.discountPrice > 0 ? product.discountPrice : product.price,
                            image: product.imageSrc,
                            imageSrc: product.imageSrc,
                            stock: product.stock,
                            maxPerOrder: product.maxPerOrder
                        });
                        setIsCartConfirmModalOpen(true);
                    }}
                >
                    <ShoppingBag size={20} />
                    <span>장바구니</span>
                </button>
                <button
                    className={styles.mainCta}
                    disabled={product.isSoldOut || isUpcoming}
                    onClick={() => handleAction(() => {
                        setIsOrderModalOpen(true);
                    })}
                >
                    {isUpcoming ? '오픈 대기 중' : product.isSoldOut ? '현재 준비 중입니다' : '주문하기 🎀'}
                </button>
            </div>

            {/* 로그인 필요 모달 */}
            <Modal
                isOpen={isLoginModalOpen}
                onClose={() => setIsLoginModalOpen(false)}
                title="로그인이 필요해요 🎀"
                description="말랑이가 사장님을 기다리고 있어요! 로그인하고 상품를 주문하시겠어요?"
                confirmText="로그인하러 가기"
                cancelText="나중에 할게요"
                onConfirm={() => router.push('/login')}
                variant="ditto"
            />



            {/* 주문하기 모달 */}
            <Modal
                isOpen={isOrderModalOpen}
                onClose={() => setIsOrderModalOpen(false)}
                title="단일 상품 주문! ☕"
                description={`${product.korName} 상품만 바로 결제하시겠어요?\n(기존 장바구니 내용은 그대로 보존됩니다.)`}
                confirmText="주문 페이지로 이동"
                cancelText="취소"
                onConfirm={() => {
                    setIsOrderModalOpen(false);
                    // 옵션: 장바구니에 담지 않고 SessionStorage를 활용하여 단건 결제 데이터만 넘김
                    sessionStorage.setItem('directOrder', JSON.stringify([{
                        id: product.id.toString(),
                        productId: product.id,
                        korName: product.korName,
                        engName: product.engName,
                        price: product.discountPrice && product.discountPrice > 0 ? product.discountPrice : product.price,
                        quantity: 1,
                        image: product.imageSrc,
                        imageSrc: product.imageSrc
                    }]));
                    router.push('/order');
                }}
                variant="ditto"
            />

            {/* 장바구니 확인 모달 */}
            <Modal
                isOpen={isCartConfirmModalOpen}
                onClose={() => setIsCartConfirmModalOpen(false)}
                title="장바구니에 담았어요! 🎀"
                description={`${product.korName}을(를) 장바구니에 담았습니다. 바로 확인해 보시겠어요?`}
                confirmText="장바구니 가기"
                cancelText="계속 쇼핑하기"
                onConfirm={() => {
                    setIsCartConfirmModalOpen(false);
                    setCartOpen(true);
                }}
                variant="ditto"
            />
        </div>
    );
}
