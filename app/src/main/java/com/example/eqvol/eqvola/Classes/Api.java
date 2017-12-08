package com.example.eqvol.eqvola.Classes;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;


import com.example.eqvol.eqvola.Adapters.SupportFragmentPagerAdapter;
import com.example.eqvol.eqvola.LoginActivity;
import com.example.eqvol.eqvola.fragments.Support;
import com.example.eqvol.eqvola.fragments.SupportChat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by eqvol on 13.10.2017.
 */

public class Api extends LoginActivity {

    private static String token;
    public static User user;
    public static List<Country> countries = null;
    public static List<Group> groups = null;
    public static List<Category> categories = null;
    private static final String siteUrl = "http://api.eqvola.net/";
    public static final String FILENAME = "eqvolaUserToken";
    public static List<Message> currentChatMessages;



    public static FragmentLoader chatLoader = null;

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        Api.token = token;
    }

    public static String login(String email, String password) throws IOException {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("email", email);
        params.put("password", password);
        return performPostCall(params, siteUrl+"account/login");
    }

    public static String registration(Map<String, Object> params)
    {
        return performPostCall(params, siteUrl+"account/register");
    }
    public static String checkToken(String token)
    {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("token", token);
        return performPostCall(params, siteUrl+"account/check_token");
    }
    public static String checkEmail(Map<String, Object> params)
    {
        return performPostCall(params, siteUrl+"account/check_email");
    }
    public static String setAttachment(Map<String, Object> params)
    {
        return performPostCall(params, siteUrl+"support/attachment/set");
    }

    public static String getAttachment(Map<String, Object> params)
    {
        Map<String, Object> mapUserId = new HashMap<String, Object>();
        Attachment attachment = null;
        try {

            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if (entry.getKey().contentEquals("attachment")) {
                    attachment = (Attachment)entry.getValue();
                    mapUserId.put("attachment_id", Integer.toString(attachment.getId()));
                }
                else if (entry.getKey().contentEquals("token")) {
                    mapUserId.put("token", entry.getValue());
                }
            }
            attachment.setData(performGetCall(mapUserId, siteUrl+"support/attachment/get"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Integer.toString(attachment.getId());
    }

    public static String getTickets(Map<String, Object> params)
    {
        return performPostCall(params, siteUrl+"support/ticket/get");
    }

    public static String getTicketMessages(Map<String, Object> params)
    {
        return performPostCall(params, siteUrl+"support/message/get");
    }


    public static String sendMessage(Map<String, Object> params)
    {
        return performPostCall(params, siteUrl+"support/message/set");
    }

    public static String createTicket(Map<String, Object> params)
    {
        return performPostCall(params, siteUrl+"support/ticket/set");
    }

    public static String getCategories(Map<String, Object> params)
    {
        return performPostCall(params, siteUrl+"support/category/get");
    }

    public static String createAccount(Map<String, Object> params)
    {
        return performPostCall(params, siteUrl+"mt4/account_create");
    }

    public static String getAccounts(Map<String, Object> params)
    {
        return performPostCall(params, siteUrl+"mt4/account/get");
    }

    public static String getGroups(Map<String, Object> params)
    {
        return performPostCall(params, siteUrl+"mt4/group/get");
    }
    public static String setUser(Map<String, Object> params)
    {
        return performPostCall(params, siteUrl+"account/user/set");
    }
    public static String userLogout(Map<String, Object> params)
    {
        String str = performPostCall(params, siteUrl+"logout");
        if(str == "")
            return "logout";
        else
        return str;
    }

    public static String getUser(int id, String token)
    {
        Gson gson = new GsonBuilder().create();
        HashMap<String, Integer> mapUserId = new HashMap<String, Integer>();
        mapUserId.put("id", id);
        String json = gson.toJson(mapUserId);
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("token", token);
        params.put("where", json);

        return performPostCall(params, siteUrl+"account/user/get");
    }

    public static String setUserAvatar(Map<String, Object> params){
        return performPostCall(params, siteUrl+"account/user/set");
    }

    public static String getUserAvatar(Map<String, Object> params) throws UnsupportedEncodingException {
        Map<String, Object> mapUserId = new HashMap<String, Object>();
        User user = null;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (entry.getKey().contentEquals("user")) {
                user = (User)entry.getValue();
                mapUserId.put("user_id", Integer.toString(user.getId()));
            }
        }
        user.setAvatar(performGetCall(mapUserId, siteUrl+"account/user/avatar"));
        return Integer.toString(user.getId());
    }

    public static String getCountries()
    {
        return performPostCall(siteUrl+"location/get");
    }


    private static byte[]  performGetCall(Map<String, Object> getDataParams, String strUrl) throws UnsupportedEncodingException {
        OkHttpClient client = new OkHttpClient();
        Request request = null;
        Response response = null;
        byte[] json = null;
        request = new Request.Builder()
                    .url(strUrl+"?"+getPostDataString(getDataParams)).build();//
        try {
            response = client.newCall(request).execute();
            json = response.body().bytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    private static String  performPostCall( String strUrl) {

        String response = "";
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");

            conn.setDoInput(true);
            conn.setDoOutput(true);

            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    private static String  performPostCall(Map<String, Object> postDataParams, String strUrl) {

        String response = "";
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(30000);
            conn.setConnectTimeout(30000);
            conn.setRequestMethod("POST");

            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();

            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    private static String getPostDataString(Map<String, Object> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, Object> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
        }
        return result.toString();
    }

    public static Map<String, Object> jsonToMap(String json)
    {
        Gson gson = new Gson();
        JsonElement element = new JsonParser().parse(json);
        Map<String, Object> map = gson.fromJson(json, Map.class);
        return map;
    }

    private static Map<String, Object> JSONArrayToMap(Map<String, Object> map, JSONObject jObject) // map - родительский эллемент
    {
        try {
            JSONArray array = jObject.names();//получаем список имен всех ключей объекта
            for(int i =0; i<array.length();i++)
            {
                String keyName = null;
                keyName = array.get(i).toString();

                Object value = jObject.get(keyName);//получаем объект json по имени, null если это не объект
                if(JSONArray.class != value.getClass())
                {
                    if(value.getClass() == JSONObject.class)
                    {
                        JSONObject data = (JSONObject)value;
                        map.put(keyName, JSONArrayToMap(new HashMap<String, Object> (), data));
                    }
                    else if(value.getClass() == Integer.class)
                    {
                        map.put(keyName, (Integer)value);
                    }
                    else if(value.getClass() == Boolean.class)
                    {
                        map.put(keyName, (boolean)value);
                    }
                    else if(value.getClass() == String.class)
                    {
                        map.put(keyName, (String)value);
                    }
                    else
                    {
                        map.put(keyName, value);
                    }
                }
                else
                {
                    JSONObject obj1 = ((JSONArray)value).getJSONObject(0);
                    map.put(keyName, JSONArrayToMap(new HashMap<String, Object> (), obj1));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static Map<String, Object> JsonToMap(String json)
    {
        Map<String, Object> map = new HashMap<> (); //создаю гланый массив

        JSONObject jObject;
        try {
            Log.d("myTag", json);

            jObject = new JSONObject(json);//создаем json object из нашей json строки
            map = JSONArrayToMap(map, jObject); //вызываю метод, который вернет полный ассоциативный массив
            Log.d("myTag", " " + map.size());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("myTag", "test3");
        return map;
    }

}
