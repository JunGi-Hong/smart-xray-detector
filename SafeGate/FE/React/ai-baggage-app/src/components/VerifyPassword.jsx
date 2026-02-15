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

    const handleSubmit = (e) => {
        e.preventDefault()
        //TODO: verify password
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