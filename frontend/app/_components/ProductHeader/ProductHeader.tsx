'use client';

import Link from 'next/link';
import { ArrowLeft } from 'lucide-react';
import styles from './ProductHeader.module.css';

export default function ProductHeader() {
    return (
        <header className={styles.header}>
            <div className={styles.inner}>
                <div className={styles.bannerArea}>
                    <picture>
                        <source media="(max-width: 768px)" srcSet="/images/banner-mobile.png" />
                        <img src="/images/hero-banner.png" alt="Mallang Shop Banner" className={styles.heroBanner} />
                    </picture>
                </div>
            </div>
        </header>
    );
}
