package com.example.eqvol.eqvola.Classes;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.example.eqvol.eqvola.fragments.MyProgressBar;
import com.example.eqvol.eqvola.fragments.SupportChat;

/**
 * Created by eqvol on 03.11.2017.
 */

public class FragmentLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    private boolean isDataLoaded;
    public Fragment fragment = null;
    private MyProgressBar myProgressBar;
    private FragmentManager fragmentManager;
    private int fragmentContainerId;

    public FragmentLoader(Class fragmentClass, FragmentManager fm, int id, boolean isDataLoaded)
    {
        this.isDataLoaded = isDataLoaded;
        this.fragmentContainerId = id;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        this.myProgressBar = MyProgressBar.newInstance();
        this.fragmentManager = fm;
    }

    public void startLoading()
    {
        fragmentManager.beginTransaction().replace(fragmentContainerId, myProgressBar).commit();
    }

    public void endLoading()
    {
        fragmentManager.beginTransaction().replace(fragmentContainerId, fragment).commit();
    }

    public MyProgressBar getProgressBar()
    {
        return this.myProgressBar;
    }

    public void setFragmentManager(FragmentManager fm)
    {
        this.fragmentManager = fm;
    }
    /*public MyProgressBar newInstance(Class fragmentClass) throws java.lang.InstantiationException, IllegalAccessException {
        MyProgressBar pb = new MyProgressBar();
        fragment = (Fragment) fragmentClass.newInstance();

        return pb;
    }*/



    /*//@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void endLoad() throws IllegalAccessException, java.lang.InstantiationException {


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
        try {
            mProgressView.setVisibility(show ? ProgressBar.VISIBLE : ProgressBar.INVISIBLE);
            mFormView.setVisibility(show ? ProgressBar.INVISIBLE : ProgressBar.VISIBLE);
        }
        catch(Exception ex){
            String str = ex.getMessage();
        }
        //}
    }*/

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
