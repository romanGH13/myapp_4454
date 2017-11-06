package com.example.eqvol.eqvola.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.eqvol.eqvola.R;

import java.util.List;


public class MyProgressBar extends Fragment {

    private View mProgressView;
    //private static Fragment fragment;


    public static MyProgressBar newInstance() {
        MyProgressBar pb = new MyProgressBar();
        //fragment = (Fragment) fragmentClass.newInstance();

        return pb;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mProgressView = inflater.inflate(R.layout.fragment_progress_bar, container, false);

        return mProgressView;
    }

    //@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    /*public void endLoad() throws IllegalAccessException, java.lang.InstantiationException {


        // Вставляем фрагмент, заменяя текущий фрагмент

        if(fragment.getClass() != SupportChat.class) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        } else {
            try {
                FragmentManager fragmentManager = getFragmentManager();
                String str = "";
            }
            catch(Exception e)
            {
                String str = e.getMessage();
            }
        }
        // Выделяем выбранный пункт меню в шторке
        //item.setChecked(true);
        // Выводим выбранный пункт в заголовке
        //setTitle(item.getTitle());
        /*try {
            mProgressView.setVisibility(show ? ProgressBar.VISIBLE : ProgressBar.INVISIBLE);
            mFormView.setVisibility(show ? ProgressBar.INVISIBLE : ProgressBar.VISIBLE);
        }
        catch(Exception ex){
            String str = ex.getMessage();
        }*/
        //}
    //}

    /*@Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }*/
}

