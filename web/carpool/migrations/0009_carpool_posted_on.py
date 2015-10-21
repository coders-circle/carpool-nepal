# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import migrations, models
import datetime
from django.utils.timezone import utc


class Migration(migrations.Migration):

    dependencies = [
        ('carpool', '0008_auto_20151021_1428'),
    ]

    operations = [
        migrations.AddField(
            model_name='carpool',
            name='posted_on',
            field=models.DateTimeField(default=datetime.datetime(2015, 10, 21, 11, 42, 7, 382143, tzinfo=utc), auto_now_add=True),
            preserve_default=False,
        ),
    ]
