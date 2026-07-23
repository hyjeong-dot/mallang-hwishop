// Product Image Type
export interface ProductImage {
    id: string;
    url: string;
    isPrimary: boolean;
    sortOrder: number;
}

// Product Option Item Type
export interface OptionItem {
    id: string;
    name: string;
    priceDelta: number; // 추가 가격 (0이면 무료)
}

// Product Option Type
export interface ProductOption {
    id: string;
    name: string;
    type: 'radio' | 'checkbox';
    required: boolean;
    items: OptionItem[];
}

// Product Category Type
export interface ProductCategory {
    id: string;
    korName: string;
    engName: string;
    icon?: string;
    sortOrder: number;
}

// Product Type
export interface Product {
    id: string;
    korName: string;
    engName: string;
    description: string;
    price: number;
    category: ProductCategory;
    images: ProductImage[];
    isAvailable: boolean;
    isSoldOut: boolean;
    sortOrder: number;
    options: ProductOption[];
    createdAt: Date;
    updatedAt: Date;
}

// Product Status for filtering
export type ProductStatus = 'all' | 'available' | 'soldOut' | 'hidden';
