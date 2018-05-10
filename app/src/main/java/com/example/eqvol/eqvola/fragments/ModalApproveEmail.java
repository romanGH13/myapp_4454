package com.example.eqvol.eqvola.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.eqvol.eqvola.LoginActivity;
import com.example.eqvol.eqvola.R;
import com.example.eqvol.eqvola.RegistrationActivity;

import java.util.Timer;


public class ModalApproveEmail extends DialogFragment implements DialogInterface.OnDismissListener {

    private Dialog dialog;
    private View mView = null;
    private String description;
    private Activity activity;

    TextView labelResendView;

    private Timer timer;
    private int timerCounter;

    public ModalApproveEmail() {
        this.description = "";
        this.activity = null;
    }

    public static ModalApproveEmail newInstance(String description, Activity activity) {
        ModalApproveEmail fragment = new ModalApproveEmail();
        fragment.description = description;
        fragment.activity = activity;
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mView = inflater.inflate(R.layout.custom_alert_approve_email, null);
        TextView text = (TextView)mView.findViewById(R.id.alertText);
        final EditText code = (EditText)mView.findViewById(R.id.code);
        Button btn = (Button)mView.findViewById(R.id.alertBtn);


        text.setText(description);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(activity != null) {
                if (activity.getClass() == RegistrationActivity.class) {
                    ((RegistrationActivity)activity).approveEmail(code.getText().toString());
                }
                else if(activity.getClass() == LoginActivity.class)
                {
                    ((LoginActivity)activity).approveEmail(code.getText().toString());
                }
            }
            }
        });
        Drawable drawable = btn.getBackground();
        drawable.setColorFilter(getResources().getColor(R.color.colorBlue), PorterDuff.Mode.MULTIPLY);

        FloatingActionButton btnClose = (FloatingActionButton)mView.findViewById(R.id.closeDialog);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activity.getClass() == RegistrationActivity.class) {
                    ((RegistrationActivity) activity).goToLogin();
                    dialog.dismiss();
                }
                else if(activity.getClass() == LoginActivity.class)
                {
                    dialog.dismiss();
                }
            }
        });
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
