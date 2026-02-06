import React from "react"
import Navbar from "./Navbar"
import EventTable from "./EventTable"
import { mockEvents } from "../data/mockEvents"
import Pagination from "./Pagination"
import '../style/event-history.css'

export default function History() {
    const EVENTS_PER_PAGE = 10
    const [currentPage, setCurrentPage] = React.useState(1)
    const totalPages = Math.ceil(mockEvents.length / EVENTS_PER_PAGE)
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