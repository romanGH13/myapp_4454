package com.example.eqvol.eqvola.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.example.eqvol.eqvola.Adapters.WithdrawalPaymentsAdapter;
import com.example.eqvol.eqvola.Classes.Api;
import com.example.eqvol.eqvola.Classes.AsyncHttpTask;
import com.example.eqvol.eqvola.Classes.AsyncMethodNames;
import com.example.eqvol.eqvola.Classes.PaymentSystem;
import com.example.eqvol.eqvola.Classes.SpaceItemDecoration;
import com.example.eqvol.eqvola.MainActivity;
import com.example.eqvol.eqvola.R;

import java.util.HashMap;

public class WithdrawalPaymentsFragment extends Fragment {

    private static View mView = null;
    private static RecyclerView list = null;
    public static int currentPaymentId = -1;

    public WithdrawalPaymentsFragment() {
        // Required empty public constructor
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("is_withdrawal", 1);

        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(where);

        HashMap<String, Object> parametrs = new HashMap<String, Object>();
        parametrs.put("token", Api.getToken());
        parametrs.put("where", json);

        AsyncHttpTask ckeckTokenTask = new AsyncHttpTask(parametrs, AsyncMethodNames.GET_WITHDRAWAL_PAYMENTS, getActivity());
        ckeckTokenTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    public static WithdrawalPaymentsFragment newInstance() {
        WithdrawalPaymentsFragment fragment = new WithdrawalPaymentsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_withdrawal_payments, container, false);
        createTable();

        return mView;
    }
    public void createTable() {
        list = (RecyclerView) mView.findViewById(R.id.withdrawal_payments_list);
        list.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mView.getContext());
        llm.setItemPrefetchEnabled(false);
        list.setLayoutManager(llm);
        list.addItemDecoration(new DividerItemDecoration(mView.getContext(), LinearLayout.VERTICAL));
        list.addItemDecoration(new SpaceItemDecoration(10, mView.getContext()));

        WithdrawalPaymentsAdapter adapter = new WithdrawalPaymentsAdapter(mView.getContext());
        list.setAdapter(adapter);
    }

    public static void getImageForPayments()
    {
        for(PaymentSystem paymentSystem: Api.withdrawalPayments)
        {
            if(paymentSystem.getAvatar() == null)
            {
                HashMap<String, Object> parametrs = new HashMap<String, Object>();
                parametrs.put("payment", paymentSystem);
                AsyncHttpTask getUserAvatarTask = new AsyncHttpTask(parametrs, AsyncMethodNames.GET_PAYMENT_IMAGE, null);
                getUserAvatarTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
    }

    public static void imageLoading()
    {
        boolean isAllLoading = true;
        for(PaymentSystem paymentSystem: Api.withdrawalPayments) {
            if(paymentSystem.getAvatar() == null) {
                isAllLoading = false;
                break;
            }
        }
        if(isAllLoading)
        {
            MainActivity.currentLoader.endLoading();
        }
    }

}
