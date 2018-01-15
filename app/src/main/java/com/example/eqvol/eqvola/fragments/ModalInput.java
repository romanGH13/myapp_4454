package com.example.eqvol.eqvola.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eqvol.eqvola.R;
import com.example.eqvol.eqvola.RegistrationActivity;


public class ModalInput extends DialogFragment implements DialogInterface.OnDismissListener {

    private Dialog dialog;
    private View mView = null;
    private String description;
    private Activity activity;

    public ModalInput(String descripton) {
        this.description = descripton;
        this.activity = null;
    }

    public ModalInput(String descripton, Activity activity) {
        this.description = descripton;
        this.activity = activity;
    }



    public static ModalInput newInstance(String descripton) {
        ModalInput fragment = new ModalInput(descripton);

        return fragment;
    }

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.custom_alert_input, container, false);
        return mView;
    }*/



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mView = inflater.inflate(R.layout.custom_alert_input, null);
        TextView text = (TextView)mView.findViewById(R.id.alertText);
        final EditText code = (EditText)mView.findViewById(R.id.code);
        Button btn = (Button)mView.findViewById(R.id.alertBtn);
        TextView labelResendView = (TextView) mView.findViewById(R.id.labelResend);
        ImageView imageResendView = (ImageView) mView.findViewById(R.id.imgResend);

        text.setText(description);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activity != null) {
                    if (activity.getClass().toString().contentEquals(RegistrationActivity.class.toString())) {
                        ((RegistrationActivity)activity).checkBeforeRegister(code.getText().toString());
                    }
                }
            }
        });

        View.OnClickListener resendOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activity != null) {
                    if (activity.getClass().toString().contentEquals(RegistrationActivity.class.toString())) {
                        ((RegistrationActivity)activity).resendBeforeRegister();
                    }
                }

            }
        };

        labelResendView.setOnClickListener(resendOnClickListener);
        imageResendView.setOnClickListener(resendOnClickListener);

        builder.setView(mView);
        dialog = builder.create();

        return dialog;
    }

    public void showError(String errorText)
    {
        EditText mCodeView = (EditText)mView.findViewById(R.id.code);
        mCodeView.setError(errorText);
    }
    public void closeDialog()
    {
        dialog.dismiss();
    }



}
