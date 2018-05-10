package com.example.eqvol.eqvola.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.Group;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.eqvol.eqvola.Adapters.DefferedOrderTypeAdapter;
import com.example.eqvol.eqvola.Adapters.QuotationsForSpinnerAdapter;
import com.example.eqvol.eqvola.Adapters.RequestTransferAccountAdapter;
import com.example.eqvol.eqvola.Classes.Account;
import com.example.eqvol.eqvola.Classes.Api;
import com.example.eqvol.eqvola.Classes.AsyncHttpTask;
import com.example.eqvol.eqvola.Classes.AsyncMethodNames;
import com.example.eqvol.eqvola.Classes.Quotation;
import com.example.eqvol.eqvola.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class CreateOrderFragment extends Fragment {

    private static View mView;

    private EditText mAccountBalance;
    private EditText mOrderVolume;

    private Spinner mSpinnerAccount;
    private static Spinner mSpinnerSymbol;
    private Spinner mSpinnerOrderType;

    private EditText mStopLoss;
    private EditText mTakeProfit;
    private static EditText mBid;
    private static EditText mAsk;
    private EditText mComment;

    private Button mBtnSell;
    private Button mBtnBuy;
    private Button mBtnSetAnOrder;

    private RadioGroup radioGroup;
    private Group groupButtons;
    private Group groupDeferredOrder;

    private CheckBox mExpirationCheckBox;
    private TextView mDateAndTime;
    private TextView mPrice;

    private Calendar dateAndTime = Calendar.getInstance();

    public static String currentSymbol = "";

    public CreateOrderFragment() {
        // Required empty public constructor
    }

    public static CreateOrderFragment newInstance() {
        CreateOrderFragment fragment = new CreateOrderFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_create_order, container, false);

        mAccountBalance = (EditText) mView.findViewById(R.id.account_balance);
        mOrderVolume = (EditText) mView.findViewById(R.id.order_volume);

        mSpinnerAccount = (Spinner) mView.findViewById(R.id.spinner_accounts);
        mSpinnerSymbol = (Spinner) mView.findViewById(R.id.spinner_symbol);
        mSpinnerOrderType = (Spinner) mView.findViewById(R.id.spinner_type);

        mStopLoss = (EditText) mView.findViewById(R.id.stop_loss);
        mTakeProfit = (EditText) mView.findViewById(R.id.take_profit);
        mBid = (EditText) mView.findViewById(R.id.bid);
        mAsk = (EditText) mView.findViewById(R.id.ask);
        mComment = (EditText) mView.findViewById(R.id.comment);

        mBtnSell = (Button) mView.findViewById(R.id.btnSell);
        mBtnBuy = (Button) mView.findViewById(R.id.btnBuy);
        mBtnSetAnOrder = (Button) mView.findViewById(R.id.btnSetAnOrder);


        groupButtons = (Group) mView.findViewById(R.id.buttonsGroup);
        groupDeferredOrder = (Group) mView.findViewById(R.id.deferredOrderGroup);

        radioGroup = (RadioGroup) mView.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case -1:
                        break;
                    case R.id.radioMarketExecution:
                        groupDeferredOrder.setVisibility(View.GONE);
                        groupButtons.setVisibility(View.VISIBLE);
                        break;
                    case R.id.radioDeferredOrder:
                        groupButtons.setVisibility(View.GONE);
                        groupDeferredOrder.setVisibility(View.VISIBLE);
                        break;

                    default:
                        break;
                }
            }
        });

        mPrice = (TextView) mView.findViewById(R.id.order_price);




        mDateAndTime = (TextView) mView.findViewById(R.id.expirationDateAndTime);
        mDateAndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate(v);
            }
        });
        setInitialDateTime();

        mExpirationCheckBox = (CheckBox) mView.findViewById(R.id.expiration);
        mExpirationCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mExpirationCheckBox.isChecked())
                    mDateAndTime.setEnabled(true);
                else
                    mDateAndTime.setEnabled(false);
            }
        });



        //изменение фона кнопок
        Drawable drawableSell = mBtnSell.getBackground();
        drawableSell.setColorFilter(getResources().getColor(R.color.colorRed), PorterDuff.Mode.MULTIPLY);
        mBtnSell.setBackground(drawableSell);

        Drawable drawableBuy = mBtnBuy.getBackground();
        drawableBuy.setColorFilter(getResources().getColor(R.color.colorBlue), PorterDuff.Mode.MULTIPLY);
        mBtnBuy.setBackground(drawableBuy);

        Drawable drawableSetAnOrder = mBtnSetAnOrder.getBackground();
        drawableSetAnOrder.setColorFilter(getResources().getColor(R.color.colorRed), PorterDuff.Mode.MULTIPLY);
        mBtnSetAnOrder.setBackground(drawableSetAnOrder);

        setSpinnerAccounts();
        //setSpinnerQuotations();
        setSpinnerType();

        mBtnSell.setOnClickListener(myOnClickListener);
        mBtnBuy.setOnClickListener(myOnClickListener);
        mBtnSetAnOrder.setOnClickListener(myOnClickListener);

        return mView;
    }

    View.OnClickListener myOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Account userAccount = (Account)mSpinnerAccount.getSelectedItem();
            if(Double.parseDouble(userAccount.getBalance()) == 0 ) {
                mAccountBalance.setError("Do not have money on the account");
                return;
            }
            if(mOrderVolume.getText().length() < 1)
            {
                mOrderVolume.setError("Enter volume");
                mOrderVolume.requestFocus();
                return;
            }

            HashMap<String, Object> params = new HashMap<String, Object>();

            if(mSpinnerSymbol.getSelectedItem() == null)
            {
                return;
            }
            Quotation quotation = (Quotation) mSpinnerSymbol.getSelectedItem();

            int login = userAccount.getLogin();
            String symbol = quotation.getSymbol();
            int volume = Integer.parseInt(mOrderVolume.getText().toString());

            double stopLoss = 0;
            double takeProfit = 0;

            if(mStopLoss.getText().toString().length() > 1)
            {
                stopLoss = Double.parseDouble(mStopLoss.getText().toString());
            }

            if(mTakeProfit.getText().toString().length() > 1)
            {
                takeProfit = Double.parseDouble(mTakeProfit.getText().toString());
            }

            String comment = mComment.getText().toString();

            String cmd = "";
            Button btn = (Button) v;
            if(btn.getText().toString().contentEquals("Sell"))
            {
                cmd = "Sell";
            }
            else if(btn.getText().toString().contentEquals("Buy"))
            {
                cmd = "Buy";
            }
            else if(btn.getText().toString().contentEquals("Set an order"))
            {
                if(mPrice.getText().length() < 1)
                {
                    mPrice.setError("Enter price");
                    mPrice.requestFocus();
                    return;
                }

                String strCmd = (String)mSpinnerOrderType.getSelectedItem();

                switch(strCmd)
                {
                    case "Buy limit":
                        cmd = "BuyLimit";
                        break;
                    case "Sell limit":
                        cmd = "SellLimit";
                        break;
                    case "Buy stop":
                        cmd = "BuyStop";
                        break;
                    case "Sell stop":
                        cmd = "SellStop";
                        break;
                }

                double price = Double.parseDouble(mPrice.getText().toString());
                params.put("price", price);

                if(mExpirationCheckBox.isChecked())
                {
                    Date expirationDate = dateAndTime.getTime();
                    SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    String date = dt.format(expirationDate);
                    params.put("expired", date);
                }

            }


            params.put("token", Api.getToken());
            params.put("login", login);
            params.put("symbol", symbol);
            params.put("volume", volume);
            params.put("sl", stopLoss);
            params.put("tp", takeProfit);
            params.put("comment", comment);
            params.put("cmd", cmd);


            AsyncHttpTask getOrdersTask = new AsyncHttpTask(params, AsyncMethodNames.OPEN_ORDER, getActivity());
            getOrdersTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    };

    private void setSpinnerType()
    {

        ArrayList<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("Buy limit");
        spinnerArray.add("Sell limit");
        spinnerArray.add("Buy stop");
        spinnerArray.add("Sell stop");
        DefferedOrderTypeAdapter spinnerArrayAdapter = new DefferedOrderTypeAdapter(getContext(), spinnerArray);
        mSpinnerOrderType.setAdapter(spinnerArrayAdapter);
    }

    public static void setSpinnerQuotations(List<Quotation> list)
    {
        QuotationsForSpinnerAdapter adapter = new QuotationsForSpinnerAdapter(mView.getContext(), list);
        //adapter.setDropDownViewResource(R.layout.drop_down_item);
        mSpinnerSymbol.setPrompt("Account");
        mSpinnerSymbol.setAdapter(adapter);
        mSpinnerSymbol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Quotation quotation = (Quotation) parent.getItemAtPosition(pos);

                int digits = quotation.getDigits();
                String mask = "0.";
                for(int i = 0; i < digits; i++)
                {
                    mask += "0";
                }

                DecimalFormat myFormatter = new DecimalFormat(mask);

                String bid = myFormatter.format(quotation.getBid());
                String ask = myFormatter.format(quotation.getAsk());

                mBid.setText(bid);
                mAsk.setText(ask);

                currentSymbol = quotation.getSymbol();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    public static void updateSpinnerQuotations(List<String> pairList)
    {
        String currentPair = "";

        if(mSpinnerSymbol.getAdapter() != null) {
            if(mSpinnerSymbol.getSelectedItem() == null)
                currentPair = "";
            else
                currentPair = ((Quotation) mSpinnerSymbol.getSelectedItem()).getSymbol();
        }

        List<Quotation> list = new ArrayList<>();
        for(Quotation q: QuotationsFragment.quotations)
        {
            for(String symbol: pairList) {
                if (q.getSymbol().contentEquals(symbol))
                {
                    list.add(q);
                }
            }
        }

        setSpinnerQuotations(list);
        if(!currentPair.contentEquals("")) {
            QuotationsForSpinnerAdapter adapter = (QuotationsForSpinnerAdapter) mSpinnerSymbol.getAdapter();
            mSpinnerSymbol.setSelection(adapter.getPosititonBySymbol(currentPair));
        }
    }

    private void setSpinnerAccounts()
    {
        List<Account> accounts = Api.user.accounts;
        for(Account a: accounts)
        {
            if(a.getLogin() == 0) {
                accounts.remove(a);
                break;
            }
        }
        try {
            RequestTransferAccountAdapter adapter = new RequestTransferAccountAdapter(mView.getContext(), accounts);
            adapter.setDropDownViewResource(R.layout.drop_down_item);
            mSpinnerAccount.setAdapter(adapter);
            mSpinnerAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int pos, long id) {
                    Account account = (Account) parent.getItemAtPosition(pos);
                    mAccountBalance.setText(account.getBalance());
                    mAccountBalance.setError(null);

                    HashMap<String, Object> params = new HashMap<String, Object>();
                    params.put("token", Api.getToken());
                    params.put("login", account.getLogin());

                    AsyncHttpTask getOrdersTask = new AsyncHttpTask(params, AsyncMethodNames.GET_TRADE_PAIRS, getActivity());
                    getOrdersTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {

                }
            });
        } catch(Exception ex)
        {
            String str = ex.getMessage();
        }
    }

    // изменение выбранной котировки, при обновлении информации
    public static void updateCurrentQuotation(Quotation quotation)
    {
        int digits = quotation.getDigits();
        String mask = "0.";
        for(int i = 0; i < digits; i++)
        {
            mask += "0";
        }

        DecimalFormat myFormatter = new DecimalFormat(mask);

        String bid = myFormatter.format(quotation.getBid());
        String ask = myFormatter.format(quotation.getAsk());

        mBid.setText(bid);
        mAsk.setText(ask);
    }

    // метод, который открывает диалог выбора даты
    public void getDate(View v)
    {
        new DatePickerDialog(v.getContext(), d, dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    // метод, который открывает диалог выбора времени
    public void getTime(View v) {
        new TimePickerDialog(v.getContext(), t,
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE), true)
                .show();
    }

    // установка начальных даты и времени
    private void setInitialDateTime() {

        mDateAndTime.setText(DateUtils.formatDateTime(getContext(),
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
                        | DateUtils.FORMAT_SHOW_TIME));
    }

    // обработчик выбора времени
    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            setInitialDateTime();
        }
    };

    //обработчик выбора даты
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();

            //вызов метода выбора времени
            getTime(view);
        }
    };

}
