import React from 'react'
import LoginHeader from './LoginHeader'
import { Link, useNavigate } from 'react-router-dom';



export default function Login() {
    const navigate = useNavigate();

    const handleLogin = (e) => {
        e.preventDefault();
        // ❗ 임시: 로그인 성공했다고 가정
        navigate('/dashboard');
    }

    const handleKakaoLogin = (e) => {
        // 카카오 로그인 로직
        e.preventDefault();
        // ❗ 임시: 로그인 성공했다고 가정
        navigate('/dashboard');
    }

    return (
        <div className='login-page'>
            <main>
                <LoginHeader />
                <div className='login-wrapper'>
                    <div className='login-box'>
                        <form onSubmit={handleLogin}>
                            <input
                                type='email'
                                className='login-input'
                                placeholder='이메일'
                                aria-label='email'
                            />
                            <input
                                type='password'
                                className='login-input'
                                placeholder='비밀번호'
                                aria-label='password'
                            />
                            <button
                                type='submit'
                                className='login-button'
                            >로그인</button>
                        </form>
                        <button
                            type='button'
                            className='kakao-login-button'
                            onClick={handleKakaoLogin}
                        >카카오 로그인</button>
                        <Link to='/user/register' className="register">
                            회원가입
                        </Link>
                    </div>

                </div>
            </main>
        </div>
    )
}

