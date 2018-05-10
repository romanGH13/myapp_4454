package com.example.eqvol.eqvola;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eqvol.eqvola.Adapters.RegistrationPagerAdapter;
import com.example.eqvol.eqvola.Classes.AsyncHttpTask;
import com.example.eqvol.eqvola.Classes.AsyncMethodNames;
import com.example.eqvol.eqvola.Classes.MyDateFormat;
import com.example.eqvol.eqvola.fragments.ModalAlert;
import com.example.eqvol.eqvola.fragments.ModalApproveEmail;
import com.example.eqvol.eqvola.fragments.ModalInput;
import com.example.eqvol.eqvola.fragments.Registration.FirstStepFragment;
import com.example.eqvol.eqvola.fragments.Registration.FourthStepFragment;
import com.example.eqvol.eqvola.fragments.Registration.SecondStepFragment;
import com.example.eqvol.eqvola.fragments.Registration.ThirdStepFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

public class RegistrationActivity extends AppCompatActivity implements TextView.OnEditorActionListener, ActivityCompat.OnRequestPermissionsResultCallback, android.app.LoaderManager.LoaderCallbacks<Cursor> {

    private LocationManager locationManager;

    private static final int MY_LOCATION_REQUEST_CODE = 3;
    private static String country = "";

    String checkedEmail;
    boolean isClickNext;
    boolean isEmailAlreadyUse;


    private MyDateFormat mdt = new MyDateFormat();
    HashMap<String, Object> userMetaData = null;
    private TextView btnPrev;
    private TextView btnNext;

    ViewPager pager;
    public int currentStep;

    private RegistrationPagerAdapter adapter;
    private FirstStepFragment step1;
    private SecondStepFragment step2;
    private ThirdStepFragment step3;

    private ModalInput myDialogFragment;
    private String register_id;
    private String register_code;

    private ModalApproveEmail modalApproveEmail;

    public RegistrationActivity() {

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        Drawable backgroundDrawable = getResources().getDrawable(R.drawable.background_image);
        ((LinearLayout)findViewById(R.id.mainLayout)).setBackground(backgroundDrawable);

        populateAutoComplete();

        country = getCountry(getApplicationContext());

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new RegistrationPagerAdapter(getSupportFragmentManager(), createFragments()));
        pager.setOnTouchListener(null);

        btnPrev = (TextView) findViewById(R.id.btnPrev);
        btnNext = (TextView) findViewById(R.id.btnNext);

        userMetaData = new HashMap<String, Object>();
        checkedEmail = "";
        isClickNext = false;
        isEmailAlreadyUse = true;

        getAllCountries();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        adapter = (RegistrationPagerAdapter)pager.getAdapter();
        step1 = (FirstStepFragment) adapter.getItem(0);
        step2 = (SecondStepFragment) adapter.getItem(1);
        step3 = (ThirdStepFragment) adapter.getItem(2);
    }

    private void beforeRegistration()
    {
        Map<String, Object> parametrs = new HashMap<String, Object>();
        parametrs.put("language", "en");
        parametrs.put("email", step1.getEmail());
        parametrs.put("phone_code", step2.getPhoneCode());
        parametrs.put("phone_number", step2.getPhoneNumber());

        AsyncHttpTask beforeRegistration = new AsyncHttpTask(parametrs, AsyncMethodNames.USER_BEFORE_REGISTRATION, this);
        beforeRegistration.target = step2.getPhoneCode() + step2.getPhoneNumber();
        beforeRegistration.execute();
    }

    public static String getCountry(Context context) {
        /*String country = PreferencesManager.getInstance(context).getString(COUNTRY);
        if (country != null) {
            return country;
        }*/

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return "";
            }
            Location location;// = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            //if (location == null) {
                location = locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            //}
            if (location != null) {
                Geocoder gcd = new Geocoder(context, Locale.getDefault());
                List<Address> addresses;
                try {
                    addresses = gcd.getFromLocation(location.getLatitude(),
                            location.getLongitude(), 1);

                    if (addresses != null && !addresses.isEmpty()) {
                        country = addresses.get(0).getCountryCode();
                        if (country != null) {
                            //PreferencesManager.getInstance(context).putString(COUNTRY, country);
                            return country;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        country = getCountryBasedOnSimCardOrNetwork(context);
        if (country != null) {
            //PreferencesManager.getInstance(context).putString(COUNTRY, country);
            return country;
        }
        return null;
    }

    /**
     * Get ISO 3166-1 alpha-2 country code for this device (or null if not available)
     *
     * @param context Context reference to get the TelephonyManager instance from
     * @return country code or null
     */
    private static String getCountryBasedOnSimCardOrNetwork(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getNetworkCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                return simCountry.toLowerCase(Locale.US);
            } else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    return networkCountry.toLowerCase(Locale.US);
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (!shouldShowRequestPermissionRationale(ACCESS_COARSE_LOCATION)) {
            requestPermissions(new String[]{ACCESS_COARSE_LOCATION}, MY_LOCATION_REQUEST_CODE);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    permissions[0].contentEquals(ACCESS_COARSE_LOCATION.toString()) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                        country = getCountry(getApplicationContext());
                        step2.setSelectionCountry(country);

            } else {
                // Permission was denied. Display an error message.
                country = "";
            }
        }
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
                    beforeRegistration();
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
            fragmentStep2.setSpinner(country);
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
        //requestCodeDialog("Your Phone xxxxxxxxxxx.\nEnter the code from Sms.", "321321");
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
        userMetaData.putAll(step2.getData());
        userMetaData.putAll(step3.getData());
        userMetaData.put("name", step1.getName());

        Map<String, Object> parametrs = step1.getData();

        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(userMetaData);
        parametrs.put("metas", json);

        if(register_id != null && register_code != null)
        {
            parametrs.put("register_id", register_id);
            parametrs.put("register_code", register_code);
        }

        AsyncHttpTask userLoginTask = new AsyncHttpTask(parametrs, AsyncMethodNames.USER_REGISTRATION, this);
        userLoginTask.execute();
    }


    public void cancelRegistration(View view) {
        isClickNext = false;
    }

    public void inputCodeDialog(String description)
    {
        modalApproveEmail = ModalApproveEmail.newInstance(description, this);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        modalApproveEmail.setCancelable(false);
        modalApproveEmail.show(transaction, "dialog");
    }

    public void emailApproved()
    {
        modalApproveEmail.closeDialog();
        showDialog(true, "Your email has been approved!");

    }

    public void wrongApproveCode()
    {
        modalApproveEmail.showError("WrongCode");

    }

    public void approveEmail(String code)
    {
        Map<String, Object> parametrs = new HashMap<String, Object>();
        parametrs.put("email_code", code);
        AsyncHttpTask userLoginTask = new AsyncHttpTask(parametrs, AsyncMethodNames.APPROVE_EMAIL, this);
        userLoginTask.execute();
    }

    public void showDialog(boolean status, String description)
    {
        ModalAlert myDialogFragment = ModalAlert.newInstance(status, description, this);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        myDialogFragment.show(transaction, "dialog");
    }

    public void requestCodeDialog(String description, String register_id)
    {
        this.register_id = register_id;
        myDialogFragment = ModalInput.newInstance( description, this);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        myDialogFragment.show(transaction, "dialog");
    }

    public void showError(String errorText)
    {
        myDialogFragment.showError(errorText);
    }

    public void closeInputDialog()
    {
        myDialogFragment.closeDialog();
        goToStep3();
    }

    public void startTimer()
    {
        myDialogFragment.startTimer();
    }

    public void setCodeFromSms(String code)
    {
        myDialogFragment.setCode(code);
    }

    public void checkBeforeRegister(String code)
    {
        this.register_code = code;
        Map<String, Object> parametrs = new HashMap<String, Object>();
        parametrs.put("register_id", register_id);
        parametrs.put("register_code", code);

        AsyncHttpTask checkCodeTask = new AsyncHttpTask(parametrs, AsyncMethodNames.USER_CHECK_BEFORE_REGISTRATION, this);
        checkCodeTask.execute();
    }

    public void resendBeforeRegister()
    {
        Map<String, Object> parametrs = new HashMap<String, Object>();
        parametrs.put("register_id", register_id);

        AsyncHttpTask checkCodeTask = new AsyncHttpTask(parametrs, AsyncMethodNames.USER_RESEND_BEFORE_REGISTRATION, this);
        checkCodeTask.execute();
    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {

    }


}
