package com.example.eqvol.eqvola.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableLayout;

import com.example.eqvol.eqvola.Adapters.AccountsAdapter;
import com.example.eqvol.eqvola.Classes.Account;
import com.example.eqvol.eqvola.Classes.Api;
import com.example.eqvol.eqvola.Classes.AsyncHttpTask;
import com.example.eqvol.eqvola.Classes.AsyncMethodNames;
import com.example.eqvol.eqvola.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.List;


public class MyAccounts extends Fragment {

    private static View mView = null;

    public MyAccounts()
    {
        Gson gson = new GsonBuilder().create();
        HashMap<String, String> mapUserId = new HashMap<String, String>();
        mapUserId.put("email", Api.user.getEmail());
        String json = gson.toJson(mapUserId);
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("token", Api.getToken());
        params.put("where", json);

        AsyncHttpTask userLoginTask = new AsyncHttpTask(params, AsyncMethodNames.GET_ACCOUNTS, getActivity());
        userLoginTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_my_accounts, container, false);
        createTable();
        return mView;
    }


    public void createTable() {
        List<Account> accounts = Api.user.accounts;
        ListView gl = (ListView) mView.findViewById(R.id.gridView1);
        gl.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                ConstraintLayout constraintLayout = (ConstraintLayout)arg1.findViewById(R.id.layoutDetail);
                if(constraintLayout.getVisibility() == View.VISIBLE) {
                    constraintLayout.setVisibility(View.GONE);
                }
                else {
                    constraintLayout.setVisibility(View.VISIBLE);
                }
            }

        });
        ArrayAdapter<Account> adapter = new AccountsAdapter(mView.getContext(), accounts);
        gl.setAdapter(adapter);
    }
}
