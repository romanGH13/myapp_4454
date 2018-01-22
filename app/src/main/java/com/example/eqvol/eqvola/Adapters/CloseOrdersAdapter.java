package com.example.eqvol.eqvola.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eqvol.eqvola.Classes.Api;
import com.example.eqvol.eqvola.Classes.Order;
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

public class CloseOrdersAdapter extends RecyclerView.Adapter<CloseOrdersAdapter.CloseOrdersViewHolder> implements Comparator<Order> {

    Context ctx;
    LayoutInflater lInflater;

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnCLickListener(OnItemClickListener onItemClickListener)
    {
        this.mOnItemClickListener = onItemClickListener;
    }

    public CloseOrdersAdapter(@NonNull Context context) {
        this.ctx = context;
        Collections.sort(Api.account.closeOrders, this);
        this.lInflater = LayoutInflater.from(this.ctx);
    }


    public static class CloseOrdersViewHolder extends RecyclerView.ViewHolder{
        RecyclerView rv;
        TextView mOrderView;
        TextView mCurrencyPairView;
        TextView mOpenPriceView;
        TextView mClosePriceView;
        TextView mCloseTimeView;
        TextView mProfitView;

        public View container;

        CloseOrdersViewHolder(View itemView) {
            super(itemView);
            container = itemView;
            rv = (RecyclerView)itemView.findViewById(R.id.open_orders_list);
            mOrderView = ((TextView) itemView.findViewById(R.id.order));
            mCurrencyPairView = ((TextView) itemView.findViewById(R.id.currencyPair));
            mOpenPriceView = ((TextView) itemView.findViewById(R.id.openPrice));
            mClosePriceView = ((TextView) itemView.findViewById(R.id.closePrice));
            mCloseTimeView = ((TextView) itemView.findViewById(R.id.closeTime));
            mProfitView = ((TextView) itemView.findViewById(R.id.profit));


        }

    }

    @Override
    public CloseOrdersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.close_order_item, parent, false);
        CloseOrdersViewHolder pvh = new CloseOrdersViewHolder(v);
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
    public void onBindViewHolder(CloseOrdersViewHolder holder, final int position) {

        double profit = Api.account.closeOrders.get(position).getProfit();

        holder.mOrderView.setText("Order #" + Integer.toString(Api.account.closeOrders.get(position).getOrder()));
        holder.mCurrencyPairView.setText(Api.account.closeOrders.get(position).getSymbol());
        holder.mOpenPriceView.setText(Double.toString(Api.account.closeOrders.get(position).getOpenPrice()));
        holder.mClosePriceView.setText(Double.toString(Api.account.closeOrders.get(position).getClosePrice()));
        holder.mProfitView.setText(Double.toString(profit));
        holder.mCloseTimeView.setText(Api.account.closeOrders.get(position).getCloseTime());

        if (profit >= 0)
        {
            holder.mProfitView.setTextAppearance(ctx, R.style.ThemeAccountDetailGreen);
        }
        else
        {
            holder.mProfitView.setTextAppearance(ctx, R.style.ThemeAccountDetailOrange);
        }
    }

    @Override
    public long getItemId(int position) {
        return Api.account.closeOrders.get(position).getOrder();
    }

    @Override
    public int getItemCount() {
        return Api.account.closeOrders.size();
    }

    @Override
    public int compare(Order o1, Order o2) {
        DateFormat format = new SimpleDateFormat(ctx.getString(R.string.date_format));
        try {
            Date date1 = format.parse(o1.getOpenTime());
            Date date2 = format.parse(o2.getOpenTime());
            return date1.compareTo(date2)*(-1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void UpdateOrderById(Order order){
        for(Order o: Api.account.closeOrders){
            if(o.getOrder() == order.getOrder())
                o = order;
        }
        Collections.sort(Api.account.closeOrders, this);
    }

    public Order getOrderById(long id){
        for(Order o: Api.account.closeOrders){
            if(o.getOrder() == id)
                return o;
        }
        return null;
    }

    public void updateOrders(List<Order> orders)
    {
        Api.account.closeOrders = orders;
        Collections.sort(Api.account.closeOrders, this);
    }



}