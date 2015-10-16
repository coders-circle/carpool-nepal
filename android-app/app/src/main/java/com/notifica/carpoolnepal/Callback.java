package com.notifica.carpoolnepal;

import org.json.JSONObject;

public interface Callback {
    void onComplete(JSONObject response);
}
