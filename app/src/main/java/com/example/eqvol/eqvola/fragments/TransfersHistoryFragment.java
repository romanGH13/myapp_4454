package com.example.eqvol.eqvola.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.eqvol.eqvola.Adapters.OpenOrdersAdapter;
import com.example.eqvol.eqvola.Adapters.TransferHistoryAdapter;
import com.example.eqvol.eqvola.Classes.Api;
import com.example.eqvol.eqvola.Classes.AsyncHttpTask;
import com.example.eqvol.eqvola.Classes.AsyncMethodNames;
import com.example.eqvol.eqvola.Classes.SpaceItemDecoration;
import com.example.eqvol.eqvola.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

public class TransfersHistoryFragment extends Fragment {

    private static  View mView;
    private static RecyclerView list;
    private static ProgressBar progressBar;
    private static EditText mSearchView;
    private static TransferHistoryAdapter adapter;
    private static SwipeRefreshLayout swipeRefreshLayout;


    static float x;
    static float y;
    static String sDown;
    static String sMove;
    static String sUp;


    public TransfersHistoryFragment() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("token", Api.getToken());

        AsyncHttpTask getAllTransfersTask = new AsyncHttpTask(params, AsyncMethodNames.GET_TRANSFERS, getActivity());
        getAllTransfersTask.target = TransfersHistoryFragment.class.toString();
        getAllTransfersTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    public static TransfersHistoryFragment newInstance() {
        TransfersHistoryFragment fragment = new TransfersHistoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_transfers_history, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout)mView.findViewById(R.id.transfer_history_swipe_refresh);
        progressBar = (ProgressBar) mView.findViewById(R.id.login_progress);
        mSearchView = (EditText) mView.findViewById(R.id.search);
        return mView;
    }


    public static void setList() {
        try {
            list = (RecyclerView) mView.findViewById(R.id.transfer_history_list);
            list.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(mView.getContext());
            llm.setItemPrefetchEnabled(false);
            list.setLayoutManager(llm);
            list.addItemDecoration(new DividerItemDecoration(mView.getContext(), LinearLayout.VERTICAL));
            list.addItemDecoration(new SpaceItemDecoration(10, mView.getContext()));

            adapter = new TransferHistoryAdapter(mView.getContext(), mSearchView);
            list.setAdapter(adapter);

            progressBar.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);

            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    HashMap<String, Object> params = new HashMap<String, Object>();
                    params.put("token", Api.getToken());

                    AsyncHttpTask getAllTransfersTask = new AsyncHttpTask(params, AsyncMethodNames.GET_TRANSFERS, (Activity) mView.getContext());
                    getAllTransfersTask.target = "update";
                    getAllTransfersTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                }
            });

            mSearchView = (EditText) mView.findViewById(R.id.search);
            mSearchView.setVisibility(View.VISIBLE);
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
        } catch (Exception ex) {
            String str = ex.getMessage();
        }
    }

    public static void updateList() {
        adapter.UpdateTransfers();
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

}
