package com.example.eqvol.eqvola.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
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


        if(position%2==0)
            convertView.setBackgroundColor(Color.parseColor("#83EBFB"));
        else
            convertView.setBackgroundColor(Color.WHITE);
        ((TextView) convertView.findViewById(R.id.table_row_account)).setText(Integer.toString(account.getLogin()));
        ((TextView) convertView.findViewById(R.id.table_row_balance)).setText(account.getBalance());

        return convertView;
    }


}