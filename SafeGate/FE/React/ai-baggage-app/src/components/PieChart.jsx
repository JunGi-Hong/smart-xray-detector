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

                const tempCounts = Object.fromEntries(
                    Array.from({ length: 32 }, (_, i) => [i + 1, 0])
                );

                // 2. 옵셔널 체이닝(?.) 적용으로 빈 데이터/오류 대비
                responseData.forEach(event => {
                    event.detect?.forEach(value => {
                        if (tempCounts[value] !== undefined) {
                            tempCounts[value] += 1;
                        }
                    });
                });

                const formattedData = Object.keys(tempCounts)
                    .filter(key => tempCounts[key] > 0)
                    .map(key => ({
                        id: getName(Number(key)),
                        label: getName(Number(key)),
                        value: tempCounts[key]
                    }))
                    .sort((a, b) => b.value - a.value)
                    .slice(0, 4);

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