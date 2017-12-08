package com.example.eqvol.eqvola.fragments;

/**
 * Created by eqvol on 19.10.2017.
 */

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.eqvol.eqvola.Classes.Api;
import com.example.eqvol.eqvola.Classes.AsyncHttpTask;
import com.example.eqvol.eqvola.Classes.Attachment;
import com.example.eqvol.eqvola.Classes.Country;
import com.example.eqvol.eqvola.Adapters.CountryAdapter;
import com.example.eqvol.eqvola.Classes.MyDateFormat;
import com.example.eqvol.eqvola.Classes.MyFirstLoader;
import com.example.eqvol.eqvola.MenuActivity;
import com.example.eqvol.eqvola.R;
import com.example.eqvol.eqvola.Classes.AsyncMethodNames;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class UserPageFragment extends Fragment{

    private static View mView = null;
    private MyDateFormat mdt = new MyDateFormat();


    private static final int REQUEST_GALLERY = 2;

    public UserPageFragment()
    {
        try {
            /*mLoader = getLoaderManager().initLoader(LOADER_ID, null, this);
            mLoader.forceLoad();*/
            getAllCountries();
           // mLoader.onContentChanged();
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
        ((TextView) mView.findViewById(R.id.user_page_postal_code)).setText(Api.user.getPostal_code());

        setUserImage();

        setDateBirth();
        setSpinner();

        FloatingActionButton fab = (FloatingActionButton) mView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadGallery(mView);
            }
        });

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
                userLoginTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
        return  mView;
    }

    public void setUserImage()
    {
        ImageView img = (ImageView) mView.findViewById(R.id.imageUserAvatar);
        byte[] data = Api.user.getAvatar();
        //byte[] decodedString = //Base64.decode(data, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        img.setImageBitmap(bitmap);
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

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(mView.getContext(), R.layout.spinner_item);
        adapter2.setDropDownViewResource(R.layout.drop_down_item);
        adapter2.addAll(mdt.getMonths());
        spinMonth.setAdapter(adapter2);
        spinMonth.setOnItemSelectedListener(listener);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(mView.getContext(), R.layout.spinner_item);
        adapter3.setDropDownViewResource(R.layout.drop_down_item);
        adapter3.addAll(mdt.getYears());
        spinYear.setAdapter(adapter3);
        spinYear.setOnItemSelectedListener(listener);

        String year = ((Spinner)mView.findViewById(R.id.spinnerYear)).getSelectedItem().toString();
        String month = ((Spinner)mView.findViewById(R.id.spinnerMonth)).getSelectedItem().toString();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mView.getContext(), R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.drop_down_item);
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

    public void loadGallery(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            /*case REQUEST_CAMERA:

                break;*/
            case REQUEST_GALLERY:
                if (resultCode == RESULT_OK) {
                    final Uri imageUri = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(mView.getContext().getContentResolver(), imageUri);
                        String encodedData = imageToString(bitmap);

                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("data:");//
                        stringBuilder.append(getMimeType(imageUri));// image/jpeg;base64,");
                        stringBuilder.append(";base64,");
                        stringBuilder.append(encodedData);

                        HashMap<String, Object> paramData = new HashMap<String, Object>();
                        paramData.put("id", Api.user.getId());
                        paramData.put("avatar", stringBuilder.toString());

                        Gson gson = new GsonBuilder().create();
                        HashMap<String, Object> params = new HashMap<String, Object>();
                        params.put("token", Api.getToken());
                        params.put("data", gson.toJson(paramData));

                        AsyncHttpTask userLoginTask = new AsyncHttpTask(params, AsyncMethodNames.SET_USER_AVATAR, getActivity());
                        userLoginTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                        ImageView img = (ImageView)mView.findViewById(R.id.imageUserAvatar);
                        img.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

        // Получаем изображение из потока в виде байтов
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public String getMimeType(Uri uri) {
        String mimeType;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = mView.getContext().getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

}
