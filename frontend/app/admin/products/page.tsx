'use client';

import { useState } from 'react';
import PageHeader from './_components/PageHeader';
import ProductStats from './_components/ProductStats';
import SearchBar from './_components/SearchBar';
import CategoryTabs from './_components/CategoryTabs';
import ProductGrid from './_components/ProductGrid';
import { useProducts } from './_components/ProductGrid/useProducts';
import styles from './page.module.css';

export default function ProductsPage() {
    const [selectedCategory, setSelectedCategory] = useState<number | null>(null);
    const [searchQuery, setSearchQuery] = useState('');

    const { stats } = useProducts({ selectedCategory, searchQuery });

    return (
        <main className={styles.container}>
            <PageHeader />
            <ProductStats stats={stats} />

            <SearchBar
                searchQuery={searchQuery}
                setSearchQuery={setSearchQuery}
            />

            <CategoryTabs
                onCategoryChange={setSelectedCategory}
                selectedCategory={selectedCategory}
            />

            <ProductGrid
                selectedCategory={selectedCategory}
                searchQuery={searchQuery}
            />
        </main>
    );
}
