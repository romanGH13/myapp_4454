package com.example.eqvol.eqvola.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.eqvol.eqvola.fragments.Registration.FirstStepFragment;
import com.example.eqvol.eqvola.fragments.Registration.FourthStepFragment;
import com.example.eqvol.eqvola.fragments.Registration.SecondStepFragment;
import com.example.eqvol.eqvola.fragments.Registration.ThirdStepFragment;

import java.util.ArrayList;

/**
 * Created by eqvol on 22.12.2017.
 */

public class RegistrationPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments;
    private int countPages;
    public RegistrationPagerAdapter(FragmentManager mgr, ArrayList<Fragment> fragments) {
        super(mgr);
        countPages = 4;
        this.fragments = fragments;
    }
    @Override
    public int getCount() {
        return(countPages);
    }
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        fragment = fragments.get(position);
        return fragment;
    }
}