package com.example.eqvol.eqvola;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eqvol.eqvola.Classes.AsyncHttpTask;
import com.example.eqvol.eqvola.Classes.AsyncMethodNames;
import com.example.eqvol.eqvola.fragments.ModalAlert;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPasswordActivity extends AppCompatActivity implements TextView.OnEditorActionListener {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        Drawable backgroundDrawable = getResources().getDrawable(R.drawable.background_image);
        ((LinearLayout)findViewById(R.id.mainLayout)).setBackground(backgroundDrawable);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        mEmailView.setOnEditorActionListener(this);
        mEmailView.setImeActionLabel("Done", EditorInfo.IME_ACTION_DONE);

        //замена цвета для кнопки, что бы не копировать лишний раз Drawable для каждой кнопки
        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        Drawable drawable = getResources().getDrawable(R.drawable.rectangle);//btnLogin.getBackground();
        drawable.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        btnLogin.setBackground(drawable);
    }

    public void backToLogin(View v)
    {
        Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginActivity);
        finish();
    }

    public void requestRecoverPassword(View v)
    {
        String email = mEmailView.getText().toString();

        boolean isEmailValid = false;

        isEmailValid = isEmailValid(email);
        if(!isEmailValid)
        {
            mEmailView.requestFocus();
        }

        if(isEmailValid) {
            HashMap<String, Object> parametrs = new HashMap<String, Object>();
            parametrs.put("email", email);

            AsyncHttpTask userLoginTask = new AsyncHttpTask(parametrs, AsyncMethodNames.FORGOT_PASSWORD, this);
            userLoginTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    private boolean isEmailValid(String email)
    {
        boolean isValid = false;
        // Check for a valid email address.
        if(!isFieldEmpty(mEmailView)) {
            if (isEmailTextValid(mEmailView.getText().toString())) {
                isValid = true;
            } else {
                mEmailView.setError(getString(R.string.error_invalid_email));
            }
        }
        return isValid;
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

    private boolean isEmailTextValid(String email) {
        Pattern p = Pattern.compile("^[A-Za-z0-9_\\.-]+(\\-[A-Za-z0-9_]-)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9])");
        Matcher m = p.matcher(email);
        return m.find();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v.getId() == R.id.email){
            String email = mEmailView.getText().toString();
            if(isEmailValid(email))
            {
                return false;
            }
            else {
                mEmailView.requestFocus();
                return true;
            }
        }
        return false;
    }

    public void showDialog(boolean status, String description, Activity activity)
    {

        ModalAlert myDialogFragment = new ModalAlert(status, description, activity);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        myDialogFragment.show(transaction, "dialog");
    }
}
