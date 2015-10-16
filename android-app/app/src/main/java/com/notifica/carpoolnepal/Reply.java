package com.notifica.carpoolnepal;

import com.orm.SugarRecord;

public class Reply extends SugarRecord<Reply> {
    String message;
    User poster;
    Response response;
    long posted_on;

    long remote_id = -1;
}
