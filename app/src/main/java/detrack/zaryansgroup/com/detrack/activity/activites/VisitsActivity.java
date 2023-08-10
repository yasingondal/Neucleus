package detrack.zaryansgroup.com.detrack.activity.activites;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import detrack.zaryansgroup.com.detrack.R;
import detrack.zaryansgroup.com.detrack.activity.Adapter.VisitedCustomerAdapter;
import detrack.zaryansgroup.com.detrack.activity.Model.CustomerVisitedModel;
import detrack.zaryansgroup.com.detrack.activity.Model.RegisterCustomerModel.RegisterdCustomerModel;
import detrack.zaryansgroup.com.detrack.activity.SQLlite.ZEDTrackDB;
import detrack.zaryansgroup.com.detrack.activity.Service.CompanyInfoService;
import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;
import detrack.zaryansgroup.com.detrack.activity.retrofit.Api_Reto;
import detrack.zaryansgroup.com.detrack.activity.utilites.ConnectionDetector;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import timber.log.Timber;

public class VisitsActivity extends AppCompatActivity {

    int rCurrentVisitSyncPosition;
    int rMaxPoistion;

    int CounterMaxTime;
    int secondsCounter;

    Dialog dialog;
    ProgressDialog progress;
    SharedPrefs prefs;

    List<CustomerVisitedModel> rVisitedCustomersList;
    List<CustomerVisitedModel> rMultiSyncCustomersList;

    RecyclerView rv_VisitedCustomers;
    VisitedCustomerAdapter visitedCustomerAdapter;
    ZEDTrackDB db;

    String date;

    int rPosition;

    String rVisitDateTime, rLongitude, rLatitude;
    String rImageName="null";

    int rRouteID, rCustomerId, rSalesmanID, rStatusID, rCompanyID, rCompanySiteID;

    boolean statusSync;
    String rUploadingCheck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visits);



        rCurrentVisitSyncPosition = -1;
        progress = new ProgressDialog(this);
        progress.setTitle("Please Wait Visits are Syncing to Server....");
        progress.setCancelable(true);


        rVisitedCustomersList = new ArrayList<>();
        rMultiSyncCustomersList = new ArrayList<>();

        db = new ZEDTrackDB(VisitsActivity.this);
        date = getCurrentDate();
        prefs = new SharedPrefs(this);

        getSupportActionBar().setTitle("Customer Visits");

        xmlinIt();
        rCollectVisitsFromLocalDb();
        rFilterList();
        rCollectData();
        rMakeReadyListForMultiSyncing();


        rMaxPoistion = rMultiSyncCustomersList.size()-1;
        visitedCustomerAdapter = new VisitedCustomerAdapter(rVisitedCustomersList,VisitsActivity.this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_VisitedCustomers.setLayoutManager(mLayoutManager);
        rv_VisitedCustomers.setAdapter(visitedCustomerAdapter);

    }



    private void rMakeReadyListForMultiSyncing() {
        for(int i=0;i<rVisitedCustomersList.size(); i++)
        {
            if(rVisitedCustomersList.get(i).getIsSync() == 0)
            {
                rMultiSyncCustomersList.add(rVisitedCustomersList.get(i));
            }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_add_visits,menu);
        MenuItem rBtnaddVisit   = menu.findItem(R.id.addVisitButton);
        MenuItem rSyncAllVisits = menu.findItem(R.id.btnSyncAllVisits);

        rBtnaddVisit.setOnMenuItemClickListener(item -> {
            startActivity(new Intent(VisitsActivity.this,AddNewVisitActivity.class));
            return false;
        });



        rSyncAllVisits.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                showMultiSyncVisitDialogue();
                return false;

            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    private void showMultiSyncVisitDialogue() {

        dialog = new Dialog(VisitsActivity.this);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);

        dialog.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        dialog.setContentView(R.layout.confirm_visit_sync_dialogue);

        TextView tvttitle = dialog.findViewById(R.id.tvtitle);
        Button btnyes = dialog.findViewById(R.id.btnyes);
        Button btnno = dialog.findViewById(R.id.btnno);

        tvttitle.setText(
                "Are You Sure About Sending " +
                        " \n" +
                        rMultiSyncCustomersList.size()+" Visits to Server on Single Click"
        );


        dialog.show();
        btnyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                if (ConnectionDetector.isConnectingToInternet(VisitsActivity.this)) {

                    CounterMaxTime = rMultiSyncCustomersList.size()*1000*4;
                    secondsCounter = CounterMaxTime/1000;
                    progress.show();
                    new CountDownTimer(CounterMaxTime, 4000) {
                        public void onTick(long millisUntilFinished) {
                            progress.setMessage("Time Remaining "+ secondsCounter +" Seconds");
                            secondsCounter = secondsCounter-4;
                            rCurrentVisitSyncPosition = rCurrentVisitSyncPosition +1;
                            uploadImageToServer(rCurrentVisitSyncPosition);
                            rSyncMultiVisitsToServer(rCurrentVisitSyncPosition);
                        }

                        public void onFinish() {
                            Toast.makeText(VisitsActivity.this, rMultiSyncCustomersList.size()+" Visit has been Synced Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(VisitsActivity.this,WelcomeActivity.class));
                            finish();
                        }
                    }.start();

                } else {
                    Utility.Toast(VisitsActivity.this, "Check network connection and try again");
                }

            }

        });



        btnno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });

    }




    private void rSyncMultiVisitsToServer(int rCurrentSycPosition) {
        progress.show();

        if(rUploadingCheck == "false"){
            rImageName = "null";
        }


//        if(rImageName.isEmpty()){
//            rImageName = "null";
//        }


        String api = "api/Visit/AddVisit?";
        String url = Utility.BASE_LIVE_URL +
                api + "VisitDateTime=" + rMultiSyncCustomersList.get(rCurrentSycPosition).getVisitDate() + "+" + rVisitedCustomersList.get(rCurrentSycPosition).getVisitTime()+
                "&RouteID=" + rMultiSyncCustomersList.get(rCurrentSycPosition).getRouteID() + "&CustomerId=" + rMultiSyncCustomersList.get(rCurrentSycPosition).getCustomerId()
                 + "&SalesmanID=" + prefs.getEmployeeID() + "&StatusID=" + rMultiSyncCustomersList.get(rCurrentSycPosition).getStatusID() + "&CompanyID=" + rCompanyID +
                "&CompanySiteID=" + rCompanySiteID +"&Longitude="+rMultiSyncCustomersList.get(rCurrentSycPosition).getLongitude()
                +"&Latitude="+rMultiSyncCustomersList.get(rCurrentSycPosition).getLatitude()
                +"&ImageName="+rImageName;



        Observable<String> hResponseObservable = Api_Reto.getRetrofit().getRetrofit_services().addNewVisit(url);
        CompositeDisposable hCompositeDisposable = new CompositeDisposable();

        Disposable hAddNewOrderDisposable = hResponseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                            if (response != null) {

                                try {

                                    JSONObject parentObject = new JSONObject(response);
                                    JSONArray tableArray = parentObject.getJSONArray("data");

                                    Timber.d("Response from api is "+tableArray);

                                    if (!(tableArray.length() > 0)) {
                                        Toast.makeText(VisitsActivity.this, "Please Try Manual for"+rMultiSyncCustomersList.get(rCurrentSycPosition).getCustomerName() + " Visit", Toast.LENGTH_SHORT).show();

                                    } else {

                                        statusSync = db.rUpdateVisitStatusToSync(rMultiSyncCustomersList.get(rCurrentSycPosition).getId());
                                        if(statusSync){
                                            Toast.makeText(VisitsActivity.this, "Visits Synced to Server Successfully", Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(VisitsActivity.this, "Something went Wrong Please Try Manually to sync "+rMultiSyncCustomersList.get(rCurrentSycPosition).getCustomerName()+" Visit", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } catch (Exception e) {
                                    Utility.Toast(VisitsActivity.this, "Please Try Manual for "+rMultiSyncCustomersList.get(rCurrentSycPosition).getCustomerName()+" Visit");
                                    e.getMessage();
                                    Timber.d("Error is"+e.getMessage());
                                }

                            } else {
                                // progressDialog.dismiss();
                                Utility.logCatMsg("****** NULL ******");
                                Utility.Toast(VisitsActivity.this, "Server error");
                            }


                        } /*OnNext*/,
                        throwable -> {
                            if (throwable instanceof ServerError) {
                                Toast.makeText(VisitsActivity.this, "Server error occurred", Toast.LENGTH_SHORT).show();
                            } else if (throwable instanceof NetworkError) {
                                Toast.makeText(VisitsActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                            } else if (throwable instanceof TimeoutError) {
                                Toast.makeText(VisitsActivity.this, "Connection timeout error", Toast.LENGTH_SHORT).show();
                            }
                        }/*On Error*/,
                        () -> {

                            hCompositeDisposable.dispose();

                        }/*On Complete*/

                );

        hCompositeDisposable.add(hAddNewOrderDisposable);





// converted from volley to retrofit...
//        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                if (response != null) {
//                    try {
//
//                        JSONObject parentObject = new JSONObject(response);
//                        JSONArray tableArray = parentObject.getJSONArray("Table");
//
//                        Timber.d("Response from api is "+tableArray);
//
//                        if (!(tableArray.length() > 0)) {
//                            Toast.makeText(VisitsActivity.this, "Please Try Manual for"+rMultiSyncCustomersList.get(rCurrentSycPosition).getCustomerName() + " Visit", Toast.LENGTH_SHORT).show();
//
//                        } else {
//
//                            statusSync = db.rUpdateVisitStatusToSync(rMultiSyncCustomersList.get(rCurrentSycPosition).getId());
//                            if(statusSync){
//                                Toast.makeText(VisitsActivity.this, "Visits Synced to Server Successfully", Toast.LENGTH_SHORT).show();
//                            }else{
//                                Toast.makeText(VisitsActivity.this, "Something went Wrong Please Try Manually to sync "+rMultiSyncCustomersList.get(rCurrentSycPosition).getCustomerName()+" Visit", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    } catch (Exception e) {
//                        Utility.Toast(VisitsActivity.this, "Please Try Manual for "+rMultiSyncCustomersList.get(rCurrentSycPosition).getCustomerName()+" Visit");
//                        e.getMessage();
//                        Timber.d("Error is"+e.getMessage());
//                    }
//                } else {
//                    Utility.Toast(VisitsActivity.this, "Please Try Manual for "+rMultiSyncCustomersList.get(rCurrentSycPosition).getCustomerName()+" Visit");
//                    Utility.logCatMsg("****** NULL ******");
//                    Timber.d("Response is Null case is running... ");
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                progress.dismiss();
//                if (error instanceof ServerError) {
//                    Toast.makeText(VisitsActivity.this, "Server Error Occurred for "+rMultiSyncCustomersList.get(rCurrentSycPosition).getCustomerName(), Toast.LENGTH_SHORT).show();
//                } else if (error instanceof NetworkError) {
//                    Toast.makeText(VisitsActivity.this, "Network Error Occurred for "+rMultiSyncCustomersList.get(rCurrentSycPosition).getCustomerName(), Toast.LENGTH_SHORT).show();
//                } else if (error instanceof TimeoutError) {
//                    Toast.makeText(VisitsActivity.this, "Time Out Error Occurred for "+rMultiSyncCustomersList.get(rCurrentSycPosition).getCustomerName(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        RequestQueue queue = Volley.newRequestQueue(VisitsActivity.this);
//        queue.add(request);
    }



    private void rCollectData() {

        Intent intent = getIntent();
        rPosition = intent.getIntExtra("position",123546);

        if(rPosition!=123546) {

            rImageName = rVisitedCustomersList.get(rPosition).getImageName();

            if(rImageName!=null)
            {
                uploadImageToServer(rPosition);
            }else{
                Timber.d("Image Name is Empty");
            }


            rVisitDateTime = rVisitedCustomersList.get(rPosition).getVisitDate() + "+" + rVisitedCustomersList.get(rPosition).getVisitTime();
            rRouteID = rVisitedCustomersList.get(rPosition).getRouteID();
            rCustomerId = rVisitedCustomersList.get(rPosition).getCustomerId();
            rLongitude = rVisitedCustomersList.get(rPosition).getLongitude();
            rLatitude = rVisitedCustomersList.get(rPosition).getLatitude();
            rStatusID = rVisitedCustomersList.get(rPosition).getStatusID();
            rSalesmanID = prefs.getEmployeeID();
            rCompanySiteID = Integer.parseInt(prefs.getCompanySiteID());
            rCompanyID = Integer.parseInt(prefs.getCompanyID());
            SyncSingleVisitToServer();

        }
        else{
            Timber.d("How do you think its a good logic ? :D ");
        }

    }

    private String uploadImageToServer(int rPosition) {

        rUploadingCheck = "false";
        rImageName = rVisitedCustomersList.get(rPosition).getImageName();
        File FilePath = readFileFromInternalStorage(rImageName);


        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("filename", FilePath.getName(),
                        RequestBody.create(
                                FilePath,
                                MediaType.parse("application/octet-stream")
                        )
                )
                .build();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(Utility.BASE_LIVE_URL+"api/POD/PostVisitImage")
                .method("POST", body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                rUploadingCheck = "false";
                rImageName="null";
                Timber.d("Onfailure issue is "+e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    rUploadingCheck = "true";
                    Timber.d("Image is uploaded in visits Success");
                }
            }

        });

        return rUploadingCheck;


    }

    private File readFileFromInternalStorage(String image_name) {
        // Reading file from internal Storage..
        File mypath = new File(android.os.Environment.getExternalStorageDirectory() + "/ZEDDelivery/" + image_name);
        return mypath;
    }





    private void SyncSingleVisitToServer() {

        Timber.d("The uploading check is "+rUploadingCheck);

        if(rUploadingCheck == "false"){
            rImageName = "null";
        }

        String foo = null;

//        if(rImageName == foo)
//        {
//            rImageName = "null";
//            Timber.d("Image is not attached");
//            Timber.d("Image name is "+rImageName);
//        }

        String api = "api/Visit/AddVisit?";
        String url = Utility.BASE_LIVE_URL +
                api + "VisitDateTime=" + rVisitDateTime +
                "&RouteID=" + rRouteID + "&CustomerId=" + rCustomerId
                + "&SalesmanID=" + rSalesmanID + "&StatusID=" + rStatusID + "&CompanyID=" + rCompanyID +
                "&CompanySiteID=" + rCompanySiteID +"&Longitude="+ rLongitude
                +"&Latitude="+ rLatitude+"&ImageName="+rImageName;

        Timber.d("Url call is " + url);



        Observable<String> hResponseObservable = Api_Reto.getRetrofit().getRetrofit_services().addNewVisit(url);
        CompositeDisposable hCompositeDisposable = new CompositeDisposable();

        Disposable hAddNewOrderDisposable = hResponseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                            if (response != null) {

                                try {

                                    JSONObject parentObject = new JSONObject(response);
                                    JSONArray tableArray = parentObject.getJSONArray("data");
                                    if (!(tableArray.length() > 0)) {
                                        Timber.d("Not Success");
                                    } else {

                                        Timber.d(">0 case is running");

                                        statusSync = db.rUpdateVisitStatusToSync(rVisitedCustomersList.get(rPosition).getId());
                                        Toast.makeText(VisitsActivity.this, "Visit has been Saved Successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(VisitsActivity.this,WelcomeActivity.class));
                                        finish();
                                    }
                                } catch (Exception e) {
                                    Utility.Toast(VisitsActivity.this, "Please Try Again ");
                                    e.getMessage();
                                    Utility.logCatMsg("User Error " + e);
                                }

                            } else {
                                // progressDialog.dismiss();
                                Utility.logCatMsg("****** NULL ******");
                                Utility.Toast(VisitsActivity.this, "Server error");
                            }


                        } /*OnNext*/,
                        throwable -> {
                            if (throwable instanceof ServerError) {
                                Toast.makeText(VisitsActivity.this, "Server error occurred", Toast.LENGTH_SHORT).show();
                            } else if (throwable instanceof NetworkError) {
                                Toast.makeText(VisitsActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                            } else if (throwable instanceof TimeoutError) {
                                Toast.makeText(VisitsActivity.this, "Connection timeout error", Toast.LENGTH_SHORT).show();
                            }
                        }/*On Error*/,
                        () -> {

                            hCompositeDisposable.dispose();

                        }/*On Complete*/

                );

        hCompositeDisposable.add(hAddNewOrderDisposable);






//                            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//                                @Override
//                                public void onResponse(String response) {
//
//                                    if (response != null) {
//                                        try {
//
//                                            JSONObject parentObject = new JSONObject(response);
//                                            JSONArray tableArray = parentObject.getJSONArray("Table");
//                                            if (!(tableArray.length() > 0)) {
//                                                Timber.d("Not Success");
//                                            } else {
//
//                                                statusSync = db.rUpdateVisitStatusToSync(rVisitedCustomersList.get(rPosition).getId());
//                                                Toast.makeText(VisitsActivity.this, "Visit has been Saved Successfully", Toast.LENGTH_SHORT).show();
//                                                startActivity(new Intent(VisitsActivity.this,WelcomeActivity.class));
//                                                finish();
//                                            }
//                                        } catch (Exception e) {
//                                            Utility.Toast(VisitsActivity.this, "Please Try Again ");
//                                            e.getMessage();
//                                            Utility.logCatMsg("User Error " + e);
//                                        }
//                                    } else {
//
//                                        Utility.logCatMsg("****** NULL ******");
//                                    }
//
//                                }
//                            }, new Response.ErrorListener() {
//                                @Override
//                                public void onErrorResponse(VolleyError error) {
//
//                                    if (error instanceof ServerError) {
//                                        Toast.makeText(VisitsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
//                                    } else if (error instanceof NetworkError) {
//                                        Toast.makeText(VisitsActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
//                                    } else if (error instanceof TimeoutError) {
//                                        Toast.makeText(VisitsActivity.this, "Time Out Error", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
//
//                            RequestQueue queue = Volley.newRequestQueue(VisitsActivity.this);
//                            queue.add(request);
    }



    private String getCurrentDate() {
        String currentDate;
        currentDate  = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        return currentDate;
    }



    private void rFilterList() {
        for(int i=0; i<rVisitedCustomersList.size(); i++)
        {
            if(rVisitedCustomersList.get(i).getIsSync() == 1 && !rVisitedCustomersList.get(i).getVisitDate().equalsIgnoreCase(date))
            {
                rVisitedCustomersList.remove(i);
            }
        }
    }



    private void rCollectVisitsFromLocalDb() {
        rVisitedCustomersList.clear();
        rVisitedCustomersList = db.getAllVisitDetails();
    }

    private void xmlinIt() {
        rv_VisitedCustomers = findViewById(R.id.rv_VisistedList);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
}