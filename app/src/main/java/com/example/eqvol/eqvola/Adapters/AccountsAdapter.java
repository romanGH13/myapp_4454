package com.example.eqvol.eqvola.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.eqvol.eqvola.Classes.Account;
import com.example.eqvol.eqvola.R;

import java.util.List;

/**
 * Created by eqvol on 26.10.2017.
 */

public class AccountsAdapter extends ArrayAdapter<Account>
{
    private int lastItem = 0;
    public AccountsAdapter(@NonNull Context context, List<Account> accounts) {
        super(context, R.layout.table_row, accounts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Account account = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.table_row, null);
        }
        TextView accountView = (TextView) convertView.findViewById(R.id.account);
        TextView balanceView = (TextView) convertView.findViewById(R.id.balance);
        TextView accountTypeView = (TextView) convertView.findViewById(R.id.accountType);
        TextView leverageView = (TextView) convertView.findViewById(R.id.leverage);
        TextView registrationDateView = (TextView) convertView.findViewById(R.id.registrationDate);

        TextView labelBalanceView = (TextView) convertView.findViewById(R.id.labelBalance);
        TextView labelAccountTypeView = (TextView) convertView.findViewById(R.id.labelAccountType);
        TextView labelLeverageView = (TextView) convertView.findViewById(R.id.labelLeverage);
        TextView labelRegistrationDateView = (TextView) convertView.findViewById(R.id.labelRegistrationDate);

        /*if(position%2==0) {
            convertView.setBackgroundColor(convertView.getResources().getColor(R.color.colorMenuBackground));
            accountView.setTextAppearance(getContext(), R.style.ThemeAccountDetailLight);
            balanceView.setTextAppearance(getContext(), R.style.ThemeAccountDetailLight);
            accountTypeView.setTextAppearance(getContext(), R.style.ThemeAccountDetailLight);
            leverageView.setTextAppearance(getContext(), R.style.ThemeAccountDetailLight);
            registrationDateView.setTextAppearance(getContext(), R.style.ThemeAccountDetailLight);

            labelAccountTypeView.setTextAppearance(getContext(), R.style.ThemeAccountDetailLight);
            labelBalanceView.setTextAppearance(getContext(), R.style.ThemeAccountDetailLight);
            labelLeverageView.setTextAppearance(getContext(), R.style.ThemeAccountDetailLight);
            labelRegistrationDateView.setTextAppearance(getContext(), R.style.ThemeAccountDetailLight);

        }
        else {*/
            convertView.setBackgroundColor(Color.WHITE);
            accountView.setTextAppearance(getContext(), R.style.ThemeAccountDetailDark);
            balanceView.setTextAppearance(getContext(), R.style.ThemeAccountDetailGreen);
            accountTypeView.setTextAppearance(getContext(), R.style.ThemeAccountDetailBlue);
            leverageView.setTextAppearance(getContext(), R.style.ThemeAccountDetailOrange);
            registrationDateView.setTextAppearance(getContext(), R.style.ThemeAccountDetailDark);

            labelAccountTypeView.setTextAppearance(getContext(), R.style.ThemeAccountDetailDark);
            labelBalanceView.setTextAppearance(getContext(), R.style.ThemeAccountDetailDark);
            labelLeverageView.setTextAppearance(getContext(), R.style.ThemeAccountDetailDark);
            labelRegistrationDateView.setTextAppearance(getContext(), R.style.ThemeAccountDetailDark);
        //}

        accountView.setText("Account " + Integer.toString(account.getLogin()));
        balanceView.setText(account.getBalance());
        accountTypeView.setText(account.getGroup().getName());
        leverageView.setText("1:" + Integer.toString(account.getLeverage()));
        registrationDateView.setText(account.getRegisterDate());

        if(account.isDetailOpen())
        {
            ((ConstraintLayout)convertView.findViewById(R.id.layoutDetail)).setVisibility(View.VISIBLE);
        }
        else
        {
            ((ConstraintLayout)convertView.findViewById(R.id.layoutDetail)).setVisibility(View.GONE);
        }
        return convertView;
    }




}