package com.example.eqvol.eqvola.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.eqvol.eqvola.Adapters.TicketsAdapter;
import com.example.eqvol.eqvola.ChatActivity;
import com.example.eqvol.eqvola.Classes.Api;
import com.example.eqvol.eqvola.Classes.AsyncHttpTask;
import com.example.eqvol.eqvola.Classes.AsyncMethodNames;
import com.example.eqvol.eqvola.Classes.FragmentLoader;
import com.example.eqvol.eqvola.Classes.Message;
import com.example.eqvol.eqvola.Classes.NewMessagesHandler;
import com.example.eqvol.eqvola.Classes.SpaceItemDecoration;
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
    private static FragmentLoader fl = null;
    public static int currentTicketId;
    public static String title;
    public static int currentTicketLastMessageId;
    private static boolean isFirstLoad;

    public static List<Ticket> tickets;
    public static List<User> users;
    public static Map<Integer, Bitmap> images;
    public static List<Message> newMessages;

    private static ProgressBar mProgressBar;
    private static RecyclerView mRecyclerView;

    public static NewMessagesHandler handler;


    public SupportChat()
    {
        isFirstLoad = true;
        users = new ArrayList<User>();
        images = new HashMap<Integer, Bitmap>();
        newMessages = null;
        tickets = null;
        handler = null;
    }

    public static SupportChat newInstance() {
        SupportChat fragment = new SupportChat();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {

            mView = inflater.inflate(R.layout.fragment_support_chat, container, false);
            mRecyclerView = (RecyclerView) mView.findViewById(R.id.support_chat_list);
            mRecyclerView.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(mView.getContext());
            llm.setItemPrefetchEnabled(false);
            mRecyclerView.setLayoutManager(llm);
            mRecyclerView.addItemDecoration(new SpaceItemDecoration(10, getContext()));
            mRecyclerView.addItemDecoration(new DividerItemDecoration(this.getActivity(), LinearLayout.VERTICAL));

            mProgressBar = (ProgressBar)mView.findViewById(R.id.progress_bar);
        } catch(Exception ex){
            String str = ex.getMessage();
        }
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    public static void checkUsersInTickets(Activity activity)
    {
        try {
            for (Ticket t : tickets) {
                Message message = t.getMessage();
                User u = null;
                if(message == null)
                {
                    u = t.getUser();
                }
                else
                {
                    u = t.getMessage().getUser();
                }

                if (!isUserContains(u.getId())) {
                    users.add(u);
                }
            }

            for(User u: users)
            {
                HashMap<String, Object> parametrs = new HashMap<String, Object>();
                parametrs.put("user", u);

                AsyncHttpTask getUserTask = new AsyncHttpTask(parametrs, AsyncMethodNames.GET_USER_AVATAR, activity);
                getUserTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        } catch(Exception ex)
        {
            String str = ex.getMessage();
        }
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

        for(User u: users)
        {
            byte[] avatar = u.getAvatar();
            if(avatar != null) {
                Bitmap bitmap;
                if(!images.containsKey(u.getId())) {
                    byte[] image = avatar;
                    bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                    bitmap = bitmap.createScaledBitmap(bitmap, 150, 150, false);
                    images.put(u.getId(), bitmap);
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

    public static void updateTickets(){
        try {
            if (isFirstLoad) {
                isFirstLoad = false;
                SupportChat.setTickets();
            } else {
                newMessages();
            }
        }catch(Exception ex)
        {
            String str = ex.getMessage();
        }
    }

    public static void setTickets(){
        final TicketsAdapter adapter = new TicketsAdapter(mView.getContext(), tickets);
        adapter.setOnCLickListener(new TicketsAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {

                ///list item was clicked
                Ticket ticket = adapter.getTicketById(adapter.getItemId(position));
                currentTicketId = ticket.getId();
                title = ticket.getTitle();

                Api.ticket = ticket;

                if(ticket.getMessage() == null)
                {
                    Api.currentChatMessages = null;
                    goToChatActivity();
                    return;
                }

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


        mRecyclerView.setAdapter(adapter);
        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);


        handler = NewMessagesHandler.getInstance();
        handler.startListen(getLastMessageId(), mView.getContext());
    }

    private static int getLastMessageId()
    {
        try {
            int last_message_id = 0;

            try {
                for (Ticket t : tickets) {
                    int message_id = t.getMessage().getId();
                    if (message_id > last_message_id) {
                        last_message_id = message_id;
                    }
                }
            } catch(Exception ex)
            {

            }
            return last_message_id;
        }
        catch (Exception ex)
        {
            return 0;
        }
    }


    public static void newMessagesHandler()
    {
        handler = NewMessagesHandler.getInstance();
        handler.startListen(getLastMessageId(), mView.getContext());
    }

    public static void checkNewMessages(List<Message> newMessages, Activity activity){
        SupportChat.newMessages = newMessages;
        for(Message m: newMessages){
            User u = m.getUser();
            if(!isUserContains(u.getId())){
                users.add(u);

                HashMap<String, Object> parametrs = new HashMap<String, Object>();
                parametrs.put("user", u);

                AsyncHttpTask getUserTask = new AsyncHttpTask(parametrs, AsyncMethodNames.GET_USER_AVATAR, activity);
                getUserTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
        AsyncHttpTask getUserTask = new AsyncHttpTask(null, AsyncMethodNames.WAIT_AVATAR_LOADING, activity);
        getUserTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static User getUserById(int id){
        for(User u: users){
            if(u.getId() == id)
                return u;
        }
        return null;
    }

    public static void closeTicket(int ticket_id){
        for(Ticket t: tickets)
        {
            if(t.getId() == ticket_id)
            {
                t.setStatus(1);
            }
        }
        RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
        adapter.notifyDataSetChanged();
    }

    public static void newMessages(){
        boolean isChanged = false;
        Ticket t = null;
        for(Message m: SupportChat.newMessages){
            t = getTicketById(m.getTicket().getId());
            if(t!=null){
                /*User u = getUserById(m.getUser().getId());
                if(u != null){
                    m.setUser(u);
                }*/
                if(t.getMessage().getId()<m.getId()){
                    t.setMessage(m);
                    isChanged = true;
                }
            }
        }
        if(isChanged) {
            ((TicketsAdapter) (mRecyclerView.getAdapter())).UpdateTicketById(t);
            ((TicketsAdapter) (mRecyclerView.getAdapter())).notifyDataSetChanged();
            if(ChatActivity.isActive){
                if(ChatActivity.ticket_id == newMessages.get(0).getTicket().getId()){
                    if(newMessages != null) {
                        for (Message m : newMessages) {
                            if (ChatActivity.isMessageContains(m)) {
                                newMessages.remove(m);
                            }

                        }
                        ChatActivity.addNewMessages(newMessages);
                    }
                }
            }
        }
        newMessagesHandler();

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
        intent.putExtra("title", title);
        //intent.putExtra("last_message_id", currentTicketLastMessageId);
        mView.getContext().startActivity(intent);
    }

}
