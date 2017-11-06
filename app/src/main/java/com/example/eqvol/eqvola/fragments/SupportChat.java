package com.example.eqvol.eqvola.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.eqvol.eqvola.Adapters.SupportFragmentPagerAdapter;
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


public class SupportChat extends Fragment {

    private static View mView = null;
    private static FragmentLoader fl = null;



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

    public void loader()
    {
        String str = "";
        FragmentManager fm = getChildFragmentManager();
        fl = new FragmentLoader(SupportChat.class, getFragmentManager(), R.id.support_chat_container, false);
        fl.startLoading();
    }




    public static void setTickets(){
        List<Ticket> tickets = Api.user.tickets;

        ListView lvTickets = (ListView)mView.findViewById(R.id.support_chat_list);
        TicketsAdapter adapter = new TicketsAdapter(mView.getContext(), tickets);
        lvTickets.setAdapter(adapter);

        lvTickets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Ticket ticket = (Ticket)parent.getItemAtPosition(position);


                Gson gson = new GsonBuilder().create();
                HashMap<String, Object> mapUserId = new HashMap<String, Object>();
                mapUserId.put("ticket_id", ticket.getId());
                String json = gson.toJson(mapUserId);
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("token", Api.getToken());
                params.put("where", json);

                AsyncHttpTask userLoginTask = new AsyncHttpTask(params, AsyncMethodNames.GET_TICKET_MESSAGES, (Activity)mView.getContext());
                userLoginTask.execute();
            }
        });
    }

    public static void goToChatActivity()
    {
        Intent intent = new Intent(mView.getContext(), ChatActivity.class);
        //intent.putExtra("messages", messages);
        //intent.putExtra("lname", etLName.getText().toString());
        mView.getContext().startActivity(intent);
    }




}
