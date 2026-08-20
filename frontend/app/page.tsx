'use client';

import { useState } from 'react';
import ProductHeader from './_components/ProductHeader/ProductHeader';
import SearchBar from './_components/SearchBar/SearchBar';
import CategoryFilter from './_components/CategoryFilter/CategoryFilter';
import ProductGrid from './_components/ProductGrid/ProductGrid';
import NoticePopup from '@/components/common/NoticePopup/NoticePopup';
import styles from './page.module.css';

export default function ProductsPage() {
    const [selectedCategory, setSelectedCategory] = useState<number | null>(null);
    const [searchQuery, setSearchQuery] = useState('');

    return (
        <div className={styles.page}>
            <NoticePopup 
                id="main_notice_popup" 
                imageUrl="/images/notice.png" // 더미 이미지
            />
            <ProductHeader />
            <SearchBar
                searchQuery={searchQuery}
                setSearchQuery={setSearchQuery}
            />
            <CategoryFilter
                selectedCategory={selectedCategory}
                onCategoryChange={setSelectedCategory}
            />
            <ProductGrid
                selectedCategory={selectedCategory}
                searchQuery={searchQuery}
            />
        </div>
    );
}
