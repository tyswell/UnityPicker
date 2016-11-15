package com.eightunity.unitypicker.service;

import com.eightunity.unitypicker.authenticator.Account;
import com.eightunity.unitypicker.model.account.User;
import com.eightunity.unitypicker.model.server.device.DeviceToken;
import com.eightunity.unitypicker.model.server.search.DeleteSearching;
import com.eightunity.unitypicker.model.server.search.Searching;
import com.eightunity.unitypicker.model.server.user.LoginReceive;
import com.eightunity.unitypicker.model.server.user.LoginResponse;
import com.eightunity.unitypicker.model.service.ResponseService;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by deksen on 8/31/16 AD.
 */
public interface ApiService {

    @POST("userservice/login")
    Call<LoginResponse> login(@Body LoginReceive loginReceive);

    @POST("deviceservice/updatetoken")
    Call<Boolean> updateToken(@Body DeviceToken deviceToken);

    @POST("searchingservice/addsearch")
    Call<Integer> addSearching(@Body Searching searching);

    @POST("searchingservice/deletesearch")
    Call<Boolean> deleteSearching(@Body DeleteSearching deleteSearching);

}
