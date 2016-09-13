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
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.eightunity.unitypicker.authenticator.Constant.AuthenticatorConstant;
import com.eightunity.unitypicker.authenticator.LoginActivity;
import com.eightunity.unitypicker.database.DatabaseManager;
import com.eightunity.unitypicker.database.UnityPickerDB;
import com.eightunity.unitypicker.model.account.User;
import com.eightunity.unitypicker.ui.Application;
import com.eightunity.unitypicker.utility.notification.FirebaseMsgService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.io.IOException;

/**
 * Created by chokechaic on 8/26/2016.
 */
public class UnityPicker extends AppCompatActivity {

    private AccountManager accountManager;

    private static final String TAG = "UnityPicker";

    private static UnityPicker app = null;


    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 2404;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        accountManager = AccountManager.get(this);
        app = this;

        if (checkGooglePlayService()) {
            if (isAuthen()) {
                initData();
            }
        }
    }



    private boolean checkGooglePlayService() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        REQUEST_GOOGLE_PLAY_SERVICES).show();
            }

            return false;
        }

        return true;
    }

    private void initData() {
        AccountManager am = AccountManager.get(this);

        am.getAuthToken(getCurrentAccount(),
                AuthenticatorConstant.UNITY_PICKER_AUTHTOKEN_TYPE,
                null,
                true,
                new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> future) {
                        // SET AUTHENTICATOR
                        Bundle authTokenBundle = null;
                        try {
                            authTokenBundle = future.getResult();
                        } catch (OperationCanceledException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (AuthenticatorException e) {
                            e.printStackTrace();
                        }
                        User user = new User();

                        String authToken = authTokenBundle.get(AccountManager.KEY_AUTHTOKEN).toString();
                        String username = getCurrentAccount().name;

                        user.setUsername(username);
                        user.setToken(authToken);

                        // SET DATABSE
                        if (!DatabaseManager.isDBMInitial()) {
                            initDatabase();
                        }

                        Intent intent = new Intent(getBaseContext(), MainActivity.class);

                        if (getIntent().getStringExtra(FirebaseMsgService.NOTIFICATION_INTENT) != null) {
                            intent.putExtra(FirebaseMsgService.NOTIFICATION_INTENT, "tempValue");
                        }

                        ((Application) getApplicationContext()).user = user;

                        startActivityForResult(intent, 1);
                    }
                }, null);
    }

    private void initDatabase() {
        UnityPickerDB db = new UnityPickerDB(this);
        DatabaseManager.initializeInstance(db);
    }

    private Account getCurrentAccount() {
        Account[] fbAccounts = accountManager.getAccountsByType(AuthenticatorConstant.UNITY_PICKER_ACCOUNTYTPE);
        if (fbAccounts.length > 0) {
            return fbAccounts[0];
        }

        return null;
    }

    private boolean isAuthen() {
        if (getAccount() == null) {
            Intent intent = new Intent(UnityPicker.this, LoginActivity.class);
            startActivityForResult(intent, 1);
            return false;
        } else {
            return true;
        }
    }

    private Account getAccount() {
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
        initData();
    }

    public static Context getContext() {
        return app.getApplicationContext();
    }


}
