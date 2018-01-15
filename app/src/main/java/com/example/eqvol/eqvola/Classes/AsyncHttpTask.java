package com.example.eqvol.eqvola.Classes;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.eqvol.eqvola.Adapters.MessagesAdapter;
import com.example.eqvol.eqvola.ChatActivity;
import com.example.eqvol.eqvola.JsonResponse.JsonResponceCountries;
import com.example.eqvol.eqvola.JsonResponse.JsonResponse;
import com.example.eqvol.eqvola.JsonResponse.JsonResponseAccounts;
import com.example.eqvol.eqvola.JsonResponse.JsonResponseCategories;
import com.example.eqvol.eqvola.JsonResponse.JsonResponseGroups;
import com.example.eqvol.eqvola.JsonResponse.JsonResponseOrders;
import com.example.eqvol.eqvola.JsonResponse.JsonResponseTicketMessages;
import com.example.eqvol.eqvola.JsonResponse.JsonResponseTickets;
import com.example.eqvol.eqvola.JsonResponse.JsonResponseTransfers;
import com.example.eqvol.eqvola.JsonResponse.JsonResponseUser;
import com.example.eqvol.eqvola.JsonResponse.JsonResponseWithdrawal;
import com.example.eqvol.eqvola.LoginActivity;
import com.example.eqvol.eqvola.MainActivity;
import com.example.eqvol.eqvola.R;
import com.example.eqvol.eqvola.RegistrationActivity;
import com.example.eqvol.eqvola.fragments.AccountOrdersFragment;
import com.example.eqvol.eqvola.fragments.CreateAccount;
import com.example.eqvol.eqvola.fragments.FinanceHistoryFragment;
import com.example.eqvol.eqvola.fragments.MenuFragment;
import com.example.eqvol.eqvola.fragments.MyAccounts;
import com.example.eqvol.eqvola.fragments.OpenOrdersFragment;
import com.example.eqvol.eqvola.fragments.RequestTransferFragment;
import com.example.eqvol.eqvola.fragments.Support;
import com.example.eqvol.eqvola.fragments.SupportChat;
import com.example.eqvol.eqvola.fragments.TransfersFragment;
import com.example.eqvol.eqvola.fragments.TransfersHistoryFragment;
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
import java.util.TreeMap;

/**
 * Created by eqvol on 18.10.2017.
 */

public class AsyncHttpTask extends AsyncTask<Void, Void, String> {

    private final Map<String, Object> parametrs;
    private final Activity act;
    private final AsyncMethodNames methodName;

    public String target;

    public AsyncHttpTask(Map<String, Object> params, AsyncMethodNames methodName,  Activity act) {
        this.parametrs = params;
        this.act = act;
        this.methodName = methodName;
        target = "";
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

            }
        }
        else if(methodName == AsyncMethodNames.USER_REGISTRATION){
            response = Api.registration(parametrs);
        }
        else if(methodName == AsyncMethodNames.USER_BEFORE_REGISTRATION) {
            response = Api.beforeRegistration(parametrs);
        }
        else if(methodName == AsyncMethodNames.USER_CHECK_BEFORE_REGISTRATION) {
            response = Api.checkBeforeRegistration(parametrs);
        }
        else if(methodName == AsyncMethodNames.USER_RESEND_BEFORE_REGISTRATION) {
            response = Api.resendBeforeRegistration(parametrs);
        }

        else if (methodName == AsyncMethodNames.SET_USER_AVATAR) {
            response = Api.setUserAvatar(parametrs);
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
        else if (methodName == AsyncMethodNames.CHECK_EMAIL) {
            response = Api.checkEmail(parametrs);
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
        else if(methodName == AsyncMethodNames.GET_ACCOUNT_HOLDER_NAME) {
            response = Api.getAccountHolderName(parametrs);
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
            response = "";
            SupportChat.checkUsersInTickets(act);
            return null;
        }
        else if (methodName == AsyncMethodNames.GET_TICKET_MESSAGES)
        {
            response = Api.getTicketMessages(parametrs);
        }
        else if (methodName == AsyncMethodNames.GET_NEW_MESSAGE)
        {
            response = Api.getTicketMessages(parametrs);
        }
        else if (methodName == AsyncMethodNames.WAIT_AVATAR_LOADING)
        {
            SupportChat.isDataLoading();
            response = "";
        }
        else if (methodName == AsyncMethodNames.GET_DATA_FROM_MESSAGES)
        {

            List<Message> list = null;
            for (Map.Entry<String, Object> entry : parametrs.entrySet()) {
                if (entry.getKey().contentEquals("newMessages")) {
                    Object o = entry.getValue();
                    list = (ArrayList<Message>)o;
                }
            }
            SupportChat.checkNewMessages(list, act);
            response = "";
        }
        else if (methodName == AsyncMethodNames.GET_ATTACHMENT)
        {
            Api.getAttachment(parametrs);
        }
        else if (methodName == AsyncMethodNames.SET_ATTACHMENT) {
            response = Api.setAttachment(parametrs);
        }
        else if (methodName == AsyncMethodNames.GET_ACCOUNT_ORDERS) {
            response = Api.getAccountOrders(parametrs);
        }
        else if (methodName == AsyncMethodNames.UPDATE_ACCOUNT_ORDERS) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            response = Api.getAccountOrders(parametrs);
        }
        else if (methodName == AsyncMethodNames.GET_PAYMENTS) {
            response = Api.getPayments(parametrs);
        }
        else if (methodName == AsyncMethodNames.GET_WITHDRAWAL) {
            response = Api.getWithdrawal(parametrs);
        }
        else if (methodName == AsyncMethodNames.GET_NOTIFICATION)
        {
            response = Api.getNotification(parametrs);
        }
        else if (methodName == AsyncMethodNames.CLEAR_NOTIFICATIONS)
        {
            response = Api.clearNotifications(parametrs);
        }
        else if (methodName == AsyncMethodNames.REQUEST_TRANSFER)
        {
            response = Api.requestTransfer(parametrs);
        }
        else if (methodName == AsyncMethodNames.GET_TRANSFERS)
        {
            response = Api.getTransfers(parametrs);
        }
        return response;
    }

    @Override
    protected void onPostExecute(final String success) {

        if(success!=null) {
            if (success.contentEquals("Error with connection to Api")) {
                Toast toast = Toast.makeText(act.getApplicationContext(),
                        success, Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
        }


        if (methodName == AsyncMethodNames.USER_REGISTRATION) {
            Map<String, Object> map = Api.jsonToMap(success);
            for (Map.Entry<String, Object> response : map.entrySet()) {
                if (response.getKey().contentEquals("status")) {
                    if(response.getValue().toString().contentEquals("success")){
                        //((RegistrationActivity)act).goToLogin();
                        ((RegistrationActivity)act).showDialog(true, act.getString(R.string.account_register_info));
                    }

                }
                if(response.getKey().toString().contentEquals("errors")) {
                    for(Object list : (ArrayList<Object>)(response.getValue())){
                        for (Map.Entry<String, Object> error : ((Map<String, Object>) list).entrySet()) {
                            if (error.getKey().contentEquals("description")) {
                                ((RegistrationActivity)act).showDialog(false, (String) error.getValue());
                            }
                        }
                    }
                }
            }
        }
        else if(methodName == AsyncMethodNames.USER_BEFORE_REGISTRATION) {
            Map<String, Object> map = Api.jsonToMap(success);
            for (Map.Entry<String, Object> response : map.entrySet()) {

                if(response.getKey().toString().contentEquals("errors")) {
                    for(Object list : (ArrayList<Object>)(response.getValue())){
                        for (Map.Entry<String, Object> error : ((Map<String, Object>) list).entrySet()) {
                            if (error.getKey().contentEquals("description")) {
                                if(error.getValue().toString().contentEquals("Sms register does not exist!"))
                                {
                                    ((RegistrationActivity)act).goToStep3();
                                }
                                else {
                                    ((RegistrationActivity) act).showDialog(false, (String) error.getValue());
                                }
                            }
                        }
                    }
                }
                if(response.getKey().toString().contentEquals("data")) {

                    for (Map.Entry<String, Object> data : ((Map<String, Object>) response.getValue()).entrySet()) {
                        if (data.getKey().contentEquals("register_id")) {
                            String register_id = data.getValue().toString();
                            ((RegistrationActivity) act).requestCodeDialog("Your Phone " + target + ".\n\r Enter the code from Sms.", register_id);
                        }
                    }

                }
            }
            String str = "";
        }
        else if(methodName == AsyncMethodNames.USER_CHECK_BEFORE_REGISTRATION) {
            Map<String, Object> map = Api.jsonToMap(success);
            for (Map.Entry<String, Object> response : map.entrySet()) {

                if(response.getKey().toString().contentEquals("errors")) {
                    for(Object list : (ArrayList<Object>)(response.getValue())){
                        for (Map.Entry<String, Object> error : ((Map<String, Object>) list).entrySet()) {
                            if (error.getKey().contentEquals("code")) {
                                int errorCode = (int) Double.parseDouble(error.getValue().toString());
                                if(errorCode == 5)
                                {
                                    String errorText = "Incorrect code";
                                    ((RegistrationActivity)act).showError(errorText);
                                    return;
                                }

                            }
                            if(error.getKey().contentEquals("description"))
                            {
                                ((RegistrationActivity)act).showError(error.getValue().toString());
                            }
                        }
                    }
                }
                if(response.getKey().toString().contentEquals("status")) {

                    if(response.getValue().toString().contentEquals("success"))
                    {
                        ((RegistrationActivity)act).closeInputDialog();
                    }
                }
            }
        }
        else if (methodName == AsyncMethodNames.CHECK_TOKEN) {
            Map<String, Object> map = Api.jsonToMap(success);
            if (map.containsKey("data")) {
                int userId = -1;
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
                                getUserTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            }
                        }
                    }
                }
                if(userId == -1)
                {
                    ((LoginActivity) act).showProgress(false);
                }
            }
            else
            {
                ((LoginActivity) act).showProgress(false);
            }

        } else if (methodName == AsyncMethodNames.CHECK_EMAIL) {
            Map<String, Object> map = Api.jsonToMap(success);
            if (map.size() > 0) {
                boolean isAlreadyUse = true;
                for (Map.Entry<String, Object> response : map.entrySet()) {
                    if (response.getKey().contentEquals("status")) {
                        if (response.getValue().toString().contentEquals("success")) {
                            ((RegistrationActivity) act).emailChecked(isAlreadyUse);
                        }
                    }
                    if (response.getKey().contentEquals("errors")) {
                        for(Object list : (ArrayList<Object>)(response.getValue())) {
                            for (Map.Entry<String, Object> error : ((Map<String, Object>) list).entrySet()) {
                                if (error.getKey().contentEquals("code")) {
                                    int code = (int) Double.parseDouble(error.getValue().toString());
                                    if (code == 3) {
                                        isAlreadyUse = false;
                                        ((RegistrationActivity) act).emailChecked(isAlreadyUse);
                                    }
                                }
                            }
                        }

                    }
                }
            }
        } else if (methodName == AsyncMethodNames.GET_USER) {
            {
                User user = JsonResponseUser.getUser(success);

                if(act.getClass() == LoginActivity.class) {
                    Api.user = user;
                    ((LoginActivity) act).goToMenuActivity();
                }
                else
                {
                    Api.user.updateMetaData(user);
                }
            }
        }
        else if(methodName == AsyncMethodNames.SET_USER_AVATAR){
            Map<String, Object> map = Api.jsonToMap(success);
            for (Map.Entry<String, Object> response : map.entrySet()) {
                if (response.getKey().contentEquals("status")) {

                    if (response.getValue().toString().contentEquals("success")) {
                        Toast toast = Toast.makeText(act.getApplicationContext(),
                                "Avatar was saved!", Toast.LENGTH_SHORT);
                        toast.show();

                        HashMap<String, Object> parametrs = new HashMap<String, Object>();
                        parametrs.put("user", Api.user);
                        AsyncHttpTask getUserTask = new AsyncHttpTask(parametrs, AsyncMethodNames.GET_USER_AVATAR, act);
                        getUserTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }

                }
            }
        }
        else if(methodName == AsyncMethodNames.GET_USER_AVATAR){
            if(target.contentEquals(MenuFragment.class.toString()))
            {
                MainActivity.currentLoader.endLoading();
            }
        }
        else if(methodName == AsyncMethodNames.USER_LOGIN) {

            Map<String, Object> map = Api.jsonToMap(success);
            String token = null;
            int errorCode = -1;
            String errorDescription = null;

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
                ckeckTokenTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
            else if(errorCode != -1 && errorDescription != null){
                ((LoginActivity) act).showProgress(false);
                ((LoginActivity) act).showDialog(false, errorDescription);
            }
        }

        else if(methodName == AsyncMethodNames.GET_COUNTRIES)
        {
            Api.countries = JsonResponceCountries.getCountries(success);

            if(act != null) {
                if (act.getClass() == RegistrationActivity.class) {
                    ((RegistrationActivity) act).setCountries();
                }

            }
            if(MainActivity.currentLoader != null) {
                if (MainActivity.currentLoader.fragment.getClass() == UserPageFragment.class) {
                    MainActivity.currentLoader.endLoading();
                }
            }
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

                        HashMap<String, Object> userData = new HashMap<String, Object>();
                        userData.put("userId", Api.user.getId());
                        userData.put("token", Api.getToken());
                        AsyncHttpTask getUserTask = new AsyncHttpTask(userData, AsyncMethodNames.GET_USER, act);
                        getUserTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                }
            }
        }
        else if (methodName == AsyncMethodNames.GET_GROUPS)
        {
            Api.groups = JsonResponseGroups.getGroups(success);
            if(MainActivity.currentLoader.fragment.getClass() == CreateAccount.class) {
                MainActivity.currentLoader.endLoading();
            }
        }
        else if (methodName == AsyncMethodNames.CREATE_ACCOUNT)
        {
            Map<String, Object> map = null;
            try {
                map = Api.jsonToMap(success);
            }
            catch(Exception ex)
            {
                if(act.getClass() == MainActivity.class) {
                    ((MainActivity) act).showDialog(false, "Something went wrong. Please write to support.");
                    return;
                }
            }
            for (Map.Entry<String, Object> response : map.entrySet()) {
                if (response.getKey().contentEquals("data")) {
                    for (Map.Entry<String, Object> dataEntry : ((Map<String, Object>) (response.getValue())).entrySet()) {
                        if (dataEntry.getKey().contentEquals("login")) {

                            if(act.getClass() == MainActivity.class) {
                                ((MainActivity) act).showDialog(true, "Account was created with login: " + (int) (double) dataEntry.getValue());
                            }
                        }
                    }
                }
                if (response.getKey().contentEquals("errors")) {
                    Object tmp = response.getValue();
                    ArrayList<Object> errorList = (ArrayList<Object>)response.getValue();
                    for(Object error: errorList) {
                        Map<String, Object> tmpMap = (Map<String, Object>) error;
                        for (Map.Entry<String, Object> dataEntry : tmpMap.entrySet()) {
                            if (dataEntry.getKey().contentEquals("description")) {
                                String description = (String) dataEntry.getValue();
                                if(act.getClass() == MainActivity.class) {
                                    ((MainActivity) act).showDialog(false, description);
                                }
                            }
                        }
                    }
                }
            }
        }
        else if (methodName == AsyncMethodNames.GET_ACCOUNTS)
        {
            Api.user.accounts = JsonResponseAccounts.getAccounts(success);
            if(MainActivity.currentLoader.fragment.getClass() == MyAccounts.class) {
                if(target.contentEquals(MyAccounts.class.toString())) {
                    MainActivity.currentLoader.endLoading();
                }
                else{
                    MyAccounts.updateAccounts(Api.user.accounts);
                }
            }
            if(MainActivity.currentLoader.fragment.getClass() == TransfersFragment.class) {
                MainActivity.currentLoader.endLoading();
            }

        }

        else if(methodName == AsyncMethodNames.GET_ACCOUNT_HOLDER_NAME) {
            Map<String, Object> map = Api.jsonToMap(success);
            String accountHolderName = "";
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if(entry.getKey().contentEquals("status"))
                {
                    if(entry.getValue().toString().contentEquals("error"))
                    {
                        RequestTransferFragment.setError(target);
                        return;
                    }
                }
                if(entry.getKey().contentEquals("data"))
                {
                    Map<String, Object> tmp2 = (Map<String, Object>) entry.getValue();
                    //Map<String, Object> map2 = entry.getValue();
                    /*for (Map.Entry<String, Object> dataEntry : entry.entrySet()) {

                    }*/
                    for (Map.Entry<String, Object> dataEntry : tmp2.entrySet()) {
                        if(dataEntry.getKey().contentEquals("name")) {
                            accountHolderName = dataEntry.getValue().toString();
                            RequestTransferFragment.setHolderName(accountHolderName, target);
                        }
                    }
                }
            }
        }
        else if (methodName == AsyncMethodNames.GET_CATEGORIES)
        {
            Api.categories = JsonResponseCategories.getCategories(success);
            if(MainActivity.currentLoader.fragment.getClass() == Support.class) {
                MainActivity.currentLoader.endLoading();
            }
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
                            Api.user.tickets.remove(Api.user.getCurrentTicketId());
                            Api.user.setCurrentTicketId(0);

                            SupportChat.tickets.add(ticket);

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
                            sendMessageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                            if(act.getClass() == MainActivity.class) {
                                ((MainActivity) act).showDialog(true, "Ticket was created!");
                            }
                        }
                    }
                }
                if (response.getKey().contentEquals("errors")) {
                    ArrayList<Object> errorList = (ArrayList<Object>)response.getValue();
                    for(Object error: errorList) {
                        Map<String, Object> tmpMap = (Map<String, Object>) error;
                        for (Map.Entry<String, Object> dataEntry : tmpMap.entrySet()) {
                            if (dataEntry.getKey().contentEquals("description")) {
                                String description = (String) dataEntry.getValue();

                                if(act.getClass() == MainActivity.class) {
                                    ((MainActivity) act).showDialog(false, description);
                                }
                            }
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

                    }
                }
            }
        }
        else if (methodName == AsyncMethodNames.GET_TICKETS) {
            Map<String, Object> map = Api.jsonToMap(success);

            JsonResponseTickets jrt = new JsonResponseTickets(success);
            SupportChat.tickets = jrt.getTickets();
            int countLastMessages = jrt.getLastMessages();

            AsyncHttpTask getDataFromTickets = new AsyncHttpTask(null, AsyncMethodNames.GET_DATA_FROM_TICKETS, act);
            getDataFromTickets.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else if (methodName == AsyncMethodNames.GET_DATA_FROM_TICKETS) {

            AsyncHttpTask getUserTask = new AsyncHttpTask(null, AsyncMethodNames.WAIT_AVATAR_LOADING, act);
            getUserTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else if (methodName == AsyncMethodNames.GET_TICKET_MESSAGES) {
            Api.currentChatMessages = JsonResponseTicketMessages.getMessages(success);
            Map<String, Object> map = Api.jsonToMap(success);
            SupportChat.goToChatActivity();
        }

        else if (methodName == AsyncMethodNames.GET_NEW_MESSAGE) {
            try {
                SupportChat.handler.endListen();
                Map<String, Object> map = Api.jsonToMap(success);
                List<Message> newMessages = null;
                for (Map.Entry<String, Object> response : map.entrySet()) {
                    if (response.getKey().contentEquals("status")) {

                        if (response.getValue().toString().contentEquals("success")) {
                            newMessages = JsonResponseTicketMessages.getMessages(success);
                        }
                    }
                }
                if (newMessages != null) {
                    SupportChat.checkNewMessages(newMessages, act);
                }
                if (!act.isFinishing()) {
                    SupportChat.newMessagesHandler();
                }
            } catch(Exception ex)
            {
                String str = ex.getMessage();
            }
        }
        else if (methodName == AsyncMethodNames.WAIT_AVATAR_LOADING) {

            if(act.getClass() == ChatActivity.class)
                ((ChatActivity) act).setMessages();
            else
                SupportChat.updateTickets();

        }
        else if (methodName == AsyncMethodNames.GET_DATA_FROM_MESSAGES) {
            SupportChat.newMessages();
        }
        else if (methodName == AsyncMethodNames.GET_ATTACHMENT) {
            ((MessagesAdapter)ChatActivity.lvMessages.getAdapter()).notifyDataSetChanged();
        }
        else if (methodName == AsyncMethodNames.SET_ATTACHMENT) {
            /*Map<String, Object> map = Api.jsonToMap(success);
            for (Map.Entry<String, Object> response : map.entrySet()) {
                if (response.getKey().contentEquals("status")) {

                    if (response.getValue().toString().contentEquals("success")) {
                        Toast toast = Toast.makeText(act.getApplicationContext(),
                                "Attachment was send!", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                }
            }*/
        }
        else if (methodName == AsyncMethodNames.GET_ACCOUNT_ORDERS) {
            List<Order> orders = JsonResponseOrders.getOrders(success);
            Api.account.openOrders = new ArrayList<Order>();
            Api.account.closeOrders = new ArrayList<Order>();
            for(Order o: orders)
            {
                if (o.getCmd().contentEquals("Sell") || o.getCmd().contentEquals("Buy")) {
                    if (o.getCloseTime().contentEquals("1970-01-01 00:00:00"))
                        Api.account.openOrders.add(o);
                    else
                        Api.account.closeOrders.add(o);
                }
            }
            if (MainActivity.currentLoader.fragment.getClass() == AccountOrdersFragment.class) {
                MainActivity.currentLoader.endLoading();
            }

        }
        else if (methodName == AsyncMethodNames.UPDATE_ACCOUNT_ORDERS) {
            OpenOrdersFragment.updateOrders(JsonResponseOrders.getOrders(success));
            if(act.getClass().toString().contentEquals(MainActivity.class.toString())) {
                if (OpenOrdersFragment.isNeedUpdate) {
                    AsyncHttpTask updateOrders = new AsyncHttpTask(parametrs, AsyncMethodNames.UPDATE_ACCOUNT_ORDERS, act);
                    updateOrders.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }
        }

        else if (methodName == AsyncMethodNames.GET_WITHDRAWAL)
        {
            Map<String, Object> map = Api.jsonToMap(success);
            Api.withdrawals = JsonResponseWithdrawal.getWithdrawals(success);

            FinanceHistoryFragment.isWithdrawalLoaded = true;
            if(FinanceHistoryFragment.isWithdrawalLoaded && FinanceHistoryFragment.isDepositsLoaded) {
                if (MainActivity.currentLoader.fragment.getClass() == FinanceHistoryFragment.class) {
                    MainActivity.currentLoader.endLoading();
                }
            }
        }

        else if (methodName == AsyncMethodNames.GET_PAYMENTS)
        {
            Map<String, Object> map = Api.jsonToMap(success);
            Api.deposits = JsonResponseWithdrawal.getWithdrawals(success);
            FinanceHistoryFragment.isDepositsLoaded = true;
            if(FinanceHistoryFragment.isWithdrawalLoaded && FinanceHistoryFragment.isDepositsLoaded) {
                if (MainActivity.currentLoader.fragment.getClass() == FinanceHistoryFragment.class) {
                    MainActivity.currentLoader.endLoading();
                }
            }
        }
        else if (methodName == AsyncMethodNames.REQUEST_TRANSFER)
        {
            Map<String, Object> map = Api.jsonToMap(success);
            if(act != null)
            {
                if(act.getClass() == MainActivity.class)
                {
                    for (Map.Entry<String, Object> response : map.entrySet()) {
                        if (response.getKey().contentEquals("status")) {
                            if(response.getValue().toString().contentEquals("success"))
                            {
                                MainActivity activity = (MainActivity) act;
                                activity.showDialog(true, "Transfer was requested");
                            }
                        }
                        if (response.getKey().contentEquals("errors")) {
                            ArrayList<Object> errorList = (ArrayList<Object>)response.getValue();
                            for(Object error: errorList) {
                                Map<String, Object> tmpMap = (Map<String, Object>) error;
                                for (Map.Entry<String, Object> dataEntry : tmpMap.entrySet()) {
                                    if (dataEntry.getKey().contentEquals("description")) {
                                        String description = (String) dataEntry.getValue();

                                        MainActivity activity = (MainActivity) act;
                                        activity.showDialog(false, description);
                                    }
                                }
                            }
                        }
                    }

                }
            }

        }
        else if (methodName == AsyncMethodNames.GET_TRANSFERS)
        {
            Map<String, Object> map = Api.jsonToMap(success);
            List<Transfer> transfers = JsonResponseTransfers.getTransfers(success);
            Api.transfers = transfers;
            if(target.contentEquals(TransfersHistoryFragment.class.toString())) {
                TransfersHistoryFragment.setList();
            }
            else{
                TransfersHistoryFragment.updateList();
            }

        }
    }
}