import { Navigate, Outlet } from 'react-router-dom';

const ProtectedRoute = ({ isAuthenticated }) => {
    if (!isAuthenticated) {
        // 인증되지 않은 경우 로그인 페이지로 이동시키고, 
        // replace 옵션을 통해 뒤로가기 시 무한 루프에 빠지는 것을 방지합니다.
        return <Navigate to="user/login" replace />;
    }

    // 인증된 경우 하위 라우트 컴포넌트들을 렌더링합니다.
    return <Outlet />;
};

export default ProtectedRoute;