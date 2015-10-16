package com.notifica.carpoolnepal;

import java.util.List;

public class DatabaseHelper {

    public static void deleteResponses(Carpool carpool) {
        List<Response> responses;
        if (carpool == null)
            responses = Response.listAll(Response.class);
        else
            responses = Response.find(Response.class, "carpool=?", carpool.getId()+"");

        for (Response response: responses) {
            Reply.deleteAll(Reply.class, "response=?", response.getId()+"");
            response.delete();
        }
    }

    public static void deleteCarpool(Carpool carpool) {
        deleteResponses(carpool);
        carpool.delete();
    }

    public static void deleteAllCarpools() {
        List<Carpool> carpools = Carpool.listAll(Carpool.class);
        for (Carpool carpool: carpools)
            deleteCarpool(carpool);
    }
}
