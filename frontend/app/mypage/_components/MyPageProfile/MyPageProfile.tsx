import { useState, useEffect } from "react";
import styles from "./MyPageProfile.module.css";
import { useAuth } from "@/context/AuthContext";
import { toast } from "react-hot-toast";
import { User, Mail, Phone, Lock, Save, AlertCircle, MapPin, Building, CreditCard } from "lucide-react";

interface UserData {
    id: string;
    username: string;
    name: string;
    email: string;
    phoneNumber: string;
    role: string;
    zipcode?: string;
    address?: string;
    detailAddress?: string;
    refundBank?: string;
    refundAccount?: string;
    refundHolder?: string;
}

export default function MyPageProfile() {
    const { updateUser } = useAuth();
    const [fullUser, setFullUser] = useState<UserData | null>(null);
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [phoneNumber, setPhoneNumber] = useState('');
    
    const [zipcode, setZipcode] = useState('');
    const [address, setAddress] = useState('');
    const [detailAddress, setDetailAddress] = useState('');
    
    const [refundBank, setRefundBank] = useState('');
    const [refundAccount, setRefundAccount] = useState('');
    const [refundHolder, setRefundHolder] = useState('');

    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [isSaving, setIsSaving] = useState(false);

    useEffect(() => {
        const fetchUserData = async () => {
            try {
                const res = await fetch('/api/auth/me');
                const result = await res.json();
                if (result.success && result.data) {
                    const data = result.data;
                    setFullUser(data);
                    setName(data.name || '');
                    setEmail(data.email || '');
                    setPhoneNumber(data.phoneNumber || '');
                    setZipcode(data.zipcode || '');
                    setAddress(data.address || '');
                    setDetailAddress(data.detailAddress || '');
                    setRefundBank(data.refundBank || '');
                    setRefundAccount(data.refundAccount || '');
                    setRefundHolder(data.refundHolder || '');
                }
            } catch (err) {
                console.error("Failed to fetch user profile", err);
            }
        };
        fetchUserData();
    }, []);

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
                }
            }).open();
        } else {
            toast.error('주소 검색 서비스를 로드하는 중입니다. 잠시 후 다시 시도해 주세요.');
        }
    };

    const handleSave = async () => {
        // 이름 검증
        if (name.trim().length < 2) {
            toast.error("이름은 2자 이상 입력해주세요! 🎀");
            return;
        }

        // 이메일 검증
        if (email.trim()) {
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailRegex.test(email)) {
                toast.error("올바른 이메일 형식을 입력해주세요! 🎀");
                return;
            }
        }

        // 전화번호 검증
        if (phoneNumber.trim()) {
            const phoneRegex = /^010-\d{4}-\d{4}$/;
            if (!phoneRegex.test(phoneNumber)) {
                toast.error("전화번호를 올바르게 입력해주세요! (010-0000-0000) 🎀");
                return;
            }
        }

        // 비밀번호 검증 (입력한 경우만)
        if (password) {
            if (password.length < 6) {
                toast.error("비밀번호는 6자 이상이어야 해요! 🎀");
                return;
            }
            if (password !== confirmPassword) {
                toast.error("비밀번호가 일치하지 않아요! 😢");
                return;
            }
        }

        setIsSaving(true);
        try {
            const res = await fetch('/api/auth/me', {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    name,
                    email,
                    phoneNumber,
                    zipcode,
                    address,
                    detailAddress,
                    refundBank,
                    refundAccount,
                    refundHolder,
                    password: password || undefined
                })
            });

            const result = await res.json();
            if (result.success) {
                toast.success("내 정보가 귀엽게 수정되었어요! 🎀");
                updateUser(result.data);
                setPassword('');
                setConfirmPassword('');
            } else {
                toast.error(result.message || "수정에 실패했어요.");
            }
        } catch (err) {
            toast.error("서버 연결에 실패했어요.");
        } finally {
            setIsSaving(false);
        }
    };

    if (!fullUser) return <div className={styles.content}>로딩 중...</div>;

    return (
        <div className={styles.content}>
            <div className={styles.settingsForm}>
                <h3 className={styles.sectionTitle}>계정 설정</h3>

                <div className={styles.inputGroup}>
                    <label className={styles.label}>
                        <div className={styles.labelWrapper}>
                            <User size={16} />
                            <span>아이디</span>
                        </div>
                    </label>
                    <input
                        className={styles.input}
                        type="text"
                        value={fullUser.username}
                        readOnly
                        disabled
                    />
                    <div className={styles.helperText}>
                        아이디는 변경할 수 없어요.
                    </div>
                </div>

                <div className={styles.inputGroup}>
                    <label className={styles.label}>
                        <div className={styles.labelWrapper}>
                            <User size={16} />
                            <span>이름</span>
                        </div>
                    </label>
                    <input
                        className={styles.input}
                        type="text"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        placeholder="이름을 입력해주세요!"
                    />
                </div>

                <div className={styles.inputGroup}>
                    <label className={styles.label}>
                        <div className={styles.labelWrapper}>
                            <Mail size={16} />
                            <span>이메일</span>
                        </div>
                    </label>
                    <input
                        className={styles.input}
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        placeholder="example@ncafe.com"
                    />
                </div>

                <div className={styles.inputGroup}>
                    <label className={styles.label}>
                        <div className={styles.labelWrapper}>
                            <Phone size={16} />
                            <span>휴대폰 번호</span>
                        </div>
                    </label>
                    <input
                        className={styles.input}
                        type="tel"
                        value={phoneNumber}
                        onChange={handlePhoneChange}
                        placeholder="010-0000-0000"
                        maxLength={13}
                    />
                </div>

                <div style={{ marginTop: '20px', marginBottom: '10px', fontSize: '14px', fontWeight: 'bold', color: 'var(--gray-700)' }}>
                    배송지 주소
                </div>
                <div className={styles.inputGroup}>
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '4px' }}>
                        <label className={styles.label} style={{ margin: 0 }}>
                            <div className={styles.labelWrapper}>
                                <MapPin size={16} />
                                <span>기본 주소</span>
                            </div>
                        </label>
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
                            주소 검색
                        </button>
                    </div>
                    <input
                        className={styles.input}
                        type="text"
                        value={address}
                        readOnly
                        onClick={handleSearchAddress}
                        placeholder="주소 검색 버튼을 눌러주세요"
                        style={{ cursor: 'pointer', backgroundColor: '#fafafa' }}
                    />
                </div>
                <div className={styles.inputGroup}>
                    <label className={styles.label}>
                        <div className={styles.labelWrapper}>
                            <MapPin size={16} />
                            <span>상세 주소</span>
                        </div>
                    </label>
                    <input
                        className={styles.input}
                        type="text"
                        value={detailAddress}
                        onChange={(e) => setDetailAddress(e.target.value)}
                        placeholder="상세 주소"
                    />
                </div>

                <div style={{ marginTop: '20px', marginBottom: '10px', fontSize: '14px', fontWeight: 'bold', color: 'var(--gray-700)' }}>
                    환불 계좌 정보
                </div>
                <div className={styles.inputGroup}>
                    <label className={styles.label}>
                        <div className={styles.labelWrapper}>
                            <Building size={16} />
                            <span>은행명</span>
                        </div>
                    </label>
                    <input
                        className={styles.input}
                        type="text"
                        value={refundBank}
                        onChange={(e) => setRefundBank(e.target.value)}
                        placeholder="은행명"
                    />
                </div>
                <div className={styles.inputGroup}>
                    <label className={styles.label}>
                        <div className={styles.labelWrapper}>
                            <CreditCard size={16} />
                            <span>계좌번호</span>
                        </div>
                    </label>
                    <input
                        className={styles.input}
                        type="text"
                        value={refundAccount}
                        onChange={(e) => setRefundAccount(e.target.value)}
                        placeholder="계좌번호"
                    />
                </div>
                <div className={styles.inputGroup}>
                    <label className={styles.label}>
                        <div className={styles.labelWrapper}>
                            <User size={16} />
                            <span>예금주</span>
                        </div>
                    </label>
                    <input
                        className={styles.input}
                        type="text"
                        value={refundHolder}
                        onChange={(e) => setRefundHolder(e.target.value)}
                        placeholder="예금주"
                    />
                </div>

                <div className={styles.inputGroup}>
                    <label className={styles.label}>
                        <div className={styles.labelWrapper}>
                            <Lock size={16} />
                            <span>비밀번호 변경</span>
                        </div>
                    </label>
                    <input
                        className={styles.input}
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        placeholder="비워두면 기존 비밀번호가 유지됩니다"
                    />
                    <input
                        className={styles.input}
                        type="password"
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                        placeholder="비밀번호를 한 번 더 입력해주세요"
                        style={{ marginTop: 'var(--space-2)' }}
                    />
                </div>

                <button 
                    className={styles.saveButton}
                    onClick={handleSave}
                    disabled={isSaving}
                >
                    <div className={styles.labelWrapper}>
                        {isSaving ? (
                            <span>변경하는 중... 🎀</span>
                        ) : (
                            <>
                                <Save size={18} />
                                <span>변경내용 저장할까요? 🎀</span>
                            </>
                        )}
                    </div>
                </button>
            </div>
        </div>
    );
}
