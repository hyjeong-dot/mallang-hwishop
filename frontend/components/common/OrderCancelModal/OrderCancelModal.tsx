'use client';

import { useEffect, useState } from 'react';
import { createPortal } from 'react-dom';
import modalStyles from '@/components/common/Modal/Modal.module.css';
import styles from './OrderCancelModal.module.css';

interface CancelReasonOption {
    value: string;
    label: string;
}

interface OrderCancelModalProps {
    isOpen: boolean;
    onClose: () => void;
    onConfirm: (reasonType: string, reasonText: string) => void;
    title?: string;
    description?: string;
    variant?: 'default' | 'danger' | 'ditto';
    options?: CancelReasonOption[];
    allowCustomReason?: boolean;
}

export default function OrderCancelModal({
    isOpen,
    onClose,
    onConfirm,
    title = '주문 취소',
    description = '주문을 취소하시겠습니까? 취소 사유를 선택해주세요.',
    variant = 'danger',
    options = [],
    allowCustomReason = false
}: OrderCancelModalProps) {
    const [mounted, setMounted] = useState(false);
    const [reasonType, setReasonType] = useState(options.length > 0 ? options[0].value : '');
    const [reasonText, setReasonText] = useState('');

    useEffect(() => {
        setMounted(true);
        if (isOpen) {
            document.body.style.overflow = 'hidden';
            setReasonType(options.length > 0 ? options[0].value : '');
            setReasonText('');

            const handleKeyDown = (e: KeyboardEvent) => {
                if (e.key === 'Escape') onClose();
            };
            document.addEventListener('keydown', handleKeyDown);
            return () => {
                document.body.style.overflow = 'unset';
                document.removeEventListener('keydown', handleKeyDown);
            };
        } else {
            document.body.style.overflow = 'unset';
        }
    }, [isOpen, onClose]);

    if (!mounted || !isOpen) return null;

    const handleSubmit = () => {
        onConfirm(reasonType, reasonText);
    };

    return createPortal(
        <div className={modalStyles.overlay} onClick={onClose}>
            <div className={modalStyles.modal} onClick={(e) => e.stopPropagation()}>
                <div className={modalStyles.header}>
                    <h2 className={modalStyles.title}>{title}</h2>
                    {description && <p className={modalStyles.description}>{description}</p>}
                </div>

                <div className={styles.body}>
                    <label className={styles.label}>취소 사유 선택</label>
                    <div className={styles.radioGroup}>
                        {options.map(reason => (
                            <label key={reason.value} className={`${styles.radioItem} ${reasonType === reason.value ? styles.radioItemActive : ''}`}>
                                <input
                                    type="radio"
                                    name="cancelReason"
                                    value={reason.value}
                                    checked={reasonType === reason.value}
                                    onChange={(e) => setReasonType(e.target.value)}
                                    className={styles.radioInput}
                                />
                                <span className={styles.radioLabel}>{reason.label}</span>
                            </label>
                        ))}
                    </div>

                    {reasonType === 'OTHER' && allowCustomReason && (
                        <div className={styles.inputGroup}>
                            <label className={styles.label}>상세 사유</label>
                            <textarea
                                className={styles.textarea}
                                value={reasonText}
                                onChange={(e) => setReasonText(e.target.value)}
                                placeholder="취소 사유를 상세히 적어주세요."
                                maxLength={200}
                            />
                        </div>
                    )}
                </div>

                <div className={modalStyles.footer}>
                    <button className={`${modalStyles.button} ${modalStyles.cancelButton}`} onClick={onClose}>
                        닫기
                    </button>
                    <button
                        className={`${modalStyles.button} ${
                            variant === 'danger'
                                ? modalStyles.dangerButton
                                : variant === 'ditto'
                                    ? modalStyles.dittoButton
                                    : modalStyles.confirmButton
                        }`}
                        onClick={handleSubmit}
                        disabled={allowCustomReason && reasonType === 'OTHER' && reasonText.trim().length === 0}
                    >
                        취소 요청
                    </button>
                </div>
            </div>
        </div>,
        document.body
    );
}
