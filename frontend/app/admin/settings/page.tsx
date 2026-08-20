import React from 'react';
import styles from './page.module.css';
import DeliverySettingsForm from './_components/DeliverySettingsForm';

export default function SettingsPage() {
    return (
        <div className={styles.container}>
            <div className={styles.header}>
                <h1 className={styles.title}>상점 배송비 설정</h1>
                <p className={styles.subtitle}>쇼핑몰의 배송비 정책을 관리할 수 있습니다.</p>
            </div>
            
            <div className={styles.content}>
                <DeliverySettingsForm />
            </div>
        </div>
    );
}
