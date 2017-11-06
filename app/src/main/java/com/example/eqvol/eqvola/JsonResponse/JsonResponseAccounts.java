package com.example.eqvol.eqvola.JsonResponse;

import com.example.eqvol.eqvola.Classes.Account;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eqvol on 26.10.2017.
 */

public class JsonResponseAccounts {
    public String status;
    public List<Account> data;
    public static List<Account> getAccounts(String json)
    {
        Gson gson = new Gson();
        JsonElement element = new JsonParser().parse(json);
        JsonResponseAccounts response = gson.fromJson(element, JsonResponseAccounts.class);
        List<Account> list = new ArrayList<Account>();
        for(Account entry :response.data)
        {
            list.add(entry);
        }

        return list;
    }
}
