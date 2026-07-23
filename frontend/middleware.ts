import { NextResponse } from 'next/server';
import type { NextRequest } from 'next/server';

export function middleware(request: NextRequest) {
    const sessionCookie = request.cookies.get('ncafe_session');
    const path = request.nextUrl.pathname;

    // 보호해야 할 경로
    const isProtectedRoute = path.startsWith('/admin') || path.startsWith('/cart');

    // 로그인하지 않은 사용자가 보호된 경로에 접근하려고 할 때
    if (isProtectedRoute && !sessionCookie) {
        const loginUrl = new URL('/login', request.url);
        loginUrl.searchParams.set('redirect', path);
        return NextResponse.redirect(loginUrl);
    }

    // 로그인한 사용자가 로그인 페이지에 접근하려고 할 때
    // (클라이언트 AuthContext나 서버 컴포넌트에서 더 정확한 검증 후 리다이렉트 하는 것이 안전합니다)

    return NextResponse.next();
}

export const config = {
    matcher: ['/admin/:path*', '/cart', '/login'],
};
