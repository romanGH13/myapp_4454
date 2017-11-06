package com.example.eqvol.eqvola.fragments;

/**
 * Created by eqvol on 19.10.2017.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.eqvol.eqvola.Classes.Api;
import com.example.eqvol.eqvola.Classes.AsyncHttpTask;
import com.example.eqvol.eqvola.Classes.Country;
import com.example.eqvol.eqvola.Adapters.CountryAdapter;
import com.example.eqvol.eqvola.Classes.MyDateFormat;
import com.example.eqvol.eqvola.R;
import com.example.eqvol.eqvola.Classes.AsyncMethodNames;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

public class UserPageFragment extends Fragment {

    private static View mView = null;
    private MyDateFormat mdt = new MyDateFormat();

    public UserPageFragment()
    {
        try {
            getAllCountries();
        }
        catch(Exception ex)
        {
            String str = ex.getMessage();
        }
    }


    public static UserPageFragment newInstance() {
        UserPageFragment fragment = new UserPageFragment();

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_user_page, container, false);
        ((TextView) mView.findViewById(R.id.user_page_name)).setText(Api.user.getName());
        ((TextView) mView.findViewById(R.id.user_page_phone_code)).setText(Integer.toString(Api.user.getPhone_code()));
        ((TextView) mView.findViewById(R.id.user_page_phone_number)).setText(Integer.toString(Api.user.getPhone_number()));
        ((TextView) mView.findViewById(R.id.user_page_city)).setText(Api.user.getCity());
        ((TextView) mView.findViewById(R.id.user_page_state)).setText(Api.user.getState());
        ((TextView) mView.findViewById(R.id.user_page_street)).setText(Api.user.getStreet());
        ((TextView) mView.findViewById(R.id.user_page_postal_code)).setText(Integer.toString(Api.user.getPostal_code()));




        setDateBirth();
        setSpinner();
        //getAllCountries();

        Button btn = (Button)mView.findViewById(R.id.btnSave);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new GsonBuilder().create();

                String name = ((TextView) mView.findViewById(R.id.user_page_name)).getText().toString();
                Spinner spinCountry = (Spinner) mView.findViewById(R.id.spinnerCountries);
                String country = ((Country)(spinCountry.getSelectedItem())).getName();
                String city = ((TextView) mView.findViewById(R.id.user_page_city)).getText().toString();
                String state = ((TextView) mView.findViewById(R.id.user_page_state)).getText().toString();
                String street = ((TextView) mView.findViewById(R.id.user_page_street)).getText().toString();
                int phone_code = Integer.parseInt(((TextView) mView.findViewById(R.id.user_page_phone_code)).getText().toString());
                String phone_number = ((TextView) mView.findViewById(R.id.user_page_phone_number)).getText().toString();
                String postal_code = ((TextView) mView.findViewById(R.id.user_page_postal_code)).getText().toString();

                HashMap<String, Object> mapUserId = new HashMap<String, Object>();
                mapUserId.put("id", Api.user.getId());
                mapUserId.put("name", name);
                mapUserId.put("country", country);
                mapUserId.put("city", city);
                mapUserId.put("state", state);
                mapUserId.put("street", street);
                mapUserId.put("postal_code", postal_code);
                mapUserId.put("phone_code", phone_code);
                mapUserId.put("phone_number", phone_number);
                String json = gson.toJson(mapUserId);

                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("token", Api.getToken());
                params.put("data", json);

                AsyncHttpTask userLoginTask = new AsyncHttpTask(params, AsyncMethodNames.SET_USER, getActivity());
                userLoginTask.execute();
            }
        });
        return  mView;
    }
    public void setDateBirth()
    {
        String[] date = Api.user.getBirth_date().split("-");
        Spinner spinDay = (Spinner)mView.findViewById(R.id.spinnerDay);
        Spinner spinMonth = (Spinner)mView.findViewById(R.id.spinnerMonth);
        Spinner spinYear = (Spinner)mView.findViewById(R.id.spinnerYear);

        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                String year = ((Spinner)mView.findViewById(R.id.spinnerYear)).getSelectedItem().toString();
                String month = ((Spinner)mView.findViewById(R.id.spinnerMonth)).getSelectedItem().toString();
                Spinner spin = (Spinner)mView.findViewById(R.id.spinnerDay);
                ArrayAdapter<String> adapter = (ArrayAdapter<String>)spin.getAdapter();
                adapter.clear();
                adapter.addAll(mdt.getDays(year, month));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        };

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(mView.getContext(), android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        adapter2.addAll(mdt.getMonths());
        spinMonth.setAdapter(adapter2);
        spinMonth.setOnItemSelectedListener(listener);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(mView.getContext(), android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        adapter3.addAll(mdt.getYears());
        spinYear.setAdapter(adapter3);
        spinYear.setOnItemSelectedListener(listener);

        String year = ((Spinner)mView.findViewById(R.id.spinnerYear)).getSelectedItem().toString();
        String month = ((Spinner)mView.findViewById(R.id.spinnerMonth)).getSelectedItem().toString();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mView.getContext(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        adapter.addAll(mdt.getDays(year, month));
        spinDay.setAdapter(adapter);

        selectValue(spinYear, date[0]);
        selectValue(spinMonth, mdt.getMonthByIndex(date[1]));
        selectValue((Spinner)mView.findViewById(R.id.spinnerDay), date[2]);
    }

    public  void setSpinner()
    {
        ArrayAdapter<Country> adapter = new CountryAdapter(mView.getContext());
        adapter.setDropDownViewResource(R.layout.drop_down_item);

        final Spinner spinner = (Spinner) mView.findViewById(R.id.spinnerCountries);
        spinner.setAdapter(adapter);
        selectValue(spinner, Api.user.getCountry());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                Country country = (Country)parent.getItemAtPosition(pos);
                ((TextView) mView.findViewById(R.id.user_page_phone_code)).setText(Integer.toString(country.getPhone_code()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }



    public void getAllCountries()
    {
        AsyncHttpTask userLoginTask = new AsyncHttpTask(null, AsyncMethodNames.GET_COUNTRIES, getActivity());
        userLoginTask.execute();
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

}
