package com.example.eqvol.eqvola.Classes;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


import com.example.eqvol.eqvola.LoginActivity;
import com.example.eqvol.eqvola.fragments.ModalAlert;
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
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by eqvol on 13.10.2017.
 */

public class Api extends LoginActivity {

    public static Context context;

    private static String token;
    public static User user = null;
    public static List<Country> countries = null;
    public static List<Group> groups = null;
    public static List<Category> categories = null;
    private static final String siteUrl = "http://api.eqvola.net/";
    public static final String FILENAME = "eqvolaUserToken";
    public static List<Message> currentChatMessages;
    public static List<Withdrawal> withdrawals;
    public static List<Withdrawal> deposits;
    public static List<Transfer> transfers;
    public static Withdrawal currentOperation;
    public static Account account;
    public static Ticket ticket;

    public static FragmentLoader chatLoader = null;

    public static void showDialog(FragmentManager manager, boolean status, String description)
    {
        ModalAlert myDialogFragment = new ModalAlert(status, description);
        //FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        myDialogFragment.show(transaction, "dialog");
    }

    public static String getUrl()
    {
        return siteUrl;
    }
    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        Api.token = token;
    }



    /*Методы для регистрации нового пользователя*/
    public static String registration(Map<String, Object> params)
    {
        return performPostCall(params, siteUrl+"account/register");
    }
    public static String beforeRegistration(Map<String, Object> params)
    {
        return performPostCall(params, siteUrl+"account/before_register ");
    }
    public static String checkBeforeRegistration(Map<String, Object> params)
    {
        return performPostCall(params, siteUrl+"account/check_before_register ");
    }
    public static String resendBeforeRegistration(Map<String, Object> params)
    {
        return performPostCall(params, siteUrl+"account/resend_before_register");
    }

    public static String checkEmail(Map<String, Object> params)
    {
        return performPostCall(params, siteUrl+"account/check_email");
    }

    /*Метод для авторизации пользователя*/
    public static String login(String email, String password) throws IOException {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("email", email);
        params.put("password", password);
        return performPostCall(params, siteUrl+"account/login");
    }

    public static String checkToken(String token)
    {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("token", token);
        return performPostCall(params, siteUrl+"account/check_token");
    }


    public static String forgotPassword(Map<String, Object> params)
    {
        return performPostCall(params, siteUrl+"account/forgot");
    }
    public static String closeTicket(Map<String, Object> params)
    {
        return performPostCall(params, siteUrl+"support/ticket/set");
    }
    public static String getWithdrawal(Map<String, Object> params)
    {
        return performPostCall(params, siteUrl+"withdrawal/get");
    }
    public static String getPayments(Map<String, Object> params)
    {
        return performPostCall(params, siteUrl+"payment/history/get");
    }

    public static String getNotification(Map<String, Object> params)
    {
        return performPostCall(params, siteUrl+"notification/get");
    }
    public static String clearNotifications(Map<String, Object> params)
    {
        return performPostCall(params, siteUrl+"notification/clear");
    }

    public static String getAccountOrders(Map<String, Object> params)
    {
        return performPostCall(params, siteUrl+"mt4/trade/get");
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


    public static String requestTransfer(Map<String, Object> params)
    {
        return performPostCall(params, siteUrl+"transfer/do");
    }
    public static String getTransfers(Map<String, Object> params)
    {
        return performPostCall(params, siteUrl+"transfer/self");
    }
    public static String getAccountHolderName(Map<String, Object> params)
    {
        return performPostCall(params, siteUrl+"mt4/account/get_name");
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

    public static String userLogout(Map<String, Object> params)
    {
        String str = performPostCall(params, siteUrl+"logout");
        if(str == "")
            return "logout";
        else
            return str;
    }


    private static byte[]  performGetCall(Map<String, Object> getDataParams, String strUrl) throws UnsupportedEncodingException {
        Request request = null;
        Response response = null;
        byte[] json = null;
        if (isNetworkAvailable(context)) {
            try {
                OkHttpClient client = new OkHttpClient();

                request = new Request.Builder().url(strUrl + "?" + getPostDataString(getDataParams)).build();

                response = client.newCall(request).execute();
                json = response.body().bytes();
            } catch (Exception e) {
                //e.printStackTrace();
                json = null;
            }
        }
        else {
            json = null;
        }
        return json;
    }

    private static String  performPostCall( String strUrl) {

        String response = "";

        if (isNetworkAvailable(context)) {
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
                    response="Error with connection to Api";

                }
            } catch (Exception e) {
                //e.printStackTrace();
                response="Error with connection to Api";
                return response;
            }
        } else {
            response="Problems with connections";
        }

        return response;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    private static String  performPostCall(Map<String, Object> postDataParams, String strUrl) {

        String response = "";
        if (isNetworkAvailable(context)) {
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

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                } else {
                    response = "";

                }
            } catch (Exception e) {
                //e.printStackTrace();
                response = "Error with connection to Api";
                return response;
            }
        }
        else {
            response="Problems with connections";
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
        //JsonElement element = new JsonParser().parse(json);
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

            jObject = new JSONObject(json);//создаем json object из нашей json строки
            map = JSONArrayToMap(map, jObject); //вызываю метод, который вернет полный ассоциативный массив

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }

}
