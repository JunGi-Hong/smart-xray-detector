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

        /*try {
            const response = await fetch('/user/verify-password', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    //data to verify user
                    password: password
                })
            })
            if (response.ok) {
                navigate('/edit-profile')
            } else {
                console.log(response.message)
                alert('잘못된 비밀번호. 다시 입력하세요.')
            }

        }
        catch (error) {
            console.error('Verifiy password error')
            alert('오류 발생')
        } */
        const success = true

        if (success) {
            navigate('/user/edit-profile')
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