import os
import uuid
import platform
import matplotlib

matplotlib.use('Agg')
import matplotlib.pyplot as plt
from django.conf import settings


if platform.system() == 'Windows':
    matplotlib.rc('font', family='Malgun Gothic')
elif platform.system() == 'Darwin':
    matplotlib.rc('font', family='AppleGothic')
else:
    matplotlib.rc('font', family='NanumGothic')
matplotlib.rc('axes', unicode_minus=False)


def generate_graphs(processed_data):
    """
    Pandas 데이터를 바탕으로 원형/꺾은선 그래프를 그리고, '임시 폴더'에 이미지를 저장
    """

    temp_dir = os.path.join(settings.MEDIA_ROOT, 'temp_graphs')
    os.makedirs(temp_dir, exist_ok=True)

    report_id = str(uuid.uuid4())
    report_dir = os.path.join(temp_dir, report_id)
    os.makedirs(report_dir, exist_ok=True)

    graphs = {
        "report_dir": report_dir,
        "daily_pies": {},
        "total_pie": "",
        "total_line": "",
        "weekly_pies": {},
        "weekly_line": ""
    }

    daily_pivot = processed_data['daily_pivot']
    total_counts = processed_data['total_counts']
    period = processed_data['period']

    # 일별 원형 그래프
    for date_idx, row in daily_pivot.iterrows():
        row = row[row > 0]
        if not row.empty:
            date_str = date_idx.strftime('%Y-%m-%d')
            plt.figure(figsize=(5, 5))
            plt.pie(row.values, labels=row.index, autopct='%1.1f%%', startangle=140)
            plt.title(f"{date_str} 탐지 비율")

            filepath = os.path.join(report_dir, f"daily_pie_{date_str}.png")
            plt.savefig(filepath, bbox_inches='tight')
            plt.close()

            graphs['daily_pies'][date_str] = filepath

    # 전체 기간 누적 원형 그래프
    if total_counts:
        plt.figure(figsize=(6, 6))
        plt.pie(total_counts.values(), labels=total_counts.keys(), autopct='%1.1f%%', startangle=140)
        plt.title("전체 기간 탐지 항목 비율")

        filepath = os.path.join(report_dir, "total_pie.png")
        plt.savefig(filepath, bbox_inches='tight')
        plt.close()
        graphs['total_pie'] = filepath

    # 전체 기간 꺾은선 그래프
    plt.figure(figsize=(10, 5))
    for item in daily_pivot.columns:
        plt.plot(daily_pivot.index, daily_pivot[item], marker='o', label=item)

    plt.title("전체 기간 탐지 항목 증감 추이")
    plt.xlabel("날짜")
    plt.ylabel("탐지 건수")
    plt.xticks(rotation=45)
    plt.legend(loc='upper left', bbox_to_anchor=(1, 1))

    filepath = os.path.join(report_dir, "total_line.png")
    plt.savefig(filepath, bbox_inches='tight')
    plt.close()
    graphs['total_line'] = filepath

    # 주차별 그래프 (월간 요청 시)
    if period == 'month' and 'weekly_pivot' in processed_data:
        weekly_pivot = processed_data['weekly_pivot']

        # 주차별 원형 그래프
        for date_idx, row in weekly_pivot.iterrows():
            row = row[row > 0]
            if not row.empty:
                week_str = date_idx.strftime('%Y-%m-%d (주간)')
                plt.figure(figsize=(5, 5))
                plt.pie(row.values, labels=row.index, autopct='%1.1f%%', startangle=140)
                plt.title(f"{week_str} 탐지 비율")

                filepath = os.path.join(report_dir, f"weekly_pie_{date_idx.strftime('%Y%m%d')}.png")
                plt.savefig(filepath, bbox_inches='tight')
                plt.close()
                graphs['weekly_pies'][week_str] = filepath

        # 주차별 꺾은선 그래프
        plt.figure(figsize=(10, 5))
        for item in weekly_pivot.columns:
            plt.plot(weekly_pivot.index.strftime('%Y-%m-%d'), weekly_pivot[item], marker='s', label=item)

        plt.title("주차별 탐지 항목 증감 추이")
        plt.xlabel("주차 (해당 주의 월요일 기준)")
        plt.ylabel("탐지 건수")
        plt.xticks(rotation=45)
        plt.legend(loc='upper left', bbox_to_anchor=(1, 1))

        filepath = os.path.join(report_dir, "weekly_line.png")
        plt.savefig(filepath, bbox_inches='tight')
        plt.close()
        graphs['weekly_line'] = filepath

    return graphs