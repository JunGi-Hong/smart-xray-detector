import { Link, useNavigate } from 'react-router-dom';
import '../style/navbar.css'
import ClientIcon from '../assets/client.png'
import LogoutIcon from '../assets/logout.png'
import { useUser } from '../contexts/UserContext'

export default function Navbar() {
    const navigate = useNavigate()
    const user = useUser()

    const handleLogout = (e) => {
        //로그 아웃 구현
        //일단은 로그인 페이지로 복귀하게
        navigate('/user/login')
    }


    return (
        <header className='main-header'>
            <div className='header-left'>{user.name + ' 님'}</div>

            <nav className='header-right'>
                <Link to='/history' className='nav-item'>
                    <img src={ClientIcon} className='client-icon' />
                    탐지 이력
                </Link>
                <Link to='/user/verify-password' className='nav-item'>
                    <img src={LogoutIcon} className='client-icon' />
                    회원정보 수정
                </Link>
                <button type='button' className='nav-item logout-button' onClick={handleLogout}>
                    <img src={LogoutIcon} className='client-icon' />
                    로그 아웃
                </button>
            </nav>
        </header>
    )
}