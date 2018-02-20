package com.example.eqvol.eqvola.Notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.eqvol.eqvola.Classes.Api;
import com.example.eqvol.eqvola.Classes.AsyncHttpTask;
import com.example.eqvol.eqvola.Classes.AsyncMethodNames;
import com.example.eqvol.eqvola.Classes.Notification;
import com.example.eqvol.eqvola.Classes.Ticket;
import com.example.eqvol.eqvola.ForgotPasswordActivity;
import com.example.eqvol.eqvola.MainActivity;
import com.example.eqvol.eqvola.R;
import com.example.eqvol.eqvola.fragments.SupportChat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class MyService extends Service {

    private static final String siteUrl = "http://api.cabinet.eqvola.info/";
    private static final String FILENAMEFORDATA = "userDataForNotification";
    private static final String FILENAMEFORCOUNTER = "userCounterForNotification";
    final String LOG_TAG = "myLogs";
    private static int counter;
    private static String token;
    private static int user_id;
    private boolean isStopped;

    public MyService() {
        Log.d(LOG_TAG, "MyService");
        counter = 0;
        isStopped = false;
    }

    public void onCreate() {
        super.onCreate();
    }



    public int onStartCommand(final Intent intent,final int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        new Thread(new Runnable() {
            public void run() {
                if(intent != null) {

                    Bundle b = intent.getExtras();
                    token = b.getString("token");
                    user_id = b.getInt("user_id");
                    saveDataInFile();
                }
                else
                {
                    getDataFromFile();
                }
                getCounterFromFile();
                while (!isStopped) {
                    try {

                        getNotification();
                    } catch (Exception ex) {
                    }
                }
            }
        }).start();

        return Service.START_STICKY;
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    //функция для сохранения токена при авторизации
    public void saveDataInFile(){

        FileOutputStream fos = null;
        try {
            fos = openFileOutput(FILENAMEFORDATA, MODE_PRIVATE);
            String str = "token:" + token + "\n\r user_id:" + user_id;
            fos.write(str.getBytes());
        }
        catch(IOException ex) { }
        finally{
            try{
                if(fos!=null)
                    fos.close();
            }
            catch(IOException ex){ }
        }
    }

    public void getCounterFromFile(){

        FileInputStream fin = null;
        String fileContent = null;
        try {
            fin = openFileInput(FILENAMEFORCOUNTER);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            fileContent = new String (bytes);
        }
        catch(IOException ex) {
        }
        finally{
            try{
                if(fin!=null)
                    fin.close();
            }
            catch(IOException ex){
            }
        }
        int id = 0;
        if(fileContent != null) {
            String[] var = fileContent.split(":");
            if(var[0].contentEquals("counter"))
                id = Integer.parseInt(var[1]);
        }
        counter = id;
    }

    //функция для сохранения токена при авторизации
    public void saveCounterInFile(){

        FileOutputStream fos = null;
        try {
            fos = openFileOutput(FILENAMEFORCOUNTER, MODE_PRIVATE);
            String str = "counter:" + counter;
            fos.write(str.getBytes());
        }
        catch(IOException ex) { }
        finally{
            try{
                if(fos!=null)
                    fos.close();
            }
            catch(IOException ex){ }
        }
    }

    public void getDataFromFile(){

        FileInputStream fin = null;
        String fileContent = null;
        try {
            fin = openFileInput(FILENAMEFORDATA);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            fileContent = new String (bytes);
        }
        catch(IOException ex) {
        }
        finally{
            try{
                if(fin!=null)
                    fin.close();
            }
            catch(IOException ex){
            }
        }
        if(fileContent != null) {
            String[] arrayData = fileContent.split("\n\r");
            for(String str: arrayData)
            {
                String[] var = str.split(":");
                if(var[0].contentEquals("token"))
                    token = var[1];
                if(var[0].contentEquals("user_id"))
                    user_id = Integer.parseInt(var[1]);
            }
        }
    }


    private void getNotification() {
        //формирование данных для запроса
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("user_id", user_id);
        where.put("is_show", 0);
        Gson gson = new GsonBuilder().create();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("token", token);
        params.put("where", gson.toJson(where));

        //запрос на получение всех непрочитанных оповещений
        String resp = "";
        try {
            resp = performPostCall(params, siteUrl + "notification/get");
            if (resp.contentEquals("Error with connection to Api")) {
                return;
            }

            Map<String, Object> map = Api.jsonToMap(resp);
            for (Map.Entry<String, Object> response : map.entrySet()) {
                if (response.getKey().contentEquals("errors")) {
                    ArrayList<Object> errorList = (ArrayList<Object>)response.getValue();
                    for(Object error: errorList) {
                        Map<String, Object> tmpMap = (Map<String, Object>) error;
                        for (Map.Entry<String, Object> dataEntry : tmpMap.entrySet()) {
                            if (dataEntry.getKey().contentEquals("description")) {
                                String description = (String) dataEntry.getValue();

                                if(description.contentEquals("Token does not exist!"))
                                {
                                    isStopped = true;
                                    stopSelf();
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex)
        {
            resp = "";
        }

        //парсим Json строку, что бы получить список объектов типа Notification
        JsonResponseNotification parser = new JsonResponseNotification();

        List<Notification> notifications = null;

        if(!resp.contentEquals("")) {
            try {
                List<Notification> list = null;
                list = parser.getNotifications(resp);
                if (list != null) {
                    notifications = list;
                }
            } catch (Exception ex) {
            }
            if (notifications != null) {
                for (Notification n : notifications) {
                    counter = showNotification(n.getDescription(), counter);
                    saveCounterInFile();
                }
            }


        }

        if(notifications != null)
        {
            //делаем все полученные уведомления прочитаными
            HashMap<String, Object> parametrForClear = new HashMap<String, Object>();
            parametrForClear.put("token", token);
            performPostCall(params, siteUrl+"notification/clear");
        }
        saveDataInFile();
        return;
    }


    /*
    * метод для отображения уведомления
    * */
    private int showNotification(String title, int id) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_stat_new_message)
                        .setContentTitle(title)
                        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                        .setVibrate(new long[] { 1000, 1000})
                        .setLights(0xff00ff00, 300, 100);

        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);


        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(id, mBuilder.build());
        return ++id;
    }

    /*
    * Метод, который выполняет POST запрос на сервер
    * */
    private static String performPostCall(Map<String, Object> postDataParams, String strUrl) {

        String response = "";
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(20000);
            conn.setConnectTimeout(20000);
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

        return response;
    }

    /*
    * Метод для преобразования ассоциативного массива в строку для запроса */
    private static String getPostDataString(Map<String, Object> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
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

    /*
    * Класс обработки Json строки
    * */
    class JsonResponseNotification {
        public String status;
        private List<Notification> data;

        public List<Notification> getNotifications(String json)
        {
            Gson gson = new GsonBuilder().create();
            JsonReader reader = new JsonReader(new StringReader(json));
            reader.setLenient(true);
            JsonResponseNotification response = gson.fromJson(reader, JsonResponseNotification.class);
            if(response.status.contentEquals("error"))
                return null;
            return response.data;
        }
    }


}

