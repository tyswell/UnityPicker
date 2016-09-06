package com.eightunity.unitypicker.authenticator;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.eightunity.unitypicker.R;
import com.eightunity.unitypicker.authenticator.Constant.AuthenticatorConstant;
import com.eightunity.unitypicker.model.account.User;
import com.eightunity.unitypicker.model.service.ResponseService;
import com.eightunity.unitypicker.service.ApiService;
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
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by deksen on 8/12/16 AD.
 */
public class LoginActivity extends AccountAuthenticatorActivity {

    private String TAG = "LoginActivity";

    public static final String PARAM_USERNAME = "fb_email";
    public static final String PARAM_AUTHTOKEN_TYPE = "authtokenType";
    public static final String PROFILE_URL = "PROFILE_URL";
    private AccountManager mAccountManager;
    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;
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
        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(AppIndex.API).build();

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

        SignInButton googleBtn = (SignInButton) findViewById(R.id.login_google_button);
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
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
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.eightunity.unitypicker/http/host/path")
        );
        AppIndex.AppIndexApi.start(mGoogleApiClient, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.eightunity.unitypicker/http/host/path")
        );
        AppIndex.AppIndexApi.end(mGoogleApiClient, viewAction);
        mGoogleApiClient.disconnect();
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

                    User user = new User();
                    user.setUsername(username);
                    user.setToken(accessToken);
                    user.setTokenType(AuthenticatorConstant.FACEBOOK_ACCOUNTYTPE);
                    user.setProfileURL("https://graph.facebook.com/" + username + "/picture?type=large");
                    loginService(user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                mHandler.post(new DisplayException("API error:\n" + response.getError().getErrorMessage()));
            }
        }
    }

    private void setAuthenticator(final String username, final String token, final String tokenType, final String profileUrl) {
        Account account;
        if (mRequestNewAccount) {
            account = new Account(username, AuthenticatorConstant.UNITY_PICKER_ACCOUNTYTPE);
            mAccountManager.addAccountExplicitly(account, token, null);
        } else {
            account = new Account(mUsername, AuthenticatorConstant.UNITY_PICKER_ACCOUNTYTPE);
            mAccountManager.setPassword(account, token);
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mLoading != null) {
                    mLoading.dismiss();
                }
                final Intent intent = new Intent();
                intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, username);
                intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, tokenType);
                intent.putExtra(AccountManager.KEY_AUTHTOKEN, token);
                setAccountAuthenticatorResult(intent.getExtras());
                setResult(1, intent);
                finish();
            }
        });
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

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            String idToken = acct.getIdToken();
            Log.d(TAG, "ID Token: " + idToken);

            //TODO CALL SERVICE TO REGISTER
            User user = new User();
            user.setUsername(acct.getEmail());
            user.setToken(idToken);
            user.setTokenType(AuthenticatorConstant.GOOGLE_ACCOUNTYTPE);
            user.setProfileURL(acct.getPhotoUrl().toString());
            loginService(user);

//            Account account = new Account(acct.getEmail(), AuthenticatorConstant.UNITY_PICKER_ACCOUNTYTPE);
//            mAccountManager.addAccountExplicitly(account, idToken, null);
//            final Intent intent = new Intent();
//            intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, acct.getEmail());
//            intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, AuthenticatorConstant.GOOGLE_ACCOUNTYTPE);
//            intent.putExtra(AccountManager.KEY_AUTHTOKEN, idToken);
//            setAccountAuthenticatorResult(intent.getExtras());
//            setResult(1, intent);

            //finish();
        } else {
            // Signed out, show unauthenticated UI.
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

    private void loginService(final User user) {
        /*
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_service_url))
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);

        Call<ResponseService> call = service.login(user);
        call.enqueue(new Callback<ResponseService>() {
            @Override
            public void onResponse(Call<ResponseService> call, retrofit2.Response<ResponseService> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "SUCCESS LOGIN CODE="+response.body().getCode());
                    setAuthenticator(user.getUsername(), user.getToken(), user.getTokenType());
                } else {
                    Log.e(TAG, "ERROR" + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseService> call, Throwable t) {
                Log.d(TAG, "ERROR" + t.getMessage());
            }
        });
        */

        setAuthenticator(user.getUsername(), user.getToken(), user.getTokenType(), user.getProfileURL());
    }


}
