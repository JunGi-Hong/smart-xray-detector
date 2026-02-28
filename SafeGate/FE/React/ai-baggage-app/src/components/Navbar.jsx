import { Link, useNavigate } from 'react-router-dom';
import '../style/navbar.css'
import ClientIcon from '../assets/client.png'
import LogoutIcon from '../assets/logout.png'
import HistoryIcon from '../assets/history.png'
import { useUser } from '../contexts/UserContext'

export default function Navbar() {
    const navigate = useNavigate()
    const user = useUser()
    const username = localStorage.getItem('username')

    const handleLogout = async (e) => {
        //로그 아웃 구현

        try {
            const token = localStorage.getItem('accessToken')
            if (token) {
                await fetch('/user/logout', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${token}`
                    }
                })
            }

        } catch (error) {
            console.error("로그 아웃 중 서버 에러 발생(무시하고 진행)")
        } finally {
            localStorage.clear()
            alert('로그아웃 되었습니다.')
            navigate('/user/login')
        }

    }


    return (
        <header className='main-header'>
            <div className='header-left'><strong>{username}</strong>{'님'}</div>

            <nav className='header-right'>
                <Link to='/history?page=1' className='nav-item'>
                    <img src={HistoryIcon} className='client-icon' />
                    탐지 이력
                </Link>
                <Link to='/user/verify-password' className='nav-item'>
                    <img src={ClientIcon} className='client-icon' />
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