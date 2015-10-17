package com.notifica.carpoolnepal;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GcmRegisterIntent extends IntentService {

    private static final String TAG = "GcmRegIntentService";

    public GcmRegisterIntent() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            // In the (unlikely) event that multiple refresh operations occur simultaneously,
            // ensure that they are processed sequentially.
            synchronized (TAG) {
                InstanceID instanceID = InstanceID.getInstance(this);
                String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                Log.i(TAG, "GCM Registration Token: " + token);

                sendRegistrationToServer(this, token);
                sharedPreferences.edit().putString("gcm_token", token).apply();
            }
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            sharedPreferences.edit().putString("gcm_token", "").apply();
            sharedPreferences.edit().putBoolean("gcm_token_sent", false).apply();
        }
    }

    public static void sendRegistrationToServer(Context context, String token) {
        final JSONObject json = new JSONObject();
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final String device_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        try {
            String username = preferences.getString("username","");
            String password = preferences.getString("password", "");

            json.put("device_id", device_id);
            json.put("token", token);

            final NetworkHandler handler = new NetworkHandler(username, password);
            // First check if this device id exists
            handler.GetAsync("gcm-registrations/?device=" + Uri.encode(device_id), new Callback() {
                @Override
                public void onComplete(String response) {
                    long id = -1;
                    try {
                        JSONArray obj = new JSONArray(response);
                        if (obj.length() > 0)
                            id = obj.optJSONObject(0).optLong("id", -1);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Callback cb = new Callback() {
                        @Override
                        public void onComplete(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                if (obj.optString("device_id").equals(device_id))
                                    preferences.edit().putBoolean("gcm_token_sent", true).apply();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    if (id >= 0)
                        handler.PutAsync("gcm-registrations/" + id + "/", json, cb);
                    else
                        handler.PostAsync("gcm-registrations/", json, cb);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}