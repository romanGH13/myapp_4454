package com.example.eqvol.eqvola.fragments;


import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.eqvol.eqvola.Adapters.RequestTransferAccountAdapter;
import com.example.eqvol.eqvola.Classes.Account;
import com.example.eqvol.eqvola.Classes.Api;
import com.example.eqvol.eqvola.Classes.AsyncHttpTask;
import com.example.eqvol.eqvola.Classes.AsyncMethodNames;
import com.example.eqvol.eqvola.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.List;


public class RequestWithdrawalFragment extends Fragment {

    private View mView = null;
    private Spinner mAccountsSpinner;
    private EditText mBalance;

    public RequestWithdrawalFragment() {
        Gson gson = new GsonBuilder().create();
        HashMap<String, Object> mapUserId = new HashMap<String, Object>();
        mapUserId.put("email", Api.user.getEmail());
        String json = gson.toJson(mapUserId);
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("token", Api.getToken());
        params.put("where", json);

        AsyncHttpTask getUserAccounts = new AsyncHttpTask(params, AsyncMethodNames.GET_ACCOUNTS, getActivity());
        getUserAccounts.target = "get accounts for withdrawal";
        getUserAccounts.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    public static RequestWithdrawalFragment newInstance() {
        RequestWithdrawalFragment fragment = new RequestWithdrawalFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_request_withdrawal, container, false);

        Button btn = (Button) mView.findViewById(R.id.btnSubmit);

        mAccountsSpinner = (Spinner) mView.findViewById(R.id.spinner_accounts) ;
        mBalance = (EditText) mView.findViewById(R.id.request_withdrawal_balance) ;

        final EditText mAmount = (EditText) mView.findViewById(R.id.request_withdrawal_amount);
        final EditText mPurseNumber = (EditText) mView.findViewById(R.id.request_withdrawal_purse_number);
        EditText mCurrency = (EditText) mView.findViewById(R.id.request_withdrawal_currency);
        String currency = "USD";

        mCurrency.setText(currency);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Account account = (Account)mAccountsSpinner.getSelectedItem();
                    if (mAmount.getText().length() == 0) {
                        mAmount.setError("Amount can not be empty");
                        return;
                    }
                    if (mPurseNumber.getText().length() == 0) {
                        mPurseNumber.setError("Purse number can not be empty");
                        return;
                    }
                    double accountBalance = Double.parseDouble(account.getBalance());
                    double amount = Double.parseDouble(mAmount.getText().toString());
                    if (amount > accountBalance) {
                        mAmount.setError("Not enough money on the account");
                        return;
                    }


                    HashMap<String, Object> params = new HashMap<String, Object>();
                    params.put("token", Api.getToken());
                    params.put("amount", amount);
                    params.put("login", account.getLogin());
                    params.put("payment_setting_id", WithdrawalPaymentsFragment.currentPaymentId);
                    params.put("wallet", mPurseNumber.getText().toString());

                    AsyncHttpTask getUserAccounts = new AsyncHttpTask(params, AsyncMethodNames.REQUEST_WITHDRAWAL, getActivity());
                    getUserAccounts.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                }catch(Exception ex)
                {

                }
            }
        });

        Drawable drawable = btn.getBackground();
        drawable.setColorFilter(getResources().getColor(R.color.colorNext), PorterDuff.Mode.MULTIPLY);
        setSpinner();
        return mView;
    }

    private void setSpinner()
    {
        List<Account> accounts = Api.user.accounts;
        for(Account a: accounts)
        {
            if(a.getLogin() == 0)
            {
                accounts.remove(a);
                break;
            }
        }
        mAccountsSpinner = (Spinner)mView.findViewById(R.id.spinner_accounts);
        try {
            RequestTransferAccountAdapter adapter = new RequestTransferAccountAdapter(mView.getContext(), accounts);
            adapter.setDropDownViewResource(R.layout.drop_down_item);
            mAccountsSpinner.setAdapter(adapter);
            mAccountsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int pos, long id) {
                    Account account = (Account) parent.getItemAtPosition(pos);
                    mBalance.setText(account.getBalance()+"$");
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {

                }
            });
        } catch(Exception ex)
        {
            String str = ex.getMessage();
        }
    }
}
