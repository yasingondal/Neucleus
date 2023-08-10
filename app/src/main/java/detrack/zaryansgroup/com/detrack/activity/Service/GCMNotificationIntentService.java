package detrack.zaryansgroup.com.detrack.activity.Service;



import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;


import detrack.zaryansgroup.com.detrack.activity.activites.RegisterActivity;
import detrack.zaryansgroup.com.detrack.activity.activites.WelcomeActivity;
import detrack.zaryansgroup.com.detrack.activity.BrodCastReciver.GcmBoardCastReciver;
import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;
import detrack.zaryansgroup.com.detrack.R;

/**
 * Created by 6520 on 4/11/2016.
 */
public class GCMNotificationIntentService  extends IntentService {
    SharedPrefs prefs;

    public   int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    public int notificationNumber = 1;

    public GCMNotificationIntentService() {

        super("GcmIntentService");
    }

    public static final String TAG = "GCMNotificationIntentSe";

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
//        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
//        prefs = new SharedPrefs(this);
//        String messageType = gcm.getMessageType(intent);
//
//        if (extras != null && !extras.isEmpty()) {
//            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
//                    .equals(messageType)) {
//                sendNotification("Send error: " + extras.toString());
//            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
//                    .equals(messageType)) {
//                sendNotification("Deleted messages on server: "
//                        + extras.toString());
//            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
//                    .equals(messageType)) {
//                sendNotification("Message " + extras.get(Config.MESSAGE_KEY));
//                Log.i(TAG, "Received: " + extras.toString());
//
//                BadgeUtils.setBadgeCount(this, notificationNumber, R.drawable.zedlog1);
//                notificationNumber++;
//                SyncDataFromServer SynData = new SyncDataFromServer(GCMNotificationIntentService.this);
//                SynData.GetJobs();
//            }
//        }

        GcmBoardCastReciver.completeWakefulIntent(intent);
    }

    private void sendNotification(String msg) {
        PendingIntent contentIntent;
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if(prefs.getEmployeeName().equals("")){
            contentIntent = PendingIntent.getActivity(this, 0,
                    new Intent(this, RegisterActivity.class).putExtra("syncedOrder","no"), 0);
        }
        else{
            contentIntent = PendingIntent.getActivity(this, 0,
                    new Intent(this, WelcomeActivity.class).putExtra("syncedOrder","no"), 0);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this);
        Notification notification;
        notification = mBuilder.setSmallIcon(R.drawable.zedlogo).
                setAutoCancel(true)
                .setContentTitle("New Order Received")
                .setContentIntent(contentIntent).setSound(soundUri)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.zedlog1)
                .setContentText("Tap here to see order detail")
                .build();
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(1, notification);
        }
    }
}