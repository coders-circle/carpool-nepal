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

    public void PostCarpool(final Carpool carpool, final Callback callback) {
        NetworkHandler handler = new NetworkHandler(mUsername, mPassword);

        JSONObject carpoolObj = new JSONObject();
        try {
            carpoolObj.put("carpool_type", carpool.type);
            carpoolObj.put("source", carpool.source);
            carpoolObj.put("destination", carpool.destination);
            carpoolObj.put("description", carpool.description);
            carpoolObj.put("seats", carpool.seats);
            carpoolObj.put("time", LongToTime(carpool.time));
            carpoolObj.put("date", LongToDate(carpool.date));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Callback cb = new Callback() {
            @Override
            public void onComplete(String response) {
                String result;
                try {
                    JSONObject responseObj = new JSONObject(response);

                    if (responseObj.has("detail"))
                        result = responseObj.getString("detail");
                    else {
                        carpool.remote_id = responseObj.getLong("id");
                        carpool.save();
                        result = "Success";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    result = "Couldn't post carpool.";
                }
                if (callback != null)
                    callback.onComplete(result);
            }
        };

        if (carpool.remote_id < 0)
            handler.PostAsync("carpools/", carpoolObj, cb);
        else
            handler.PutAsync("carpools/" + carpool.remote_id + "/", carpoolObj, cb);
    }

    public void PostCarpool(final Response response, final Callback callback) {
        NetworkHandler handler = new NetworkHandler(mUsername, mPassword);

        JSONObject responseObj = new JSONObject();
        try {
            responseObj.put("carpool", response.carpool.remote_id);
            responseObj.put("urgency", response.urgency);
            responseObj.put("message", response.message);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Callback cb = new Callback() {
            @Override
            public void onComplete(String sResponse) {
                String result;
                try {
                    JSONObject responseObj = new JSONObject(sResponse);

                    if (responseObj.has("detail"))
                        result = responseObj.getString("detail");
                    else {
                        response.remote_id = responseObj.getLong("id");
                        response.save();
                        result = "Success";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    result = "Couldn't post response.";
                }
                if (callback != null)
                    callback.onComplete(result);
            }
        };

        if (response.remote_id < 0)
            handler.PostAsync("responses/", responseObj, cb);
        else
            handler.PutAsync("responses/" + response.remote_id + "/", responseObj, cb);
    }

    public void PostCarpool(final Reply reply, final Callback callback) {
        NetworkHandler handler = new NetworkHandler(mUsername, mPassword);

        JSONObject replyObj = new JSONObject();
        try {
            replyObj.put("response", reply.response.remote_id);
            replyObj.put("message", reply.message);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Callback cb = new Callback() {
            @Override
            public void onComplete(String response) {
                String result;
                try {
                    JSONObject responseObj = new JSONObject(response);

                    if (responseObj.has("detail"))
                        result = responseObj.getString("detail");
                    else {
                        reply.remote_id = responseObj.getLong("id");
                        reply.save();
                        result = "Success";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    result = "Couldn't post reply.";
                }
                if (callback != null)
                    callback.onComplete(result);
            }
        };

        if (reply.remote_id < 0)
            handler.PostAsync("replies/", replyObj, cb);
        else
            handler.PutAsync("replies/" + reply.remote_id + "/", replyObj, cb);
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
            public void onComplete(String response) {
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
            public void onComplete(String sResponse) {
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
                            response.remote_id = responseObj.optLong("id");
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
                                reply.remote_id = replyObj.optLong("id");
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

    private void RefreshUsers() {
        Iterator<Long> i = userIds.iterator();
        while (i.hasNext()) {
            Long uid = i.next();
            NetworkHandler handler = new NetworkHandler(mUsername, mPassword);
            handler.GetAsync("users/" + uid, new Callback() {
                @Override
                public void onComplete(String response) {
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
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        try {
            Date date = formatter.parse(dateString);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String LongToDate(long date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
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
