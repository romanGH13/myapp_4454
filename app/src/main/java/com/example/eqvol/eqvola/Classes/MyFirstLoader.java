package com.example.eqvol.eqvola.Classes;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.eqvol.eqvola.JsonResponse.JsonResponceCountries;
import com.example.eqvol.eqvola.MenuActivity;

/**
 * Created by eqvol on 06.12.2017.
 */

public class MyFirstLoader extends AsyncTaskLoader<String> {
    public final String TAG = getClass().getSimpleName();

    public MyFirstLoader(Context context) {
        super(context);
    }

    @Override
    public String loadInBackground() {
        return Api.getCountries();
    }

    @Override
    public void forceLoad() {
        Log.d(TAG, "forceLoad");
        super.forceLoad();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        Log.d(TAG, "onStartLoading");
        forceLoad();
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        Log.d(TAG, "onStopLoading");
    }

    @Override
    public void deliverResult(String data) {
        Log.d(TAG, "deliverResult");
        super.deliverResult(data);
        Api.countries = JsonResponceCountries.getCountries(data);
        MenuActivity.currentLoader.endLoading();
    }
}
