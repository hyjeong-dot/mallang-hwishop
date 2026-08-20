'use client';

import { useState, useEffect } from 'react';
import { X } from 'lucide-react';
import styles from './NoticePopup.module.css';

interface NoticePopupProps {
    imageUrl: string;
    linkUrl?: string;
    id: string; // 고유 ID로 로컬 스토리지에 저장 시 키값으로 사용
}

export default function NoticePopup({ imageUrl, linkUrl, id }: NoticePopupProps) {
    const [isOpen, setIsOpen] = useState(false);

    useEffect(() => {
        // "오늘 하루 안 보기" 체크 확인
        const hideUntil = localStorage.getItem(`hide_notice_${id}`);
        if (hideUntil) {
            const expiryTime = parseInt(hideUntil, 10);
            if (new Date().getTime() < expiryTime) {
                return; // 안 보기 유효기간 내이므로 열지 않음
            }
        }
        setIsOpen(true);
    }, [id]);

    const handleClose = () => {
        setIsOpen(false);
    };

    const handleHideToday = () => {
        const now = new Date();
        const tomorrow = new Date(now.getFullYear(), now.getMonth(), now.getDate() + 1); // 내일 자정
        localStorage.setItem(`hide_notice_${id}`, tomorrow.getTime().toString());
        setIsOpen(false);
    };

    if (!isOpen) return null;

    return (
        <div className={styles.overlay}>
            <div className={styles.popup}>
                <div className={styles.imageContainer}>
                    {linkUrl ? (
                        <a href={linkUrl} target="_blank" rel="noopener noreferrer">
                            <img src={imageUrl} alt="공지사항" className={styles.image} />
                        </a>
                    ) : (
                        <img src={imageUrl} alt="공지사항" className={styles.image} />
                    )}
                </div>
                <div className={styles.footer}>
                    <button onClick={handleHideToday} className={styles.hideBtn}>
                        오늘 하루 안 보기
                    </button>
                    <button onClick={handleClose} className={styles.closeBtn}>
                        <X size={24} />
                    </button>
                </div>
            </div>
        </div>
    );
}
