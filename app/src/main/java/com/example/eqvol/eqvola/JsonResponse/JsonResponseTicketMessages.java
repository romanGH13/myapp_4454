package com.example.eqvol.eqvola.JsonResponse;

import com.example.eqvol.eqvola.Classes.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.util.List;

/**
 * Created by eqvol on 06.11.2017.
 */

public class JsonResponseTicketMessages {
    public String status;
    public List<Message> data;
    public static List<Message> getMessages(String json)
    {
        Gson gson = new GsonBuilder().create();
        JsonReader reader = new JsonReader(new StringReader(json));
        reader.setLenient(true);
        JsonResponseTicketMessages response = gson.fromJson(reader, JsonResponseTicketMessages.class);

        return response.data;
    }
}
