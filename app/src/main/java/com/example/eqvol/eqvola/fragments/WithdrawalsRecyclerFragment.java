package com.example.eqvol.eqvola.fragments;

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

import com.example.eqvol.eqvola.Adapters.WithdrawalsAdapter;
import com.example.eqvol.eqvola.Classes.Api;
import com.example.eqvol.eqvola.Classes.AsyncHttpTask;
import com.example.eqvol.eqvola.Classes.AsyncMethodNames;
import com.example.eqvol.eqvola.Classes.SpaceItemDecoration;
import com.example.eqvol.eqvola.Classes.Withdrawal;
import com.example.eqvol.eqvola.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.List;


public class WithdrawalsRecyclerFragment extends Fragment {

    private View mView;
    private static SwipeRefreshLayout swipeRefreshLayout;
    private static WithdrawalsAdapter adapter;

    public WithdrawalsRecyclerFragment() {
        // Required empty public constructor
    }


    public static WithdrawalsRecyclerFragment newInstance() {
        WithdrawalsRecyclerFragment fragment = new WithdrawalsRecyclerFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_withdrawals_recycler, container, false);
        RecyclerView list = (RecyclerView)mView.findViewById(R.id.withdrawals_list);
        swipeRefreshLayout = (SwipeRefreshLayout)mView.findViewById(R.id.withdrawals_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Gson gson = new GsonBuilder().create();
                HashMap<String, Object> mapUserId = new HashMap<String, Object>();
                mapUserId.put("user_id", Api.user.getId());

                String json = gson.toJson(mapUserId);

                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("token", Api.getToken());
                params.put("where", json);

                AsyncHttpTask getWithdrawalsTask = new AsyncHttpTask(params, AsyncMethodNames.GET_WITHDRAWAL, getActivity());
                getWithdrawalsTask.target = "update";
                getWithdrawalsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            }
        });

        list.addItemDecoration(new SpaceItemDecoration(10, getContext()));

        setList();
        return mView;
    }

    private void setList()
    {
        List<Withdrawal> withdrawals = Api.withdrawals;

        try {
            RecyclerView mRecyclerView = (RecyclerView) mView.findViewById(R.id.withdrawals_list);
            mRecyclerView.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(getContext());
            llm.setItemPrefetchEnabled(false);
            mRecyclerView.setLayoutManager(llm);
            mRecyclerView.addItemDecoration(new DividerItemDecoration(this.getActivity(), LinearLayout.VERTICAL));

            adapter = new WithdrawalsAdapter(mView.getContext(), withdrawals);
            mRecyclerView.setAdapter(adapter);
        } catch(Exception ex){
            String str = ex.getMessage();
        }
    }

    public static void updateWithdrawals(List<Withdrawal> withdrawals)
    {
        adapter.UpdateData(withdrawals);
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

}
