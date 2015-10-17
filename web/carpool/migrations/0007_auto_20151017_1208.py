# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('carpool', '0006_gcmregistration'),
    ]

    operations = [
        migrations.AlterField(
            model_name='gcmregistration',
            name='device_id',
            field=models.TextField(unique=True),
        ),
    ]
