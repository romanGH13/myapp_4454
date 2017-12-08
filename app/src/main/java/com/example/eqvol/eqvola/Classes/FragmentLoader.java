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

public class FragmentLoader {
    public Fragment fragment = null;
    private MyProgressBar myProgressBar;
    private FragmentManager fragmentManager;
    private int fragmentContainerId;

    public FragmentLoader(Class fragmentClass, FragmentManager fm, int id, boolean isDataLoaded)
    {
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

    public void closeLoading()
    {
        //this.myProgressBar.getFragmentManager()
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

}
