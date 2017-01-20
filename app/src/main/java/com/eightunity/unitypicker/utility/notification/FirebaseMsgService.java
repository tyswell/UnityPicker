package com.eightunity.unitypicker.utility.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.eightunity.unitypicker.MainActivity;
import com.eightunity.unitypicker.R;
import com.eightunity.unitypicker.UnityPicker;
import com.eightunity.unitypicker.database.DatabaseManager;
import com.eightunity.unitypicker.database.EMatchingDAO;
import com.eightunity.unitypicker.database.UnityPickerDB;
import com.eightunity.unitypicker.model.dao.EMatching;
import com.eightunity.unitypicker.utility.DateUtil;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by deksen on 9/2/16 AD.
 */
public class FirebaseMsgService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMsgService";
    private EMatchingDAO dao;
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String NOTIFICATION_INTENT = "ACTION_INTENT";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        EMatching eMatching = convertMsg(remoteMessage.getData());

        addToDB(eMatching);

        sendNotification(eMatching.getSearch_word_desc(), eMatching.getTitle_content());

        Intent registrationComplete = new Intent(REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String title, String messageBody) {
        Intent intent = new Intent(this, UnityPicker.class);
        intent.putExtra(NOTIFICATION_INTENT, "tempValue");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private EMatching convertMsg(Map<String, String> map) {
        EMatching eMatching = new EMatching();
        eMatching.setUser_id(map.get("user_id"));
        eMatching.setMatching_date(DateUtil.stringToDate(map.get("matching_date")));
        eMatching.setSeacrh_word_id(Integer.parseInt(map.get("seacrh_word_id")));
        eMatching.setSearch_word_desc(map.get("search_word_desc"));
        eMatching.setContent_id(Integer.parseInt(map.get("content_id")));
        eMatching.setTitle_content(map.get("title_content"));
        eMatching.setUrl(map.get("url"));
        eMatching.setWeb_name(map.get("web_name"));

        for (String key : map.keySet()) {
            Log.d(TAG, "KEY : "+key + " | value : "+map.get(key));
        }

        return eMatching;
    }

    private void addToDB(EMatching eMatching) {
        if (!DatabaseManager.isDBMInitial()) {
            initDatabase();
        }
        dao = new EMatchingDAO();
        dao.add(eMatching);
    }

    private void initDatabase() {
        UnityPickerDB db = new UnityPickerDB(this);
        DatabaseManager.initializeInstance(db);
    }

}
