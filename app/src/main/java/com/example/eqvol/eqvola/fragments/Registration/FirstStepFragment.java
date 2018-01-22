package com.example.eqvol.eqvola.fragments.Registration;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eqvol.eqvola.Classes.AsyncHttpTask;
import com.example.eqvol.eqvola.Classes.AsyncMethodNames;
import com.example.eqvol.eqvola.R;
import com.example.eqvol.eqvola.RegistrationActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FirstStepFragment extends Fragment implements TextView.OnEditorActionListener{

    private View mView;

    private EditText mNameView;
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;
    private EditText mPartnerCodeView;

    private Drawable backgroundDrawable;

    String checkedEmail;
    boolean isClickNext;
    boolean isEmailAlreadyUse;

    public FirstStepFragment() {
    }


    public static FirstStepFragment newInstance() {
        FirstStepFragment fragment = new FirstStepFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkedEmail = "";
        isClickNext = false;
        isEmailAlreadyUse = true;

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        backgroundDrawable = getResources().getDrawable(R.drawable.rectangle);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_first_step, container, false);

        mNameView = (EditText) mView.findViewById(R.id.name);
        mEmailView = (EditText) mView.findViewById(R.id.email);
        mPasswordView = (EditText) mView.findViewById(R.id.password);
        mConfirmPasswordView = (EditText) mView.findViewById(R.id.confirmPassword);
        mPartnerCodeView = (EditText) mView.findViewById(R.id.partnerCode);

        mNameView.setBackground(backgroundDrawable);
        mEmailView.setBackground(backgroundDrawable);
        mPasswordView.setBackground(backgroundDrawable);
        mConfirmPasswordView.setBackground(backgroundDrawable);
        mPartnerCodeView.setBackground(backgroundDrawable);

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

        return mView;
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
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
                checkEmail();
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
            }
            else {
                mConfirmPasswordView.setError(getString(R.string.error_password_do_not_match));
                return true;
            }
        }
        if (v.getId() == R.id.partnerCode){
            RegistrationActivity activity = (RegistrationActivity)getActivity();
            activity.nextStep(null);
        }


        return false;
    }

    public boolean isConfirmPasswordValid(String confirmPassword)
    {
        if(mPasswordView.getText().toString().contentEquals(confirmPassword))
            return true;
        return false;
    }
    public void checkEmail()
    {
        String email = mEmailView.getText().toString();
        if(checkedEmail != email) {
            checkedEmail = email;
            isEmailAlreadyUse = true;
            HashMap<String, Object> parametrs = new HashMap<String, Object>();
            parametrs.put("email", email);
            AsyncHttpTask userLoginTask = new AsyncHttpTask(parametrs, AsyncMethodNames.CHECK_EMAIL, (Activity)mView.getContext());
            userLoginTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
                checkEmail();
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
        Pattern p = Pattern.compile("^[A-Za-z][A-Za-z]+\\s[A-Za-z][A-Za-z]+$");
        Matcher m = p.matcher(name);
        return m.find();
    }
    private boolean isEmailValid(String email) {
        Pattern p = Pattern.compile("^[A-Za-z0-9_\\.-]+(\\-[A-Za-z0-9_]-)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9])");
        Matcher m = p.matcher(email);
        return m.find();
    }

    private boolean isPasswordValid(String password) {
        Pattern p = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{8,}$");
        Matcher m = p.matcher(password);
        return m.find();
    }

    public void setEmailError(String error)
    {
        mEmailView.setError(error);
        mEmailView.requestFocus();
    }
    public String getEmail()
    {
        return mEmailView.getText().toString();
    }

    public String getName()
    {
        return mNameView.getText().toString();
    }

    public Map<String, Object> getData()
    {
        HashMap<String, Object> parametrs = new HashMap<String, Object>();
        parametrs.put("email", mEmailView.getText().toString());
        parametrs.put("password", mPasswordView.getText().toString());
        parametrs.put("language", "en");
        if(!mPartnerCodeView.getText().toString().contentEquals("")) {
            parametrs.put("partner_code", mPartnerCodeView.getText().toString());
        }
        return parametrs;
    }
}
