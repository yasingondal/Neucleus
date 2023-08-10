package detrack.zaryansgroup.com.detrack.activity.utilites;
import static detrack.zaryansgroup.com.detrack.activity.activites.WelcomeActivity.rAssignedOrders;
import static detrack.zaryansgroup.com.detrack.activity.activites.WelcomeActivity.rDeliveredOrders;
import static detrack.zaryansgroup.com.detrack.activity.activites.WelcomeActivity.rReceipts;
import static detrack.zaryansgroup.com.detrack.activity.activites.WelcomeActivity.rReturnedOrders;
import static detrack.zaryansgroup.com.detrack.activity.activites.WelcomeActivity.rVisits;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import detrack.zaryansgroup.com.detrack.R;
import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;
import detrack.zaryansgroup.com.detrack.activity.activites.RegisterActivity;
import detrack.zaryansgroup.com.detrack.activity.activites.SelectCustomerActivity;
import timber.log.Timber;

/**
 * Created by 6520 on 2/2/2016.
 */
public class Utility {

    private static final int FLAG_ACTIVITY_NEW_TASK = 1;
    private static final String KEY_RESTART_INTENT = "ABC";
    static String TAG = "zeddelivery";
    //public static final String WEBSERVICES_SERVER_IP = "deliverywebapi.zederp.net/";
    public static final String WEBSERVICES_SERVER_IP = "aquagreenwebapi.zederp.net/";
    //for Development version
    public static final String BASE_DEVELOPMENT_URL = "http://deliveryoldapidevelopment.zederp.net/";
    public static final String BASE_DEVELOPMENT_SALES_RETURN = "http://deliveryapidevelopment.zederp.net/";
//    //for Live version
//    public static final String BASE_SALES_RETURN_URL = "http://deliveryapinew.zederp.net/";
//    public static final String BASE_LIVE_URL = "http://deliveryapi.zederp.net/";
//    for Live version
  //  public static final String BASE_SALES_RETURN_URL = "http://aquagreenapinew.zederp.net/";//replace url which has new//old

 //   public static final String BASE_SALES_RETURN_URL = "http://appapinew.zeddelivery.com";
//    public static final String BASE_LIVE_URL = "http://aquagreenapi.zederp.net/"; // replace with no new in the link//ol
//      public static final String BASE_LIVE_URL = "http://stageapizeddelivery.zederp.net/"; // replace with no new in the link
//      public static final String CDownloadImages = "http://staging.zeddelivery.com/ContactImages/";
//        // Product Images base url demo
//      public static final String PDownloadImages = "http://staging.zeddelivery.com/ProductImages/";

//    upper one i have changed for to make the app

    //------------------------------------ UnCommented on 21 Nov 2021----------------------------

    //For Production
    public static final String BASE_SALES_RETURN_URL = "http://stageapinewzeddelivery.zederp.net/";
   // public static final String BASE_LIVE_URL = "http://appapi.zeddelivery.com/"; // replace with no new in the link

  //  public static final String CDownloadImages = "http://aquagreen.zeddelivery.com/ContactImages/";
    // Product Images base url demo
  //   public static final String PDownloadImages = "http://aquagreen.zeddelivery.com/ProductImages/";

     //--------------------------------------------API Given for Discount Policies

//    public static final String BASE_LIVE_URL = "http://stageapi.zeddelivery.com/";

    public static final String BASE_LIVE_URL = "http://nucleusapi.zeddelivery.com/";

//    public static final String BASE_LIVE_URL = "http://stagenucleusapi.zeddelivery.com/";

    public static final String CDownloadImages = "http://staging.zeddelivery.com/ContactImages/";
    // Product Images base url demo
    public static final String PDownloadImages = "http://staging.zeddelivery.com/ProductImages/";

    //------------------------------------ Commented On on 28 Oct 2021----------------------------

//    Demo Purpose Urls
//    public static final String BASE_SALES_RETURN_URL = "http://dwdapinew.zeddelivery.com/";

//    public static final String BASE_LIVE_URL = "http://dwdapi.zeddelivery.com/"; // replace with no new in the link

//    Url for uploading Images to online
//    public static final String ImageUploadingUrl = "http://appapi.zeddelivery.com/api/POD/UploadPodImage?";
//    Customer Images Base url demo

//    public static final String CDownloadImages = "http://dwd.zeddelivery.com/ContactImages/";

//    Product Images base url demo
//    public static final String PDownloadImages = "http://dwd.zeddelivery.com/ProductImages/";

    public static final int DB_VERSION = 1;


    public static final int GST_PERCENT = 0;


    public static String username = "it";
    public static String password = "pakistan123";
    public static ProgressDialog progressDialog;

    public static void logCatMsg(String msg) {
        Log.d(TAG, msg);
    }

    public static void Toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static String getCurrentTime() {
        String AM_PM;
        Calendar calendar = Calendar.getInstance();
        AM_PM = calendar.get(Calendar.AM_PM) == 0 ? "am" : "pm";
        return calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + " " + AM_PM;
    }

    public static void clearAppData(Context context) {
        try {
            // clearing app data
            String packageName = context.getPackageName();
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("pm clear "+packageName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getTime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
        return sdf.format(cal.getTime());
    }

    public static String getTimeUpdated() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(cal.getTime());
    }


    public static String getMacAddress(@NonNull Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        return wInfo.getMacAddress();
    }


    public static void showChangedToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static String generateRandomCode() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

    public static void generateHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA1");
                md.update(signature.toByteArray());
                Utility.logCatMsg("HashKey: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getPhoto(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public static String getCurrentDate() {
        String date = "";
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        if (mDay < 10 && mMonth < 10) {

            date = mYear + "-0" + (mMonth + 1) + "-0" + mDay;
        } else if (mDay < 10 && mMonth > 10) {
            date = mYear + "-" + (mMonth + 1) + "-0" + mDay;
        } else if (mMonth < 10) {

            date = mYear + "-0" + (mMonth + 1) + "-" + mDay;

        } else {

            date = mYear + "-" + (mMonth + 1) + "-" + mDay;

        }
        return date;
    }


    public static String getDateForOrder() {
        String date = "";
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        if (mDay < 10 && mMonth < 10) {

            date = mYear + "-0" + (mMonth + 1) + "-0" + mDay;
        } else if (mDay < 10 && mMonth > 10) {
            date = mYear + "-" + (mMonth + 1) + "-0" + mDay;
        } else if (mMonth < 10) {

            date = mYear + "-0" + (mMonth + 1) + "-" + mDay;

        } else {

            date = mYear + "-" + (mMonth + 1) + "-" + mDay;

        }
        return date;
    }

    public static String getCurrentDateForV() {
        String currentDate;
        currentDate  = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        return currentDate;
    }

    public static Bitmap getImage(String imgName) {
        String dirName = "ZEDDelivery";
        File direct = new File(Environment.getExternalStorageDirectory(), dirName);
        if (direct.exists()) {
            File file = new File(new File("/sdcard/" + dirName + "/"), imgName);
            Utility.logCatMsg("File Path " + file.getPath());
            // Get the dimensions of the bitmap
            if (file.exists()) {
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(file.getAbsolutePath(), bmOptions);
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;

                // Determine how much to scale down the image
                int scaleFactor = Math.min(photoW / 150, photoH / 150);

                // Decode the image file into a Bitmap sized to fill the View
                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;
                bmOptions.inPurgeable = true;

                return BitmapFactory.decodeFile(file.getAbsolutePath(), bmOptions);
            } else {
                return null;
            }
        } else {
            Utility.logCatMsg("Dir not found..");
        }
        return null;
    }

    public static void HideKeyBoard(View v, Context context) {
        if (v != null) {
            InputMethodManager inm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public static void logoutDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_custom_title, null);
        TextView tvCustomTitle = view.findViewById(R.id.tvCustomTitle);
        tvCustomTitle.setText("Logout");
        builder.setCustomTitle(view);
        builder.setMessage("Make sure you upload your orders to server,other wise you will lost it.")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Timber.d(
                                "rVisits : "+rVisits
                                +" rAssignedOrders : "+rAssignedOrders
                                +" rDeliveredOrders : "+rDeliveredOrders
                                +" rReturnedOrders : "+rReturnedOrders
                                +" rReceipts : "+rReceipts
                        );

                        if(
                                rVisits==0 && rAssignedOrders==0 && rDeliveredOrders==0
                                        && rReturnedOrders==0 && rReceipts==0
                        )
                        {

                            Timber.d("Zeero case is running in logout");

                        SharedPrefs sharedPrefs = new SharedPrefs(context);
                        sharedPrefs.ClearPrefs();
                        sharedPrefs.hSetLogout(true);
                        Intent intent = new Intent(context, RegisterActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        clearAppData(context);
                        context.startActivity(intent);

                        }
                        else
                            {

                                if(rVisits>0){
                                    Toast.makeText(context, "Sorry you can't logout before Syncing "+rVisits+" Visits", Toast.LENGTH_SHORT).show();
                                }else if(rAssignedOrders>0){
                                    Toast.makeText(context, "Sorry you can't logout before Syncing "+rAssignedOrders+" Assign Orders", Toast.LENGTH_SHORT).show();
                                }else if(rDeliveredOrders>0){
                                    Toast.makeText(context, "Sorry you can't logout before Syncing "+rDeliveredOrders+" Delivered Orders", Toast.LENGTH_SHORT).show();
                                }
                                else if(rReturnedOrders>0){
                                    Toast.makeText(context, "Sorry you can't logout before Syncing "+rReturnedOrders+" Return Orders", Toast.LENGTH_SHORT).show();
                                }else if(rReceipts>0){
                                    Toast.makeText(context, "Sorry you can't logout before Syncing "+rReceipts+" Receipts ", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, "Sorry Please Try again, There is something went wrong.", Toast.LENGTH_SHORT).show();
                                }

                            }

                    }
                }).setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void userInfoDialog(final Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.userinfo_custom_dialog);
        dialog.getWindow().setLayout((6 * width) / 7, (4 * height) / 12);
        TextView tvProfileName = dialog.findViewById(R.id.tvProfileName);
        tvProfileName.setText(new SharedPrefs(context).getEmployeeName());
        TextView tvVehicleName = dialog.findViewById(R.id.tvVehicleName);
        tvVehicleName.setText(new SharedPrefs(context).getVehicleName());
        TextView tvDesignation = dialog.findViewById(R.id.tvDesignation);
        tvDesignation.setText(new SharedPrefs(context).getDesignation());
        dialog.show();
    }

    public static void salesReturnDialog(final Context context) {

//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//        alertDialogBuilder.setTitle("Sales Return").setMessage("Select type of Sales Return")
//                .setPositiveButton("Item Wise", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                if (new SharedPrefs(context).getView().equals("secondView")) {
//                    context.startActivity(new Intent(context, TakeOrder.class).putExtra("addOrder","true"));
//                  dialog.dismiss();
//
//                } else {


                        context.startActivity(new Intent(context, SelectCustomerActivity.class)
                                .putExtra("updateLocation", false).putExtra("addOrder", "false"));



//                        dialog.dismiss();
//                }
//                    }
//                }).setNegativeButton("Order Wise", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                Intent intent = new Intent(context, ReturnOrderSearchActivity.class);
//                context.startActivity(intent);
//            }
//        }).show();

    }


    public static void setProgressDialog(Context context, String title, String msg) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(title);
        progressDialog.setMessage(msg);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.setCancelable(false);
    }

    public static void showProgressDialog() {
        if (progressDialog != null) progressDialog.show();
    }

    public static void hideProgressDialog() {
        if (progressDialog != null) progressDialog.dismiss();
    }
}
