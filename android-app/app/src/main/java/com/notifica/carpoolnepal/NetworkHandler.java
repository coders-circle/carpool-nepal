package com.notifica.carpoolnepal;

import android.os.AsyncTask;
import android.util.Base64;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class NetworkHandler {
    private final String BASE_URL = "http://192.168.0.40:8000/carpool/";
    public final String ERR_CONNECTION = "{ \"detail\": \"Error on connection.\" }";

    private final String username, password;

    public NetworkHandler(String username, String password) {
        this.username = username;
        this.password = password;
    }

    private String Request(HttpUriRequest request) {
        HttpClient client = new DefaultHttpClient();
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Accept", "application/json");

        String authorizationString = "Basic " + Base64.encodeToString(
                (username + ":" + password).getBytes(),
                Base64.DEFAULT);
        request.setHeader("Authorization", authorizationString);

        HttpResponse response;
        String result = "";
        try {
            response = client.execute(request);
            if (response != null) {
                InputStream is = response.getEntity().getContent();
                result = convertStreamToString(is);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = ERR_CONNECTION;
        }

        return result;
    }

    public String Get(String url) {
        HttpGet request = new HttpGet(BASE_URL + url);
        return Request(request);
    }

    public String Post(String address, JSONObject jsonObject) {
        HttpPost request = new HttpPost(BASE_URL + address);
        try {
            request.setEntity(new StringEntity(jsonObject.toString()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Request(request);
    }

    public String Put(String address, JSONObject jsonObject) {
        HttpPut request = new HttpPut(BASE_URL + address);
        try {
            request.setEntity(new StringEntity(jsonObject.toString()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Request(request);
    }

    private String Delete(String url) {
        HttpDelete request = new HttpDelete(BASE_URL + url);
        return Request(request);
    }

    public void GetAsync(final String url, final Callback callback) {
        class GetAsyncTask extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... params) {
                return Get(url);
            }

            @Override
            protected void onPostExecute(String result) {
                if (callback != null)
                    callback.onComplete(result);
            }
        }
        new GetAsyncTask().execute();
    }

    public void PostAsync(final String url, final JSONObject data, final Callback callback) {
        class GetAsyncTask extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... params) {
                return Post(url, data);
            }

            @Override
            protected void onPostExecute(String result) {
                if (callback != null)
                    callback.onComplete(result);
            }
        }
        new GetAsyncTask().execute();
    }

    public void PutAsync(final String url, final JSONObject data, final Callback callback) {
        class GetAsyncTask extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... params) {
                return Put(url, data);
            }

            @Override
            protected void onPostExecute(String result) {
                if (callback != null)
                    callback.onComplete(result);
            }
        }
        new GetAsyncTask().execute();
    }

    public void DeleteAsync(final String url, final Callback callback) {
        class GetAsyncTask extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... params) {
                return Delete(url);
            }

            @Override
            protected void onPostExecute(String result) {
                if (callback != null)
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
