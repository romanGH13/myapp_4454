package com.example.eqvol.eqvola.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.eqvol.eqvola.fragments.CreateOrderFragment;
import com.example.eqvol.eqvola.fragments.DepositsRecyclerFragment;
import com.example.eqvol.eqvola.fragments.OpenOrdersForAccountFragment;
import com.example.eqvol.eqvola.fragments.OpenOrdersFragment;
import com.example.eqvol.eqvola.fragments.QuotationsFragment;
import com.example.eqvol.eqvola.fragments.RequestTransferFragment;
import com.example.eqvol.eqvola.fragments.SupportChat;
import com.example.eqvol.eqvola.fragments.SupportCreateTicket;
import com.example.eqvol.eqvola.fragments.TradingHistoryFragment;
import com.example.eqvol.eqvola.fragments.TransfersHistoryFragment;
import com.example.eqvol.eqvola.fragments.WithdrawalsRecyclerFragment;

import java.util.List;

/**
 * Created by eqvol on 18.12.2017.
 */

public class MyPagerAdapter extends FragmentStatePagerAdapter {


    public FragmentManager fragmentManager;
    private List<Fragment> fragments;

    public MyPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        fragmentManager = fm;
        this.fragments = fragments;
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
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Fragment fragment = getItem(position);

        if(fragment.getClass() == OpenOrdersForAccountFragment.class || fragment.getClass() == OpenOrdersFragment.class )
        {
            return "Open Orders";
        }
        else if(fragment.getClass() == TradingHistoryFragment.class)
        {
            return "Trading History";
        }
        else if(fragment.getClass() == DepositsRecyclerFragment.class)
        {
            return "Deposits";
        }
        else if(fragment.getClass() == WithdrawalsRecyclerFragment.class)
        {
            return "Withdrawals";
        }
        else if(fragment.getClass() == RequestTransferFragment.class)
        {
            return "Request";
        }
        else if(fragment.getClass() == TransfersHistoryFragment.class)
        {
            return "History";
        }
        else if(fragment.getClass() == SupportCreateTicket.class)
        {
            return "Create ticket";
        }
        else if(fragment.getClass() == SupportChat.class)
        {
            return "Chat";
        }
        else if(fragment.getClass() == QuotationsFragment.class)
        {
            return "Quotations";
        }
        else if(fragment.getClass() == CreateOrderFragment.class)
        {
            return "Create order";
        }

        return "";
    }
}