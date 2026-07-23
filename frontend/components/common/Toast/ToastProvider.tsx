'use client';

import { Toaster } from 'react-hot-toast';

export default function ToastProvider() {
    return (
        <Toaster
            position="top-center"
            toastOptions={{
                duration: 3000,
                style: {
                    background: '#FF8BA7', // ditto-700
                    color: '#fff',
                    borderRadius: '12px',
                },
                success: {
                    style: {
                        background: '#FFF8F0', // ditto-100
                        color: '#E67A95', // ditto-800
                        border: '1px solid #FFB5C2', // ditto-400
                    },
                    iconTheme: {
                        primary: '#FF8BA7', // ditto-600
                        secondary: 'white',
                    },
                },
                error: {
                    style: {
                        background: '#fff1f2', // soft pink
                        color: '#e11d48', // ditto danger
                        border: '1px solid #fb7185',
                    },
                    iconTheme: {
                        primary: '#e11d48',
                        secondary: 'white',
                    },
                },
            }}
        />
    );
}
