package com.example.eqvol.eqvola.JsonResponse;

import com.example.eqvol.eqvola.Classes.Notification;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.util.List;

/**
 * Created by eqvol on 19.12.2017.
 */

public class JsonResponseNotification {
    public String status;
    public List<Notification> data;
    public static List<Notification> getNotifications(String json)
    {
        Gson gson = new GsonBuilder().create();
        JsonReader reader = new JsonReader(new StringReader(json));
        reader.setLenient(true);
        JsonResponseNotification response = gson.fromJson(reader, JsonResponseNotification.class);
        return response.data;
    }
}