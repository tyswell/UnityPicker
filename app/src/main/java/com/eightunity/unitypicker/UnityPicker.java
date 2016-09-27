package com.eightunity.unitypicker;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.eightunity.unitypicker.database.DatabaseManager;
import com.eightunity.unitypicker.database.UnityPickerDB;
import com.eightunity.unitypicker.ui.AuthenticaterActivity;
import com.eightunity.unitypicker.utility.notification.FirebaseMsgService;

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
