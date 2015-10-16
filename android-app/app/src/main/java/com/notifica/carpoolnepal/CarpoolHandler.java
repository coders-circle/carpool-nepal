package com.notifica.carpoolnepal;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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

    private User AddUser(long remote_id) {
        List<User> users = User.find(User.class, "remote_id=?", remote_id + "");
        User user;
        if (users.size() == 0) {
            user = new User();
            user.remote_id = remote_id;
            user.save();
        }
        else
            user = users.get(0);
        userIds.add(user.getId());
        return user;
    }

    public void GetCarpools(String location) {
        String url = "carpools/";

        if (location != null)
            url += "?location=" + Uri.encode(location);

        NetworkHandler handler = new NetworkHandler(mUsername, mPassword);
        handler.GetAsync(url, new Callback() {
            @Override
            public void onComplete(boolean cancelled, String response) {
                if (!cancelled)
                    return;

                try {
                    JSONArray carpools = new JSONArray(response);
                    DatabaseHelper.deleteAllCarpools();

                    for (int i = 0; i < carpools.length(); ++i) {
                        JSONObject carpool = carpools.optJSONObject(i);
                        if (carpool != null) {
                            Carpool cp = new Carpool();

                            cp.remote_id = carpool.optLong("id");
                            cp.type = carpool.optInt("carpool_type");
                            cp.source = carpool.optString("source");
                            cp.destination = carpool.optString("destination");
                            cp.description = carpool.optString("description");
                            cp.seats = carpool.optInt("seats");

                            cp.time = TimeToLong(carpool.optString("time"));
                            cp.date = DateToLong(carpool.optString("date"));
                            cp.poster = AddUser(carpool.optLong("poster"));
                            cp.save();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RefreshUsers();
            }
        });
    }

    public void GetResponses(final Carpool carpool) {
        String url = "responses/";

        if (carpool != null)
            url += "?carpool=" + carpool.remote_id;

        NetworkHandler handler = new NetworkHandler(mUsername, mPassword);
        handler.GetAsync(url, new Callback() {
            @Override
            public void onComplete(boolean cancelled, String sResponse) {
                if (cancelled)
                    return;
                try {
                    JSONArray responses = new JSONArray(sResponse);
                    DatabaseHelper.deleteResponses(carpool);

                    if (carpool == null)
                        Response.deleteAll(Response.class);
                    else
                        Response.deleteAll(Response.class, "carpool=?", carpool.remote_id+"");

                    for (int i=0; i<responses.length(); ++i) {
                        JSONObject responseObj = responses.optJSONObject(i);
                        if (responseObj != null) {
                            Response response = new Response();
                            response.carpool = carpool;
                            response.posted_on = DateTimeToLong(responseObj.optString("posted_on"));
                            response.poster = AddUser(responseObj.optLong("poster"));
                            response.urgency = responseObj.optInt("urgency");
                            response.message = responseObj.optString("message");
                            response.save();

                            JSONArray replies = responseObj.optJSONArray("replies");
                            if (replies != null)
                            for (int j=0; j<replies.length(); ++j) {
                                JSONObject replyObj = replies.optJSONObject(j);
                                Reply reply = new Reply();
                                reply.response = response;
                                reply.posted_on = DateTimeToLong(replyObj.optString("posted_on"));
                                reply.message = replyObj.optString("message");
                                reply.poster = AddUser(replyObj.optLong("poster"));
                                reply.save();
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RefreshUsers();
            }
        });
    }

    public void RefreshUsers() {
        Iterator<Long> i = userIds.iterator();
        while (i.hasNext()) {
            Long uid = i.next();
            NetworkHandler handler = new NetworkHandler(mUsername, mPassword);
            handler.GetAsync("users/" + uid, new Callback() {
                @Override
                public void onComplete(boolean cancelled, String response) {
                    if (cancelled)
                        return;
                    try {
                        JSONObject userObj = new JSONObject(response);
                        long userId = userObj.optLong("id");
                        List<User> users = User.find(User.class, "remote_id=?", userId + "");
                        User user;
                        if (users.size() == 0) {
                            user = new User();
                            user.remote_id = userId;
                        }
                        else
                            user = users.get(0);

                        JSONObject iUserObj = userObj.optJSONObject("user");
                        user.firstName = iUserObj.optString("first_name");
                        user.lastName = iUserObj.optString("last_name");
                        user.userName = iUserObj.optString("username");
                        user.email = iUserObj.optString("email");
                        user.contactAddress = userObj.optString("contact_address");
                        user.contactNumber = userObj.optLong("contact_number");

                        user.save();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            i.remove();
        }

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

    public static String LongToDate(long date) {
        DateFormat formatter = SimpleDateFormat.getDateInstance();
        return formatter.format(new Date(date));
    }

    public static long TimeToLong(String timeString) {
        return Time.valueOf(timeString).getTime();
    }

    public static String LongToTime(long time) {
        Time t = new Time(time);
        return t.toString();
    }

    public static long DateTimeToLong(String dateTimeString) {
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
        try {
            Date date = df1.parse(dateTimeString);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String LongToDateTime(long dateTime) {
        DateFormat formatter = SimpleDateFormat.getDateTimeInstance();
        return formatter.format(new Date(dateTime));
    }
}
