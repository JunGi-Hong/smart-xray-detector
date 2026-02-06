import { useNavigate } from "react-router-dom"
import Navbar from "./Navbar"
import '../style/edit-profile.css'

export default function EditProfile() {
    const navigate = useNavigate()

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
                        <input type="text" defaultValue="홍길동" />
                    </div>
                    <div className="input-field">
                        <label>이메일</label>
                        <input type="text" defaultValue="ID@email.com" readOnly />
                    </div>
                    <div className="input-field">
                        <label>전화번호</label>
                        <input type="text" defaultValue="01012345678" />
                    </div>
                    <div className="input-field">
                        <label>비밀번호</label>
                        <input type="password" defaultValue="ajdhskdfhksjhfd" />
                    </div>
                    <div className="input-field">
                        <label>비밀번호 확인</label>
                        <input type="password" defaultValue="ajdhskdfhksjhfd" />
                    </div>
                    <button type='submit' className="save-button">저장</button>
                    <button onClick={handleCancel} className="cancel-button">취소</button>
                </form>
            </main>
        </div>
    )
}