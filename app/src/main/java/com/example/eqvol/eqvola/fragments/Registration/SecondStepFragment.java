package com.example.eqvol.eqvola.fragments.Registration;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.eqvol.eqvola.Adapters.CountryAdapter;
import com.example.eqvol.eqvola.Classes.Api;
import com.example.eqvol.eqvola.Classes.AsyncHttpTask;
import com.example.eqvol.eqvola.Classes.AsyncMethodNames;
import com.example.eqvol.eqvola.Classes.Country;
import com.example.eqvol.eqvola.Classes.MyDateFormat;
import com.example.eqvol.eqvola.R;
import com.example.eqvol.eqvola.RegistrationActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SecondStepFragment extends Fragment implements TextView.OnEditorActionListener {
    private View mView;

    private EditText mPhoneCodeView;
    private EditText mPhoneNumberView;

    private Spinner mBirthDaySpinnerView;
    private Spinner mBirthMonthSpinnerView;
    private Spinner mBirthYearSpinnerView;
    private Spinner mCountrySpinnerView;

    private MyDateFormat mdt = new MyDateFormat();

    public SecondStepFragment() {
    }


    public static SecondStepFragment newInstance() {
        SecondStepFragment fragment = new SecondStepFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_second_step, container, false);

        mBirthDaySpinnerView = (Spinner) mView.findViewById(R.id.spinnerDayOfBirth);
        mBirthMonthSpinnerView = (Spinner) mView.findViewById(R.id.spinnerMonthOfBirth);
        mBirthYearSpinnerView = (Spinner) mView.findViewById(R.id.spinnerYearOfBirth);
        mCountrySpinnerView = (Spinner) mView.findViewById(R.id.spinnerCountries);

        mPhoneCodeView = (EditText) mView.findViewById(R.id.phoneCode);
        mPhoneNumberView = (EditText) mView.findViewById(R.id.phoneNumber);

        mPhoneNumberView = (EditText) mView.findViewById(R.id.phoneNumber);
        mPhoneNumberView.setOnEditorActionListener(this);
        mPhoneNumberView.setImeActionLabel("Next step", EditorInfo.IME_ACTION_DONE);

        setDateBirth();
        if(Api.countries != null)
        setSpinner();

        return mView;
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v.getId() == R.id.phoneNumber){
            return checkPhoneNumber();
        }
        return false;
    }

    public boolean checkPhoneNumber()
    {
        if (isPhoneNumberValid()) {
            RegistrationActivity act = (RegistrationActivity)getActivity();
            act.goToStep3();
            return false;
        }
        else {
            mPhoneNumberView.setError(getString(R.string.error_phone_number));
            mPhoneNumberView.requestFocus();
            return true;
        }
    }

    public boolean isPhoneNumberValid() {
        String password = mPhoneNumberView.getText().toString();
        Pattern p = Pattern.compile("^([0-9]).{8}$");
        Matcher m = p.matcher(password);
        return m.find();
    }

    public void setDateBirth()
    {
        Spinner spinDay = (Spinner)mView.findViewById(R.id.spinnerDayOfBirth);
        Spinner spinMonth = (Spinner)mView.findViewById(R.id.spinnerMonthOfBirth);
        Spinner spinYear = (Spinner)mView.findViewById(R.id.spinnerYearOfBirth);

        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                String year = ((Spinner)mView.findViewById(R.id.spinnerYearOfBirth)).getSelectedItem().toString();
                String month = ((Spinner)mView.findViewById(R.id.spinnerMonthOfBirth)).getSelectedItem().toString();
                Spinner spin = (Spinner)mView.findViewById(R.id.spinnerDayOfBirth);
                ArrayAdapter<String> adapter = (ArrayAdapter<String>)spin.getAdapter();
                adapter.clear();
                adapter.addAll(mdt.getDays(year, month));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        };

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        adapter2.addAll(mdt.getMonths());
        spinMonth.setAdapter(adapter2);
        spinMonth.setOnItemSelectedListener(listener);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        adapter3.addAll(mdt.getYears());
        spinYear.setAdapter(adapter3);
        spinYear.setOnItemSelectedListener(listener);

        String year = ((Spinner)mView.findViewById(R.id.spinnerYearOfBirth)).getSelectedItem().toString();
        String month = ((Spinner)mView.findViewById(R.id.spinnerMonthOfBirth)).getSelectedItem().toString();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        adapter.addAll(mdt.getDays(year, month));
        spinDay.setAdapter(adapter);

        selectValue(spinYear, "1990");
    }

    public  void setSpinner()
    {
        ArrayAdapter<Country> adapter = new CountryAdapter(getActivity(), Api.countries);
        adapter.setDropDownViewResource(R.layout.drop_down_item);

        Spinner spinner = (Spinner) mView.findViewById(R.id.spinnerCountries);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                Country country = (Country) parent.getItemAtPosition(pos);
                ((TextView) mView.findViewById(R.id.phoneCode)).setText(Integer.toString(country.getPhone_code()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    public static void selectValue(Spinner spinner, String value)
    {
        for (int i = 0; i < spinner.getCount(); i++)
        {
            if(spinner.getAdapter().getClass() == CountryAdapter.class)
            {
                if (((Country)spinner.getItemAtPosition(i)).getName().equals(value))
                {
                    spinner.setSelection(i);
                    break;
                }
            }
            else if ((spinner.getItemAtPosition(i)).toString().equals(value))
            {
                spinner.setSelection(i);
                break;
            }
        }
    }

    public Map<String, Object> getData()
    {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("birth_date", mBirthDaySpinnerView.getSelectedItem().toString() + "-" + mBirthMonthSpinnerView.getSelectedItem().toString() + "-" + mBirthYearSpinnerView.getSelectedItem().toString());
        data.put("country", ((Country)mCountrySpinnerView.getSelectedItem()).getName());
        data.put("phone_code", mPhoneCodeView.getText().toString());
        data.put("phone_number", mPhoneNumberView.getText().toString());
        return data;
    }
}