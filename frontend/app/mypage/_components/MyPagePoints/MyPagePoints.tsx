"use client";

import { useState, useEffect } from "react";
import { fetchAPI } from "@/lib/api";
import { useAuth } from "@/context/AuthContext";
import styles from "./MyPagePoints.module.css";
import LoadingCharacter from "@/components/common/LoadingCharacter/LoadingCharacter";

interface PointHistory {
    id: string;
    memberId: string;
    amount: number;
    type: 'EARN' | 'USE' | 'EXPIRE' | 'CANCEL';
    description: string;
    createdAt: string;
    expireAt: string | null;
}

export default function MyPagePoints() {
    const { user } = useAuth();
    const [histories, setHistories] = useState<PointHistory[]>([]);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const loadHistories = async () => {
            try {
                const data = await fetchAPI('/points/history');
                setHistories(data || []);
            } catch (error) {
                console.error("Failed to fetch point histories", error);
            } finally {
                setIsLoading(false);
            }
        };

        if (user) {
            loadHistories();
        }
    }, [user]);

    if (isLoading) {
        return (
            <div className={styles.loading}>
                <LoadingCharacter message="적립금 내역을 불러오고 있어요..." />
            </div>
        );
    }

    return (
        <section className={styles.container}>
            <header className={styles.header}>
                <h2 className={styles.title}>내 적립금</h2>
                <div className={styles.pointSummary}>
                    <span>보유 적립금</span>
                    <strong className={styles.pointValue}>{user?.currentPoint?.toLocaleString() || 0} 원</strong>
                </div>
            </header>

            <div className={styles.list}>
                {histories.length === 0 ? (
                    <div className={styles.empty}>
                        적립금 내역이 없습니다.
                    </div>
                ) : (
                    histories.map(history => (
                        <div key={history.id} className={styles.historyCard}>
                            <div className={styles.historyInfo}>
                                <span className={styles.date}>
                                    {new Date(history.createdAt).toLocaleDateString()}
                                </span>
                                <span className={styles.description}>
                                    {history.description}
                                </span>
                            </div>
                            <div className={styles.historyAmount}>
                                <span className={`${styles.amount} ${history.type === 'EARN' ? styles.earn : styles.use}`}>
                                    {history.type === 'EARN' ? '+' : '-'}{Math.abs(history.amount).toLocaleString()} 원
                                </span>
                                {history.expireAt && history.type === 'EARN' && (
                                    <span className={styles.expire}>
                                        ~ {new Date(history.expireAt).toLocaleDateString()} 까지
                                    </span>
                                )}
                            </div>
                        </div>
                    ))
                )}
            </div>
        </section>
    );
}
