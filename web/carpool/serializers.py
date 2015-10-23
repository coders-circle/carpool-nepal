from rest_framework import serializers
from django.contrib.auth.models import User as django_user

from .models import *


class DjangoUserSerializer(serializers.ModelSerializer):
    password = serializers.CharField(write_only=True)
    class Meta:
        model = django_user
        fields = ('first_name', 'last_name', 'username', 'password', 'email')


class UserSerializer(serializers.ModelSerializer):
    user = DjangoUserSerializer()

    class Meta:
        model = User
        fields = ('id', 'user', 'contact_number', 'contact_address')

    def create(self, validated_data):
        user_data = validated_data.pop('user')
        duser = django_user.objects.create_user(**user_data)
        user = User.objects.create(user=duser, **validated_data)
        return user

 
# class CommentMiniSerializer(serializers.ModelSerializer):
# 
#     class Meta:
#         model = Comment
#         fields = ('id', 'poster', 'message', 'posted_on')


datetimeformat = "%Y-%m-%d-%H-%M-%S"
 

class CarpoolSerializer(serializers.ModelSerializer):
    poster = serializers.PrimaryKeyRelatedField(read_only=True)
    posted_on = serializers.DateTimeField(format=datetimeformat, input_formats=[datetimeformat, 'iso-8601'], read_only=True)
    # comments = CommentMiniSerializer(many=True)

    class Meta:
        model = Carpool
        fields = ('id', 'carpool_type', 'status', 'source', 'destination', 'description', 'seats', 'poster', 'time', 'date', 'posted_on') #, 'comments')


class CommentSerializer(serializers.ModelSerializer):
    poster = serializers.PrimaryKeyRelatedField(read_only=True)
    posted_on = serializers.DateTimeField(format=datetimeformat, input_formats=[datetimeformat, 'iso-8601'], read_only=True)

    class Meta:
        model = Comment
        fields = ('id', 'carpool', 'poster', 'message', 'posted_on')


class GcmRegistrationSerializer(serializers.ModelSerializer):
    user = serializers.PrimaryKeyRelatedField(read_only=True)

    class Meta:
        model = GcmRegistration
        fields = ('id', 'device_id', 'token', 'user')
