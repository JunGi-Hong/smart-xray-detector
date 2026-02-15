import { useNavigate } from "react-router-dom"
import Navbar from "./Navbar"
import '../style/edit-profile.css'
import { useUser } from '../contexts/UserContext';

export default function EditProfile() {
    const navigate = useNavigate()
    const user = useUser()

    const handleSumbit = (e) => {
        e.preventDefault()
        //TODO: send edited user info to backend server
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
                        <input type="text" defaultValue={user.name} />
                    </div>
                    <div className="input-field">
                        <label>이메일</label>
                        <input type="text" defaultValue={user.email} readOnly />
                    </div>
                    <div className="input-field">
                        <label>전화번호</label>
                        <input type="text" defaultValue={user.tel} />
                    </div>
                    <div className="input-field">
                        <div className="password">
                            <label>비밀번호</label>
                            <text>*수정 사항 없으면 비워두세요.</text>
                        </div>
                        <input type="password" />
                    </div>
                    <div className="input-field">
                        <label>비밀번호 확인</label>
                        <input type="password" />
                    </div>
                    <button type='submit' className="save-button">저장</button>
                    <button onClick={handleCancel} className="cancel-button">취소</button>
                </form>
            </main>
        </div>
    )
}