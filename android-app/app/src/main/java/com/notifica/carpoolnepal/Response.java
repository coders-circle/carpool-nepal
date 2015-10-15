package com.notifica.carpoolnepal;

import com.orm.SugarRecord;

public class Response extends SugarRecord<Response> {
    Carpool carpool;
    User poster;
    int urgency;
    String message;
    long posted_on;
}
