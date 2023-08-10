package detrack.zaryansgroup.com.detrack.activity.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.provider.Settings;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;

import detrack.zaryansgroup.com.detrack.activity.activites.RegisterActivity;
import detrack.zaryansgroup.com.detrack.R;

/**
 * Created by HP on 2/20/2017.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d("firebaseMessage", remoteMessage.toString());
        showNotification(remoteMessage.getData().get("message"));
    }

    private void showNotification(String message) {


        Intent i1 = new Intent(this, RegisterActivity.class);
        i1.putExtra("status", "accept");
        PendingIntent pendingIntent1 = PendingIntent.getActivity(this, 0, i1, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("New Order Received")
                .setContentText("Tap here to see order details")
                .setVibrate(new long[]{1000, 1000})
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.zedlog1)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent1);
        builder.build().flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify((int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE), builder.build());

        //manager.cancel(0);
        //System.exit(0);
    }

}
