package com.eightunity.unitypicker.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.eightunity.unitypicker.authenticator.Constant.AuthenticatorConstant;
import com.eightunity.unitypicker.authenticator.LoginActivity;
import com.eightunity.unitypicker.model.account.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * Created by deksen on 5/15/16 AD.
 */
public class AuthenticaterActivity extends AppCompatActivity {

    private Bundle bnd;


    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 2404;

    private static final String TAG = "AuthenticaterActivity";

    private SharedPreferences pref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = getApplicationContext().getSharedPreferences(AuthenticatorConstant.SHARE_PREFERENCE_NAME, 0);

        Log.d(TAG, "CHECK GOOGLE PLAY SERVICE");
        checkGooglePlayService();
        checkAuthen();
    }

    private void checkAuthen() {
        Log.d(TAG, "CHECK AUTHEN");
        if (getAccount() == null) {
            Log.d(TAG, "CHECK AUTHEN = DON'T HAS ACCOUNT");
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivityForResult(intent, 1);
        }
    }

    protected boolean isLogined() {
        if (getAccount() == null) {
            return false;
        } else {
            return true;
        }
    }

    private void checkGooglePlayService() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        REQUEST_GOOGLE_PLAY_SERVICES).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void logout() {
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();

        FirebaseAuth.getInstance().signOut();
    }

    public FirebaseUser getAccount() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}
