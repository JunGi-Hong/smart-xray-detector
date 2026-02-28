import json
from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
from .services import process_report_data, get_openai_analysis
from .graphs import generate_graphs
from .pdf_maker import generate_pdf_report

@csrf_exempt
def generate_report(request):
    if request.method == 'POST':
        try:
            data = json.loads(request.body)
            period = data.get('period', 'week')
            processed_data = process_report_data(data)

            if processed_data.get('empty'):
                print(f"[{period}] 탐지 내역이 없습니다.")
                return JsonResponse({"message": "해당 기간 동안 탐지 내역이 없습니다."}, status=200)

            # 데이터 전처리
            print(f"\n==========[1단계: Pandas 데이터 전처리 완료 ({period})] ==========")
            print("1. 전체 누적 개수:")
            print(processed_data['total_counts'])
            print("\n2. 일별 탐지 개수 표:")
            print(processed_data['daily_pivot'])

            if period == 'month':
                print("\n3. 주차별 탐지 개수 표:")
                print(processed_data['weekly_pivot'])
            print("=================================================================\n")

            # GPT 문장 생성
            print(f"[{period}] OpenAI에 데이터 분석을 요청합니다... (잠시 대기 ⏳)")
            ai_analysis_dict = get_openai_analysis(data, period)

            # 📌 2단계 상세 로그 출력
            print("\n========== [2단계: OpenAI 분석 완료] ==========")
            print(json.dumps(ai_analysis_dict, ensure_ascii=False, indent=4))
            print("=================================================\n")

            print(f"[{period}] 그래프 이미지를 생성하고 있습니다... 📊")
            graph_files = generate_graphs(processed_data)

            # 그래프 생성
            print("\n========== [3단계: 그래프 임시 파일 생성 완료] ==========")
            print(f"저장된 최상위 폴더: {graph_files['report_dir']}")
            print("생성된 파일 목록:")
            for key, val in graph_files.items():
                if key != "report_dir" and val:
                    print(f" - {key}: {val}")
            print("=========================================================\n")

            # PDF 생성
            print(f"[{period}] PDF 리포트 병합을 시작합니다... 📄")
            pdf_relative_url = generate_pdf_report(ai_analysis_dict, graph_files, data)

            absolute_pdf_url = request.build_absolute_uri(pdf_relative_url)

            print("\n========== [4단계: PDF 생성 완료] ==============")
            print(f"최종 PDF 다운로드 URL: {absolute_pdf_url}")
            print("=================================================\n")

            return JsonResponse({"pdf_url": absolute_pdf_url}, status=200)

        except Exception as e:
            print(f"Error occurred: {str(e)}")
            return JsonResponse({"error": str(e)}, status=500)

    return JsonResponse({"error": "POST 요청만 지원합니다."}, status=405)