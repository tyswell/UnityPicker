package com.eightunity.unitypicker;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.eightunity.unitypicker.authenticator.Constant.AuthenticatorConstant;
import com.eightunity.unitypicker.authenticator.LoginActivity;
import com.eightunity.unitypicker.model.User;
import com.eightunity.unitypicker.ui.Application;
import com.eightunity.unitypicker.ui.AuthenticaterActivity;

import java.io.IOException;

/**
 * Created by chokechaic on 8/26/2016.
 */
public class UnityPicker extends AppCompatActivity {

    private AccountManager accountManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        accountManager = AccountManager.get(this);
        if (isAuthen()) {
            initData();
        }
    }

    private void initData() {
        AccountManager am = AccountManager.get(this);

        am.getAuthToken(getCurrentAccount(),
                AuthenticatorConstant.FACEBOOK_AUTHTOKEN_TYPE,
                null,
                true,
                new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> future) {
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
                        Boolean isFacebookLogin = true;
                        String username = getCurrentAccount().name;

                        user.setFacebookLogin(isFacebookLogin);
                        if (isFacebookLogin) {
                            user.setFacebookUserId(username);
                        } else {
                            user.setNativeUserId(username);
                        }

                        user.setToken(authToken);

                        Intent intent = new Intent(getBaseContext(), MainActivity.class);

                        ((Application) getApplicationContext()).user = user;

                        startActivityForResult(intent, 0);
                    }
                }, null);
    }

    private Account getCurrentAccount() {
        Account[] fbAccounts = accountManager.getAccountsByType(AuthenticatorConstant.FACEBOOK_ACCOUNTYTPE);
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
        Account [] accounts = am.getAccountsByType(AuthenticatorConstant.FACEBOOK_ACCOUNTYTPE);

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
}
