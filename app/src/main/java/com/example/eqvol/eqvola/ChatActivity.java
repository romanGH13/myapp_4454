package com.example.eqvol.eqvola;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.example.eqvol.eqvola.Adapters.MessagesAdapter;
import com.example.eqvol.eqvola.Classes.Api;
import com.example.eqvol.eqvola.Classes.AsyncHttpTask;
import com.example.eqvol.eqvola.Classes.AsyncMethodNames;
import com.example.eqvol.eqvola.Classes.Message;
import com.example.eqvol.eqvola.Classes.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private EditText input = null;
    private int ticket_id;
    public int last_message_id;
    public List<User> users;
    public List<Message> messages;

    public Map<Integer, Bitmap> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        input = (EditText)findViewById(R.id.chat_message_text);

        Intent intent = getIntent();
        ticket_id = intent.getIntExtra("ticket_id", 0);
        last_message_id = intent.getIntExtra("last_message_id", 0);
        images = new HashMap<Integer, Bitmap>();

        messages = Api.currentChatMessages;

        if(ticket_id == 0 || last_message_id == 0) finish();

        users = new ArrayList<User>();
        users.add(Api.user);

        checkUsersAvatar();

        //setMessages();
        //newMessagesHandler();
    }

    public void checkUsersAvatar()
    {
        for(Message m: messages){
            if(!isUserContains(m.getUser().getId())){
                User user = m.getUser();
                users.add(user);

                HashMap<String, Object> parametrs = new HashMap<String, Object>();
                parametrs.put("user", user);

                AsyncHttpTask getUserTask = new AsyncHttpTask(parametrs, AsyncMethodNames.GET_USER_AVATAR, this);
                getUserTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
        AsyncHttpTask getUserTask = new AsyncHttpTask(null, AsyncMethodNames.WAIT_AVATAR_LOADING, this);
        getUserTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private boolean isUserContains(int id){
        for(User u: users){
            if(u.getId() == id)
            {
                return true;
            }
        }
        return false;
    }

    public void isDataLoading()
    {
        int count = users.size();
        while(count!=0)
        {
            count = users.size();
            for(User u: users){
                if(u.getAvatar()!=null){
                    count--;
                }
            }
        }
    }

    public User getUserById(int id)
    {
        for(User u: users){
            if(u.getId() == id)
                return u;
        }
        return null;
    }


    public void setMessages()
    {
        ListView lvMessages = (ListView)findViewById(R.id.list_of_messages);
        MessagesAdapter adapter = new MessagesAdapter(this, this, messages);
        lvMessages.setAdapter(adapter);

        //newMessagesHandler();
    }

    /*public void newMessagesHandler()
    {
        Gson gson = new GsonBuilder().create();
        HashMap<String, Object> mapUserId = new HashMap<String, Object>();
        mapUserId.put("ticket_id", ticket_id);
        mapUserId.put("id>", last_message_id);
        String json = gson.toJson(mapUserId);
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("token", Api.getToken());
        params.put("where", json);

        AsyncHttpTask getNewMessages = new AsyncHttpTask(params, AsyncMethodNames.GET_NEW_MESSAGE, this);
        getNewMessages.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }*/

    //обработчик нажатия кнопки Send для отправки сообщения
    public void sendMessage(View view) {

        String message = input.getText().toString();
        if(message == "") return;

        HashMap<String, Object> mapUserId = new HashMap<String, Object>();
        mapUserId.put("user_id", Api.user.getId());
        mapUserId.put("ticket_id", ticket_id);
        mapUserId.put("message", message);

        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(mapUserId);

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("token", Api.getToken());
        params.put("data", json);

        AsyncHttpTask sendMessageTask = new AsyncHttpTask(params, AsyncMethodNames.SEND_MESSAGE, this);
        sendMessageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        // Clear the input
        input.setText("");
    }

}
