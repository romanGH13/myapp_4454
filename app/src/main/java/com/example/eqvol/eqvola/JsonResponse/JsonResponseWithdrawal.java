package com.example.eqvol.eqvola.JsonResponse;

import com.example.eqvol.eqvola.Classes.Withdrawal;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.util.List;

/**
 * Created by eqvol on 29.12.2017.
 */

public class JsonResponseWithdrawal {
    public String status;
    public List<Withdrawal> data;
    public static List<Withdrawal> getWithdrawals(String json)
    {

        Gson gson = new GsonBuilder().create();
        JsonReader reader = new JsonReader(new StringReader(json));
        reader.setLenient(true);
        JsonResponseWithdrawal response = gson.fromJson(reader, JsonResponseWithdrawal.class);
        return response.data;
    }
}