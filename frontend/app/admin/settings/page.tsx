import React from 'react';
import styles from './page.module.css';
import SiteSettingsForm from './_components/SiteSettingsForm';

export default function SettingsPage() {
    return (
        <div className={styles.container}>
            <div className={styles.header}>
                <h1 className={styles.title}>상점 설정</h1>
                <p className={styles.subtitle}>쇼핑몰의 배송비 및 공통 설정을 관리할 수 있습니다.</p>
            </div>
            
            <div className={styles.content}>
                <SiteSettingsForm />
            </div>
        </div>
    );
}
