from django.contrib import admin
from .models import *


class ReplyInline(admin.StackedInline):
    model = Reply
    extra = 1


class ResponseAdmin(admin.ModelAdmin):
    inlines = [ReplyInline]


class ResponseInline(admin.StackedInline):
    model = Response
    extra = 2


class CarpoolAdmin(admin.ModelAdmin):
    inlines = [ResponseInline]


admin.site.register(User)
admin.site.register(Carpool, CarpoolAdmin)
admin.site.register(Response, ResponseAdmin)
