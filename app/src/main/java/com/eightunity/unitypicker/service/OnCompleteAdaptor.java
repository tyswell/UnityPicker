package com.eightunity.unitypicker.service;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.eightunity.unitypicker.ui.BaseActivity;
import com.eightunity.unitypicker.ui.ErrorDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.GetTokenResult;

/**
 * Created by chokechaic on 11/21/2016.
 */

public abstract class OnCompleteAdaptor implements OnCompleteListener<GetTokenResult> {

    public abstract void doSuccess(String tokenId);

    private Activity mActivity;

    public OnCompleteAdaptor(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void onComplete(@NonNull Task<GetTokenResult> task) {
        ErrorDialog errorDialog = new ErrorDialog();
        if (task.isSuccessful()) {
            doSuccess(task.getResult().getToken());
        } else {
            errorDialog.showDialog(mActivity, task.getException().getMessage());
            ((BaseActivity)mActivity).hideLoading();
        }
    }
}
