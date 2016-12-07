package com.eightunity.unitypicker.authenticator;

import android.accounts.AccountAuthenticatorActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.eightunity.unitypicker.R;
import com.eightunity.unitypicker.database.ESearchWordDAO;
import com.eightunity.unitypicker.model.account.OSType;
import com.eightunity.unitypicker.model.dao.ESearchWord;
import com.eightunity.unitypicker.model.server.search.Searching;
import com.eightunity.unitypicker.model.server.user.Device;
import com.eightunity.unitypicker.model.server.user.LoginReceive;
import com.eightunity.unitypicker.model.server.user.LoginResponse;
import com.eightunity.unitypicker.service.ApiService;
import com.eightunity.unitypicker.service.CallBackAdaptor;
import com.eightunity.unitypicker.service.ServiceAdaptor;
import com.eightunity.unitypicker.ui.BaseActivity;
import com.eightunity.unitypicker.ui.ErrorDialog;
import com.eightunity.unitypicker.ui.TransparentProgressDialog;
import com.eightunity.unitypicker.utility.DeviceUtil;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by deksen on 8/12/16 AD.
 */
public class LoginActivity extends AppCompatActivity {

    private String TAG = "LoginActivity";

    public static final String PARAM_USERNAME = "fb_email";
    public static final String PARAM_AUTHTOKEN_TYPE = "authtokenType";
    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;
    private String mUsername;
    //    protected ProgressDialog mLoading;
    private TransparentProgressDialog mLoading;
    protected boolean mRequestNewAccount = false;
    private CallbackManager mCallbackManager;
    public final Handler mHandler = new Handler();

    private FirebaseAuth mAuth;

    private ESearchWordDAO dao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                ErrorDialog ed = new ErrorDialog();
                Log.d(TAG, "facebook:onError", error);
                ed.showDialog(LoginActivity.this, error.getMessage());
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

        mLoading = new TransparentProgressDialog(this);
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

        final Intent intent = getIntent();
        mUsername = intent.getStringExtra(PARAM_USERNAME);
        mRequestNewAccount = (mUsername == null);

        mAuth = FirebaseAuth.getInstance();
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
                        loginService(loginObj, fuser.getUid());
                        Log.d(TAG, "Notification token :"+loginObj.getDevice().getTokenNotification());
//                        loginServiceTemp(loginObj, fuser.getUid());
                    } else {
                        ErrorDialog errorDialog = new ErrorDialog();
                        Log.d(TAG, "task.getException()="+task.getException());
                        errorDialog.showDialog(LoginActivity.this, task.getException().getMessage());
                    }
                }
            });
        } else {
            Log.d(TAG, "onAuthStateChanged:signed_out");
        }
    }

    private LoginReceive setUserInfo(FirebaseUser fUser, String tokenId) {
        LoginReceive loginObj = new LoginReceive();

        loginObj.setTokenId(tokenId);
        List<Integer> searchIds = dao.getAllId(fUser.getUid());
        Log.d(TAG, "searchIds.size())="+searchIds.size());
        loginObj.setSearchingIds(searchIds);

        Device device = new Device();
        device.setOsTypeCode(OSType.ANDROID_OS);
        device.setTokenNotification(FirebaseInstanceId.getInstance().getToken());
        device.setDeviceModel(DeviceUtil.getDeviceName());
        loginObj.setDevice(device);

        return loginObj;
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

        mGoogleApiClient.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "Login Page",
                Uri.parse("http://host/path"),
                Uri.parse("android-app://com.eightunity.unitypicker/http/host/path")
        );
        AppIndex.AppIndexApi.start(mGoogleApiClient, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "Login Page",
                Uri.parse("http://host/path"),
                Uri.parse("android-app://com.eightunity.unitypicker/http/host/path")
        );
        AppIndex.AppIndexApi.end(mGoogleApiClient, viewAction);
        mGoogleApiClient.disconnect();
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token.getToken());
        mLoading.show();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
//                logined();
                loginServiceX();
            }
        });
    }

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount account = result.getSignInAccount();
            firebaseAuthWithGoogle(account);
        } else {
            ErrorDialog ed = new ErrorDialog();
            Log.e(TAG, "GOOGLE SIGNIN NOT SUCCESS :" + GoogleSignInStatusCodes.getStatusCodeString(result.getStatus().getStatusCode()));
            ed.showDialog(this, getResources().getString(R.string.google_signin_error_message));
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        mLoading.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                if (task.isSuccessful()) {
//                    logined();
          loginServiceX();
                } else {
                    mLoading.dismiss();
                    ErrorDialog ed = new ErrorDialog();
                    Log.d(TAG, "GOOGLE SIGNIN NOT SUCCESS :" + task.getException().getMessage());
                    ed.showDialog(LoginActivity.this, task.getException().getMessage());
                }
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
    public void onDestroy() {
        super.onDestroy();
        mLoading.dismiss();
    }

    private void loginServiceTemp(final LoginReceive loginObj, final String username) {
        addSearchDao(new ArrayList<Searching>(), username);
        finish();
    }

    private void loginServiceX() {

        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        fUser.getToken(true).addOnCompleteListener(this, new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<GetTokenResult> task) {

                if (task.isSuccessful()) {
                    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY);

                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .addInterceptor(logging)
                            .build();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(LoginActivity.this.getResources().getString(R.string.base_service_url))
                            .client(okHttpClient)
                            .addConverterFactory(JacksonConverterFactory.create())
                            .build();
                    ApiService service = retrofit.create(ApiService.class);

                    LoginReceive loginObj = setUserInfo(fUser, task.getResult().getToken());
                    Call<LoginResponse> call = service.login(loginObj);
                    call.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            if (response.isSuccessful()) {
                                Log.d(TAG, "loginResponse.getSearching() = "+response.body().getSearching());
                                addSearchDao(response.body().getSearching(), fUser.getUid());
                                finish();
                                mLoading.dismiss();
                            } else {
                                Log.e(TAG, "ERROR" + response.message());
                                ErrorDialog errorDialog = new ErrorDialog();
                                errorDialog.showDialog(LoginActivity.this, response.errorBody().toString());
                                mLoading.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            Log.d(TAG, "ERROR" + t.getMessage());
                            ErrorDialog errorDialog = new ErrorDialog();
                            errorDialog.showDialog(LoginActivity.this, t.getMessage());
                            mLoading.dismiss();
                        }
                    });
                } else {
                    ErrorDialog errorDialog = new ErrorDialog();
                    errorDialog.showDialog(LoginActivity.this, task.getException().getMessage());
                    mLoading.dismiss();
                }
            }
        });
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

                    ErrorDialog errorDialog = new ErrorDialog();
                    errorDialog.showDialog(LoginActivity.this, response.message());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.d(TAG, "ERROR" + t.getMessage());
                FirebaseAuth.getInstance().signOut();

                ErrorDialog errorDialog = new ErrorDialog();
                errorDialog.showDialog(LoginActivity.this, t.getMessage());
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
                search.setModified_date(searching.getCreateDate());
                search.setUser_id(userId);
                dao.add(search);
            }
        }
    }


}
