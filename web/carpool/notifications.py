from .models import *
import requests
import json


def NotifyClient(user, title, message, remote_id):
    url = "https://gcm-http.googleapis.com/gcm/send"
    ids = set()
    gcms = GcmRegistration.objects.filter(user=user)
    for gcm in gcms:
        ids.add(gcm.token)

    message = { "message": message, "title": title, "remote_id": remote_id }
    data = {"data": message, "registration_ids":list(ids)}

    key = "key=AIzaSyCzlXDI3R65Kgtwf5Oxw-wDTA7hRnFbsuk"
    headers = {'Content-type':'application/json', 'Authorization':key}
    r = requests.post(url, data = json.dumps(data), headers = headers)
