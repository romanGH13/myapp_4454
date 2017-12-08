package com.example.eqvol.eqvola.JsonResponse;

import com.example.eqvol.eqvola.Classes.Category;
import com.example.eqvol.eqvola.Classes.Group;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.util.List;

/**
 * Created by eqvol on 30.10.2017.
 */

public class JsonResponseCategories {
    public String status;
    public List<Category> data;
    public static List<Category> getCategories(String json)
    {
        Gson gson = new GsonBuilder().create();
        JsonReader reader = new JsonReader(new StringReader(json));
        reader.setLenient(true);
        JsonResponseCategories response = gson.fromJson(reader, JsonResponseCategories.class);
        /*Gson gson = new Gson();
        JsonElement element = new JsonParser().parse(json);
        JsonResponseCategories response = null;
        try {
            response = gson.fromJson(element, JsonResponseCategories.class);
        }catch(Exception ex){

        }*/

        return response.data;
    }
}
