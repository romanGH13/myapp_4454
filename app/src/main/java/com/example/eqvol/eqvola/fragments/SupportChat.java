package com.example.eqvol.eqvola.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.eqvol.eqvola.Adapters.TicketsAdapter;
import com.example.eqvol.eqvola.ChatActivity;
import com.example.eqvol.eqvola.Classes.Api;
import com.example.eqvol.eqvola.Classes.AsyncHttpTask;
import com.example.eqvol.eqvola.Classes.AsyncMethodNames;
import com.example.eqvol.eqvola.Classes.FragmentLoader;
import com.example.eqvol.eqvola.Classes.Message;
import com.example.eqvol.eqvola.Classes.Ticket;
import com.example.eqvol.eqvola.Classes.User;
import com.example.eqvol.eqvola.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SupportChat extends Fragment {

    private static View mView = null;
    private static ListView lvTickets = null;
    private static FragmentLoader fl = null;
    private static int currentTicketId;
    private static int currentTicketLastMessageId;

    public static List<Ticket> tickets = null;
    public static List<User> users = new ArrayList<User>();
    public static Map<Integer, Bitmap> images = new HashMap<Integer, Bitmap>();

    public SupportChat()
    {
        //users = ;

    }

    public static SupportChat newInstance() {
        SupportChat fragment = new SupportChat();
        //users = new ArrayList<User>();
        //users.add(Api.user);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //loader();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_support_chat, container, false);

        setTickets();

        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public static void checkUsersInTickets(Activity activity)
    {
        users.add(Api.user);
        for(Ticket t: tickets){
            User u = t.getUser();
            if(!isUserContains(u.getId())){
                users.add(u);
                //TODO: загрузку аватарки пользователя
                HashMap<String, Object> parametrs = new HashMap<String, Object>();
                parametrs.put("user", u);

                AsyncHttpTask getUserTask = new AsyncHttpTask(parametrs, AsyncMethodNames.GET_USER_AVATAR, activity);
                getUserTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }

        isDataLoading();
    }

    public static void isDataLoading()
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

    private static boolean isUserContains(int id){
        for(User user: users){
            if(user.getId() == id)
            {
                return true;
            }
        }
        return false;
    }

    public static void setTickets(){
        //List<Ticket> tickets = Api.user.tickets;

        lvTickets = (ListView)mView.findViewById(R.id.support_chat_list);
        TicketsAdapter adapter = new TicketsAdapter(mView.getContext(), tickets);
        lvTickets.setAdapter(adapter);

        lvTickets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Ticket ticket = (Ticket)parent.getItemAtPosition(position);
                currentTicketId = ticket.getId();
                currentTicketLastMessageId = ticket.getMessage().getId();

                Gson gson = new GsonBuilder().create();
                HashMap<String, Object> mapUserId = new HashMap<String, Object>();
                mapUserId.put("ticket_id", ticket.getId());
                String json = gson.toJson(mapUserId);
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("token", Api.getToken());
                params.put("where", json);

                AsyncHttpTask userLoginTask = new AsyncHttpTask(params, AsyncMethodNames.GET_TICKET_MESSAGES, (Activity)mView.getContext());
                userLoginTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });


        newMessagesHandler();
    }


    public static void newMessagesHandler()
    {
        int last_message_id = 0;

        for(Ticket t: tickets){
            int message_id = t.getMessage().getId();
            if(message_id > last_message_id){
                last_message_id = message_id;
            }
        }

        Gson gson = new GsonBuilder().create();
        HashMap<String, Object> mapUserId = new HashMap<String, Object>();
        //mapUserId.put("ticket_id", ticket_id);
        mapUserId.put("id>", last_message_id);
        String json = gson.toJson(mapUserId);
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("token", Api.getToken());
        params.put("where", json);

        AsyncHttpTask getNewMessages = new AsyncHttpTask(params, AsyncMethodNames.GET_NEW_MESSAGE, (Activity)mView.getContext());
        getNewMessages.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static void newMessages(List<Message> newMessages){
        boolean isChanged = false;
        for(Message m: newMessages){
            Ticket t = getTicketById(m.getTicket().getId());
            if(t!=null){
                if(t.getMessage().getId()<m.getId()){
                    t.setMessage(m);
                }
            }
        }
        if(isChanged)
            ((TicketsAdapter)(lvTickets.getAdapter())).notifyDataSetChanged();
    }

    private static Ticket getTicketById(int id)
    {
        for(Ticket t:tickets){
            if(t.getId() == id)
                return t;
        }
        return null;
    }


    public static void goToChatActivity()
    {
        Intent intent = new Intent(mView.getContext(), ChatActivity.class);
        intent.putExtra("ticket_id", currentTicketId);
        intent.putExtra("last_message_id", currentTicketLastMessageId);
        mView.getContext().startActivity(intent);
    }


}
