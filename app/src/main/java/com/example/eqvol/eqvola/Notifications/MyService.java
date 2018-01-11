package com.example.eqvol.eqvola.Notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.eqvol.eqvola.Classes.Api;
import com.example.eqvol.eqvola.Classes.Notification;
import com.example.eqvol.eqvola.MainActivity;
import com.example.eqvol.eqvola.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class MyService extends Service {

    final String LOG_TAG = "myLogs";
    private int counter;

    public MyService() {
        Log.d(LOG_TAG, "MyService");
        counter = 0;
    }

    public void onCreate() {
        super.onCreate();
    }

    public int onStartCommand(final Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    //someTask();
                    Bundle b = intent.getExtras();

                    try {
                        b = getNotification(b);
                    } catch (Exception ex) {
                        showNotification(ex.getMessage(), 0);
                    }
                    intent.putExtras(b);
                }
            }
        }).start();
        super.onStartCommand(intent, flags, startId);
        return Service.START_REDELIVER_INTENT;
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    private Bundle getNotification(Bundle b) {

        //получаем данные из намерения
        String token = b.getString("token");
        int user_id = b.getInt("user_id");

        //формирование данных для запроса
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("user_id", user_id);
        where.put("is_show", 0);
        Gson gson = new GsonBuilder().create();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("token", token);
        params.put("where", gson.toJson(where));

        //запрос на получение всех непрочитанных оповещений
        String resp = performPostCall(params, "http://api.eqvola.net/notification/get");
        if(resp.contentEquals("Error with connection to Api"))
        {
            return b;
        }

        //парсим Json строку, что бы получить список объектов типа Notification
        JsonResponseNotification parser = new JsonResponseNotification();

        List<Notification> notifications = null;
        try {
            List<Notification> list = null;
            list = parser.getNotifications(resp);
            if(list != null)
            {
                notifications = list;
            }
        } catch (Exception ex) { }
        if (notifications != null) {
            for (Notification n : notifications) {
                counter = showNotification(n.getDescription(), counter);
            }
        }

        if(notifications != null)
        {
            //делаем все полученные уведомления прочитаными
            HashMap<String, Object> parametrForClear = new HashMap<String, Object>();
            parametrForClear.put("token", token);
            String str = Api.clearNotifications(parametrForClear);
        }

        b.putInt("counter", counter);
        return b;
    }

    /*
    * метод для отображения уведомления
    * */
    private int showNotification(String title, int id) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_menu_share)
                        .setContentTitle(title);

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
            e.printStackTrace();
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

