package com.example.eqvol.eqvola.fragments;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDelegate;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eqvol.eqvola.Adapters.RequestTransferAccountAdapter;
import com.example.eqvol.eqvola.Classes.Account;
import com.example.eqvol.eqvola.Classes.Api;
import com.example.eqvol.eqvola.Classes.AsyncHttpTask;
import com.example.eqvol.eqvola.Classes.AsyncMethodNames;
import com.example.eqvol.eqvola.R;

import java.util.HashMap;
import java.util.List;


public class RequestTransferFragment extends Fragment implements TextView.OnEditorActionListener{

    private static View mView = null;
    private static EditText mBalance = null;
    private static EditText mTargetAccount = null;
    private static EditText mAmount = null;
    private static Spinner mAccountsSpinner = null;

    private static TextView mAccountHolderName = null;
    private  static Button btn = null;

    private static boolean isAccountExists;
    private static boolean isRequestClick;

    private Drawable backgroundDrawable;

    public RequestTransferFragment() {
        isAccountExists = false;
        isRequestClick = false;
    }

    public static RequestTransferFragment newInstance() {
        RequestTransferFragment fragment = new RequestTransferFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_request_transfer, container, false);
        mBalance = (EditText) mView.findViewById(R.id.request_transfer_account_balance);
        mTargetAccount = (EditText) mView.findViewById(R.id.request_transfer_target_account);
        mAmount = (EditText) mView.findViewById(R.id.request_transfer_amount);
        mAccountHolderName = (TextView) mView.findViewById(R.id.request_transfer_account_holder_name);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        try {
            backgroundDrawable = ContextCompat.getDrawable(mView.getContext(), R.drawable.my_text_border);
        }
        catch(Exception ex)
        {
            Toast toast = Toast.makeText(mView.getContext(),
                    ex.toString(), Toast.LENGTH_SHORT);
            toast.show();
        }

        ((LinearLayout)mView.findViewById(R.id.linearSpinGroup)).setBackground(backgroundDrawable);
        mBalance.setBackground(backgroundDrawable);
        mTargetAccount.setBackground(backgroundDrawable);
        mAmount.setBackground(backgroundDrawable);
        mAccountHolderName.setBackground(backgroundDrawable);

        mAmount.setOnEditorActionListener(this);
        mTargetAccount.setOnEditorActionListener(this);

        TextWatcher myTextWatcher = new TextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                isAccountExists = false;
                isRequestClick = false;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };

        mTargetAccount.addTextChangedListener(myTextWatcher);
        mAmount.addTextChangedListener(myTextWatcher);

        mTargetAccount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                try {
                    EditText mTargetView = (EditText) v;
                    if (!hasFocus && mTargetView.getText().length() > 0) {
                        getAccountHolderName();
                    }
                } catch (Exception ex)
                {

                }
            }
        });

        mTargetAccount.setImeActionLabel("Next", EditorInfo.IME_ACTION_NEXT);
        mTargetAccount.setNextFocusForwardId(R.id.request_transfer_amount);
        mAmount.setImeActionLabel("Done", EditorInfo.IME_ACTION_SEND);

        btn = (Button) mView.findViewById(R.id.btnSubmit);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if (mTargetAccount.getText().length() == 0) {
                        mTargetAccount.setError("Recipient account can not be empty");
                        return;
                    }
                    if (mAmount.getText().length() == 0) {
                        mAmount.setError("Amount can not be empty");
                        return;
                    }

                    isRequestClick = true;

                    if (onEditorAction(mAmount, 0, null)) {
                        if (!onEditorAction(mTargetAccount, 0, null)) {
                            getAccountHolderName();
                        }
                    }
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

    private static void requestTransfer(String sender, String recipient, double amount)
    {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("token", Api.getToken());
        params.put("sender", sender);
        params.put("recipient", recipient);
        params.put("amount", amount);

        AsyncHttpTask requestTransferTask = new AsyncHttpTask(params, AsyncMethodNames.REQUEST_TRANSFER, (Activity) mView.getContext());
        requestTransferTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        Account account = (Account)mAccountsSpinner.getSelectedItem();
        double accountBalance = Double.parseDouble(account.getBalance());
        mTargetAccount.setText("");
        mAccountHolderName.setText("");
        mAmount.setText("");
        account.setBalance(Double.toString(accountBalance - amount));
        RequestTransferAccountAdapter adapter = (RequestTransferAccountAdapter)mAccountsSpinner.getAdapter();
        adapter.notifyDataSetChanged();
        mBalance.setText(account.getBalance());
    }

    private void setSpinner()
    {
        List<Account> accounts = Api.user.accounts;
        mAccountsSpinner = (Spinner)mView.findViewById(R.id.request_transfer_spinner_accounts);
        try {
            RequestTransferAccountAdapter adapter = new RequestTransferAccountAdapter(mView.getContext(), accounts);
            adapter.setDropDownViewResource(R.layout.drop_down_item);
            mAccountsSpinner.setAdapter(adapter);
            mAccountsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int pos, long id) {
                    Account account = (Account) parent.getItemAtPosition(pos);
                    mBalance.setText(account.getBalance());
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

    private void getAccountHolderName()
    {
        try {
            String accountLogin = mTargetAccount.getText().toString();

            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("token", Api.getToken());
            params.put("login", accountLogin);

            AsyncHttpTask getAccountHolderNameTask = new AsyncHttpTask(params, AsyncMethodNames.GET_ACCOUNT_HOLDER_NAME, getActivity());
            getAccountHolderNameTask.target = accountLogin;
            getAccountHolderNameTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch(Exception ex)
        {

        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v.getId() == R.id.request_transfer_target_account){
            if(mTargetAccount.getText().length() > 0) {
                getAccountHolderName();
                mAmount.requestFocus();
            }
            return true;

        }
        else if (v.getId() == R.id.request_transfer_amount){
            if(mAmount.getText().length() == 0)
                return false;
            double amount = Double.parseDouble(mAmount.getText().toString());

            if(amount<0)
            {
                mAmount.setError("Amount can not be negative");
                mAmount.requestFocus();
                return false;
            }

            if(amount <= Double.parseDouble(mBalance.getText().toString()))
            {
                btn.requestFocus();
                return true;
            }
            else
            {
                mAmount.setError("Not enough money on the account");
                mAmount.requestFocus();
                return false;
            }
        }
        return false;
    }

    public static void setHolderName(String holderName, String login)
    {
        try {
            if (mAccountHolderName != null) {
                if (mTargetAccount != null) {
                    String currentTargetAccount = mTargetAccount.getText().toString();
                    if (currentTargetAccount.contentEquals(login)) {
                        mAccountHolderName.setText(holderName);
                        mAccountHolderName.setError(null);
                        isAccountExists = true;
                        if (isRequestClick) {
                            if (mAccountsSpinner != null && mAmount != null && mTargetAccount != null) {
                                double amount = Double.parseDouble(mAmount.getText().toString());
                                Account account = (Account) mAccountsSpinner.getSelectedItem();
                                String recipient = mTargetAccount.getText().toString();
                                String sender = Integer.toString(account.getLogin());
                                requestTransfer(sender, recipient, amount);
                            }
                        }
                    }
                }
            }
        } catch(Exception ex)
        {

        }
    }
    public static void setError(String login)
    {
        try {
            if (mAccountHolderName != null) {
                if (mTargetAccount != null) {
                    if (mTargetAccount.getText().toString().contentEquals(login)) {
                        mAccountHolderName.setTextColor(Color.BLACK);
                        mAccountHolderName.setText("Account does not exist");
                        mAccountHolderName.setError("");
                        isAccountExists = false;
                    }
                }
            }
        } catch(Exception ex)
        {

        }
    }
}
