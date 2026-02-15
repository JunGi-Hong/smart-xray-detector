import React from 'react'
import LoginHeader from './LoginHeader'
import { Link, useNavigate } from 'react-router-dom';



export default function Login() {
    const navigate = useNavigate();
    const [loginData, setLoginData] = React.useState({
        'email': '',
        'password': ''
    })

    const handleChange = (e) => {
        const { name, value } = e.target
        setLoginData({
            ...loginData,
            [name]: value
        })
    }

    const handleLogin = async (e) => {
        e.preventDefault();

        if (!loginData.email || !loginData.password) {
            alert('이메일과 비밀번호를 모두 입력해주세요.');
            return;
        }

        try {
            const response = await fetch('/user/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    email: loginData.email,
                    password: loginData.password
                }),
            });

            const data = await response.json();

            if (response.ok) {
                //username,access token, refresh token local storage에 저장
                localStorage.setItem('username', data.username);
                localStorage.setItem('accessToken', data['access token']);
                localStorage.setItem('refreshToken', data['refresh token']);

                //저장 확인 (개발자 도구 콘솔용, 배포 시 삭제)
                console.log('토큰 저장 완료:', localStorage.getItem('accessToken'));

                alert('로그인되었습니다.');
                navigate('/dashboard'); // 대시보드로 이동
            } else {
                //로그인 실패 처리
                alert(data.message || '이메일 또는 비밀번호가 일치하지 않습니다.');
            }

        } catch (error) {
            console.error('Login Error:', error);
            alert('서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.');
        }
    }

    const handleKakaoLogin = async (e) => {
        // 카카오 로그인 로직
        e.preventDefault();
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
                                name='email'
                                value={loginData.email}
                                onChange={handleChange}
                                placeholder='이메일'
                                aria-label='email'
                            />
                            <input
                                type='password'
                                className='login-input'
                                name='password'
                                value={loginData.password}
                                onChange={handleChange}
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

