package com.notifica.carpoolnepal;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginHandler {

    /*
    Returns:
    {"user":"TestUser","auth":"None"} on success
    {"detail":"Error-String"} on failure
     */
    public static void Login(String username, String password, Callback callback) {
        NetworkHandler handler = new NetworkHandler(username, password);
        handler.GetAsync("authenticate", callback);
    }

    /*
    Returns:
    User details on success ({ "user" : {...}, "id":.., "contact_number":..., "contact_address":... })
    {"detail":"Error-String"} on failure
    */
    public static void Register(String username, String password,
                         String firstName, String lastName, String email,
                         int contactNumber, String contactAddress,
                         Callback callback) {
        /*{
            "user": {
                "first_name": "",
                "last_name": "",
                "username": "",
                "password": "",
                "email": ""
            },
            "contact_number": null,
            "contact_address": ""
        }*/
        try {
            JSONObject data = new JSONObject();

            JSONObject user = new JSONObject();
            user.put("first_name", firstName);
            user.put("last_name", lastName);
            user.put("user_name", username);
            user.put("password", password);
            user.put("email", email);

            data.put("user", user);
            data.put("contact_number", contactNumber);
            data.put("contact_address", contactAddress);

            NetworkHandler handler = new NetworkHandler("nothing", "nothing");
            handler.PostAsync("users", data, callback);
        } catch (Exception ex) {
            if (callback != null) {
                JSONObject registerErr = new JSONObject();
                try {
                    registerErr = new JSONObject("{ \"detail\": \"Error creating registration data.\" }");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.onComplete(registerErr);
            }
        }

    }
}
