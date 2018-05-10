package com.example.eqvol.eqvola.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.eqvol.eqvola.Adapters.QuotationsAdapter;
import com.example.eqvol.eqvola.Classes.Quotation;
import com.example.eqvol.eqvola.Classes.SpaceItemDecoration;
import com.example.eqvol.eqvola.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import tech.gusavila92.websocketclient.WebSocketClient;


public class QuotationsFragment extends Fragment {

    private String socketAddress = "ws://95.211.148.40:1189/quotations/";

    private static View mView;
    private RecyclerView list;
    private WebSocketClient client;

    private static QuotationsAdapter adapter;
    private EditText mSearchView;

    public static List<Quotation> quotations;

    public QuotationsFragment() {
        quotations = new ArrayList<Quotation>();
    }


    public static QuotationsFragment newInstance() {
        QuotationsFragment fragment = new QuotationsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_quotations, container, false);
        createTable();
        openWebSocket();
        return mView;
    }

    private void openWebSocket()
    {
        URI uri = null;
        try {
            uri = new URI(socketAddress);
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        if(uri != null) {
            client = new WebSocketClient(uri) {
                @Override
                public void onOpen() {
                    String str = "connected";
                }

                @Override
                public void onTextReceived(String message) {
                    Gson gson = new GsonBuilder().create();
                    List<Quotation> newQuotations = gson.fromJson(message, new TypeToken<List<Quotation>>() {}.getType());

                    updateQuotations(newQuotations);
                }

                @Override
                public void onBinaryReceived(byte[] data) {

                }

                @Override
                public void onPingReceived(byte[] data) {

                }

                @Override
                public void onPongReceived(byte[] data) {

                }

                @Override
                public void onException(Exception e) {
                    String str = e.getMessage();
                }

                @Override
                public void onCloseReceived() {

                }
            };

            client.setConnectTimeout(10000);
            client.setReadTimeout(60000);
            client.enableAutomaticReconnection(5000);
            client.connect();
        }
    }

    @Override
    public void onDestroy() {

        client.close();
        super.onDestroy();
    }

    public static void updateQuotations(List<Quotation> newQuotations)
    {

        if(quotations.size() == 0)
        {
            quotations.addAll(newQuotations);
        }
        else
        {
            for(final Quotation newQuotation: newQuotations)
            {
                for(int i = 0; i < quotations.size(); i++)
                {

                    if(quotations.get(i).getSymbol().contentEquals(newQuotation.getSymbol()))
                    {
                        quotations.set(i, newQuotation);
                        ((Activity)mView.getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            adapter.UpdateQuotationBySymbol(newQuotation);
                            adapter.notifyDataSetChanged();

                            if(CreateOrderFragment.currentSymbol.contentEquals(newQuotation.getSymbol()))
                            {
                                CreateOrderFragment.updateCurrentQuotation(newQuotation);
                            }
                            }
                        });
                    }
                }
            }
        }

    }

    public void createTable() {

        list = (RecyclerView) mView.findViewById(R.id.quotations_list);
        list.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mView.getContext());
        llm.setItemPrefetchEnabled(false);
        list.setLayoutManager(llm);
        list.addItemDecoration(new DividerItemDecoration(mView.getContext(), LinearLayout.VERTICAL));
        list.addItemDecoration(new SpaceItemDecoration(10, mView.getContext()));

        mSearchView = (EditText) mView.findViewById(R.id.search);
        adapter = new QuotationsAdapter(mView.getContext(), mSearchView, quotations);
        list.setAdapter(adapter);

        mSearchView.addTextChangedListener(new TextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                if(adapter!= null) {
                    adapter.searchStringChanged(quotations);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
    }
}
