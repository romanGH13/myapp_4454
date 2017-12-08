package com.example.eqvol.eqvola.JsonResponse;

import com.example.eqvol.eqvola.Classes.Group;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.util.List;

/**
 * Created by eqvol on 25.10.2017.
 */

public class JsonResponseGroups {
    public String status;
    public List<Group> data;
    public static List<Group> getGroups(String json)
    {
        Gson gson = new GsonBuilder().create();
        JsonReader reader = new JsonReader(new StringReader(json));
        reader.setLenient(true);
        JsonResponseGroups response = gson.fromJson(reader, JsonResponseGroups.class);

        /*Gson gson = new Gson();
        JsonElement element = new JsonParser().parse(json);
        JsonResponseGroups response = gson.fromJson(element, JsonResponseGroups.class);
        String str = "";*/

        return response.data;
    }
}
