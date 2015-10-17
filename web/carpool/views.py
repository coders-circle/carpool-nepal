from django.shortcuts import render
from django.db.models import Q
from rest_framework import viewsets, permissions, views, authentication
from rest_framework.response import Response as RestResponse

from .models import *
from .serializers import *
from .permissions import *

import re


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
            words = re.sub("[^\S]", " ", location).split()
            regexstring = r'(' + '|'.join([re.escape(n) for n in words]) + ')'
            queryset = Carpool.objects.filter(Q(source__iregex=regexstring) | Q(destination__iregex=regexstring))
        else:
            queryset = Carpool.objects.all()

        return queryset


class ResponseViewSet(viewsets.ModelViewSet):
    serializer_class = ResponseSerializer
    permission_classes = (permissions.IsAuthenticatedOrReadOnly, IsOwnerOrReadOnly)

    def perform_create(self, serializer):
        user = User.objects.filter(user=self.request.user).get()
        serializer.save(poster=user)

    def get_queryset(self):
        carpool_id = self.request.GET.get("carpool")
        if carpool_id:
            queryset = Response.objects.filter(carpool__pk=carpool_id)
        else:
            queryset = Response.objects.all()

        return queryset


class ReplyViewSet(viewsets.ModelViewSet):
    queryset = Reply.objects.all()
    serializer_class = ReplySerializer
    permission_classes = (permissions.IsAuthenticatedOrReadOnly, IsOwnerOrReadOnly)
    
    def perform_create(self, serializer):
        user = User.objects.filter(user=self.request.user).get()
        serializer.save(poster=user)


class GcmRegistrationViewSet(viewsets.ModelViewSet):
    serializer_class = GcmRegistrationSerializer
    permission_classes = (permissions.IsAuthenticatedOrReadOnly, IsOwnerOrReadOnly)

    def perform_create(self, serializer):
        user = User.objects.filter(user=self.request.user).get()
        serializer.save(user=user)

    def get_queryset(self):
        device_id = self.request.GET.get("device")
        if device_id:
            queryset = GcmRegistration.objects.filter(device_id=device_id)
        else:
            queryset = GcmRegistration.objects.all()
        return queryset
