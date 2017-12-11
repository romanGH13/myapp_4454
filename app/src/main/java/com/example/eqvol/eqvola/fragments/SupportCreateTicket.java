package com.example.eqvol.eqvola.fragments;

import android.content.Context;
import android.icu.util.ULocale;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.eqvol.eqvola.Adapters.AccountsAdapter;
import com.example.eqvol.eqvola.Adapters.CategoryAdapter;
import com.example.eqvol.eqvola.Classes.Account;
import com.example.eqvol.eqvola.Classes.Api;
import com.example.eqvol.eqvola.Classes.AsyncHttpTask;
import com.example.eqvol.eqvola.Classes.AsyncMethodNames;
import com.example.eqvol.eqvola.Classes.Category;
import com.example.eqvol.eqvola.Classes.Leverage;
import com.example.eqvol.eqvola.Classes.Message;
import com.example.eqvol.eqvola.Classes.Ticket;
import com.example.eqvol.eqvola.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.List;


public class SupportCreateTicket extends Fragment {
    private static View mView = null;

    public SupportCreateTicket()
    {

    }

    public static SupportCreateTicket newInstance() {
        SupportCreateTicket fragment = new SupportCreateTicket();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_support_create_ticket, container, false);


        setSpinner();

        Button btn = (Button) mView.findViewById(R.id.support_create_ticket_btnSend);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner spinCategory = (Spinner) mView.findViewById(R.id.support_create_ticket_spinner);
                EditText etSubject = (EditText)(mView.findViewById(R.id.support_create_ticket_subject));
                EditText etMessage = (EditText)(mView.findViewById(R.id.support_create_ticket_text));

                Category category = (Category)(spinCategory.getSelectedItem());
                String subject = etSubject.getText().toString();
                String messageText = etMessage.getText().toString();
                if(subject.contentEquals("")) {
                    Toast toast = Toast.makeText(mView.getContext(),
                            "Subject can not be empty.", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                if( messageText.contentEquals("")) {
                    Toast toast = Toast.makeText(mView.getContext(),
                            "Message can not be empty.", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                Message message = new Message(messageText);
                Ticket ticket = new Ticket(category, subject, message);
                Api.user.addTicket(ticket);

                Gson gson = new GsonBuilder().create();
                HashMap<String, Object> mapUserId = new HashMap<String, Object>();
                mapUserId.put("user_id", Api.user.getId());
                mapUserId.put("category_id", category.getId());
                mapUserId.put("title", subject);

                String json = gson.toJson(mapUserId);

                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("token", Api.getToken());
                params.put("data", json);
                AsyncHttpTask userLoginTask = new AsyncHttpTask(params, AsyncMethodNames.CREATE_TICKET, getActivity());
                userLoginTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

        return mView;
    }

    public  void setSpinner()
    {
        List<Category> categories = Api.categories;
        Spinner spinner = (Spinner)mView.findViewById(R.id.support_create_ticket_spinner);
        ArrayAdapter<Category> adapter = new CategoryAdapter(mView.getContext(), categories);
        spinner.setAdapter(adapter);
    }

}
