package com.eightunity.unitypicker.service;

import com.eightunity.unitypicker.model.account.User;
import com.eightunity.unitypicker.model.server.user.LoginReceive;
import com.eightunity.unitypicker.model.service.ResponseService;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by deksen on 8/31/16 AD.
 */
public interface ApiService {

    @POST("userservice/login")
    Call<ResponseService> login(@Body LoginReceive loginReceive);

}
