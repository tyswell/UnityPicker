package com.eightunity.unitypicker.ui;

import android.os.Bundle;

import com.eightunity.unitypicker.model.account.User;


/**
 * Created by chokechaic on 4/28/2016.
 */
public class BaseActivity extends AuthenticaterActivity {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public User getUser() {
        return ((Application)getApplicationContext()).user;
    }

}
