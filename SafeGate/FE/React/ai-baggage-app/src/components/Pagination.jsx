export default function Pagination({
    currentPage,
    totalPages,
    onPageChange,
}) {
    // 총 페이지가 5개라면 [1,2,3,4,5] 배열 생성
    const pages = Array.from({ length: totalPages }, (_, i) => i + 1)
    //현재 페이지 값 숫자로 변경
    const pageNum = Number(currentPage)

    return (
        <div className="pagination">
            <button onClick={() => onPageChange(1)}>{'<<'}</button>
            <button
                onClick={() => onPageChange(pageNum - 1)} //한 페이지 전으로 state 변경
                disabled={pageNum === 1} //1page에서는 disable
            >
                {'<'}
            </button>

            {pages.map((page) => (
                //페이지 수만큼 버튼 만들어냄
                <button
                    key={page}
                    className={page === pageNum ? 'active' : ''} //페이지가 현재 페이지이면 active
                    onClick={() => onPageChange(page)} //페이지 누르면 해당 페이지로 state 변경
                >
                    {page}
                </button>
            ))}

            <button
                onClick={() => onPageChange(pageNum + 1)}
                disabled={pageNum === totalPages}
            >
                {'>'}
            </button>
            <button onClick={() => onPageChange(totalPages)}>{'>>'}</button>
        </div>
    );
}
