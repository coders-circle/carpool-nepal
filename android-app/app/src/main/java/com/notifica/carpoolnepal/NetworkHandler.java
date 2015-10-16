package com.notifica.carpoolnepal;

import android.os.AsyncTask;
import android.util.Base64;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class NetworkHandler {
    private final String BASE_URL = "http://192.168.0.40:8000/carpool/";
    public final String ERR_CONNECTION = "{ \"detail\": \"Error on connection.\" }";
    public final String ERR_RESPONSE = "{ \"detail\": \"Unknown response from server.\" }";
    public JSONObject ERR_RESPONSE_JSON;

    private final String username, password;

    public NetworkHandler(String username, String password) {
        this.username = username;
        this.password = password;

        try {
            ERR_RESPONSE_JSON = new JSONObject(ERR_RESPONSE);
        } catch (JSONException e) {
            ERR_RESPONSE_JSON = new JSONObject();
            e.printStackTrace();
        }
    }

    private JSONObject Get(String url) {
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        String result = "";
        try {
            HttpGet request = new HttpGet(BASE_URL + url);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Accept", "application/json");

            String authorizationString = "Basic " + Base64.encodeToString(
                    (username + ":" + password).getBytes(),
                    Base64.DEFAULT);
            request.setHeader("Authorization", authorizationString);

            response = client.execute(request);
            if (response != null) {
                InputStream is = response.getEntity().getContent();
                result = convertStreamToString(is);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = ERR_CONNECTION;
        }

        JSONObject jsonResult;
        try {
            jsonResult = new JSONObject(result);
        } catch (Exception e) {
            e.printStackTrace();
            jsonResult = ERR_RESPONSE_JSON;
        }
        return jsonResult;
    }

    public JSONObject Post(String address, JSONObject jsonObject) {
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        String result = "";
        try {
            HttpPost request = new HttpPost(BASE_URL + address);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Accept", "application/json");

            String authorizationString = "Basic " + Base64.encodeToString(
                    (username + ":" + password).getBytes(),
                    Base64.DEFAULT);
            request.setHeader("Authorization", authorizationString);

            request.setEntity(new StringEntity(jsonObject.toString()));
            response = client.execute(request);
            if(response!=null){
                InputStream in = response.getEntity().getContent();
                result = convertStreamToString(in);
            }
        } catch(Exception e) {
            e.printStackTrace();
            result = ERR_CONNECTION;
        }

        JSONObject jsonResult;
        try {
            jsonResult = new JSONObject(result);
        } catch (Exception e) {
            e.printStackTrace();
            jsonResult = ERR_RESPONSE_JSON;
        }
        return jsonResult;
    }

    public JSONObject Put(String address, JSONObject jsonObject) {
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        String result = "";
        try {
            HttpPut request = new HttpPut(BASE_URL + address);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Accept", "application/json");

            String authorizationString = "Basic " + Base64.encodeToString(
                    (username + ":" + password).getBytes(),
                    Base64.DEFAULT);
            request.setHeader("Authorization", authorizationString);

            request.setEntity(new StringEntity(jsonObject.toString()));
            response = client.execute(request);
            if(response!=null){
                InputStream in = response.getEntity().getContent();
                result = convertStreamToString(in);
            }
        } catch(Exception e) {
            e.printStackTrace();
            result = ERR_CONNECTION;
        }

        JSONObject jsonResult;
        try {
            jsonResult = new JSONObject(result);
        } catch (Exception e) {
            e.printStackTrace();
            jsonResult = ERR_RESPONSE_JSON;
        }
        return jsonResult;
    }

    private JSONObject Delete(String url) {
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        String result = "";
        try {
            HttpDelete request = new HttpDelete(BASE_URL + url);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Accept", "application/json");

            String authorizationString = "Basic " + Base64.encodeToString(
                    (username + ":" + password).getBytes(),
                    Base64.DEFAULT);
            request.setHeader("Authorization", authorizationString);

            response = client.execute(request);
            if (response != null) {
                InputStream is = response.getEntity().getContent();
                result = convertStreamToString(is);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = ERR_CONNECTION;
        }

        JSONObject jsonResult;
        try {
            jsonResult = new JSONObject(result);
        } catch (Exception e) {
            e.printStackTrace();
            jsonResult = ERR_RESPONSE_JSON;
        }
        return jsonResult;
    }

    public void GetAsync(final String url, final Callback callback) {
        class GetAsyncTask extends AsyncTask<Void, Void, JSONObject> {

            @Override
            protected JSONObject doInBackground(Void... params) {
                return Get(url);
            }

            @Override
            protected void onPostExecute(JSONObject result) {
                if (callback != null)
                if (result.equals(ERR_CONNECTION))
                    callback.onComplete(result);
                else
                    callback.onComplete(result);
            }
        }
        new GetAsyncTask().execute();
    }

    public void PostAsync(final String url, final JSONObject data, final Callback callback) {
        class GetAsyncTask extends AsyncTask<Void, Void, JSONObject> {

            @Override
            protected JSONObject doInBackground(Void... params) {
                return Post(url, data);
            }

            @Override
            protected void onPostExecute(JSONObject result) {
                if (callback != null)
                if (result.equals(ERR_CONNECTION))
                    callback.onComplete(result);
                else
                    callback.onComplete(result);
            }
        }
        new GetAsyncTask().execute();
    }

    public void PutAsync(final String url, final JSONObject data, final Callback callback) {
        class GetAsyncTask extends AsyncTask<Void, Void, JSONObject> {

            @Override
            protected JSONObject doInBackground(Void... params) {
                return Put(url, data);
            }

            @Override
            protected void onPostExecute(JSONObject result) {
                if (callback != null)
                if (result.equals(ERR_CONNECTION))
                    callback.onComplete(result);
                else
                    callback.onComplete(result);
            }
        }
        new GetAsyncTask().execute();
    }

    public void DeleteAsync(final String url, final Callback callback) {
        class GetAsyncTask extends AsyncTask<Void, Void, JSONObject> {

            @Override
            protected JSONObject doInBackground(Void... params) {
                return Delete(url);
            }

            @Override
            protected void onPostExecute(JSONObject result) {
                if (callback != null)
                if (result.equals(ERR_CONNECTION))
                    callback.onComplete(result);
                else
                    callback.onComplete(result);
            }
        }
        new GetAsyncTask().execute();
    }

    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}
