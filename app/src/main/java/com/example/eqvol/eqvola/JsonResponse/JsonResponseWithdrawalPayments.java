package com.example.eqvol.eqvola.JsonResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.example.eqvol.eqvola.Classes.PaymentSystem;

import java.io.StringReader;
import java.util.List;

/**
 * Created by eqvol on 23.10.2017.
 */

public class JsonResponseWithdrawalPayments {
    public List<PaymentSystem> data;
    public static List<PaymentSystem> getWithdrawalPayments(String json)
    {
        Gson gson = new GsonBuilder().create();
        JsonReader reader = new JsonReader(new StringReader(json));
        reader.setLenient(true);
        JsonResponseWithdrawalPayments response = gson.fromJson(reader, JsonResponseWithdrawalPayments.class);

        return response.data;
    }
}
