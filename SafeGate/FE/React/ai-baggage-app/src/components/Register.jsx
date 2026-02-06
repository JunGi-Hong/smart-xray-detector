import React from 'react';
import '../style/register.css';
import './LoginHeader'
import LoginHeader from './LoginHeader';
import { useNavigate } from 'react-router-dom';

export default function Register() {

    const navigate = useNavigate();

    const handleRegister = (e) => {
        e.preventDefault()
        // 회원가입 처리
        navigate('/user/login')
    };

    return (
        <div className="register-page">
            {/* Header */}
            <LoginHeader />

            {/* Main */}
            <main className="register-main">
                <form className="register-box" onSubmit={handleRegister}>
                    <div className="input-field">
                        <label>이름</label>
                        <input type="text" placeholder="홍길동" />
                    </div>

                    <div className="input-field">
                        <label>이메일</label>
                        <input type="email" placeholder="ID@example.com" />
                    </div>

                    <div className="input-field">
                        <label>전화번호</label>
                        <input type="tel" placeholder="ex) 01012345678" />
                    </div>

                    <div className="input-field">
                        <label>비밀번호</label>
                        <input type="password" placeholder="비밀번호" />
                    </div>

                    <div className="input-field">
                        <label>비밀번호 확인</label>
                        <input type="password" placeholder="비밀번호 확인" />
                    </div>

                    <button type="submit" className="register-button">
                        회원가입
                    </button>
                </form>
            </main>
        </div>
    );
}
