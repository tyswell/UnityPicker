package com.eightunity.unitypicker;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.eightunity.unitypicker.authenticator.Constant.AuthenticatorConstant;
import com.eightunity.unitypicker.authenticator.LoginActivity;
import com.eightunity.unitypicker.database.DatabaseManager;
import com.eightunity.unitypicker.database.UnityPickerDB;
import com.eightunity.unitypicker.model.account.User;
import com.eightunity.unitypicker.ui.Application;
import com.eightunity.unitypicker.ui.AuthenticaterActivity;
import com.eightunity.unitypicker.utility.notification.FirebaseMsgService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

/**
 * Created by chokechaic on 8/26/2016.
 */
public class UnityPicker extends AuthenticaterActivity {

    private AccountManager accountManager;

    private static final String TAG = "UnityPicker";

    private static UnityPicker app = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = this;

        if (isLogined()) {
            initDatabase();
            openFirstPage();
        }
    }

    private void openFirstPage() {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);

        if (getIntent().getStringExtra(FirebaseMsgService.NOTIFICATION_INTENT) != null) {
            intent.putExtra(FirebaseMsgService.NOTIFICATION_INTENT, "tempValue");
        }

        startActivityForResult(intent, 1);
    }


    private void initDatabase() {
        if (!DatabaseManager.isDBMInitial()) {
            UnityPickerDB db = new UnityPickerDB(this);
            DatabaseManager.initializeInstance(db);
        }
    }

    public static Context getContext() {
        return app.getApplicationContext();
    }


}
