package com.example.eqvol.eqvola.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.example.eqvol.eqvola.Classes.Order;
import com.example.eqvol.eqvola.Classes.SpaceItemDecoration;
import com.example.eqvol.eqvola.R;

import java.util.ArrayList;
import java.util.List;


public class TradingHistoryFragment extends Fragment {

    private View mView;
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

            CloseOrdersAdapter adapter = new CloseOrdersAdapter(mView.getContext());
            mRecyclerView.setAdapter(adapter);
        } catch(Exception ex){
            String str = ex.getMessage();
        }
    }
}
