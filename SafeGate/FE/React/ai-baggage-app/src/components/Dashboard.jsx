import React from 'react'
import Navbar from './Navbar'
import UploadBox from './UploadBox'
import PieChart from './PieChart'
import '../dashboard.css'

export default function Dashboard() {
    const [username, setUsername] = React.useState('이름')
    return (
        <div className='dashboard-page'>
            <Navbar name={username} />
            <main className='dashboard-main'>
                <UploadBox />

                <aside className="stats-section">
                    <p>일주일간 탐지내역</p>
                    <div className="pie-chart-box">
                        <PieChart />
                    </div>

                    <button className="download-btn">
                        통계 리포트 다운로드
                    </button>
                </aside>
            </main>
        </div>
    )
}