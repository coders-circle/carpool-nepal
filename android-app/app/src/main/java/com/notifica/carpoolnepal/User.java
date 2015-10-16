package com.notifica.carpoolnepal;

import com.orm.SugarRecord;

public class User extends SugarRecord<User> {
    String firstName;
    String lastName;
    String userName;
    String email;
    String contactNumber;
    String contactAddress;

    long remote_id;
}
