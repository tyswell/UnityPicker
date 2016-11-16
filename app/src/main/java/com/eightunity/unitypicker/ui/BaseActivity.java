package com.eightunity.unitypicker.ui;

import android.os.Bundle;

import com.eightunity.unitypicker.model.account.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * Created by chokechaic on 4/28/2016.
 */
public class BaseActivity extends AuthenticaterActivity {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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


}
