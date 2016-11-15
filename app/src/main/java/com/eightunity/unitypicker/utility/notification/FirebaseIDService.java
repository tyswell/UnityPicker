package com.eightunity.unitypicker.utility.notification;

import android.support.annotation.NonNull;
import android.util.Log;

import com.eightunity.unitypicker.R;
import com.eightunity.unitypicker.model.server.device.DeviceToken;
import com.eightunity.unitypicker.model.server.user.LoginReceive;
import com.eightunity.unitypicker.model.service.ResponseService;
import com.eightunity.unitypicker.service.ApiService;
import com.eightunity.unitypicker.utility.DeviceUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by deksen on 9/2/16 AD.
 */
public class FirebaseIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(final String token) {
        // TODO: Implement this method to send token to your app server.
        Log.d(TAG, "token notification ="+token);
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fUser != null) {
            fUser.getToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                @Override
                public void onComplete(@NonNull Task<GetTokenResult> task) {
                    String tokenId = task.getResult().getToken();
                    updateTokenService(tokenId, token);
                }
            });
        }
    }

    private void updateTokenService(String tokenId, String token) {
        Log.d(TAG, "UPDATE NOTIFICATION TOKEN : " + token);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_service_url))
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);

        DeviceToken deviceObj = new DeviceToken();
        deviceObj.setTokenNotification(token);
        deviceObj.setDeviceModel(DeviceUtil.getDeviceName());
        deviceObj.setTokenId(tokenId);

        Call<Boolean> call = service.updateToken(deviceObj);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, retrofit2.Response<Boolean> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "SUCCESS UPDATE TOKEN="+response.body());
                } else {
                    Log.e(TAG, "ERROR" + response.message());
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.d(TAG, "ERROR" + t.getMessage());
            }
        });



    }

}
