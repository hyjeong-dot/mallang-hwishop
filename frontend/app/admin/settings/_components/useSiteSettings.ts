import { useState, useEffect } from 'react';
import { fetchAPI } from '@/lib/api';

interface SiteSettings {
    basicFee: number;
    jejuExtraFee: number;
    instagramUrl: string;
    cancelTimeoutMinutes: number;
}

export default function useSiteSettings() {
    const [settings, setSettings] = useState<SiteSettings | null>(null);
    const [isLoading, setIsLoading] = useState(true);

    const fetchSettings = async () => {
        try {
            const data = await fetchAPI('/admin/settings');
            setSettings(data);
        } catch (error) {
            console.error('Failed to fetch site settings:', error);
        } finally {
            setIsLoading(false);
        }
    };

    const updateSettings = async (newSettings: SiteSettings) => {
        const data = await fetchAPI('/admin/settings', {
            method: 'PUT',
            body: JSON.stringify(newSettings)
        });
        setSettings(data);
        return data;
    };

    useEffect(() => {
        fetchSettings();
    }, []);

    return {
        settings,
        isLoading,
        updateSettings
    };
}
