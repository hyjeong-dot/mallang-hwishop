'use client';

import { useState } from 'react';
import { ChevronDown, ChevronUp } from 'lucide-react';
import { AdminOrder } from './useAdminOrders';
import OrderCancelModal from '@/components/common/OrderCancelModal/OrderCancelModal';
import styles from '../page.module.css';

interface OrderCardProps {
    order: AdminOrder;
    onUpdateStatus: (orderId: number, status: string, cancelReasonType?: string, cancelReason?: string) => void;
    onUpdateTrackingInfo: (orderId: number, carrier: string, trackingNumber: string) => void;
    isSelected: boolean;
    onToggleSelect: () => void;
    onRefundShippingGroup: (shippingGroupId: number) => void;
}

export default function OrderCard({ 
    order, 
    onUpdateStatus, 
    onUpdateTrackingInfo,
    isSelected,
    onToggleSelect,
    onRefundShippingGroup
}: OrderCardProps) {
    const [isCancelOpen, setIsCancelOpen] = useState(false);
    const [isExpanded, setIsExpanded] = useState(false);
    const [trackingCarrier, setTrackingCarrier] = useState(order.trackingCarrier || '');
    const [trackingNumber, setTrackingNumber] = useState(order.trackingNumber || '');
    const [isEditingTracking, setIsEditingTracking] = useState(false);

    const formatDate = (dateStr: string) => {
        const d = new Date(dateStr);
        return d.toLocaleDateString('ko-KR', {
            year: 'numeric', month: '2-digit', day: '2-digit',
        }) + ' ' + d.toLocaleTimeString('ko-KR', {
            hour: '2-digit', minute: '2-digit', second: '2-digit'
        });
    };

    return (
        <>
            <div className={styles.orderCard}>
                {/* 헤더 행: 주문번호 | 날짜 | 타입 | 상태뱃지 | 접기 */}
                <div className={styles.cardHeader} onClick={() => setIsExpanded(!isExpanded)}>
                    <div className={styles.headerLeft} style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
                        <input 
                            type="checkbox" 
                            checked={isSelected}
                            onChange={(e) => {
                                e.stopPropagation();
                                onToggleSelect();
                            }}
                            onClick={(e) => e.stopPropagation()}
                            style={{ width: '18px', height: '18px', cursor: 'pointer' }}
                        />
                        <span className={styles.orderId}>#{order.orderUid || order.id}</span>
                        <span className={styles.headerMeta}>
                            📅 {formatDate(order.createdAt)}
                        </span>
                        <span className={styles.headerMeta}>
                            배송
                        </span>
                    </div>
                    <div className={styles.headerRight} style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                        {order.shippingGroupId && (
                            <div style={{ fontSize: '12px', padding: '2px 6px', background: '#e0e7ff', color: '#4338ca', borderRadius: '4px', fontWeight: 600 }}>
                                합배송 #{order.shippingGroupId}
                            </div>
                        )}
                        {order.shippingGroupId && order.shippingGroupIsRefunded === false && (
                            <button
                                onClick={(e) => { e.stopPropagation(); onRefundShippingGroup(order.shippingGroupId!); }}
                                style={{ fontSize: '11px', padding: '2px 6px', background: '#fee2e2', color: '#dc2626', border: '1px solid #fecaca', borderRadius: '4px', cursor: 'pointer' }}
                            >
                                환불 필요
                            </button>
                        )}
                        {order.shippingGroupId && order.shippingGroupIsRefunded === true && (
                            <div style={{ fontSize: '11px', padding: '2px 6px', background: '#dcfce7', color: '#166534', borderRadius: '4px' }}>
                                환불 완료
                            </div>
                        )}
                        <div className={`${styles.statusBadge} ${styles[`badge-${order.status.toLowerCase()}`]}`}>
                            {order.statusLabel}
                        </div>
                        <span className={styles.expandIcon}>
                            {isExpanded ? <ChevronUp size={18} /> : <ChevronDown size={18} />}
                        </span>
                    </div>
                </div>

                {/* 요약 (접힌 상태에서도 보이는 정보) */}
                {!isExpanded && (
                    <div className={styles.collapsedSummary}>
                        <div className={styles.summaryInfo}>
                            <span className={styles.summaryLabel}>최종 결제 금액</span>
                            <span className={styles.summaryPrice}>₩{order.totalPrice.toLocaleString()}</span>
                        </div>
                        <div className={styles.actions}>
                            {order.status === 'PAID' && (
                                <button
                                    className={`${styles.btn} ${styles.btnPrepare}`}
                                    onClick={(e) => { e.stopPropagation(); onUpdateStatus(order.id, 'PREPARING'); }}
                                >
                                    🪄 상품 준비 시작
                                </button>
                            )}
                            {order.status === 'PREPARING' && (
                                <button
                                    className={`${styles.btn} ${styles.btnComplete}`}
                                    onClick={(e) => { e.stopPropagation(); onUpdateStatus(order.id, 'COMPLETED'); }}
                                >
                                    ✅ 제공 완료
                                </button>
                            )}
                            {['PAID', 'PENDING', 'PREPARING'].includes(order.status) && (
                                <button
                                    className={`${styles.btn} ${styles.btnCancel}`}
                                    onClick={(e) => { e.stopPropagation(); setIsCancelOpen(true); }}
                                >
                                    ⊘ 주문 취소
                                </button>
                            )}
                        </div>
                    </div>
                )}

                {/* 펼친 상태 */}
                {isExpanded && (
                    <div className={styles.expandedBody}>
                        <div className={styles.cardBody}>
                            <div className={styles.itemsInfo}>
                                <div className={styles.sectionLabel}>주문 상품 ({order.items.length})</div>
                                {order.items.map((item, idx) => (
                                    <div key={idx} className={styles.itemLine}>
                                        <span className={styles.itemName}>{item.productName}</span>
                                        <span className={styles.itemDetail}>
                                            {item.price.toLocaleString()}원 × {item.quantity}개
                                        </span>
                                    </div>
                                ))}
                            </div>

                            <div className={styles.customerInfo}>
                                <div className={styles.sectionLabel}>고객 정보</div>
                                <div className={styles.infoGroup}>
                                    <span className={styles.infoLabel}>👤 주문자</span>
                                    <div className={styles.infoValue}>{order.name}</div>
                                </div>
                                {order.requestMemo && (
                                    <div className={styles.infoGroup}>
                                        <span className={styles.infoLabel}>📝 메모</span>
                                        <div className={styles.infoValue}>{order.requestMemo}</div>
                                    </div>
                                )}
                                {(order.trackingCarrier || order.trackingNumber) && !isEditingTracking && (
                                    <div className={styles.infoGroup}>
                                        <span className={styles.infoLabel}>📦 송장 번호</span>
                                        <div className={styles.infoValue} style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                                            {order.trackingCarrier} {order.trackingNumber}
                                            <button 
                                                onClick={(e) => { e.stopPropagation(); setIsEditingTracking(true); }}
                                                style={{ fontSize: '12px', padding: '2px 8px', borderRadius: '4px', border: '1px solid #d1d5db', background: '#fff', cursor: 'pointer' }}
                                            >
                                                수정
                                            </button>
                                        </div>
                                    </div>
                                )}
                                {(!order.trackingCarrier && !order.trackingNumber && !isEditingTracking) && (
                                    <div className={styles.infoGroup}>
                                        <span className={styles.infoLabel}>📦 송장 번호</span>
                                        <div className={styles.infoValue}>
                                            <button 
                                                onClick={(e) => { e.stopPropagation(); setIsEditingTracking(true); }}
                                                style={{ fontSize: '12px', padding: '2px 8px', borderRadius: '4px', border: '1px solid #d1d5db', background: '#fff', cursor: 'pointer' }}
                                            >
                                                수기 입력
                                            </button>
                                        </div>
                                    </div>
                                )}
                                {isEditingTracking && (
                                    <div className={styles.infoGroup} onClick={(e) => e.stopPropagation()}>
                                        <span className={styles.infoLabel}>📦 송장 번호</span>
                                        <div className={styles.infoValue} style={{ display: 'flex', gap: '8px', alignItems: 'center' }}>
                                            <input 
                                                type="text" 
                                                placeholder="택배사" 
                                                value={trackingCarrier}
                                                onChange={(e) => setTrackingCarrier(e.target.value)}
                                                style={{ width: '80px', padding: '4px 8px', border: '1px solid #d1d5db', borderRadius: '4px' }}
                                            />
                                            <input 
                                                type="text" 
                                                placeholder="송장번호" 
                                                value={trackingNumber}
                                                onChange={(e) => setTrackingNumber(e.target.value)}
                                                style={{ flex: 1, padding: '4px 8px', border: '1px solid #d1d5db', borderRadius: '4px' }}
                                            />
                                            <button 
                                                onClick={() => {
                                                    onUpdateTrackingInfo(order.id, trackingCarrier, trackingNumber);
                                                    setIsEditingTracking(false);
                                                }}
                                                style={{ fontSize: '12px', padding: '4px 8px', borderRadius: '4px', border: 'none', background: 'var(--primary-color)', color: '#fff', cursor: 'pointer' }}
                                            >
                                                저장
                                            </button>
                                            <button 
                                                onClick={() => {
                                                    setTrackingCarrier(order.trackingCarrier || '');
                                                    setTrackingNumber(order.trackingNumber || '');
                                                    setIsEditingTracking(false);
                                                }}
                                                style={{ fontSize: '12px', padding: '4px 8px', borderRadius: '4px', border: '1px solid #d1d5db', background: '#fff', cursor: 'pointer' }}
                                            >
                                                취소
                                            </button>
                                        </div>
                                        {order.shippingGroupId && (
                                            <div style={{ fontSize: '11px', color: '#6b7280', marginTop: '4px' }}>
                                                * 동일 합배송 그룹 전체에 일괄 적용됩니다.
                                            </div>
                                        )}
                                    </div>
                                )}
                                {order.status === 'CANCELLED' && order.cancelReasonType && (
                                    <div className={styles.infoGroup}>
                                        <span className={styles.infoLabel} style={{ color: '#dc2626' }}>⊘ 취소 사유</span>
                                        <div className={styles.infoValue} style={{ color: '#dc2626' }}>
                                            {
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
                                    </div>
                                )}
                                <div className={styles.infoGroup}>
                                    <span className={styles.infoLabel}>📞 연락처</span>
                                    <div className={styles.infoValue}>{order.phoneNumber}</div>
                                </div>
                                <div className={styles.infoGroup}>
                                    <span className={styles.infoLabel}>📍 배송지</span>
                                    <div className={styles.infoValue}>
                                        {order.recipientName}<br />
                                        {order.address} {order.detailAddress} ({order.zipcode})
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div className={styles.cardFooter}>
                            <div className={styles.totalSection}>
                                <div style={{ display: 'flex', flexDirection: 'column', gap: '4px', textAlign: 'right', width: '100%' }}>
                                    <div style={{ fontSize: '13px', color: 'var(--text-secondary)' }}>
                                        상품 금액: ₩{(order.totalPrice - (order.deliveryFee || 0)).toLocaleString()}
                                    </div>
                                    {(order.deliveryFee || 0) > 0 && (
                                        <div style={{ fontSize: '13px', color: 'var(--text-secondary)' }}>
                                            배송비: +₩{(order.deliveryFee || 0).toLocaleString()}
                                        </div>
                                    )}
                                    {(order.pointUsed || 0) > 0 && (
                                        <div style={{ fontSize: '13px', color: 'var(--color-primary-600)' }}>
                                            포인트 사용: -₩{(order.pointUsed || 0).toLocaleString()}
                                        </div>
                                    )}
                                    <div style={{ marginTop: '4px', display: 'flex', justifyContent: 'flex-end', alignItems: 'center', gap: '12px' }}>
                                        <span className={styles.totalLabel}>최종 결제 금액</span>
                                        <span className={styles.totalPrice}>₩{order.totalPrice.toLocaleString()}</span>
                                    </div>
                                </div>
                            </div>

                            <div className={styles.actions}>
                                {order.status === 'PAID' && (
                                    <button
                                        className={`${styles.btn} ${styles.btnPrepare}`}
                                        onClick={() => onUpdateStatus(order.id, 'PREPARING')}
                                    >
                                        🪄 상품 준비 시작
                                    </button>
                                )}
                                {order.status === 'PREPARING' && (
                                    <button
                                        className={`${styles.btn} ${styles.btnComplete}`}
                                        onClick={() => onUpdateStatus(order.id, 'COMPLETED')}
                                    >
                                        ✅ 제공 완료
                                    </button>
                                )}
                                {['PAID', 'PENDING', 'PREPARING'].includes(order.status) && (
                                    <button
                                        className={`${styles.btn} ${styles.btnCancel}`}
                                        onClick={() => setIsCancelOpen(true)}
                                    >
                                        ⊘ 주문 취소
                                    </button>
                                )}
                            </div>
                        </div>
                    </div>
                )}
            </div>

            <OrderCancelModal
                isOpen={isCancelOpen}
                onClose={() => setIsCancelOpen(false)}
                title="주문 취소"
                description="정말 이 주문을 취소하시겠어요? 취소된 주문은 되돌릴 수 없어요. 😢"
                variant="danger"
                options={[
                    { value: 'BEFORE_OPEN', label: '오픈시간 전 결제 건' },
                    { value: 'EXCEED_COMBINED', label: '합배송 2건 초과 건' },
                    { value: 'OUT_OF_STOCK', label: '재고부족' },
                    { value: 'OTHER', label: '기타 (작성란)' },
                ]}
                allowCustomReason={true}
                onConfirm={(reasonType, reasonText) => {
                    onUpdateStatus(order.id, 'CANCELLED', reasonType, reasonText);
                }}
            />
        </>
    );
}
