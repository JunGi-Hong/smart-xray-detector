import { useState, useEffect } from 'react';
import { ResponsivePie } from '@nivo/pie';
import { ITEM_MAP, getName } from '../data/ItemID';

export default function PieChart() {
    const [chartData, setChartData] = useState([]);
    const [isLoading, setIsLoading] = useState(true); // 1. 로딩 상태 추가

    const fetchData = async () => {
        try {
            const accessToken = localStorage.getItem('accessToken');
            const response = await fetch('/board/events/recent', {
                method: 'GET',
                headers: {
                    'Content-type': 'application/json',
                    'Authorization': `Bearer ${accessToken}`
                }
            });

            if (response.ok) {
                const responseData = await response.json();

                // 1. 빈 객체로 시작 (아이디가 몇 번까지 올지 모르니 동적으로 할당)
                const tempCounts = {};

                // 2. 객체 형태의 detect 데이터를 순회하며 카운트 누적
                responseData.forEach(event => {
                    // event.detect가 존재하고, 객체 형태일 때만 실행
                    if (event.detect && typeof event.detect === 'object') {
                        // Object.entries를 쓰면 { "37": 2, "3": 1 }를 [["37", 2], ["3", 1]] 형태의 배열로 바꿔서 반복문을 돌릴 수 있습니다.
                        Object.entries(event.detect).forEach(([id, count]) => {
                            const numId = Number(id);
                            // 기존에 값이 있으면 거기에 더하고, 없으면 새로 count를 넣음
                            tempCounts[numId] = (tempCounts[numId] || 0) + count;
                        });
                    }
                });

                // 3. Nivo 파이 차트 형식에 맞게 데이터 변환 및 필터링
                const formattedData = Object.keys(tempCounts)
                    .filter(key => tempCounts[key] > 0)
                    .map(key => ({
                        id: getName(Number(key)),    // 툴팁이나 식별자로 쓸 이름
                        label: getName(Number(key)), // 화면에 보일 라벨
                        value: tempCounts[key]       // 누적된 갯수
                    }))
                    .sort((a, b) => b.value - a.value) // value 기준으로 내림차순 정렬
                    .slice(0, 4);                      // 상위 4개 데이터만 추출

                // 상태 업데이트 -> 차트 그려짐
                setChartData(formattedData);
            }
        } catch (error) {
            console.error('piechart error:', error);
        } finally {
            // 성공, 실패 여부와 상관없이 페칭이 끝나면 로딩 상태 해제
            setIsLoading(false);
        }
    };

    useEffect(() => {
        fetchData();
    }, []);

    // 3. 로딩 중 처리와 데이터 없음 분리
    if (isLoading) {
        return <div>차트 데이터를 불러오는 중입니다...</div>;
    }

    if (chartData.length === 0) {
        return <div>표시할 차트 데이터가 없습니다.</div>;
    }

    // 4. 차트를 감싸는 부모 div에 명시적 높이 지정 (필수)
    return (
        <div style={{ height: '400px', width: '100%' }}>
            <ResponsivePie
                data={chartData}
                margin={{ top: 40, right: 80, bottom: 80, left: 80 }}
                innerRadius={0.5}
                padAngle={0.7}
                cornerRadius={3}
                activeOuterRadiusOffset={8}
                colors={{ scheme: 'set2' }}
                borderWidth={1}
                borderColor={{ from: 'color', modifiers: [['darker', 0.2]] }}
                arcLinkLabelsSkipAngle={10}
                arcLinkLabelsTextColor="#333"
                arcLinkLabelsThickness={2}
                arcLabelsSkipAngle={10}
                arcLabelsTextColor="#fff"
                legends={[
                    {
                        anchor: 'bottom',
                        direction: 'row',
                        translateY: 56,
                        itemWidth: 100,
                        itemHeight: 18,
                        symbolSize: 18,
                        symbolShape: 'circle',
                    },
                ]}
            />
        </div>
    );
}