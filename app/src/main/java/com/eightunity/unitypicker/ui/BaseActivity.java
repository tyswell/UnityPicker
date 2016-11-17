package com.eightunity.unitypicker.ui;

import android.os.Bundle;

import com.eightunity.unitypicker.model.account.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


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

    public User getUser() {
        User user = new User();

        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();

        user.setEmail(fbUser.getEmail());
        user.setUserId(fbUser.getUid());
        user.setDisplayName(fbUser.getDisplayName());
        user.setProfileURL(fbUser.getPhotoUrl().toString());

        return user;
    }

    public void showLoading() {
        loadingDialog.show();
    }

    public void hideLoading() {
        loadingDialog.dismiss();
    }


}
