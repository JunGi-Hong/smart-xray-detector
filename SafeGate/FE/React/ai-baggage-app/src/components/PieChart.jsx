import { ResponsivePie } from '@nivo/pie'
import { detectionData } from '../data/detectionData'

export default function PieChart() {

    //가져온 데이터 형식 라이브러리에 맞게 조작
    const data = detectionData.map(item => (
        {
            id: item.status,
            label: item.status,
            value: item.count
        }
    ))

    //파이 차트
    return (
        <ResponsivePie
            data={data}
            margin={{ top: 40, right: 80, bottom: 80, left: 80 }}
            innerRadius={0.5}          // 도넛 형태
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
