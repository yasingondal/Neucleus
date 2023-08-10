package detrack.zaryansgroup.com.detrack.activity.utilites;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.installations.InstallationTokenResult;

import java.util.HashMap;

import detrack.zaryansgroup.com.detrack.activity.activites.MainActivity;
import detrack.zaryansgroup.com.detrack.activity.retrofit.Api_Reto;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFCM {

    static String token = null;
    static String mac_address = null;
    private static int contactId ;
    public static final String REG_ID = "regId";
    private static final String APP_VERSION = "appVersion";

    public static void initFCM_Register(final Context context, int contactid){

        contactId = contactid;
        mac_address = Utility.getMacAddress(context);
        FirebaseInstallations.getInstance().getToken(true).addOnCompleteListener(new OnCompleteListener<InstallationTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<InstallationTokenResult> task) {
                token = task.getResult().getToken();
                sendTokenToServer(token);
                storeRegistrationId(context,token);
            }
        });

    }

    private static void sendTokenToServer(String token) {

        HashMap<String ,String> hashMap=new HashMap<String ,String>();
        hashMap.put("ContactId",String.valueOf(contactId));
        hashMap.put("Mac_address",mac_address);
        hashMap.put("GCMID",token);

        Api_Reto.getRetrofit().getRetrofit_services().firebaseTokenRegistration(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    public static void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = context.getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        int appVersion = getAppVersion(context);
        Utility.logCatMsg("Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(REG_ID, regId);
        editor.putInt(APP_VERSION, appVersion);
        editor.apply();
        editor.commit();
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Utility.logCatMsg("I never expected this! Going down, going down!" + e);
            throw new RuntimeException(e);
        }
    }

}
