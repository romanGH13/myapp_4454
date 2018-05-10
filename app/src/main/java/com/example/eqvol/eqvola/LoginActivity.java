package com.example.eqvol.eqvola;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
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

import com.example.eqvol.eqvola.Classes.Api;
import com.example.eqvol.eqvola.Classes.AsyncHttpTask;
import com.example.eqvol.eqvola.Classes.AsyncMethodNames;
import com.example.eqvol.eqvola.fragments.ModalAlert;
import com.example.eqvol.eqvola.fragments.ModalApproveEmail;
import com.example.eqvol.eqvola.fragments.ModalInputAuthCode;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements TextView.OnEditorActionListener{

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private ModalApproveEmail modalApproveEmail;
    private ModalInputAuthCode modalInputAuthCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Api.context = getApplicationContext();
        setContentView(R.layout.activity_login);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        Drawable backgroundDrawable = getResources().getDrawable(R.drawable.background_image);
        ((LinearLayout)findViewById(R.id.mainLayout)).setBackground(backgroundDrawable);

        // получение всех графических элементов
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        mEmailView.setOnEditorActionListener(this);
        mEmailView.setImeActionLabel("Next", EditorInfo.IME_ACTION_NEXT);
        mPasswordView.setOnEditorActionListener(this);
        mPasswordView.setImeActionLabel("Sign In", EditorInfo.IME_ACTION_DONE);

        //замена цвета для кнопки, что бы не копировать лишний раз Drawable для каждой кнопки
        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        Drawable drawable = getResources().getDrawable(R.drawable.rectangle);
        drawable.setColorFilter(getResources().getColor(R.color.colorBlue), PorterDuff.Mode.MULTIPLY);
        btnLogin.setBackground(drawable);
        //проверка токена, если токен уже есть приложение само авторизирует пользователя.
        checkTokenInFile();
    }

    //получение токена из файла и попытка авторизации через токен
    public void checkTokenInFile(){

        FileInputStream fin = null;
        String fileContent = null;
        try {
            fin = openFileInput(Api.FILENAME);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            fileContent = new String (bytes);
        }
        catch(IOException ex) { }
        finally{
            try{
                if(fin!=null)
                    fin.close();
            }
            catch(IOException ex){ }
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

    //функция для сохранения токена при авторизации
    public void saveTokenInFile(String token){

        FileOutputStream fos = null;
        try {
            fos = openFileOutput(Api.FILENAME, MODE_PRIVATE);
            String str = "token:" + token;
            fos.write(str.getBytes());
        }
        catch(IOException ex) { }
        finally{
            try{
                if(fos!=null)
                    fos.close();
            }
            catch(IOException ex){ }
        }
    }

    // переход на экран восстановления пароля
    public void forgotPassword(View view)
    {
        Intent SecAct = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
        startActivity(SecAct);
        finish();
    }

    /*
    * переход на главный экран приложения, после авторизации
    * */
    public void goToMenuActivity()
    {
        Intent MenuActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(MenuActivity);

        finish();
    }

    /**
     * Метод для авторизации пользователя в кабинете
     */
    public void attemptLogin(View view) {
        if (Api.user != null) {
            return;
        }

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean isEmailValid = false;
        boolean isPasswordValid = false;

        isEmailValid = isEmailValid();
        if(!isEmailValid)
        {
            mEmailView.requestFocus();
        }
        isPasswordValid = isPasswordValid();
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

    /**
     * Метод для авторизации пользователя в кабинете с кодом двухэтапной аутентификации
     */
    public void attemptLoginWithAuthCode(String code) {
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        showProgress(true);
        HashMap<String, Object> parametrs = new HashMap<String, Object>();
        parametrs.put("email", email);
        parametrs.put("password", password);
        parametrs.put("code", code);

        AsyncHttpTask userLoginWithAuthTask = new AsyncHttpTask(parametrs, AsyncMethodNames.USER_LOGIN, this);
        userLoginWithAuthTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    //метод для перехода на экран регистрации
    public void toRegistr(View view) {
        Intent SecAct = new Intent(getApplicationContext(), RegistrationActivity.class);
        startActivity(SecAct);
        finish();
    }

    private boolean isEmailValid()
    {
        boolean isValid = false;
        if(!isFieldEmpty(mEmailView)) {
            if (isEmailTextValid(mEmailView.getText().toString())) {
                isValid = true;
            } else {
                mEmailView.setError(getString(R.string.error_invalid_email));
            }
        }
        return isValid;
    }

    private boolean isPasswordValid()
    {
        boolean isValid = false;
        if(!isFieldEmpty(mPasswordView)) {
           if (isPasswordTextValid(mPasswordView.getText().toString())) {
               isValid = true;
            } else {
               mPasswordView.setError(getString(R.string.error_invalid_password));
           }
        }
        return isValid;
    }

    /*
    * функция для проверки наличия данных в поле
    * */
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

    private boolean isPasswordTextValid(String password) {
        Pattern p = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{8,}$");
        Matcher m = p.matcher(password);
        return m.find();
    }

    /**
     * показывает прогресс выполнение и прячем форму авторизации
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
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
    }

    /**
     * обрабатывает переходы от поля к полю
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v.getId() == R.id.email){
            if(isEmailValid())
            {
                return false;
            }
            else {
                mEmailView.requestFocus();
                return true;
            }
        }
        else if (v.getId() == R.id.password){
            if (isPasswordValid()) {
                attemptLogin(mPasswordView);
            }
            else {
                mPasswordView.requestFocus();
                return true;
            }
        }
        return false;
    }

    /*
    * функция для отображения модального окна, в котором можно ввести код подвтержение с почты
    * */
    public void inputCodeDialog(String description)
    {
        modalApproveEmail = ModalApproveEmail.newInstance(description, this);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        modalApproveEmail.setCancelable(false);
        modalApproveEmail.show(transaction, "dialog");
    }

    /*
     * функция для отображения модального окна, в котором нужно ввести код двухэтапной аутентификации
     * */
    public void twoStepDialog(String description)
    {
        modalInputAuthCode = ModalInputAuthCode.newInstance(description, this);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        modalInputAuthCode.setCancelable(false);
        modalInputAuthCode.show(transaction, "dialog");
    }

    public void emailApproved()
    {
        modalApproveEmail.closeDialog();
        showDialog(true, "Your email has been approved!");
    }

    public void wrongApproveCode()
    {
        modalApproveEmail.showError("Wrong approve code");
    }

    public void wrongTwoStepCode()
    {
        modalInputAuthCode.showError("Wrong code");
    }

    /*
     * функция для подтвержения почты
     * */
    public void approveEmail(String code)
    {
        Map<String, Object> parametrs = new HashMap<String, Object>();
        parametrs.put("email_code", code);
        AsyncHttpTask userLoginTask = new AsyncHttpTask(parametrs, AsyncMethodNames.APPROVE_EMAIL, this);
        userLoginTask.execute();
    }

    /*
    * функция для отображения диалога со статусом выполненой операции
    * */
    public void showDialog(boolean status, String description)
    {
        ModalAlert myDialogFragment = ModalAlert.newInstance(status, description);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        myDialogFragment.show(transaction, "dialog");
    }
}

