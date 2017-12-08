package com.example.eqvol.eqvola.Adapters;

import android.content.Context;
import android.icu.util.ULocale;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.eqvol.eqvola.Classes.Category;
import com.example.eqvol.eqvola.R;

import java.util.List;

/**
 * Created by eqvol on 30.10.2017.
 */

public class CategoryAdapter extends ArrayAdapter<Category>
{

    public CategoryAdapter(@NonNull Context context, List<Category> categories) {
        super(context, R.layout.spinner_category_item, categories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Category category = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.spinner_item, null);
        }
        TextView tv = ((TextView) convertView.findViewById(R.id.item));
        String name = category.getName();
        tv.setText(name);
        return convertView;
    }
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        Category category = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.drop_down_item, null);
        }
        TextView tv = ((TextView) convertView.findViewById(R.id.drop_down_item));

        String name = category.getName();
        tv.setText(name);
        return convertView;
    }

}