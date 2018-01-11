package com.example.eqvol.eqvola;

import android.app.ActivityManager;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.eqvol.eqvola.Classes.*;
import com.example.eqvol.eqvola.Classes.Api;
import com.example.eqvol.eqvola.Notifications.MyService;
import com.example.eqvol.eqvola.fragments.CreateAccount;
import com.example.eqvol.eqvola.fragments.FinanceHistoryFragment;
import com.example.eqvol.eqvola.fragments.FinanceOperationFragment;
import com.example.eqvol.eqvola.fragments.MenuFragment;
import com.example.eqvol.eqvola.fragments.ModalAlert;
import com.example.eqvol.eqvola.fragments.MyAccounts;
import com.example.eqvol.eqvola.fragments.Support;
import com.example.eqvol.eqvola.fragments.TransfersFragment;

import java.io.File;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private String currentItem;
    public static FragmentLoader currentLoader = null;

    public BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if(currentItem == item.toString())
                return false;
            currentItem = item.toString();
            Class fragmentClass = null;
            switch (item.getItemId()) {
                case R.id.navigation_my_accounts:
                    fragmentClass = MyAccounts.class;
                    break;
                case R.id.navigation_finance_history:
                    fragmentClass = FinanceHistoryFragment.class;
                    break;
                case R.id.navigation_support:
                    fragmentClass = Support.class;
                    break;
                case R.id.navigation_create_account:
                    fragmentClass = CreateAccount.class;
                    break;
                case R.id.navigation_menu:
                    fragmentClass = MenuFragment.class;
                    break;
                case R.id.navigation_transfers:
                    fragmentClass = TransfersFragment.class;
                    break;
                case R.id.nav_logout:

                    HashMap<String, Object> params = new HashMap<String, Object>();
                    params.put("token", Api.getToken());

                    AsyncHttpTask userLogoutTask = new AsyncHttpTask(params, AsyncMethodNames.USER_LOGOUT, getParent());
                    userLogoutTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                    deleteFileToken();

                    Api.setToken(null);
                    Api.user = null;
                    Api.countries = null;

                    goToLoginActivity();

                    break;

            }

            try {
                if(fragmentClass != null) {
                    FragmentLoader fl = new FragmentLoader(fragmentClass, getSupportFragmentManager(), R.id.container, false);
                    fl.startLoading();
                    currentLoader = fl;
                    /*if(item.getItemId() == R.id.navigation_transfers)
                        fl.endLoading();*/

                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    };

    public void openMyAccounts()
    {
        FragmentLoader fl = new FragmentLoader(MyAccounts.class, getSupportFragmentManager(), R.id.container, false);
        fl.startLoading();
        currentLoader = fl;
    }

    public void openFinanceHistory()
    {
        FragmentLoader fl = new FragmentLoader(FinanceHistoryFragment.class, getSupportFragmentManager(), R.id.container, false);
        fl.startLoading();
        currentLoader = fl;
    }

    public void openFinanceOperation(Withdrawal operation)
    {
        Api.currentOperation = operation;
        FragmentLoader fl = new FragmentLoader(FinanceOperationFragment.class, getSupportFragmentManager(), R.id.container, false);
        fl.startLoading();
        fl.endLoading();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentItem = "";

        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_my_accounts);

        if(!isMyServiceRunning(MyService.class)) {
            Intent intentMyIntentService = new Intent(this, MyService.class);
            intentMyIntentService.putExtra("user_id", Api.user.getId());
            intentMyIntentService.putExtra("token", Api.getToken());
            startService(intentMyIntentService);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }



    public void deleteFileToken()
    {
        File file = new File(getFilesDir(), Api.FILENAME);
        file.delete();
    }

    public void goToLoginActivity()
    {
        Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginActivity);
        finish();
    }

    public void showDialog(boolean status, String description)
    {
        ModalAlert myDialogFragment = new ModalAlert(status, description);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        myDialogFragment.show(transaction, "dialog");
    }

}
