from django.conf.urls import url, include
from rest_framework.routers import DefaultRouter

from . import views


router = DefaultRouter()
router.register(r'users', views.UserViewSet)
router.register(r'carpools', views.CarpoolViewSet, base_name='carpool')
router.register(r'responses', views.ResponseViewSet, base_name='response')
router.register(r'replies', views.ReplyViewSet)

urlpatterns = [
    url(r'^', include(router.urls)),
    url(r'^api-auth/', include('rest_framework.urls', namespace='rest_framework')),

    url(r'^authenticate/', views.AuthView.as_view(), name='authenticate'),
]
