# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import migrations, models
from django.conf import settings


class Migration(migrations.Migration):

    dependencies = [
        migrations.swappable_dependency(settings.AUTH_USER_MODEL),
    ]

    operations = [
        migrations.CreateModel(
            name='Carpool',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, primary_key=True, auto_created=True)),
                ('carpool_type', models.IntegerField(default=0, choices=[(0, 'Offer'), (1, 'Request')])),
                ('source', models.CharField(max_length=100)),
                ('destination', models.CharField(max_length=100)),
                ('description', models.TextField()),
                ('seats', models.IntegerField(default=1)),
                ('start_time', models.TimeField()),
                ('end_time', models.TimeField()),
                ('date', models.DateField()),
            ],
        ),
        migrations.CreateModel(
            name='Reply',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, primary_key=True, auto_created=True)),
                ('message', models.TextField()),
            ],
        ),
        migrations.CreateModel(
            name='Response',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, primary_key=True, auto_created=True)),
                ('urgency', models.IntegerField(default=0, choices=[(0, 'Normal'), (1, 'Urgent')])),
                ('message', models.TextField()),
                ('carpool', models.ForeignKey(to='carpool.Carpool')),
            ],
        ),
        migrations.CreateModel(
            name='User',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, primary_key=True, auto_created=True)),
                ('contact_number', models.IntegerField()),
                ('contact_address', models.TextField()),
                ('user', models.OneToOneField(to=settings.AUTH_USER_MODEL)),
            ],
        ),
        migrations.AddField(
            model_name='response',
            name='responder',
            field=models.ForeignKey(to='carpool.User'),
        ),
        migrations.AddField(
            model_name='reply',
            name='response',
            field=models.ForeignKey(to='carpool.Response'),
        ),
        migrations.AddField(
            model_name='carpool',
            name='poster',
            field=models.ForeignKey(to='carpool.User'),
        ),
    ]
