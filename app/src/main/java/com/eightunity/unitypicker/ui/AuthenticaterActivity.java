package com.eightunity.unitypicker.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.eightunity.unitypicker.authenticator.LoginActivity;
import com.eightunity.unitypicker.model.account.Device;
import com.eightunity.unitypicker.model.account.FacebookUser;
import com.eightunity.unitypicker.model.account.GoogleUser;
import com.eightunity.unitypicker.model.account.OSType;
import com.eightunity.unitypicker.model.account.User;
import com.eightunity.unitypicker.model.account.UserLoginType;
import com.eightunity.unitypicker.utility.DeviceUtil;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;


/**
 * Created by deksen on 5/15/16 AD.
 */
public class AuthenticaterActivity extends AppCompatActivity {

    private Bundle bnd;


    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 2404;

    private static final String TAG = "AuthenticaterActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        User user = getUser();
        if (user.getUserLoginType() == UserLoginType.FACEBOOK_LOGIN_TYPE_CODE) {
            FacebookSdk.sdkInitialize(getApplicationContext());
            LoginManager.getInstance().logOut();
        }

        FirebaseAuth.getInstance().signOut();
    }

    public FirebaseUser getAccount() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public User getUser() {
        User user = new User();

        FirebaseUser fUser = getAccount();
        if (fUser != null) {
            user.setEmail(fUser.getEmail());
            user.setUserId(fUser.getUid());
            user.setDisplayName(fUser.getDisplayName());
            user.setProfileURL(fUser.getPhotoUrl().toString());
            user.setUserLoginType(UserLoginType.getCode(fUser.getProviders()));

            if (user.getUserLoginType() == UserLoginType.FACEBOOK_LOGIN_TYPE_CODE) {
                FacebookUser fbUser = new FacebookUser();
                user.setFacebookUser(fbUser);
            } else if (user.getUserLoginType() == UserLoginType.GOOGLE_LOGIN_TYPE_CODE) {
                GoogleUser ggUser = new GoogleUser();
                user.setGoogleUser(ggUser);
            }

            Device device = new Device();
            device.setOsTypeCode(OSType.ANDROID_OS);
            device.setTokenNotification(FirebaseInstanceId.getInstance().getToken());
            device.setDeviceModel(DeviceUtil.getDeviceName());
            user.setDevice(device);

            return user;
        } else {
            Log.d(TAG, "GET CURRENT USER IS NULL");

            FacebookSdk.sdkInitialize(getApplicationContext());
            LoginManager.getInstance().logOut();

            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, 1);

            return null;
        }
    }

}
