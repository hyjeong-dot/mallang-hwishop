'use client';

import { useState, useEffect } from 'react';
import NoticePopup from '@/components/common/NoticePopup/NoticePopup';
import { fetchAPI, getImageSrc } from '@/lib/api';

export default function MainPopups() {
    const [popups, setPopups] = useState<any[]>([]);

    useEffect(() => {
        const loadPopups = async () => {
            try {
                // public 엔드포인트이므로 /api/popups/active 로 호출 (인증 불필요)
                const data = await fetchAPI('/popups/active');
                if (data && Array.isArray(data)) {
                    setPopups(data);
                }
            } catch (error) {
                console.error('Failed to load active popups:', error);
            }
        };

        loadPopups();
    }, []);

    if (popups.length === 0) return null;

    return (
        <>
            {popups.map(popup => (
                <NoticePopup
                    key={popup.id}
                    id={popup.id.toString()}
                    imageUrl={getImageSrc(popup.imageUrl)}
                    linkUrl={popup.linkUrl}
                />
            ))}
        </>
    );
}
