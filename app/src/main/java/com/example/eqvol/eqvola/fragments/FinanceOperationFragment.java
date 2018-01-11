package com.example.eqvol.eqvola.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eqvol.eqvola.Classes.Api;
import com.example.eqvol.eqvola.Classes.Withdrawal;
import com.example.eqvol.eqvola.MainActivity;
import com.example.eqvol.eqvola.R;

public class FinanceOperationFragment extends Fragment {

    private View mView;
    private Withdrawal order;
    public FinanceOperationFragment() {
        order = Api.currentOperation;
        // Required empty public constructor
    }

    public static FinanceOperationFragment newInstance() {
        FinanceOperationFragment fragment = new FinanceOperationFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_finance_operation, container, false);
        if(order.getWallet() == null)
        {
            ((TextView)mView.findViewById(R.id.wallet)).setVisibility(View.GONE);
            ((TextView)mView.findViewById(R.id.labelWallet)).setVisibility(View.GONE);
        }
        if(order.getTransaction().contentEquals(""))
        {
            ((TextView)mView.findViewById(R.id.transactionId)).setVisibility(View.GONE);
            ((TextView)mView.findViewById(R.id.labelTransactionId)).setVisibility(View.GONE);
        }
        ((TextView)mView.findViewById(R.id.operation)).setText("Operation #" + Integer.toString(order.getId()));
        ((TextView)mView.findViewById(R.id.login)).setText(Integer.toString(order.getLogin()));
        ((TextView)mView.findViewById(R.id.amount)).setText(Double.toString(order.getAmount()));
        ((TextView)mView.findViewById(R.id.commition)).setText(Double.toString(order.getCommission()));
        ((TextView)mView.findViewById(R.id.paymentSystem)).setText(order.getPayment_setting().getCode());
        ((TextView)mView.findViewById(R.id.wallet)).setText(order.getWallet());
        ((TextView)mView.findViewById(R.id.transactionId)).setText(order.getTransaction());
        ((TextView)mView.findViewById(R.id.comment)).setText(order.getComment());
        ((TextView)mView.findViewById(R.id.date)).setText(order.getDate());
        int status = order.getStatus();
        TextView mStatusView = (TextView)mView.findViewById(R.id.status);
        switch(status)
        {
            case 0:
                mStatusView.setText("Pending");
                mStatusView.setTextAppearance(getContext(), R.style.ThemeAccountDetailBlue);
                break;
            case 1:
                mStatusView.setText("Confirmed");
                mStatusView.setTextAppearance(getContext(), R.style.ThemeAccountDetailGreen);
                break;
            case 2:
                mStatusView.setText("Canceled");
                mStatusView.setTextAppearance(getContext(), R.style.ThemeAccountDetailOrange);
                break;
        }

        LinearLayout header = (LinearLayout)mView.findViewById(R.id.header);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity main = (MainActivity)getContext();
                main.openFinanceHistory();
            }
        });

        return mView;
    }

}
