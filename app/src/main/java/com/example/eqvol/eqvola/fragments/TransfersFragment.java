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

public class TransfersFragment extends Fragment {


    private View mView;
    private ViewPager mViewPager;

    public TransfersFragment() {
        Gson gson = new GsonBuilder().create();
        HashMap<String, Object> mapUserId = new HashMap<String, Object>();
        mapUserId.put("email", Api.user.getEmail());
        String json = gson.toJson(mapUserId);
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("token", Api.getToken());
        params.put("where", json);

        AsyncHttpTask userLoginTask = new AsyncHttpTask(params, AsyncMethodNames.GET_ACCOUNTS, getActivity());
        userLoginTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }


    public static TransfersFragment newInstance() {
        TransfersFragment fragment = new TransfersFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_transfers, container, false);

        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(RequestTransferFragment.newInstance());
        fragments.add(TransfersHistoryFragment.newInstance());

        mViewPager = (ViewPager) mView.findViewById(R.id.transfers_pager);

        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getChildFragmentManager(), fragments);
        mViewPager.setAdapter(pagerAdapter);

        ((TabLayout)mView.findViewById(R.id.sliding_tabs)).setupWithViewPager(mViewPager);

        return mView;
    }


}
