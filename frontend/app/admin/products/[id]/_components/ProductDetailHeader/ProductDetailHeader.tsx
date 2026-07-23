import Link from 'next/link';
import { ArrowLeft, Edit2, Trash2 } from 'lucide-react';
import Button from '@/components/common/Button/Button';
import styles from './ProductDetailHeader.module.css';

interface ProductDetailHeaderProps {
    title?: string;
    id: number;
    onDelete?: () => void;
}

export default function ProductDetailHeader({ title = "상품 상세", id, onDelete }: ProductDetailHeaderProps) {
    return (
        <div className={styles.wrapper}>
            <Link href="/admin/products" className={styles.backButton}>
                <ArrowLeft size={20} />
                <nav>목록으로 돌아가기</nav>
            </Link>

            <div className={styles.headerContent}>
                <h1 className={styles.title}>{title}</h1>
                <div className={styles.actions}>
                    <Link href={`/admin/products/${id}/edit`}>
                        <Button variant="primary" size="sm">
                            <Edit2 size={16} />
                            상품 수정하기
                        </Button>
                    </Link>
                    <Button
                        variant="danger"
                        size="sm"
                        onClick={onDelete}
                    >
                        <Trash2 size={16} />
                        삭제
                    </Button>
                </div>
            </div>
        </div>
    );
}
