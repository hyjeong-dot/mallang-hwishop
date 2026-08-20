'use client';

import { AdminOrder } from './useAdminOrders';
import OrderCard from './OrderCard';
import styles from '../page.module.css';

interface OrderListProps {
    orders: AdminOrder[];
    onUpdateStatus: (orderId: number, status: string, cancelReasonType?: string, cancelReason?: string) => void;
    onUpdateTrackingInfo: (orderId: number, carrier: string, trackingNumber: string) => void;
    selectedOrderIds: Set<number>;
    toggleSelectOrder: (orderId: number) => void;
    toggleSelectAll: (isSelectAll: boolean) => void;
}

export default function OrderList({ 
    orders, 
    onUpdateStatus, 
    onUpdateTrackingInfo,
    selectedOrderIds,
    toggleSelectOrder,
    toggleSelectAll
}: OrderListProps) {
    const isAllSelected = orders.length > 0 && orders.length === selectedOrderIds.size;

    return (
        <div className={styles.orderList}>
            <div className={styles.listHeader} style={{ padding: '0 24px', display: 'flex', alignItems: 'center', gap: '12px' }}>
                <input 
                    type="checkbox" 
                    checked={isAllSelected}
                    onChange={(e) => toggleSelectAll(e.target.checked)}
                    style={{ width: '18px', height: '18px', cursor: 'pointer' }}
                />
                <span style={{ fontSize: '14px', fontWeight: 600, color: 'var(--text-secondary)' }}>전체 선택</span>
            </div>
            {orders.map((order) => (
                <OrderCard 
                    key={order.id} 
                    order={order} 
                    onUpdateStatus={onUpdateStatus}
                    onUpdateTrackingInfo={onUpdateTrackingInfo}
                    isSelected={selectedOrderIds.has(order.id)}
                    onToggleSelect={() => toggleSelectOrder(order.id)}
                />
            ))}
        </div>
    );
}
