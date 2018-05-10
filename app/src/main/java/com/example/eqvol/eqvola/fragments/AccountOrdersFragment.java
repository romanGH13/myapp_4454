package com.example.eqvol.eqvola.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eqvol.eqvola.Adapters.MyPagerAdapter;
import com.example.eqvol.eqvola.Classes.Api;
import com.example.eqvol.eqvola.MainActivity;
import com.example.eqvol.eqvola.R;

import java.util.ArrayList;
import java.util.List;


public class AccountOrdersFragment extends Fragment {

    public static View mView = null;
    public static ViewPager mViewPager = null;

    private int login;
    public AccountOrdersFragment() {

    }


    // TODO: Rename and change types and number of parameters
    public static AccountOrdersFragment newInstance() {
        AccountOrdersFragment fragment = new AccountOrdersFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_account_orders, container, false);

        FragmentManager f1 = ((AppCompatActivity)getContext()).getSupportFragmentManager();

        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(OpenOrdersForAccountFragment.newInstance());
        fragments.add(TradingHistoryFragment.newInstance());

        mViewPager = (ViewPager) mView.findViewById(R.id.account_orders_pager);

        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getChildFragmentManager(), fragments);
        mViewPager.setAdapter(pagerAdapter);

        ((TabLayout)mView.findViewById(R.id.sliding_tabs)).setupWithViewPager(mViewPager);

        try {
            login = Api.account.getLogin();
            TextView mAccountId = (TextView) mView.findViewById(R.id.login);
            mAccountId.setText("Account " + login);
        }
        catch(Exception ex) { }

        LinearLayout header = (LinearLayout)mView.findViewById(R.id.header);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity main = (MainActivity)getContext();
                main.openMyAccounts();
            }
        });

        return mView;
    }


}
