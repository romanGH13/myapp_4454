package com.example.eqvol.eqvola;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.constraint.Group;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.eqvol.eqvola.Adapters.CountryAdapter;
import com.example.eqvol.eqvola.Classes.Api;
import com.example.eqvol.eqvola.Classes.AsyncHttpTask;
import com.example.eqvol.eqvola.Classes.AsyncMethodNames;
import com.example.eqvol.eqvola.Classes.Country;
import com.example.eqvol.eqvola.Classes.MyDateFormat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity implements  TextView.OnEditorActionListener{

    String checkedEmail;
    boolean isClickNext;
    boolean isEmailAlreadyUse;

    int currentGroup;
    Group group1;
    Group group2;
    Group group3;
    Group group4;

    private EditText mNameView;
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;
    private EditText mPartnerCodeView;

    private EditText mPhoneCodeView;
    private EditText mPhoneNumberView;

    private Spinner mBirthDaySpinnerView;
    private Spinner mBirthMonthSpinnerView;
    private Spinner mBirthYearSpinnerView;
    private Spinner mCountrySpinnerView;

    private EditText mStreetView;
    private EditText mCityView;
    private EditText mStateView;
    private EditText mPostalCodeView;

    private MyDateFormat mdt = new MyDateFormat();
    HashMap<String, Object> userMetaData = null;

    public RegistrationActivity()
    {
        getAllCountries();
       /* HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("token", Api.getToken());
        AsyncHttpTask userLoginTask = new AsyncHttpTask(params, AsyncMethodNames.GET_GROUPS, this);
        userLoginTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        userMetaData = new HashMap<String, Object>();
        checkedEmail = "";
        isClickNext = false;
        isEmailAlreadyUse = true;

        currentGroup = 1;
        group1 = (Group) findViewById(R.id.groupStep1);
        group2 = (Group) findViewById(R.id.groupStep2);
        group3 = (Group) findViewById(R.id.groupStep3);
        group4 = (Group) findViewById(R.id.groupStep4);

        mNameView = (EditText) findViewById(R.id.name);
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mConfirmPasswordView = (EditText) findViewById(R.id.confirmPassword);
        mPartnerCodeView = (EditText) findViewById(R.id.partnerCode);

        mBirthDaySpinnerView = (Spinner) findViewById(R.id.spinnerDayOfBirth);
        mBirthMonthSpinnerView = (Spinner) findViewById(R.id.spinnerMonthOfBirth);
        mBirthYearSpinnerView = (Spinner) findViewById(R.id.spinnerYearOfBirth);
        mCountrySpinnerView = (Spinner) findViewById(R.id.spinnerCountries);

        mStreetView = (EditText) findViewById(R.id.street);
        mCityView = (EditText) findViewById(R.id.city);
        mStateView = (EditText) findViewById(R.id.state);
        mPostalCodeView = (EditText) findViewById(R.id.postalCode);

        mPhoneCodeView = (EditText) findViewById(R.id.phoneCode);
        mPhoneNumberView = (EditText) findViewById(R.id.phoneNumber);

        mNameView.setOnEditorActionListener(this);
        mNameView.setImeActionLabel("Next", EditorInfo.IME_ACTION_NEXT);
        mEmailView.setOnEditorActionListener(this);
        mEmailView.setImeActionLabel("Next", EditorInfo.IME_ACTION_NEXT);
        mPasswordView.setOnEditorActionListener(this);
        mPasswordView.setImeActionLabel("Next", EditorInfo.IME_ACTION_NEXT);
        mConfirmPasswordView.setOnEditorActionListener(this);
        mConfirmPasswordView.setImeActionLabel("Next", EditorInfo.IME_ACTION_NEXT);
        mPartnerCodeView.setOnEditorActionListener(this);
        mPartnerCodeView.setImeActionLabel("Next step", EditorInfo.IME_ACTION_DONE);

        mPhoneNumberView = (EditText) findViewById(R.id.phoneNumber);
        mPhoneNumberView.setOnEditorActionListener(this);
        mPhoneNumberView.setImeActionLabel("Next step", EditorInfo.IME_ACTION_DONE);

        mStreetView.setOnEditorActionListener(this);
        mStreetView.setImeActionLabel("Next", EditorInfo.IME_ACTION_NEXT);
        mCityView.setOnEditorActionListener(this);
        mCityView.setImeActionLabel("Next", EditorInfo.IME_ACTION_NEXT);
        mStateView.setOnEditorActionListener(this);
        mStateView.setImeActionLabel("Next", EditorInfo.IME_ACTION_NEXT);
        mPostalCodeView.setOnEditorActionListener(this);
        mPostalCodeView.setImeActionLabel("Next step", EditorInfo.IME_ACTION_DONE);

        findViewById(R.id.termsPolicyText).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                Uri adress= Uri.parse("http://lk.eqvola.net/documents/PrivacyPolicy.pdf");
                Intent browser= new Intent(Intent.ACTION_VIEW, adress);
                startActivity(browser);
            }

        });

        setDateBirth();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        /*if (v.getId() != R.id.promoCode) {
            if (isFieldEmpty(v)) {
                return true;
            }
        }*/
        if (v.getId() == R.id.name){

            String name = mNameView.getText().toString();

            if(isNameValid(name))
            {
                return false;
            }
            else {
                mNameView.setError(getString(R.string.error_invalid_name));
                mNameView.requestFocus();
                return true;
            }
        }
        if (v.getId() == R.id.email){

            String email = mEmailView.getText().toString();

            if(isEmailValid(email))
            {
                checkEmail(mEmailView.getText().toString());
                return false;
            }
            else {
                mEmailView.setError(getString(R.string.error_invalid_email));
                mEmailView.requestFocus();
                return true;
            }
        }
        if (v.getId() == R.id.password){
            String password = mPasswordView.getText().toString();
            if (isPasswordValid(password)) {
                return false;
            }
            else {
                mPasswordView.setError(getString(R.string.error_invalid_password));
                mPasswordView.requestFocus();
                return true;
            }
        }
        if (v.getId() == R.id.confirmPassword){
            String confirmPassword = mConfirmPasswordView.getText().toString();
            if (isConfirmPasswordValid(confirmPassword)) {
                return false;
                //attemptLogin(mPasswordView);
            }
            else {
                mConfirmPasswordView.setError(getString(R.string.error_password_do_not_match));
                mConfirmPasswordView.requestFocus();
                return true;
            }
        }
        if (v.getId() == R.id.partnerCode){
            nextClick(null);
        }

        if (v.getId() == R.id.postalCode){
            nextClick(null);
        }

        if (v.getId() == R.id.phoneNumber){
            String phoneNumber = mPhoneNumberView.getText().toString();
            if (isPhoneNumberValid(phoneNumber)) {
                goToStep3();
                //nextClick(null);
                //return false;
                //attemptLogin(mPasswordView);
            }
            else {
                mPhoneNumberView.setError(getString(R.string.error_phone_number));
                mPhoneNumberView.requestFocus();
                return true;
            }
        }
        return false;
    }
    public void checkEmail(String email)
    {
        if(checkedEmail != email) {
            checkedEmail = email;
            isEmailAlreadyUse = true;
            HashMap<String, Object> parametrs = new HashMap<String, Object>();
            parametrs.put("email", email);
            AsyncHttpTask userLoginTask = new AsyncHttpTask(parametrs, AsyncMethodNames.CHECK_EMAIL, this);
            userLoginTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    public void emailChecked(boolean isAlreadyUse)
    {
        if (isAlreadyUse) {
            isEmailAlreadyUse = true;
            mEmailView.setError(getString(R.string.error_email_already_use));
            mEmailView.requestFocus();
        } else {
            isEmailAlreadyUse = false;
            checkedEmail = mEmailView.getText().toString();
            if (isClickNext) {
                if (checkedEmail.contentEquals(mEmailView.getText().toString())) {
                    if (checkDataStep1()) {
                        goToStep2();
                    }
                } else {
                    checkEmail(mEmailView.getText().toString());
                }
            }

        }
    }

    private boolean isFieldEmpty(View view)
    {
        boolean isEmpty = true;
        Class str = EditText.class;

        EditText field = (EditText) view;
        field.setError(null);
        if (TextUtils.isEmpty(field.getText())) {
            field.setError(getString(R.string.error_field_required));
        } else {
            isEmpty = false;
        }

        return isEmpty;
    }

    private boolean isNameValid(String name) {
        Pattern p = Pattern.compile("^[A-Za-z][A-Za-z]+\\s[A-Za-z][A-Za-z]+$");//"^([A-Za-z]).{4,} *' '+([A-Za-z]).{4,}");
        Matcher m = p.matcher(name);
        return m.find();
    }
    private boolean isEmailValid(String email) {
        Pattern p = Pattern.compile("^[A-Za-z0-9-]+(\\-[A-Za-z0-9])*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9])");
        Matcher m = p.matcher(email);
        return m.find();
    }

    private boolean isPasswordValid(String password) {
        Pattern p = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$");
        Matcher m = p.matcher(password);
        return m.find();
    }

    private boolean isPhoneNumberValid(String password) {
        Pattern p = Pattern.compile("^([0-9]).{8}$");
        Matcher m = p.matcher(password);
        return m.find();
    }

    private boolean isConfirmPasswordValid(String confirmPassword) {
        return mPasswordView.getText().toString().contentEquals(confirmPassword);
    }

    public void goToLogin()
    {
        Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginActivity);
        finish();
    }

    public void cancelRegistration(View view) {
        isClickNext = false;
        if(currentGroup == 1) {
            goToLogin();
        }
        else if(currentGroup == 2)
        {
            group2.setVisibility(View.GONE);
            group1.setVisibility(View.VISIBLE);
            currentGroup = 1;
        }
        else if(currentGroup == 3)
        {
            group3.setVisibility(View.GONE);
            group2.setVisibility(View.VISIBLE);
            currentGroup = 2;
        }
        else if(currentGroup == 4)
        {
            group4.setVisibility(View.GONE);
            group3.setVisibility(View.VISIBLE);
            currentGroup = 3;
        }
    }

    public boolean checkDataStep1()
    {

        isEmailAlreadyUse = true;
        boolean isDataValid = true;
        if(onEditorAction(mNameView, 0, null)){
            isDataValid = false;
        }
        if(onEditorAction(mEmailView, 0, null)){
            isDataValid = false;
        }
        else{
            if(!checkedEmail.contentEquals(mEmailView.getText().toString())) {
                checkEmail(mEmailView.getText().toString());
            }
        }
        if(onEditorAction(mPasswordView, 0, null)){
            isDataValid = false;
        }
        else if(onEditorAction(mConfirmPasswordView, 0, null)){
            isDataValid = false;
        }
        return isDataValid;
    }

    public void goToStep2()
    {
        currentGroup = 2;
        group1.setVisibility(View.GONE);
        group2.setVisibility(View.VISIBLE);
        userMetaData.put("name", mNameView.getText().toString());
    }
    public void goToStep3()
    {
        currentGroup = 3;
        group2.setVisibility(View.GONE);
        group3.setVisibility(View.VISIBLE);
        userMetaData.put("birth_date", mBirthDaySpinnerView.getSelectedItem().toString() + "-" + mBirthMonthSpinnerView.getSelectedItem().toString() + "-" + mBirthYearSpinnerView.getSelectedItem().toString());
        userMetaData.put("country", ((Country)mCountrySpinnerView.getSelectedItem()).getName());
        userMetaData.put("phone_code", mPhoneCodeView.getText().toString());
        userMetaData.put("phone_number", mPhoneNumberView.getText().toString());
    }

    public void goToStep4()
    {
        currentGroup = 4;
        group3.setVisibility(View.GONE);
        group4.setVisibility(View.VISIBLE);

        userMetaData.put("city", mCityView.getText().toString());
        userMetaData.put("state", mStateView.getText().toString());
        userMetaData.put("street", mStreetView.getText().toString());
        userMetaData.put("postal_code", mPostalCodeView.getText().toString());
    }

    public void nextClick(View view) {
        isClickNext = false;
        if(currentGroup == 1) {
            isClickNext = true;
            if(checkDataStep1()) {

            }
        }
        else if(currentGroup == 2)
        {
            if(!onEditorAction(mPhoneNumberView, 0, null)){

            }
        }
        else if(currentGroup == 3)
        {
            goToStep4();
        }

        else if(currentGroup == 4)
        {
            CheckBox cb = (CheckBox)findViewById(R.id.checkbox);
            if(cb.isChecked())
            {
                HashMap<String, Object> parametrs = new HashMap<String, Object>();
                parametrs.put("email", mEmailView.getText().toString());
                parametrs.put("password", mPasswordView.getText().toString());
                parametrs.put("language", "en");
                if(!mPartnerCodeView.getText().toString().contentEquals("")) {
                    parametrs.put("partner_code", mPartnerCodeView.getText().toString());
                }

                Gson gson = new GsonBuilder().create();
                String json = gson.toJson(userMetaData);
                parametrs.put("metas", json);

                AsyncHttpTask userLoginTask = new AsyncHttpTask(parametrs, AsyncMethodNames.USER_REGISTRATION, this);
                userLoginTask.execute();
            }
        }
    }

    public void setDateBirth()
    {
        Spinner spinDay = (Spinner)findViewById(R.id.spinnerDayOfBirth);
        Spinner spinMonth = (Spinner)findViewById(R.id.spinnerMonthOfBirth);
        Spinner spinYear = (Spinner)findViewById(R.id.spinnerYearOfBirth);

        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                String year = ((Spinner)findViewById(R.id.spinnerYearOfBirth)).getSelectedItem().toString();
                String month = ((Spinner)findViewById(R.id.spinnerMonthOfBirth)).getSelectedItem().toString();
                Spinner spin = (Spinner)findViewById(R.id.spinnerDayOfBirth);
                ArrayAdapter<String> adapter = (ArrayAdapter<String>)spin.getAdapter();
                adapter.clear();
                adapter.addAll(mdt.getDays(year, month));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        };

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        adapter2.addAll(mdt.getMonths());
        spinMonth.setAdapter(adapter2);
        spinMonth.setOnItemSelectedListener(listener);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        adapter3.addAll(mdt.getYears());
        spinYear.setAdapter(adapter3);
        spinYear.setOnItemSelectedListener(listener);

        String year = ((Spinner)findViewById(R.id.spinnerYearOfBirth)).getSelectedItem().toString();
        String month = ((Spinner)findViewById(R.id.spinnerMonthOfBirth)).getSelectedItem().toString();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        adapter.addAll(mdt.getDays(year, month));
        spinDay.setAdapter(adapter);

        selectValue(spinYear, "1990");
    }

    public  void setSpinner()
    {
        ArrayAdapter<Country> adapter = new CountryAdapter(this);
        adapter.setDropDownViewResource(R.layout.drop_down_item);

        final Spinner spinner = (Spinner) findViewById(R.id.spinnerCountries);
        spinner.setAdapter(adapter);
        //selectValue(spinner, Api.user.getCountry());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                Country country = (Country)parent.getItemAtPosition(pos);
                ((TextView) findViewById(R.id.phoneCode)).setText(Integer.toString(country.getPhone_code()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    public static void selectValue(Spinner spinner, String value)
    {
        for (int i = 0; i < spinner.getCount(); i++)
        {
            if(spinner.getAdapter().getClass() == CountryAdapter.class)
            {
                if (((Country)spinner.getItemAtPosition(i)).getName().equals(value))
                {
                    spinner.setSelection(i);
                    break;
                }
            }
            else if ((spinner.getItemAtPosition(i)).toString().equals(value))
            {
                spinner.setSelection(i);
                break;
            }
        }
    }

    public void getAllCountries()
    {
        AsyncHttpTask userLoginTask = new AsyncHttpTask(null, AsyncMethodNames.GET_COUNTRIES, this);
        userLoginTask.execute();
    }


    public void showAccessRegisterDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.account_was_created))
                .setMessage(getString(R.string.account_register_info))
                .setIcon(R.drawable.ic_status_success)
                .setCancelable(false)
                .setNegativeButton("Close",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                goToLogin();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void showErrorRegisterDialog(String error)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.registation_error))
                .setMessage(error)
                .setIcon(R.drawable.ic_status_error)
                .setCancelable(false)
                .setNegativeButton("Close",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
