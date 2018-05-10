package com.example.eqvol.eqvola.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eqvol.eqvola.Classes.Api;
import com.example.eqvol.eqvola.Classes.FragmentLoader;
import com.example.eqvol.eqvola.Classes.PaymentSystem;
import com.example.eqvol.eqvola.MainActivity;
import com.example.eqvol.eqvola.R;
import com.example.eqvol.eqvola.fragments.RequestTransferFragment;
import com.example.eqvol.eqvola.fragments.RequestWithdrawalFragment;
import com.example.eqvol.eqvola.fragments.WithdrawalPaymentsFragment;

import java.util.Collections;
import java.util.Comparator;

/**
 * Created by eqvol on 27.12.2017.
 */

public class WithdrawalPaymentsAdapter extends RecyclerView.Adapter<WithdrawalPaymentsAdapter.WithdrawalPaymentsViewHolder> implements Comparator<PaymentSystem> {

    Context ctx;
    LayoutInflater lInflater;

    private WithdrawalPaymentsAdapter.OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnCLickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public WithdrawalPaymentsAdapter(@NonNull Context context) {
        this.ctx = context;
        Collections.sort(Api.withdrawalPayments, this);
        this.lInflater = LayoutInflater.from(this.ctx);
    }


    public static class WithdrawalPaymentsViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView currency;
        TextView paymentName;

        ConstraintLayout layoutDetail;

        WithdrawalPaymentsViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.payment_image);
            currency = (TextView) itemView.findViewById(R.id.payment_currency);
            paymentName = (TextView) itemView.findViewById(R.id.payment_name);
            layoutDetail = (ConstraintLayout) itemView.findViewById(R.id.layoutPayment);
        }
    }

    @Override
    public WithdrawalPaymentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_item, parent, false);
        WithdrawalPaymentsViewHolder pvh = new WithdrawalPaymentsViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final WithdrawalPaymentsViewHolder holder, final int position) {

        final PaymentSystem paymentSystem = Api.withdrawalPayments.get(position);

        if(paymentSystem.getAvatar() != null) {
            byte[] data = paymentSystem.getAvatar();
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            holder.image.setImageBitmap(bitmap);
        }
        holder.currency.setText(paymentSystem.getCurrencies());
        holder.paymentName.setText(paymentSystem.getTitle());

        holder.layoutDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: переход на следующюю страничку

                WithdrawalPaymentsFragment.currentPaymentId = paymentSystem.getId();
                FragmentLoader fl = new FragmentLoader(RequestWithdrawalFragment.class, ((MainActivity)ctx).getSupportFragmentManager(), R.id.container, false);
                fl.startLoading();
                MainActivity.currentLoader = fl;
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return Api.withdrawalPayments.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return Api.withdrawalPayments.size();
    }

    @Override
    public int compare(PaymentSystem t1, PaymentSystem t2) {
        if(t1.getTitle() == null)
            return -100;
        if(t2.getTitle() == null)
            return 100;

        return t1.getTitle().compareTo(t2.getTitle());
    }



}
