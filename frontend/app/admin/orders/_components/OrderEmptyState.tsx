'use client';

import styles from '../page.module.css';

export default function OrderEmptyState() {
    return (
        <div className={styles.emptyState}>
            <p>현재 들어온 주문이 없어요 😢</p>
        </div>
    );
}
