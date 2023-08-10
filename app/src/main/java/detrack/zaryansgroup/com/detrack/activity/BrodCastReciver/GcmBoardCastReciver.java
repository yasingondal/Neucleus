package detrack.zaryansgroup.com.detrack.activity.BrodCastReciver;

/**
 * Created by 6520 on 4/11/2016.
 */
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;


import androidx.legacy.content.WakefulBroadcastReceiver;

import detrack.zaryansgroup.com.detrack.activity.Service.GCMNotificationIntentService;

public class GcmBoardCastReciver  extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ComponentName comp = new ComponentName(context.getPackageName(),
                GCMNotificationIntentService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}