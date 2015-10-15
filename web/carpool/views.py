from django.shortcuts import render
from django.db.models import Q
from rest_framework import viewsets, permissions, views, authentication
from rest_framework.response import Response as RestResponse

from .models import *
from .serializers import *
from .permissions import *


class AuthView(views.APIView):
    authentication_classes = (authentication.SessionAuthentication, authentication.BasicAuthentication)
    permission_classes = (permissions.IsAuthenticated, )

    def get(self, request, format=None):
        content = {
            "user": str(request.user),
            "auth": str(request.auth)
        }
        return RestResponse(content)


class UserViewSet(viewsets.ModelViewSet):
    queryset = User.objects.all()
    serializer_class = UserSerializer


class CarpoolViewSet(viewsets.ModelViewSet):
    serializer_class = CarpoolSerializer
    permission_classes = (permissions.IsAuthenticatedOrReadOnly, IsOwnerOrReadOnly)

    def perform_create(self, serializer):
        user = User.objects.filter(user=self.request.user).get()
        serializer.save(poster=user)

    def get_queryset(self):
        location = self.request.GET.get("location")
        if location and location != "":
            queryset = Carpool.objects.filter(Q(source__iexact=location) | Q(destination__iexact=location))
        else:
            queryset = Carpool.objects.all()

        return queryset


class ResponseViewSet(viewsets.ModelViewSet):
    queryset = Response.objects.all()
    serializer_class = ResponseSerializer
    permission_classes = (permissions.IsAuthenticatedOrReadOnly, IsOwnerOrReadOnly)


class ReplyViewSet(viewsets.ModelViewSet):
    queryset = Reply.objects.all()
    serializer_class = ReplySerializer
    permission_classes = (permissions.IsAuthenticatedOrReadOnly, IsOwnerOrReadOnly)
