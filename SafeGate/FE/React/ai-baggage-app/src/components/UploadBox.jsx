import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import '../style/uploadbox.css'

export default function UploadBox() {
    const navigate = useNavigate()
    const [fileName, setFileName] = useState('') //src
    const [previewUrl, setPreviewUrl] = useState(null)
    const [selectedFile, setSelectedFile] = useState(null)
    const [isUploading, setIsUploading] = useState(false)

    // 🌟 파일 선택 시 공통으로 실행되는 로직 (미리보기 생성 포함)
    const handleImageSelection = (file) => {
        if (!file) return;

        // 이미지 파일인지 검증
        if (!file.type.startsWith('image/')) {
            alert('이미지 파일만 업로드할 수 있습니다.');
            return;
        }

        setFileName(file.name);
        setSelectedFile(file);

        // FileReader를 사용하여 로컬 파일 읽기 및 미리보기 URL 생성
        const reader = new FileReader();
        reader.onloadend = () => {
            setPreviewUrl(reader.result);
        };
        reader.readAsDataURL(file);
    }

    const handleDragOver = (e) => {
        e.preventDefault()
    }

    const handleDrop = (e) => {
        e.preventDefault()//이미지를 새탭에서 여는 동작 방지
        const file = e.dataTransfer.files[0] //파일 배열의 첫번째 파일에 접근
        if (file) {
            handleImageSelection(file)
        }
    }

    const handleFileChange = (e) => {
        const file = e.target.files[0];
        handleImageSelection(file)
    }


    const handleSubmit = async (e) => {
        e.preventDefault()

        if (!selectedFile) {
            alert("이미지 파일을 먼저 선택해주세요.")
            return
        }

        const formData = new FormData()
        formData.append('image', selectedFile)

        setIsUploading(true) // 로딩 시작
        try {
            const accessToken = localStorage.getItem('accessToken')
            console
            const response = await fetch('/x-ray/image-upload', {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${accessToken}`
                },
                body: formData,
            })

            if (!response.ok) {
                const errorData = await response.json()
                throw new Error(errorData.fail || "업로드에 실패했습니다.")
            }

            const aiResult = await response.json()
            //response 처리 추가 필요
            console.log("AI 분석 완료: ", aiResult)
            // 성공 시 상세 페이지로 이동
            //const eventId = aiResult.id
            //navigate(`/history/detail/${eventId}`)

        } catch (error) {
            console.error("에러 발생:", error)
            alert(error.message)
        } finally {
            setIsUploading(false) // 로딩 종료
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
                    accept='image/*' //이미지 파일만 받는다.
                ></input>
                {previewUrl ? (
                    <div className="preview-container">
                        <img src={previewUrl} alt="X-ray 미리보기" className="image-preview" />
                        <p className="file-name">{fileName}</p>
                    </div>
                ) : (
                    <p>이미지 파일 드래그 또는 클릭하여 업로드</p>
                )}
            </label>
            <button
                onClick={handleSubmit}
                className='file-upload-button'
                disabled={isUploading}
            >
                {isUploading ? '분석 중...' : '업로드'}
            </button>
        </div>
    )
}
