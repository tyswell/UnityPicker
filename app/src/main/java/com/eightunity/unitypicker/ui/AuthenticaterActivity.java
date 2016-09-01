package com.eightunity.unitypicker.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.eightunity.unitypicker.authenticator.Constant.AuthenticatorConstant;
import com.eightunity.unitypicker.authenticator.LoginActivity;


/**
 * Created by deksen on 5/15/16 AD.
 */
public class AuthenticaterActivity extends AppCompatActivity {

    private AccountManager accountManager;
    private Bundle bnd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountManager = AccountManager.get(this);
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

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAuthen();
    }

    /**
     * Add new account to the account manager
     * @param accountType
     * @param authTokenType
     */
    private void addNewAccount(String accountType, String authTokenType) {
        final AccountManagerFuture<Bundle> future = accountManager.addAccount(accountType, authTokenType, null, null, this, new AccountManagerCallback<Bundle>() {
            @Override
            public void run(AccountManagerFuture<Bundle> future) {
                try {
                    Log.d("udinic", "AddNewAccount Bundle is " + bnd);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, null);
    }

    protected void checkAuthen() {
        if (getAccount() == null) {
            Intent intent = new Intent(AuthenticaterActivity.this, LoginActivity.class);
            startActivityForResult(intent, 1);
        }
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
    }

}
