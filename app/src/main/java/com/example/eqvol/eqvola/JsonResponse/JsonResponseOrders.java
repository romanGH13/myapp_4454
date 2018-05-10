package com.example.eqvol.eqvola.JsonResponse;

import com.example.eqvol.eqvola.Classes.Order;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.util.List;

/**
 * Created by eqvol on 18.12.2017.
 */

public class JsonResponseOrders {
    public String status;
    public List<Order> data;

    public static List<Order> getOrders(String json) {
        Gson gson = new GsonBuilder().create();
        JsonReader reader = new JsonReader(new StringReader(json));
        reader.setLenient(true);
        JsonResponseOrders response = gson.fromJson(reader, JsonResponseOrders.class);

        return response.data;
    }
}