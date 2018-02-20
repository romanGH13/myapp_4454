package com.example.eqvol.eqvola.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.eqvol.eqvola.Classes.Account;
import com.example.eqvol.eqvola.Classes.Quotation;
import com.example.eqvol.eqvola.R;
import com.example.eqvol.eqvola.fragments.QuotationsFragment;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by eqvol on 25.10.2017.
 */

public class QuotationsForSpinnerAdapter extends ArrayAdapter<Quotation> implements Comparator<Quotation> {

    private List<Quotation> list;
    public QuotationsForSpinnerAdapter(@NonNull Context context, List<Quotation> list) {
        super(context, R.layout.spinner_group_item, list);
        this.list = list;
        Collections.sort(list, this);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Quotation quotation = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.spinner_quotation_item, null);
        }
        TextView tv = ((TextView) convertView.findViewById(R.id.item));
        tv.setText(quotation.getSymbol());

        return convertView;
    }
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        Quotation quotation = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.drop_down_item, null);
        }
        TextView tv = ((TextView) convertView.findViewById(R.id.drop_down_item));
        tv.setText(quotation.getSymbol());
        return convertView;
    }

    @Override
    public int compare(Quotation q1, Quotation q2) {
        return q1.getSymbol().compareTo(q2.getSymbol());
    }

    public int getPosititonBySymbol(String symbol)
    {
        for(Quotation q: list)
        {
            if(q.getSymbol().contentEquals(symbol))
                return getPosition(q);
        }
        return 0;
    }
}
