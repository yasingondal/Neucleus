package detrack.zaryansgroup.com.detrack.activity.activites;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import detrack.zaryansgroup.com.detrack.R;
import detrack.zaryansgroup.com.detrack.activity.Adapter.VehicleListAdapter;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyDetailsModel.CompanyDetailsListModel;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyDetailsModel.CompanyDetailsModel;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.CompanyItemListModel;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.DeliveryItemModel;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyRouteModel.CompanyRouteListModel;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyRouteModel.RouteModel;
import detrack.zaryansgroup.com.detrack.activity.Model.CustomerPriceModel.CustomerPriceListModel;
import detrack.zaryansgroup.com.detrack.activity.Model.LoginModel.LoginListModel;
import detrack.zaryansgroup.com.detrack.activity.Model.LoginModel.LoginModel;
import detrack.zaryansgroup.com.detrack.activity.Model.Params;
import detrack.zaryansgroup.com.detrack.activity.Model.RegisterCustomerModel.RegisterdCustomerListModel;
import detrack.zaryansgroup.com.detrack.activity.Model.RegisterCustomerModel.RegisterdCustomerModel;
import detrack.zaryansgroup.com.detrack.activity.Model.VehiclesModel.VehicleListModel;
import detrack.zaryansgroup.com.detrack.activity.Model.VehiclesModel.VehicleModel;
import detrack.zaryansgroup.com.detrack.activity.SQLlite.ZEDTrackDB;
import detrack.zaryansgroup.com.detrack.activity.Service.CompanyInfoService;
import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;
import detrack.zaryansgroup.com.detrack.activity.SyncDataServerToSqliteDB.SyncDataFromServer;
import detrack.zaryansgroup.com.detrack.activity.utilites.ConnectionDetector;
import detrack.zaryansgroup.com.detrack.activity.utilites.RegisterFCM;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import detrack.zaryansgroup.com.detrack.activity.viewmodels.Response_Login_VM;
import timber.log.Timber;

public class RegisterActivity extends AppCompatActivity {

    int CountBank=0;
    ProgressDialog progress;

    ZEDTrackDB db;
    ArrayList<RegisterdCustomerModel> feedRegCustomerList;
    ArrayList<DeliveryItemModel> feedCompanyItemList;

    // ArrayListBankFeed Will come here...
    ArrayList<Params> paramsArrayList;
    ArrayList<RouteModel> routelist;
    Button loginBtn;
    EditText UserNameET, PasswordET;
    Context context;
    String regId;
    public static final String REG_ID = "regId";
    private static final String APP_VERSION = "appVersion";
    SharedPrefs prefs;
    CheckBox isStayLogin;
    boolean StayLogin = false;
    private ProgressDialog initialFileDilog;
    String firebaseToken = "";


    private ProgressDialog dialog;
    Response_Login_VM response_login_vm;
    boolean refreshData=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        context = getApplicationContext();
        UserNameET = findViewById(R.id.user_name);
        PasswordET = findViewById(R.id.user_password);
        loginBtn = findViewById(R.id.btn_login);
        isStayLogin = findViewById(R.id.islogin);
        prefs = new SharedPrefs(this);
        db = new ZEDTrackDB(this);
        dialog = new ProgressDialog(RegisterActivity.this);
        dialog.setCancelable(false);

        progress = new ProgressDialog(this);
        progress.setMessage("Loading data, please wait");


        initialFileDilog = new ProgressDialog(RegisterActivity.this);
        if (getActionBar() != null) {
            setUpActionBar(getSupportActionBar());
        }

        paramsArrayList = new ArrayList<>();
        feedRegCustomerList = new ArrayList<>();
        feedCompanyItemList = new ArrayList<>();
        routelist = new ArrayList<>();
        // here i will initialize my banks arraylist


        //login ViewModel initialize
        response_login_vm = new ViewModelProvider(this).get(Response_Login_VM.class);
        response_login_vm.init();

        // registering Observers
        boolean hIsLogout = prefs.hGetLogOut();


        hSubscribeObservers();

        loginBtn.setOnClickListener(v -> {

            View view = RegisterActivity.this.getCurrentFocus();
            Utility.HideKeyBoard(v, context);
            if (ConnectionDetector.isConnectingToInternet(RegisterActivity.this)) {
                if (UserNameET.getText().toString().equals("") || PasswordET.getText().toString().equals("")) {
                    Utility.Toast(RegisterActivity.this, "Field cannot be empty.");
                } else {
                    prefs.hSetLogout(false);
                    if (!prefs.hGetLogOut()) {
                        hSubscribeObservers();
                    }

                    ArrayList<Params> parameter = new ArrayList<Params>();
                    Params p1 = new Params();
                    Params p2 = new Params();
                    p1.setKey("UserName");
                    p1.setValue(UserNameET.getText().toString());

                    p2.setKey("Password");
                    p2.setValue(PasswordET.getText().toString());

                    parameter.add(p1);
                    parameter.add(p2);

                    dialog.setMessage("Login...");
                    dialog.show();
                    response_login_vm.ReqLogin(UserNameET.getText().toString(), PasswordET.getText().toString());
                }
            } else {
                Utility.Toast(RegisterActivity.this, "Check network connection and try again");
            }

        });


//        isStayLogin.setOnClickListener(v -> StayLogin = isStayLogin.isChecked());
//        if (prefs.IsStayLogin()) {
//            Intent intent = new Intent(RegisterActivity.this, WelcomeActivity.class);
//            startActivity(intent);
//            overridePendingTransition(R.anim.right_in, R.anim.left_out);
//            finish();
//
//            Timber.d("SiteId="+prefs.getCompanySiteID()+"");
//            Timber.d("CompanyId="+prefs.getCompanyID()+"");
//            Timber.d("SiteId="+prefs.getUserId()+"");
//
//        }

    }


    private void hSubscribeObservers() {

        if (!prefs.hGetLogOut()) {


            requestLoginVM();
            getRegisterCustomerInfoVM();
            getCompanyRoutesVM();
            getCompanyDetailsVM();




//            response_login_vm.getCompanyVehicles().observe(
//                    this,
//                    vehicleListModel -> {
//                        CountBank++;
//                        hShowVehicleDialog(vehicleListModel);
//                    }
//            );

        }


    }

    private void refreshLocalDbData() {
        if (ConnectionDetector.isConnectingToInternet(RegisterActivity.this)) {
            startService(new Intent(RegisterActivity.this, CompanyInfoService.class));
            refreshData = true;
        }
    }

    private void hShowVehicleDialog(VehicleListModel vehicleListModel) {

        if (vehicleListModel != null) {
            List<VehicleModel> tlist = vehicleListModel.getTable();
            if (tlist.size() > 0) {
                selectVehicleDialog((ArrayList<VehicleModel>) tlist);
                initialFileDilog.dismiss();
            }
        } else {
            Toast.makeText(context, "No Vehicle Found", Toast.LENGTH_SHORT).show();
            initialFileDilog.dismiss();
        }


    }


    private void requestLoginVM() {

        final LiveData<LoginListModel> logindataObserver = response_login_vm.getLogindata();

        logindataObserver.observe(this, new Observer<LoginListModel>() {
            @Override
            public void onChanged(LoginListModel loginListModel) {



                if (loginListModel != null) {
                    List<LoginModel> loginModel = loginListModel.getTable();

                    if (loginModel.size() > 0) {

                        Timber.d("requestLoginVM is correct");

                        LoginModel responce_loginModel = loginModel.get(0);

                        prefs.SetUserId(responce_loginModel.getUserId());
                        prefs.SetIsFOCRequired(responce_loginModel.getIsFOCRequired());
                        prefs.setCompanyID(responce_loginModel.getCompanyID());
                        prefs.setCompanySiteID(responce_loginModel.getCompanySiteID());
                        prefs.setUserName(responce_loginModel.getUsername());
                        prefs.setUserPassword(responce_loginModel.getPassword());
                        prefs.setEmployeeID(responce_loginModel.getEmployeeID());
                        prefs.setEmployeeName(responce_loginModel.getEmployeeName() == null ? "N/A" : responce_loginModel.getEmployeeName());
                        prefs.setCompanyName(responce_loginModel.getEmployeeName() == null ? "N/A" : responce_loginModel.getEmployeeName());


                        prefs.setDesignation(responce_loginModel.getUserRole());

                        Utility.Toast(RegisterActivity.this, "Login Successfully..");
                        initialFileDilog.setMessage("Please Wait Saving Initial Files");
//                        GetGCMId();
                        GetFCM();

                    } else {
                        dialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Username / Password not correct", Toast.LENGTH_SHORT).show();
                    }
                }
                logindataObserver.removeObservers(RegisterActivity.this);

            }
        });
    }

    private void GetFCM() {
        regId = getRegistrationId(context);
        //init observer for customer info
        if (TextUtils.isEmpty(regId)) {
            if (prefs.getEmployeeID() != 0) {
                RegisterFCM.initFCM_Register(this, prefs.getEmployeeID());
                new CountDownTimer(1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        if (!getRegistrationId(context).isEmpty()) {

                            dialog.dismiss();
                            initialFileDilog.show();
                            response_login_vm.ReqRegisterCustomerInfo(prefs.getCompanyID(), prefs.getCompanySiteID(),prefs.getUserId());
                        } else {
                            dialog.dismiss();
                            initialFileDilog.dismiss();
                            Utility.Toast(RegisterActivity.this, "Failed try again");
                        }
                    }
                }.start();
            } else {
                dialog.dismiss();
                Toast.makeText(context, "Employee id not found", Toast.LENGTH_SHORT).show();
            }
            Utility.logCatMsg("registerGCM - successfully registered with GCM server - regId: " + regId);
        } else {

            dialog.dismiss();
            Utility.logCatMsg("RegId already available. RegId: 1" + regId);
            if (prefs.getEmployeeID() != 0) {

                initialFileDilog.show();
                response_login_vm.ReqRegisterCustomerInfo(prefs.getCompanyID(), prefs.getCompanySiteID(),prefs.getUserId());
            }
        }

    }


    private void getRegisterCustomerInfoVM() {

        final LiveData<RegisterdCustomerListModel> customerinfodataObserver = response_login_vm.getRegisteredCustomerInfo();
        customerinfodataObserver.observe(this, new Observer<RegisterdCustomerListModel>() {
            @Override
            public void onChanged(RegisterdCustomerListModel registerdCustomerListModel) {



                if (registerdCustomerListModel != null) {
                    feedRegCustomerList.clear();
                    List<RegisterdCustomerModel> feedArray = registerdCustomerListModel.getTable();

                    Timber.d("The customer feedsize is "+feedArray.size());

                    if (feedArray.size() > 0) {

                        Timber.d("CustomerVM is correct");

                        feedRegCustomerList.addAll(feedArray);



//                        getCompanyRoutesMethod(Utility.BASE_LIVE_URL +"api/Company/GetRegisterCompanyRoutes?Company_id="
//                                + prefs.getCompanyID() + "&CompanySiteId=" + prefs.getCompanySiteID());
                        response_login_vm.ReqCompanyRoutes(prefs.getCompanyID(), prefs.getCompanySiteID(),prefs.getUserId());
                    } else {
                        initialFileDilog.dismiss();

                    }
                } else {

                    initialFileDilog.dismiss();
                    Utility.Toast(RegisterActivity.this, "Failed....Please Assign Customers");
                }
                customerinfodataObserver.removeObserver(this);

            }
        });
    }

    private void getCompanyRoutesVM() {
        final LiveData<CompanyRouteListModel> routedataObserver = response_login_vm.getCompanyRoute();

        routedataObserver.observe(this, new Observer<CompanyRouteListModel>() {
            @Override
            public void onChanged(CompanyRouteListModel companyRouteListModel) {


                routelist.clear();
                List<RouteModel> feedArray = companyRouteListModel.getTable();
                
//                if (feedArray.size() > 0) {
                    routelist.addAll(feedArray);
//                   getCompanyDetailsMethod(Utility.BASE_LIVE_URL +"api/Company/CompanyDetails?Company_id=" + prefs.getCompanyID());
                    response_login_vm.ReqCompanyDetails(prefs.getCompanyID());


//                } else {
//                    Log.d("companyroute", "null");
//                    Toast.makeText(RegisterActivity.this, "Please Assign Routes to Current User", Toast.LENGTH_SHORT).show();
//                    initialFileDilog.dismiss();
//                }


                routedataObserver.removeObserver(this);

            }
        });
    }

    private void getCompanyDetailsVM() {

        final LiveData<CompanyDetailsListModel> detailsdataObserver = response_login_vm.getCompanyDetails();
        detailsdataObserver.observe(this, new Observer<CompanyDetailsListModel>() {
            @Override
            public void onChanged(CompanyDetailsListModel companyDetailsListModel) {


                if (companyDetailsListModel != null) {
                    if (companyDetailsListModel.getTable().size() > 0) {
                        CompanyDetailsModel feedObj = companyDetailsListModel.getTable().get(0);


                        String companyName = feedObj.getCompanyTitle();
                        String companyLog = feedObj.getLogo();
                        String companyview = feedObj.getLayout();

                        //For Controlling Discount and GST on TotalBill Activity
                        String tempIsGSTVisible = feedObj.getIsGSTVisible();
                        String tempIsDiscountVisible = feedObj.getIsDiscountVisible();


                        prefs.setIsDiscountVisible(tempIsDiscountVisible);
                        prefs.setIsGSTVisible(tempIsGSTVisible);
                        prefs.setCurrency(feedObj.getCurrency());




                        if (companyview.equalsIgnoreCase("2nd View")) {
                            prefs.setView("secondView");
                        }


                        if (!companyName.equals("null"))
                            new SharedPrefs(RegisterActivity.this).setCompanyName(companyName);
                        if (!companyLog.equals("null"))
                            Log.d("logo", companyLog);
                        prefs.setComapnyLogo(companyLog);
                        prefs.setGSTScheme(feedObj.getGSTScheme());
                        prefs.setWholeSalePriceTax(feedObj.getWholeSalePriceTax());
                        prefs.setCostPriceTax(feedObj.getCostPriceTax());
                        prefs.setSalesDiscPolicy(feedObj.getSalesDiscPolicy());

                        refreshLocalDbData();

                        if(refreshData==true){
                            initialFileDilog.dismiss();
                            prefs.setIsStayLogin(true);
                            prefs.hSetLogout(false);
                            startActivity(new Intent(RegisterActivity.this,WelcomeActivity.class));
                            finish();
                        }else{
                            Toast.makeText(context, "Please Sync data again from Server again", Toast.LENGTH_SHORT).show();
                        }



                    } else {

                        initialFileDilog.dismiss();
                    }

                } else {

                    initialFileDilog.dismiss();
                }

                detailsdataObserver.removeObserver(this);


            }
        });
    }

    private void getCompanyItemsVM() {



        final LiveData<CompanyItemListModel> itemdataObserver = response_login_vm.getCompanyItems();
        itemdataObserver.observe(this, new Observer<CompanyItemListModel>() {
            @Override
            public void onChanged(CompanyItemListModel companyItemListModel) {

                feedCompanyItemList.clear();
                List<DeliveryItemModel> feedArray = companyItemListModel.getTable();


                if (feedArray.size() > 0) {
                    feedCompanyItemList.addAll(feedArray);

                } else {
                    initialFileDilog.dismiss();
                    Toast.makeText(context, "Item Not Found", Toast.LENGTH_SHORT).show();
                }

                if (feedRegCustomerList.size() > 0) {

                    Timber.d("getCompanyItemsVM Db insertions");

                    db.dropPlanOrderTables();
                    db.dropRunTimeOrderTables();
                    db.createPlanOrderTables();
                    db.createRunTimeOrderTables();
                    db.insertCompanyCustomer(feedRegCustomerList, "False");

                    if (routelist.size() > 0)
                        Timber.d("routelist size is "+routelist.size());
                        db.insertCompanyRoute(routelist);

                    if (feedCompanyItemList.size() > 0) {
                        Timber.d("feedCompanyItemList size is"+feedCompanyItemList.size());
                        db.insertCompanyItem(feedCompanyItemList);
                    }


                } else {
                    //Utility.Toast(getApplicationContext(), "No Job Available Today");

                    initialFileDilog.dismiss();
                }

                itemdataObserver.removeObserver(this);
                response_login_vm.ReqCompanyVehicles(prefs.getCompanyID(), prefs.getCompanySiteID());
                response_login_vm.ReqCustomerPriceList(prefs.getCompanyID(),prefs.getCompanySiteID());
            }
        });
    }

    //todo
    private void getCustomerPrice() {
        final LiveData<CustomerPriceListModel> customerPriceListObv = response_login_vm.getCustomerPriceList();
        customerPriceListObv.observe(this, new Observer<CustomerPriceListModel>() {
            @Override
            public void onChanged(CustomerPriceListModel customerPriceListModel) {

                Timber.d("getCustomerPrice is runnning");

                db.insertCustomerPrice(customerPriceListModel.getTable());
                response_login_vm.ReqCompanyVehicles(prefs.getCompanyID(), prefs.getCompanySiteID());

            }
        });
    }

    private void getCompanyVehicleVM() {
        Timber.d("getCompanyVehicleVM");
        final LiveData<VehicleListModel> vehicledataObserver = response_login_vm.getCompanyVehicles();
        vehicledataObserver.observe(this, new Observer<VehicleListModel>() {
            @Override
            public void onChanged(VehicleListModel vehicleListModel) {
                Timber.d("onChanged");
                if(vehicleListModel.getTable().size()>0){
                    Timber.d("Vehicles Found Success");
                }
            }
        });
    }


    //new login api implemented
//    private void requestLogin(String url) {
//        Log.d("loginUrl", url);
//        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                dialog.dismiss();
//                Log.d("loginResponse", response);
//                try {
//                    JSONObject parent = new JSONObject(response);
//                    JSONArray Table = parent.getJSONArray("Table");
//                    if(Table.length() > 0){
//                        JSONObject userObject = Table.getJSONObject(0);
//                        prefs.setCompanyID(userObject.getString("CompanyID"));
//                        prefs.setCompanySiteID(userObject.getString("CompanySiteID"));
//                        prefs.setUserName(userObject.getString("Username"));
//                        prefs.setUserPassword(userObject.getString("Password"));
//                        prefs.setEmployeeID(userObject.getInt("EmployeeID"));
//                        prefs.setEmployeeName(userObject.getString("EmployeeName").equals("null") ? "N/A" : userObject.getString("EmployeeName"));
//                        prefs.setCompanyName(userObject.getString("EmployeeName").equals("null") ? "N/A" : userObject.getString("EmployeeName"));
//                        prefs.setDesignation(userObject.getString("ContactType"));
//                        // dialog.dismiss();
//                        Utility.Toast(RegisterActivity.this, "Login Successfully..");
//                        initialFileDilog.setMessage("Saving initial files");
//                        GetGCMId();
//                    }
//                    else{
//                        Toast.makeText(RegisterActivity.this, "invalid user", Toast.LENGTH_SHORT).show();
//                    }
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                dialog.dismiss();
//                Log.d("loginError", error.getMessage() + "");
//                if (error instanceof ServerError) {
//                    Toast.makeText(RegisterActivity.this, "Server error occurred", Toast.LENGTH_SHORT).show();
//                } else if (error instanceof NetworkError) {
//                    Toast.makeText(RegisterActivity.this, "Network error", Toast.LENGTH_SHORT).show();
//                } else if (error instanceof TimeoutError) {
//                    Toast.makeText(RegisterActivity.this, "Connection timeout error", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        //dialog.show();
//        queue.add(request);
//    }

    //setGCM new API implemented
//    private void setGcmMethod(String url) {
//
//        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.d("response", response);
//                if (response != null) {
//                    try {
//                        if (response.equals("-1")) {
//                            initialFileDilog.dismiss();
//                            Utility.Toast(RegisterActivity.this, "Failed try again");
//                        } else {
//
//                            Params p1 = new Params();
//                            Params p2 = new Params();
//                            p1.setKey("Company_id");
//                            p1.setValue(prefs.getCompanyID());
//                            p2.setKey("CompanySiteId");
//                            p2.setValue(prefs.getCompanySiteID());
//                            paramsArrayList.add(p1);
//                            paramsArrayList.add(p2);
//                            //new GetRegisterCustomerInfo("GetRegisterCompanyCustomer", "ZEDtrack.asmx", paramsArrayList).execute();
//                            getRegisterCustomerInfoMethod(Utility.BASE_LIVE_URL +"api/Customer/GetRegisterCompanyCustomer?Company_id=" + prefs.getCompanyID() + "&CompanySiteID=" + prefs.getCompanySiteID());
//                            Log.d("registerCustomerUrl", Utility.BASE_LIVE_URL +"api/Customer/GetRegisterCompanyCustomer?Company_id=" + prefs.getCompanyID() + "&CompanySiteID=" + prefs.getCompanySiteID());
//
//                        }
//                    } catch (Exception e) {
//                        e.getMessage();
//                        Utility.logCatMsg("User Error " + e);
//                        initialFileDilog.dismiss();
//                        Utility.Toast(RegisterActivity.this, "Failed try again");
//                    }
//                } else {
//                    Utility.Toast(RegisterActivity.this, "Sever Error");
//                    Utility.logCatMsg("****** NULL ******");
//                    initialFileDilog.dismiss();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                initialFileDilog.dismiss();
//                Log.d("setGCMError", error.getMessage() + "");
//                if (error instanceof ServerError) {
//                    Toast.makeText(RegisterActivity.this, "Server error occurred", Toast.LENGTH_SHORT).show();
//                } else if (error instanceof NetworkError) {
//                    Toast.makeText(RegisterActivity.this, "Network error", Toast.LENGTH_SHORT).show();
//                } else if (error instanceof TimeoutError) {
//                    Toast.makeText(RegisterActivity.this, "Connection timeout error", Toast.LENGTH_SHORT).show();
//                }            }
//        });
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        initialFileDilog.show();
//        queue.add(request);
//
//    }

//    public String GetGCMId() {
////        gcm = GoogleCloudMessaging.getInstance(this);
//        regId = getRegistrationId(context);
////        firebaseToken = FirebaseInstanceId.getInstance().getToken();
//
//        Log.d("myFirebaseToken", firebaseToken);
//
//        Log.d("myGCMID", regId);
//        if (TextUtils.isEmpty(regId)) {
//            registerInBackground();
//            Utility.logCatMsg("registerGCM - successfully registered with GCM server - regId: " + regId);
//        } else {
//            Utility.logCatMsg("RegId already available. RegId: " + regId);
//            if (prefs.getEmployeeID() != 0) {
//                ArrayList<Params> parameters = new ArrayList<Params>();
//                Params p1 = new Params();
//                Params p2 = new Params();
//                Params p3 = new Params();
//                Params p4 = new Params();
//                p1.setKey("Mac_address");
//                p1.setValue(getMacAddress());
//                p2.setKey("GCM_RegId");
//                p2.setValue(regId);
//                //p3.setKey("TrackingId");
//                // p3.setValue(prefs.getTrackID());
//                p4.setKey("UserId");
//                p4.setValue(prefs.getEmployeeID() + "");
//                parameters.add(p1);
//                parameters.add(p2);
//                // parameters.add(p3);
//                parameters.add(p4);
//                //new SetGCMId("SetGCMId", "ZEDtrack.asmx", parameters).execute();
//                setGcmMethod(Utility.BASE_LIVE_URL +"api/GCM/SetGCMID?ContactId=" + prefs.getEmployeeID() + "&Mac_address=" + getMacAddress() + "&GCMID=" + regId);
//                Log.d("setGcmUrl", Utility.BASE_LIVE_URL +"api/GCM/SetGCMID?ContactId=" + prefs.getEmployeeID() + "&Mac_address=" + getMacAddress() + "&GCMID=" + regId);
//            }
//        }
//        return regId;
//    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        String registrationId = prefs.getString(REG_ID, "");
        if (registrationId.isEmpty()) {
            Utility.logCatMsg("Registration not found.");
            return "";
        }
        int registeredVersion = prefs.getInt(APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Utility.logCatMsg("App version changed.");
            return "";
        }
        return registrationId;
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

//    private void registerInBackground() {
//
//        //by mubashir
//        FirebaseInstallations.getInstance().getToken(true).addOnCompleteListener(new OnCompleteListener<InstallationTokenResult>() {
//            @Override
//            public void onComplete(@NonNull Task<InstallationTokenResult> task) {
//
//                firebaseToken = task.getResult().getToken();
//                regId = firebaseToken;
//                Log.d("newFcmID", regId);
//                Utility.logCatMsg("registerInBackground - regId: "
//                        + regId);
//                String  msg = "Device registered, registration ID=" + regId;
//                storeRegistrationId(context, regId);
//                Utility.logCatMsg("AsyncTask completed: " + msg);
//
//                if (prefs.getEmployeeID() != 0) {
//                    ArrayList<Params> parameters = new ArrayList<Params>();
//                    Params p1 = new Params();
//                    Params p2 = new Params();
//                    Params p3 = new Params();
//                    Params p4 = new Params();
//                    p1.setKey("Mac_address");
//                    p1.setValue(getMacAddress());
//                    p2.setKey("GCM_RegId");
//                    p2.setValue(regId);
//                    p4.setKey("UserId");
//                    p4.setValue(prefs.getEmployeeID() + "");
//                    parameters.add(p1);
//                    parameters.add(p2);
//                    parameters.add(p4);
//                    //                   new SetGCMId("SetGCMId", "ZEDtrack.asmx", parameters).execute();
//                    setGcmMethod(Utility.BASE_LIVE_URL +"api/GCM/SetGCMID?ContactId=" + prefs.getEmployeeID() + "&Mac_address=" + getMacAddress() + "&GCMID=" + regId);
//                    Log.d("setGcmUrl", Utility.BASE_LIVE_URL +"api/GCM/SetGCMID?ContactId=" + prefs.getEmployeeID() + "&Mac_address=" + getMacAddress() + "&GCMID=" + regId);
//                } else {
//                    Utility.Toast(RegisterActivity.this, "Check network connection and try again");
//                }
//            }
//        });
////        new AsyncTask<Void, Void, String>() {
////            String msg = "";
////
////            @Override
////            protected String doInBackground(Void... params) {
//////                if (gcm == null) {
//////                    gcm = GoogleCloudMessaging.getInstance(context);
//////                }
////                // regId = gcm.register(Config.GOOGLE_PROJECT_ID);
////                //Getting FCM token instead of GCM token
////                regId = FirebaseInstanceId.getInstance().getToken();
////                Log.d("newFcmID", regId);
////                Utility.logCatMsg("registerInBackground - regId: "
////                        + regId);
////                msg = "Device registered, registration ID=" + regId;
////                storeRegistrationId(context, regId);
////                Utility.logCatMsg("AsyncTask completed: " + msg);
////                return msg;
////            }
////
////            @Override
////            protected void onPostExecute(String aVoid) {
////                if (prefs.getEmployeeID() != 0) {
////                    ArrayList<Params> parameters = new ArrayList<Params>();
////                    Params p1 = new Params();
////                    Params p2 = new Params();
////                    Params p3 = new Params();
////                    Params p4 = new Params();
////                    p1.setKey("Mac_address");
////                    p1.setValue(getMacAddress());
////                    p2.setKey("GCM_RegId");
////                    p2.setValue(regId);
////                    p4.setKey("UserId");
////                    p4.setValue(prefs.getEmployeeID() + "");
////                    parameters.add(p1);
////                    parameters.add(p2);
////                    parameters.add(p4);
////                    //                   new SetGCMId("SetGCMId", "ZEDtrack.asmx", parameters).execute();
////                    setGcmMethod(Utility.BASE_LIVE_URL +"api/GCM/SetGCMID?ContactId=" + prefs.getEmployeeID() + "&Mac_address=" + getMacAddress() + "&GCMID=" + regId);
////                    Log.d("setGcmUrl", Utility.BASE_LIVE_URL +"api/GCM/SetGCMID?ContactId=" + prefs.getEmployeeID() + "&Mac_address=" + getMacAddress() + "&GCMID=" + regId);
////                } else {
////                    Utility.Toast(RegisterActivity.this, "Check network connection and try again");
////                }
////            }
////
////
////        }.execute(null, null, null);
//    }

//    private void storeRegistrationId(Context context, String regId) {
//        final SharedPreferences prefs = getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
//        int appVersion = getAppVersion(context);
//        Utility.logCatMsg("Saving regId on app version " + appVersion);
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putString(REG_ID, regId);
//        editor.putInt(APP_VERSION, appVersion);
//        editor.commit();
//    }

//    private String getMacAddress() {
//        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        WifiInfo wInfo = wifiManager.getConnectionInfo();
//        return wInfo.getMacAddress();
//    }

    //new GetRegisterCustomer api implemented
//    private void getRegisterCustomerInfoMethod(String url) {
//        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.d("CustomerResponse", response);
//                initialFileDilog.dismiss();
//                try {
//                    JSONObject parentObject = new JSONObject(response);
//                    JSONArray Table = parentObject.getJSONArray("Table");
//                    if (Table.length() > 0) {
//                        parseJsonFeedItems(Table);
//                        getCompanyRoutesMethod(Utility.BASE_LIVE_URL +"api/Company/GetRegisterCompanyRoutes?Company_id=" + prefs.getCompanyID() + "&CompanySiteId=" + prefs.getCompanySiteID());
//                        Log.d("routesUrl", Utility.BASE_LIVE_URL +"api/Company/GetRegisterCompanyRoutes?Company_id=" + prefs.getCompanyID() + "&CompanySiteId=" + prefs.getCompanySiteID());
////                        new GetCompanyRoutes("GetRegisterCompanyRoutes", "ZEDtrack.asmx", paramsArrayList).execute();
//                    } else {
//                        initialFileDilog.dismiss();
//                        Utility.Toast(RegisterActivity.this, "Failed....Login again ");
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                initialFileDilog.dismiss();
//                if (error instanceof ServerError) {
//                    Toast.makeText(RegisterActivity.this, "Server error occurred", Toast.LENGTH_SHORT).show();
//                } else if (error instanceof NetworkError) {
//                    Toast.makeText(RegisterActivity.this, "Network error", Toast.LENGTH_SHORT).show();
//                } else if (error instanceof TimeoutError) {
//                    Toast.makeText(RegisterActivity.this, "Connection timeout error", Toast.LENGTH_SHORT).show();
//                }
//                Log.d("registerCustomerError", error.getMessage() + "");
//            }
//        });
//
//        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
//        initialFileDilog.show();
//        queue.add(request);
//    }

    //new CompanyRoutesApi implemented
//    public void getCompanyRoutesMethod(String url) {
//
//        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                initialFileDilog.dismiss();
//                Log.d("routeResponse", response);
//                try {
//                    JSONObject parent = new JSONObject(response);
//                    JSONArray tableArray = parent.getJSONArray("Table");
//                    if (tableArray.length() > 0) {
//                        parseJsonFeedCompanyRoute(tableArray);
//                        getCompanyDetailsMethod(Utility.BASE_LIVE_URL +"api/Company/CompanyDetails?Company_id=" + prefs.getCompanyID());
//                        Log.d("companyDetailUrl", Utility.BASE_LIVE_URL +"api/Company/CompanyDetails?Company_id=" + prefs.getCompanyID());
//                        //new GetCompanyDetail("GetCompanyDetails", "ZEDtrack.asmx", paramsArrayList).execute();
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                initialFileDilog.dismiss();
//                if (error instanceof ServerError) {
//                    Toast.makeText(RegisterActivity.this, "Server error occurred", Toast.LENGTH_SHORT).show();
//                } else if (error instanceof NetworkError) {
//                    Toast.makeText(RegisterActivity.this, "Network error", Toast.LENGTH_SHORT).show();
//                } else if (error instanceof TimeoutError) {
//                    Toast.makeText(RegisterActivity.this, "Connection timeout error", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        initialFileDilog.show();
//        queue.add(request);
//
//    }

    //new CompanyDetailsAPI implemented
//    public void getCompanyDetailsMethod(String url) {
//
//        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                initialFileDilog.dismiss();
//                Log.d("companyDetailResponse", response);
//                try {
//                    JSONObject parentObject = new JSONObject(response);
//                    JSONArray tableArray = parentObject.getJSONArray("Table");
//                    JSONObject feedObj = (JSONObject) tableArray.get(0);
//                    Log.d("companyObject", feedObj.toString());
//                    String companyName = feedObj.getString("CompanyTitle");
//                    String companyLog = feedObj.getString("Logo");
//                    //String companyId = feedObj.getString("Company_Id");
//                    if (!companyName.equals("null"))
//                        new SharedPrefs(RegisterActivity.this).setCompanyName(companyName);
//                    if (!companyLog.equals("null"))
//                        Log.d("logo", companyLog);
//                    new SharedPrefs(RegisterActivity.this).setComapnyLogo(companyLog);
//                    Log.d("companyItemList : ", paramsArrayList.toString());
//                    getCompanyItemsMethod(Utility.BASE_LIVE_URL +"api/Company/GetCompanyItems?Company_id=" + prefs.getCompanyID());
//                    Log.d("itemsUrl", Utility.BASE_LIVE_URL +"api/Company/GetCompanyItems?Company_id=" + prefs.getCompanyID());
//                    // new GetCompanyItems("GetCompanyItems", "ZEDtrack.asmx", paramsArrayList).execute();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                initialFileDilog.dismiss();
//                if (error instanceof ServerError) {
//                    Toast.makeText(RegisterActivity.this, "Server error occurred", Toast.LENGTH_SHORT).show();
//                } else if (error instanceof NetworkError) {
//                    Toast.makeText(RegisterActivity.this, "Network error", Toast.LENGTH_SHORT).show();
//                } else if (error instanceof TimeoutError) {
//                    Toast.makeText(RegisterActivity.this, "Connection timeout error", Toast.LENGTH_SHORT).show();
//                }
//                Log.d("companydetailError", error.getMessage() + "");
//            }
//        });
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        queue.add(request);
//    }

    //new GetCompanyItems API implemented
//    private void getCompanyItemsMethod(String url) {
//
//        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                initialFileDilog.dismiss();
//                Log.d("itemsResponse", response);
//                try {
//                    JSONObject parentObject = new JSONObject(response);
//                    JSONArray tableArray = parentObject.getJSONArray("Table");
//                    if (tableArray.length() > 0) {
//                        parseJsonFeedCompanyItems(tableArray);
//                        getCompanyVehiclesMethod(Utility.BASE_LIVE_URL +"api/Vehicle/getCompanyVehicles?Company_id="+prefs.getCompanyID()+"&Compnay_siteId="+prefs.getCompanySiteID());
//                        Log.d("vehicleUrl",Utility.BASE_LIVE_URL +"api/Vehicle/getCompanyVehicles?Company_id="+prefs.getCompanyID()+"&Compnay_siteId="+prefs.getCompanySiteID());
//                    } else {
//                        Toast.makeText(RegisterActivity.this, "No items found", Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                initialFileDilog.dismiss();
//                Log.d("itemsError", error.getMessage() + "");
//                if (error instanceof ServerError) {
//                    Toast.makeText(RegisterActivity.this, "Server error occurred", Toast.LENGTH_SHORT).show();
//                } else if (error instanceof NetworkError) {
//                    Toast.makeText(RegisterActivity.this, "Network error", Toast.LENGTH_SHORT).show();
//                } else if (error instanceof TimeoutError) {
//                    Toast.makeText(RegisterActivity.this, "Connection timeout error", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        queue.add(request);
//    }

    //new GetVehicles api implemented
//    private void getCompanyVehiclesMethod(String url){
//
//        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                Log.d("vehiclesResponse",response);
//                try {
//                    JSONObject parentObject = new JSONObject(response);
//                   JSONArray tableArray = parentObject.getJSONArray("Table");
//                    ArrayList<VehicleModel> list = new ArrayList<>();
//                    if(tableArray.length() > 0){
//                        for (int i = 0; i < tableArray.length(); i++) {
//                            VehicleModel model = new VehicleModel();
//                            JSONObject object = (JSONObject) tableArray.get(i);
//                            model.setId(object.getInt("id"));
//                            model.setRegNo(object.getString("RegNo"));
//                            model.setIsSelected(false);
//                            list.add(model);
//                        }
//                        initialFileDilog.dismiss();
//                        selectVehicleDialog(list);
//                    }
//                    else{
//                        Toast.makeText(RegisterActivity.this, "No Vehicles Found", Toast.LENGTH_SHORT).show();
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                initialFileDilog.dismiss();
//                Log.d("vehiclesError",error.getMessage()+"");
//                if (error instanceof ServerError) {
//                    Toast.makeText(RegisterActivity.this, "Server error occurred", Toast.LENGTH_SHORT).show();
//                } else if (error instanceof NetworkError) {
//                    Toast.makeText(RegisterActivity.this, "Network error", Toast.LENGTH_SHORT).show();
//                } else if (error instanceof TimeoutError) {
//                    Toast.makeText(RegisterActivity.this, "Connection timeout error", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        queue.add(request);
//    }

    private void selectVehicleDialog(final ArrayList<VehicleModel> list) {

        new SyncDataFromServer(this).GetJobs(); //todo to sync data from server
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        View v = getLayoutInflater().inflate(R.layout.vehicle_list_layout, null);
        ListView listview = v.findViewById(R.id.listview);
        final VehicleListAdapter adp = new VehicleListAdapter(RegisterActivity.this, list);
        listview.setAdapter(adp);
        builder.setView(v).setTitle("Select Vehicle").setCancelable(false);
        builder.setPositiveButton("Ok", (dialog, which) -> {
            prefs.setIsStayLogin(true);
            prefs.hSetLogout(false);

            Intent intent = new Intent(RegisterActivity.this, WelcomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
            finish();
//              showRoutesDialog();
            dialog.dismiss();
        });

        final AlertDialog dialog = builder.create();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                progress.dismiss();

                //Validation for to select one vehicle...
                list.get(position).setIsSelected(true);
                int SelectedIndex = list.get(position).getId();
                for (int i = 0; i < list.size(); i++) {
                    list.get(position).setIsSelected(true);
                    list.get(i).setIsSelected(false);


                    if (list.get(i).getId() == SelectedIndex) {
                        list.get(position).setIsSelected(true);
                    }

                }


                prefs.setVehicleID(list.get(position).getId());
                prefs.setVehicleName(list.get(position).getRegNo());



                adp.notifyDataSetChanged();
            }
        });
        dialog.show();
    }


//    private void showRoutesDialog(){
//
//        FragmentManager fm = getSupportFragmentManager();
//        SelectRoutesFragment selectRoutesFragment = SelectRoutesFragment.newInstance("yaseen");
//        selectRoutesFragment.show(fm, "yaseengondal");
//    }

//    private void parseJsonFeedItems(JSONArray feedArray) {
//        try {
//            feedRegCustomerList.clear();
//            for (int i = 0; i < feedArray.length(); i++) {
//                JSONObject feedObj = (JSONObject) feedArray.get(i);
//                RegisterdCustomerModel model = new RegisterdCustomerModel();
//                model.setCustomer_id(feedObj.getInt("ContactId"));
//                model.setName(feedObj.getString("Name").equals("null") ? "" : feedObj.getString("Name"));
//                model.setAddress(feedObj.getString("Address").equals("null") ? "" : feedObj.getString("Address"));
//                model.setAddress1(feedObj.getString("Address1").equals("null") ? "" : feedObj.getString("Address1"));
//                model.setCell(feedObj.getString("Phone").equals("null") ? "" : feedObj.getString("Phone"));
//                model.setPhone(feedObj.getString("Phone2").equals("null") ? "" : feedObj.getString("Phone2"));
//                model.setCity(feedObj.getString("City").equals("null") ? "" : feedObj.getString("City"));
//                model.setCountry(feedObj.getString("Country").equals("null") ? "" : feedObj.getString("Country"));
//                model.setLat(feedObj.getString("Latitude").trim());
//                model.setLng(feedObj.getString("Longitude").trim());
//                model.setRoute(feedObj.getInt("RouteId"));
//                model.setCode(feedObj.getString("ContactCode"));
//                feedRegCustomerList.add(model);
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Utility.logCatMsg("Feed Register Customer error" + e);
//        }
//
//    }

//    private void parseJsonFeedCompanyItems(JSONArray feedArray) {
//        try {
//            feedCompanyItemList.clear();
//            for (int i = 0; i < feedArray.length(); i++) {
//                JSONObject feedObj = (JSONObject) feedArray.get(i);
//                DeliveryItemModel model = new DeliveryItemModel();
//                model.setItem_id(feedObj.getInt("Id"));
//                model.setTitle(feedObj.getString("Title"));
//                model.setCode(feedObj.getString("Code"));
//                model.setName(feedObj.getString("Name"));
//                model.setItemDetail(feedObj.getString("ItemDetail"));
//                model.setCostCtnPrice(feedObj.getInt("CostCtnPrice"));
//                model.setCostPackPrice(feedObj.getInt("CostPackPrice"));
//                model.setCostPiecePrice(feedObj.getInt("CostPiecePrice"));
//                model.setWSCtnPrice(feedObj.getInt("WSCtnPrice"));
//                model.setWSPackPrice(feedObj.getInt("WSPackPrice"));
//                model.setWSPiecePrice(feedObj.getInt("WSPiecePrice"));
//                model.setRetailCtnPrice(feedObj.getInt("RetailCtnPrice"));
//                model.setRetailPackPrice(feedObj.getInt("RetailPackPrice"));
//                model.setRetailPiecePrice(feedObj.getInt("RetailPiecePrice"));
//                model.setDisplayPrice(feedObj.getInt("DisplayPrice"));
//                model.setCtnSize(feedObj.getInt("CtnSize"));
//                model.setPackSize(feedObj.getInt("PackSize"));
////                boolean emptyFlag =
//                model.setEmptyFlag(feedObj.getBoolean("EmptyFlag"));
//
//                feedCompanyItemList.add(model);
//            }
//        } catch (JSONException e) {
//
//            e.printStackTrace();
//            Utility.logCatMsg("Feed CompanyItems error" + e);
//        }
//        if (feedRegCustomerList.size() > 0) {
//            db.dropPlanOrderTables();
//            db.dropRunTimeOrderTables();
//            db.createPlanOrderTables();
//            db.createRunTimeOrderTables();
//            db.insertCompanyCustomer(feedRegCustomerList, "False");
//
//            if (routelist.size() > 0)
//                db.insertCompanyRoute(routelist);
//
//            if (feedCompanyItemList.size() > 0) {
//                db.insertCompanyItem(feedCompanyItemList);
//            }
//        } else {
//            //Utility.Toast(getApplicationContext(), "No Job Available Today");
//
//        }
//    }

//    private void parseJsonFeedCompanyRoute(JSONArray feedArray) {
//        try {
//            routelist.clear();
//            for (int i = 0; i < feedArray.length(); i++) {
//                JSONObject feedObj = (JSONObject) feedArray.get(i);
//                RouteModel model = new RouteModel();
//                model.setRoute_id(feedObj.getInt("Route_Id"));
//                model.setRoute_code(feedObj.getString("Code"));
//                model.setRoute_name(feedObj.getString("Title"));
//                model.setRoute_description(feedObj.getString("Descript"));
//                routelist.add(model);
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Utility.logCatMsg("Feed CompanyRoute error" + e);
//        }
//
//    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    private void setUpActionBar(ActionBar actionBar) {
        LinearLayout mainLayout = findViewById(R.id.mainLayout);
        View v = getLayoutInflater().inflate(R.layout.actionbar_view, mainLayout, false);
        ImageButton btnMenu = v.findViewById(R.id.btnMenu);
        btnMenu.setVisibility(View.GONE);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.appbluegrey)));
        actionBar.setCustomView(v);
    }

}
