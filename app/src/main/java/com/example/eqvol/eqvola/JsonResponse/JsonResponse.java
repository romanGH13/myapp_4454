package com.example.eqvol.eqvola.JsonResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.util.List;

/**
 * Created by eqvol on 29.12.2017.
 */

public class JsonResponse<T> {
    public String status;
    public List<T> data;


    public JsonResponse(T myClass, String json)
    {
        Gson gson = new GsonBuilder().create();
        JsonReader reader = new JsonReader(new StringReader(json));
        reader.setLenient(true);
        JsonResponse response = gson.fromJson(reader, JsonResponse.class);
        status = response.status;
        data = response.data;
    }

    public List<T> getData() {

        return data;
    }
}