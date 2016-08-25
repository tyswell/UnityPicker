package com.eightunity.unitypicker.authenticator;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eightunity.unitypicker.R;
import com.eightunity.unitypicker.authenticator.Constant.AuthenticatorConstant;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by deksen on 8/12/16 AD.
 */
public class LoginActivity extends AccountAuthenticatorActivity {

    public static final String PARAM_USERNAME = "fb_email";
    public static final String PARAM_AUTHTOKEN_TYPE = "authtokenType";
    private AccountManager mAccountManager;

    private String mUsername;
    private TextView loginStatusView;
    protected AlertDialog mDialog;
    protected ProgressDialog mLoading;
    protected boolean mRequestNewAccount = false;
    private CallbackManager mCallbackManager;
    public final Handler mHandler = new Handler();
    private SessionStatusCallback mStatusCallback = new SessionStatusCallback();

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_login);

        final LoginActivity activity = this;

        Button facebookBtn = (Button) findViewById(R.id.login_facebook_button);
        facebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().registerCallback(mCallbackManager, mStatusCallback);
                LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList(Authenticator.REQUIRED_PERMISSIONS));
            }
        });

        loginStatusView = (TextView) findViewById(R.id.loginStatusView);

        mLoading = new ProgressDialog(this);
        mLoading.setTitle("TEST FACEBBBOOOK");
        mLoading.setMessage("Loading ... ");
        mLoading.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mHandler.post(new Runnable() {
                    public void run() {
                        LoginActivity.this.finish();
                    }
                });
            }
        });

        mAccountManager = AccountManager.get(this);

        final Intent intent = getIntent();
        mUsername = intent.getStringExtra(PARAM_USERNAME);
        mRequestNewAccount = (mUsername == null);


        mCallbackManager = CallbackManager.Factory.create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private class SessionStatusCallback implements FacebookCallback<LoginResult> {

        @Override
        public void onSuccess(LoginResult loginResult) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mLoading.show();
                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,first_name,middle_name,last_name,link,email");
            GraphRequest gr = new GraphRequest(AccessToken.getCurrentAccessToken(),
                    "me", parameters, HttpMethod.GET, new getUserInfo());
            gr.executeAsync();
        }

        @Override
        public void onCancel() {
            loginStatusView.setText("Facebook login canceled.");
            loginStatusView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onError(FacebookException error) {
            loginStatusView.setText("Facebook login failed:\n" + error.getMessage());
            loginStatusView.setVisibility(View.VISIBLE);
        }
    }

    public class getUserInfo implements GraphRequest.Callback {

        @Override
        public void onCompleted(GraphResponse response) {
            Log.i("response profile", response.toString());
            if (response.getError() == null) {
                try {
                    JSONObject userInfo = response.getJSONObject();
                    String username = userInfo.getString("email");
                    if (username == null ||
                            username.length() == 0) {
                        username = AccessToken.getCurrentAccessToken().getToken();
                    }
                    final String accessToken = AccessToken.getCurrentAccessToken().getToken();

                    Account account;
                    if (mRequestNewAccount) {
                        account = new Account(username, AuthenticatorConstant.ACCOUNT_TYPE);
                        mAccountManager.addAccountExplicitly(account, accessToken, null);
                    } else {
                        account = new Account(mUsername, AuthenticatorConstant.ACCOUNT_TYPE);
                        mAccountManager.setPassword(account, accessToken);
                    }
                    final String finalUsername = username;
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mLoading != null) {
                                mLoading.dismiss();
                            }
                            final Intent intent = new Intent();
                            intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, finalUsername);
                            intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, AuthenticatorConstant.ACCOUNT_TYPE);
                            intent.putExtra(AccountManager.KEY_AUTHTOKEN, accessToken);
                            setAccountAuthenticatorResult(intent.getExtras());
                            setResult(1, intent);
                            finish();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                mHandler.post(new DisplayException("API error:\n" + response.getError().getErrorMessage()));
            }
        }
    }

    protected  class DisplayException implements Runnable {
        String mMessage;

        public DisplayException(String msg) {
            mMessage = msg;
        }

        @Override
        public void run() {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setTitle("Facebook Error");
            builder.setMessage(mMessage);
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    mDialog.dismiss();
                    mLoading.dismiss();
                    LoginActivity.this.finish();
                }
            });
            try {
                mDialog = builder.create();
                mDialog.show();
            } catch (Exception e) { }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mLoading != null) {
            try {
                mLoading.dismiss();
            } catch (Exception e) { }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLoading.dismiss();
    }
}
