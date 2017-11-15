package com.example.eqvol.eqvola;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.example.eqvol.eqvola.Classes.Api;
import com.example.eqvol.eqvola.Classes.AsyncHttpTask;
import com.example.eqvol.eqvola.Classes.AsyncMethodNames;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements TextView.OnEditorActionListener, LoaderCallbacks<Cursor>{

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    public TextView mLabelView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mLabelView = (TextView) findViewById(R.id.textComment);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        mEmailView.setOnEditorActionListener(this);
        mEmailView.setImeActionLabel("Next", EditorInfo.IME_ACTION_NEXT);
        mPasswordView.setOnEditorActionListener(this);
        mPasswordView.setImeActionLabel("Sign In", EditorInfo.IME_ACTION_DONE);

        //checkTokenInFile();
    }

    public void checkTokenInFile(){

        FileInputStream fin = null;
        String fileContent = null;
        try {
            fin = openFileInput(Api.FILENAME);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            fileContent = new String (bytes);
        }
        catch(IOException ex) {
            //Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally{
            try{
                if(fin!=null)
                    fin.close();
            }
            catch(IOException ex){
                //Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        if(fileContent != null) {
            String[] arr = fileContent.split(":");
            if(arr[0].contentEquals("token")){
                String token = arr[1];
                Api.setToken(token);
                showProgress(true);
                HashMap<String, Object> parametrs = new HashMap<String, Object>();
                parametrs.put("token", token);
                AsyncHttpTask ckeckTokenTask = new AsyncHttpTask(parametrs, AsyncMethodNames.CHECK_TOKEN, this);
                ckeckTokenTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
    }

    public void saveTokenInFile(String token){

        FileOutputStream fos = null;
        try {
            fos = openFileOutput(Api.FILENAME, MODE_PRIVATE);
            String str = "token:" + token;
            fos.write(str.getBytes());
        }
        catch(IOException ex) {
            //Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally{
            try{
                if(fos!=null)
                    fos.close();
            }
            catch(IOException ex){
                //Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void goToMenuActivity()
    {
        Intent MenuActivity = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(MenuActivity);
        finish();
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
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin(View view) {
        if (Api.user != null) {
            return;
        }

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean isEmailValid = false;
        boolean isPasswordValid = false;

        isEmailValid = isEmailValid(email);
        if(!isEmailValid)
        {
            mEmailView.requestFocus();
        }
        isPasswordValid = isPasswordValid(password);
        if (!isPasswordValid) {
            mPasswordView.requestFocus();
        }

        if(isEmailValid && isPasswordValid){
            showProgress(true);
            HashMap<String, Object> parametrs = new HashMap<String, Object>();
            parametrs.put("email", email);
            parametrs.put("password", password);

            AsyncHttpTask userLoginTask = new AsyncHttpTask(parametrs, AsyncMethodNames.USER_LOGIN, this);
            userLoginTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    public void toRegistr(View view) {
        Intent SecAct = new Intent(getApplicationContext(), RegistrationActivity.class);
        startActivity(SecAct);
        finish();
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

    private boolean isPasswordValid(String password)
    {
        boolean isValid = false;
        // Check for a valid email address.
        if(!isFieldEmpty(mPasswordView)) {
           if (isPasswordTextValid(mPasswordView.getText().toString())) {
               isValid = true;
            } else {
               mPasswordView.setError(getString(R.string.error_invalid_password));
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
        Pattern p = Pattern.compile("^[A-Za-z0-9-]+(\\-[A-Za-z0-9])*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9])");
        Matcher m = p.matcher(email);
        return m.find();
    }

    private boolean isPasswordTextValid(String password) {
        Pattern p = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$");
        Matcher m = p.matcher(password);
        return m.find();
    }


    /**
     * Shows the progress UI and hides the login form.
     */
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
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
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
        else if (v.getId() == R.id.password){
            String password = mPasswordView.getText().toString();
            if (isPasswordValid(password)) {
                attemptLogin(mPasswordView);
            }
            else {
                mPasswordView.requestFocus();
                return true;
            }
        }
        return false;
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };
        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

}

