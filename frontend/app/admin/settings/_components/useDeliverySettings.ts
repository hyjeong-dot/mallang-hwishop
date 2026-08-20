import { useState, useEffect } from 'react';
import { fetchAPI } from '@/lib/api';

interface DeliverySettings {
    basicFee: number;
    jejuExtraFee: number;
}

export default function useDeliverySettings() {
    const [settings, setSettings] = useState<DeliverySettings | null>(null);
    const [isLoading, setIsLoading] = useState(true);

    const fetchSettings = async () => {
        try {
            const data = await fetchAPI('/admin/delivery/settings');
            setSettings(data);
        } catch (error) {
            console.error('Failed to fetch delivery settings:', error);
        } finally {
            setIsLoading(false);
        }
    };

    const updateSettings = async (newSettings: DeliverySettings) => {
        const data = await fetchAPI('/admin/delivery/settings', {
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
