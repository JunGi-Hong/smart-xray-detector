import React from "react"
import Navbar from "./Navbar"
import EventTable from "./EventTable"
import { mockEvents } from "../data/mockEvents"
import Pagination from "./Pagination"
import '../style/event-history.css'

export default function History() {
    /*
    제공받는 데이터: 현재 페이지, 전달받는 이벤트 수, 총 이벤트 수, 총 페이지 수, 해당하는 이벤트들
    -> pageInfo 를 object객체의 state값으로 처리 가능
    ex)
    const [pageInfo, setPageInfo] = useState({
    page: 1,
    size: 10,
    totalElements: 0,
    totalPages: 0
  })
    api 연동하는 부분 구현 필요
    */
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
    )

    return (
        <>
            <Navbar />
            <div className="event-page">
                <EventTable events={currentEvents} />
                <Pagination
                    currentPage={currentPage}
                    totalPages={totalPages}
                    onPageChange={setCurrentPage}
                />
            </div>
        </>
    )
}