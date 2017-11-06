package com.example.eqvol.eqvola.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.eqvol.eqvola.Classes.Api;
import com.example.eqvol.eqvola.Classes.Country;
import com.example.eqvol.eqvola.R;

/**
 * Created by eqvol on 23.10.2017.
 */

public class CountryAdapter extends ArrayAdapter<Country>
{

    public CountryAdapter(@NonNull Context context) {
        super(context, R.layout.spinner_item, Api.countries);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Country country = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.spinner_item, null);
        }
        TextView tv = ((TextView) convertView.findViewById(R.id.item));
        String name = country.getName();
        tv.setText(name);
        /*if(Api.user.getCountry() == country.getName())
        {
            parent.dispatchSetSelected(true);
        }*/
        return convertView;
    }
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        Country country = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.drop_down_item, null);
        }
        TextView tv = ((TextView) convertView.findViewById(R.id.drop_down_item));

        String name = country.getName();
        tv.setText(name);
        return convertView;
    }

}