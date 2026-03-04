import '../style/modal.css'
import React, { useState } from 'react'

export default function ReportDateModal({ onClose }) {
    const [selectedPeriod, setSelectedPeriod] = useState('week')
    // 1. 로딩 상태를 관리하는 state 추가
    const [isLoading, setIsLoading] = useState(false)

    const handleSave = async (e) => {
        e.preventDefault()

        // 2. API 요청 직전 로딩 상태를 true로 변경
        setIsLoading(true)

        try {
            const accessToken = localStorage.getItem('accessToken')
            const response = await fetch(`/report?period=${selectedPeriod}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${accessToken}`
                }
            })

            if (response.ok) {
                const blob = await response.blob()
                const url = window.URL.createObjectURL(blob)
                const a = document.createElement('a')
                a.href = url
                a.download = `report_${selectedPeriod}.pdf`
                document.body.appendChild(a)
                a.click()
                a.remove()
                window.URL.revokeObjectURL(url)
            } else {
                console.error('PDF 다운로드 실패:', response.statusText)
            }

        } catch (error) {
            console.error('네트워크 에러:', error)
        } finally {
            // 3. 통신 성공/실패 여부와 상관없이 마지막에 실행
            setIsLoading(false)
            onClose() // 다운로드 또는 에러 처리가 끝난 후 모달 닫기
        }
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
                        {/* 4. isLoading 상태에 따라 버튼 텍스트 변경 및 비활성화 처리 */}
                        <button
                            type='submit'
                            className='save'
                            disabled={isLoading}
                        >
                            {isLoading ? '로딩중...' : '저장'}
                        </button>
                        {/* 취소 버튼도 로딩 중일 때는 클릭하지 못하게 막아두면 좋습니다 */}
                        <button
                            type="button"
                            className="cancel"
                            onClick={onClose}
                            disabled={isLoading}
                        >
                            취소
                        </button>
                    </div>
                </form>
            </div>
        </div>
    )
}