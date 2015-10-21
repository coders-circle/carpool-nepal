from django.db import models
from django.contrib.auth.models import User as django_user


class User(models.Model):
    # built-in user already contains:
    # first_name, last_name, user_name, password
    # email, last_login, date_joined, is_superuser fields.
    # https://docs.djangoproject.com/en/1.8/ref/contrib/auth/#django.contrib.auth.models.User

    user = models.OneToOneField(django_user)
    contact_number = models.IntegerField()
    contact_address = models.TextField()

    def __str__(self):
        return self.user.username


class Carpool(models.Model):
    carpool_types = (
        (0, 'Offer'),
        (1, 'Request'),
    )

    status_types = (
        (0, 'Open'),
        (1, 'Pending'),
        (2, 'Closed'),
    )

    carpool_type = models.IntegerField(default=0, choices=carpool_types)
    source = models.CharField(max_length=100)
    destination = models.CharField(max_length=100)
    description = models.TextField()
    seats = models.IntegerField(default=1)
    status = models.IntegerField(default=0, choices=status_types)
    poster = models.ForeignKey(User)
    time = models.TimeField()
    date = models.DateField()
    posted_on = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        string = self.source + " - " + self.destination
        string += " Seats: " + str(self.seats)
        string += " By: " + str(self.poster)
        return string


class Comment(models.Model):
    carpool = models.ForeignKey(Carpool, related_name='responses')
    poster = models.ForeignKey(User)
    message = models.TextField()
    posted_on = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return self.message


class GcmRegistration(models.Model):
    user = models.ForeignKey(User)
    device_id = models.TextField(unique=True)
    token = models.TextField()
