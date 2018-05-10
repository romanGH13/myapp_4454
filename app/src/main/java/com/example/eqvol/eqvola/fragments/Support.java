package com.example.eqvol.eqvola.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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

public class Support extends Fragment {

    public static View mView = null;
    public static ViewPager mViewPager = null;


    public Support()
    {
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("is_active", 1);

        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(where);


        HashMap<String, Object> parametrs = new HashMap<String, Object>();
        parametrs.put("token", Api.getToken());
        parametrs.put("where", json);

        AsyncHttpTask ckeckTokenTask = new AsyncHttpTask(parametrs, AsyncMethodNames.GET_CATEGORIES, getActivity());
        ckeckTokenTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_support, container, false);

        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(SupportCreateTicket.newInstance());
        fragments.add(SupportChat.newInstance());

        mViewPager = (ViewPager) mView.findViewById(R.id.support_pager);
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getChildFragmentManager(), fragments);
        mViewPager.setAdapter(pagerAdapter);

        ((TabLayout)mView.findViewById(R.id.sliding_tabs)).setupWithViewPager(mViewPager);


        HashMap<String, Object> mapUserId = new HashMap<String, Object>();
        mapUserId.put("user_id", Api.user.getId());

        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(mapUserId);

        HashMap<String, Object> parametrs = new HashMap<String, Object>();
        parametrs.put("token", Api.getToken());
        parametrs.put("where", json);

        AsyncHttpTask getTicketsTask = new AsyncHttpTask(parametrs, AsyncMethodNames.GET_TICKETS, getActivity());
        getTicketsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        return mView;
    }




}
