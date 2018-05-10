package com.example.eqvol.eqvola.Classes;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.eqvol.eqvola.Adapters.MessagesAdapter;
import com.example.eqvol.eqvola.ChatActivity;
import com.example.eqvol.eqvola.ForgotPasswordActivity;
import com.example.eqvol.eqvola.JsonResponse.JsonResponceCountries;
import com.example.eqvol.eqvola.JsonResponse.JsonResponseAccounts;
import com.example.eqvol.eqvola.JsonResponse.JsonResponseCategories;
import com.example.eqvol.eqvola.JsonResponse.JsonResponseGroups;
import com.example.eqvol.eqvola.JsonResponse.JsonResponseOrders;
import com.example.eqvol.eqvola.JsonResponse.JsonResponseTicketMessages;
import com.example.eqvol.eqvola.JsonResponse.JsonResponseTickets;
import com.example.eqvol.eqvola.JsonResponse.JsonResponseTransfers;
import com.example.eqvol.eqvola.JsonResponse.JsonResponseUser;
import com.example.eqvol.eqvola.JsonResponse.JsonResponseWithdrawal;
import com.example.eqvol.eqvola.JsonResponse.JsonResponseWithdrawalPayments;
import com.example.eqvol.eqvola.LoginActivity;
import com.example.eqvol.eqvola.MainActivity;
import com.example.eqvol.eqvola.R;
import com.example.eqvol.eqvola.RegistrationActivity;
import com.example.eqvol.eqvola.fragments.AccountOrdersFragment;
import com.example.eqvol.eqvola.fragments.CreateAccount;
import com.example.eqvol.eqvola.fragments.CreateOrderFragment;
import com.example.eqvol.eqvola.fragments.DepositsRecyclerFragment;
import com.example.eqvol.eqvola.fragments.FinanceHistoryFragment;
import com.example.eqvol.eqvola.fragments.MenuFragment;
import com.example.eqvol.eqvola.fragments.MobileTraderFragment;
import com.example.eqvol.eqvola.fragments.MyAccounts;
import com.example.eqvol.eqvola.fragments.OpenOrdersForAccountFragment;
import com.example.eqvol.eqvola.fragments.OpenOrdersFragment;
import com.example.eqvol.eqvola.fragments.OrderFragment;
import com.example.eqvol.eqvola.fragments.RequestTransferFragment;
import com.example.eqvol.eqvola.fragments.RequestWithdrawalFragment;
import com.example.eqvol.eqvola.fragments.Support;
import com.example.eqvol.eqvola.fragments.SupportChat;
import com.example.eqvol.eqvola.fragments.TradingHistoryFragment;
import com.example.eqvol.eqvola.fragments.TransfersFragment;
import com.example.eqvol.eqvola.fragments.TransfersHistoryFragment;
import com.example.eqvol.eqvola.fragments.UserPageFragment;
import com.example.eqvol.eqvola.fragments.WithdrawalPaymentsFragment;
import com.example.eqvol.eqvola.fragments.WithdrawalsRecyclerFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;


import org.json.JSONArray;

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
            /*String email = "";
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

            }*/
            response = Api.login(parametrs);

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
        else if(methodName == AsyncMethodNames.APPROVE_EMAIL)
        {
            response = Api.approveEmail(parametrs);
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
            String str ="";
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
        else if (methodName == AsyncMethodNames.CLOSE_TICKET)
        {
            response = Api.closeTicket(parametrs);
        }
        else if (methodName == AsyncMethodNames.GET_DATA_FROM_TICKETS)
        {
            response = "";
            SupportChat.checkUsersInTickets(act);
            return null;
        }
        else if (methodName == AsyncMethodNames.GET_TICKET_MESSAGES)
        {
            //SupportChat.goToChatActivity();
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
        else if (methodName == AsyncMethodNames.GET_ORDER_UPDATE) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            response = Api.getOrderUpdate(parametrs);
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
        else if (methodName == AsyncMethodNames.FORGOT_PASSWORD)
        {
            response = Api.forgotPassword(parametrs);
        }
        else if (methodName == AsyncMethodNames.OPEN_ORDER)
        {
            response = Api.openOrder(parametrs);
        }
        else if (methodName == AsyncMethodNames.GET_TRADE_PAIRS)
        {
            response = Api.getTradePairs(parametrs);
        }
        else if (methodName == AsyncMethodNames.CLOSE_ORDER)
        {
            response = Api.closeOrder(parametrs);
        }
        else if (methodName == AsyncMethodNames.UPDATE_ORDER)
        {
            response = Api.updateOrder(parametrs);
        }
        else if (methodName == AsyncMethodNames.DELETE_ORDER)
        {
            response = Api.deleteOrder(parametrs);
        }
        else if (methodName == AsyncMethodNames.GET_WITHDRAWAL_PAYMENTS)
        {
            response = Api.getWithdrawalPayments(parametrs);
        }
        else if (methodName == AsyncMethodNames.GET_PAYMENT_IMAGE)
        {
            try {
                response = Api.getPaymentImage(parametrs);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        else if (methodName == AsyncMethodNames.REQUEST_WITHDRAWAL)
        {
            response = Api.requestWithdrawal(parametrs);
        }
        return response;
    }

    @Override
    protected void onPostExecute(final String success) {

        if(success!=null) {
            try {
                if (success.contentEquals("Error with connection to Api") || success.contentEquals("Problems with connections")) {
                    Toast toast2 = Toast.makeText(Api.context,
                            success, Toast.LENGTH_SHORT);
                    toast2.show();
                    if (act.getClass() == LoginActivity.class) {
                        ((LoginActivity) act).showProgress(false);
                    }
                    return;
                }
            } catch(Exception e)
            {
                return;
            }
        }


        if (methodName == AsyncMethodNames.USER_REGISTRATION) {
            Map<String, Object> map = Api.jsonToMap(success);
            for (Map.Entry<String, Object> response : map.entrySet()) {
                if (response.getKey().contentEquals("status")) {
                    if(response.getValue().toString().contentEquals("success")){
                        ((RegistrationActivity)act).inputCodeDialog(act.getString(R.string.account_register_info));
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
        else if(methodName == AsyncMethodNames.USER_RESEND_BEFORE_REGISTRATION) {
            Map<String, Object> map = Api.jsonToMap(success);
            String str = "";
            ((RegistrationActivity)act).startTimer();

        }
        else if(methodName == AsyncMethodNames.APPROVE_EMAIL)
        {
            Map<String, Object> map = Api.jsonToMap(success);
            for (Map.Entry<String, Object> response : map.entrySet()) {

                if (response.getKey().toString().contentEquals("status")) {

                    if (response.getValue().toString().contentEquals("success")) {
                        if(act.getClass() == RegistrationActivity.class)
                        {
                            ((RegistrationActivity) act).emailApproved();
                        }
                        else if(act.getClass() == LoginActivity.class) {
                            ((LoginActivity) act).emailApproved();
                        }
                    }
                    else
                    {
                        if(act.getClass() == RegistrationActivity.class)
                        {
                            ((RegistrationActivity) act).wrongApproveCode();
                        }
                        else if(act.getClass() == LoginActivity.class) {
                            ((LoginActivity) act).wrongApproveCode();
                        }

                    }
                }
            }
        }
        else if (methodName == AsyncMethodNames.CHECK_TOKEN) {
            Map<String, Object> map = Api.jsonToMap(success);
            if(map == null)
            {
                return;
            }
            if (map.containsKey("data")) {
                /*int userId = -1;
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    if (entry.getKey().contentEquals("data"))
                    {
                        for (Map.Entry<String, Object> data : ((LinkedTreeMap<String, Object>) entry.getValue()).entrySet()) {
                            if (data.getKey().contentEquals("user_id")) {
                                userId = (int) ((double) data.getValue());


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
                }*/
                ((LoginActivity)act).saveTokenInFile(Api.getToken());
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
            else if(errorCode == 3 && errorDescription.contains("Email approve does not exist!"))
            {
                ((LoginActivity) act).showProgress(false);
                ((LoginActivity) act).inputCodeDialog(act.getString(R.string.account_login_email_not_approved));
            }
            else if(errorDescription.contains("Verification code does not exist"))
            {
                ((LoginActivity) act).showProgress(false);
                ((LoginActivity) act).twoStepDialog(act.getString(R.string.account_two_step_auth));
            }
            else if(errorDescription.contains("Verification code is incorrect"))
            {
                ((LoginActivity) act).showProgress(false);
                ((LoginActivity) act).wrongTwoStepCode();
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
            Map<String, Object> map = Api.jsonToMap(success);
            Api.user.accounts = JsonResponseAccounts.getAccounts(success);
            if(MainActivity.currentLoader.fragment.getClass() == MyAccounts.class) {
                if(target.contentEquals(MyAccounts.class.toString())) {
                    MainActivity.currentLoader.endLoading();
                }
                else{
                    MyAccounts.updateAccounts(Api.user.accounts);
                }
            }
            else if(MainActivity.currentLoader.fragment.getClass() == TransfersFragment.class) {
                MainActivity.currentLoader.endLoading();
            }
            else if(MainActivity.currentLoader.fragment.getClass() == MobileTraderFragment.class)
            {
                MobileTraderFragment.isAccountsLoaded = true;
                if(MobileTraderFragment.isOrderLoaded && MobileTraderFragment.isAccountsLoaded) {
                    MainActivity.currentLoader.endLoading();
                }
            }
            else if(MainActivity.currentLoader.fragment.getClass() == RequestWithdrawalFragment.class) {
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
                                //((MainActivity) act).showDialog(true, "Ticket was created!");

                                SupportChat.currentTicketId = ticket.getId();
                                SupportChat.title = ticket.getTitle();
                                SupportChat.currentTicketLastMessageId = 0;
                                HashMap<String, Object> where = new HashMap<String, Object>();
                                where.put("ticket_id", ticket.getId());
                                String jsonWhere = gson.toJson(where);
                                HashMap<String, Object> parametrs = new HashMap<String, Object>();
                                parametrs.put("token", Api.getToken());
                                parametrs.put("where", jsonWhere);

                                AsyncHttpTask userLoginTask = new AsyncHttpTask(parametrs, AsyncMethodNames.GET_TICKET_MESSAGES, act);
                                userLoginTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
        else if (methodName == AsyncMethodNames.CLOSE_TICKET)
        {
            Map<String, Object> map = Api.jsonToMap(success);
            for (Map.Entry<String, Object> response : map.entrySet()) {
                if (response.getKey().contentEquals("data")) {

                    for (Map.Entry<String, Object> dataEntry : ((Map<String, Object>)response.getValue()).entrySet()) {
                        if (dataEntry.getKey().toString().contentEquals("ticket_id")) {

                            int ticket_id =(int)((double)dataEntry.getValue());
                            SupportChat.closeTicket(ticket_id);
                            try {
                                if (ChatActivity.ticket_id == ticket_id) {
                                    ChatActivity.setClosed(ticket_id);
                                }
                            } catch (Exception ex)
                            {

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
            Map<String, Object> map = Api.jsonToMap(success);
            for (Map.Entry<String, Object> response : map.entrySet()) {
                if (response.getKey().contentEquals("status")) {
                    if (response.getValue().toString().contentEquals("success")) {
                        Api.currentChatMessages = JsonResponseTicketMessages.getMessages(success);
                        //ChatActivity.load();
                        SupportChat.goToChatActivity();
                    }
                    else
                    {
                        Api.currentChatMessages = null;

                    }
                }
            }
            /*Api.currentChatMessages = JsonResponseTicketMessages.getMessages(success);

            SupportChat.goToChatActivity();*/
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

            Api.openOrders = new ArrayList<Order>();

            if (MainActivity.currentLoader.fragment.getClass() == AccountOrdersFragment.class) {
                if(target.contentEquals(TransfersHistoryFragment.class.toString()))
                {
                    Api.account.closeOrders = new ArrayList<Order>();
                    for(Order o: orders)
                    {
                        if (o.getCmd().contains("Sell") || o.getCmd().contains("Buy")) {
                            if (!o.getCloseTime().contentEquals("1970-01-01 00:00:00"))
                                Api.account.closeOrders.add(o);
                        }
                    }
                    TradingHistoryFragment.updateList(Api.account.closeOrders);
                }
                else if(target.contentEquals(OpenOrdersForAccountFragment.class.toString()))
                {

                    Api.account.openOrders = new ArrayList<Order>();
                    for(Order o: orders)
                    {
                        if (o.getCmd().contains("Sell") || o.getCmd().contains("Buy")) {
                            if (o.getCloseTime().contentEquals("1970-01-01 00:00:00"))
                                Api.account.openOrders.add(o);
                        }
                    }
                    OpenOrdersForAccountFragment.updateOrders(Api.account.openOrders);
                }
                else {
                    Api.account.openOrders = new ArrayList<Order>();
                    Api.account.closeOrders = new ArrayList<Order>();
                    for(Order o: orders)
                    {
                        if (o.getCmd().contains("Sell") || o.getCmd().contains("Buy")) {
                            if (o.getCloseTime().contentEquals("1970-01-01 00:00:00"))
                                Api.account.openOrders.add(o);
                            else
                                Api.account.closeOrders.add(o);
                        }
                    }
                    MainActivity.currentLoader.endLoading();
                }
            }
            else if (MainActivity.currentLoader.fragment.getClass() == MobileTraderFragment.class)
            {
                if(target.contentEquals(MobileTraderFragment.class.toString())) {
                    Api.openOrders = new ArrayList<Order>();
                    for (Order o : orders) {
                        if (o.getCmd().contains("Sell") || o.getCmd().contains("Buy")) {
                            if (o.getCloseTime().contentEquals("1970-01-01 00:00:00"))
                                Api.openOrders.add(o);
                        }
                    }
                    MobileTraderFragment.isOrderLoaded = true;
                    if(MobileTraderFragment.isOrderLoaded && MobileTraderFragment.isAccountsLoaded) {
                        MainActivity.currentLoader.endLoading();
                    }
                }
                else if(target.contentEquals(OpenOrdersFragment.class.toString()))
                {
                    Api.openOrders = new ArrayList<Order>();
                    for(Order o: orders)
                    {
                        if (o.getCmd().contains("Sell") || o.getCmd().contains("Buy")) {
                            if (o.getCloseTime().contentEquals("1970-01-01 00:00:00"))
                                Api.openOrders.add(o);
                        }
                    }
                    OpenOrdersFragment.updateOrders(Api.openOrders);
                }
            }

        }
        else if (methodName == AsyncMethodNames.UPDATE_ACCOUNT_ORDERS) {
            if(target.contentEquals(OpenOrdersForAccountFragment.class.toString())) {
                OpenOrdersForAccountFragment.updateOrders(JsonResponseOrders.getOrders(success));
                if (act.getClass().toString().contentEquals(MainActivity.class.toString())) {
                    if (OpenOrdersForAccountFragment.isNeedUpdate) {
                        AsyncHttpTask updateOrders = new AsyncHttpTask(parametrs, AsyncMethodNames.UPDATE_ACCOUNT_ORDERS, act);
                        updateOrders.target = target;
                        updateOrders.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                }
            }
            else if(target.contentEquals(OpenOrdersFragment.class.toString()))
            {
                OpenOrdersFragment.updateOrders(JsonResponseOrders.getOrders(success));
                if (act.getClass().toString().contentEquals(MainActivity.class.toString())) {
                    if (OpenOrdersFragment.isNeedUpdate) {
                        AsyncHttpTask updateOrders = new AsyncHttpTask(parametrs, AsyncMethodNames.UPDATE_ACCOUNT_ORDERS, act);
                        updateOrders.target = target;
                        updateOrders.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                }
            }
        }
        else if (methodName == AsyncMethodNames.GET_ORDER_UPDATE) {
            List<Order> orders = JsonResponseOrders.getOrders(success);
            if(OrderFragment.isNeedUpdate)
            {
                int order = OrderFragment.order.getOrder();
                for(Order o: orders) {
                    if(o.getOrder() == order) {

                        OrderFragment.order = o;
                        OrderFragment.updateData();
                        break;
                    }
                }
            }

            if(OrderFragment.isNeedUpdate) {
                AsyncHttpTask updateOrders = new AsyncHttpTask(parametrs, AsyncMethodNames.GET_ORDER_UPDATE, act);
                updateOrders.target = target;
                updateOrders.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
        else if (methodName == AsyncMethodNames.GET_WITHDRAWAL)
        {
            Map<String, Object> map = Api.jsonToMap(success);
            Api.withdrawals = JsonResponseWithdrawal.getWithdrawals(success);

            FinanceHistoryFragment.isWithdrawalLoaded = true;
            if(FinanceHistoryFragment.isWithdrawalLoaded && FinanceHistoryFragment.isDepositsLoaded) {
                if (MainActivity.currentLoader.fragment.getClass() == FinanceHistoryFragment.class) {
                    if(target.contentEquals(WithdrawalsRecyclerFragment.class.toString())) {
                        MainActivity.currentLoader.endLoading();
                    }
                    else
                    {
                        WithdrawalsRecyclerFragment.updateWithdrawals(Api.withdrawals);
                    }
                }
            }
        }

        else if (methodName == AsyncMethodNames.GET_PAYMENTS)
        {
            Map<String, Object> map = Api.jsonToMap(success);
            String parsedResponse = success;
            for (Map.Entry<String, Object> response : map.entrySet()) {
                if (response.getKey().contentEquals("data")) {
                    ArrayList<Object> list = (ArrayList<Object>)response.getValue();
                    for(Object object: list) {
                        Map<String, Object> tmpMap = (Map<String, Object>) object;
                        for (Map.Entry<String, Object> entry1 : tmpMap.entrySet()) {

                            if (entry1.getKey().contentEquals("payment_setting")) {

                                if(entry1.getValue().getClass() == ArrayList.class) {
                                    ArrayList payment = (ArrayList) entry1.getValue();
                                    if (payment.size() == 0) {
                                        String str = "";
                                        parsedResponse = parsedResponse.replace("\"payment_setting\": []", "\"payment_setting\": {}");
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Api.deposits = JsonResponseWithdrawal.getWithdrawals(parsedResponse);
            FinanceHistoryFragment.isDepositsLoaded = true;
            if(FinanceHistoryFragment.isWithdrawalLoaded && FinanceHistoryFragment.isDepositsLoaded) {
                if (MainActivity.currentLoader.fragment.getClass() == FinanceHistoryFragment.class) {
                    if(target.contentEquals(DepositsRecyclerFragment.class.toString())) {
                        MainActivity.currentLoader.endLoading();
                    }
                    else
                    {
                        DepositsRecyclerFragment.updateDeposits(Api.deposits);
                    }
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
        else if (methodName == AsyncMethodNames.FORGOT_PASSWORD)
        {
            Map<String, Object> map = Api.jsonToMap(success);
            for (Map.Entry<String, Object> response : map.entrySet()) {
                if (response.getKey().contentEquals("status")) {
                    if(response.getValue().toString().contentEquals("success"))
                    {
                        if(act.getClass() == ForgotPasswordActivity.class) {
                            ForgotPasswordActivity activity = (ForgotPasswordActivity) act;
                            activity.showDialog(true, "A letter with further instructions has been send to your email.", act);
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

                                if(act.getClass() == ForgotPasswordActivity.class) {
                                    ForgotPasswordActivity activity = (ForgotPasswordActivity) act;
                                    activity.showDialog(false, description, act);
                                }
                            }
                        }
                    }
                }
            }
        }
        else if (methodName == AsyncMethodNames.USER_LOGOUT)
        {
            String str = "";
        }
        else if (methodName == AsyncMethodNames.OPEN_ORDER)
        {
            //Map<String, Object> map = Api.jsonToMap(success);
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
                if (response.getKey().contentEquals("status")) {
                    if(response.getValue().toString().contentEquals("success"))
                    {

                        ((MainActivity)act).showDialog(true, "Order has been created.");
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
        else if (methodName == AsyncMethodNames.GET_TRADE_PAIRS)
        {

            Map<String, Object> map = Api.jsonToMap(success);
            String str = "";
            for (Map.Entry<String, Object> response : map.entrySet()) {
                if (response.getKey().contentEquals("data")) {
                    List<String> pairList = (List<String>)response.getValue();
                    CreateOrderFragment.updateSpinnerQuotations(pairList);
                }

            }
        }
        else if (methodName == AsyncMethodNames.CLOSE_ORDER)
        {
            Map<String, Object> map = Api.jsonToMap(success);
            for (Map.Entry<String, Object> response : map.entrySet()) {
                if (response.getKey().contentEquals("status")) {
                    if(response.getValue().toString().contentEquals("success"))
                    {
                        if(act.getClass() == MainActivity.class) {
                            MainActivity activity = (MainActivity) act;
                            activity.showDialog(true, "Order has been closed.", act, "close_order");
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
                                    MainActivity activity = (MainActivity) act;
                                    activity.showDialog(false, description);
                                }
                            }
                        }
                    }
                }
            }
        }
        else if (methodName == AsyncMethodNames.UPDATE_ORDER)
        {

            Map<String, Object> map = Api.jsonToMap(success);
            for (Map.Entry<String, Object> response : map.entrySet()) {
                if (response.getKey().contentEquals("status")) {
                    if(response.getValue().toString().contentEquals("success"))
                    {
                        if(act.getClass() == MainActivity.class) {
                            MainActivity activity = (MainActivity) act;
                            activity.showDialog(true, "Order has been updated.");
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
                                    MainActivity activity = (MainActivity) act;
                                    activity.showDialog(false, description);
                                }
                            }
                        }
                    }
                }
            }
        }
        else if (methodName == AsyncMethodNames.DELETE_ORDER)
        {
            Map<String, Object> map = Api.jsonToMap(success);
            for (Map.Entry<String, Object> response : map.entrySet()) {
                if (response.getKey().contentEquals("status")) {
                    if(response.getValue().toString().contentEquals("success"))
                    {
                        if(act.getClass() == MainActivity.class) {
                            MainActivity activity = (MainActivity) act;
                            activity.showDialog(true, "Order has been deleted.", act, "delete_order");
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
                                    MainActivity activity = (MainActivity) act;
                                    activity.showDialog(false, description);
                                }
                            }
                        }
                    }
                }
            }
        }
        else if (methodName == AsyncMethodNames.GET_WITHDRAWAL_PAYMENTS)
        {
            List<PaymentSystem> withdrawalPayments = JsonResponseWithdrawalPayments.getWithdrawalPayments(success);
            Api.withdrawalPayments = withdrawalPayments;
            WithdrawalPaymentsFragment.getImageForPayments();
        }
        else if (methodName == AsyncMethodNames.GET_PAYMENT_IMAGE)
        {
            WithdrawalPaymentsFragment.imageLoading();
        }

        else if (methodName == AsyncMethodNames.REQUEST_WITHDRAWAL)
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
                if (response.getKey().contentEquals("status")) {
                    if (((String)response.getValue()).contentEquals("success")) {

                        if(act.getClass() == MainActivity.class) {
                            ((MainActivity) act).showDialog(true, "Request for withdrawal was sent!");
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
    }
}