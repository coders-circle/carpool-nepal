package com.notifica.carpoolnepal;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONObject;

public class MyGcmListenerService extends  GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    @Override
    public void onMessageReceived(String from, Bundle data) {
//        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
//        if (!settings.getBoolean("pref_key_push_notify", true))
//            return;
//
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
//        if (!preferences.getBoolean("logged-in", false))
//            return;
//
//        String userType = preferences.getString("user-type", "");
//        if (userType != null && userType.equals("Teacher"))
//            return;
        
        String message = data.getString("message");
        String title = data.getString("title");
        long id = Long.parseLong(data.getString("remote_id"));
        sendNotification(title, message, id);
    }

    private void sendNotification(String title, String message, long remote_id) {
        //new NotificationTask(title, message, remote_id).execute();

//        Intent intent = new Intent(MyGcmListenerService.this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtra("notification-title", title);
//        PendingIntent pendingIntent = PendingIntent.getActivity(MyGcmListenerService.this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MyGcmListenerService.this)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(title)
//                .setContentText(message)
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(0, notificationBuilder.build());

    }
}