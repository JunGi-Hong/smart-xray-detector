import React from 'react'
import LoginHeader from './LoginHeader'
import { Link, useNavigate } from 'react-router-dom';



export default function Login({ setIsAuthenticated }) {
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
        e.preventDefault()

        if (!loginData.email || !loginData.password) {
            alert('이메일과 비밀번호를 모두 입력해주세요.')
            return
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
            })

            const data = await response.json()

            if (response.ok) {
                //username,access token, refresh token local storage에 저장
                localStorage.setItem('username', data.username);
                localStorage.setItem('accessToken', data['access token']);
                localStorage.setItem('refreshToken', data['refresh token']);

                console.log('토큰 저장 완료:', localStorage.getItem('accessToken'));

                const userResponse = await fetch('/user/me', {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${data['access token']}`,
                        'Content-Type': 'application/json',
                    }
                })

                if (userResponse.ok) {
                    const userData = await userResponse.json();
                    localStorage.setItem('userInfo', JSON.stringify(userData));
                    console.log(userData)
                    alert('로그인되었습니다.')
                    setIsAuthenticated(true)
                    navigate('/dashboard') // 대시보드로 이동
                }
                else {
                    alert(`${data.message}(userResponse 오류)`);
                }

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

        const CLIENT_ID = '927b620abcb396eb1a9a65ea207c6dc3'; // 아까 말씀하신 REST API 키

        // 주의: 이 부분은 프론트엔드가 인가 코드를 받을 주소로 설정해야 합니다. (예: React 서버가 3000번 포트일 경우)
        const REDIRECT_URI = 'http://localhost:5173/dashboard';

        // 2. 카카오 인증 URL 만들기
        const kakaoAuthUrl = `https://kauth.kakao.com/oauth/authorize?client_id=${CLIENT_ID}&redirect_uri=${REDIRECT_URI}&response_type=code`;

        // 3. 현재 창을 카카오 로그인 페이지로 이동시키기
        window.location.href = kakaoAuthUrl;
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

