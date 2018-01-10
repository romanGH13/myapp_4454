package com.example.eqvol.eqvola.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.eqvol.eqvola.Classes.Account;
import com.example.eqvol.eqvola.Classes.Group;
import com.example.eqvol.eqvola.R;

import java.util.List;

/**
 * Created by eqvol on 25.10.2017.
 */

public class RequestTransferAccountAdapter extends ArrayAdapter<Account> {
    public RequestTransferAccountAdapter(@NonNull Context context, List<Account> accounts) {
        super(context, R.layout.spinner_group_item, accounts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Account account = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.spinner_group_item, null);
        }
        TextView tv = ((TextView) convertView.findViewById(R.id.item));
        int login = account.getLogin();
        tv.setText(Integer.toString(login));
        return convertView;
    }
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        Account account = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.drop_down_item, null);
        }
        TextView tv = ((TextView) convertView.findViewById(R.id.drop_down_item));

        int login = account.getLogin();
        tv.setText(Integer.toString(login));
        return convertView;
    }
}
