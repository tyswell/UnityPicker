package com.eightunity.unitypicker;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.eightunity.unitypicker.authenticator.Constant.AuthenticatorConstant;
import com.eightunity.unitypicker.authenticator.LoginActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getAccount() == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivityForResult(intent, 1);
        }
    }

    public Account getAccount() {
        AccountManager am = AccountManager.get(this);
        Account [] accounts = am.getAccountsByType(AuthenticatorConstant.ACCOUNT_TYPE);

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
