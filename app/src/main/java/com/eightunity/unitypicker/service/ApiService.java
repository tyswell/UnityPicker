package com.eightunity.unitypicker.service;

import com.eightunity.unitypicker.model.server.device.DeviceToken;
import com.eightunity.unitypicker.model.server.search.AddSearchingResponse;
import com.eightunity.unitypicker.model.server.search.InactiveSearching;
import com.eightunity.unitypicker.model.server.search.Searching;
import com.eightunity.unitypicker.model.server.user.LoginReceive;
import com.eightunity.unitypicker.model.server.user.LoginResponse;
import com.eightunity.unitypicker.model.server.user.LogoutReceive;

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
    Call<AddSearchingResponse> addSearching(@Body Searching searching);

    @POST("searchingservice/inactivesearch")
    Call<Boolean> deleteSearching(@Body InactiveSearching deleteSearching);

    @POST("userservice/logout")
    Call<Boolean> logout(@Body LogoutReceive logoutReceive);

}
