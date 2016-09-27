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


/**
 * Created by deksen on 5/15/16 AD.
 */
public class AuthenticaterActivity extends AppCompatActivity {

    private AccountManager accountManager;
    private Bundle bnd;


    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 2404;

    private static final String TAG = "AuthenticaterActivity";

    private SharedPreferences pref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountManager = AccountManager.get(this);

        pref = getApplicationContext().getSharedPreferences(AuthenticatorConstant.SHARE_PREFERENCE_NAME, 0);

        Log.d(TAG, "CHECK GOOGLE PLAY SERVICE");
        checkGooglePlayService();

        checkAuthen();

    }

    private User getUserInfo() {


        User user = new User();

        String authToken = pref.getString(AuthenticatorConstant.PARAM_TOEN, "");
        String name = pref.getString(AuthenticatorConstant.PARAM_NAME, "");
        String username = pref.getString(AuthenticatorConstant.PARAM_USERNAME, "");
        String providerID = pref.getString(AuthenticatorConstant.PARAM_PROVIDERID, "");

        String photoUrl = pref.getString(AuthenticatorConstant.PARAM_PROFILE_URL, "");;

        user.setUsername(username);
        user.setToken(authToken);
        user.setProfileURL(photoUrl);
        user.setName(name);
        user.setTokenType(providerID);

        return user;
    }


    private void checkAuthen() {
        Log.d(TAG, "CHECK AUTHEN");
        if (getAccount() == null) {
            Log.d(TAG, "CHECK AUTHEN = DON'T HAS ACCOUNT");
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.commit();

            Intent intent = new Intent(AuthenticaterActivity.this, LoginActivity.class);
            startActivityForResult(intent, 1);
        } else {
            ((Application) getApplicationContext()).user = getUserInfo();
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

    public void logout() {
        Account[] accounts = accountManager.getAccountsByType(AuthenticatorConstant.UNITY_PICKER_ACCOUNTYTPE);
        if (accounts.length != 0) {
            for (int i = 0; i < accounts.length; i++) {
                accountManager.clearPassword(accounts[i]);
                accountManager.invalidateAuthToken(AuthenticatorConstant.UNITY_PICKER_ACCOUNTYTPE,
                        accountManager.getAuthToken(accounts[i],
                                AuthenticatorConstant.UNITY_PICKER_AUTHTOKEN_TYPE,
                                null,
                                true,
                                new AccountManagerCallback<Bundle>() {
                                    @Override
                                    public void run(AccountManagerFuture<Bundle> future) {
                                        try {
                                            Log.d("invalidateAuthToken", future.getResult().toString());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, null).toString());

                if (Build.VERSION.SDK_INT < 23) { // use deprecated method
                    accountManager.removeAccount(accounts[i], new AccountManagerCallback<Boolean>() {
                        @Override
                        public void run(AccountManagerFuture<Boolean> future) {
                            try {
                                if (future.getResult()) {
                                    Log.d("ACCOUNT REMOVAL23", "ACCOUNT  REMOVED");
                                    checkAuthen();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }, null);
                } else {
                    accountManager.removeAccount(accounts[i], this, new AccountManagerCallback<Bundle>() {
                        @Override
                        public void run(AccountManagerFuture<Bundle> future) {
                            try {
                                if (future.getResult() != null) {
                                    Log.d("ACCOUNT REMOVAL", "ACCOUNT REMOVED");
                                    checkAuthen();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, null);
                }
            }
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.commit();

            FirebaseAuth.getInstance().signOut();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAuthen();
    }

    public Account getAccount() {
        AccountManager am = AccountManager.get(this);
        Account [] accounts = am.getAccountsByType(AuthenticatorConstant.UNITY_PICKER_ACCOUNTYTPE);

        if (accounts.length > 0) {
            return accounts[0];
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ((Application) getApplicationContext()).user = getUserInfo();
    }

}
