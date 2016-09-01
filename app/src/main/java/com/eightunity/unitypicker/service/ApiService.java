package com.eightunity.unitypicker.service;

import com.eightunity.unitypicker.R;
import com.eightunity.unitypicker.model.User;
import com.eightunity.unitypicker.model.service.ModelService;
import com.eightunity.unitypicker.model.service.ResponseService;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by deksen on 8/31/16 AD.
 */
public interface ApiService {

    @POST("login")
    Call<ResponseService> login(@Body User user);

}
