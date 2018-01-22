package com.example.eqvol.eqvola.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import java.util.Map;

/**
 * Created by eqvol on 27.12.2017.
 */

public class OpenOrdersAdapter extends RecyclerView.Adapter<OpenOrdersAdapter.OpenOrdersViewHolder> implements Comparator<Order> {

    Context ctx;
    LayoutInflater lInflater;
    //List<Order> orders;

    private OpenOrdersAdapter.OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnCLickListener(OpenOrdersAdapter.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public OpenOrdersAdapter(@NonNull Context context) {
        this.ctx = context;
        //this.orders = orders;
        Collections.sort(Api.account.openOrders, this);
        this.lInflater = LayoutInflater.from(this.ctx);
    }


    public static class OpenOrdersViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rv;
        TextView mOrderView;
        TextView mCurrencyPairView;
        TextView mOpenPriceView;
        TextView mCurrentPriceView;
        TextView mProfitView;

        public View container;

        OpenOrdersViewHolder(View itemView) {
            super(itemView);
            container = itemView;
            rv = (RecyclerView) itemView.findViewById(R.id.open_orders_list);
            mOrderView = ((TextView) itemView.findViewById(R.id.order));
            mCurrencyPairView = ((TextView) itemView.findViewById(R.id.currencyPair));
            mOpenPriceView = ((TextView) itemView.findViewById(R.id.openPrice));
            mCurrentPriceView = ((TextView) itemView.findViewById(R.id.currentPrice));
            mProfitView = ((TextView) itemView.findViewById(R.id.profit));
        }

    }

    @Override
    public OpenOrdersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.open_order_item, parent, false);
        OpenOrdersViewHolder pvh = new OpenOrdersViewHolder(v);
        return pvh;
    }

    private long timestampToDays(long timestamp) {
        long seconds = timestamp / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        return days;
    }

    @Override
    public void onBindViewHolder(OpenOrdersAdapter.OpenOrdersViewHolder holder, final int position) {

        double openPrice = Api.account.openOrders.get(position).getOpenPrice();
        double currentPrice = Api.account.openOrders.get(position).getClosePrice();
        double profit = Api.account.openOrders.get(position).getProfit();

        holder.mOrderView.setText("Order #" + Integer.toString(Api.account.openOrders.get(position).getOrder()));
        holder.mCurrencyPairView.setText(Api.account.openOrders.get(position).getSymbol());
        holder.mOpenPriceView.setText(Double.toString(openPrice));
        holder.mCurrentPriceView.setText(Double.toString(currentPrice));
        holder.mProfitView.setText(Double.toString(profit));

        if (profit >= 0) {
            holder.mProfitView.setTextAppearance(ctx, R.style.ThemeAccountDetailGreen);
        } else {
            holder.mProfitView.setTextAppearance(ctx, R.style.ThemeAccountDetailOrange);
        }
    }

    @Override
    public long getItemId(int position) {
        return Api.account.openOrders.get(position).getOrder();
    }

    @Override
    public int getItemCount() {
        return Api.account.openOrders.size();
    }

    @Override
    public int compare(Order o1, Order o2) {
        DateFormat format = new SimpleDateFormat(ctx.getString(R.string.date_format));
        try {
            Date date1 = format.parse(o1.getOpenTime());
            Date date2 = format.parse(o2.getOpenTime());
            return date1.compareTo(date2) * (-1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void UpdateOrderById(Order order) {
        for (Order o : Api.account.openOrders) {
            if (o.getOrder() == order.getOrder())
                o = order;
        }
        Collections.sort(Api.account.openOrders, this);
    }

    public Order getOrderById(long id) {
        for (Order o : Api.account.openOrders) {
            if (o.getOrder() == id)
                return o;
        }
        return null;
    }

    public void UpdateOrders(List<Order> orders)
    {
        try {
            Api.account.openOrders = orders;
            Collections.sort(Api.account.openOrders, this);
        } catch(Exception ex) {
            String str = ex.getMessage();
        }
    }



}
