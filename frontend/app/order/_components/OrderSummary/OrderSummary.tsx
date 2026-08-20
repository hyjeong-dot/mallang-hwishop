"use client";

import { CartItem } from '@/context/CartContext';
import styles from './OrderSummary.module.css';

interface OrderSummaryProps {
    items: CartItem[];
    totalPrice: number;
    finalPrice: number;
    isSubmitting: boolean;
    onSubmit: () => void;
    pointUsed: number;
    onPointChange: (val: number) => void;
    currentPoint: number;
    expectedEarnPoint: number;
    deliveryFee: number;
}

export default function OrderSummary({
    items,
    totalPrice,
    finalPrice,
    isSubmitting,
    onSubmit,
    pointUsed,
    onPointChange,
    currentPoint,
    expectedEarnPoint,
    deliveryFee
}: OrderSummaryProps) {
    return (
        <div className={styles.summarySection}>
            <h3>주문 상세 정보</h3>
            
            <div className={styles.itemList}>
                {items.map(item => (
                    <div key={item.id} className={styles.item}>
                        <div className={styles.itemInfo}>
                            <div className={styles.itemName}>
                               {item.korName}
                            </div>
                            {item.selectedOptionNames && item.selectedOptionNames.length > 0 && (
                                <div className={styles.itemOptions}>
                                    {item.selectedOptionNames.join(' · ')}
                                </div>
                            )}
                        </div>
                        <div className={styles.itemQty}>{item.quantity}개</div>
                        <div className={styles.itemPrice}>{(item.price * item.quantity).toLocaleString()}원</div>
                    </div>
                ))}
            </div>

            {/* 적립금 사용 */}
            <div className={styles.couponSection}>
                <label className={styles.couponLabel}>🎁 적립금 사용</label>
                <div className={styles.pointInputWrapper}>
                    <input 
                        type="number"
                        className={styles.pointInput}
                        value={pointUsed || ''}
                        onChange={(e) => onPointChange(parseInt(e.target.value) || 0)}
                        placeholder="0"
                    />
                    <span className={styles.pointUnit}>원</span>
                    <button 
                        className={styles.pointAllBtn}
                        onClick={() => onPointChange(currentPoint)}
                    >
                        전액 사용
                    </button>
                </div>
                <div className={styles.pointInfo}>
                    보유 적립금: {currentPoint.toLocaleString()}원
                </div>
            </div>

            <div className={styles.totalRow}>
                <span className={styles.totalLabel}>상품 금액</span>
                <span className={styles.originalPrice}>{totalPrice.toLocaleString()}원</span>
            </div>

            <div className={styles.totalRow}>
                <span className={styles.totalLabel}>배송비</span>
                <span className={styles.originalPrice}>+{deliveryFee.toLocaleString()}원</span>
            </div>

            {pointUsed > 0 && (
                <div className={styles.discountRow}>
                    <span className={styles.discountLabel}>적립금 사용</span>
                    <span className={styles.discountPrice}>-{pointUsed.toLocaleString()}원</span>
                </div>
            )}

            <div className={styles.finalRow}>
                <span className={styles.totalLabel}>총 결제 금액</span>
                <span className={styles.totalPrice}>{finalPrice.toLocaleString()}원</span>
            </div>

            <div className={styles.expectedPointRow}>
                <span className={styles.expectedPointLabel}>적립 예정 금액</span>
                <span className={styles.expectedPointValue}>+{expectedEarnPoint.toLocaleString()}원</span>
            </div>

            <button 
                className={styles.submitBtn} 
                onClick={onSubmit}
                disabled={isSubmitting || items.length === 0}
            >
                {isSubmitting ? '결제 처리 중...' : `${finalPrice.toLocaleString()}원 결제하기`}
            </button>
        </div>
    );
}
