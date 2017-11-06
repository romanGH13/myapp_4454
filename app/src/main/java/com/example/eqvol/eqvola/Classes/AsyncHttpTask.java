package com.example.eqvol.eqvola.Classes;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.eqvol.eqvola.Adapters.SupportFragmentPagerAdapter;
import com.example.eqvol.eqvola.JsonResponse.JsonResponceCountries;
import com.example.eqvol.eqvola.JsonResponse.JsonResponseAccounts;
import com.example.eqvol.eqvola.JsonResponse.JsonResponseCategories;
import com.example.eqvol.eqvola.JsonResponse.JsonResponseGroups;
import com.example.eqvol.eqvola.JsonResponse.JsonResponseTicketMessages;
import com.example.eqvol.eqvola.JsonResponse.JsonResponseTickets;
import com.example.eqvol.eqvola.JsonResponse.JsonResponseUser;
import com.example.eqvol.eqvola.LoginActivity;
import com.example.eqvol.eqvola.MenuActivity;
import com.example.eqvol.eqvola.R;
import com.example.eqvol.eqvola.fragments.CreateAccount;
import com.example.eqvol.eqvola.fragments.MyAccounts;
import com.example.eqvol.eqvola.fragments.Support;
import com.example.eqvol.eqvola.fragments.SupportChat;
import com.example.eqvol.eqvola.fragments.SupportCreateTicket;
import com.example.eqvol.eqvola.fragments.UserPageFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by eqvol on 18.10.2017.
 */

public class AsyncHttpTask extends AsyncTask<Void, Void, String> {

    private final Map<String, Object> parametrs;
    private final Activity act;
    private final AsyncMethodNames methodName;

    public AsyncHttpTask(Map<String, Object> params, AsyncMethodNames methodName,  Activity act) {
        this.parametrs = params;
        this.act = act;
        this.methodName = methodName;
    }

    @Override
    protected String doInBackground(Void... params) {
        String response = null;

        if(methodName == AsyncMethodNames.USER_LOGIN){
            String email = "";
            String password = "";
            for (Map.Entry<String, Object> entry : parametrs.entrySet()) {
                if (entry.getKey().contentEquals("email"))
                    email = (String) entry.getValue();
                else if (entry.getKey().contentEquals("password"))
                    password = (String) entry.getValue();
            }
            try {
                response = Api.login(email, password);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (methodName == AsyncMethodNames.CHECK_TOKEN) {
            //act.showProgress(true);
            String token = "";
            for (Map.Entry<String, Object> entry : parametrs.entrySet()) {
                if (entry.getKey().contentEquals("token"))
                    token = (String) entry.getValue();
            }
            response = Api.checkToken(token);
        }
        else if (methodName == AsyncMethodNames.GET_USER) {
            int id = 0;
            String token = "";
            for (Map.Entry<String, Object> entry : parametrs.entrySet()) {
                if (entry.getKey().contentEquals("userId"))
                    id = (int) entry.getValue();
                else if (entry.getKey().contentEquals("token"))
                    token = (String) entry.getValue();
            }
            response = Api.getUser(id, token);
        }
        else if(methodName == AsyncMethodNames.GET_USER_AVATAR)
        {
            try {
                response = Api.getUserAvatar(parametrs);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        else if(methodName == AsyncMethodNames.GET_COUNTRIES)
        {
            response = Api.getCountries();
        }
        else if (methodName == AsyncMethodNames.SET_USER)
        {
            response = Api.setUser(parametrs);
        }
        else if (methodName == AsyncMethodNames.GET_GROUPS)
        {
            response = Api.getGroups(parametrs);
        }
        else if (methodName == AsyncMethodNames.CREATE_ACCOUNT)
        {
            response = Api.createAccount(parametrs);
        }
        else if (methodName == AsyncMethodNames.GET_ACCOUNTS)
        {
            response = Api.getAccounts(parametrs);
        }
        else if (methodName == AsyncMethodNames.USER_LOGOUT)
        {
            response = Api.userLogout(parametrs);
        }
        else if (methodName == AsyncMethodNames.GET_CATEGORIES)
        {
            response = Api.getCategories(parametrs);
        }
        else if (methodName == AsyncMethodNames.CREATE_TICKET)
        {
            response = Api.createTicket(parametrs);
        }
        else if (methodName == AsyncMethodNames.SEND_MESSAGE)
        {
            response = Api.sendMessage(parametrs);
        }
        else if (methodName == AsyncMethodNames.GET_TICKETS)
        {
            response = Api.getTickets(parametrs);
        }
        else if (methodName == AsyncMethodNames.GET_DATA_FROM_TICKETS)
        {
            Api.getDataFromTickets(act);
        }
        else if (methodName == AsyncMethodNames.GET_TICKET_MESSAGES)
        {
            response = Api.getTicketMessages(parametrs);
        }
        return response;
    }

    @Override
    protected void onPostExecute(final String success) {
        if(success == "")
        {
            Toast toast = Toast.makeText(act.getApplicationContext(),
                    "Problems with connection!", Toast.LENGTH_SHORT);
            toast.show();
        }
        else if (methodName == AsyncMethodNames.CHECK_TOKEN) {
            Map<String, Object> map = Api.jsonToMap(success);
            if (map.containsKey("data")) {
                int userId = 0;
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    if (entry.getKey().contentEquals("data"))
                    {
                        for (Map.Entry<String, Object> data : ((LinkedTreeMap<String, Object>) entry.getValue()).entrySet()) {
                            if (data.getKey().contentEquals("user_id")) {
                                userId = (int) ((double) data.getValue());
                                ((LoginActivity)act).saveTokenInFile(Api.getToken());

                                HashMap<String, Object> parametrs = new HashMap<String, Object>();
                                parametrs.put("userId", userId);
                                parametrs.put("token", Api.getToken());
                                AsyncHttpTask getUserTask = new AsyncHttpTask(parametrs, AsyncMethodNames.GET_USER, act);
                                getUserTask.execute();
                            }
                        }
                    }
                }
            }
        } else if (methodName == AsyncMethodNames.GET_USER) {
            {
                User user = JsonResponseUser.getUser(success);
                Api.user = user;
                ((LoginActivity)act).goToMenuActivity();
            }
        } else if(methodName == AsyncMethodNames.GET_USER_AVATAR){
            if(Api.user.getId() == Integer.parseInt(success)) {
                ImageView img = (ImageView) act.findViewById(R.id.imageView);
                byte[] data = Api.user.getAvatar();
                //byte[] decodedString = //Base64.decode(data, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                img.setImageBitmap(bitmap);
            }
            else
            {

            }
        }
        else if(methodName == AsyncMethodNames.USER_LOGIN) {

            Map<String, Object> map = Api.jsonToMap(success);
            String token = null;
            int errorCode = -1;
            String errorDescription = null;
            Object obj;

            for (Map.Entry<String, Object> response : map.entrySet()) {
                if(response.getKey().contentEquals("data"))
                {
                    for (Map.Entry<String, Object> dataEntry : ((Map<String, Object>)(response.getValue())).entrySet()) {
                        if(dataEntry.getKey().contentEquals("token")){
                            token = (String)dataEntry.getValue();
                        }
                    }

                } else if(response.getKey().contentEquals("errors")){
                    for(Object list : (ArrayList<Object>)(response.getValue())){
                        for (Map.Entry<String, Object> error : ((Map<String, Object>) list).entrySet()) {
                            if (error.getKey().contentEquals("code")) {
                                errorCode = (int) ((double)error.getValue());
                            } else if (error.getKey().contentEquals("description")) {
                                errorDescription = (String) error.getValue();
                            }
                        }
                    }
                }
            }
            if(token != null) {
                Api.setToken(token);
                HashMap<String, Object> parametrs = new HashMap<String, Object>();
                parametrs.put("token", token);
                AsyncHttpTask ckeckTokenTask = new AsyncHttpTask(parametrs, AsyncMethodNames.CHECK_TOKEN, act);
                ckeckTokenTask.execute();
            }
            else if(errorCode != -1 && errorDescription != null){
                ((LoginActivity)act).mLabelView.setText(errorDescription);
                View view = ((LoginActivity)act).mLabelView;
                view.requestFocus();
                ((LoginActivity) act).showProgress(false);
            }
        }

        else if(methodName == AsyncMethodNames.GET_COUNTRIES)
        {
            Api.countries = JsonResponceCountries.getCountries(success);
            MenuActivity.currentLoader.endLoading();
        }
        else if (methodName == AsyncMethodNames.SET_USER)
        {
            Map<String, Object> map = Api.jsonToMap(success);
            for (Map.Entry<String, Object> response : map.entrySet()) {
                if (response.getKey().contentEquals("status")) {

                    if (response.getValue().toString().contentEquals("success")) {
                        Toast toast = Toast.makeText(act.getApplicationContext(),
                                "Data was saved!", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                }
            }
        }
        else if (methodName == AsyncMethodNames.GET_GROUPS)
        {
            Api.groups = JsonResponseGroups.getGroups(success);
            MenuActivity.currentLoader.endLoading();
        }
        else if (methodName == AsyncMethodNames.CREATE_ACCOUNT)
        {
            Map<String, Object> map = Api.jsonToMap(success);
            for (Map.Entry<String, Object> response : map.entrySet()) {
                if (response.getKey().contentEquals("data")) {
                    for (Map.Entry<String, Object> dataEntry : ((Map<String, Object>) (response.getValue())).entrySet()) {
                        if (dataEntry.getKey().contentEquals("login")) {
                            Toast toast = Toast.makeText(act.getApplicationContext(),
                                    "Account was created with login: " + (int)(double)dataEntry.getValue(), Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                }
            }
        }
        else if (methodName == AsyncMethodNames.GET_ACCOUNTS)
        {

            Api.user.accounts = JsonResponseAccounts.getAccounts(success);
            MenuActivity.currentLoader.endLoading();

        }
        else if (methodName == AsyncMethodNames.GET_CATEGORIES)
        {
            Api.categories = JsonResponseCategories.getCategories(success);
            MenuActivity.currentLoader.endLoading();
        }
        else if (methodName == AsyncMethodNames.CREATE_TICKET)
        {
            Map<String, Object> map = Api.jsonToMap(success);
            for (Map.Entry<String, Object> response : map.entrySet()) {
                if (response.getKey().contentEquals("data")) {

                    for (Map.Entry<String, Object> dataEntry : ((Map<String, Object>)response.getValue()).entrySet()) {
                        if (dataEntry.getKey().toString().contentEquals("ticket_id")) {

                            int currentTicketId = Api.user.getCurrentTicketId();
                            Ticket ticket = Api.user.getTicket(currentTicketId);
                            ticket.setId((int)((double)dataEntry.getValue()));

                            HashMap<String, Object> mapUserId = new HashMap<String, Object>();
                            mapUserId.put("user_id", Api.user.getId());
                            mapUserId.put("ticket_id", ticket.getId());
                            mapUserId.put("message", ticket.getMessage().getMessage());

                            Gson gson = new GsonBuilder().create();
                            String json = gson.toJson(mapUserId);

                            HashMap<String, Object> params = new HashMap<String, Object>();
                            params.put("token", Api.getToken());
                            params.put("data", json);

                            AsyncHttpTask sendMessageTask = new AsyncHttpTask(params, AsyncMethodNames.SEND_MESSAGE, act);
                            sendMessageTask.execute();
                        }
                    }
                }
            }
        }
        else if (methodName == AsyncMethodNames.SEND_MESSAGE) {
            Map<String, Object> map = Api.jsonToMap(success);
            for (Map.Entry<String, Object> response : map.entrySet()) {
                if (response.getKey().contentEquals("status")) {

                    if (response.getValue().toString().contentEquals("success")) {
                        Toast toast = Toast.makeText(act.getApplicationContext(),
                                "Ticket was created!", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                }
            }
        }
        else if (methodName == AsyncMethodNames.GET_TICKETS) {
            //Map<String, Object> map = Api.jsonToMap(success);

            JsonResponseTickets jrt = new JsonResponseTickets(success);
            Api.user.tickets = jrt.getTickets();
            int countLastMessages = jrt.getLastMessages();

            AsyncHttpTask getDataFromTickets = new AsyncHttpTask(null, AsyncMethodNames.GET_DATA_FROM_TICKETS, act);
            getDataFromTickets.execute();

        }
        else if (methodName == AsyncMethodNames.GET_DATA_FROM_TICKETS) {
            int count = Api.users.size();
            //TODO: добавить лоадер
            while(count!=0) {
                count = Api.users.size();
                for (int i = 0; i < Api.users.size(); i++) {
                    if (Api.users.get(i).getAvatar() != null)
                        count--;
                }
            }
            SupportFragmentPagerAdapter adapter = (SupportFragmentPagerAdapter) Support.mViewPager.getAdapter();
            int itempos = adapter.getItemPosition(Api.chatLoader.getProgressBar());
            adapter.replaceFragment(itempos, Api.chatLoader.fragment);
            adapter.notifyDataSetChanged();
            Support.mViewPager.setAdapter(adapter);
        }
        else if (methodName == AsyncMethodNames.GET_TICKET_MESSAGES) {
            Map<String, Object> map = Api.jsonToMap(success);
            Api.currentChatMessages = JsonResponseTicketMessages.getMessages(success);
            SupportChat.goToChatActivity();

        }

    }

}