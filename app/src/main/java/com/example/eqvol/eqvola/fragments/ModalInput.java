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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eqvol.eqvola.Adapters.RegistrationPagerAdapter;
import com.example.eqvol.eqvola.R;
import com.example.eqvol.eqvola.RegistrationActivity;


public class ModalAlert extends DialogFragment implements DialogInterface.OnDismissListener {

    private Dialog dialog;
    private static View mView = null;
    private boolean status;
    private String description;
    private Activity activity;

    public ModalAlert(boolean status, String descripton) {
        // Required empty public constructor
        this.status = status;
        this.description = descripton;
        this.activity = null;
    }

    public ModalAlert(boolean status, String descripton, Activity activity) {
        // Required empty public constructor
        this.status = status;
        this.description = descripton;
        this.activity = activity;
    }



    public static ModalAlert newInstance(boolean status, String descripton) {
        ModalAlert fragment = new ModalAlert(status, descripton);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_create_account, container, false);
        return mView;
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_alert, null);
        ImageView img = (ImageView)view.findViewById(R.id.alertImage);
        TextView text = (TextView)view.findViewById(R.id.alertText);
        Button btn = (Button)view.findViewById(R.id.alertBtn);
        if(status) {
            img.setImageResource(R.drawable.image_success);
        }
        else {
            img.setImageResource(R.drawable.image_error);
        }
        text.setText(description);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activity != null) {
                    if (activity.getClass().toString().contentEquals(RegistrationActivity.class.toString())) {
                        if (status) {
                            ((RegistrationActivity) activity).goToLogin();
                        }
                    }
                }
                dialog.dismiss();
            }
        });

        builder.setView(view);
        dialog = builder.create();

        return dialog;
    }



}
