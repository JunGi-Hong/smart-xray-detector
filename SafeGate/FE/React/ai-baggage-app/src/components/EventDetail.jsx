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

    //if (isLoading) return <div className="loading">로딩 중...</div>;
    if (!eventData) return <div className="loading">데이터를 찾을 수 없습니다.</div>;

    return (
        //get event id param from url -> use useEffect to fetch data from API
        <>
            <Navbar />
            <div className="detail-container">
                <div className="detail-card">
                    <h2 className="detail-title">
                        위해물품 탐지 - {(eventData["start-time"])}
                    </h2>

                    <div className="detail-content">
                        {/* 왼쪽: 이미지 영역 */}
                        <div className="image-section">
                            <img
                                src={eventData.src}
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
                                    // 1. 중복된 ID의 개수를 객체 형태로 셉니다. (예: { "1": 2, "3": 1 })
                                    Object.entries(
                                        eventData.detect.reduce((acc, id) => {
                                            acc[id] = (acc[id] || 0) + 1;
                                            return acc;
                                        }, {})
                                    ).map(([id, count], index) => (
                                        // 2. 센 결과를 화면에 출력합니다.
                                        <li key={`${id}-${index}`}>
                                            {ITEM_MAP[Number(id)] || `미분류 물품(코드:${id})`}
                                            {count > 1 && ` (x${count})`} {/* 개수가 2개 이상일 때만 수량 표시 */}
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