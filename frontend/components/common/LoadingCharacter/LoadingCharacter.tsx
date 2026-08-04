'use client';

import Image from 'next/image';
import styles from './LoadingCharacter.module.css';

interface LoadingCharacterProps {
    message?: string;
    size?: number;
}

export default function LoadingCharacter({ message = '말랑이 로딩 중...', size = 100 }: LoadingCharacterProps) {
    return (
        <div className={styles.loadingContainer}>
            <div className={styles.characterWrapper}>
                <Image
                    src="/images/scroll-top-3.png"
                    alt="Loading..."
                    width={size}
                    height={size}
                    className={styles.character}
                    priority
                />
            </div>
            {message && <p className={styles.loadingMessage}>{message}</p>}
        </div>
    );
}
