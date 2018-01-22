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

import com.example.eqvol.eqvola.Adapters.CloseOrdersAdapter;
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


public class TradingHistoryFragment extends Fragment {

    private View mView;
    private static SwipeRefreshLayout swipeRefreshLayout;
    private static CloseOrdersAdapter adapter;

    public TradingHistoryFragment() {
        // Required empty public constructor
    }


    public static TradingHistoryFragment newInstance() {
        TradingHistoryFragment fragment = new TradingHistoryFragment();
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
        swipeRefreshLayout = (SwipeRefreshLayout)mView.findViewById(R.id.open_orders_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("Login", Api.account.getLogin());
                //where.put("CloseTime", "1970-01-01 00:00:00");

                Gson gson = new GsonBuilder().create();

                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("token", Api.getToken());
                params.put("where", gson.toJson(where));


                AsyncHttpTask getOrdersTask = new AsyncHttpTask(params, AsyncMethodNames.GET_ACCOUNT_ORDERS, (Activity) mView.getContext());
                getOrdersTask.target = TransfersHistoryFragment.class.toString();
                getOrdersTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            }
        });
        setList();
        return mView;
    }

    private void setList()
    {
        try {
            RecyclerView mRecyclerView = (RecyclerView) mView.findViewById(R.id.open_orders_list);
            mRecyclerView.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(llm);
            mRecyclerView.addItemDecoration(new DividerItemDecoration(this.getActivity(), LinearLayout.VERTICAL));
            mRecyclerView.addItemDecoration(new SpaceItemDecoration(10, getContext()));

            adapter = new CloseOrdersAdapter(mView.getContext());
            mRecyclerView.setAdapter(adapter);
        } catch(Exception ex){
            String str = ex.getMessage();
        }
    }
    public static void updateList(List<Order> orders)
    {
        adapter.updateOrders(orders);
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }
}
