import '../style/modal.css'
import DatePicker from 'react-datepicker'
import React from 'react'

export default function ReportDateModal({ onClose }) {
    const [startDate, setStartDate] = React.useState(new Date());
    const [endDate, setEndDate] = React.useState(new Date());

    const handleSave = (e) => {
        e.preventDefault()
        //TODO:통계 리포트 기간 보내기
        console.log(startDate)
        console.log(endDate)
        onClose()
    }
    return (
        <div className="modal-overlay">
            <div className="modal">
                <h2>기간 설정</h2>
                <div className='date-field'>
                    <label>시작 날짜</label>
                    <DatePicker
                        selected={startDate}
                        onChange={date => setStartDate(date)}
                        dateFormat="yyyy.MM.dd"
                    />
                </div>
                <div className='date-field'>
                    <label>종료 날짜</label>
                    <DatePicker
                        selected={endDate}
                        onChange={date => setEndDate(date)}
                        dateFormat="yyyy.MM.dd"
                    />
                </div>
                <div className='buttons'>
                    <button className='save' onClick={handleSave}>저장</button>
                    <button className="cancel" onClick={onClose}>취소</button>
                </div>
            </div>
        </div>
    )

}