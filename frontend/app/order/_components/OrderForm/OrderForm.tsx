"use client";

import React from 'react';
import { Search, MapPin, User, Phone, Shield } from 'lucide-react';
import toast from 'react-hot-toast';
import { useAuth } from '@/context/AuthContext';
import styles from './OrderForm.module.css';

interface OrderFormProps {
    shippingInfo: {
        recipientName: string;
        phoneNumber: string;
        zipcode: string;
        address: string;
        detailAddress: string;
    };
    setShippingInfo: React.Dispatch<React.SetStateAction<any>>;
    requestMemo: string;
    setRequestMemo: (memo: string) => void;
}

export default function OrderForm({
    shippingInfo,
    setShippingInfo,
    requestMemo,
    setRequestMemo
}: OrderFormProps) {
    const { user } = useAuth();
    const [useDefaultAddress, setUseDefaultAddress] = React.useState(true);

    const handleCheckboxChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const checked = e.target.checked;
        setUseDefaultAddress(checked);
        if (checked && user) {
            setShippingInfo((prev: any) => ({
                ...prev,
                recipientName: user.name || '',
                phoneNumber: user.phoneNumber || '',
                zipcode: user.zipcode || '',
                address: user.address || '',
                detailAddress: user.detailAddress || ''
            }));
        }
    };

    const handleSearchAddress = () => {
        if (typeof window !== 'undefined' && (window as any).daum?.Postcode) {
            new (window as any).daum.Postcode({
                oncomplete: (data: any) => {
                    const zonecode = data.zonecode;
                    const fullAddr = data.roadAddress || data.jibunAddress;
                    setShippingInfo((prev: any) => ({
                        ...prev,
                        zipcode: zonecode,
                        address: `(${zonecode}) ${fullAddr}`
                    }));
                    setUseDefaultAddress(false);
                    document.getElementById('detailAddress')?.focus();
                }
            }).open();
        } else {
            toast.error('주소 검색 서비스를 로드하는 중입니다. 잠시 후 다시 시도해 주세요.');
        }
    };

    const handlePhoneChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setUseDefaultAddress(false);
        const value = e.target.value.replace(/[^0-9]/g, '');
        let formattedValue = '';
        if (value.length < 4) formattedValue = value;
        else if (value.length < 8) formattedValue = `${value.slice(0, 3)}-${value.slice(3)}`;
        else formattedValue = `${value.slice(0, 3)}-${value.slice(3, 7)}-${value.slice(7, 11)}`;
        
        setShippingInfo((prev: any) => ({ ...prev, phoneNumber: formattedValue }));
    };

    return (
        <div className={styles.formSection}>
            {/* 결제 수단 */}
            <div className={styles.formGroup}>
                <h3>결제 수단 💳</h3>
                <div className={styles.paymentCard}>
                    <div className={styles.tossBanner}>
                        <div className={styles.tossLeft}>
                            <Shield size={18} className={styles.shieldIcon} />
                            <span className={styles.tossText}>tosspayments</span>
                        </div>
                        <span className={styles.tossDesc}>안전한 결제</span>
                    </div>
                    <div className={styles.paymentInfo}>
                        <div className={styles.paymentMethod}>
                            <span className={styles.methodIcon}>💳</span>
                            <div>
                                <p className={styles.methodTitle}>신용/체크카드</p>
                                <p className={styles.methodSub}>결제하기를 누르면 토스 결제창이 열립니다</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            {/* 배송지 정보 */}
            <div className={styles.formGroup}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <h3>배송지 정보 🚚</h3>
                    <label style={{ display: 'flex', alignItems: 'center', gap: '6px', cursor: 'pointer', fontSize: '14px', color: 'var(--gray-700)', fontWeight: 600 }}>
                        <input 
                            type="checkbox" 
                            checked={useDefaultAddress} 
                            onChange={handleCheckboxChange} 
                            style={{ accentColor: 'var(--color-primary-600)', width: '16px', height: '16px', cursor: 'pointer' }}
                        />
                        기본 배송지 사용
                    </label>
                </div>
                <div className={styles.addressForm}>
                    <div className={styles.inputGroup}>
                        <label className={styles.label}>수령인</label>
                        <div className={styles.inputWrapper}>
                            <User size={18} className={styles.inputIcon} />
                            <input
                                type="text"
                                value={shippingInfo.recipientName}
                                onChange={(e) => {
                                    setUseDefaultAddress(false);
                                    setShippingInfo((prev: any) => ({ ...prev, recipientName: e.target.value }));
                                }}
                                className={styles.input}
                                placeholder="이름 입력"
                            />
                        </div>
                    </div>

                    <div className={styles.inputGroup}>
                        <label className={styles.label}>연락처</label>
                        <div className={styles.inputWrapper}>
                            <Phone size={18} className={styles.inputIcon} />
                            <input
                                type="tel"
                                value={shippingInfo.phoneNumber}
                                onChange={handlePhoneChange}
                                className={styles.input}
                                placeholder="010-0000-0000"
                                maxLength={13}
                            />
                        </div>
                    </div>

                    <div className={styles.inputGroup}>
                        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '4px' }}>
                            <label className={styles.label} style={{ margin: 0 }}>기본 주소</label>
                            <button
                                type="button"
                                onClick={handleSearchAddress}
                                className={styles.searchAddressBtn}
                            >
                                <Search size={13} /> 주소 검색
                            </button>
                        </div>
                        <div className={styles.inputWrapper} style={{ cursor: 'pointer' }} onClick={handleSearchAddress}>
                            <MapPin size={18} className={styles.inputIcon} />
                            <input
                                type="text"
                                value={shippingInfo.address}
                                readOnly
                                className={styles.input}
                                placeholder="주소 검색 버튼을 눌러주세요"
                                style={{ cursor: 'pointer', backgroundColor: '#fafafa' }}
                            />
                        </div>
                    </div>

                    <div className={styles.inputGroup}>
                        <label className={styles.label}>상세 주소</label>
                        <div className={styles.inputWrapper}>
                            <MapPin size={18} className={styles.inputIcon} />
                            <input
                                type="text"
                                id="detailAddress"
                                value={shippingInfo.detailAddress}
                                onChange={(e) => {
                                    setUseDefaultAddress(false);
                                    setShippingInfo((prev: any) => ({ ...prev, detailAddress: e.target.value }));
                                }}
                                className={styles.input}
                                placeholder="상세 주소를 입력하세요 (동, 호수 등)"
                            />
                        </div>
                    </div>
                </div>
            </div>

            {/* 요청사항 */}
            <div className={styles.formGroup}>
                <h3>요청사항 📝</h3>
                <textarea
                    className={styles.textarea}
                    placeholder="배송 요청사항을 입력해주세요 (예: 문 앞에 놓아주세요)"
                    value={requestMemo}
                    onChange={(e) => setRequestMemo(e.target.value)}
                    maxLength={500}
                />
            </div>
        </div>
    );
}
