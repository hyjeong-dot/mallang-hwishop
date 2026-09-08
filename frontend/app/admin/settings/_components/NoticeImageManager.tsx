"use client";

import React, { useState, useEffect } from 'react';
import {
    DndContext,
    closestCenter,
    KeyboardSensor,
    PointerSensor,
    useSensor,
    useSensors,
    DragEndEvent
} from '@dnd-kit/core';
import {
    arrayMove,
    SortableContext,
    sortableKeyboardCoordinates,
    verticalListSortingStrategy,
    useSortable
} from '@dnd-kit/sortable';
import { CSS } from '@dnd-kit/utilities';
import { fetchAPI } from '@/lib/api';
import toast from 'react-hot-toast';
import styles from './NoticeImageManager.module.css';

interface NoticeImage {
    id: number;
    url: string;
    sortOrder: number;
}

function SortableItem({ item, onRemove }: { item: NoticeImage; onRemove: (id: number) => void }) {
    const {
        attributes,
        listeners,
        setNodeRef,
        transform,
        transition,
    } = useSortable({ id: item.id.toString() });

    const style = {
        transform: CSS.Transform.toString(transform),
        transition,
    };

    return (
        <div ref={setNodeRef} style={style} className={styles.imageItem}>
            <div className={styles.dragHandle} {...attributes} {...listeners}>
                ☰
            </div>
            <img src={item.url} alt="안내사항" className={styles.imagePreview} />
            <button onClick={() => onRemove(item.id)} className={styles.deleteBtn}>삭제</button>
        </div>
    );
}

export default function NoticeImageManager() {
    const [images, setImages] = useState<NoticeImage[]>([]);
    const [isLoading, setIsLoading] = useState(true);

    const sensors = useSensors(
        useSensor(PointerSensor),
        useSensor(KeyboardSensor, {
            coordinateGetter: sortableKeyboardCoordinates,
        })
    );

    useEffect(() => {
        fetchImages();
    }, []);

    const fetchImages = async () => {
        try {
            const data = await fetchAPI('/admin/settings/notices');
            setImages(data);
        } catch (error) {
            console.error('Failed to load notice images', error);
        } finally {
            setIsLoading(false);
        }
    };

    const handleUpload = async (e: React.ChangeEvent<HTMLInputElement>) => {
        if (!e.target.files || e.target.files.length === 0) return;
        const file = e.target.files[0];

        try {
            // S3 upload first
            const formData = new FormData();
            formData.append('file', file);
            
            const uploadResponse = await fetch('/api/admin/images/upload', {
                method: 'POST',
                body: formData
            });

            if (!uploadResponse.ok) {
                throw new Error('이미지 업로드 실패');
            }

            const { url } = await uploadResponse.json();

            // Register notice image
            const newImage = await fetchAPI('/admin/settings/notices', {
                method: 'POST',
                body: JSON.stringify({ url })
            });

            setImages(prev => [...prev, newImage]);
            toast.success('이미지가 추가되었습니다.');
        } catch (error) {
            console.error('Failed to upload image', error);
            toast.error('이미지 업로드에 실패했습니다.');
        }
    };

    const handleRemove = async (id: number) => {
        if (!confirm('정말 삭제하시겠습니까?')) return;

        try {
            await fetchAPI(`/admin/settings/notices/${id}`, {
                method: 'DELETE'
            });
            setImages(prev => prev.filter(img => img.id !== id));
            toast.success('삭제되었습니다.');
        } catch (error) {
            console.error('Failed to delete image', error);
            toast.error('삭제에 실패했습니다.');
        }
    };

    const handleDragEnd = async (event: DragEndEvent) => {
        const { active, over } = event;

        if (over && active.id !== over.id) {
            const oldIndex = images.findIndex(img => img.id.toString() === active.id);
            const newIndex = images.findIndex(img => img.id.toString() === over.id);

            const newImages = arrayMove(images, oldIndex, newIndex);
            setImages(newImages);

            try {
                const orderedIds = newImages.map(img => img.id);
                await fetchAPI('/admin/settings/notices/sort', {
                    method: 'PUT',
                    body: JSON.stringify({ orderedIds })
                });
                toast.success('순서가 변경되었습니다.');
            } catch (error) {
                console.error('Failed to update sort order', error);
                toast.error('순서 변경에 실패했습니다.');
                // 롤백
                fetchImages();
            }
        }
    };

    if (isLoading) return <div>로딩 중...</div>;

    return (
        <div className={styles.container}>
            <div className={styles.header}>
                <h4>안내사항 이미지</h4>
                <p>상품 상세페이지 하단에 노출될 공통 안내사항 이미지를 관리합니다.</p>
            </div>

            <div className={styles.uploadSection}>
                <input
                    type="file"
                    id="noticeImageUpload"
                    accept="image/*"
                    onChange={handleUpload}
                    style={{ display: 'none' }}
                />
                <label htmlFor="noticeImageUpload" className={styles.uploadLabel}>
                    + 새 이미지 추가
                </label>
            </div>

            <DndContext
                sensors={sensors}
                collisionDetection={closestCenter}
                onDragEnd={handleDragEnd}
            >
                <SortableContext
                    items={images.map(img => img.id.toString())}
                    strategy={verticalListSortingStrategy}
                >
                    <div className={styles.imageList}>
                        {images.map(img => (
                            <SortableItem key={img.id} item={img} onRemove={handleRemove} />
                        ))}
                        {images.length === 0 && (
                            <div className={styles.emptyMsg}>등록된 이미지가 없습니다.</div>
                        )}
                    </div>
                </SortableContext>
            </DndContext>
        </div>
    );
}
