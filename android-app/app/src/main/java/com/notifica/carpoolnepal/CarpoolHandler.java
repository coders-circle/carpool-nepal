package com.notifica.carpoolnepal;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

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
import java.util.TimeZone;

public class CarpoolHandler {
    private final String mUsername;
    private final String mPassword;

    private List<Long> userIds = new ArrayList<>();

    public CarpoolHandler(String username, String password) {
        mUsername = username;
        mPassword = password;
    }

    public void postCarpool(final Carpool carpool, final Callback callback) {
        NetworkHandler handler = new NetworkHandler(mUsername, mPassword);

        JSONObject carpoolObj = new JSONObject();
        try {
            carpoolObj.put("carpool_type", carpool.type);
            carpoolObj.put("source", carpool.source);
            carpoolObj.put("destination", carpool.destination);
            carpoolObj.put("description", carpool.description);
            carpoolObj.put("seats", carpool.seats);
            carpoolObj.put("status", carpool.status);
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
                        addCarpool(responseObj);
                        result = "Success";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    result = "Couldn't post carpool.";
                }
                if (callback != null)
                    callback.onComplete(result);

                refreshUsers(callback);
            }
        };

        if (carpool.remoteId < 0)
            handler.PostAsync("carpools/", carpoolObj, cb);
        else
            handler.PutAsync("carpools/" + carpool.remoteId + "/", carpoolObj, cb);
    }

    public void postComment(final Comment comment, final Callback callback) {
        NetworkHandler handler = new NetworkHandler(mUsername, mPassword);

        JSONObject postJson = new JSONObject();
        try {
            postJson.put("carpool", comment.carpool.remoteId);
            postJson.put("message", comment.message);
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
//                        comment.remoteId = responseObj.getLong("id");
//                        comment.postedOn = DateTimeToLong(responseObj.getString("posted_on"));
//                        comment.poster = addUser(responseObj.getLong("poster"));
//                        comment.save();
                        addComment(comment.carpool, responseObj);
                        result = "Success";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    result = "Couldn't post comment.";
                }
                if (callback != null)
                    callback.onComplete(result);

                refreshUsers(callback);
            }
        };

        if (comment.remoteId < 0)
            handler.PostAsync("comments/", postJson, cb);
        else
            handler.PutAsync("comments/" + comment.remoteId + "/", postJson, cb);
    }

    private User addUser(long remote_id) {
        List<User> users = User.find(User.class, "remote_id=?", remote_id + "");
        User user;
        if (users.size() == 0) {
            user = new User();
            user.remoteId = remote_id;
            user.save();
        }
        else
            user = users.get(0);
        userIds.add(user.getId());
        return user;
    }

    private void addCarpool(JSONObject carpool) {
        Carpool cp = new Carpool();

        cp.remoteId = carpool.optLong("id");

        List<Carpool> ocps = Carpool.find(Carpool.class, "remote_id=?", cp.remoteId + "");
        if (ocps.size() > 0)
            ocps.get(0).delete();

        cp.type = carpool.optInt("carpool_type");
        cp.source = carpool.optString("source");
        cp.destination = carpool.optString("destination");
        cp.description = carpool.optString("description");
        cp.seats = carpool.optInt("seats");
        cp.status = carpool.optInt("status");

        cp.time = TimeToLong(carpool.optString("time"));
        cp.date = DateToLong(carpool.optString("date"));
        cp.poster = addUser(carpool.optLong("poster"));
        cp.postedOn = DateTimeToLong(carpool.optString("posted_on"));
        cp.save();
    }

    private void getCarpool(long remote_id) {
        NetworkHandler handler = new NetworkHandler(mUsername, mPassword);
        handler.GetAsync("carpools/" + remote_id + "/", new Callback() {
            @Override
            public void onComplete(String response) {
                try {
                    JSONObject carpool = new JSONObject(response);
                    addCarpool(carpool);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getCarpools(String location, final Callback callback) {
        String url = "carpools/";

        if (location != null)
            url += "?location=" + Uri.encode(location);

        NetworkHandler handler = new NetworkHandler(mUsername, mPassword);
        handler.GetAsync(url, new Callback() {
            @Override
            public void onComplete(String response) {
                try {
                    JSONArray carpools = new JSONArray(response);
                    //DatabaseHelper.deleteAllCarpools();

                    for (int i = 0; i < carpools.length(); ++i) {
                        JSONObject carpool = carpools.optJSONObject(i);
                        if (carpool != null) {
                            addCarpool(carpool);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                refreshUsers(callback);

                if (callback != null)
                    callback.onComplete("Success");
            }
        });
    }

    public void addComment(Carpool carpool, JSONObject jsonObject) {
        Comment comment = new Comment();
        comment.remoteId = jsonObject.optLong("id");
        comment.carpool = carpool;
        comment.postedOn = DateTimeToLong(jsonObject.optString("posted_on"));
        comment.poster = addUser(jsonObject.optLong("poster"));
        comment.message = jsonObject.optString("message");
        comment.save();
    }

    public void getComments(final Carpool carpool, final Callback callback) {
        String url = "comments/";

        if (carpool != null)
            url += "?carpool=" + carpool.remoteId;

        NetworkHandler handler = new NetworkHandler(mUsername, mPassword);
        handler.GetAsync(url, new Callback() {
            @Override
            public void onComplete(String sResponse) {
                try {
                    JSONArray comments = new JSONArray(sResponse);
                    DatabaseHelper.deleteComments(carpool);

                    if (carpool == null)
                        Comment.deleteAll(Comment.class);
                    else
                        Comment.deleteAll(Comment.class, "carpool=?", carpool.remoteId +"");

                    for (int i=0; i<comments.length(); ++i) {
                        JSONObject responseObj = comments.optJSONObject(i);
                        if (responseObj != null) {
                            addComment(carpool, responseObj);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                refreshUsers(callback);


                if (callback != null)
                    callback.onComplete("Success");
            }
        });
    }

    public void refreshUsers(final Callback callback) {
        class GetUsersAsyncTask extends AsyncTask<Void, Void, String> {
            @Override
            protected String doInBackground(Void... params) {
                Iterator<Long> i = userIds.iterator();
                while (i.hasNext()) {
                    Long uid = i.next();
                    NetworkHandler handler = new NetworkHandler(mUsername, mPassword);
                    Long rem_uid = User.findById(User.class, uid).remoteId;
                    String response = handler.Get("users/" + rem_uid + "/");
                    try {
                        JSONObject userObj = new JSONObject(response);
                        long userId = userObj.optLong("id");
                        List<User> users = User.find(User.class, "remote_id=?", userId + "");
                        User user;
                        if (users.size() == 0) {
                            user = new User();
                            user.remoteId = userId;
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
                    i.remove();
                }
                return "Success";
            }

            @Override
            protected void onPostExecute(String result) {
                if (callback != null)
                    callback.onComplete(result);
            }
        }
        new GetUsersAsyncTask().execute();
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

    public static String LongDateToDayOfWeek(long date){
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE", Locale.US);
        return formatter.format(new Date(date));
    }

    public static String LongToDayMonthDate(long date){
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMMM d", Locale.US);
        return formatter.format(new Date(date));
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

    public static String LongToTime12(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a", Locale.US);
        return formatter.format(new Time(time));
    }

    public static long DateTimeToLong(String dateTimeString) {
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US);
        df1.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date date = df1.parse(dateTimeString);
            Log.d("date", date.toString());
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
