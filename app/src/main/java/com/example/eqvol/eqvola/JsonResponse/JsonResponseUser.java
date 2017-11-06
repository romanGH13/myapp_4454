package com.example.eqvol.eqvola.JsonResponse;

import com.example.eqvol.eqvola.Classes.User;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.List;

/**
 * Created by eqvol on 18.10.2017.
 */

public class JsonResponseUser {
    public String status;
    public List<User> data;
    public static User getUser(String json)
    {
        Gson gson = new Gson();
        //User user;
        JsonElement element = new JsonParser().parse(json);
        JsonResponseUser response = gson.fromJson(element, JsonResponseUser.class);

        for(User entry :response.data)
        {
            return entry;
        }
        return null;
    }
}