import os
from datetime import datetime
from django.conf import settings

from reportlab.lib.pagesizes import A4
from reportlab.platypus import SimpleDocTemplate, Paragraph, Spacer, Image as RLImage, Table, TableStyle, KeepTogether
from reportlab.lib.styles import ParagraphStyle
from reportlab.pdfbase import pdfmetrics
from reportlab.pdfbase.ttfonts import TTFont
from reportlab.lib import colors
from reportlab.lib.colors import HexColor

def generate_pdf_report(ai_analysis_dict, graph_files, data):
    font_path_regular = os.path.join(settings.BASE_DIR, 'fonts', 'NanumGothic-Regular.ttf')
    font_path_bold = os.path.join(settings.BASE_DIR, 'fonts', 'NanumGothic-Bold.ttf')

    pdfmetrics.registerFont(TTFont('NanumGothic', font_path_regular))
    pdfmetrics.registerFont(TTFont('NanumGothic-Bold', font_path_bold))

    period_val = data.get('period', 'week')
    timestamp = datetime.now().strftime("%Y%m%d%H%M%S")
    pdf_filename = f"SafeGate_{period_val}_{timestamp}.pdf"

    reports_dir = os.path.join(settings.MEDIA_ROOT, 'reports')
    os.makedirs(reports_dir, exist_ok=True)
    pdf_file_path = os.path.join(reports_dir, pdf_filename)

    doc = SimpleDocTemplate(pdf_file_path, pagesize=A4, rightMargin=45, leftMargin=45, topMargin=50, bottomMargin=50)

    COLOR_PRIMARY = HexColor("#2c3e50") # 어두운 네이비 (주요 제목)
    COLOR_BLUE = HexColor("#007bff")    # 밝은 파란색 (포인트 선)
    COLOR_BG = HexColor("#f8f9fa")      # 아주 연한 회색 (박스 배경)
    COLOR_DATE = HexColor("#d35400")    # 주황색 (날짜 포인트)
    COLOR_LINE = HexColor("#cccccc")    # 연한 회색 (구분선)

    # 일반 본문과 날짜 스타일 설정
    style_normal = ParagraphStyle(name='Normal', fontName='NanumGothic', fontSize=11, leading=18, textColor=colors.darkslategray)
    style_date = ParagraphStyle(name='Date', fontName='NanumGothic-Bold', fontSize=13, textColor=COLOR_DATE, spaceAfter=8, leftIndent=0)

    style_h1 = ParagraphStyle(name='H1', fontName='NanumGothic-Bold', fontSize=24, alignment=1, leading=30, textColor=COLOR_PRIMARY)
    style_h2 = ParagraphStyle(name='H2', fontName='NanumGothic-Bold', fontSize=18, alignment=1, leading=24, textColor=COLOR_PRIMARY)
    style_sub = ParagraphStyle(name='Sub', fontName='NanumGothic', fontSize=12, alignment=1, leading=16, textColor=colors.gray)

    story =[]

    period_text = '주간' if period_val == 'week' else '월간'
    start_date = data.get('start_date', '')
    end_date = data.get('end_date', '')

    header_content = [[Paragraph("<b>[SafeGate]</b>", style_h1)],[Paragraph(f"- {period_text} 보안 탐지 리포트 -", style_h2)],[Paragraph(f"분석 기간: {start_date} ~ {end_date}", style_sub)]
    ]

    header_table = Table(header_content, colWidths=[505])
    header_table.setStyle(TableStyle([
        ('ALIGN', (0,0), (-1,-1), 'CENTER'),
        ('BOTTOMPADDING', (0,0), (-1,0), 8),
        ('BOTTOMPADDING', (0,1), (-1,1), 15),
        ('BOTTOMPADDING', (0,2), (-1,2), 20),
        ('LINEBELOW', (0,2), (-1,2), 2, COLOR_PRIMARY),
    ]))
    story.append(header_table)
    story.append(Spacer(1, 25))

    summary_text = ai_analysis_dict.get('overall_summary', '')
    summary_content =[
        [Paragraph("<b>[ AI 보안 총평 ]</b>", ParagraphStyle(name='BoxH', fontName='NanumGothic-Bold', fontSize=13))],[Paragraph(summary_text, ParagraphStyle(name='BoxB', fontName='NanumGothic', fontSize=11.5, leading=20, spaceBefore=10))]
    ]

    summary_table = Table(summary_content, colWidths=[485])
    summary_table.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,-1), COLOR_BG),
        ('LINEBEFORE', (0,0), (0,-1), 5, COLOR_BLUE),
        ('TOPPADDING', (0,0), (-1,-1), 15),
        ('BOTTOMPADDING', (0,0), (-1,-1), 15),
        ('LEFTPADDING', (0,0), (-1,-1), 20),
        ('RIGHTPADDING', (0,0), (-1,-1), 15),
    ]))
    story.append(summary_table)
    story.append(Spacer(1, 30))

    def add_section_title(title_text):
        t = Table([[Paragraph(f"<b>{title_text}</b>", ParagraphStyle(name='SecH', fontName='NanumGothic-Bold', fontSize=15, textColor=COLOR_PRIMARY))]], colWidths=[505])
        t.setStyle(TableStyle([
            ('LINEBELOW', (0,0), (-1,-1), 1, COLOR_LINE),
            ('BOTTOMPADDING', (0,0), (-1,-1), 8),
        ]))
        story.append(t)
        story.append(Spacer(1, 15))

    def get_image_element(filepath, width, height):
        if filepath and os.path.exists(filepath):
            img = RLImage(filepath, width=width, height=height)
            img.hAlign = 'CENTER'
            return img
        return None

    # 전체 기간 탐지 동향
    add_section_title("1. 전체 기간 탐지 동향")

    pie_img = get_image_element(graph_files.get('total_pie'), width=320, height=320)
    if pie_img:
        story.append(pie_img)
        story.append(Spacer(1, 10))

    line_img = get_image_element(graph_files.get('total_line'), width=450, height=225)
    if line_img:
        story.append(line_img)
        story.append(Spacer(1, 30))

    # 일자별 상세 분석
    add_section_title("2. 일자별 상세 분석")

    daily_analysis = ai_analysis_dict.get('daily_analysis', {})
    daily_pies = graph_files.get('daily_pies', {})

    for date_key, text in daily_analysis.items():
        daily_block =[]

        daily_block.append(Paragraph(f"# {date_key}", style_date))
        daily_block.append(Paragraph(text, style_normal))
        daily_block.append(Spacer(1, 10))

        img_element = get_image_element(daily_pies.get(date_key), width=280, height=280)
        if img_element:
            daily_block.append(img_element)

        daily_block.append(Spacer(1, 25))

        story.append(KeepTogether(daily_block))

    doc.build(story)

    pdf_relative_url = f"{settings.MEDIA_URL}reports/{pdf_filename}"
    return pdf_relative_url