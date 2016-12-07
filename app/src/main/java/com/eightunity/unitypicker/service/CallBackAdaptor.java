package com.eightunity.unitypicker.service;

import android.app.Activity;
import android.util.Log;

import com.eightunity.unitypicker.model.search.Search;
import com.eightunity.unitypicker.ui.BaseActivity;
import com.eightunity.unitypicker.ui.ErrorDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by chokechaic on 11/21/2016.
 */

public abstract class CallBackAdaptor<T> implements Callback<T> {

    private static final String TAG = "CallBackAdaptor";

    private Activity activity;

    public CallBackAdaptor(Activity activity) {
        this.activity = activity;
    }

    public abstract void onSuccess(T response);

    @Override
    public void onResponse(Call<T> call, retrofit2.Response<T> response) {
        if (response.isSuccessful()) {
            onSuccess(response.body());
            ((BaseActivity)activity).hideLoading();
        } else {

            ErrorDialog errorDialog = new ErrorDialog();
            try {
                JSONObject jObjError = new JSONObject(response.errorBody().string());
                String statusCode = jObjError.getString("statusCode");
                String errorDesc = jObjError.getString("errorMessage");
                Log.e(TAG, "statusCode : "+statusCode + " || ERROR MESSAGE : "+errorDesc);
                errorDialog.showDialog(activity, "statusCode : "+statusCode + " || ERROR MESSAGE : "+errorDesc);
            } catch (JSONException e) {
                Log.e(TAG, "JSONException e : "+e.getMessage());
                errorDialog.showDialog(activity, "JSONException e : "+e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "IOException : "+e.getMessage());
                errorDialog.showDialog(activity, "IOException : "+e.getMessage());
            }
            ((BaseActivity)activity).hideLoading();
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Log.e(TAG, "ERROR onFailure =" + t.getMessage());
        ErrorDialog errorDialog = new ErrorDialog();
        errorDialog.showDialog(activity, t.getMessage());
        ((BaseActivity)activity).hideLoading();
    }
}
