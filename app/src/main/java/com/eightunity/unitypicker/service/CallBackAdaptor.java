package com.eightunity.unitypicker.service;

import android.app.Activity;
import android.util.Log;

import com.eightunity.unitypicker.model.search.Search;
import com.eightunity.unitypicker.ui.BaseActivity;
import com.eightunity.unitypicker.ui.ErrorDialog;

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
            Log.e(TAG, "ERROR" + response.message());
            ErrorDialog errorDialog = new ErrorDialog();
            errorDialog.showDialog(activity, response.message());
            ((BaseActivity)activity).hideLoading();
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Log.d(TAG, "ERROR" + t.getMessage());
        ErrorDialog errorDialog = new ErrorDialog();
        errorDialog.showDialog(activity, t.getMessage());
        ((BaseActivity)activity).hideLoading();
    }
}
