from django.contrib import admin
import nested_admin

from .models import *


class ReplyInline(nested_admin.NestedStackedInline):
    model = Reply
    extra = 1


class ResponseInline(nested_admin.NestedStackedInline):
    model = Response
    extra = 2
    inlines = [ReplyInline]


class CarpoolAdmin(nested_admin.NestedAdmin):
    inlines = [ResponseInline]


admin.site.register(User)
admin.site.register(Carpool, CarpoolAdmin)
