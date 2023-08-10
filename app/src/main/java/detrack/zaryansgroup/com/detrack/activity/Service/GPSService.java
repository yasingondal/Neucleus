package detrack.zaryansgroup.com.detrack.activity.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

import detrack.zaryansgroup.com.detrack.activity.Map.GPSTracker;
import detrack.zaryansgroup.com.detrack.activity.Model.Params;
import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;
import detrack.zaryansgroup.com.detrack.activity.ksoap.SendDataToService;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;

/**
 * Created by 6520 on 3/22/2016.
 */
public class GPSService extends Service {
    GPSTracker gps;
    SharedPrefs prefs;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Utility.logCatMsg("GPSService start..");
        prefs = new SharedPrefs(this);
        getLatLong();
        return START_STICKY;


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void getLatLong() {
        gps = new GPSTracker(GPSService.this);
        // check if GPS enabled
        if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            // \n is for new line
            //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

            Utility.logCatMsg("Lat: " + latitude + "Long: " + longitude);
            if (latitude != 0.0 && longitude != 0.0) {
                ArrayList<Params> parameters = new ArrayList<Params>();
                Params p1 = new Params();
                Params p2 = new Params();
                Params p3 = new Params();

                p1.setKey("ContactId");
                p1.setValue(prefs.getEmployeeID() + ""); // user_id

                p2.setKey("Lat");
                p2.setValue(latitude + "");

                p3.setKey("Long");
                p3.setValue(longitude + "");

                parameters.add(p1);
                parameters.add(p2);
                parameters.add(p3);
                updateVehiclePosition(Utility.BASE_LIVE_URL+"api/Vehicle/UpdateVehiclePosition?ContactId="+prefs.getEmployeeID()+"&Lat="+latitude+"&Long="+longitude);
//                Log.d("updatePosition",Utility.BASE_LIVE_URL+"api/Vehicle/UpdateVehiclePosition?ContactId="+prefs.getEmployeeID()+"&Lat="+latitude+"&Long="+longitude);
                //new UpdateLatLng("UpdateVehiclePosition", "ZEDtrack.asmx", parameters).execute();
            } else {
                Utility.logCatMsg("Lat: " + latitude + "Long: " + longitude);
            }
        } else {
            //gps.showSettingsAlert();
            Utility.logCatMsg("Location is Off");

        }
    }

    private class UpdateLatLng extends SendDataToService {

        public UpdateLatLng(String methodName, String className, ArrayList<Params> list) {
            super(GPSService.this, methodName, className, list);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            if (aVoid != null) {
                try {
                    if (aVoid.equals("[]")) {
                        Utility.logCatMsg("Lat Lng NOT Updated ");
                    } else {

                        Utility.logCatMsg("Lat Lng Updated successfully");
                    }
                } catch (Exception e) {
                    e.getMessage();
                    Utility.logCatMsg("User Error " + e);
                }
            } else {
                Utility.logCatMsg("****** NULL ******");
            }

        }
    }

    //update Vehicle position new api
    public void updateVehiclePosition(String url){
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        if (!response.contains("1")) {
                            Utility.logCatMsg("Lat Lng NOT Updated ");
                        } else {
                            Utility.logCatMsg("Lat Lng Updated successfully");
                        }
                    } catch (Exception e) {
                        e.getMessage();
                        Utility.logCatMsg("User Error " + e);
                    }
                } else {
                    Utility.logCatMsg("****** NULL ******");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

}
