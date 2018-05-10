package com.example.eqvol.eqvola.fragments.Registration;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.example.eqvol.eqvola.R;
import com.example.eqvol.eqvola.RegistrationActivity;

public class FourthStepFragment extends Fragment{
    private View mView;

    private Drawable backgroundDrawable;

    public FourthStepFragment() {
    }


    public static FourthStepFragment newInstance() {
        FourthStepFragment fragment = new FourthStepFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        backgroundDrawable = getResources().getDrawable(R.drawable.rectangle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_fourth_step, container, false);
        mView.findViewById(R.id.termsPolicyText).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                Uri adress= Uri.parse("http://lk.eqvola.net/documents/PrivacyPolicy.pdf");
                Intent browser= new Intent(Intent.ACTION_VIEW, adress);
                startActivity(browser);
            }

        });

        ((LinearLayout)mView.findViewById(R.id.layoutTermsPolicy)).setBackground(backgroundDrawable);

        Button btnLogin = (Button) mView.findViewById(R.id.btnRegistration);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox)mView.findViewById(R.id.checkbox);
                if(cb.isChecked()) {
                    RegistrationActivity activity = (RegistrationActivity) getActivity();
                    activity.attemptLogin();
                }
                else
                {
                    ((RegistrationActivity) getActivity()).showDialog(false, "Agreements is not checked.");
                }
            }
        });
        Drawable drawable = getResources().getDrawable(R.drawable.rectangle);//btnLogin.getBackground();
        drawable.setColorFilter(getResources().getColor(R.color.colorRegister), PorterDuff.Mode.MULTIPLY);
        btnLogin.setBackground(drawable);
        return mView;
    }

}
