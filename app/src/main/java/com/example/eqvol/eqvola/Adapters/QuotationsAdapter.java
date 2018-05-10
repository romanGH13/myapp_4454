package com.example.eqvol.eqvola.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.eqvol.eqvola.Classes.Quotation;
import com.example.eqvol.eqvola.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by eqvol on 27.12.2017.
 */

public class QuotationsAdapter extends RecyclerView.Adapter<QuotationsAdapter.QuotationViewHolder> implements Comparator<Quotation> {

    Context ctx;
    LayoutInflater lInflater;
    public EditText mSearchStringView;
    private List<Quotation> quotations;

    private QuotationsAdapter.OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnCLickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public QuotationsAdapter(@NonNull Context context, EditText mSearchStringView, List<Quotation> quotations) {
        this.ctx = context;
        this.quotations = quotations;
        //searchStringChanged(quotations);
        Collections.sort(this.quotations, this);
        this.lInflater = LayoutInflater.from(this.ctx);
        this.mSearchStringView = mSearchStringView;
    }

    public static class QuotationViewHolder extends RecyclerView.ViewHolder {
        TextView symbolView;
        TextView bidView;
        TextView askView;

        QuotationViewHolder(View itemView) {
            super(itemView);

            symbolView = (TextView) itemView.findViewById(R.id.symbol);
            bidView = (TextView) itemView.findViewById(R.id.bid);
            askView = (TextView) itemView.findViewById(R.id.ask);
        }
    }

    @Override
    public QuotationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.quotation_item, parent, false);
        QuotationViewHolder pvh = new QuotationViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final QuotationViewHolder holder, final int position) {

        Quotation quotation = this.quotations.get(position);

        holder.symbolView.setText(quotation.getSymbol());

        int digits = quotation.getDigits();
        String mask = "0.";
        for(int i = 0; i < digits; i++)
        {
            mask += "0";
        }

        DecimalFormat myFormatter = new DecimalFormat(mask);

        String bid = myFormatter.format(quotation.getBid());
        String ask = myFormatter.format(quotation.getAsk());

        holder.bidView.setText(bid);
        holder.askView.setText(ask);

        if(quotation.getDirection().contains("Up"))
        {
            holder.bidView.setTextColor(ctx.getResources().getColor(R.color.colorGreenBalance));
            holder.askView.setTextColor(ctx.getResources().getColor(R.color.colorGreenBalance));
        }
        else
        {
            holder.bidView.setTextColor(ctx.getResources().getColor(R.color.colorOrangeLeverage));
            holder.askView.setTextColor(ctx.getResources().getColor(R.color.colorOrangeLeverage));
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return quotations.size();
    }

    @Override
    public int compare(Quotation t1, Quotation t2) {

        return t1.getSymbol().compareTo(t2.getSymbol());
    }

    public void searchStringChanged(List<Quotation> quotations)
    {
        this.quotations = new ArrayList<Quotation>();

        for(Quotation q: quotations)
        {
            if(q.getSymbol().contains(mSearchStringView.getText().toString().toUpperCase()))
            {
                if(!q.getSymbol().contains("."))
                    this.quotations.add(q);
            }
        }

        Collections.sort(this.quotations, this);
    }

    public void UpdateQuotations(List<Quotation> quotations)
    {
        this.quotations = quotations;
        searchStringChanged(this.quotations);
        Collections.sort(this.quotations, this);
    }

    public void UpdateQuotationBySymbol(Quotation quotation)
    {
        for(int i = 0; i < quotations.size(); i++)
        {
            if(quotations.get(i).getSymbol().contains("."))
                quotations.remove(i);
            else if(quotations.get(i).getSymbol().contentEquals(quotation.getSymbol()))
            {
                quotations.set(i, quotation);
                return;
            }
        }
    }

}
