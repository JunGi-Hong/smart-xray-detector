import { useState, useEffect } from 'react';
import { ResponsivePie } from '@nivo/pie';
import { ITEM_MAP, getName } from '../data/ItemID';

export default function PieChart() {
    const [chartData, setChartData] = useState([]);

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

                //카운팅을 위한 임시 객체 (API 호출 시마다 깔끔하게 0으로 시작)
                const tempCounts = Object.fromEntries(
                    Array.from({ length: 32 }, (_, i) => [i + 1, 0])
                );

                // 데이터 카운팅 로직 
                responseData.forEach(event => {
                    event.detect.forEach(value => {
                        if (tempCounts[value] !== undefined) {
                            tempCounts[value] += 1;
                        }
                    });
                });

                // Nivo 파이 차트 형식에 맞게 데이터 변환 및 필터링
                const formattedData = Object.keys(tempCounts)
                    .filter(key => tempCounts[key] > 0)
                    .map(key => ({
                        id: getName(Number(key)),    // 툴팁이나 식별자로 쓸 이름
                        label: getName(Number(key)), // 화면에 보일 라벨
                        value: tempCounts[key]       // 카운트된 갯수
                    }));

                // 상태 업데이트 -> 차트 그려짐
                setChartData(formattedData);
            }
        } catch (error) {
            console.error('piechart error:', error);
        }
    };

    useEffect(() => {
        fetchData();
    }, []); // 의존성 배열을 비워두어 마운트 시 한 번만 실행 (Strict Mode 제외)

    // 데이터가 로딩 중이거나 없을 때의 처리
    if (chartData.length === 0) {
        return <div>차트 데이터를 불러오는 중입니다...</div>;
    }

    return (
        <ResponsivePie
            data={chartData}//데이터
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
    );
}