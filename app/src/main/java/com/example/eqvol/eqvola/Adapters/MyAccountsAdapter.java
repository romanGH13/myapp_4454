package com.example.eqvol.eqvola.Adapters;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.eqvol.eqvola.Classes.Account;
import com.example.eqvol.eqvola.Classes.Api;
import com.example.eqvol.eqvola.Classes.AsyncHttpTask;
import com.example.eqvol.eqvola.Classes.AsyncMethodNames;
import com.example.eqvol.eqvola.Classes.FragmentLoader;
import com.example.eqvol.eqvola.MainActivity;
import com.example.eqvol.eqvola.R;
import com.example.eqvol.eqvola.fragments.AccountOrdersFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by eqvol on 27.12.2017.
 */

public class MyAccountsAdapter extends RecyclerView.Adapter<MyAccountsAdapter.MyAccountsViewHolder> implements Comparator<Account> {

    Context ctx;
    LayoutInflater lInflater;
    public EditText mSearchStringView;
    private List<Account> accounts;

    private MyAccountsAdapter.OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnCLickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public MyAccountsAdapter(@NonNull Context context, EditText mSearchStringView) {
        this.ctx = context;
        this.accounts = Api.user.accounts;
        Collections.sort(accounts, this);
        this.lInflater = LayoutInflater.from(this.ctx);
        this.mSearchStringView = mSearchStringView;
    }


    public static class MyAccountsViewHolder extends RecyclerView.ViewHolder {
        TextView accountView;
        TextView balanceView;
        TextView accountTypeView;
        TextView leverageView;
        TextView registrationDateView;

        TextView labelBalanceView;
        TextView labelAccountTypeView;
        TextView labelLeverageView;
        TextView labelRegistrationDateView;

        ConstraintLayout layoutDetail;

        MyAccountsViewHolder(View itemView) {
            super(itemView);
            //container = itemView;
            accountView = (TextView) itemView.findViewById(R.id.account);
            balanceView = (TextView) itemView.findViewById(R.id.balance);
            accountTypeView = (TextView) itemView.findViewById(R.id.accountType);
            leverageView = (TextView) itemView.findViewById(R.id.leverage);
            registrationDateView = (TextView) itemView.findViewById(R.id.registrationDate);

            labelBalanceView = (TextView) itemView.findViewById(R.id.labelBalance);
            labelAccountTypeView = (TextView) itemView.findViewById(R.id.labelAccountType);
            labelLeverageView = (TextView) itemView.findViewById(R.id.labelLeverage);
            labelRegistrationDateView = (TextView) itemView.findViewById(R.id.labelRegistrationDate);

            layoutDetail = (ConstraintLayout) itemView.findViewById(R.id.layoutAccount);
        }

    }

    @Override
    public MyAccountsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_row, parent, false);
        MyAccountsViewHolder pvh = new MyAccountsViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final MyAccountsViewHolder holder, final int position) {

        final Account account = accounts.get(position);

        if(account.getLogin() != 0) {
            holder.layoutDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = ((MainActivity) holder.itemView.getContext()).getSupportFragmentManager();

                    FragmentLoader fl = new FragmentLoader(AccountOrdersFragment.class, fragmentManager, R.id.container, false);
                    fl.startLoading();
                    MainActivity.currentLoader = fl;

                    HashMap<String, Object> where = new HashMap<String, Object>();
                    where.put("Login", account.getLogin());

                    Gson gson = new GsonBuilder().create();

                    HashMap<String, Object> params = new HashMap<String, Object>();
                    params.put("token", Api.getToken());
                    params.put("where", gson.toJson(where));

                    Api.account = account;

                    AsyncHttpTask getOrdersTask = new AsyncHttpTask(params, AsyncMethodNames.GET_ACCOUNT_ORDERS, (Activity) holder.itemView.getContext());
                    getOrdersTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            });
        }


        if(account.getLogin() == 0) {
            holder.accountView.setText("Local wallet");
            holder.balanceView.setText(account.getBalance());
            holder.accountTypeView.setVisibility(View.GONE);
            holder.leverageView.setVisibility(View.GONE);
            holder.registrationDateView.setVisibility(View.GONE);

            holder.labelAccountTypeView.setVisibility(View.GONE);
            holder.labelLeverageView.setVisibility(View.GONE);
            holder.labelRegistrationDateView.setVisibility(View.GONE);
        }
        else if(account.getLogin() != 0) {
            holder.accountView.setText("Account " + Integer.toString(account.getLogin()));
            holder.balanceView.setText(account.getBalance());
            holder.accountTypeView.setText(account.getGroup().getName());
            holder.leverageView.setText("1:" + Integer.toString(account.getLeverage()));
            holder.registrationDateView.setText(account.getRegisterDate());

            holder.accountTypeView.setVisibility(View.VISIBLE);
            holder.leverageView.setVisibility(View.VISIBLE);
            holder.registrationDateView.setVisibility(View.VISIBLE);
            holder.labelAccountTypeView.setVisibility(View.VISIBLE);
            holder.labelLeverageView.setVisibility(View.VISIBLE);
            holder.labelRegistrationDateView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public long getItemId(int position) {
        return accounts.get(position).getLogin();
    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    @Override
    public int compare(Account t1, Account t2) {
        if(t1.getRegisterDate() == null)
            return -100;
        if(t2.getRegisterDate() == null)
            return 100;

        DateFormat format = new SimpleDateFormat(ctx.getString(R.string.date_format));
        try {
            Date date1 = format.parse(t1.getRegisterDate());
            Date date2 = format.parse(t2.getRegisterDate());
            return date1.compareTo(date2) * (-1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void searchStringChanged()
    {
        accounts = new ArrayList<Account>();

        for(Account a: Api.user.accounts)
        {
            if(Integer.toString(a.getLogin()).startsWith(mSearchStringView.getText().toString()))
            {
                accounts.add(a);
            }
        }
        Collections.sort(accounts, this);
    }

    public void UpdateTransferById(Account account) {
        for (Account a : accounts) {
            if (a.getLogin() == account.getLogin())
                a = account;
        }
        Collections.sort(accounts, this);
    }

    public void UpdateAccounts()
    {
        accounts = Api.user.accounts;
        searchStringChanged();
        Collections.sort(accounts, this);
    }



}
