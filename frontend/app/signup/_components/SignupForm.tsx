'use client';

import { useState, FormEvent, useMemo } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';
import Image from 'next/image';
import { UserPlus, User, Lock, Eye, EyeOff, AlertCircle, Mail, Phone, Smile, CheckSquare, Square, Check, X, MapPin, Building, CreditCard, Search } from 'lucide-react';
import { useAuth } from '@/context/AuthContext';
import toast from 'react-hot-toast';
import styles from '@/app/login/login.module.css';

// 비밀번호 강도 계산
function getPasswordStrength(pw: string) {
    let score = 0;
    if (pw.length >= 6) score++;
    if (pw.length >= 8) score++;
    if (/[a-zA-Z]/.test(pw)) score++;
    if (/\d/.test(pw)) score++;
    if (/[!@#$%^&*(),.?":{}|<>]/.test(pw)) score++;

    if (score <= 1) return { label: '약함', color: '#fc8181', percent: 20 };
    if (score <= 2) return { label: '보통', color: '#f6ad55', percent: 40 };
    if (score <= 3) return { label: '보통', color: '#ecc94b', percent: 60 };
    if (score <= 4) return { label: '강함', color: '#48bb78', percent: 80 };
    return { label: '매우 강함', color: '#38a169', percent: 100 };
}

// 검증 힌트 컴포넌트
function ValidationHints({ hints }: { hints: { label: string; pass: boolean }[] }) {
    return (
        <div className={styles.validationHints}>
            {hints.map((h, i) => (
                <span key={i} className={`${styles.hintItem} ${h.pass ? styles.hintPass : styles.hintFail}`}>
                    {h.pass ? <Check size={12} /> : <X size={12} />}
                    {h.label}
                </span>
            ))}
        </div>
    );
}

export default function SignupForm() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [phoneNumber, setPhoneNumber] = useState('');
    
    // 추가정보 (주소 및 환불계좌)
    const [zipcode, setZipcode] = useState('');
    const [address, setAddress] = useState('');
    const [detailAddress, setDetailAddress] = useState('');
    const [refundBank, setRefundBank] = useState('');
    const [refundAccount, setRefundAccount] = useState('');
    const [refundHolder, setRefundHolder] = useState('');

    const [showPassword, setShowPassword] = useState(false);
    const [termsAgreed, setTermsAgreed] = useState(false);
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const { login } = useAuth();

    // 각 필드 touched 상태 (포커스 후 blur 했는지)
    const [touched, setTouched] = useState<Record<string, boolean>>({});
    const router = useRouter();

    const markTouched = (field: string) => setTouched(prev => ({ ...prev, [field]: true }));

    // ─── 실시간 검증 ───
    const usernameValid = username.trim().length >= 3;
    const usernameHints = [
        { label: '3자 이상', pass: username.trim().length >= 3 },
    ];

    const passwordHints = [
        { label: '6자 이상', pass: password.length >= 6 },
        { label: '영문 포함', pass: /[a-zA-Z]/.test(password) },
        { label: '숫자 포함', pass: /\d/.test(password) },
    ];
    const passwordValid = passwordHints.every(h => h.pass);
    const strength = useMemo(() => getPasswordStrength(password), [password]);

    const confirmHints = [
        { label: '비밀번호 일치', pass: confirmPassword.length > 0 && password === confirmPassword },
    ];
    const confirmValid = confirmPassword.length > 0 && password === confirmPassword;

    const nameValid = name.trim().length >= 2;
    const nameHints = [
        { label: '2자 이상', pass: name.trim().length >= 2 },
    ];

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const emailValid = emailRegex.test(email);
    const emailHints = [
        { label: '올바른 이메일 형식', pass: emailValid },
    ];

    const phoneRegex = /^010-\d{4}-\d{4}$/;
    const phoneValid = phoneRegex.test(phoneNumber);
    const phoneHints = [
        { label: '010-0000-0000 형식', pass: phoneValid },
    ];

    // 입력 상태 클래스
    const getFieldClass = (field: string, isValid: boolean, value: string) => {
        if (!touched[field] || value.length === 0) return '';
        return isValid ? styles.inputValid : styles.inputInvalid;
    };

    const handlePhoneChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const value = e.target.value.replace(/[^0-9]/g, '');
        let formattedValue = '';
        if (value.length < 4) formattedValue = value;
        else if (value.length < 8) formattedValue = `${value.slice(0, 3)}-${value.slice(3)}`;
        else formattedValue = `${value.slice(0, 3)}-${value.slice(3, 7)}-${value.slice(7, 11)}`;
        setPhoneNumber(formattedValue);
    };

    const handleSearchAddress = () => {
        if (typeof window !== 'undefined' && (window as any).daum?.Postcode) {
            new (window as any).daum.Postcode({
                oncomplete: (data: any) => {
                    const zonecode = data.zonecode;
                    const fullAddr = data.roadAddress || data.jibunAddress;
                    setZipcode(zonecode);
                    setAddress(`(${zonecode}) ${fullAddr}`);
                    document.getElementById('detailAddress')?.focus();
                }
            }).open();
        } else {
            toast.error('주소 검색 서비스를 로드하는 중입니다. 잠시 후 다시 시도해 주세요.');
        }
    };

    const addressValid = address.trim().length > 0;
    const refundValid = refundBank.trim().length > 0 && refundAccount.trim().length > 0 && refundHolder.trim().length > 0;

    const allValid = usernameValid && passwordValid && confirmValid && nameValid && emailValid && phoneValid && addressValid && refundValid && termsAgreed;

    const handleSubmit = async (e: FormEvent) => {
        e.preventDefault();
        setError('');

        // 모든 필수 필드 touched 처리
        setTouched({ username: true, password: true, confirmPassword: true, name: true, email: true, phoneNumber: true, address: true, refundBank: true, refundAccount: true, refundHolder: true });

        if (!allValid) {
            if (!termsAgreed) setError('이용약관 및 개인정보 처리방침에 동의해주세요.');
            else if (!addressValid) setError('주소를 입력해주세요. (주소 검색 버튼을 눌러주세요)');
            else if (!refundValid) setError('환불 계좌 정보를 모두 입력해주세요.');
            else setError('입력 항목을 다시 확인해주세요.');
            return;
        }

        setIsLoading(true);

        try {
            // 1) 회원가입
            const signupRes = await fetch('/api/auth/signup', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ 
                    username, password, name, email, phoneNumber,
                    zipcode, address, detailAddress,
                    refundBank, refundAccount, refundHolder
                }),
            });

            if (!signupRes.ok) {
                const data = await signupRes.json();
                setError(data.message || '회원가입에 실패했습니다.');
                setIsLoading(false);
                return;
            }

            // 2) 자동 로그인
            const loginRes = await fetch('/api/auth/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, password }),
            });

            const loginResult = await loginRes.json();

            if (loginRes.ok && loginResult.success) {
                login(loginResult.data);
                toast.success('회원가입 완료! 환영해요 🎀');
                window.location.href = '/';
            } else {
                // 가입은 됐지만 로그인 실패 시 로그인 페이지로
                toast.success('회원가입 완료! 로그인해주세요 🎀');
                router.push('/login');
            }
        } catch (err) {
            setError('서버에 연결할 수 없습니다. 잠시 후 다시 시도해주세요.');
            setIsLoading(false);
        }
    };

    return (
        <div className={`${styles.card} ${styles.signupCard}`}>
            <div className={styles.header}>
                <h1 className={styles.title}>
                    <div className={styles.titleIcon}>
                        <UserPlus size={24} color="#FF8BA7" />
                    </div>
                    <span>회원가입</span>
                </h1>
                <div className={styles.subtitle}>
                    말랑이와 친구 맺기 🎀
                </div>
            </div>

            <div className={styles.body}>
                <form onSubmit={handleSubmit} className={styles.form}>
                    {error && (
                        <div className={styles.errorMessage}>
                            <AlertCircle size={16} />
                            <span>{error}</span>
                        </div>
                    )}

                    <div className={styles.signupGrid}>
                        {/* 1. 계정 정보 */}
                        <div className={styles.sectionTitle}>
                            <span>🔑 계정 정보</span>
                        </div>

                        {/* 아이디 */}
                        <div className={styles.inputGroup}>
                            <label htmlFor="username" className={styles.label}>아이디</label>
                            <div className={`${styles.inputWrapper} ${getFieldClass('username', usernameValid, username)}`}>
                                <User size={18} className={styles.inputIcon} />
                                <input
                                    type="text"
                                    id="username"
                                    value={username}
                                    onChange={(e) => setUsername(e.target.value)}
                                    onBlur={() => markTouched('username')}
                                    className={styles.input}
                                    placeholder="사용할 아이디"
                                    required
                                    autoComplete="off"
                                />
                            </div>
                            {touched.username && username.length > 0 && <ValidationHints hints={usernameHints} />}
                        </div>

                        {/* 이름 */}
                        <div className={styles.inputGroup}>
                            <label htmlFor="name" className={styles.label}>이름</label>
                            <div className={`${styles.inputWrapper} ${getFieldClass('name', nameValid, name)}`}>
                                <Smile size={18} className={styles.inputIcon} />
                                <input
                                    type="text"
                                    id="name"
                                    value={name}
                                    onChange={(e) => setName(e.target.value)}
                                    onBlur={() => markTouched('name')}
                                    className={styles.input}
                                    placeholder="이름 입력"
                                    required
                                />
                            </div>
                            {touched.name && name.length > 0 && <ValidationHints hints={nameHints} />}
                        </div>

                        {/* 비밀번호 */}
                        <div className={styles.inputGroup}>
                            <label htmlFor="password" className={styles.label}>비밀번호</label>
                            <div className={`${styles.inputWrapper} ${getFieldClass('password', passwordValid, password)}`}>
                                <Lock size={18} className={styles.inputIcon} />
                                <input
                                    type={showPassword ? 'text' : 'password'}
                                    id="password"
                                    value={password}
                                    onChange={(e) => setPassword(e.target.value)}
                                    onBlur={() => markTouched('password')}
                                    className={styles.input}
                                    placeholder="비밀번호"
                                    required
                                />
                                <button
                                    type="button"
                                    onClick={() => setShowPassword(!showPassword)}
                                    className={styles.togglePassword}
                                    tabIndex={-1}
                                >
                                    {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
                                </button>
                            </div>
                            {password.length > 0 && (
                                <>
                                    <div className={styles.strengthBar}>
                                        <div className={styles.strengthTrack}>
                                            <div
                                                className={styles.strengthFill}
                                                style={{ width: `${strength.percent}%`, backgroundColor: strength.color }}
                                            />
                                        </div>
                                        <span className={styles.strengthLabel} style={{ color: strength.color }}>
                                            {strength.label}
                                        </span>
                                    </div>
                                    <ValidationHints hints={passwordHints} />
                                </>
                            )}
                        </div>

                        {/* 비밀번호 확인 */}
                        <div className={styles.inputGroup}>
                            <label htmlFor="confirmPassword" className={styles.label}>비밀번호 확인</label>
                            <div className={`${styles.inputWrapper} ${getFieldClass('confirmPassword', confirmValid, confirmPassword)}`}>
                                <Lock size={18} className={styles.inputIcon} />
                                <input
                                    type={showPassword ? 'text' : 'password'}
                                    id="confirmPassword"
                                    value={confirmPassword}
                                    onChange={(e) => setConfirmPassword(e.target.value)}
                                    onBlur={() => markTouched('confirmPassword')}
                                    className={styles.input}
                                    placeholder="비밀번호 재입력"
                                    required
                                />
                            </div>
                            {touched.confirmPassword && confirmPassword.length > 0 && <ValidationHints hints={confirmHints} />}
                        </div>

                        {/* 2. 연락처 & 배송지 주소 */}
                        <div className={styles.sectionTitle}>
                            <span>📱 연락처 & 배송지 주소</span>
                        </div>

                        {/* 이메일 */}
                        <div className={styles.inputGroup}>
                            <label htmlFor="email" className={styles.label}>이메일</label>
                            <div className={`${styles.inputWrapper} ${getFieldClass('email', emailValid, email)}`}>
                                <Mail size={18} className={styles.inputIcon} />
                                <input
                                    type="email"
                                    id="email"
                                    value={email}
                                    onChange={(e) => setEmail(e.target.value)}
                                    onBlur={() => markTouched('email')}
                                    className={styles.input}
                                    placeholder="example@ncafe.com"
                                    required
                                />
                            </div>
                            {touched.email && email.length > 0 && <ValidationHints hints={emailHints} />}
                        </div>

                        {/* 휴대폰 번호 */}
                        <div className={styles.inputGroup}>
                            <label htmlFor="phoneNumber" className={styles.label}>휴대폰 번호</label>
                            <div className={`${styles.inputWrapper} ${getFieldClass('phoneNumber', phoneValid, phoneNumber)}`}>
                                <Phone size={18} className={styles.inputIcon} />
                                <input
                                    type="tel"
                                    id="phoneNumber"
                                    value={phoneNumber}
                                    onChange={(e) => { handlePhoneChange(e); markTouched('phoneNumber'); }}
                                    onBlur={() => markTouched('phoneNumber')}
                                    className={styles.input}
                                    placeholder="010-0000-0000"
                                    maxLength={13}
                                    required
                                />
                            </div>
                            {touched.phoneNumber && phoneNumber.length > 0 && <ValidationHints hints={phoneHints} />}
                        </div>

                        {/* 기본 주소 */}
                        <div className={`${styles.inputGroup} ${styles.fullWidth}`}>
                            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '4px' }}>
                                <label htmlFor="address" className={styles.label} style={{ margin: 0 }}>기본 주소</label>
                                <button
                                    type="button"
                                    onClick={handleSearchAddress}
                                    style={{
                                        padding: '4px 12px',
                                        background: 'var(--color-primary-600, #FF8BA7)',
                                        color: '#fff',
                                        border: 'none',
                                        borderRadius: '8px',
                                        fontSize: '12px',
                                        fontWeight: '700',
                                        cursor: 'pointer',
                                        display: 'flex',
                                        alignItems: 'center',
                                        gap: '4px',
                                        boxShadow: '0 2px 6px rgba(255, 139, 167, 0.3)'
                                    }}
                                >
                                    <Search size={13} /> 주소 검색
                                </button>
                            </div>
                            <div className={styles.inputWrapper} style={{ cursor: 'pointer' }} onClick={handleSearchAddress}>
                                <MapPin size={18} className={styles.inputIcon} />
                                <input
                                    type="text"
                                    id="address"
                                    value={address}
                                    readOnly
                                    className={styles.input}
                                    placeholder="주소 검색 버튼을 눌러주세요"
                                    style={{ cursor: 'pointer', backgroundColor: '#fafafa' }}
                                />
                            </div>
                        </div>

                        {/* 상세 주소 */}
                        <div className={`${styles.inputGroup} ${styles.fullWidth}`}>
                            <label htmlFor="detailAddress" className={styles.label}>상세 주소</label>
                            <div className={styles.inputWrapper}>
                                <MapPin size={18} className={styles.inputIcon} />
                                <input
                                    type="text"
                                    id="detailAddress"
                                    value={detailAddress}
                                    onChange={(e) => setDetailAddress(e.target.value)}
                                    className={styles.input}
                                    placeholder="상세 주소를 입력하세요 (동, 호수 등)"
                                />
                            </div>
                        </div>

                        {/* 3. 환불 계좌 정보 */}
                        <div className={styles.sectionTitle}>
                            <span>💳 환불 계좌 정보</span>
                        </div>

                        {/* 은행명 */}
                        <div className={styles.inputGroup}>
                            <label htmlFor="refundBank" className={styles.label}>은행명</label>
                            <div className={styles.inputWrapper}>
                                <Building size={18} className={styles.inputIcon} />
                                <input
                                    type="text"
                                    id="refundBank"
                                    value={refundBank}
                                    onChange={(e) => setRefundBank(e.target.value)}
                                    className={styles.input}
                                    placeholder="예: 국민은행"
                                    required
                                />
                            </div>
                        </div>

                        {/* 예금주 */}
                        <div className={styles.inputGroup}>
                            <label htmlFor="refundHolder" className={styles.label}>예금주</label>
                            <div className={styles.inputWrapper}>
                                <User size={18} className={styles.inputIcon} />
                                <input
                                    type="text"
                                    id="refundHolder"
                                    value={refundHolder}
                                    onChange={(e) => setRefundHolder(e.target.value)}
                                    className={styles.input}
                                    placeholder="예금주 성명"
                                    required
                                />
                            </div>
                        </div>

                        {/* 계좌번호 */}
                        <div className={`${styles.inputGroup} ${styles.fullWidth}`}>
                            <label htmlFor="refundAccount" className={styles.label}>계좌번호</label>
                            <div className={styles.inputWrapper}>
                                <CreditCard size={18} className={styles.inputIcon} />
                                <input
                                    type="text"
                                    id="refundAccount"
                                    value={refundAccount}
                                    onChange={(e) => setRefundAccount(e.target.value)}
                                    className={styles.input}
                                    placeholder="- 없이 계좌번호 입력"
                                    required
                                />
                            </div>
                        </div>

                        {/* 약관 동의 */}
                        <div className={`${styles.termsGroup} ${styles.fullWidth}`}>
                            <label className={styles.termsLabel}>
                                <input
                                    type="checkbox"
                                    checked={termsAgreed}
                                    onChange={(e) => setTermsAgreed(e.target.checked)}
                                    className={styles.hiddenCheckbox}
                                />
                                <div className={styles.customCheckbox}>
                                    {termsAgreed ? <CheckSquare size={20} color="#FF8BA7" /> : <Square size={20} color="#ddd" />}
                                </div>
                                <span className={styles.termsText}>
                                    [필수] 이용약관 및 개인정보 처리방침에 동의합니다.
                                </span>
                            </label>
                        </div>

                        {/* 가입하기 버튼 */}
                        <button
                            type="submit"
                            className={`${styles.submitButton} ${styles.fullWidth}`}
                            disabled={isLoading}
                        >
                            {isLoading ? (
                                <>
                                    <Image
                                        src="/images/logo.png"
                                        alt="Loading..."
                                        width={24}
                                        height={24}
                                        className={styles.loadingDitto}
                                    />
                                    <span>가입 중... 🎀</span>
                                </>
                            ) : (
                                <>
                                    <UserPlus size={20} />
                                    가입하기 🎀
                                </>
                            )}
                        </button>
                    </div>
                </form>

                <div className={styles.signupPrompt}>
                    <span>이미 회원이신가요? 🫠</span>
                    <Link href="/login" className={styles.signupLink}>
                        로그인하러 가기 →
                    </Link>
                </div>
            </div>
        </div>
    );
}
