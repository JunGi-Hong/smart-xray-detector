from django.urls import path
from . import views

urlpatterns =[
    path('detect/', views.detect_xray, name='detect_xray'),
]