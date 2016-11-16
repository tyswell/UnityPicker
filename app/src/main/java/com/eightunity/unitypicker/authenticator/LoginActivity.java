package com.eightunity.unitypicker.authenticator;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.eightunity.unitypicker.MainActivity;
import com.eightunity.unitypicker.R;
import com.eightunity.unitypicker.UnityPicker;
import com.eightunity.unitypicker.authenticator.Constant.AuthenticatorConstant;
import com.eightunity.unitypicker.database.ESearchWordDAO;
import com.eightunity.unitypicker.model.account.OSType;
import com.eightunity.unitypicker.model.account.User;
import com.eightunity.unitypicker.model.account.UserLoginType;
import com.eightunity.unitypicker.model.dao.ESearchWord;
import com.eightunity.unitypicker.model.search.Search;
import com.eightunity.unitypicker.model.server.search.Searching;
import com.eightunity.unitypicker.model.server.user.Device;
import com.eightunity.unitypicker.model.server.user.FacebookUser;
import com.eightunity.unitypicker.model.server.user.LoginReceive;
import com.eightunity.unitypicker.model.server.user.LoginResponse;
import com.eightunity.unitypicker.model.service.ResponseService;
import com.eightunity.unitypicker.service.ApiService;
import com.eightunity.unitypicker.ui.Application;
import com.eightunity.unitypicker.utility.DeviceUtil;
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
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
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

    private FirebaseAuth mAuth;
//    private FirebaseAuth.AuthStateListener mAuthListener;

    private ESearchWordDAO dao;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        FacebookSdk.sdkInitialize(this);

        dao = new ESearchWordDAO();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(AppIndex.API).build();

        setContentView(R.layout.activity_login);

        final LoginActivity activity = this;

        LoginButton facebookBtn = (LoginButton) findViewById(R.id.login_facebook_button);
        mCallbackManager = CallbackManager.Factory.create();
        facebookBtn.setReadPermissions("email", "public_profile", "user_managed_groups", "user_likes");
        facebookBtn.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
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




        mAuth = FirebaseAuth.getInstance();
//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                final FirebaseUser fuser = firebaseAuth.getCurrentUser();
//                if (fuser != null) {
//                    fuser.getToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
//                        public void onComplete(@NonNull Task<GetTokenResult> task) {
//                            if (task.isSuccessful()) {
//                                String idToken = task.getResult().getToken();
//                                Log.d(TAG, "idTokeAAAAA="+idToken);
//                                Log.d(TAG, "UID="+fuser.getUid());
//                                LoginReceive loginObj = setUserInfo(fuser, idToken);
//
//                                loginService(loginObj);
//                            } else {
//                                Log.d(TAG, "task.getException()="+task.getException());
//                            }
//                        }
//                    });
//                } else {
//                    Log.d(TAG, "onAuthStateChanged:signed_out");
//                }
//
//            }
//        };
    }

    private void logined() {
        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (fuser != null) {
            fuser.getToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                public void onComplete(@NonNull Task<GetTokenResult> task) {
                    if (task.isSuccessful()) {
                        String idToken = task.getResult().getToken();
                        Log.d(TAG, "idTokeAAAAA="+idToken);
                        Log.d(TAG, "UID="+fuser.getUid());
                        LoginReceive loginObj = setUserInfo(fuser, idToken);

//                        loginService(loginObj, fuser.getUid());
                        loginServiceTemp(loginObj, fuser.getUid());
                    } else {
                        Log.d(TAG, "task.getException()="+task.getException());
                    }
                }
            });
        } else {
            Log.d(TAG, "onAuthStateChanged:signed_out");
        }
    }

    private LoginReceive setUserInfo(FirebaseUser fuser, String tokenId) {
        LoginReceive loginObj = new LoginReceive();

        loginObj.setTokenId(tokenId);
        List<Integer> uids = dao.getAllId(fuser.getUid());
        Log.d(TAG, "uids.size())="+uids.size());
        loginObj.setSearchingIds(uids);
        loginObj.setUserLoginType(UserLoginType.FACEBOOK_LOGIN_TYPE_CODE);

        Device device = new Device();
        device.setOsTypeCode(OSType.ANDROID_OS);
        device.setTokenNotification(FirebaseInstanceId.getInstance().getToken());
        device.setDeviceModel(DeviceUtil.getDeviceName());
        loginObj.setDevice(device);

        FacebookUser fbUser = new FacebookUser();
        fbUser.setFacebookID(fuser.getProviderId());
        loginObj.setFacebookUser(fbUser);

        return loginObj;

//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        String name = fuser.getDisplayName();
//        String username = fuser.getEmail();
//        String providerID = fuser.getProviderId();
//        String photoUrl = fuser.getPhotoUrl().toString();
//
//        device.setOsType(OSType.ANDROID_OS);
//        device.setTokenNotification(refreshedToken);
//        device.setDeviceModel(DeviceUtil.getDeviceName());
//
//        loginObj.setUsername(username);
//        loginObj.setProfileURL(photoUrl);
//        loginObj.setDisplayName(name);
//        loginObj.setUserLoginType(UserLoginType.getCode(providerID));
//
//        user.setDevice(device);
//
//        return user;
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

//        mAuth.addAuthStateListener(mAuthListener);

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

//        if (mAuthListener != null) {
//            mAuth.removeAuthStateListener(mAuthListener);
//        }

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
//            mHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    mLoading.show();
//                }
//            });
//            Bundle parameters = new Bundle();
//            parameters.putString("fields", "id,name,first_name,middle_name,last_name,link,email");
//            GraphRequest gr = new GraphRequest(AccessToken.getCurrentAccessToken(),
//                    "me", parameters, HttpMethod.GET, new getUserInfo());
//            gr.executeAsync();
            Log.d(TAG, "facebook:onSuccess:" + loginResult);
            handleFacebookAccessToken(loginResult.getAccessToken());
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

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token.getToken());

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                logined();
            }
        });
    }

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
            }

        } else {
            // Signed out, show unauthenticated UI.
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                logined();
            }
        });
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

    private void loginServiceTemp(final LoginReceive loginObj, final String username) {
        addSearchDao(new ArrayList<Searching>(), username);
        finish();
    }

    private void loginService(final LoginReceive loginObj, final String userId) {
        Log.d(TAG, "LOGIN SERVICE : " + loginObj.getTokenId());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_service_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);

        Call<LoginResponse> call = service.login(loginObj);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "SUCCESS LOGIN CODE="+response.body());
                    LoginResponse loginResponse = response.body();
                    Log.d(TAG, "loginResponse.getSearching() = "+loginResponse.getSearching());
                    addSearchDao(loginResponse.getSearching(), userId);
                    finish();
                } else {
                    Log.e(TAG, "ERROR" + response.message());
                    FirebaseAuth.getInstance().signOut();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.d(TAG, "ERROR" + t.getMessage());
                FirebaseAuth.getInstance().signOut();
            }
        });
    }

    private void addSearchDao(List<Searching> searchings, String userId) {
        if (searchings != null) {
            for (Searching searching : searchings) {
                ESearchWord search = new ESearchWord();
                search.setDescription(searching.getDescription());
                search.setSearch_type(searching.getSearchTypeCode());
                search.setSearch_id(searching.getSearchingId());
                search.setUser_id(userId);
                dao.add(search);
            }
        }
    }


}
