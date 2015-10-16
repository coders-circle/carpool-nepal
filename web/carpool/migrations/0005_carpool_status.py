# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('carpool', '0004_auto_20151015_1832'),
    ]

    operations = [
        migrations.AddField(
            model_name='carpool',
            name='status',
            field=models.IntegerField(choices=[(0, 'Open'), (1, 'Pending'), (2, 'Closed')], default=0),
        ),
    ]
