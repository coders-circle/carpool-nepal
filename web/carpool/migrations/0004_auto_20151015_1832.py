# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import migrations, models
from django.utils.timezone import utc
import datetime


class Migration(migrations.Migration):

    dependencies = [
        ('carpool', '0003_auto_20151015_1801'),
    ]

    operations = [
        migrations.AddField(
            model_name='reply',
            name='posted_on',
            field=models.DateTimeField(default=datetime.datetime(2015, 10, 15, 12, 47, 42, 628086, tzinfo=utc), auto_now_add=True),
            preserve_default=False,
        ),
        migrations.AddField(
            model_name='response',
            name='posted_on',
            field=models.DateTimeField(default=datetime.datetime(2015, 10, 15, 12, 47, 46, 877289, tzinfo=utc), auto_now_add=True),
            preserve_default=False,
        ),
    ]
