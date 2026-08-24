import { useState, useEffect } from 'react';
import { fetchAPI } from '@/lib/api';

export interface Popup {
    id: number;
    title: string;
    imageUrl: string;
    linkUrl?: string;
    isActive: boolean;
    createdAt: string;
    updatedAt: string;
}

export function usePopups() {
    const [popups, setPopups] = useState<Popup[]>([]);
    const [isLoading, setIsLoading] = useState(true);

    const fetchPopups = async () => {
        try {
            setIsLoading(true);
            const data = await fetchAPI('/admin/popups');
            if (data) {
                setPopups(data);
            }
        } catch (e) {
            console.error('Failed to fetch popups:', e);
        } finally {
            setIsLoading(false);
        }
    };

    useEffect(() => {
        fetchPopups();
    }, []);

    const createPopup = async (title: string, imageUrl: string, linkUrl?: string, isActive: boolean = false) => {
        const newPopup = await fetchAPI('/admin/popups', {
            method: 'POST',
            body: JSON.stringify({ title, imageUrl, linkUrl, isActive }),
        });
        if (newPopup) {
            setPopups([newPopup, ...popups]);
        }
    };

    const updatePopup = async (id: number, title: string, imageUrl: string, linkUrl?: string, isActive: boolean = false) => {
        const updatedPopup = await fetchAPI(`/admin/popups/${id}`, {
            method: 'PUT',
            body: JSON.stringify({ title, imageUrl, linkUrl, isActive }),
        });
        if (updatedPopup) {
            setPopups(popups.map(p => p.id === id ? updatedPopup : p));
        }
    };

    const deletePopup = async (id: number) => {
        await fetchAPI(`/admin/popups/${id}`, {
            method: 'DELETE',
        });
        setPopups(popups.filter(p => p.id !== id));
    };

    const togglePopupActive = async (id: number, isActive: boolean) => {
        await fetchAPI(`/admin/popups/${id}/active?isActive=${isActive}`, {
            method: 'PATCH',
        });
        setPopups(popups.map(p => p.id === id ? { ...p, isActive } : p));
    };

    return {
        popups,
        isLoading,
        createPopup,
        updatePopup,
        deletePopup,
        togglePopupActive,
        refresh: fetchPopups
    };
}
