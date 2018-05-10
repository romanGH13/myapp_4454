package com.example.eqvol.eqvola.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.eqvol.eqvola.Adapters.MyAccountsAdapter;
import com.example.eqvol.eqvola.Classes.Account;
import com.example.eqvol.eqvola.Classes.Api;
import com.example.eqvol.eqvola.Classes.AsyncHttpTask;
import com.example.eqvol.eqvola.Classes.AsyncMethodNames;
import com.example.eqvol.eqvola.Classes.SpaceItemDecoration;
import com.example.eqvol.eqvola.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.List;


public class MyAccounts extends Fragment {

    private static View mView = null;
    private static EditText mSearchView;
    private static RecyclerView list = null;
    private static SwipeRefreshLayout swipeRefreshLayout;
    private static MyAccountsAdapter adapter;

    public MyAccounts()
    {
        Gson gson = new GsonBuilder().create();
        HashMap<String, String> mapUserId = new HashMap<String, String>();
        mapUserId.put("email", Api.user.getEmail());
        String json = gson.toJson(mapUserId);
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("token", Api.getToken());
        params.put("where", json);

        AsyncHttpTask getAllAccountsTask = new AsyncHttpTask(params, AsyncMethodNames.GET_ACCOUNTS, getActivity());
        getAllAccountsTask.target = MyAccounts.class.toString();
        getAllAccountsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_my_accounts, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout)mView.findViewById(R.id.my_accounts_swipe_refresh);
        mSearchView = (EditText) mView.findViewById(R.id.search);

        createTable();
        return mView;
    }


    public void createTable() {
        List<Account> accounts = Api.user.accounts;

        list = (RecyclerView) mView.findViewById(R.id.my_account_list);
        list.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mView.getContext());
        llm.setItemPrefetchEnabled(false);
        list.setLayoutManager(llm);
        list.addItemDecoration(new DividerItemDecoration(mView.getContext(), LinearLayout.VERTICAL));
        list.addItemDecoration(new SpaceItemDecoration(10, mView.getContext()));

        adapter = new MyAccountsAdapter(mView.getContext(), mSearchView);
        list.setAdapter(adapter);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Gson gson = new GsonBuilder().create();
                HashMap<String, String> mapUserId = new HashMap<String, String>();
                mapUserId.put("email", Api.user.getEmail());
                String json = gson.toJson(mapUserId);
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("token", Api.getToken());
                params.put("where", json);

                AsyncHttpTask getAllAccountsTask = new AsyncHttpTask(params, AsyncMethodNames.GET_ACCOUNTS, getActivity());
                getAllAccountsTask.target = "update";
                getAllAccountsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            }
        });

        mSearchView.addTextChangedListener(new TextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                if(adapter!= null) {
                    adapter.searchStringChanged();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
    }

    public static void updateAccounts(List<Account> accounts)
    {
        adapter.UpdateAccounts();
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }
}
