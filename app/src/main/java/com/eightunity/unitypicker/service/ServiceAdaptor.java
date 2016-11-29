package com.eightunity.unitypicker.service;

import android.app.Activity;

import com.eightunity.unitypicker.R;
import com.eightunity.unitypicker.ui.BaseActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by chokechaic on 11/21/2016.
 */

public abstract class ServiceAdaptor {

    private FirebaseUser fUser;

    public abstract void callService(FirebaseUser fUser, String tokenId, ApiService service);

    public ServiceAdaptor(final Activity activity) {
        ((BaseActivity)activity).showLoading();
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        fUser.getToken(true).addOnCompleteListener(new OnCompleteAdaptor(activity) {
            @Override
            public void doSuccess(String tokenId) {
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);

                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .addInterceptor(logging)
                        .build();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(activity.getResources().getString(R.string.base_service_url))
                        .client(okHttpClient)
                        .addConverterFactory(JacksonConverterFactory.create())
                        .build();
                ApiService service = retrofit.create(ApiService.class);
                callService(fUser, tokenId, service);
            }
        });
    }
}
