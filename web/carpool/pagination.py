from rest_framework import pagination
from rest_framework.response import Response

class CustomPagination(pagination.PageNumberPagination):
    page_size = 1000
    page_size_query_param = "num-pages"

    def get_paginated_response(self, data):
        return Response(data)
