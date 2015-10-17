# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('carpool', '0005_carpool_status'),
    ]

    operations = [
        migrations.CreateModel(
            name='GcmRegistration',
            fields=[
                ('id', models.AutoField(serialize=False, verbose_name='ID', primary_key=True, auto_created=True)),
                ('device_id', models.TextField()),
                ('token', models.TextField()),
                ('user', models.ForeignKey(to='carpool.User')),
            ],
        ),
    ]
