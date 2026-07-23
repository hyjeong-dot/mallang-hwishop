'use client';

import { useRouter } from 'next/navigation';
import Link from 'next/link';
import { ArrowLeft } from 'lucide-react';
import { useCategories } from '../_components/CategoryTabs/useCategories';
import ProductForm, { ProductFormData, ImageItem, OptionFormData } from '../_components/ProductForm/ProductForm';
import { useProductSubmit } from '../_components/ProductForm/useProductSubmit';
import styles from './page.module.css';

export default function NewProductPage() {
    const router = useRouter();
    const { categories } = useCategories();
    const { createProduct, isSubmitting } = useProductSubmit();

    const handleSubmit = async (formData: ProductFormData, images: ImageItem[], options: OptionFormData[]) => {
        try {
            await createProduct(formData, images, options);
            router.push('/admin/products');
        } catch (error) {
            console.error('Submit failed:', error);
        }
    };

    const handleCancel = () => {
        router.push('/admin/products');
    };

    return (
        <div className={styles.container}>
            <Link href="/admin/products" className={styles.backButton}>
                <ArrowLeft size={20} />
                <span>목록으로 돌아가기</span>
            </Link>

            <header className={styles.header}>
                <h1 className={styles.title}>새 상품 등록</h1>
                <p className={styles.subtitle}>새로운 상품를 등록하고 옵션을 설정하세요</p>
            </header>

            <ProductForm
                categories={categories}
                onSubmit={handleSubmit}
                onCancel={handleCancel}
                isSubmitting={isSubmitting}
                submitLabel="상품 등록"
            />
        </div>
    );
}
