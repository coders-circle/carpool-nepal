# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('carpool', '0001_initial'),
    ]

    operations = [
        migrations.RenameField(
            model_name='carpool',
            old_name='start_time',
            new_name='time',
        ),
        migrations.RemoveField(
            model_name='carpool',
            name='end_time',
        ),
    ]
