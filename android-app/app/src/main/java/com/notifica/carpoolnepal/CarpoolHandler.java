package com.notifica.carpoolnepal;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CarpoolHandler {
    private final String mUsername;
    private final String mPassword;

    private List<Long> userIds = new ArrayList<>();

    public CarpoolHandler(String username, String password) {
        mUsername = username;
        mPassword = password;
    }

    public void GetCarpools(String location) {
        String url = "carpools/";

        if (location != null)
            url += "?location=" + Uri.encode(location);

        NetworkHandler handler = new NetworkHandler(mUsername, mPassword);
        handler.GetAsync(url, new Callback() {
            @Override
            public void onComplete(boolean cancelled, String response) {
                if (!cancelled) {
                    try {
                        JSONArray carpools = new JSONArray(response);

                        Carpool.deleteAll(Carpool.class);
                        for (int i=0; i<carpools.length(); ++i) {
                            JSONObject carpool = carpools.optJSONObject(i);
                            if (carpool != null) {
                                Carpool cp = new Carpool();

                                cp.remote_id = carpool.optLong("id");
                                cp.type = carpool.optInt("carpool_type");
                                cp.source = carpool.optString("source");
                                cp.destination = carpool.optString("destination");
                                cp.seats = carpool.optInt("seats");

                                cp.time = TimeToLong(carpool.optString("time"));
                                cp.date = DateToLong(carpool.optString("date"));

                                long userId = carpool.optLong("poster");

                                if (User.count(User.class, "remote_id=?", new String[] { ""+userId }) == 0) {
                                    User user = new User();
                                    user.remote_id = userId;
                                    user.save();

                                    userIds.add(user.getId());
                                }

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }


    public static long DateToLong(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        try {
            Date date = formatter.parse(dateString);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long TimeToLong(String timeString) {
        return Time.valueOf(timeString).getTime();
    }

    public static String LongToTime(long time) {
        Time t = new Time(time);
        return t.toString();
    }
}
