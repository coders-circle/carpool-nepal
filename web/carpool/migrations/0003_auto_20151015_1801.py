# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('carpool', '0002_auto_20151015_1637'),
    ]

    operations = [
        migrations.RenameField(
            model_name='response',
            old_name='responder',
            new_name='poster',
        ),
        migrations.AddField(
            model_name='reply',
            name='poster',
            field=models.ForeignKey(to='carpool.User', default=1),
            preserve_default=False,
        ),
        migrations.AlterField(
            model_name='reply',
            name='response',
            field=models.ForeignKey(related_name='replies', to='carpool.Response'),
        ),
        migrations.AlterField(
            model_name='response',
            name='carpool',
            field=models.ForeignKey(related_name='responses', to='carpool.Carpool'),
        ),
    ]
