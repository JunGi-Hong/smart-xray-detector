import { useNavigate } from "react-router-dom"

export default function EventTable({ events }) {
    const navigate = useNavigate()

    const handleBodyClick = (eventID) => {
        navigate(`/history/detail/${eventID}`)
    }

    //click single event in table -> navigate to details page -> API call to get data
    return (
        <table className="event-table">
            <thead>
                <tr>
                    <th>Event id</th>
                    <th>시간</th>
                    <th>제목</th>
                    <th>이름</th>
                </tr>
            </thead>
            <tbody >
                {events.map(
                    (event) => (
                        //onclick->navigate to history/event-id and get event info
                        <tr key={event['event-id']}
                            onClick={() => handleBodyClick(event['event-id'])}
                        >
                            <td>{event['event-id']}</td>
                            <td>{event['start-time']}</td>
                            <td>{event.title}</td>
                            <td>{event['user-name']}</td>
                        </tr>
                    )
                )}
            </tbody>
        </table>
    )
}