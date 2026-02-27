import React, { useEffect } from "react"
import Navbar from "./Navbar"
import EventTable from "./EventTable"
//import { mockEvents } from "../data/mockEvents"
import { useSearchParams } from "react-router-dom"
import Pagination from "./Pagination"
import '../style/event-history.css'

export default function History() {
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
                const response = await fetch(`/board/${currentPage}`, {
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