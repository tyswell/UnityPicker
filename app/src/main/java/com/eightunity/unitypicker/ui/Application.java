package com.eightunity.unitypicker.ui;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.eightunity.unitypicker.model.account.User;

/**
 * Created by chokechaic on 8/26/2016.
 */
public class Application extends android.app.Application {

    public User user;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
