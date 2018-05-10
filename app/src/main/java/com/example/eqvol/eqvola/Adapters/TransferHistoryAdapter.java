package com.example.eqvol.eqvola.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.eqvol.eqvola.Classes.Api;
import com.example.eqvol.eqvola.Classes.Transfer;
import com.example.eqvol.eqvola.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by eqvol on 27.12.2017.
 */

public class TransferHistoryAdapter extends RecyclerView.Adapter<TransferHistoryAdapter.TransfersHistoryViewHolder> implements Comparator<Transfer> {

    Context ctx;
    LayoutInflater lInflater;
    public EditText mSearchStringView;
    private List<Transfer> transfers;

    private TransferHistoryAdapter.OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnCLickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public TransferHistoryAdapter(@NonNull Context context, EditText mSearchStringView) {
        this.ctx = context;
        this.transfers = Api.transfers;
        Collections.sort(transfers, this);
        this.lInflater = LayoutInflater.from(this.ctx);
        this.mSearchStringView = mSearchStringView;
    }


    public static class TransfersHistoryViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rv;
        TextView mTransferView;
        TextView mTransferFromView;
        TextView mTransferToView;
        TextView mAmountView;
        TextView mDateView;
        TextView mStatusView;
        TextView mCommentView;

        public View container;

        TransfersHistoryViewHolder(View itemView) {
            super(itemView);
            container = itemView;
            mTransferView = ((TextView) itemView.findViewById(R.id.transfer));
            mTransferFromView = ((TextView) itemView.findViewById(R.id.transferFrom));
            mTransferToView = ((TextView) itemView.findViewById(R.id.transferTo));
            mAmountView = ((TextView) itemView.findViewById(R.id.amount));
            mDateView = ((TextView) itemView.findViewById(R.id.date));
            mStatusView = ((TextView) itemView.findViewById(R.id.status));
            mCommentView = ((TextView) itemView.findViewById(R.id.comment));
        }

    }

    @Override
    public TransfersHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.transfers_history_item, parent, false);
        TransfersHistoryViewHolder pvh = new TransfersHistoryViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(TransfersHistoryViewHolder holder, final int position) {

        int status = transfers.get(position).getStatus();

        holder.mTransferView.setText("Transfer #" + Integer.toString(transfers.get(position).getId()));
        int login = transfers.get(position).getSender();
        if(login == 0)
        {
            holder.mTransferFromView.setText("Local wallet");
        }
        else
        {
            holder.mTransferFromView.setText(Integer.toString(login));
        }

        holder.mTransferToView.setText(Integer.toString(transfers.get(position).getRecipient()));
        holder.mAmountView.setText(Double.toString(transfers.get(position).getAmount()));
        holder.mDateView.setText(transfers.get(position).getDate());
        holder.mCommentView.setText(transfers.get(position).getComment());

        switch (status)
        {
            case 0:
                holder.mStatusView.setText("In process");
                holder.mStatusView.setTextAppearance(ctx, R.style.ThemeAccountDetailBlue);
                break;
            case 1:
                holder.mStatusView.setText("Confirmed");
                holder.mStatusView.setTextAppearance(ctx, R.style.ThemeAccountDetailGreen);
                break;
            case 2:
                holder.mStatusView.setText("Rejected");
                holder.mStatusView.setTextAppearance(ctx, R.style.ThemeAccountDetailOrange);
                break;
        }
    }

    @Override
    public long getItemId(int position) {
        return transfers.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return transfers.size();
    }

    @Override
    public int compare(Transfer t1, Transfer t2) {
        DateFormat format = new SimpleDateFormat(ctx.getString(R.string.date_format));
        try {
            Date date1 = format.parse(t1.getDate());
            Date date2 = format.parse(t2.getDate());
            return date1.compareTo(date2) * (-1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void searchStringChanged()
    {
        transfers = new ArrayList<Transfer>();

        for(Transfer t: Api.transfers)
        {
            if(Integer.toString(t.getId()).startsWith(mSearchStringView.getText().toString()))
            {
                transfers.add(t);
            }
        }
        Collections.sort(transfers, this);
    }

    public void UpdateTransferById(Transfer transfer) {
        for (Transfer t : transfers) {
            if (t.getId() == transfer.getId())
                t = transfer;
        }
        Collections.sort(transfers, this);
    }

    public void UpdateTransfers()
    {
        transfers = Api.transfers;
        searchStringChanged();
        Collections.sort(transfers, this);
    }



}
