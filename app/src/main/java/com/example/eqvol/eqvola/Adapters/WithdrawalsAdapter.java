package com.example.eqvol.eqvola.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eqvol.eqvola.Classes.Withdrawal;
import com.example.eqvol.eqvola.MainActivity;
import com.example.eqvol.eqvola.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by eqvol on 27.12.2017.
 */

public class WithdrawalsAdapter extends RecyclerView.Adapter<WithdrawalsAdapter.WithdrawalsViewHolder> implements Comparator<Withdrawal>{

    Context ctx;
    LayoutInflater lInflater;
    List<Withdrawal> withdrawals;


    public WithdrawalsAdapter(@NonNull Context context, List<Withdrawal> withdrawals) {
        this.ctx = context;
        this.withdrawals = withdrawals;
        Collections.sort(this.withdrawals, this);
        this.lInflater = LayoutInflater.from(this.ctx);
    }


    public  class WithdrawalsViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        RecyclerView rv;
        TextView mAccountView;
        TextView mAmountView;
        TextView mStatusView;

        public View container;

        WithdrawalsViewHolder(View itemView) {
            super(itemView);
            container = itemView;
            rv = (RecyclerView)itemView.findViewById(R.id.withdrawals_list);
            mAccountView = ((TextView) itemView.findViewById(R.id.account));
            mAmountView = ((TextView) itemView.findViewById(R.id.amount));
            mStatusView = ((TextView) itemView.findViewById(R.id.status));
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            Withdrawal withdrawal = get(position);
            MainActivity main = (MainActivity)container.getContext();
            main.openFinanceOperation(withdrawal);
        }
    }

    @Override
    public WithdrawalsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.withdrawal_item, parent, false);
        WithdrawalsViewHolder pvh = new WithdrawalsViewHolder(v);
        return pvh;
    }

    private long timestampToDays(long timestamp)
    {
        long seconds = timestamp / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        return days;
    }

    @Override
    public void onBindViewHolder(WithdrawalsViewHolder holder, final int position) {

        int login = withdrawals.get(position).getLogin();
        if(login == 0)
        {
            holder.mAccountView.setText("Local wallet");
        }
        else {
            holder.mAccountView.setText("Account " + Integer.toString(login));
        }
        holder.mAmountView.setText(Double.toString(withdrawals.get(position).getAmount()));
        int status = withdrawals.get(position).getStatus();
        switch(status)
        {
            case 0:
                holder.mStatusView.setText("Pending");
                holder.mStatusView.setTextAppearance(ctx, R.style.ThemeAccountDetailBlue);
                break;
            case 1:
                holder.mStatusView.setText("Confirmed");
                holder.mStatusView.setTextAppearance(ctx, R.style.ThemeAccountDetailGreen);
                break;
            case 2:
                holder.mStatusView.setText("Canceled");
                holder.mStatusView.setTextAppearance(ctx, R.style.ThemeAccountDetailOrange);
                break;
        }
    }

    @Override
    public long getItemId(int position) {
        return withdrawals.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return withdrawals.size();
    }

    @Override
    public int compare(Withdrawal o1, Withdrawal o2) {
        DateFormat format = new SimpleDateFormat(ctx.getString(R.string.date_format));
        try {
            Date date1 = format.parse(o1.getDate());
            Date date2 = format.parse(o2.getDate());
            return date1.compareTo(date2)*(-1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void UpdateOrderById(Withdrawal withdrawal){
        for(Withdrawal w: withdrawals){
            if(w.getId() == withdrawal.getId())
                w = withdrawal;
        }
        Collections.sort(this.withdrawals, this);
    }

    public Withdrawal getOrderById(long id){
        for(Withdrawal w: withdrawals){
            if(w.getId() == id)
                return w;
        }
        return null;
    }

    public Withdrawal get(int position)
    {
        return getOrderById(getItemId(position));
    }

    public void UpdateData(List<Withdrawal> data)
    {
        withdrawals = data;
        Collections.sort(withdrawals, this);
    }



}
