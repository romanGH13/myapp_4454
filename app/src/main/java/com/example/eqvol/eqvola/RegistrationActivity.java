package com.example.eqvol.eqvola;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eqvol.eqvola.Adapters.RegistrationPagerAdapter;
import com.example.eqvol.eqvola.Classes.AsyncHttpTask;
import com.example.eqvol.eqvola.Classes.AsyncMethodNames;
import com.example.eqvol.eqvola.Classes.MyDateFormat;
import com.example.eqvol.eqvola.fragments.ModalAlert;
import com.example.eqvol.eqvola.fragments.Registration.FirstStepFragment;
import com.example.eqvol.eqvola.fragments.Registration.FourthStepFragment;
import com.example.eqvol.eqvola.fragments.Registration.SecondStepFragment;
import com.example.eqvol.eqvola.fragments.Registration.ThirdStepFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity implements TextView.OnEditorActionListener{

    String checkedEmail;
    boolean isClickNext;
    boolean isEmailAlreadyUse;


    private MyDateFormat mdt = new MyDateFormat();
    HashMap<String, Object> userMetaData = null;
    private TextView btnPrev;
    private TextView btnNext;

    ViewPager pager;
    public int currentStep;

    public RegistrationActivity()
    {

    }

    public void prevStep(View v)
    {
        int currentItem = pager.getCurrentItem();
        if(currentItem == 0) {
            goToLogin();
        }
        if(currentItem == 1) {
            goToStep1();
        }
        if(currentItem == 2) {
            goToStep2();
        }
        if(currentItem == 3) {
            goToStep3();
        }

    }

    public void nextStep(View v)
    {
        int currentItem = pager.getCurrentItem();
        if(currentItem == 0) {
            isClickNext = false;
            RegistrationPagerAdapter adapter = (RegistrationPagerAdapter)pager.getAdapter();
            Fragment fragment = adapter.getItem(pager.getCurrentItem());
            if(fragment.getClass() == FirstStepFragment.class)
            {
                FirstStepFragment fragmentStep1 = (FirstStepFragment)fragment;
                if(fragmentStep1.checkDataStep1())
                {
                    isClickNext = true;
                    fragmentStep1.checkEmail();

                }
            }
        }
        if(currentItem == 1){
            RegistrationPagerAdapter adapter = (RegistrationPagerAdapter)pager.getAdapter();
            Fragment fragment = adapter.getItem(1);
            if(fragment.getClass() == SecondStepFragment.class) {
                SecondStepFragment fragmentStep2 = (SecondStepFragment) fragment;
                if (!fragmentStep2.checkPhoneNumber())
                {
                    goToStep3();
                }
            }

        }
        if(currentItem == 2) {
            btnNext.setVisibility(View.INVISIBLE);
            ((ImageView)findViewById(R.id.bntNextImage)).setVisibility(View.INVISIBLE);
            goToStep4();
        }
    }

    public void setCountries()
    {
        RegistrationPagerAdapter adapter = (RegistrationPagerAdapter)pager.getAdapter();
        Fragment fragment = adapter.getItem(1);
        if(fragment.getClass() == SecondStepFragment.class)
        {
            SecondStepFragment fragmentStep2 = (SecondStepFragment)fragment;
            fragmentStep2.setSpinner();
        }
    }
    private void goToStep1()
    {
        pager.setCurrentItem(0);
        btnPrev.setText("Login");
    }

    private void goToStep2()
    {
        pager.setCurrentItem(1);
        btnPrev.setText("Prev");
    }

    public void goToStep3()
    {
        pager.setCurrentItem(2);
        ((ImageView)findViewById(R.id.bntNextImage)).setVisibility(View.VISIBLE);
        btnNext.setVisibility(View.VISIBLE);
    }

    private void goToStep4()
    {
        pager.setCurrentItem(3);
    }

    public void emailChecked(boolean isAlreadyUse)
    {
        RegistrationPagerAdapter adapter = (RegistrationPagerAdapter)pager.getAdapter();
        Fragment fragment = adapter.getItem(pager.getCurrentItem());
        if(fragment.getClass() == FirstStepFragment.class) {
            FirstStepFragment fragmentStep1 = (FirstStepFragment) fragment;

            if (isAlreadyUse) {
                isEmailAlreadyUse = true;
                fragmentStep1.setEmailError(getString(R.string.error_email_already_use));
            } else {
                isEmailAlreadyUse = false;
                checkedEmail = fragmentStep1.getEmail();
                if (isClickNext) {
                    if (checkedEmail.contentEquals(fragmentStep1.getEmail()))
                    {
                        if (fragmentStep1.checkDataStep1()) {
                            goToStep2();
                        }
                    }
                    else {
                        fragmentStep1.checkEmail();
                    }
                }

            }
        }
    }

    private ArrayList<Fragment> createFragments()
    {
        ArrayList<Fragment> fragments = new ArrayList<Fragment>();
        for(int i = 0; i < 4; i++)
        {
            Fragment fragment = null;
            switch (i)
            {
                case 0:
                    fragment = FirstStepFragment.newInstance();
                    break;
                case 1:
                    fragment = SecondStepFragment.newInstance();
                    break;
                case 2:
                    fragment = ThirdStepFragment.newInstance();
                    break;
                case 3:
                    fragment = FourthStepFragment.newInstance();
                    break;
            }
            fragments.add(fragment);
        }
        return fragments;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        pager=(ViewPager)findViewById(R.id.pager);

        pager.setAdapter(new RegistrationPagerAdapter(getSupportFragmentManager(), createFragments()));
        pager.setOnTouchListener(null);

        btnPrev = (TextView) findViewById(R.id.btnPrev);
        btnNext = (TextView) findViewById(R.id.btnNext);

        userMetaData = new HashMap<String, Object>();
        checkedEmail = "";
        isClickNext = false;
        isEmailAlreadyUse = true;

        getAllCountries();
    }

    public void getAllCountries()
    {
        AsyncHttpTask userLoginTask = new AsyncHttpTask(null, AsyncMethodNames.GET_COUNTRIES, this);
        userLoginTask.execute();
    }

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        return false;
    }

    public void goToLogin()
    {
        Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginActivity);
        finish();
    }

    public void attemptLogin()
    {
        RegistrationPagerAdapter adapter = (RegistrationPagerAdapter)pager.getAdapter();
        FirstStepFragment step1 = (FirstStepFragment) adapter.getItem(0);
        SecondStepFragment step2 = (SecondStepFragment) adapter.getItem(1);
        ThirdStepFragment step3 = (ThirdStepFragment) adapter.getItem(2);

        userMetaData.putAll(step2.getData());
        userMetaData.putAll(step3.getData());
        userMetaData.put("name", step1.getName());

        Map<String, Object> parametrs = step1.getData();

        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(userMetaData);
        parametrs.put("metas", json);

        AsyncHttpTask userLoginTask = new AsyncHttpTask(parametrs, AsyncMethodNames.USER_REGISTRATION, this);
        userLoginTask.execute();
    }


    public void cancelRegistration(View view) {
        isClickNext = false;
    }


    public void showDialog(boolean status, String description)
    {
        ModalAlert myDialogFragment = new ModalAlert(status, description, this);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        myDialogFragment.show(transaction, "dialog");
    }
}
