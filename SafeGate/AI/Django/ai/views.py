from django.shortcuts import render

import os
from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
from django.core.files.storage import FileSystemStorage
from .services import run_xray_detection

@csrf_exempt
def detect_xray(request):
    if request.method == 'POST' and request.FILES.get('image'):
        uploaded_file = request.FILES['image']

        fs = FileSystemStorage(location=os.path.join('media', 'uploads'))
        filename = fs.save(uploaded_file.name, uploaded_file)
        uploaded_file_path = fs.path(filename)

        try:
            detection_data = run_xray_detection(uploaded_file_path, filename)

            return JsonResponse(detection_data, json_dumps_params={'ensure_ascii': False, 'indent': 4})

        except Exception as e:
            return JsonResponse({"error": str(e)}, status=500)

    return JsonResponse({"error": "POST 요청과 함께 'image' 파일을 첨부해주세요."}, status=400)