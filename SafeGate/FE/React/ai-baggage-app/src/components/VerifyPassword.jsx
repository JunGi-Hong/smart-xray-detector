import React from 'react'
import { useNavigate } from 'react-router-dom'
import Navbar from './Navbar'
import '../style/edit-profile.css'

export default function VerifyPassword() {
    const navigate = useNavigate()
    const [password, setPassword] = React.useState('')

    const handleChange = (e) => {
        setPassword(e.target.value)
    }

    const handleSubmit = async (e) => {
        e.preventDefault()

        if (localStorage.getItem('userInfo') === null) {
            alert('카카오로 로그인한 유저는 회원 정보 수정 불가')
            navigate('/dashboard')
            return
        }

        const userInfoString = localStorage.getItem('userInfo')
        //카카오로 로그인 한 경우 해당 로컬 스토리지 데이터가 없다 -> 핸들링 필요
        const accessToken = localStorage.getItem('accessToken')

        try {
            const userInfo = JSON.parse(userInfoString)
            const response = await fetch('/user/verify-password', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${accessToken}`
                },
                body: JSON.stringify({
                    email: userInfo.email,
                    password: password
                })
            })
            if (response.ok) {
                navigate('/user/edit-profile')
            } else {
                console.log(response.message)
                alert('잘못된 비밀번호.')
            }

        }
        catch (error) {
            console.error('Verifiy password error')
            alert('오류 발생')
        }
    }
    return (
        <div>
            <Navbar />
            <main className='password-page'>
                <div className='password-box'>
                    <label>비밀번호 입력</label>
                    <form>
                        <input
                            type='password'
                            value={password}
                            onChange={handleChange}
                            placeholder='비밀번호' />
                        <button onClick={handleSubmit}>비밀번호 확인</button>
                    </form>
                </div>
            </main>
        </div>
    )
}