'use client';

import { useState, useEffect } from 'react';
import { CheckCircle2, Circle, Settings2, Loader2 } from 'lucide-react';
import styles from './ProductDetailOptions.module.css';

interface OptionItem {
    id: number;
    name: string;
    priceDelta: number;
    sortOrder: number;
}

interface ProductOption {
    id: number;
    productId: number;
    name: string;
    isRequired: boolean;
    isMultiSelect: boolean;
    sortOrder: number;
    items: OptionItem[];
}

interface ProductDetailOptionsProps {
    productId: number;
}

export default function ProductDetailOptions({ productId }: ProductDetailOptionsProps) {
    const [options, setOptions] = useState<ProductOption[]>([]);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const fetchProduct = async () => {
            try {
                setIsLoading(true);
                const response = await fetch(`/api/admin/products/${productId}`);

                if (response.status === 401) {
                    if (typeof window !== 'undefined') window.location.href = '/login';
                    return;
                }

                if (!response.ok) throw new Error('Failed to fetch product');

                const data = await response.json();
                setOptions(data.options || []);
            } catch (err) {
                console.error('Error fetching product options:', err);
                setOptions([]);
            } finally {
                setIsLoading(false);
            }
        };

        if (productId) fetchProduct();
    }, [productId]);

    if (isLoading) {
        return (
            <section className={styles.optionsSection}>
                <div className={styles.header}>
                    <h2 className={styles.title}>
                        <Settings2 size={20} />
                        옵션 구성 정보
                    </h2>
                </div>
                <div className={styles.loadingState}>
                    <Loader2 size={24} className={styles.spinner} />
                    <p>옵션 정보를 불러오는 중...</p>
                </div>
            </section>
        );
    }

    return (
        <section className={styles.optionsSection}>
            <div className={styles.header}>
                <h2 className={styles.title}>
                    <Settings2 size={20} />
                    옵션 구성 정보
                </h2>
            </div>

            {options.length === 0 ? (
                <div className={styles.emptyState}>
                    <p>등록된 옵션이 없습니다.</p>
                </div>
            ) : (
                <div className={styles.optionsList}>
                    {options.map((option) => (
                        <div key={option.id} className={styles.optionGroup}>
                            <div className={styles.groupHeader}>
                                <h3 className={styles.groupName}>
                                    {option.name}
                                    {option.isRequired && <span className={styles.requiredMark}>*</span>}
                                </h3>
                                <span className={styles.typeBadge}>
                                    {option.isMultiSelect ? '다중 선택' : '단일 선택'}
                                </span>
                            </div>

                            <ul className={styles.itemList}>
                                {option.items.map((item) => (
                                    <li key={item.id} className={styles.item}>
                                        <div className={styles.itemContent}>
                                            {option.isMultiSelect ?
                                                <CheckCircle2 size={14} className={styles.icon} /> :
                                                <Circle size={14} className={styles.icon} />
                                            }
                                            <span className={styles.itemName}>{item.name}</span>
                                        </div>
                                        {item.priceDelta > 0 && (
                                            <span className={styles.itemPrice}>
                                                +{item.priceDelta.toLocaleString()}원
                                            </span>
                                        )}
                                    </li>
                                ))}
                            </ul>
                        </div>
                    ))}
                </div>
            )}
        </section>
    );
}