package com.example.eqvol.eqvola.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.eqvol.eqvola.Adapters.LeverageAdapter;
import com.example.eqvol.eqvola.Classes.Api;
import com.example.eqvol.eqvola.Classes.AsyncHttpTask;
import com.example.eqvol.eqvola.Classes.AsyncMethodNames;
import com.example.eqvol.eqvola.Classes.Group;
import com.example.eqvol.eqvola.Adapters.GroupAdapter;
import com.example.eqvol.eqvola.Classes.Leverage;
import com.example.eqvol.eqvola.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.List;


public class CreateAccount extends Fragment {

    private static View mView = null;


    public CreateAccount()
    {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("token", Api.getToken());

        AsyncHttpTask userLoginTask = new AsyncHttpTask(params, AsyncMethodNames.GET_GROUPS, getActivity());
        userLoginTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_create_account, container, false);

        setSpinnerGroups();

        /*HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("token", Api.getToken());

        /*AsyncHttpTask userLoginTask = new AsyncHttpTask(params, AsyncMethodNames.GET_GROUPS, getActivity());
        userLoginTask.execute();*/

        Button btn = (Button) mView.findViewById(R.id.btnCreateAccount);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner spinLeverage = (Spinner) mView.findViewById(R.id.create_account_spinner_leverage);
                int group_id = ((Leverage)(spinLeverage.getSelectedItem())).getGroup_id();
                int leverage_id = ((Leverage)(spinLeverage.getSelectedItem())).getId();

                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("token", Api.getToken());
                params.put("group_id", group_id);
                params.put("leverage_id", leverage_id);

                AsyncHttpTask userLoginTask = new AsyncHttpTask(params, AsyncMethodNames.CREATE_ACCOUNT, getActivity());
                userLoginTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

        return mView;
    }

    public void setSpinnerGroups()
    {
        List<Group> groups = Api.groups;
        Spinner spinnerGroups = (Spinner)mView.findViewById(R.id.create_account_spinner_group);

        ArrayAdapter<Group> adapter = new GroupAdapter(mView.getContext(), groups);
        adapter.setDropDownViewResource(R.layout.drop_down_item);

        spinnerGroups.setAdapter(adapter);

        spinnerGroups.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                Group group = (Group)parent.getItemAtPosition(pos);
                Spinner spinnerLeverages = (Spinner)mView.findViewById(R.id.create_account_spinner_leverage);
                ArrayAdapter<Leverage> adapter = new LeverageAdapter(mView.getContext(), group.getLeverages());
                adapter.setDropDownViewResource(R.layout.drop_down_item);
                spinnerLeverages.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }


}
