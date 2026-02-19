import '../style/modal.css'
import React from 'react'

export default function ReportDateModal({ onClose }) {

    //최근 7일이면 A, 최근 30일이면 B
    const [selectedPeriod, setSelectedPeriod] = React.useState('A')

    //type A/B 전달
    const handleSave = async (e) => {
        e.preventDefault()
        /*try {
            const response = fetch('/report/', {
                method: 'POST',
                headers: {
                    'Content-type': 'application/json'
                },
                body: {
                    'type': selectedPeriod
                }
            })
            if (response.ok) {
                alert('successful')
            }
        }
        catch (error) {
            alert('error')
        }
        finally {
            onClose()
        } */

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
                                value='A'
                                checked={selectedPeriod === 'A'}
                                onChange={(e) => setSelectedPeriod(e.target.value)}
                            />
                            최근 7일
                        </label>
                        <label>
                            <input
                                type='radio'
                                value='B'
                                checked={selectedPeriod === 'B'}
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