package com.example.eqvol.eqvola.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eqvol.eqvola.R;

public class MyProgressBar extends Fragment {

    private View mProgressView;


    public static MyProgressBar newInstance() {
        MyProgressBar pb = new MyProgressBar();
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
}

