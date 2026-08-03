'use client';

import Image from 'next/image';
import Link from 'next/link';
import { Edit2, Trash2 } from 'lucide-react';
import styles from './ProductTable.module.css';
import { ProductResponse } from '../ProductGrid/useProducts';
import { getImageSrc } from '@/lib/api';

interface ProductTableProps {
    products: ProductResponse[];
    onToggleSoldOut: (productId: string) => void;
    onDelete: (productId: string) => void;
}

export default function ProductTable({ products, onToggleSoldOut, onDelete }: ProductTableProps) {
    const formatPrice = (price: number) => new Intl.NumberFormat('ko-KR').format(price);

    return (
        <div className={styles.tableWrapper}>
            <table className={styles.table}>
                <thead>
                    <tr>
                        <th className={styles.thNum}>#</th>
                        <th className={styles.thImage}>이미지</th>
                        <th className={styles.thName}>상품명</th>
                        <th className={styles.thCategory}>카테고리</th>
                        <th className={styles.thPrice}>가격</th>
                        <th className={styles.thStatus}>상태</th>
                        <th className={styles.thOptions}>옵션</th>
                        <th className={styles.thActions}>관리</th>
                    </tr>
                </thead>
                <tbody>
                    {products.map((product, index) => (
                        <tr
                            key={product.id}
                            className={`${styles.row} ${product.isSoldOut ? styles.soldOutRow : ''}`}
                        >
                            <td className={styles.num}>{index + 1}</td>
                            <td className={styles.imageCell}>
                                <Link href={`/admin/products/${product.id}`} className={styles.imageLink}>
                                    <div className={styles.imageBox}>
                                        <Image
                                            src={getImageSrc(product.imageSrc)}
                                            alt={product.korName}
                                            fill
                                            className={styles.img}
                                            sizes="40px"
                                        />
                                    </div>
                                </Link>
                            </td>
                            <td className={styles.nameCell}>
                                <Link href={`/admin/products/${product.id}`} className={styles.nameLink}>
                                    <span className={styles.korName}>{product.korName}</span>
                                    <span className={styles.engName}>{product.engName}</span>
                                </Link>
                            </td>
                            <td className={styles.category}>
                                <span className={styles.categoryBadge}>
                                    {product.categoryIcon} {product.categoryName}
                                </span>
                            </td>
                            <td className={styles.price}>
                                {product.discountPrice && product.discountPrice > 0 ? (
                                    <>
                                        <span style={{ textDecoration: 'line-through', color: '#999', fontSize: '0.9em', display: 'block' }}>₩{formatPrice(product.price)}</span>
                                        <span style={{ color: '#e53e3e', fontWeight: 'bold' }}>₩{formatPrice(product.discountPrice)}</span>
                                    </>
                                ) : (
                                    <span>₩{formatPrice(product.price)}</span>
                                )}
                            </td>
                            <td className={styles.status}>
                                <button
                                    className={`${styles.statusBadge} ${product.isSoldOut ? styles.soldOut : styles.available}`}
                                    onClick={() => onToggleSoldOut(String(product.id))}
                                    title={product.isSoldOut ? '품절 해제' : '품절 처리'}
                                >
                                    {product.isSoldOut ? '품절' : '판매중'}
                                </button>
                            </td>
                            <td className={styles.options}>
                                {(product as any).options?.length > 0 ? (
                                    <span className={styles.optionCount}>{(product as any).options.length}개</span>
                                ) : (
                                    <span className={styles.noOption}>—</span>
                                )}
                            </td>
                            <td className={styles.actions}>
                                <Link
                                    href={`/admin/products/${product.id}`}
                                    className={styles.actionBtn}
                                    title="수정"
                                >
                                    <Edit2 size={15} />
                                </Link>
                                <button
                                    className={`${styles.actionBtn} ${styles.deleteBtn}`}
                                    onClick={() => onDelete(String(product.id))}
                                    title="삭제"
                                >
                                    <Trash2 size={15} />
                                </button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}
