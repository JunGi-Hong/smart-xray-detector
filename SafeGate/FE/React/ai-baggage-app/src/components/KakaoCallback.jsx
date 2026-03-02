import React from 'react'
import { useNavigate, useSearchParams } from 'react-router-dom'

export default function KakaoCallback({ setIsAuthenticated }) {
    const [searchParams] = useSearchParams()
    const navigate = useNavigate()

    const isProcessed = React.useRef(false)

    React.useEffect(() => {
        if (isProcessed.current) return

        const code = searchParams.get('code')

        if (code) {
            isProcessed.current = true

            // 1. try-catch 블록을 비동기 함수 내부로 옮겼습니다.
            const sendCode = async () => {
                try {
                    const response = await fetch('/user/kakao-login', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        // 3. 백엔드가 받을 수 있도록 객체({ code: code }) 형태로 변환합니다.
                        body: JSON.stringify({ code: code })
                    })

                    // 2. 반드시 await를 붙여서 JSON 데이터를 완전히 파싱할 때까지 기다립니다.
                    const data = await response.json()

                    if (response.ok) {
                        localStorage.setItem('accessToken', data['access token'])
                        localStorage.setItem('username', data['username'])
                        localStorage.setItem('refreshToken', data['refresh token'])

                        setIsAuthenticated(true)
                        navigate('/dashboard', { replace: true })
                    } else {
                        alert(`카카오 로그인 실패: ${data.message || '알 수 없는 오류'}`)
                        navigate('/login', { replace: true })
                    }
                } catch (error) {
                    console.error('카카오 로그인 통신 에러:', error)
                    alert('서버와 통신 중 문제가 발생했습니다.')
                    navigate('/login', { replace: true })
                }
            }

            // 함수 호출은 여기서 실행합니다.
            sendCode()
        } else {
            alert('잘못된 접근입니다.');
            navigate('/login', { replace: true });
        }
    }, [searchParams, navigate, setIsAuthenticated])

    return (
        <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
            <h2>카카오 로그인 처리 중입니다... 잠시만 기다려주세요.</h2>
        </div>
    )
}