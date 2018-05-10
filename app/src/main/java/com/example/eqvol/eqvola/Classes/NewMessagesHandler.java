package com.example.eqvol.eqvola.Classes;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

/**
 * Created by eqvol on 14.12.2017.
 */

public class NewMessagesHandler {
    private static NewMessagesHandler instance;

    private AsyncHttpTask getNewMessagesTask;
    private boolean isListen;

    private NewMessagesHandler (){

        this.isListen = false;
    }

    public void startListen(int last_message_id, Context context)
    {
        if(!isListen) {
            isListen = true;
            Gson gson = new GsonBuilder().create();
            HashMap<String, Object> mapUserId = new HashMap<String, Object>();
            mapUserId.put("id>", last_message_id);
            mapUserId.put("ticket_user_id", Api.user.getId());
            String json = gson.toJson(mapUserId);
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("token", Api.getToken());
            params.put("where", json);

            //todo перенести этот вызов в Menu activity
            getNewMessagesTask = new AsyncHttpTask(params, AsyncMethodNames.GET_NEW_MESSAGE, (Activity) context);
            getNewMessagesTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    public void endListen()
    {
        isListen = false;
    }

    public static NewMessagesHandler getInstance(){
        if (null == instance){
            instance = new NewMessagesHandler();
        }
        return instance;
    }
}