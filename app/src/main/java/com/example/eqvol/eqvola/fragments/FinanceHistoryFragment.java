package com.example.eqvol.eqvola.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eqvol.eqvola.Adapters.MyPagerAdapter;
import com.example.eqvol.eqvola.Classes.Api;
import com.example.eqvol.eqvola.Classes.AsyncHttpTask;
import com.example.eqvol.eqvola.Classes.AsyncMethodNames;
import com.example.eqvol.eqvola.MainActivity;
import com.example.eqvol.eqvola.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FinanceHistoryFragment extends Fragment {

    public static boolean isWithdrawalLoaded;
    public static boolean isDepositsLoaded;

    private View mView;
    private ViewPager mViewPager;

    public FinanceHistoryFragment() {
        isWithdrawalLoaded = false;
        isDepositsLoaded = false;

        Gson gson = new GsonBuilder().create();
        HashMap<String, Object> mapUserId = new HashMap<String, Object>();
        mapUserId.put("user_id", Api.user.getId());

        String json = gson.toJson(mapUserId);

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("token", Api.getToken());
        params.put("where", json);

        AsyncHttpTask getDepositsTask = new AsyncHttpTask(params, AsyncMethodNames.GET_PAYMENTS, getActivity());
        getDepositsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        AsyncHttpTask getWithdrawalsTask = new AsyncHttpTask(params, AsyncMethodNames.GET_WITHDRAWAL, getActivity());
        getWithdrawalsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void withdrawalLoaded(){
        MainActivity.currentLoader.endLoading();
    }


    public static FinanceHistoryFragment newInstance() {
        FinanceHistoryFragment fragment = new FinanceHistoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_finance_history, container, false);

        FragmentManager f1 = ((AppCompatActivity)getContext()).getSupportFragmentManager();

        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(WithdrawalsRecyclerFragment.newInstance());
        fragments.add(DepositsRecyclerFragment.newInstance());

        mViewPager = (ViewPager) mView.findViewById(R.id.finance_history_pager);

        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getChildFragmentManager(), fragments);
        mViewPager.setAdapter(pagerAdapter);

        ((TabLayout)mView.findViewById(R.id.sliding_tabs)).setupWithViewPager(mViewPager);


        return mView;
    }


}
