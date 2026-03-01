import React from 'react'
import { useNavigate } from "react-router-dom"
import Navbar from "./Navbar"
import '../style/edit-profile.css'

export default function EditProfile() {
    const navigate = useNavigate()

    // 1. 에러 방지: 데이터가 없으면 빈 문자열/객체로 처리합니다.
    const userInfoString = localStorage.getItem('userInfo')
    const userInfoJson = userInfoString ? JSON.parse(userInfoString) : {}

    const [userInfo, setUserInfo] = React.useState({
        'name': userInfoJson.name || '',
        'new-pw': '',
        'new-pw2': '',
        'tel': userInfoJson.tel || ''
    })

    const handleChange = (e) => {
        const { name, value } = e.target
        setUserInfo({
            ...userInfo,
            [name]: value
        })
    }

    const handleSubmit = async (e) => {
        e.preventDefault()

        // 비밀번호 일치 여부 확인 로직 추가
        if (userInfo['new-pw'] !== userInfo['new-pw2']) {
            alert('새 비밀번호가 일치하지 않습니다.')
            return
        }

        try {
            const accessToken = localStorage.getItem('accessToken')

            // 2. PUT 요청은 저장 버튼을 눌렀을 때(여기서) 실행되도록 옮겼습니다.
            const response = await fetch('/user/edit-profile', {
                method: 'PUT', // REST API 규칙상 수정은 PUT 또는 PATCH를 주로 씁니다.
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${accessToken}`
                },
                body: JSON.stringify({
                    name: userInfo.name,
                    "new-pw": userInfo['new-pw'],
                    "new-pw2": userInfo['new-pw2'],
                    tel: userInfo.tel
                })
            })

            if (response.ok) {
                alert('프로필이 성공적으로 수정되었습니다.')
                // 수정한 정보를 로컬 스토리지에도 다시 덮어씌워 줍니다.
                const updatedUserInfo = { ...userInfoJson, name: userInfo.name, tel: userInfo.tel }
                localStorage.setItem('userInfo', JSON.stringify(updatedUserInfo))
                localStorage.setItem('username', updatedUserInfo.name)

                navigate('/dashboard')
            } else {
                const errorData = await response.json()
                alert(`수정 실패: ${errorData.message || '알 수 없는 오류'}`)
            }
        } catch (error) {
            console.error('Edit profile error:', error)
            alert('서버와 통신 중 문제가 발생했습니다.')
        }
    }

    const handleCancel = (e) => {
        e.preventDefault()
        navigate('/dashboard')
    }

    return (
        <div className="edit-profile-page">
            <Navbar />
            <main className="edit-profile-box">
                <form onSubmit={handleSubmit}>
                    <div className="input-field">
                        <label>이름</label>
                        <input
                            type="text"
                            name="name"
                            value={userInfo.name}
                            onChange={handleChange}
                        />
                        {/* value가 있으므로 defaultValue는 삭제했습니다. */}
                    </div>

                    <div className="input-field">
                        <label>이메일</label>
                        <input
                            type="text"
                            name="email"
                            value={userInfoJson.email || ''} // 없는 변수 user 대신 userInfoJson 사용
                            readOnly
                        />
                    </div>

                    <div className="input-field">
                        <label>전화번호</label>
                        <input
                            type="text"
                            name="tel"
                            value={userInfo.tel}
                            onChange={handleChange}
                        />
                    </div>

                    <div className="input-field">
                        <div className="password">
                            <label>비밀번호</label>
                            {/* <text>는 HTML 표준 태그가 아니므로 <span>으로 변경했습니다. */}
                            <span>*수정 사항 없으면 비워두세요.</span>
                        </div>
                        <input
                            type="password"
                            name="new-pw"
                            value={userInfo['new-pw']}
                            onChange={handleChange}
                        />
                    </div>

                    <div className="input-field">
                        <label>비밀번호 확인</label>
                        <input
                            type="password"
                            name="new-pw2"
                            value={userInfo['new-pw2']}
                            onChange={handleChange}
                        />
                    </div>

                    <button type="submit" className="save-button">저장</button>
                    {/* 취소 버튼이 폼을 제출하지 않도록 type="button"을 꼭 넣어줍니다. */}
                    <button type="button" onClick={handleCancel} className="cancel-button">취소</button>
                </form>
            </main>
        </div>
    )
}