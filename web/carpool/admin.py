from django.contrib import admin

from .models import *


class CommentInline(admin.StackedInline):
    model = Comment
    extra = 2


class CarpoolAdmin(admin.ModelAdmin):
    inlines = [CommentInline]


admin.site.register(User)
admin.site.register(Carpool, CarpoolAdmin)
