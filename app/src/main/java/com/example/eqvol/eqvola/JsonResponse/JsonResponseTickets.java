package com.example.eqvol.eqvola.JsonResponse;

import com.example.eqvol.eqvola.Classes.Ticket;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;
import java.util.Map;

/**
 * Created by eqvol on 31.10.2017.
 */

public class JsonResponseTickets{
    public String status;
    public JsonResponseTicketsData data;


    public JsonResponseTickets(String json) {

        Gson gson = new GsonBuilder().serializeNulls().setLenient().create();
        JsonResponseTickets response = null;
        try {
            JsonElement element = new JsonParser().parse(json);
            JsonObject obj = element.getAsJsonObject();
            response = gson.fromJson(obj, JsonResponseTickets.class);

        }
        catch(Exception ex)
        {
            String str1 = ex.getMessage();
        }
        //String str = response.status;
        this.status = response.status;
        this.data = response.data;
    }

    public List<Ticket> getTickets()
    {
        return this.data.getTickets();
    }

    public int getLastMessages()
    {
        return this.data.getLast_messages();
    }

}
