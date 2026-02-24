import { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import Navbar from './Navbar'
import '../style/event-detail.css'

export default function EventDetail() {

    const mockData = {
        "src": "202601201231221.png", // 실제로는 이미지 URL
        "start-time": "202601201220",
        "detect": "라이터, 칼, 배터리, 가스통" // 문자열을 배열로 변환하여 사용
    }

    //get param from react router url...
    const { eventID } = useParams()
    const navigate = useNavigate()
    const [eventData, setEventData] = useState(mockData)

    console.log(eventID)

    const fetchEventData = async (e) => {
        try {
            //event id 에 맞는 data fetch해오기
            const response = await fetch(`/board/detail/${eventID}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
            })
            if (!response.ok) {
                throw new Error(`서버 에러: ${response.status}`);
            }
            const data = await response.json()
            setEventData(data)
        } catch (error) {
            alert('error')
            console.error('failed to lad data')
        } finally {
            setIsLoading(false)
        }
    }

    useEffect(() => {
        fetchEventData()
    }, [eventID])

    if (!eventData) return <div className="loading">데이터를 불러오는 중...</div>;

    const formatTime = (timeStr) => {
        if (!timeStr) return '';
        return `${timeStr.slice(0, 4)}-${timeStr.slice(4, 6)}-${timeStr.slice(6, 8)} / ${timeStr.slice(8, 10)}:${timeStr.slice(10, 12)}`;
    };

    //if (isLoading) return <div className="loading">로딩 중...</div>;
    if (!eventData) return <div className="loading">데이터를 찾을 수 없습니다.</div>;

    return (
        //get event id param from url -> use useEffect to fetch data from API
        <>
            <Navbar />
            <div className="detail-container">
                <div className="detail-card">
                    <h2 className="detail-title">
                        위해물품 탐지 - {formatTime(eventData["start-time"])}
                    </h2>

                    <div className="detail-content">
                        {/* 왼쪽: 이미지 영역 */}
                        <div className="image-section">
                            <img
                                src={`/images/${eventData.src}`}
                                alt="X-ray Scan"
                            //onError={(e) => e.target.src = 'https://via.placeholder.com/500x300?text=X-ray+Image'}
                            />
                        </div>

                        {/* 오른쪽: 리스트 영역 */}
                        <div className="list-section">
                            <h3>위험물 목록:</h3>
                            <ul>
                                {eventData.detect.split(',').map((item, index) => (
                                    <li key={index}>{item.trim()}</li>
                                ))}
                            </ul>
                        </div>
                    </div>
                </div>

                {/* 하단 버튼 바 */}
                <div className="button-bar">
                    <button className="btn-back" onClick={() => navigate('/dashboard')}>이전</button>
                </div>
            </div>
        </>
    )
}