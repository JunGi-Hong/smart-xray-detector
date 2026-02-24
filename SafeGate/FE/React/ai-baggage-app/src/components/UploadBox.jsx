import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import '../style/uploadbox.css'

export default function UploadBox() {
    const navigate = useNavigate()
    const [fileName, setFileName] = useState('')

    const handleDragOver = (e) => {
        e.preventDefault()
    }

    const handleDrop = (e) => {
        e.preventDefault()//이미지를 새탭에서 여는 동작 방지
        const file = e.dataTransfer.files[0] //파일 배열의 첫번째 파일에 접근
        if (file) {
            setFileName(file.name) //파일 이름 상태 변경
        }

    }

    const handleSubmit = (e) => {
        e.preventDefault()
        //이미지 파일 업로드 처리
        //탐지 되면 event id 받아야함
        //if detected -> navigate to board/details/{event-id} page 
        navigate(`/history/detail/${1}`)
    }

    const handleFileChange = (e) => {
        const file = e.target.files[0];
        if (file) {
            setFileName(file.name);
            console.log(file.name);
        }
    }

    return (
        <div className='file-upload'>
            <label
                className="file-upload-box"
                onDragOver={handleDragOver}
                onDrop={handleDrop}
            >
                <input type='file'
                    className='file'
                    onChange={handleFileChange}
                ></input>
                {fileName ? ( //파일 이름이 있으면 파일 이름 표기 -> 나중에 아미지 미리보기 추가할수도
                    <p className="file-name">{fileName}</p>
                ) : (
                    <p>이미지 파일 드래그 또는 클릭하여 업로드</p>
                )}
            </label>
            <button onClick={handleSubmit} className='file-upload-button'>업로드</button>
        </div>
    )
}
