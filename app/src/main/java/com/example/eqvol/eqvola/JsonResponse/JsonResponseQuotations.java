package com.example.eqvol.eqvola.JsonResponse;

import com.example.eqvol.eqvola.Classes.Quotation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.util.List;

/**
 * Created by eqvol on 23.10.2017.
 */

public class JsonResponseQuotations {
    public List<Quotation> data;
    public static List<Quotation> getQuotation(String json)
    {
        Gson gson = new GsonBuilder().create();
        JsonReader reader = new JsonReader(new StringReader(json));
        reader.setLenient(true);
        JsonResponseQuotations response = gson.fromJson(reader, JsonResponseQuotations.class);

        return response.data;
    }
}
