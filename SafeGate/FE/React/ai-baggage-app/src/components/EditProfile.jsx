import React from 'react'
import { useNavigate } from "react-router-dom"
import Navbar from "./Navbar"
import '../style/edit-profile.css'
import { useUser } from '../contexts/UserContext';

export default function EditProfile() {
    const navigate = useNavigate()
    const user = useUser()//temporary user data

    const [userInfo, setUserInfo] = React.useState({
        'name': '',
        'email': '',
        'new-pw': '',
        'new-pw2': '',
        'tel': ''
    })

    const handleChange = (e) => {
        const { name, value } = e.target

        setUserInfo({
            ...userInfo,
            [name]: value
        })
    }

    const fetchUserInfo = async (e) => {
        try {
            const response = await fetch('/user/edit-profile', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                },
            })
            if (!response.ok) {
                throw new Error('데이터를 불러오는데 실패했습니다.');
            }
            const result = await response.json()
            setUserInfo({
                ...userInfo,
                'name': result.name,
                'new-pw': result['new-pw'],
                'new-pw2': result['new-pw2'],
                'tel': result.tel,
            })
        } catch (error) {
            alert('error')
        }
    }

    React.useEffect(() => {
        //load user info when first mounted
        fetchUserInfo()
    }, [])

    const handleSumbit = async (e) => {
        e.preventDefault()
        //TODO: send edited user info to backend server
        //비밀번호 일치 여부 확인??
        /*try {
            const response = await fetch('/user/edit-profile', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: {
                    'name': userInfo.name,
                    'new-pw': userInfo['new-pw'],
                    'new-pw2': userInfo['new-pw2'],
                    'tel': userInfo.tel,
                    'access-token': localStorage.getItem('accessToken')
                }
            })
            if (response.ok) {
                alert('successful')
                navigate('/dashboard')
            }
        }
        catch (error) {
            console.error('fail')
        }*/

        navigate('/dashboard')
    }

    const handleCancel = (e) => {
        e.preventDefault()
        //TODO: return to dashboard
        navigate('/dashboard')
    }
    return (
        <div className="edit-profile-page">
            <Navbar />
            <main className="edit-profile-box">
                <form onSubmit={handleSumbit}>
                    {/*TODO: get user context from database(save it as user context state)
                     and display it on the input field*/}
                    <div className="input-field">
                        <label>이름</label>
                        <input
                            type="text"
                            name="name"
                            value={userInfo.name}
                            onChange={handleChange}
                            //change to userInfo.name later
                            defaultValue={user.name} />
                    </div>
                    <div className="input-field">
                        <label>이메일</label>
                        <input
                            type="text"
                            name="name"
                            //change to userInfo.email later
                            defaultValue={user.email} readOnly />
                    </div>
                    <div className="input-field">
                        <label>전화번호</label>
                        <input
                            type="text"
                            name="tel"
                            value={userInfo.tel}
                            onChange={handleChange}
                            defaultValue={user.tel} />
                    </div>
                    <div className="input-field">
                        <div className="password">
                            <label>비밀번호</label>
                            <text>*수정 사항 없으면 비워두세요.</text>
                        </div>
                        <input
                            type="password"
                            name="new-pw"
                            onChange={handleChange}
                        />
                    </div>
                    <div className="input-field">
                        <label>비밀번호 확인</label>
                        <input
                            type="password"
                            name="new-pw2"
                            onChange={handleChange} />
                    </div>
                    <button type='submit' className="save-button">저장</button>
                    <button onClick={handleCancel} className="cancel-button">취소</button>
                </form>
            </main>
        </div>
    )
}