package com.example.eqvol.eqvola.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eqvol.eqvola.Classes.Api;
import com.example.eqvol.eqvola.Classes.AsyncHttpTask;
import com.example.eqvol.eqvola.Classes.AsyncMethodNames;
import com.example.eqvol.eqvola.Classes.FragmentLoader;
import com.example.eqvol.eqvola.MainActivity;
import com.example.eqvol.eqvola.R;

import java.util.HashMap;


public class MenuFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener  {

    private View mView;

    public MenuFragment() {

        HashMap<String, Object> parametrs = new HashMap<String, Object>();
        parametrs.put("user", Api.user);
        AsyncHttpTask getUserAvatarTask = new AsyncHttpTask(parametrs, AsyncMethodNames.GET_USER_AVATAR, getActivity());
        getUserAvatarTask.target = MenuFragment.class.toString();
        getUserAvatarTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static MenuFragment newInstance() {
        MenuFragment fragment = new MenuFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_menu, container, false);
        setNavHeaderData();
        return mView;
    }

    public void setNavHeaderData()
    {
        if(Api.user == null) return;

        NavigationView navigationView = (NavigationView) mView.findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        TextView headerName = (TextView)header.findViewById(R.id.nav_header_name);
        TextView headerEmail = (TextView)header.findViewById(R.id.nav_header_email);
        headerName.setText(Api.user.getName());
        headerEmail.setText(Api.user.getEmail());

        ImageButton btn = (ImageButton)header.findViewById(R.id.btnEditProfile);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditProfileClick(v);
            }
        });

        ImageView img = (ImageView) header.findViewById(R.id.imageView);
        if(Api.user.getAvatar() != null) {
            byte[] data = Api.user.getAvatar();
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            img.setImageBitmap(bitmap);
        }

        //NavigationView navigationView = (NavigationView) mView.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        MainActivity activity = (MainActivity)getActivity();
        activity.mOnNavigationItemSelectedListener.onNavigationItemSelected(item);
        BottomNavigationView navigation = (BottomNavigationView) activity.findViewById(R.id.navigation);
        navigation.setSelectedItemId(item.getItemId());
        return true;
    }

    public MenuItem getItemByName(String name)
    {
        NavigationView navigationView = (NavigationView) mView.findViewById(R.id.nav_view);
        int itemCount = navigationView.getChildCount();
        Menu menu = navigationView.getMenu();
        for(int i = 0; i < itemCount; i++)
        {
            MenuItem item = menu.getItem(i);
            String title = item.getTitle().toString();
            String str = "";
        }
        return null;
    }

    public void onEditProfileClick(View view)
    {
        FragmentLoader fl = new FragmentLoader(UserPageFragment.class, ((MainActivity)(mView.getContext())).getSupportFragmentManager(), R.id.container, false);
        fl.startLoading();
        MainActivity.currentLoader = fl;
    }

}
