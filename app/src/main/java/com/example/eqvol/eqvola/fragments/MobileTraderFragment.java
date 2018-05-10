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
import com.example.eqvol.eqvola.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MobileTraderFragment extends Fragment {

    private View mView;
    private ViewPager mViewPager;

    public static boolean isOrderLoaded;
    public static boolean isAccountsLoaded;

    public MobileTraderFragment() {

        isOrderLoaded = false;
        isAccountsLoaded = false;

        Gson gson = new GsonBuilder().create();
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("CloseTime", "1970-01-01 00:00:00");

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("token", Api.getToken());
        params.put("self", true);
        params.put("where", gson.toJson(where));


        AsyncHttpTask getDepositsTask = new AsyncHttpTask(params, AsyncMethodNames.GET_ACCOUNT_ORDERS, getActivity());
        getDepositsTask.target = MobileTraderFragment.class.toString();
        getDepositsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


        HashMap<String, Object> mapUserId = new HashMap<String, Object>();
        mapUserId.put("email", Api.user.getEmail());
        String json = gson.toJson(mapUserId);
        HashMap<String, Object> parametr = new HashMap<String, Object>();
        parametr.put("token", Api.getToken());
        parametr.put("where", json);

        AsyncHttpTask userLoginTask = new AsyncHttpTask(parametr, AsyncMethodNames.GET_ACCOUNTS, getActivity());
        userLoginTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    public static MobileTraderFragment newInstance() {
        MobileTraderFragment fragment = new MobileTraderFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_mobile_trader, container, false);

        FragmentManager f1 = ((AppCompatActivity)getContext()).getSupportFragmentManager();

        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(OpenOrdersFragment.newInstance());
        fragments.add(QuotationsFragment.newInstance());
        fragments.add(CreateOrderFragment.newInstance());

        mViewPager = (ViewPager) mView.findViewById(R.id.mobile_trader_pager);

        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getChildFragmentManager(), fragments);
        mViewPager.setAdapter(pagerAdapter);

        ((TabLayout)mView.findViewById(R.id.sliding_tabs)).setupWithViewPager(mViewPager);

        return mView;
    }

}
