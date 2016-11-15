package com.eightunity.unitypicker;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.eightunity.unitypicker.authenticator.LoginActivity;
import com.eightunity.unitypicker.database.DatabaseManager;
import com.eightunity.unitypicker.database.UnityPickerDB;
import com.eightunity.unitypicker.ui.AuthenticaterActivity;
import com.eightunity.unitypicker.utility.notification.FirebaseMsgService;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by chokechaic on 8/26/2016.
 */
public class UnityPicker extends AppCompatActivity {

    private static final String TAG = "UnityPicker";

    private static UnityPicker app = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "ON CREATE IS RUN");

        app = this;

        if (isLogined()) {
            initDatabase();
            openFirstPage();
        } else {
            Intent intent = new Intent(UnityPicker.this, LoginActivity.class);
            startActivityForResult(intent, 1);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        initDatabase();
        openFirstPage();
    }

    private boolean isLogined() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return false;
        } else {
            return true;
        }
    }
}
