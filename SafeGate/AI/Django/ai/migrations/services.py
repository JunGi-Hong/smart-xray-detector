import os
import json
import cv2
import base64
from django.conf import settings
from ultralytics import YOLO

model = YOLO(settings.YOLO_MODEL_PATH)

def run_xray_detection(image_path, filename):
    print(f"\n🔍 이미지 분석을 시작합니다... (파일: {filename}, conf=0.05)")

    results_list = model.predict(source=image_path, imgsz=1024, conf=0.05, save=False)
    result = results_list[0]

    output_data = {
        "image_file": filename,
        "total_detected": 0,
        "detections":[]
    }

    boxes = result.boxes
    output_data["total_detected"] = len(boxes)

    if len(boxes) > 0:
        for box in boxes:
            cls_id = int(box.cls.item())
            conf_score = float(box.conf.item())
            class_name = result.names.get(cls_id, "Unknown")

            output_data["detections"].append({
                "item": class_name,
                "probability_percent": round(conf_score * 100, 2)
            })

    output_data["detections"].sort(key=lambda x: x.get("probability_percent", 0), reverse=True)

    result_dir = os.path.join(settings.MEDIA_ROOT, 'results')
    os.makedirs(result_dir, exist_ok=True)

    annotated_img = result.plot()
    result_img_name = f"result_{filename}"
    result_img_path = os.path.join(result_dir, result_img_name)
    cv2.imwrite(result_img_path, annotated_img)

    base_name = os.path.splitext(filename)[0] # 확장자 제외한 파일명
    json_save_path = os.path.join(result_dir, f"{base_name}_result.json")
    with open(json_save_path, "w", encoding="utf-8") as f:
        json.dump(output_data, f, ensure_ascii=False, indent=4)

    with open(result_img_path, "rb") as image_file:
        encoded_string = base64.b64encode(image_file.read()).decode('utf-8')

    output_data["image_base64"] = encoded_string

    return output_data