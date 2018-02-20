package com.example.eqvol.eqvola;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Fragment;
import android.app.Service;
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
import android.widget.Toast;

import com.example.eqvol.eqvola.Classes.*;
import com.example.eqvol.eqvola.Classes.Api;
import com.example.eqvol.eqvola.Notifications.MyService;
import com.example.eqvol.eqvola.fragments.AccountOrdersFragment;
import com.example.eqvol.eqvola.fragments.CreateAccount;
import com.example.eqvol.eqvola.fragments.FinanceHistoryFragment;
import com.example.eqvol.eqvola.fragments.FinanceOperationFragment;
import com.example.eqvol.eqvola.fragments.MenuFragment;
import com.example.eqvol.eqvola.fragments.MobileTraderFragment;
import com.example.eqvol.eqvola.fragments.ModalAlert;
import com.example.eqvol.eqvola.fragments.MyAccounts;
import com.example.eqvol.eqvola.fragments.OrderFragment;
import com.example.eqvol.eqvola.fragments.Support;
import com.example.eqvol.eqvola.fragments.TransfersFragment;
import com.example.eqvol.eqvola.fragments.UserPageFragment;


import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    public static String currentItem = "";
    public static FragmentLoader currentLoader = null;
    private static long back_pressed;
    private Intent intentMyIntentService;
    private MyService myService;

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
                case R.id.navigation_trader:
                    fragmentClass = MobileTraderFragment.class;
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

                    try {
                        myService.stopSelf();
                    } catch (Exception ex) {

                    }
                    goToLoginActivity();

                    break;

            }

            try {
                if(fragmentClass != null) {
                    FragmentLoader fl = new FragmentLoader(fragmentClass, getSupportFragmentManager(), R.id.container, false);
                    fl.startLoading();
                    currentLoader = fl;

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

    public void openOrderInfo(Order order)
    {
        Api.currentOrder = order;
        FragmentLoader fl = new FragmentLoader(OrderFragment.class, getSupportFragmentManager(), R.id.container, false);
        fl.startLoading();
        fl.endLoading();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Api.user == null)
        {
            goToLoginActivity();
        }
        setContentView(R.layout.activity_main);
        currentItem = "";

        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_my_accounts);

        if(!isMyServiceRunning(MyService.class)) {
            myService = new MyService();
            intentMyIntentService = new Intent(this, myService.getClass());
            intentMyIntentService.putExtra("user_id", Api.user.getId());
            intentMyIntentService.putExtra("token", Api.getToken());
            startService(intentMyIntentService);

        }

    }



    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        if(Api.user == null)
        {
            try {
                goToLoginActivity();
            } catch (Exception ex)
            {

            }
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

    public void showDialog(boolean status, String description, Activity activity, String target)
    {
        ModalAlert myDialogFragment = new ModalAlert(status, description, activity, target);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        myDialogFragment.setCancelable(false);
        myDialogFragment.show(transaction, "dialog");
    }

    @Override
    public void onBackPressed() {

        if( MainActivity.currentItem.contentEquals(AccountOrdersFragment.class.toString()))
        {
            FragmentLoader fl = new FragmentLoader(MyAccounts.class, getSupportFragmentManager(), R.id.container, false);
            fl.startLoading();
            currentLoader = fl;
            currentItem = Integer.toString(R.id.navigation_my_accounts);
        }
        else if (MainActivity.currentItem.contentEquals(UserPageFragment.class.toString())) {
            FragmentLoader fl = new FragmentLoader(MenuFragment.class, getSupportFragmentManager(), R.id.container, false);
            fl.startLoading();
            currentLoader = fl;
            currentItem = Integer.toString(R.id.navigation_menu);
        }
        else {
            if (back_pressed + 2000 > System.currentTimeMillis())
                super.onBackPressed();
            else
                Toast.makeText(getApplicationContext(), "Press once again to exit!",
                        Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }
    }
}
