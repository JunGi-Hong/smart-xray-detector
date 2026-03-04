import React from 'react'
import Navbar from './Navbar'
import UploadBox from './UploadBox'
import PieChart from './PieChart'
import 'react-datepicker/dist/react-datepicker.css';
import ReportDateModal from './ReportDateModal'

import '../style/dashboard.css'

export default function Dashboard() {
    //팝업창 열림 여부 상태
    const [isModalOpen, setIsModalOpen] = React.useState(false)
    return (
        <div className='dashboard-page'>
            <Navbar />
            <main className='dashboard-main'>
                <UploadBox />

                <aside className="stats-section">
                    <p>일주일간 탐지내역</p>
                    <div className="pie-chart-box">
                        <PieChart />
                    </div>

                    <button
                        className="download-btn"
                        onClick={() => { setIsModalOpen(true) }}
                    >
                        통계 리포트 다운로드
                    </button>
                    {//state에 따른 조건부 렌더링
                        isModalOpen && <ReportDateModal onClose={() => setIsModalOpen(false)} />}

                </aside>
            </main>
        </div>
    )
}