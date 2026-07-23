'use client';

import Link from 'next/link';
import { ArrowLeft } from 'lucide-react';
import styles from './MenuHeader.module.css';

export default function MenuHeader() {
    return (
        <header className={styles.header}>
            <div className={styles.inner}>
                <Link href="/" className={styles.backLink}>
                    <ArrowLeft size={18} />
                    <span>홈으로</span>
                </Link>
                <div className={styles.titleArea}>
                    <span className={styles.emoji}>💜</span>
                    <h1 className={styles.title}>말랑이샵 상품</h1>
                    <p className={styles.subtitle}>수제로 만든 다양한 제품을 만나보세요! 🎀</p>
                </div>
            </div>
        </header>
    );
}
