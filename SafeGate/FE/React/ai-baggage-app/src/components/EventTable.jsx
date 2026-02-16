
export default function EventTable({ events }) {
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
            <tbody>
                {events.map(
                    (event) => (
                        <tr key={event['event-id']}>
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