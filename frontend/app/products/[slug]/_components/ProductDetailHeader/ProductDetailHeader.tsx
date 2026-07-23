'use client';

import Link from 'next/link';
import { ArrowLeft } from 'lucide-react';
import styles from './ProductDetailHeader.module.css';

export default function ProductDetailHeader() {
    return (
        <header className={styles.header}>
            <div className={styles.container}>
                <Link href="/products" className={styles.backLink}>
                    <ArrowLeft size={18} />
                    <span>전체 상품</span>
                </Link>
            </div>
        </header>
    );
}
