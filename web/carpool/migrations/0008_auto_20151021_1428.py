# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('carpool', '0007_auto_20151017_1208'),
    ]

    operations = [
        migrations.CreateModel(
            name='Comment',
            fields=[
                ('id', models.AutoField(auto_created=True, verbose_name='ID', serialize=False, primary_key=True)),
                ('message', models.TextField()),
                ('posted_on', models.DateTimeField(auto_now_add=True)),
                ('carpool', models.ForeignKey(to='carpool.Carpool', related_name='responses')),
                ('poster', models.ForeignKey(to='carpool.User')),
            ],
        ),
        migrations.RemoveField(
            model_name='reply',
            name='poster',
        ),
        migrations.RemoveField(
            model_name='reply',
            name='response',
        ),
        migrations.RemoveField(
            model_name='response',
            name='carpool',
        ),
        migrations.RemoveField(
            model_name='response',
            name='poster',
        ),
        migrations.DeleteModel(
            name='Reply',
        ),
        migrations.DeleteModel(
            name='Response',
        ),
    ]
