package com.eightunity.unitypicker.ui;

import android.os.Bundle;

import com.eightunity.unitypicker.model.account.Device;
import com.eightunity.unitypicker.model.account.FacebookUser;
import com.eightunity.unitypicker.model.account.GoogleUser;
import com.eightunity.unitypicker.model.account.OSType;
import com.eightunity.unitypicker.model.account.User;
import com.eightunity.unitypicker.model.account.UserLoginType;
import com.eightunity.unitypicker.utility.DeviceUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.List;


/**
 * Created by chokechaic on 4/28/2016.
 */
public class BaseActivity extends AuthenticaterActivity {

    private TransparentProgressDialog loadingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadingDialog = new TransparentProgressDialog(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (loadingDialog != null) {
            try {
                loadingDialog.dismiss();
            } catch (Exception e) { }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        loadingDialog.dismiss();
    }

    public void showLoading() {
        loadingDialog.show();
    }

    public void hideLoading() {
        loadingDialog.dismiss();
    }


}
