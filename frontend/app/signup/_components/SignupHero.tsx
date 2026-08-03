'use client';

import Image from 'next/image';
import styles from '@/app/login/login.module.css';

export default function SignupHero() {
    return (
        <div className={styles.imageSection}>
            <div className={styles.dittoImageWrapper}>
                <Image
                    src="/images/welcome.png"
                    alt="말랑이샵 환영"
                    fill
                    className={styles.welcomeImage}
                    priority
                />
            </div>
            <div className={styles.welcomeText}>
                <div className={styles.welcomeTitle}>말랑이샵에 오신 걸 환영해요!</div>
                <p className={styles.welcomeDesc}>
                    말랑말랑한 행복을 전해드려요 🎀<br />
                    간단한 정보를 입력해 주세요! ✨
                </p>
            </div>
        </div>
    );
}
