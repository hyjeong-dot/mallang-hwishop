
"use client";
import { useEffect, useState } from "react";
import CategoryList from "./_components/CategoryList";
export default function ProductsPage() {
    // fetch products
    const [products, setProducts] = useState([]);
    const [category, setCategory] = useState(null);
    console.log("ProductsPage");
    useEffect(() => {
        // /api/admin/products
        const fetchProducts = async () => {
            const url = new URL("/api/admin/products", window.location.origin);
            const params = url.searchParams;
            if (category) {
                params.set("cid", category.id);
            }
            const response = await fetch(url);
            const data = await response.json();
            // products = data;
            setProducts(data);
        };
        fetchProducts(); // 여기서 fatch 하는 것이 올바른 곳일까요?
        console.log("ProductsPage useEffect");
        return () => {
            console.log("ProductsPage useEffect cleanup");
        };
    }, [category]);

    const categoryChangeHandler = (category) => {
        console.log(category);
        setCategory(category);
    };
    return (
        <main>
            {/*
                카테고리 블록
                상품 목록 
            */}
            <CategoryList onCategoryChange={categoryChangeHandler} />
            <section>
                <h1>상품 목록</h1>
                <div>
                    {products.map((product) => (
                        <div key={product.id}>
                            <h2>{product.korName}</h2>
                            <p>{product.engName}</p>
                            <p>{product.description}</p>
                            <p>{product.price}</p>
                        </div>
                    ))}
                </div>
            </section>
        </main>
    );
}