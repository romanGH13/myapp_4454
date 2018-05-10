package com.example.eqvol.eqvola.fragments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.eqvol.eqvola.Classes.Api;
import com.example.eqvol.eqvola.Classes.AsyncHttpTask;
import com.example.eqvol.eqvola.Classes.AsyncMethodNames;
import com.example.eqvol.eqvola.Classes.FragmentLoader;
import com.example.eqvol.eqvola.Classes.Order;
import com.example.eqvol.eqvola.MainActivity;
import com.example.eqvol.eqvola.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class OrderFragment extends Fragment {

    private static View mView;

    private static TextView mAccountLoginView;
    private static TextView mOrderTypeView;
    private static TextView mOrderSymbolView;
    private static TextView mOrderVolumeView;

    private static EditText mStopLoss;
    private static EditText mTakeProfit;

    private static EditText mComment;

    private static TextView mProfitView;
    private static EditText mPriceView;

    private static TextView mDateAndTime;

    private static Button mBtnSaveView;
    private static Button mBtnCloseView;

    public static boolean isNeedUpdate = false;

    public static Order order;
    private static Calendar dateAndTime = Calendar.getInstance();

    public OrderFragment() {
        order = Api.currentOrder;

        // Required empty public constructor
    }


    public static OrderFragment newInstance() {
        OrderFragment fragment = new OrderFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_order, container, false);

        isNeedUpdate = true;

        mAccountLoginView = (TextView) mView.findViewById(R.id.account_login);
        mOrderTypeView = (TextView) mView.findViewById(R.id.order_type);
        mOrderSymbolView = (TextView) mView.findViewById(R.id.order_symbol);
        mOrderVolumeView = (TextView) mView.findViewById(R.id.order_volume);

        mStopLoss = (EditText) mView.findViewById(R.id.order_stop_loss);
        mTakeProfit = (EditText) mView.findViewById(R.id.order_take_profit);
        mComment = (EditText) mView.findViewById(R.id.order_comment);

        mProfitView = (TextView) mView.findViewById(R.id.order_profit);
        mPriceView = (EditText) mView.findViewById(R.id.order_price);

        mBtnSaveView = (Button)mView.findViewById(R.id.btnSave);
        mBtnCloseView = (Button)mView.findViewById(R.id.btnClose);

        setData();

        Drawable drawableSell = mBtnSaveView.getBackground();
        drawableSell.setColorFilter(getResources().getColor(R.color.colorMain), PorterDuff.Mode.MULTIPLY);
        mBtnSaveView.setBackground(drawableSell);

        Drawable drawableBuy = mBtnCloseView.getBackground();
        drawableBuy.setColorFilter(getResources().getColor(R.color.colorRed), PorterDuff.Mode.MULTIPLY);
        mBtnCloseView.setBackground(drawableBuy);

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(mDateAndTime.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(date != null)
        {
            dateAndTime = Calendar.getInstance();
            dateAndTime.setTime(date);
        }

        mBtnCloseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("token", Api.getToken());
                params.put("order", order.getOrder());

                if(((Button)v).getText().toString().contentEquals("Delete order")) {
                    AsyncHttpTask getOrdersTask = new AsyncHttpTask(params, AsyncMethodNames.DELETE_ORDER, getActivity());
                    getOrdersTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
                else
                {
                    AsyncHttpTask getOrdersTask = new AsyncHttpTask(params, AsyncMethodNames.CLOSE_ORDER, getActivity());
                    getOrdersTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }
        });

        mBtnSaveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("token", Api.getToken());
                params.put("order", order.getOrder());
                params.put("price", mPriceView.getText().toString());
                params.put("sl", mStopLoss.getText().toString());
                params.put("tp", mTakeProfit.getText().toString());
                params.put("comment", mComment.getText().toString());
                if(!mDateAndTime.getText().toString().contentEquals("Without expiration"))
                {
                    Date expirationDate = dateAndTime.getTime();
                    SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    String date = dt.format(expirationDate);
                    params.put("expired", date);
                }

                AsyncHttpTask getOrdersTask = new AsyncHttpTask(params, AsyncMethodNames.UPDATE_ORDER, getActivity());
                getOrdersTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

        ((ImageView)mView.findViewById(R.id.btnBack)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentLoader fl = new FragmentLoader(MobileTraderFragment.class, ((MainActivity)(mView.getContext())).getSupportFragmentManager(), R.id.container, false);
                fl.startLoading();
                MainActivity.currentLoader = fl;
            }
        });

        HashMap<String, Object> params;

        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("order", order.getOrder());

        Gson gson = new GsonBuilder().create();

        params = new HashMap<String, Object>();
        params.put("token", Api.getToken());
        params.put("where", gson.toJson(where));

        AsyncHttpTask getOrdersTask = new AsyncHttpTask(params, AsyncMethodNames.GET_ORDER_UPDATE, (Activity) mView.getContext());
        getOrdersTask.target = OpenOrdersFragment.class.toString();
        getOrdersTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        ((TextView)mView.findViewById(R.id.order)).setText("Order #" + order.getOrder());

        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isNeedUpdate = false;
    }

    public static void setData()
    {
        try {
            mAccountLoginView.setText(Integer.toString(order.getLogin()));
            mOrderTypeView.setText(order.getCmd());
            mOrderSymbolView.setText(order.getSymbol());
            mOrderVolumeView.setText(Integer.toString(order.getVolume()));

            mStopLoss.setText(Double.toString(order.getSl()));
            mTakeProfit.setText(Double.toString(order.getTp()));
            mComment.setText(order.getComment());

            mProfitView.setText(Double.toString(order.getProfit()));
            mPriceView.setText(Double.toString(order.getOpenPrice()));

            if(order.getCmd().contentEquals("Buy") || order.getCmd().contentEquals("Sell"))
            {
                mView.findViewById(R.id.layoutExpiration).setVisibility(View.GONE);
                mView.findViewById(R.id.order_price_layout).setVisibility(View.INVISIBLE);
            }
            else
            {
                mBtnCloseView.setText("Delete order");
            }

            mDateAndTime = (TextView) mView.findViewById(R.id.expirationDateAndTime);
            mDateAndTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDate(v);
                }
            });
            //setInitialDateTime();
            String orderExpiration = order.getExpiration();
            if(orderExpiration.contentEquals("1970-01-01 00:00:00"))
            {
                mDateAndTime.setText("Without expiration");
            }
            else
            {
                mDateAndTime.setText(orderExpiration);
            }

        } catch(Exception ex)
        {
            isNeedUpdate = false;
        }
    }

    public static void updateData()
    {
        try {
            mAccountLoginView.setText(Integer.toString(order.getLogin()));
            mOrderTypeView.setText(order.getCmd());
            mOrderSymbolView.setText(order.getSymbol());
            mOrderVolumeView.setText(Integer.toString(order.getVolume()));

            mProfitView.setText(Double.toString(order.getProfit()));

            if(order.getCmd().contentEquals("Buy") || order.getCmd().contentEquals("Sell"))
            {
                mView.findViewById(R.id.layoutExpiration).setVisibility(View.GONE);
                mView.findViewById(R.id.order_price_layout).setVisibility(View.INVISIBLE);
            }
            else
            {
                mBtnCloseView.setText("Delete order");
            }

            mDateAndTime = (TextView) mView.findViewById(R.id.expirationDateAndTime);
            mDateAndTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDate(v);
                }
            });
            //setInitialDateTime();
            String orderExpiration = order.getExpiration();
            if(orderExpiration.contentEquals("1970-01-01 00:00:00"))
            {
                mDateAndTime.setText("Without expiration");
            }
            else
            {
                mDateAndTime.setText(orderExpiration);
            }

        } catch(Exception ex)
        {
            isNeedUpdate = false;
        }
    }

    // метод, который открывает диалог выбора даты
    public static void getDate(View v)
    {
        new DatePickerDialog(v.getContext(), d, dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    // метод, который открывает диалог выбора времени
    public static void getTime(View v) {
        new TimePickerDialog(v.getContext(), t,
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE), true)
                .show();
    }

    // установка начальных даты и времени
    private static void setInitialDateTime() {
        Date expirationDate = dateAndTime.getTime();
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = dt.format(expirationDate);
        mDateAndTime.setText(date);
    }

    // обработчик выбора времени
    static TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            setInitialDateTime();
        }
    };

    //обработчик выбора даты
    static DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
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
