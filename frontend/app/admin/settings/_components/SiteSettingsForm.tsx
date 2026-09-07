'use client';

import { useState, useEffect } from 'react';
import useSiteSettings from './useSiteSettings';
import styles from '../page.module.css';
import { Save } from 'lucide-react';

export default function SiteSettingsForm() {
    const { settings, isLoading, updateSettings } = useSiteSettings();
    const [basicFee, setBasicFee] = useState<number>(3500);
    const [jejuExtraFee, setJejuExtraFee] = useState<number>(3000);
    const [instagramUrl, setInstagramUrl] = useState<string>('');
    const [cancelTimeoutMinutes, setCancelTimeoutMinutes] = useState<number>(60);
    const [isSaving, setIsSaving] = useState(false);

    useEffect(() => {
        if (settings) {
            setBasicFee(settings.basicFee);
            setJejuExtraFee(settings.jejuExtraFee);
            setInstagramUrl(settings.instagramUrl || '');
            setCancelTimeoutMinutes(settings.cancelTimeoutMinutes || 60);
        }
    }, [settings]);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setIsSaving(true);
        try {
            await updateSettings({ basicFee, jejuExtraFee, instagramUrl, cancelTimeoutMinutes });
            alert('상점 설정이 저장되었습니다.');
        } catch (error) {
            alert('저장에 실패했습니다.');
        } finally {
            setIsSaving(false);
        }
    };

    if (isLoading) {
        return <div className={styles.loading}>설정을 불러오는 중...</div>;
    }

    return (
        <form className={styles.formCard} onSubmit={handleSubmit}>
            <div className={styles.formGroup}>
                <label className={styles.label}>
                    기본 배송비 (원)
                    <span className={styles.tooltip}>전국 공통 기본 배송비입니다.</span>
                </label>
                <input
                    type="number"
                    className={styles.input}
                    value={basicFee}
                    onChange={(e) => setBasicFee(Number(e.target.value))}
                    min="0"
                    step="100"
                    required
                />
            </div>

            <div className={styles.formGroup}>
                <label className={styles.label}>
                    제주/도서산간 추가 배송비 (원)
                    <span className={styles.tooltip}>제주 및 도서산간 지역에 추가되는 배송비입니다.</span>
                </label>
                <input
                    type="number"
                    className={styles.input}
                    value={jejuExtraFee}
                    onChange={(e) => setJejuExtraFee(Number(e.target.value))}
                    min="0"
                    step="100"
                    required
                />
            </div>
            
            <div className={styles.formGroup}>
                <label className={styles.label}>
                    기본 인스타그램 URL
                    <span className={styles.tooltip}>상점 헤더 등에 사용될 기본 인스타그램 링크입니다.</span>
                </label>
                <input
                    type="text"
                    className={styles.input}
                    value={instagramUrl}
                    onChange={(e) => setInstagramUrl(e.target.value)}
                    placeholder="https://instagram.com/..."
                />
            </div>

            <div className={styles.formGroup}>
                <label className={styles.label}>
                    미결제 주문 자동 취소 시간 (분)
                    <span className={styles.tooltip}>무통장 입금 등 결제 대기 상태인 주문이 이 시간을 초과하면 자동 취소됩니다.</span>
                </label>
                <input
                    type="number"
                    className={styles.input}
                    value={cancelTimeoutMinutes}
                    onChange={(e) => setCancelTimeoutMinutes(Number(e.target.value))}
                    min="10"
                    step="10"
                    required
                />
            </div>

            <div className={styles.formActions}>
                <button type="submit" className={styles.saveButton} disabled={isSaving}>
                    <Save className={styles.buttonIcon} size={18} />
                    {isSaving ? '저장 중...' : '변경사항 저장'}
                </button>
            </div>
        </form>
    );
}
