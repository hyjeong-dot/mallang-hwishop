'use client';

import { Search, Download, Upload } from 'lucide-react';
import { useRef } from 'react';
import styles from '../page.module.css';
import toast from 'react-hot-toast';

const STATUS_FILTERS = [
    { value: '', label: '모든 상태' },
    { value: 'PENDING', label: '결제 대기' },
    { value: 'PAID', label: '결제 완료' },
    { value: 'PREPARING', label: '배송 준비 중' },
    { value: 'COMPLETED', label: '배송 완료' },
    { value: 'CANCELLED', label: '주문 취소' },
];

interface OrderToolbarProps {
    searchQuery: string;
    onSearchChange: (q: string) => void;
    statusFilter: string;
    onStatusChange: (status: string) => void;
    resultCount: number;
    onExcelUploadSuccess: () => void;
    selectedOrderIds: Set<number>;
    onBatchUpdateStatus: (status: string) => void;
    onBatchCreateShippingGroup: () => void;
}

export default function OrderToolbar({
    searchQuery, onSearchChange,
    statusFilter, onStatusChange,
    resultCount, onExcelUploadSuccess,
    selectedOrderIds, onBatchUpdateStatus,
    onBatchCreateShippingGroup
}: OrderToolbarProps) {
    const fileInputRef = useRef<HTMLInputElement>(null);
    const activeLabel = STATUS_FILTERS.find(f => f.value === statusFilter)?.label;

    const handleDownloadExcel = () => {
        window.location.href = '/api/admin/orders/excel/download';
    };

    const handleUploadClick = () => {
        fileInputRef.current?.click();
    };

    const handleFileChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0];
        if (!file) return;

        const formData = new FormData();
        formData.append('file', file);

        const toastId = toast.loading('엑셀 업로드 중...');
        try {
            const res = await fetch('/api/admin/orders/excel/upload', {
                method: 'POST',
                body: formData,
            });

            if (!res.ok) throw new Error('업로드 실패');
            toast.success('송장 번호가 일괄 등록되었습니다! 📦', { id: toastId });
            onExcelUploadSuccess();
        } catch (error) {
            toast.error('엑셀 업로드 중 오류가 발생했습니다.', { id: toastId });
        } finally {
            if (fileInputRef.current) {
                fileInputRef.current.value = '';
            }
        }
    };

    return (
        <>
            <div className={styles.toolbar}>
                <div className={styles.searchBox}>
                    <Search size={16} className={styles.searchIcon} />
                    <input
                        className={styles.searchInput}
                        type="text"
                        placeholder="주문번호, 주문자명, 상품명으로 검색..."
                        value={searchQuery}
                        onChange={(e) => onSearchChange(e.target.value)}
                    />
                </div>
                <select
                    className={styles.statusSelect}
                    value={statusFilter}
                    onChange={(e) => onStatusChange(e.target.value)}
                >
                    {STATUS_FILTERS.map(f => (
                        <option key={f.value} value={f.value}>{f.label}</option>
                    ))}
                </select>
                <div className={styles.excelActions}>
                    <button className={styles.excelBtn} onClick={handleDownloadExcel}>
                        <Download size={16} /> 엑셀 다운로드
                    </button>
                    <button className={styles.excelBtn} onClick={handleUploadClick}>
                        <Upload size={16} /> 송장 업로드
                    </button>
                    <input 
                        type="file" 
                        accept=".xlsx, .xls" 
                        ref={fileInputRef} 
                        style={{ display: 'none' }} 
                        onChange={handleFileChange} 
                    />
                </div>
            </div>

            {(searchQuery || statusFilter) && (
                <div className={styles.resultCount}>
                    🔍 {resultCount}건
                    {statusFilter && ` · ${activeLabel}`}
                    {searchQuery && ` · "${searchQuery}"`}
                </div>
            )}

            {selectedOrderIds.size > 0 && (
                <div className={styles.batchActions} style={{ display: 'flex', alignItems: 'center', gap: '12px', padding: '12px', background: 'var(--bg-card)', borderRadius: '8px', border: '1px solid var(--border-color)', marginTop: '12px' }}>
                    <span style={{ fontSize: '14px', fontWeight: 600, color: 'var(--text-primary)' }}>
                        선택된 주문 {selectedOrderIds.size}개 상태 일괄 변경:
                    </span>
                    <button 
                        onClick={() => onBatchUpdateStatus('PREPARING')}
                        style={{ padding: '6px 12px', fontSize: '13px', borderRadius: '6px', border: '1px solid var(--border-color)', background: '#fff', cursor: 'pointer' }}
                    >
                        상품 준비 시작
                    </button>
                    <button 
                        onClick={() => onBatchUpdateStatus('COMPLETED')}
                        style={{ padding: '6px 12px', fontSize: '13px', borderRadius: '6px', border: 'none', background: 'var(--color-success)', color: '#fff', cursor: 'pointer' }}
                    >
                        제공 완료
                    </button>
                    <div style={{ width: '1px', height: '20px', background: 'var(--border-color)', margin: '0 8px' }} />
                    <button 
                        onClick={onBatchCreateShippingGroup}
                        style={{ padding: '6px 12px', fontSize: '13px', borderRadius: '6px', border: 'none', background: '#3b82f6', color: '#fff', cursor: 'pointer', fontWeight: 600 }}
                    >
                        합배송 묶기
                    </button>
                </div>
            )}
        </>
    );
}

export { STATUS_FILTERS };
