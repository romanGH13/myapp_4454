package com.example.eqvol.eqvola.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.eqvol.eqvola.Adapters.OpenOrdersAdapter;
import com.example.eqvol.eqvola.Classes.Api;
import com.example.eqvol.eqvola.Classes.AsyncHttpTask;
import com.example.eqvol.eqvola.Classes.AsyncMethodNames;
import com.example.eqvol.eqvola.Classes.Order;
import com.example.eqvol.eqvola.Classes.SpaceItemDecoration;
import com.example.eqvol.eqvola.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class OpenOrdersFragment extends Fragment {

    public static boolean isNeedUpdate;
    private static View mView;
    private static RecyclerView list;
    public static int accountId;
    private static SwipeRefreshLayout swipeRefreshLayout;

    private HashMap<String, Object> params;

    public OpenOrdersFragment() {
    }

    public static OpenOrdersFragment newInstance() {
        OpenOrdersFragment fragment = new OpenOrdersFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_open_orders, container, false);
        list = (RecyclerView) mView.findViewById(R.id.open_orders_list);

        list.addItemDecoration(new SpaceItemDecoration(10, getContext()));
        isNeedUpdate = true;

        setList();

        accountId = Api.account.getLogin();

        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("Login", accountId);
        where.put("CloseTime", "1970-01-01 00:00:00");

        Gson gson = new GsonBuilder().create();

        params = new HashMap<String, Object>();
        params.put("token", Api.getToken());
        params.put("where", gson.toJson(where));

        AsyncHttpTask getOrdersTask = new AsyncHttpTask(params, AsyncMethodNames.UPDATE_ACCOUNT_ORDERS, (Activity) mView.getContext());
        getOrdersTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        swipeRefreshLayout = (SwipeRefreshLayout)mView.findViewById(R.id.open_orders_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("Login", accountId);
                where.put("CloseTime", "1970-01-01 00:00:00");

                Gson gson = new GsonBuilder().create();

                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("token", Api.getToken());
                params.put("where", gson.toJson(where));


                AsyncHttpTask getOrdersTask = new AsyncHttpTask(params, AsyncMethodNames.GET_ACCOUNT_ORDERS, (Activity) mView.getContext());
                getOrdersTask.target = OpenOrdersFragment.class.toString();
                getOrdersTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            }
        });

        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isNeedUpdate = false;
    }

    private void setList() {
        try {
            list = (RecyclerView) mView.findViewById(R.id.open_orders_list);
            list.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(getContext());
            llm.setItemPrefetchEnabled(false);
            list.setLayoutManager(llm);
            list.addItemDecoration(new DividerItemDecoration(this.getActivity(), LinearLayout.VERTICAL));

            OpenOrdersAdapter adapter = new OpenOrdersAdapter(mView.getContext());
            list.setAdapter(adapter);
        } catch (Exception ex) {
            String str = ex.getMessage();
        }
    }


    public static void updateOrders(List<Order> newOrders) {
        OpenOrdersAdapter adapter = (OpenOrdersAdapter) list.getAdapter();
        synchronized(adapter) {
            adapter.UpdateOrders(newOrders);
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

}
