package com.example.eqvol.eqvola.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.eqvol.eqvola.Adapters.WithdrawalsAdapter;
import com.example.eqvol.eqvola.Classes.Api;
import com.example.eqvol.eqvola.Classes.SpaceItemDecoration;
import com.example.eqvol.eqvola.Classes.Withdrawal;
import com.example.eqvol.eqvola.MainActivity;
import com.example.eqvol.eqvola.R;

import java.util.List;


public class WithdrawalsRecyclerFragment extends Fragment {

    private View mView;
    public WithdrawalsRecyclerFragment() {
        // Required empty public constructor
    }


    public static WithdrawalsRecyclerFragment newInstance() {
        WithdrawalsRecyclerFragment fragment = new WithdrawalsRecyclerFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_withdrawals_recycler, container, false);
        RecyclerView list = (RecyclerView)mView.findViewById(R.id.withdrawals_list);

        list.addItemDecoration(new SpaceItemDecoration(10, getContext()));

        setList();
        return mView;
    }

    private void setList()
    {
        List<Withdrawal> withdrawals = Api.withdrawals;

        try {
            RecyclerView mRecyclerView = (RecyclerView) mView.findViewById(R.id.withdrawals_list);
            mRecyclerView.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(getContext());
            llm.setItemPrefetchEnabled(false);
            mRecyclerView.setLayoutManager(llm);
            mRecyclerView.addItemDecoration(new DividerItemDecoration(this.getActivity(), LinearLayout.VERTICAL));

            final WithdrawalsAdapter adapter = new WithdrawalsAdapter(mView.getContext(), withdrawals);
            /*adapter.setOnItemCLickListener(new WithdrawalsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    long id = adapter.getItemId(position);
                    Withdrawal withdrawal = adapter.getOrderById(id);
                    MainActivity main = (MainActivity)getContext();
                    main.openFinanceOperation(withdrawal);
                }

            });*/
            mRecyclerView.setAdapter(adapter);
        } catch(Exception ex){
            String str = ex.getMessage();
        }
    }

}
