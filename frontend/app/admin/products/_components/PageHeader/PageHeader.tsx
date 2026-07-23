import Link from 'next/link';
import { Plus } from 'lucide-react';
import styles from './PageHeader.module.css';

export default function PageHeader() {

    return (
        <header className={styles.header}>
            <div className={styles.titleSection}>
                <h1 className={styles.title}>상품 관리</h1>
                <p className={styles.subtitle}>카페 상품를 추가하고 관리하세요</p>
            </div>
            <Link href="/admin/products/new" className={styles.addButton}>
                <Plus size={20} />
                새 상품 추가
            </Link>
        </header>
    );
}

