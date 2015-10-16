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


class ReplyMiniSerializer(serializers.ModelSerializer):
    class Meta:
        model = Reply
        fields = ('id', 'poster', 'message', 'posted_on')
 
 
# class ResponseMiniSerializer(serializers.ModelSerializer):
#     replies = ReplyMiniSerializer(many=True)
# 
#     class Meta:
#         model = Response
#         fields = ('id', 'poster', 'urgency', 'message', 'replies', 'posted_on')
 

class CarpoolSerializer(serializers.ModelSerializer):
    poster = serializers.PrimaryKeyRelatedField(read_only=True)
    # responses = ResponseMiniSerializer(many=True)

    class Meta:
        model = Carpool
        fields = ('id', 'carpool_type', 'source', 'destination', 'description', 'seats', 'poster', 'time', 'date') #, 'responses')


class ResponseSerializer(serializers.ModelSerializer):
    replies = ReplyMiniSerializer(many=True)

    class Meta:
        model = Response
        fields = ('id', 'carpool', 'poster', 'urgency', 'message', 'replies', 'posted_on')


class ReplySerializer(serializers.ModelSerializer):
    class Meta:
        model = Reply
        fields = ('id', 'poster', 'message', 'response', 'posted_on')
