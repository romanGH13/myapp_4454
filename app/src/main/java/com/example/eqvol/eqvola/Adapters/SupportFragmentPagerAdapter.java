package com.example.eqvol.eqvola.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;

import com.example.eqvol.eqvola.Classes.Api;
import com.example.eqvol.eqvola.Classes.FragmentLoader;
import com.example.eqvol.eqvola.R;
import com.example.eqvol.eqvola.fragments.MyProgressBar;
import com.example.eqvol.eqvola.fragments.SupportChat;
import com.example.eqvol.eqvola.fragments.SupportCreateTicket;

import java.util.List;

/**
 * Created by eqvol on 27.10.2017.
 */

public class SupportFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private static final int PAGE_COUNT = 2;
    public FragmentManager fragmentManager;
    private List<Fragment> fragments;
    public SupportFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        fragmentManager = fm;
        this.fragments = fragments;
    }

    public void replaceFragment(int position, Fragment newFragment)
    {
        fragments.set(position, newFragment);
    }

    @Override
    public int getItemPosition(Object object) {
        for (int i = 0; i < getCount(); i++)
        {
            if(fragments.get(i).equals(object))
                return i;
        }
        return -1;
    }

    @Override
    public Fragment getItem(int position) {

        return fragments.get(position);

        /*if(position==0)
            return SupportCreateTicket.newInstance();
        else {
            //return SupportChat.newInstance();
            FragmentManager f1 = ((AppCompatActivity)context).getSupportFragmentManager();
            Api.chatLoader = new FragmentLoader(SupportChat.class, f1, R.id.progress_bar, false);
            //fl.startLoading();
            return Api.chatLoader.getProgressBar();
            /*try {
                return SupportChat.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        //}
        //return null;*/
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Fragment fragment = getItem(position);
        if(fragment.getClass() == SupportCreateTicket.class)
        {
            return "Create ticket";
        }
        else
        {
            return "Chat";
        }

    }
}