export default function Pagination({
    currentPage,
    totalPages,
    onPageChange,
}) {
    const pages = Array.from({ length: totalPages }, (_, i) => i + 1);

    return (
        <div className="pagination">
            <button onClick={() => onPageChange(1)}>{'<<'}</button>
            <button
                onClick={() => onPageChange(currentPage - 1)} //한 페이지 전으로 state 변경
                disabled={currentPage === 1} //1page에서는 disable
            >
                {'<'}
            </button>

            {pages.map((page) => (
                <button //페이지 숫자 = 버튼
                    key={page}
                    className={page === currentPage ? 'active' : ''} //페이지가 현재 페이지이면 active
                    onClick={() => onPageChange(page)} //페이지 누르면 해당 페이지로 state 변경
                >
                    {page}
                </button>
            ))}

            <button
                onClick={() => onPageChange(currentPage + 1)}
                disabled={currentPage === totalPages}
            >
                {'>'}
            </button>
            <button onClick={() => onPageChange(totalPages)}>{'>>'}</button>
        </div>
    );
}
