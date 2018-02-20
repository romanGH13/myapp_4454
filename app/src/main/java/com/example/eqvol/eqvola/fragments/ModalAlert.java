package com.example.eqvol.eqvola.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eqvol.eqvola.Adapters.RegistrationPagerAdapter;
import com.example.eqvol.eqvola.Classes.FragmentLoader;
import com.example.eqvol.eqvola.ForgotPasswordActivity;
import com.example.eqvol.eqvola.MainActivity;
import com.example.eqvol.eqvola.R;
import com.example.eqvol.eqvola.RegistrationActivity;


public class ModalAlert extends DialogFragment implements DialogInterface.OnDismissListener {

    private Dialog dialog;
    private static View mView = null;
    private boolean status;
    private String description;
    private Activity activity;
    private String target;

    public ModalAlert(boolean status, String descripton) {
        // Required empty public constructor
        this.status = status;
        this.description = descripton;
        this.activity = null;
        this.target = "";
    }

    public ModalAlert(boolean status, String descripton, Activity activity, String target) {
        // Required empty public constructor
        this.status = status;
        this.description = descripton;
        this.activity = activity;
        this.target = target;
    }

    public ModalAlert(boolean status, String descripton, Activity activity) {
        // Required empty public constructor
        this.status = status;
        this.description = descripton;
        this.activity = activity;
        this.target = "";
    }



    public static ModalAlert newInstance(boolean status, String descripton) {
        ModalAlert fragment = new ModalAlert(status, descripton);

        return fragment;
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
                    if (activity.getClass() == RegistrationActivity.class) {
                        if (status) {
                            ((RegistrationActivity) activity).goToLogin();
                        }
                    }
                    if (activity.getClass() == ForgotPasswordActivity.class) {
                        if (status) {
                            ((ForgotPasswordActivity) activity).backToLogin(null);
                        }
                    }
                }
                if(!target.contentEquals(""))
                {
                    FragmentLoader fl = new FragmentLoader(MobileTraderFragment.class, ((MainActivity)(activity)).getSupportFragmentManager(), R.id.container, false);
                    fl.startLoading();
                    MainActivity.currentLoader = fl;
                }
                dialog.dismiss();
            }
        });

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        Drawable drawable = getResources().getDrawable(R.drawable.rectangle);//btnLogin.getBackground();
        drawable.setColorFilter(getResources().getColor(R.color.colorMain), PorterDuff.Mode.MULTIPLY);
        btn.setBackground(drawable);

        builder.setView(view);
        dialog = builder.create();

        return dialog;
    }



}
