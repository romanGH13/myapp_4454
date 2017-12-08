package com.example.eqvol.eqvola.JsonResponse;

import com.example.eqvol.eqvola.Classes.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eqvol on 18.10.2017.
 */

public class JsonResponseUser implements JsonDeserializer<JsonResponseUser> {
    public String status;
    public List<User> data;
    public static User getUser(String json)
    {
        Gson gson = new GsonBuilder().create();
        JsonReader reader = new JsonReader(new StringReader(json));
        reader.setLenient(true);
        JsonResponseUser response = gson.fromJson(reader, JsonResponseUser.class);

        /*GsonBuilder gsonBuilder = new GsonBuilder();

        JsonDeserializer<JsonResponseUser> deserializer = new JsonResponseUser() ; // will implement in a second
        gsonBuilder.registerTypeAdapter(JsonResponseUser.class, deserializer);

        Gson customGson = gsonBuilder.create();
        JsonResponseUser response = customGson.fromJson(json, JsonResponseUser.class);*/

        for(User entry :response.data)
        {
            return entry;
        }
        return null;
    }


    @Override
    public JsonResponseUser deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        List<User> users = new ArrayList<User>();
        this.status = jsonObject.get("status").getAsString();
        JsonArray data = jsonObject.get("data").getAsJsonArray();
        for(int i = 0; i < data.size(); i++)
        {
            JsonObject dataEntry = data.get(i).getAsJsonObject();
            User user = new User();
            user.setId(dataEntry.get("id").getAsInt());
            user.setEmail(dataEntry.get("email").getAsString());
            user.setDate(dataEntry.get("date").getAsString());
            user.setRights(dataEntry.get("rights").getAsInt());
            user.setIs_approved(dataEntry.get("is_approved").getAsInt());
            user.setLanguage(dataEntry.get("language").getAsString());
            user.setName(dataEntry.get("name").getAsString());
            user.setBirth_date(dataEntry.get("birth_date").getAsString());
            user.setPhone_code(dataEntry.get("phone_code").getAsInt());
            user.setPhone_number(dataEntry.get("phone_number").getAsInt());
            user.setCountry(dataEntry.get("country").getAsString());
            user.setCity(dataEntry.get("city").getAsString());
            user.setState(dataEntry.get("state").getAsString());
            user.setStreet(dataEntry.get("street").getAsString());
            user.setPostal_code(dataEntry.get("postal_code").getAsString());
            users.add(user);
        }

        this.data = users;
        return this;
    }
}