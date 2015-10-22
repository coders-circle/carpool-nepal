package com.notifica.carpoolnepal;

import java.util.List;

public class DatabaseHelper {

    public static void deleteComments(Carpool carpool) {
        Comment.deleteAll(Comment.class, "carpool=?", carpool.getId()+"");
    }

    public static void deleteCarpool(Carpool carpool) {
        deleteComments(carpool);
        carpool.delete();
    }

    public static void deleteAllCarpools() {
        List<Carpool> carpools = Carpool.listAll(Carpool.class);
        for (Carpool carpool: carpools)
            deleteCarpool(carpool);
    }

    public static User getUser(String username) {
        return User.find(User.class, "user_name=?", username).get(0);
    }
}
