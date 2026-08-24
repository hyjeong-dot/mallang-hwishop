'use client';

import { useState, useRef } from 'react';
import { Plus, Pencil, Trash2, X, Check, Upload, Image as ImageIcon } from 'lucide-react';
import { usePopups, Popup } from './_components/usePopups';
import styles from './page.module.css';
import { getImageSrc } from '@/lib/api';

export default function PopupsPage() {
    const { popups, createPopup, updatePopup, deletePopup, togglePopupActive } = usePopups();

    // 모달 상태
    const [modalOpen, setModalOpen] = useState(false);
    const [editingId, setEditingId] = useState<number | null>(null);
    const [inputTitle, setInputTitle] = useState('');
    const [inputLinkUrl, setInputLinkUrl] = useState('');
    const [inputIsActive, setInputIsActive] = useState(true);
    const [inputImageUrl, setInputImageUrl] = useState('');
    const [uploadFile, setUploadFile] = useState<File | null>(null);
    const [isUploading, setIsUploading] = useState(false);

    const fileInputRef = useRef<HTMLInputElement>(null);

    const openCreateModal = () => {
        setEditingId(null);
        setInputTitle('');
        setInputLinkUrl('');
        setInputImageUrl('');
        setInputIsActive(true);
        setUploadFile(null);
        setModalOpen(true);
    };

    const openEditModal = (popup: Popup) => {
        setEditingId(popup.id);
        setInputTitle(popup.title);
        setInputLinkUrl(popup.linkUrl || '');
        setInputImageUrl(popup.imageUrl);
        setInputIsActive(popup.isActive);
        setUploadFile(null);
        setModalOpen(true);
    };

    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files && e.target.files[0]) {
            const file = e.target.files[0];
            setUploadFile(file);
            setInputImageUrl(URL.createObjectURL(file)); // 미리보기용 임시 URL
        }
    };

    const handleSave = async () => {
        if (!inputTitle.trim()) {
            alert('제목을 입력해주세요.');
            return;
        }
        if (!inputImageUrl && !uploadFile) {
            alert('이미지를 등록해주세요.');
            return;
        }

        setIsUploading(true);
        try {
            let finalImageUrl = inputImageUrl;

            // 파일이 새로 선택된 경우 업로드 수행
            if (uploadFile) {
                const uploadFormData = new FormData();
                uploadFormData.append('file', uploadFile);
                const uploadResponse = await fetch('/api/upload-file', {
                    method: 'POST',
                    body: uploadFormData,
                });
                
                if (uploadResponse.ok) {
                    const uploadData = await uploadResponse.json();
                    finalImageUrl = uploadData.url;
                } else {
                    alert('이미지 업로드에 실패했습니다.');
                    setIsUploading(false);
                    return;
                }
            }

            if (editingId) {
                await updatePopup(editingId, inputTitle.trim(), finalImageUrl, inputLinkUrl.trim(), inputIsActive);
            } else {
                await createPopup(inputTitle.trim(), finalImageUrl, inputLinkUrl.trim(), inputIsActive);
            }
            setModalOpen(false);
        } catch (e) {
            console.error(e);
            alert('저장 중 오류가 발생했습니다.');
        } finally {
            setIsUploading(false);
        }
    };

    const handleDelete = async (id: number, title: string) => {
        if (!confirm(`"${title}" 팝업을 삭제하시겠습니까?`)) return;
        try {
            await deletePopup(id);
        } catch (e) {
            console.error(e);
        }
    };

    const handleToggleActive = async (id: number, currentActive: boolean) => {
        try {
            await togglePopupActive(id, !currentActive);
        } catch (e) {
            console.error(e);
        }
    };

    return (
        <main className={styles.container}>
            <div className={styles.header}>
                <div>
                    <h1 className={styles.title}>공지 팝업 관리</h1>
                    <p className={styles.subtitle}>메인 페이지에 노출될 팝업을 등록하고 활성화 상태를 관리할 수 있습니다.</p>
                </div>
                <button className={styles.addButton} onClick={openCreateModal}>
                    <Plus size={16} />
                    새 팝업 추가
                </button>
            </div>

            <div className={styles.list}>
                {popups.length === 0 ? (
                    <div className={styles.emptyState}>
                        <span className={styles.emptyIcon}>📢</span>
                        <p>등록된 팝업이 없습니다.</p>
                        <button className={styles.addButton} onClick={openCreateModal}>
                            <Plus size={16} /> 첫 팝업 추가
                        </button>
                    </div>
                ) : (
                    popups.map((popup) => (
                        <div key={popup.id} className={`${styles.categoryItem} ${!popup.isActive ? styles.inactive : ''}`} style={{ cursor: 'default' }}>
                            <div className={styles.popupImagePreview} style={{ marginRight: '16px', display: 'flex', alignItems: 'center' }}>
                                {popup.imageUrl ? (
                                    <img src={getImageSrc(popup.imageUrl)} alt={popup.title} style={{ width: '40px', height: '40px', objectFit: 'cover', borderRadius: '4px' }} />
                                ) : (
                                    <ImageIcon size={24} color="#ccc" />
                                )}
                            </div>
                            <span className={styles.catName} style={{ flex: 1 }}>{popup.title}</span>
                            <div style={{ marginRight: '16px' }}>
                                <label style={{ display: 'flex', alignItems: 'center', cursor: 'pointer', fontSize: '14px', color: popup.isActive ? 'var(--primary-color)' : '#999' }}>
                                    <input 
                                        type="checkbox" 
                                        checked={popup.isActive} 
                                        onChange={() => handleToggleActive(popup.id, popup.isActive)}
                                        style={{ marginRight: '6px' }}
                                    />
                                    {popup.isActive ? '활성' : '비활성'}
                                </label>
                            </div>
                            <div className={styles.catActions}>
                                <button className={styles.editBtn} onClick={() => openEditModal(popup)} title="수정">
                                    <Pencil size={14} />
                                </button>
                                <button className={styles.deleteBtn} onClick={() => handleDelete(popup.id, popup.title)} title="삭제">
                                    <Trash2 size={14} />
                                </button>
                            </div>
                        </div>
                    ))
                )}
            </div>

            {/* 모달 */}
            {modalOpen && (
                <div className={styles.modalOverlay} onClick={() => setModalOpen(false)}>
                    <div className={styles.modal} onClick={(e) => e.stopPropagation()} style={{ width: '500px' }}>
                        <div className={styles.modalHeader}>
                            <h3>{editingId ? '팝업 수정' : '새 팝업 추가'}</h3>
                            <button className={styles.modalClose} onClick={() => setModalOpen(false)}>
                                <X size={18} />
                            </button>
                        </div>
                        <div className={styles.modalBody}>
                            
                            <label className={styles.modalLabel}>이미지 등록</label>
                            <div 
                                style={{ 
                                    border: '1px dashed #ccc', 
                                    borderRadius: '8px', 
                                    padding: '20px', 
                                    textAlign: 'center', 
                                    cursor: 'pointer',
                                    marginBottom: '16px',
                                    position: 'relative'
                                }}
                                onClick={() => fileInputRef.current?.click()}
                            >
                                {inputImageUrl ? (
                                    <img src={uploadFile ? inputImageUrl : getImageSrc(inputImageUrl)} alt="preview" style={{ maxWidth: '100%', maxHeight: '200px', objectFit: 'contain' }} />
                                ) : (
                                    <div style={{ color: '#888', display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '8px' }}>
                                        <Upload size={24} />
                                        <span>클릭하여 이미지 업로드 (추천 비율 3:4)</span>
                                    </div>
                                )}
                                <input 
                                    type="file" 
                                    ref={fileInputRef} 
                                    onChange={handleFileChange} 
                                    accept="image/*" 
                                    style={{ display: 'none' }} 
                                />
                            </div>

                            <label className={styles.modalLabel}>제목 (내부 관리용)</label>
                            <input
                                className={styles.modalInput}
                                value={inputTitle}
                                onChange={(e) => setInputTitle(e.target.value)}
                                placeholder="예: 신년맞이 세일 안내"
                            />
                            
                            <label className={styles.modalLabel}>링크 URL (선택)</label>
                            <input
                                className={styles.modalInput}
                                value={inputLinkUrl}
                                onChange={(e) => setInputLinkUrl(e.target.value)}
                                placeholder="예: /products/123 또는 https://..."
                            />

                            <label style={{ display: 'flex', alignItems: 'center', cursor: 'pointer', marginTop: '16px' }}>
                                <input 
                                    type="checkbox" 
                                    checked={inputIsActive} 
                                    onChange={(e) => setInputIsActive(e.target.checked)}
                                    style={{ marginRight: '8px', width: '18px', height: '18px' }}
                                />
                                바로 활성화하기 (메인 페이지에 노출됩니다)
                            </label>

                        </div>
                        <div className={styles.modalFooter}>
                            <button className={styles.cancelBtn} onClick={() => setModalOpen(false)}>취소</button>
                            <button className={styles.saveBtn} onClick={handleSave} disabled={isUploading || !inputTitle.trim()}>
                                {isUploading ? '업로드 중...' : <><Check size={14} /> {editingId ? '수정' : '추가'}</>}
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </main>
    );
}
