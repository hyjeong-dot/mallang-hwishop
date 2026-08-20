"use client";

import React from 'react';
import Modal from '@/components/common/Modal/Modal';
import styles from './page.module.css';
import { OrderHeader, OrderForm, OrderSummary, useOrder } from './_components';

export default function OrderPage() {
    const {
        isLoading,
        items,
        totalPrice,
        finalPrice,
        shippingInfo,
        setShippingInfo,
        requestMemo,
        setRequestMemo,
        isSubmitting,
        isSuccessModalOpen,
        handleSubmitOrder,
        handleSuccessConfirm,
        pointUsed,
        handlePointChange,
        currentPoint,
        expectedEarnPoint,
        deliveryFee
    } = useOrder();

    if (isLoading) {
        return <div className={styles.container}>로딩 중...</div>;
    }

    return (
        <div className={styles.container}>
            <OrderHeader />

            <div className={styles.content}>
                {/* 왼쪽 폼 영역 */}
                <OrderForm
                    shippingInfo={shippingInfo}
                    setShippingInfo={setShippingInfo}
                    requestMemo={requestMemo}
                    setRequestMemo={setRequestMemo}
                />

                {/* 오른쪽 장바구니 요약 정보 */}
                <OrderSummary
                    items={items}
                    totalPrice={totalPrice}
                    finalPrice={finalPrice}
                    isSubmitting={isSubmitting}
                    onSubmit={handleSubmitOrder}
                    pointUsed={pointUsed}
                    onPointChange={handlePointChange}
                    currentPoint={currentPoint}
                    expectedEarnPoint={expectedEarnPoint}
                    deliveryFee={deliveryFee}
                />
            </div>

            <Modal
                isOpen={isSuccessModalOpen}
                onClose={handleSuccessConfirm}
                title="주문이 완료되었습니다! 🎉"
                description={`말랑이가 주문을 확인하고 귀여운 상품를 준비하고 있어요!\n잠시만 기다려주세요 🎀`}
                confirmText="주문 내역 보기"
                cancelText="메인으로 가기"
                onConfirm={handleSuccessConfirm}
                variant="ditto"
            />
        </div>
    );
}
