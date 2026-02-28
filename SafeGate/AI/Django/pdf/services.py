import json
import pandas as pd
from django.conf import settings
from openai import OpenAI

def process_report_data(data):
    """
    [1단계] Spring에서 넘겨준 JSON 데이터를 받아 Pandas를 이용해 차트용 데이터로 가공합니다.
    """
    period = data.get('period', 'week')
    start_date = data.get('start_date')
    end_date = data.get('end_date')
    events = data.get('events',[])

    if not events:
        return {"empty": True, "period": period}

    df = pd.DataFrame(events)
    df = df.explode('detected_items').dropna(subset=['detected_items'])
    df.rename(columns={'detected_items': 'item'}, inplace=True)

    df['time'] = pd.to_datetime(df['time'])
    df['date'] = df['time'].dt.normalize()

    full_date_range = pd.date_range(start=start_date, end=end_date, freq='D')

    total_counts = df['item'].value_counts().to_dict()

    daily_pivot = df.pivot_table(index='date', columns='item', aggfunc='size', fill_value=0)
    daily_pivot = daily_pivot.reindex(full_date_range, fill_value=0)

    result = {
        "period": period,
        "total_counts": total_counts,
        "daily_pivot": daily_pivot,
        "raw_json": data
    }

    if period == 'month':
        weekly_pivot = daily_pivot.resample('W-MON').sum()
        result["weekly_pivot"] = weekly_pivot

    return result


def get_openai_analysis(raw_json, period):
    """[2단계] 원본 JSON 데이터를 OpenAI API에 보내 분석 문구를 JSON 형태로 받아옵니다.
    """

    if not raw_json.get('events'):
        return {
            "overall_summary": "해당 기간 동안 탐지된 위해물품이 없습니다. 안전한 상태를 유지하고 있습니다.",
            "daily_analysis": {}
        }

    client = OpenAI(api_key=settings.OPENAI_API_KEY)
    data_str = json.dumps(raw_json, ensure_ascii=False)

    # [주간 / 월간 프롬프트 분리]
    if period == 'week':
        system_prompt = """너는 공항 보안 검색대의 X-ray 탐지 데이터 분석 전문가야.
제공된 주간(최근 7일) 탐지 데이터를 분석해서 보고서를 작성해줘.

[작성 조건]
1. 말투는 '~습니다', '~합니다'와 같은 격식 있는 보고서 체로 작성할 것.
2. 탐지 내역이 존재하는 주요 일자별로 특이사항(급증한 물품 등)을 1~2문장으로 요약할 것.
3. 주간 전체 총평을 3~4문장으로 요약할 것.[출력 형식]
반드시 아래와 같은 JSON 형식으로만 응답해. (다른 말은 절대 추가하지 마)
{
    "daily_analysis": {
        "2026-02-28": "28일에는 라이터오일(9건)과 도끼(3건)가 집중적으로 탐지되어 화재 및 흉기 테러에 대한 주의가 요구됩니다.",
        "2026-03-01": "전일 대비 탐지량이 감소하였으나, 끌과 보조배터리가 새롭게 탐지되었습니다."
    },
    "overall_summary": "이번 주간은 특히 라이터오일과 같은 인화성 물질의 반입 시도가 돋보였습니다. 또한..."
}"""
    else:
        system_prompt = """너는 공항 보안 검색대의 X-ray 탐지 데이터 분석 전문가야.
제공된 월간(최근 30일) 탐지 데이터를 분석해서 보고서를 작성해줘.

[작성 조건]
1. 말투는 '~습니다', '~합니다'와 같은 격식 있는 보고서 체로 작성할 것.
2. 탐지 내역이 존재하는 주요 일자별로 특이사항을 1~2문장 요약할 것.
3. 월간 전체의 추세 및 총평을 3~4문장으로 요약할 것.

[출력 형식]
반드시 아래와 같은 JSON 형식으로만 응답해. (다른 말은 절대 추가하지 마)
{
    "daily_analysis": {
        "2026-02-28": "월말을 맞아 라이터오일 및 도끼 등 위험물 반입이 일시적으로 급증했습니다.",
        "2026-03-01": "..."
    },
    "overall_summary": "이번 달은 첫째 주에 비해 넷째 주에 인화성 물질의 탐지가 크게 증가하는 추세를 보였습니다. 전반적으로..."
}"""

    user_prompt = f"다음은 분석할 데이터야:\n{data_str}"

    try:
        # GPT 모델 호출
        response = client.chat.completions.create(
            model="gpt-5-mini",
            response_format={ "type": "json_object" },
            messages=[
                {"role": "system", "content": system_prompt},
                {"role": "user", "content": user_prompt}
            ]
        )

        ai_result_text = response.choices[0].message.content
        return json.loads(ai_result_text)

    except Exception as e:
        print(f"OpenAI 에러: {e}")
        return {
            "overall_summary": "AI 분석을 생성하는 중 오류가 발생했습니다.",
            "daily_analysis": {}
        }