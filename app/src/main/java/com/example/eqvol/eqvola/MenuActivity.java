package com.example.eqvol.eqvola;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.eqvol.eqvola.Classes.Api;
import com.example.eqvol.eqvola.Classes.AsyncHttpTask;
import com.example.eqvol.eqvola.Classes.AsyncMethodNames;
import com.example.eqvol.eqvola.Classes.FragmentLoader;
import com.example.eqvol.eqvola.fragments.CreateAccount;
import com.example.eqvol.eqvola.fragments.MyAccounts;
import com.example.eqvol.eqvola.fragments.MyProgressBar;
import com.example.eqvol.eqvola.fragments.Support;
import com.example.eqvol.eqvola.fragments.UserPageFragment;

import java.io.File;
import java.util.HashMap;


public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static boolean isLoading = false;
    public static FragmentLoader currentLoader = null;
    //public static MyProgressBar fragment = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*Fragment fragment = null;
        Class fragmentClass = null;
        fragmentClass = UserPageFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();*/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setNavHeaderData();
    }
    public void setNavHeaderData()
    {
        if(Api.user == null) return;

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        TextView headerName = (TextView)header.findViewById(R.id.nav_header_name);
        TextView headerEmail = (TextView)header.findViewById(R.id.nav_header_email);
        headerName.setText(Api.user.getName());
        headerEmail.setText(Api.user.getEmail());

        HashMap<String, Object> parametrs = new HashMap<String, Object>();
        parametrs.put("user", Api.user);
        AsyncHttpTask getUserTask = new AsyncHttpTask(parametrs, AsyncMethodNames.GET_USER_AVATAR, this);
        getUserTask.execute();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        //Fragment fragment = null;
        Class fragmentClass = null;

        if (id == R.id.nav_user_page) {
            fragmentClass = UserPageFragment.class;

        } else if (id == R.id.nav_create_account) {
            //tv.setText("user nav_share");
            fragmentClass = CreateAccount.class;
        } else if (id == R.id.nav_my_accounts) {
            fragmentClass = MyAccounts.class;
        } else if (id == R.id.nav_support) {
            fragmentClass = Support.class;
        } else if (id == R.id.nav_logout) {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("token", Api.getToken());

            AsyncHttpTask userLoginTask = new AsyncHttpTask(params, AsyncMethodNames.USER_LOGOUT, this);
            userLoginTask.execute();
            deleteFileToken();

            Api.setToken(null);
            Api.user = null;
            Api.countries = null;
            goToLoginActivity();
        }

        try {
            if(fragmentClass != null) {
                FragmentLoader fl = new FragmentLoader(fragmentClass, getSupportFragmentManager(), R.id.container, false);
                fl.startLoading();
                currentLoader = fl;
                /*fragment = MyProgressBar.newInstance(fragmentClass);
                // Вставляем фрагмент, заменяя текущий фрагмент
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                isLoading = true;
                // Выделяем выбранный пункт меню в шторке
                item.setChecked(true);
                // Выводим выбранный пункт в заголовке
                //setTitle(item.getTitle());*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void deleteFileToken()
    {
        File file = new File(getFilesDir(), Api.FILENAME);
        file.delete();
    }

    private void goToLoginActivity()
    {
        Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginActivity);
        finish();
    }
}
