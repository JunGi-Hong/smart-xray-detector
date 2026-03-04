import React from 'react';
import '../style/register.css';
import './LoginHeader'
import LoginHeader from './LoginHeader';
import { useNavigate } from 'react-router-dom';

export default function Register() {

    const navigate = useNavigate();

    const [registerForm, setRegisterForm] = React.useState({
        name: '',
        email: '',
        tel: '',
        password: '',
        password2: ''
    })

    //입력 내용 상태 변경
    const handleChange = (e) => {
        const { name, value } = e.target;

        setRegisterForm({
            ...registerForm,
            [name]: value
        })
    }

    // 회원가입 처리
    const handleRegister = async (e) => {
        e.preventDefault()
        try {
            const response = await fetch('/user/register', { //나중에 실제 API 주소로 
                method: 'POST',
                headers: {
                    'Content-type': 'application/json',
                },
                body: JSON.stringify({
                    name: registerForm.name,
                    email: registerForm.email,
                    tel: registerForm.tel,
                    password: registerForm.password,
                    password2: registerForm.password2
                })
            })

            if (response.ok) {
                //회원 가입 성공
                console.log('회원 가입 성공')
                navigate('/user/login')
            } else {
                const errorData = await response.json();
                alert(`회원가입 실패: ${errorData.message || '알 수 없는 오류'}`);
            }

        }
        catch (error) {
            console.error('Error:', error);
            alert('서버와 통신 중 오류가 발생했습니다.');
        }
    }

    return (
        <div className="register-page">
            {/* Header */}
            <LoginHeader />

            {/* Main */}
            <main className="register-main">
                <form className="register-box" onSubmit={handleRegister}>
                    <div className="input-field">
                        <label>이름</label>
                        <input
                            type="text"
                            name="name"
                            value={registerForm.name}
                            onChange={handleChange}
                            placeholder="홍길동" />
                    </div>

                    <div className="input-field">
                        <label>이메일</label>
                        <input
                            type="email"
                            name="email"
                            value={registerForm.email}
                            onChange={handleChange}
                            placeholder="ID@example.com" />
                    </div>

                    <div className="input-field">
                        <label>전화번호</label>
                        <input
                            type="tel"
                            name="tel"
                            value={registerForm.tel}
                            onChange={handleChange}
                            placeholder="ex) 01012345678" />
                    </div>

                    <div className="input-field">
                        <label>비밀번호</label>
                        <input
                            type="password"
                            name="password"
                            value={registerForm.password}
                            onChange={handleChange}
                            placeholder="비밀번호" />
                    </div>

                    <div className="input-field">
                        <label>비밀번호 확인</label>
                        <input
                            type="password"
                            name="password2"
                            value={registerForm.password2}
                            onChange={handleChange}
                            placeholder="비밀번호 확인" />
                    </div>

                    <button type="submit" className="register-button">
                        회원가입
                    </button>
                </form>
            </main>
        </div>
    );
}
