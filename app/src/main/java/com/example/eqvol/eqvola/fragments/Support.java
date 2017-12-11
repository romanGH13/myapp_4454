package com.example.eqvol.eqvola.fragments;

import android.app.Activity;
import android.content.Context;
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

import com.example.eqvol.eqvola.Adapters.SupportFragmentPagerAdapter;
import com.example.eqvol.eqvola.Classes.Api;
import com.example.eqvol.eqvola.Classes.AsyncHttpTask;
import com.example.eqvol.eqvola.Classes.AsyncMethodNames;
import com.example.eqvol.eqvola.Classes.FragmentLoader;
import com.example.eqvol.eqvola.Classes.Ticket;
import com.example.eqvol.eqvola.Classes.User;
import com.example.eqvol.eqvola.MenuActivity;
import com.example.eqvol.eqvola.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.eqvol.eqvola.R.id.container;


public class Support extends Fragment {

    public static View mView = null;
    public static ViewPager mViewPager = null;


    public Support()
    {
        //users = new ArrayList<User>();

        HashMap<String, Object> parametrs2 = new HashMap<String, Object>();
        parametrs2.put("token", Api.getToken());
        AsyncHttpTask ckeckTokenTask = new AsyncHttpTask(parametrs2, AsyncMethodNames.GET_CATEGORIES, getActivity());
        ckeckTokenTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        //mView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_support, container, false);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_support, container, false);

        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(SupportCreateTicket.newInstance());
        FragmentManager f1 = ((AppCompatActivity)getContext()).getSupportFragmentManager();
        Api.chatLoader = new FragmentLoader(SupportChat.class, f1, R.id.progress_bar, false);
        fragments.add(Api.chatLoader.getProgressBar());

        mViewPager = (ViewPager) mView.findViewById(R.id.support_pager);
        SupportFragmentPagerAdapter pagerAdapter = new SupportFragmentPagerAdapter(getChildFragmentManager(), fragments);
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
