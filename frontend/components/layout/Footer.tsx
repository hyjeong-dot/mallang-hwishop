"use client";

import { useState, useEffect } from "react";
import Image from "next/image";
import Link from "next/link";
import { Instagram, Gift, Heart, MapPin, Phone, Mail, Clock } from "lucide-react";
import styles from "./layout.module.css";

export default function Footer() {
    const [showTop, setShowTop] = useState(false);

    useEffect(() => {
        const handleScroll = () => {
            setShowTop(window.scrollY > 400);
        };
        window.addEventListener("scroll", handleScroll);
        return () => window.removeEventListener("scroll", handleScroll);
    }, []);

    const scrollToTop = () => {
        window.scrollTo({ top: 0, behavior: "smooth" });
    };

    return (
        <footer className={styles.footer}>
            <div className={styles.container}>
                <div className={styles.footerGrid}>
                    <div>
                        <div className={styles.logoArea} style={{ color: '#fff', marginBottom: '1rem' }}>
                            <Image
                                src="/images/logo.png"
                                alt="Mallang Logo"
                                width={28}
                                height={28}
                                className={styles.footerLogoImage}
                            />
                            <span>말랑이샵</span>
                        </div>
                        <p className={styles.footerDesc}>
                            세상에서 가장 귀여운 수제볼 shop
                            <br />
                            말랑말랑한 행복을 선물합니다.
                        </p>
                        <div className={styles.socialIcons}>
                            <a href="#" className={styles.socialIcon} aria-label="Instagram">
                                <Instagram size={18} />
                            </a>
                            <a href="#" className={styles.socialIcon} aria-label="Gift">
                                <Gift size={18} />
                            </a>
                            <a href="#" className={styles.socialIcon} aria-label="Heart">
                                <Heart size={18} />
                            </a>
                        </div>
                    </div>

                    <div>
                        <h4 className={styles.footerTitle}>말랑이샵</h4>
                        <nav className={styles.footerLinks}>
                            <Link href="/#about" className={styles.footerLink}>샵 소개</Link>
                            <Link href="/menus" className={styles.footerLink}>상품</Link>
                            <Link href="/#special" className={styles.footerLink}>즐길거리</Link>
                        </nav>
                    </div>

                    <div>
                        <h4 className={styles.footerTitle}>고객 지원</h4>
                        <nav className={styles.footerLinks}>
                            <Link href="#" className={styles.footerLink}>자주 묻는 질문</Link>
                            <Link href="#" className={styles.footerLink}>예약 문의</Link>
                            <Link href="#" className={styles.footerLink}>이벤트</Link>
                        </nav>
                    </div>

                    <div>
                        <h4 className={styles.footerTitle}>연락처</h4>
                        <div className={styles.contactItem}>
                            <MapPin size={16} />
                            <span>서울특별시 강남구 테헤란로 123</span>
                        </div>
                        <div className={styles.contactItem}>
                            <Phone size={16} />
                            <span>02-1234-5678</span>
                        </div>
                        <div className={styles.contactItem}>
                            <Mail size={16} />
                            <span>hello@mallangshop.kr</span>
                        </div>
                        <div className={styles.contactItem}>
                            <Clock size={16} />
                            <span>매일 09:00 - 22:00</span>
                        </div>
                    </div>
                </div>

                <div style={{ display: 'flex', justifyContent: 'center', marginBottom: '2rem' }}>
                    <Image
                        src="/images/footer-illustration.png"
                        alt="Cute Fluffy Character"
                        width={250}
                        height={250}
                        style={{ borderRadius: '20px', objectFit: 'cover', boxShadow: '0 8px 24px rgba(0,0,0,0.1)' }}
                    />
                </div>

                <div className={styles.footerBottom}>
                    <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '0.5rem' }}>
                        <Image
                            src="/images/logo.png"
                            alt="Mallang Icon"
                            width={16}
                            height={16}
                            style={{ opacity: 0.7 }}
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
                >
                    <Image
                        src="/images/logo-alt.png"
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
