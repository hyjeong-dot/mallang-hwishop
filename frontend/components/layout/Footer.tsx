"use client";

import { useState, useEffect } from "react";
import Image from "next/image";
import Link from "next/link";
import { Instagram, Gift, Heart, MapPin, Phone, Mail, Clock } from "lucide-react";
import styles from "./layout.module.css";

export default function Footer() {
    const [showTop, setShowTop] = useState(false);

    const [isHoveringTop, setIsHoveringTop] = useState(false);
    const [frame, setFrame] = useState(1);

    useEffect(() => {
        const handleScroll = () => {
            setShowTop(window.scrollY > 400);
        };
        window.addEventListener("scroll", handleScroll);
        return () => window.removeEventListener("scroll", handleScroll);
    }, []);

    useEffect(() => {
        let interval: NodeJS.Timeout;
        if (isHoveringTop) {
            interval = setInterval(() => {
                setFrame((prev) => (prev === 1 ? 2 : 1));
            }, 250); // Change frame every 250ms
        } else {
            setFrame(1);
        }
        return () => clearInterval(interval);
    }, [isHoveringTop]);

    const scrollToTop = () => {
        window.scrollTo({ top: 0, behavior: "smooth" });
    };

    return (
        <footer className={styles.footer}>
            <div className={styles.container}>
                <div style={{ display: 'flex', gap: '1.5rem', justifyContent: 'center', marginBottom: '2.5rem', borderBottom: '1px solid rgba(255,255,255,0.1)', paddingBottom: '1.5rem' }}>
                    <Link href="/terms" className={styles.footerLink}>이용약관</Link>
                    <Link href="/privacy" className={styles.footerLink}>개인정보처리방침</Link>
                    <a href="http://www.ftc.go.kr/bizCommPop.do?wrkr_no=123-45-67890" target="_blank" rel="noopener noreferrer" className={styles.footerLink}>사업자정보확인</a>
                </div>

                <div style={{ display: 'flex', flexDirection: 'row', justifyContent: 'space-between', alignItems: 'flex-start', flexWrap: 'wrap', gap: '3rem' }}>
                    <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem', minWidth: '300px' }}>
                        <div className={styles.logoArea} style={{ color: '#fff', marginBottom: 0 }}>
                            <Image
                                src="/images/logo.png"
                                alt="Mallang Logo"
                                width={28}
                                height={28}
                                className={styles.footerLogoImage}
                            />
                            <span>말랑이샵</span>
                        </div>
                        <p className={styles.footerDesc} style={{ margin: 0 }}>
                            세상에서 가장 귀여운 수제볼 shop
                            <br />
                            말랑말랑한 행복을 선물합니다.
                        </p>
                        <div className={styles.socialIcons} style={{ marginTop: '0.5rem' }}>
                            <a href="https://instagram.com/" target="_blank" rel="noopener noreferrer" className={styles.socialIcon} aria-label="Instagram">
                                <Instagram size={18} />
                            </a>
                        </div>
                    </div>

                    <div style={{ flex: 1, fontSize: '0.85rem', color: 'rgba(255,255,255,0.6)', lineHeight: '1.8' }}>
                        <p>상호명: 말랑이샵 | 대표자: 홍길동 | 개인정보관리책임자: 홍길동 | 전화: 02-1234-5678 | 이메일: hello@mallangshop.kr</p>
                        <p>주소: 서울특별시 강남구 테헤란로 123 | 사업자등록번호: 123-45-67890 | 통신판매업신고: 제2026-서울강남-0123호 </p>
                        
                        <p style={{ marginTop: '1.5rem', fontWeight: 600, color: 'rgba(255,255,255,0.8)', fontSize: '0.95rem' }}>
                            만 14세 미만의 어린이, 임산부, 호흡기 질환자, 피부가 예민하신 분들은 절대 구매/사용을 금합니다.
                        </p>
                    </div>
                </div>

                <div className={styles.footerBottom} style={{ marginTop: '1rem' }}>
                    <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '0.5rem' }}>
                        <Image
                            src="/images/footer-illustration.png"
                            alt="Cute Fluffy Character"
                            width={24}
                            height={24}
                            style={{ opacity: 0.9, borderRadius: '50%' }}
                        />
                        <p>&copy; 2026 말랑이샵. All rights reserved.</p>
                    </div>
                </div>
            </div>

            {/* Scroll To Top Button */}
            {showTop && (
                <button
                    className={styles.scrollTopButton}
                    onClick={scrollToTop}
                    title="맨 위로 가기"
                    onMouseEnter={() => setIsHoveringTop(true)}
                    onMouseLeave={() => setIsHoveringTop(false)}
                >
                    <Image
                        src={`/images/scroll-top-${frame}.png`}
                        alt="Top"
                        width={40}
                        height={40}
                        className={styles.scrollTopImage}
                    />
                    <span className={styles.scrollTopBadge}>⇧</span>
                </button>
            )}
        </footer>
    );
}
