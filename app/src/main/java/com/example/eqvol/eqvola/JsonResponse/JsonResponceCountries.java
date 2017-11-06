package com.example.eqvol.eqvola.JsonResponse;

import com.example.eqvol.eqvola.Classes.Country;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eqvol on 23.10.2017.
 */

public class JsonResponceCountries {
    public String status;
    public List<Country> data;
    public static List<Country> getCountries(String json)
    {
        Gson gson = new Gson();
        //Country country;
        JsonElement element = new JsonParser().parse(json);
        JsonResponceCountries response = gson.fromJson(element, JsonResponceCountries.class);
        List<Country> list = new ArrayList<Country>();
        for(Country entry :response.data)
        {
            list.add(entry);
        }

        return list;
    }
}
