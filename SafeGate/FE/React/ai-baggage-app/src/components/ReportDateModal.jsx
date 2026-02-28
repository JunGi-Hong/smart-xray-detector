import '../style/modal.css'
import React from 'react'

export default function ReportDateModal({ onClose }) {

    //최근 7일이면 A, 최근 30일이면 B
    const [selectedPeriod, setSelectedPeriod] = React.useState('week')

    //type A/B 전달
    const handleSave = async (e) => {
        e.preventDefault()
        try {
            const accessToken = localStorage.getItem('accessToken')
            const response = await fetch(`/report?period=${selectedPeriod}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${accessToken}`
                }
            }
            )
            if (response.ok) {
                //pass
                console.log('ok')
            }

        } catch (error) {
            console.error(error)
        }
        onClose()

    }
    return (
        <div className="modal-overlay">
            <div className="modal">
                <h2>기간 설정</h2>
                <form onSubmit={handleSave}>
                    <div className='radio-group'>
                        <label>
                            <input
                                type='radio'
                                value='week'
                                checked={selectedPeriod === 'week'}
                                onChange={(e) => setSelectedPeriod(e.target.value)}
                            />
                            최근 7일
                        </label>
                        <label>
                            <input
                                type='radio'
                                value='month'
                                checked={selectedPeriod === 'month'}
                                onChange={(e) => setSelectedPeriod(e.target.value)}
                            />
                            최근 30일
                        </label>
                    </div>
                    <div className='buttons'>
                        <button type='submit' className='save' onClick={handleSave}>저장</button>
                        <button className="cancel" onClick={onClose}>취소</button>
                    </div>
                </form>
            </div>
        </div>
    )

}