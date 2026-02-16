import React, { useEffect } from "react"
import Navbar from "./Navbar"
import EventTable from "./EventTable"
import { mockEvents } from "../data/mockEvents"
import { useSearchParams } from "react-router-dom"
import Pagination from "./Pagination"
import '../style/event-history.css'

export default function History() {
    //나중에 수정..
    /**********************************************************************************************/
    /*
    //현재 페이지
    const [currentPage, setCurrentPage] = React.useState(1)
    //전달받는 이벤트 수
    const EVENTS_PER_PAGE = 10
    //총 페이지 수
    const totalPages = Math.ceil(mockEvents.length / EVENTS_PER_PAGE)

    //전달받는 이벤트들, 나중에는 백엔드가 전달해주기 때문에 상태로 저장만 하면 된다.
    const startIndex = (currentPage - 1) * EVENTS_PER_PAGE
    const currentEvents = mockEvents.slice(
        startIndex,
        startIndex + EVENTS_PER_PAGE
    )*/
    /**********************************************************************************************/

    const [eventData, setEventData] = React.useState([])
    const [pageInfo, setPageInfo] = React.useState(null)
    const [searchParams, setSearchParams] = useSearchParams()
    const currentPage = searchParams.get('page') || 1

    //url 변경
    const handlePageChange = (page) => {
        setSearchParams({ page: page })
    }

    //처음 마운트 될 때 API 호출
    useEffect(() => {
        const fetchData = async (e) => {
            try {
                const token = localStorage.getItem('accessToken')
                console.log("현재 토큰 값:", token);
                console.log("보낼 헤더 값:", `Bearer ${token}`);
                const response = await fetch('/board/1', {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${token}`
                    }
                })
                if (!response.ok) {
                    throw new Error('데이터를 불러오는데 실패했습니다.');
                }
                const result = await response.json()
                setEventData(result.data)
                setPageInfo(result.pageInfo)
                //전달받은 데이터 확인
                console.log(result.data)
                console.log(result.pageInfo)
            } catch (error) {
                console.error("Error fetching history:", error);
            } finally {
                console.log('완료')
            }
        }
        fetchData()
    }, [currentPage])//currentPage가 바뀔 때마다 실행(url 주소가 바뀌면서 컴포넌트가 re-render됨)


    if (!pageInfo) {
        return (
            <>
                <Navbar />
                <div style={{ padding: '20px', textAlign: 'center' }}>
                    loading...
                </div>
            </>
        );
    }

    return (
        <>
            <Navbar />
            <div className="event-page">
                <EventTable events={eventData} />
                <Pagination
                    currentPage={pageInfo.page}
                    totalPages={pageInfo.totalPages}
                    onPageChange={handlePageChange}
                />
            </div>
        </>
    )
}