'use client';

import { MessageCircle } from 'lucide-react';
import styles from './KakaoFloatingButton.module.css';

export default function KakaoFloatingButton() {
    return (
        <a 
            href="http://pf.kakao.com/" 
            target="_blank" 
            rel="noopener noreferrer" 
            className={styles.floatingButton}
            aria-label="카카오톡 문의하기"
        >
            <div className={styles.iconWrapper}>
                <MessageCircle size={28} color="#371D1E" fill="#371D1E" />
            </div>
            <span className={styles.tooltip}>카카오톡 문의</span>
        </a>
    );
}
