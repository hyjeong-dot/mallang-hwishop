import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";
import ToastProvider from "@/components/common/Toast/ToastProvider";
import "./globals.css";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

export const metadata: Metadata = {
  title: "말랑이샵 🎀",
  description: "말랑말랑 귀여운 핸드메이드 말랑이 판매 자사몰",
  icons: {
    icon: "/images/favicon-origin.png",
  },
  openGraph: {
    images: ["/images/og-image.png"],
  },
};

import { HeaderWrapper } from "@/components/layout/HeaderWrapper";
import Footer from "@/components/layout/Footer";
import KakaoFloatingButton from "@/components/layout/KakaoFloatingButton";

import { AuthProvider } from "@/context/AuthContext";
import { CartProvider } from "@/context/CartContext";

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="ko">
      <head>
        <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js" async></script>
      </head>
      <body className={`${geistSans.variable} ${geistMono.variable}`}>
        <AuthProvider>
          <CartProvider>
            <HeaderWrapper />
            {children}
            <Footer />
            <KakaoFloatingButton />
            <ToastProvider />
          </CartProvider>
        </AuthProvider>
      </body>
    </html>
  );
}
