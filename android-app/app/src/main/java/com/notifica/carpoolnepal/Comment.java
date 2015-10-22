package com.notifica.carpoolnepal;

import com.orm.SugarRecord;

public class Comment extends SugarRecord<Comment> {
    public User poster;
    public Carpool carpool;
    public String message;
    public long postedOn;

    public long remoteId = -1;
}
