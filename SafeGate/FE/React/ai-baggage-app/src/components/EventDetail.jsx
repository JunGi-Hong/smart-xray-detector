import { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import Navbar from './Navbar'
import '../style/event-detail.css'
import { ITEM_MAP } from '../data/ItemID'

export default function EventDetail() {

    //get param from react router url...
    const { eventID } = useParams()
    const navigate = useNavigate()
    const [eventData, setEventData] = useState(null)

    console.log(eventID)

    const fetchEventData = async (e) => {
        try {
            //event id 에 맞는 data fetch해오기
            const accessToken = localStorage.getItem('accessToken')
            const response = await fetch(`/board/detail/${eventID}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${accessToken}`
                },
            })

            if (!response.ok) {
                throw new Error(`서버 에러: ${response.status}`);
            }
            const data = await response.json()
            console.log(data)
            setEventData(data)
        } catch (error) {
            alert('error')
            console.error('failed to load data')
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
                                {/* detect가 배열인지 확인 후 바로 map 실행 */}
                                {/*각각의 코드들을 탐지 물품에 매핑해줘야함.*/}
                                {Array.isArray(eventData.detect) && eventData.detect.length > 0 ? (
                                    eventData.detect.map((id, index) => (
                                        // 1. 중복 키 방지: 같은 물품이 2개 이상 탐지될 경우 id가 중복되어 리액트가 경고를 냅니다.
                                        // id와 index를 조합하거나 index를 사용하여 고유한 key를 부여하세요.
                                        <li key={`${id}-${index}`}>
                                            {/* 2. 매핑 결과 없을 때 대비: ITEM_MAP에 없는 ID(예: 37)가 올 경우를 대비해 기본값을 둡니다. */}
                                            {ITEM_MAP[Number(id)] || `미분류 물품(코드:${id})`}
                                        </li>
                                    ))
                                ) : (
                                    <li>탐지된 물품이 없습니다.</li>
                                )}
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