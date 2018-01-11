package com.example.eqvol.eqvola.JsonResponse;

import com.example.eqvol.eqvola.Classes.Transfer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.util.List;

/**
 * Created by eqvol on 25.10.2017.
 */

public class JsonResponseTransfers {
    public String status;
    public List<Transfer> data;
    public static List<Transfer> getTransfers(String json)
    {
        Gson gson = new GsonBuilder().create();
        JsonReader reader = new JsonReader(new StringReader(json));
        reader.setLenient(true);
        JsonResponseTransfers response = gson.fromJson(reader, JsonResponseTransfers.class);


        return response.data;
    }
}
