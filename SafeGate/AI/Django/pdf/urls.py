from django.urls import path
from . import views

urlpatterns =[
    path('report/', views.generate_report, name='generate_report'),
]