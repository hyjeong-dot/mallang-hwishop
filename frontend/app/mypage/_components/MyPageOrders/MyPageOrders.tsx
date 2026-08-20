"use client";

import { useEffect, useState } from "react";
import Link from "next/link";
import Image from "next/image";
import styles from "./MyPageOrders.module.css";
import { fetchAPI } from "@/lib/api";
import LoadingCharacter from "@/components/common/LoadingCharacter/LoadingCharacter";
import OrderCancelModal from "@/components/common/OrderCancelModal/OrderCancelModal";
import toast from 'react-hot-toast';

interface OrderLineItem {
    productId: number;
    productName: string;
    price: number;
    quantity: number;
}

interface OrderResult {
    orderId: number;
    orderUid: string;
    totalPrice: number;
    recipientName?: string;
    phoneNumber?: string;
    zipcode?: string;
    address?: string;
    detailAddress?: string;
    pointUsed?: number;
    pointEarned?: number;
    deliveryFee?: number;
    status: string;
    createdAt: string;
    items?: OrderLineItem[];
    requestMemo?: string;
    cancelReason?: string;
    cancelReasonType?: string;
    trackingCarrier?: string;
    trackingNumber?: string;
}

const TOSS_CLIENT_KEY = process.env.NEXT_PUBLIC_TOSS_CLIENT_KEY || '';

const getStatusBadge = (status: string) => {
    switch (status) {
        case 'PENDING':
            return <span style={{ color: 'var(--color-primary)' }}>결제 대기</span>;
        case 'PAID':
            return <span style={{ color: 'var(--color-secondary)' }}>결제 완료</span>;
        case 'PREPARING':
            return <span style={{ color: 'var(--color-warning)' }}>배송 준비 중</span>;
        case 'COMPLETED':
            return <span style={{ color: 'var(--color-success)' }}>배송 완료</span>;
        case 'CANCELLED':
            return <span style={{ color: 'var(--color-error)' }}>주문 취소</span>;
        default:
            return <span>{status}</span>;
    }
};

const ORDER_STATUS_STEPS = [
    { key: 'PENDING', label: '결제대기' },
    { key: 'PAID', label: '결제완료' },
    { key: 'PREPARING', label: '배송준비' },
    { key: 'COMPLETED', label: '배송완료' }
];

export default function MyPageOrders() {
    const [orders, setOrders] = useState<OrderResult[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [isCancelModalOpen, setIsCancelModalOpen] = useState(false);
    const [orderToCancel, setOrderToCancel] = useState<number | null>(null);

    // 리뷰 작성
    const [isReviewModalOpen, setIsReviewModalOpen] = useState(false);
    const [reviewOrderId, setReviewOrderId] = useState<number | null>(null);
    const [reviewContent, setReviewContent] = useState('');
    const [reviewRating, setReviewRating] = useState(5);
    const [reviewedOrders, setReviewedOrders] = useState<Set<number>>(new Set());

    useEffect(() => {
        const fetchData = async () => {
            try {
                const [ordersData, reviewsData] = await Promise.all([
                    fetchAPI('/orders'),
                    fetchAPI('/reviews')
                ]);
                setOrders(ordersData || []);

                // 이미 리뷰 작성된 주문 ID를 세팅
                if (reviewsData && Array.isArray(reviewsData)) {
                    const reviewedIds = new Set<number>(reviewsData.map((r: any) => r.orderId));
                    setReviewedOrders(reviewedIds);
                }
            } catch (error) {
                console.error("Failed to load data", error);
            } finally {
                setIsLoading(false);
            }
        };

        fetchData();
    }, []);

    const handleCancelOrderClick = (orderId: number) => {
        setOrderToCancel(orderId);
        setIsCancelModalOpen(true);
    };

    const handleConfirmCancel = async (reasonType: string, reasonText: string) => {
        if (orderToCancel === null) return;
        
        try {
            await fetchAPI(`/orders/${orderToCancel}/cancel`, {
                method: 'PATCH',
                body: JSON.stringify({
                    cancelReasonType: reasonType,
                    cancelReason: reasonType === 'OTHER' ? reasonText : ''
                })
            });
            toast.success('주문이 취소되었습니다.');
            setOrders(orders.map(o => 
                o.orderId === orderToCancel ? { ...o, status: 'CANCELLED' } : o
            ));
        } catch (error: any) {
            toast.error(error.message || '주문 취소 중 오류가 발생했습니다.');
        } finally {
            setIsCancelModalOpen(false);
            setOrderToCancel(null);
        }
    };

    const handlePayOrder = async (order: OrderResult) => {
        try {
            const script = document.createElement('script');
            script.src = 'https://js.tosspayments.com/v1/payment';
            
            await new Promise<void>((resolve, reject) => {
                if ((window as any).TossPayments) { resolve(); return; }
                script.onload = () => resolve();
                script.onerror = () => reject();
                document.head.appendChild(script);
            });

            const TossPayments = (window as any).TossPayments;
            const tossPayments = TossPayments(TOSS_CLIENT_KEY);

            tossPayments.requestPayment('카드', {
                amount: order.totalPrice,
                orderId: order.orderUid,
                orderName: `주문 #${order.orderId}`,
                successUrl: `${window.location.origin}/order/success`,
                failUrl: `${window.location.origin}/order/fail`,
            });
        } catch (error) {
            toast.error('결제 모듈 로드에 실패했습니다.');
        }
    };

    if (isLoading) {
        return (
            <div className={styles.content}>
                 <LoadingCharacter message="주문 내역을 불러오는 중..." size={150} />
            </div>
        );
    }

    return (
        <div className={styles.content}>
            <h3 className={styles.sectionTitle}>주문 내역</h3>
            
            {orders.length === 0 ? (
                <div className={styles.emptyState}>
                    아직 주문하신 내역이 없어요. <br />
                    <Link href="/" className={styles.productLink}> 상품 보러 가기 </Link>
                </div>
            ) : (
                <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem', marginTop: '1rem' }}>
                    {orders.map(order => (
                        <div key={order.orderId} style={{ 
                            padding: '1.5rem', 
                            border: '2px solid var(--border-color)', 
                            borderRadius: '12px', 
                            display: 'flex', 
                            justifyContent: 'space-between',
                            alignItems: 'center',
                            flexWrap: 'wrap',
                            gap: '1rem'
                        }}>
                            <div style={{ flex: 1 }}>
                                <div style={{ fontSize: '0.9rem', color: 'var(--color-text-light)', marginBottom: '0.5rem' }}>
                                    {new Date(order.createdAt).toLocaleDateString()} {new Date(order.createdAt).toLocaleTimeString()}
                                    &nbsp;|&nbsp;
                                    {order.recipientName} ({order.address} {order.detailAddress})
                                </div>
                                {/* 상품 항목 목록 */}
                                {order.items && order.items.length > 0 && (
                                    <div style={{ margin: '0.5rem 0', fontSize: '0.95rem' }}>
                                        {order.items.map((item, idx) => (
                                            <div key={idx} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '2px 0' }}>
                                                <span>{item.productName} × {item.quantity}</span>
                                                <span style={{ color: 'var(--color-text-light)', fontSize: '0.85rem', marginLeft: '1rem' }}>
                                                    {(item.price * item.quantity).toLocaleString()}원
                                                </span>
                                            </div>
                                        ))}
                                    </div>
                                )}
                                {(order.deliveryFee ?? 0) > 0 && (
                                    <div style={{ fontSize: '0.9rem', color: 'var(--color-text-light)' }}>
                                        + 배송비 {(order.deliveryFee ?? 0).toLocaleString()}원
                                    </div>
                                )}
                                {(order.pointUsed ?? 0) > 0 && (
                                    <div style={{ fontSize: '0.9rem', color: 'var(--color-primary-600)' }}>
                                        - 포인트 사용 {(order.pointUsed ?? 0).toLocaleString()}원
                                    </div>
                                )}
                                <div style={{ fontSize: '1.2rem', fontWeight: 'bold' }}>
                                    총 결제금액 {order.totalPrice.toLocaleString()}원
                                </div>
                                {(order.trackingCarrier || order.trackingNumber) && (
                                    <div style={{ marginTop: '1rem', fontSize: '0.9rem', padding: '0.8rem', background: 'var(--bg-primary)', borderRadius: '8px', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                                        📦 <span style={{ fontWeight: 600 }}>{order.trackingCarrier}</span> {order.trackingNumber}
                                        {order.trackingNumber && (
                                            <a 
                                                href={`https://tracker.delivery/#/${order.trackingCarrier === 'CJ대한통운' ? 'kr.cjlogistics' : order.trackingCarrier === '우체국택배' ? 'kr.epost' : 'kr.logen'}/${order.trackingNumber}`}
                                                target="_blank"
                                                rel="noopener noreferrer"
                                                style={{ marginLeft: 'auto', fontSize: '0.8rem', padding: '0.3rem 0.8rem', background: '#fff', border: '1px solid var(--color-primary-100)', borderRadius: '99px', color: 'var(--color-primary-600)', textDecoration: 'none', fontWeight: 600 }}
                                            >
                                                배송조회 〉
                                            </a>
                                        )}
                                    </div>
                                )}
                                {order.status === 'CANCELLED' && order.cancelReasonType && (
                                    <div style={{ marginTop: '0.5rem', fontSize: '0.9rem', color: 'var(--color-error)' }}>
                                        <span style={{ fontWeight: 600 }}>취소 사유:</span> {
                                            order.cancelReasonType === 'OTHER' && order.cancelReason ? order.cancelReason : 
                                            order.cancelReasonType === 'CHANGE_MIND' ? '단순변심' :
                                            order.cancelReasonType === 'REORDER_AFTER_ADD' ? '상품 추가 후 재주문' :
                                            order.cancelReasonType === 'BEFORE_OPEN' ? '오픈시간 전 결제 건' :
                                            order.cancelReasonType === 'EXCEED_COMBINED' ? '합배송 2건 초과 건' :
                                            order.cancelReasonType === 'OUT_OF_STOCK' ? '재고부족' :
                                            order.cancelReasonType === 'OTHER' ? '기타' :
                                            order.cancelReasonType
                                        }
                                    </div>
                                )}
                            </div>
                            <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem', alignItems: 'flex-end', minWidth: '300px' }}>
                                
                                {order.status !== 'CANCELLED' && (
                                    <div style={{ display: 'flex', width: '100%', justifyContent: 'space-between', position: 'relative', marginTop: '0.5rem' }}>
                                        {/* Background Line */}
                                        <div style={{ position: 'absolute', top: '12px', left: '10%', right: '10%', height: '2px', background: 'var(--border-color)', zIndex: 0 }} />
                                        
                                        {ORDER_STATUS_STEPS.map((step, idx) => {
                                            const currentIndex = ORDER_STATUS_STEPS.findIndex(s => s.key === order.status);
                                            const isActive = idx <= currentIndex;
                                            const isCurrent = idx === currentIndex;
                                            return (
                                                <div key={step.key} style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', zIndex: 1, gap: '6px' }}>
                                                    <div style={{ 
                                                        width: '26px', height: '26px', borderRadius: '50%', 
                                                        background: isActive ? 'var(--color-primary-600)' : '#fff',
                                                        border: `2px solid ${isActive ? 'var(--color-primary-600)' : 'var(--border-color)'}`,
                                                        display: 'flex', alignItems: 'center', justifyContent: 'center',
                                                        color: isActive ? '#fff' : 'transparent',
                                                        fontSize: '12px', fontWeight: 'bold',
                                                        boxShadow: isCurrent ? '0 0 0 4px var(--color-primary-100)' : 'none'
                                                    }}>
                                                        {isActive ? '✓' : ''}
                                                    </div>
                                                    <span style={{ fontSize: '0.8rem', color: isActive ? 'var(--color-primary-700)' : 'var(--text-muted)', fontWeight: isActive ? 700 : 400, whiteSpace: 'nowrap' }}>
                                                        {step.label}
                                                    </span>
                                                </div>
                                            );
                                        })}
                                    </div>
                                )}

                                <div style={{ fontWeight: 600, fontSize: '1.2rem', marginTop: '0.5rem' }}>
                                    {getStatusBadge(order.status)}
                                </div>
                                {order.status === 'PENDING' && (
                                    <button 
                                        onClick={() => handlePayOrder(order)}
                                        style={{
                                            padding: '4px 12px',
                                            background: 'var(--ditto-600)',
                                            border: 'none',
                                            color: '#fff',
                                            fontSize: '0.85rem',
                                            borderRadius: '6px',
                                            cursor: 'pointer',
                                            fontWeight: 600
                                        }}
                                    >
                                        결제하기
                                    </button>
                                )}
                                {(order.status === 'PENDING' || order.status === 'PAID') && (
                                    <button 
                                        onClick={() => handleCancelOrderClick(order.orderId)}
                                        style={{
                                            padding: 0,
                                            background: 'none',
                                            border: 'none',
                                            color: 'var(--color-text-light)',
                                            fontSize: '0.85rem',
                                            textDecoration: 'underline',
                                            cursor: 'pointer'
                                        }}
                                    >
                                        주문 취소
                                    </button>
                                )}
                                {(order.status === 'PAID' || order.status === 'COMPLETED') && !reviewedOrders.has(order.orderId) && (
                                    <button
                                        onClick={() => {
                                            setReviewOrderId(order.orderId);
                                            setReviewContent('');
                                            setReviewRating(5);
                                            setIsReviewModalOpen(true);
                                        }}
                                        style={{
                                            padding: '4px 12px',
                                            background: 'linear-gradient(135deg, #ff8fa4, #ed4b68)',
                                            border: 'none',
                                            color: '#fff',
                                            fontSize: '0.85rem',
                                            borderRadius: '6px',
                                            cursor: 'pointer',
                                            fontWeight: 600
                                        }}
                                    >
                                        ✍️ 리뷰 쓰기
                                    </button>
                                )}
                                {reviewedOrders.has(order.orderId) && (
                                    <span style={{ fontSize: '0.8rem', color: 'var(--ditto-600)' }}>✅ 리뷰 완료</span>
                                )}
                            </div>
                        </div>
                    ))}
                </div>
            )}

            <OrderCancelModal
                isOpen={isCancelModalOpen}
                onClose={() => {
                    setIsCancelModalOpen(false);
                    setOrderToCancel(null);
                }}
                onConfirm={handleConfirmCancel}
                variant="ditto"
                options={[
                    { value: 'CHANGE_MIND', label: '단순변심' },
                    { value: 'REORDER_AFTER_ADD', label: '상품 추가 후 재주문' },
                    { value: 'OTHER', label: '기타' },
                ]}
                allowCustomReason={false}
            />

            {/* 리뷰 작성 모달 */}
            {isReviewModalOpen && (
                <div style={{
                    position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.5)', zIndex: 100,
                    display: 'flex', alignItems: 'center', justifyContent: 'center', padding: '1rem'
                }} onClick={() => setIsReviewModalOpen(false)}>
                    <div style={{
                        background: '#fff', borderRadius: '16px', padding: '2rem', maxWidth: '420px', width: '100%'
                    }} onClick={e => e.stopPropagation()}>
                        <h3 style={{ fontSize: '1.2rem', fontWeight: 700, marginBottom: '1rem' }}>✍️ 리뷰 작성</h3>
                        
                        {/* 별점 */}
                        <div style={{ marginBottom: '1rem' }}>
                            <label style={{ fontSize: '0.9rem', fontWeight: 600, marginBottom: '0.5rem', display: 'block' }}>별점</label>
                            <div style={{ display: 'flex', gap: '4px' }}>
                                {[1, 2, 3, 4, 5].map(n => (
                                    <button key={n} onClick={() => setReviewRating(n)} style={{
                                        background: 'none', border: 'none', cursor: 'pointer', fontSize: '1.5rem',
                                        color: n <= reviewRating ? '#f59e0b' : '#d1d5db'
                                    }}>★</button>
                                ))}
                            </div>
                        </div>

                        {/* 리뷰 내용 */}
                        <div style={{ marginBottom: '1.5rem' }}>
                            <label style={{ fontSize: '0.9rem', fontWeight: 600, marginBottom: '0.5rem', display: 'block' }}>리뷰 내용</label>
                            <textarea
                                value={reviewContent}
                                onChange={e => setReviewContent(e.target.value)}
                                placeholder="말랑이샵에서의 경험을 남겨주세요! 🎀"
                                maxLength={500}
                                style={{
                                    width: '100%', minHeight: '120px', border: '1px solid #d1d5db', borderRadius: '8px',
                                    padding: '0.75rem', fontSize: '0.95rem', resize: 'vertical', fontFamily: 'inherit'
                                }}
                            />
                            <div style={{ textAlign: 'right', fontSize: '0.8rem', color: '#9ca3af', marginTop: '4px' }}>
                                {reviewContent.length}/500
                            </div>
                        </div>

                        <div style={{ display: 'flex', gap: '0.5rem' }}>
                            <button onClick={() => { setIsReviewModalOpen(false); }}
                                style={{ flex: 1, padding: '0.75rem', borderRadius: '8px', border: '1px solid #d1d5db', background: '#fff', cursor: 'pointer' }}>
                                닫기
                            </button>
                            <button onClick={async () => {
                                if (!reviewContent.trim()) { toast.error('리뷰 내용을 입력해주세요.'); return; }
                                try {
                                    await fetchAPI('/reviews', {
                                        method: 'POST',
                                        body: JSON.stringify({ orderId: reviewOrderId, content: reviewContent, rating: reviewRating })
                                    });
                                    toast.success('리뷰가 등록되었습니다!');
                                    setReviewedOrders(prev => new Set(prev).add(reviewOrderId!));
                                    setIsReviewModalOpen(false);
                                } catch (e: any) {
                                    toast.error(e.message || '리뷰 등록에 실패했습니다.');
                                }
                            }} style={{
                                flex: 1, padding: '0.75rem', borderRadius: '8px', border: 'none',
                                background: 'linear-gradient(135deg, var(--color-primary-400), var(--color-primary-600))', color: '#fff',
                                fontWeight: 700, cursor: 'pointer'
                            }}>
                                등록하기
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}
