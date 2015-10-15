package com.notifica.carpoolnepal;

import com.orm.SugarRecord;

public class Carpool extends SugarRecord<Carpool> {
    int type;
    String source;
    String destination;
    String description;
    int seats;
    User poster;
    long time;
    long date;
}
