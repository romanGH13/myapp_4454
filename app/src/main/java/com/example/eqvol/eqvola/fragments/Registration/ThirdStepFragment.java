package com.example.eqvol.eqvola.fragments.Registration;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.eqvol.eqvola.Classes.Country;
import com.example.eqvol.eqvola.R;

import java.util.HashMap;
import java.util.Map;

public class ThirdStepFragment extends Fragment implements TextView.OnEditorActionListener {
    private View mView;

    private EditText mStreetView;
    private EditText mCityView;
    private EditText mStateView;
    private EditText mPostalCodeView;

    private Drawable backgroundDrawable;

    public ThirdStepFragment() {
    }


    public static ThirdStepFragment newInstance() {
        ThirdStepFragment fragment = new ThirdStepFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        backgroundDrawable = getResources().getDrawable(R.drawable.rectangle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_third_step, container, false);

        mStreetView = (EditText) mView.findViewById(R.id.street);
        mCityView = (EditText) mView.findViewById(R.id.city);
        mStateView = (EditText) mView.findViewById(R.id.state);
        mPostalCodeView = (EditText) mView.findViewById(R.id.postalCode);

        mStreetView.setBackground(backgroundDrawable);
        mCityView.setBackground(backgroundDrawable);
        mStateView.setBackground(backgroundDrawable);
        mPostalCodeView.setBackground(backgroundDrawable);

        mStreetView.setOnEditorActionListener(this);
        mStreetView.setImeActionLabel("Next", EditorInfo.IME_ACTION_NEXT);
        mCityView.setOnEditorActionListener(this);
        mCityView.setImeActionLabel("Next", EditorInfo.IME_ACTION_NEXT);
        mStateView.setOnEditorActionListener(this);
        mStateView.setImeActionLabel("Next", EditorInfo.IME_ACTION_NEXT);
        mPostalCodeView.setOnEditorActionListener(this);
        mPostalCodeView.setImeActionLabel("Next step", EditorInfo.IME_ACTION_DONE);
        return mView;

    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return false;
    }

    public Map<String, Object> getData()
    {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("city", mCityView.getText().toString());
        data.put("state", mStateView.getText().toString());
        data.put("street", mStreetView.getText().toString());
        data.put("postal_code", mPostalCodeView.getText().toString());
        return data;
    }
}
