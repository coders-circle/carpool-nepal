package com.notifica.carpoolnepal;

import com.orm.SugarRecord;

public class User extends SugarRecord<User> {
    String firstName;
    String lastName;
    String userName;
    String email;
    Long contactNumber;
    String contactAddress;

    long remoteId;
}
