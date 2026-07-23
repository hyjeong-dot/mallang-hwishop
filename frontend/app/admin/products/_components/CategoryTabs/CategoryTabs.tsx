'use client';

import styles from './CategoryTabs.module.css';
import { useCategories } from './useCategories';
import { useProducts } from '../ProductGrid/useProducts';

interface CategoryTabsProps {
    onCategoryChange: (id: number | null) => void;
    selectedCategory: number | null;
}

export default function CategoryTabs({ onCategoryChange, selectedCategory }: CategoryTabsProps) {
    const { categories } = useCategories();
    const { productCounts, stats } = useProducts();

    return (
        <section className={styles.tabs} aria-label="Filter products">
            <button
                className={`${styles.tab} ${selectedCategory === null ? styles.active : ''}`}
                onClick={() => onCategoryChange(null)}
            >
                <span className={styles.tabIcon}>📋</span>
                전체
                <span className={styles.tabCount}>{stats.total}</span>
            </button>

            {categories.map((category) => (
                <button
                    key={category.id}
                    className={`${styles.tab} ${selectedCategory === category.id ? styles.active : ''}`}
                    onClick={() => onCategoryChange(category.id)}
                >
                    <span className={styles.tabIcon}>{category.icon}</span>
                    {category.name}
                    <span className={styles.tabCount}>{productCounts[category.name] || 0}</span>
                </button>
            ))}
        </section>
    );
}
