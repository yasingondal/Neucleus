package detrack.zaryansgroup.com.detrack.activity.activites.TestAct;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;

import detrack.zaryansgroup.com.detrack.R;
import detrack.zaryansgroup.com.detrack.activity.Fragment.UndeliveredFragment;
import detrack.zaryansgroup.com.detrack.activity.Map.GPSTracker;
import detrack.zaryansgroup.com.detrack.activity.Model.WelcomeModel.WelcomeDataModel;
import detrack.zaryansgroup.com.detrack.activity.SQLlite.ZEDTrackDB;
import detrack.zaryansgroup.com.detrack.activity.Service.CompanyInfoService;
import detrack.zaryansgroup.com.detrack.activity.Service.GPSService;
import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;
import detrack.zaryansgroup.com.detrack.activity.activites.DailySaleSummary;
import detrack.zaryansgroup.com.detrack.activity.activites.NewUserActivity;
import detrack.zaryansgroup.com.detrack.activity.activites.ReceiptActivity;
import detrack.zaryansgroup.com.detrack.activity.activites.RegisterActivity;
import detrack.zaryansgroup.com.detrack.activity.activites.ReportsActivity;
import detrack.zaryansgroup.com.detrack.activity.activites.ReturnOrderSearchActivity;
import detrack.zaryansgroup.com.detrack.activity.activites.SelectCustomerActivity;
import detrack.zaryansgroup.com.detrack.activity.activites.SelectProductActivity;
import detrack.zaryansgroup.com.detrack.activity.activites.SettingActivity;
import detrack.zaryansgroup.com.detrack.activity.activites.ShowTakenOrderActivity;
import detrack.zaryansgroup.com.detrack.activity.activites.TakeNewReceiptsActivity;
import detrack.zaryansgroup.com.detrack.activity.activites.TakeOrder;
import detrack.zaryansgroup.com.detrack.activity.utilites.BadgeUtils;
import detrack.zaryansgroup.com.detrack.activity.utilites.ConnectionDetector;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import detrack.zaryansgroup.com.detrack.activity.viewmodels.Welcome_ViewModel;
import detrack.zaryansgroup.com.detrack.databinding.TestWelcomeBinding;


public class TestWelcome extends AppCompatActivity {




    TestWelcomeBinding mBinding;
//    TextView actionbar;
    ImageButton btnMenu;
    ImageView companyLogo;
    //    LinearLayout assignOrder, bookedOrderLL, syncedOrderLL, receiptLL, newOrderLL, newCustomerLL, newCustomerReceiptLL, reportLL,
//            LogOutLL, deliveredOrderLL,returnedOrderLL,salesReturnLinear, linearReport;
//    TextView assignCounter, bookedCounter, syncCounter, receiptCounter, returnCounter, userName, CompanyName, deliveredCounter, tvVersionName;
    int assignOrdercounter = 0, bookedOrderCounter = 0, syncOrderCounter = 0;
    boolean isBack = false;
    //    ImageView circleImageView;
    AlarmManager alarm;
    PendingIntent pintent;
    //    ListView menuList;
    public static boolean bookingFlag = false;
    public static String versionName = "";
    public static boolean showHistory;
    public static String designation = "";
    //    ZEDTrackDB database;
    Welcome_ViewModel mWelcome_viewModel;
    WelcomeDataModel welcomeDataModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.test_welcome);
        mWelcome_viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(Welcome_ViewModel.class);
        mBinding.setWelcomeViewModelTest(mWelcome_viewModel);
//        setUpActionBar(getSupportActionBar());
//        setUpActionBar(getSupportActionBar());

//        database = new ZEDTrackDB(this);
        runTimePermission();
        InitilizeAlaram();
        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            Log.d("versionName", versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        designation = new SharedPrefs(TestWelcome.this).getDesignation();
        Log.d("designation", designation);

        BadgeUtils.setBadgeCount(this, 0, R.drawable.zedlog1);
        showHistory = false;

//        if (designation.equals("Order Booker")) {


//            mBinding.assignOrderLL.setVisibility(View.GONE);
//            mBinding.deliveredOrderLL.setVisibility(View.GONE);
//            mBinding.salesReturnLinear.setVisibility(View.GONE);
//            mBinding.returnedOrderLL.setVisibility(View.GONE);
//        } else if (designation.equals("Delivery Boy")) {
////            mBinding.bookedOrderLL.setVisibility(View.GONE);
//        }

        clickListners();
        obserVerWelcomedata();

        Log.d("idforserach", "="+new SharedPrefs(TestWelcome.this).getEmployeeID());

        if (new SharedPrefs(TestWelcome.this).getEmployeeName() != null)
            // userName.setText(new SharedPrefs(NewTestActivity.this).getEmployeeName());
            if (new SharedPrefs(TestWelcome.this).getCompanyName() != null)
//                actionbar.setText(new SharedPrefs(NewTestActivity.this).getCompanyName());
        if (new SharedPrefs(TestWelcome.this).getCompanyLogo() != null) {
            setCompanyLog();
        } else {
            Utility.Toast(this, "Logo is null");
        }

        setFragment(new UndeliveredFragment(),"Inprogress");



    }


    private void obserVerWelcomedata() {
        //obserwelcomedata
        mWelcome_viewModel.getmWelcomeData().observe(this, new Observer<WelcomeDataModel>() {
            @Override
            public void onChanged(WelcomeDataModel data) {

                welcomeDataModel = data;
                if (showHistory) {

                    mBinding.assignCounter.setText(String.valueOf(data.getNumberOfAssignOrder()));
                    if(data.getNumberOfAssignOrder() < 1) mBinding.assignCounter.setVisibility(View.INVISIBLE);
                    mBinding.bookedCounter.setText(String.valueOf(data.getNumberOfBookedUnSyncOrder()));
                    if(data.getNumberOfBookedUnSyncOrder() < 1) mBinding.bookedCounter.setVisibility(View.INVISIBLE);
                    Log.d("bookedcounter","true");
//                    mBinding.receiptCounter.setText(String.valueOf(data.getNumberOfReceipt()));
//                    if(data.getNumberOfReceipt() < 1) mBinding.receiptCounter.setVisibility(View.INVISIBLE);
                    mBinding.deliveredCounter.setText(String.valueOf(data.getNumberOfDeliveredUnSynecOrder()));
                    if(data.getNumberOfDeliveredUnSynecOrder() < 1) mBinding.deliveredCounter.setVisibility(View.INVISIBLE);
                    mBinding.returnCounter.setText(String.valueOf(data.getNumberOfReturnedOrders()));
                    if(data.getNumberOfReturnedOrders() < 1) mBinding.returnCounter.setVisibility(View.INVISIBLE);
                    //todo sync counter
//                    mBinding.syncCounter.setText(String.valueOf(data.getNumberOfSyncOrder()));
//                    if(data.getNumberOfSyncOrder() < 1) mBinding.syncCounter.setVisibility(View.INVISIBLE);
                } else {

                    mBinding.assignCounter.setText(String.valueOf(data.getNumberOfAssignOrder()));
                    if(data.getNumberOfAssignOrder() < 1) mBinding.assignCounter.setVisibility(View.INVISIBLE);
                    mBinding.bookedCounter.setText(String.valueOf(data.getNumberOfBookedUnSyncOrder()));
                    if(data.getNumberOfBookedUnSyncOrder() < 1) mBinding.bookedCounter.setVisibility(View.INVISIBLE);
                    Log.d("bookedcounter","false");
//                    mBinding.syncCounter.setText(String.valueOf(data.getNumberOfSyncOrder()));
//                    if(data.getNumberOfSyncOrder() < 1) mBinding.syncCounter.setVisibility(View.INVISIBLE);
//                    mBinding.receiptCounter.setText(String.valueOf(data.getNumberOfReceipt()));
//                    if(data.getNumberOfReceipt() < 1) mBinding.receiptCounter.setVisibility(View.INVISIBLE);
                    mBinding.deliveredCounter.setText(String.valueOf(data.getNumberOfDeliveredUnSynecOrder()));
                    if(data.getNumberOfDeliveredUnSynecOrder() < 1) mBinding.deliveredCounter.setVisibility(View.INVISIBLE);
                    mBinding.returnCounter.setText(String.valueOf(data.getNumberOfReturnedOrders()));
                    if(data.getNumberOfReturnedOrders() < 1) mBinding.returnCounter.setVisibility(View.INVISIBLE);
                }
            }
        });

        //loadiing welcome data



    }

    private void clickListners() {

        mBinding.btnmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestWelcome.this,NavbarActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });

//        mBinding.floatingNavigationView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mBinding.floatingNavigationView.open();
//            }
//        });

        mBinding.assignOrderLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (designation.equals("Order Booker")) {


                    Toast.makeText(TestWelcome.this, "You are not allowed to perfrom this action.", Toast.LENGTH_SHORT).show();
                } else {
                    setFragment(new UndeliveredFragment(),"Inprogress");
                }

            }
        });
        mBinding.bookedOrderLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (designation.equals("Delivery Boy")) {
                    Toast.makeText(TestWelcome.this, "You are not allowed to Perform this Action", Toast.LENGTH_SHORT).show();
                }else {
                    bookingFlag = true;
                    setFragment(new UndeliveredFragment(),"Booking");
                }

//                Intent intent = new Intent(TestWelcome.this, ShowTakenOrderActivity.class);
//                intent.putExtra("title", "Booked Order");
//                intent.putExtra("syncedOrder", "no");
//                startActivity(intent);
//                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });

        mBinding.deliveredOrderLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (designation.equals("Order Booker")) {
                    Toast.makeText(TestWelcome.this, "You are not allowed to perform this action", Toast.LENGTH_SHORT).show();
                } else {
                    bookingFlag = false;
                    setFragment(new UndeliveredFragment(),"Delivered");
                }



//                Intent intent = new Intent(TestWelcome.this, ShowTakenOrderActivity.class);
//                intent.putExtra("title", "Delivered Order");
//                intent.putExtra("syncedOrder", "no");
//                startActivity(intent);
//                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
        mBinding.returnedOrderLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (designation.equals("Order Booker")) {
                    Toast.makeText(TestWelcome.this, "You are not allowed to perform this action.", Toast.LENGTH_SHORT).show();
                } else {

                    setFragment(new UndeliveredFragment(),"Returned");
                }


//                Intent intent = new Intent(TestWelcome.this, ShowTakenOrderActivity.class);
//                intent.putExtra("title", "Returned Order");
//                intent.putExtra("syncedOrder", "no");
//                startActivity(intent);
//                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
//        mBinding.syncedOrderLL.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(TestWelcome.this, ShowTakenOrderActivity.class);
//                intent.putExtra("title", "Synced Order");
//                intent.putExtra("syncedOrder", "yes");
//                startActivity(intent);
//                overridePendingTransition(R.anim.right_in, R.anim.left_out);
//            }
//        });
//        mBinding.receiptLL.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(TestWelcome.this, ReceiptActivity.class);
//                intent.putExtra("Position", "2");
//                startActivity(intent);
//                overridePendingTransition(R.anim.right_in, R.anim.left_out);
//            }
//        });
//        mBinding.reportLL.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(TestWelcome.this, DailySaleSummary.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.right_in, R.anim.left_out);
//            }
//        });
//        mBinding.newCustomerLL.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(TestWelcome.this, NewUserActivity.class));
//                overridePendingTransition(R.anim.right_in, R.anim.left_out);
//            }
//        });
//        mBinding.newCustomerReceiptLL.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(TestWelcome.this, TakeNewReceiptsActivity.class));
//                overridePendingTransition(R.anim.right_in, R.anim.left_out);
//            }
//        });
//        mBinding.newOrderLL.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (new SharedPrefs(TestWelcome.this).getView().equals("secondView")) {
//                    SelectProductActivity.deliveryInfo = null;
//                    startActivity(new Intent(TestWelcome.this, TakeOrder.class).putExtra("addOrder","true"));
//
//                } else {
//                    startActivity(new Intent(TestWelcome.this, SelectCustomerActivity.class).putExtra("updateLocation","false").putExtra("addOrder","true"));
//
//                }
//                overridePendingTransition(R.anim.right_in, R.anim.left_out);
//            }
//        });
//        mBinding.salesReturnLinear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Utility.salesReturnDialog(TestWelcome.this);
//            }
//        });
//        mBinding.LogOutLL.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Utility.logoutDialog(TestWelcome.this);
//            }
//        });

//        mBinding.linearReport.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(TestWelcome.this, ReportsActivity.class);
//                startActivity(intent);
//            }
//        });

//        mBinding.mapmain.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
////                Intent intent = new Intent(NewTestActivity.this, MapsActivity2.class);
////                startActivity(intent);
//
////                new SyncDataFromServer(NewTestActivity.this).GetJobs();
//                Intent intent = new Intent(TestWelcome.this, TestWelcome.class);
//                startActivity(intent);
//            }
//        });

//        navigationmenuListner();

    }

//    private void navigationmenuListner() {
//        mBinding.floatingNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//                switch (item.getItemId()) {
//
//
//                    case R.id.linearReport:{
//                        Intent intent = new Intent(TestWelcome.this, ReportsActivity.class);
//                        startActivity(intent);
//                    }
//
//                    case R.id.newOrderLL:{
//                        if (new SharedPrefs(TestWelcome.this).getView().equals("secondView")) {
//                            SelectProductActivity.deliveryInfo = null;
//                            startActivity(new Intent(TestWelcome.this, TakeOrder.class).putExtra("addOrder","true"));
//
//                        } else {
//                            startActivity(new Intent(TestWelcome.this, SelectCustomerActivity.class).putExtra("updateLocation","false").putExtra("addOrder","true"));
//
//                        }
//                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
//                        break;
//                    }
//
//                    case R.id.newCustomerReceiptLL:{
//                        startActivity(new Intent(TestWelcome.this, TakeNewReceiptsActivity.class));
//                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
//                        break;
//                    }
//
//                    case R.id.newCustomerLL:{
//                        startActivity(new Intent(TestWelcome.this, NewUserActivity.class));
//                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
//                        break;
//                    }
//
//                    case R.id.reportLL:{
//                        Intent intent = new Intent(TestWelcome.this, DailySaleSummary.class);
//                        startActivity(intent);
//                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
//                        break;
//                    }
//
//                    case R.id.syncedOrderLL:{
//                        Intent intent = new Intent(TestWelcome.this, ShowTakenOrderActivity.class);
//                        intent.putExtra("title", "Synced Order");
//                        intent.putExtra("syncedOrder", "yes");
//                        startActivity(intent);
//                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
//                        break;
//                    }
//
//                    case R.id.salesReturnLinear:{
//
//                        Utility.salesReturnDialog(TestWelcome.this);
//                        break;
//                    }
//                    case R.id.actionSyncCompanyInfo: {
//                        if (ConnectionDetector.isConnectingToInternet(TestWelcome.this)) {
//                            startService(new Intent(TestWelcome.this, CompanyInfoService.class));
//                            Utility.Toast(TestWelcome.this, "Syncing Started...");
//                        } else {
//                            Utility.Toast(TestWelcome.this, "Check network connection and try again");
//                        }
//                        break;
//                    }
//                    case R.id.actionUpdateLocation:{
//
//                        Intent intent = new Intent(TestWelcome.this, SelectCustomerActivity.class);
//                        intent.putExtra("updateLocation",true);
//                        startActivity(intent);
//                        break;
//                    }
//                    case R.id.actionSetting: {
//                        Intent intent = new Intent(TestWelcome.this, SettingActivity.class);
//                        startActivity(intent);
//                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
//                        break;
//
//                    }
//                    case R.id.actionAbout: {
//
//                        DisplayMetrics metrics = getResources().getDisplayMetrics();
//                        int width = metrics.widthPixels;
//                        int height = metrics.heightPixels;
//
//                        Dialog dialog = new Dialog(TestWelcome.this);
//                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                        dialog.setContentView(R.layout.aboutus_custom_dialog);
//                        dialog.getWindow().setLayout((6 * width) / 7, (4 * height) / 7);
//                        TextView tvAppVersion = dialog.findViewById(R.id.tvAppVersion);
//                        tvAppVersion.setText("version" + versionName);
//                        dialog.show();
//                        break;
//                    }
//                    case R.id.actionAddSalesReturn: {
//                        Intent intent = new Intent(TestWelcome.this, ReturnOrderSearchActivity.class);
//                        startActivity(intent);
//                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
//                        break;
//                    }
//                    case R.id.actionLogOut: {
//                        logoutDialog(TestWelcome.this);
//                        break;
//                    }
//                    case R.id.actionEnableLocation: {
//                        if (item.getTitle().toString().equals("Enable Location")) {
//
//                            item.setTitle("Disable Location");
//                            GPSTracker gps = new GPSTracker(TestWelcome.this);
//                            if (ConnectionDetector.isConnectingToInternet(TestWelcome.this)) {
//                                if (gps.canGetLocation()) {
//                                    Utility.Toast(TestWelcome.this, "Location Enable Successfully");
//                                    startservice();
//                                } else {
//                                    Utility.Toast(TestWelcome.this, "Enable your GPS first and try again..");
//                                    //gps.showSettingsAlert();
//                                }
//                            } else
//                                Utility.Toast(TestWelcome.this, "Check network connection and try again");
//                            break;
//                        } else {
//                            item.setTitle("Enable Location");
//                            Utility.Toast(TestWelcome.this, "Location Disable Successfully");
//                            stopservice();
//                            break;
//                        }
//                    }
//                    case R.id.actionNameandVehical: {
//
//                        Utility.userInfoDialog(TestWelcome.this);
//                        break;
//                    }
//
//                    case R.id.actionLoadPrevious: {
//                        showHistory = true;
//                        mWelcome_viewModel.LoadWelcomeData();
////                ZEDTrackDB db = new ZEDTrackDB(NewTestActivity.this);
////                assignCounter.setText(String.valueOf(db.NumberOfAssignOrder()));
////                bookedCounter.setText(String.valueOf(db.NumberOfBookedUnSyncOrder("")));
////                Log.d("bookedcounter","inactionload pervipois");
////                receiptCounter.setText(String.valueOf(db.NumberOfReceipt("")));
////                deliveredCounter.setText(String.valueOf(db.NumberOfDeliveredUnSynecOrder("")));
////                returnCounter.setText(String.valueOf(db.NumberOfReturnedOrders("")));
//                        break;
//                    }
//                    default: {
//                        return true;
//                    }
//                }
//
//                return true;
//            }
//        });
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {

                runTimePermission();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_welcome, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {


            case R.id.linearReport:{
                Intent intent = new Intent(TestWelcome.this, ReportsActivity.class);
                startActivity(intent);
            }

            case R.id.newOrderLL:{
                        if (new SharedPrefs(TestWelcome.this).getView().equals("secondView")) {
//                    deliveryInfo = null;
//                    startActivity(new Intent(TestWelcome.this, TakeOrder.class).putExtra("addOrder","true"));

                } else {
                    startActivity(new Intent(TestWelcome.this, SelectCustomerActivity.class).putExtra("updateLocation","false").putExtra("addOrder","true"));

                }
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        break;
            }

            case R.id.newCustomerReceiptLL:{
                startActivity(new Intent(TestWelcome.this, TakeNewReceiptsActivity.class));
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            }

            case R.id.newCustomerLL:{
                startActivity(new Intent(TestWelcome.this, NewUserActivity.class));
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            }

            case R.id.reportLL:{
                Intent intent = new Intent(TestWelcome.this, DailySaleSummary.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            }

            case R.id.syncedOrderLL:{
                Intent intent = new Intent(TestWelcome.this, ShowTakenOrderActivity.class);
                intent.putExtra("title", "Synced Order");
                intent.putExtra("syncedOrder", "yes");
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            }

            case R.id.salesReturnLinear:{

                Utility.salesReturnDialog(TestWelcome.this);
                break;
            }
            case R.id.actionSyncCompanyInfo: {
                if (ConnectionDetector.isConnectingToInternet(TestWelcome.this)) {
                    startService(new Intent(TestWelcome.this, CompanyInfoService.class));
                    Utility.Toast(TestWelcome.this, "Syncing Started...");
                } else {
                    Utility.Toast(TestWelcome.this, "Check network connection and try again");
                }
                break;
            }
            case R.id.actionUpdateLocation:{

                Intent intent = new Intent(TestWelcome.this, SelectCustomerActivity.class);
                intent.putExtra("updateLocation",true);
                startActivity(intent);
                break;
            }
            case R.id.actionSetting: {
                Intent intent = new Intent(TestWelcome.this, SettingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;

            }
            case R.id.actionAbout: {

                DisplayMetrics metrics = getResources().getDisplayMetrics();
                int width = metrics.widthPixels;
                int height = metrics.heightPixels;

                Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.aboutus_custom_dialog);
                dialog.getWindow().setLayout((6 * width) / 7, (4 * height) / 7);
                TextView tvAppVersion = dialog.findViewById(R.id.tvAppVersion);
                tvAppVersion.setText("version" + versionName);
                dialog.show();
                break;
            }
            case R.id.actionAddSalesReturn: {
                Intent intent = new Intent(TestWelcome.this, ReturnOrderSearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            }
            case R.id.actionLogOut: {
                logoutDialog(TestWelcome.this);
                break;
            }
            case R.id.actionEnableLocation: {
                if (item.getTitle().toString().equals("Enable Location")) {

                    item.setTitle("Disable Location");
                    GPSTracker gps = new GPSTracker(TestWelcome.this);
                    if (ConnectionDetector.isConnectingToInternet(TestWelcome.this)) {
                        if (gps.canGetLocation()) {
                            Utility.Toast(TestWelcome.this, "Location Enable Successfully");
                            startservice();
                        } else {
                            Utility.Toast(TestWelcome.this, "Enable your GPS first and try again..");
                            //gps.showSettingsAlert();
                        }
                    } else
                        Utility.Toast(TestWelcome.this, "Check network connection and try again");
                    break;
                } else {
                    item.setTitle("Enable Location");
                    Utility.Toast(TestWelcome.this, "Location Disable Successfully");
                    stopservice();
                    break;
                }
            }
            case R.id.actionNameandVehical: {

                Utility.userInfoDialog(this);
                break;
            }

            case R.id.actionLoadPrevious: {
                showHistory = true;
                mWelcome_viewModel.LoadWelcomeData();
//                ZEDTrackDB db = new ZEDTrackDB(NewTestActivity.this);
//                assignCounter.setText(String.valueOf(db.NumberOfAssignOrder()));
//                bookedCounter.setText(String.valueOf(db.NumberOfBookedUnSyncOrder("")));
//                Log.d("bookedcounter","inactionload pervipois");
//                receiptCounter.setText(String.valueOf(db.NumberOfReceipt("")));
//                deliveredCounter.setText(String.valueOf(db.NumberOfDeliveredUnSynecOrder("")));
//                returnCounter.setText(String.valueOf(db.NumberOfReturnedOrders("")));
                break;
            }
            default: {
                return true;
            }
        }

        return true;
    }

    @Override
    protected void onStart() {
        mWelcome_viewModel.LoadWelcomeData();
        super.onStart();
    }

    @Override
    protected void onResume() {
//        String date = Utility.getCurrentDate();
//        ZEDTrackDB db = new ZEDTrackDB(NewTestActivity.this);
//        Log.d("currentDate : ", date);

//        if (showHistory.equals("true")) {
//            assignCounter.setText(String.valueOf(db.NumberOfAssignOrder()));
//            bookedCounter.setText(String.valueOf(db.NumberOfBookedUnSyncOrder("")));
//            Log.d("bookedcounter","in on Resume");
//            receiptCounter.setText(String.valueOf(db.NumberOfReceipt("")));
//            deliveredCounter.setText(String.valueOf(db.NumberOfDeliveredUnSynecOrder("")));
//            returnCounter.setText(String.valueOf(db.NumberOfReturnedOrders("")));
//            syncCounter.setText(String.valueOf(db.NumberOfSyncOrder("")));
//        } else {
//            assignCounter.setText(String.valueOf(db.NumberOfAssignOrder()));
//            bookedCounter.setText(String.valueOf(db.NumberOfBookedUnSyncOrder(date)));
//            Log.d("bookedcounter","in on Resume else");
//            syncCounter.setText(String.valueOf(db.NumberOfSyncOrder(date)));
//            receiptCounter.setText(String.valueOf(db.NumberOfReceipt(date)));
//            deliveredCounter.setText(String.valueOf(db.NumberOfDeliveredUnSynecOrder(date)));
//            returnCounter.setText(String.valueOf(db.NumberOfReturnedOrders("")));
//        }


        super.onResume();
    }

    private void setCompanyLog() {
        try {
            String base = new SharedPrefs(TestWelcome.this).getCompanyLogo();
            Log.d("companyLogo", base + "");
            byte[] imageAsBytes = Base64.decode(base.getBytes(), Base64.DEFAULT);
            //todo
//            mBinding.companylogo.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        } catch (Exception e) {
            Utility.logCatMsg("Error Company Logo" + e.getMessage());
        }
    }

    private void setUpActionBar(ActionBar actionBar) {

        ScrollView mainLayout = findViewById(R.id.mainLayout);
        View v = getLayoutInflater().inflate(R.layout.actionbar_view, mainLayout, false);
//        actionbar = v.findViewById(R.id.actionBarTextView);
        btnMenu = v.findViewById(R.id.btnMenu);
        btnMenu.setVisibility(View.GONE);
        companyLogo = v.findViewById(R.id.companyLogo);
        try {
            String base = new SharedPrefs(TestWelcome.this).getCompanyLogo();
            Log.d("companyLogo", base + "");
            byte[] imageAsBytes = Base64.decode(base.getBytes(), Base64.DEFAULT);
            companyLogo.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        } catch (Exception e) {
            Utility.logCatMsg("Error Company Logo" + e.getMessage());
        }
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.drak_blue_hader)));
        actionBar.setCustomView(v);
    }

    @Override
    public void onBackPressed() {
        if (isBack)
            finish();
        else {
            isBack = true;
            Utility.Toast(TestWelcome.this, "Press again to exit");
        }
    }

    private void startservice() {
        Calendar cal = Calendar.getInstance();
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 1 * 10 * 1000, pintent);
    }

    private void stopservice() {
        alarm.cancel(pintent);
    }

    private void InitilizeAlaram() {
        startService(new Intent(this, GPSService.class));
    }

    private void runTimePermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
    }

    private void logoutDialog(final Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_custom_title, null);
        TextView tvCustomTitle = view.findViewById(R.id.tvCustomTitle);
        tvCustomTitle.setText("Logout");
        builder.setCustomTitle(view);
        builder.setMessage("Make sure you upload your orders to server, other wise you will lost it.")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //new SharedPrefs(NewTestActivity.this).setIsStayLogin(false);

                        int assignFlag = Integer.parseInt(mBinding.assignCounter.getText().toString().trim());
                        int bookedFlag = Integer.parseInt(mBinding.bookedCounter.getText().toString().trim());
                        int deliveredFlag = Integer.parseInt(mBinding.deliveredCounter.getText().toString().trim());
                        int receiptFlag = new ZEDTrackDB(TestWelcome.this).numberOfUnsyncReceipt(Utility.getCurrentDate());
                        Log.d("receiptFlag", String.valueOf(receiptFlag));

                        if (checkOrderHistory() > 0) {
                            dialog.dismiss();
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TestWelcome.this);
                            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View view = inflater.inflate(R.layout.dialog_custom_title, null);
                            TextView tvCustomTitle = view.findViewById(R.id.tvCustomTitle);
                            tvCustomTitle.setText("Sync Order First");
                            alertDialogBuilder.setCustomTitle(view).setMessage("Some orders are not sent to the server, check order history....")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                        } else {

                            new SharedPrefs(context).ClearPrefs();
                            Intent intent = new Intent(context, RegisterActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void setFragment(Fragment fragment, String datatype) {
        Bundle bundle = new Bundle();
        bundle.putString("type", datatype);
        fragment.setArguments(bundle);
        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        ft.replace(R.id.fragent_welcome, fragment);
        // or ft.add(R.id.your_placeholder, new ABCFragment());
        // Complete the changes added above
        ft.commit();
    }
    private int checkOrderHistory() {

        int returnValue = 0;
//        int assignOrder = database.NumberOfAssignOrder();
        int assignOrder = welcomeDataModel.getNumberOfAssignOrder();
//        int bookedOrder = database.NumberOfBookedUnSyncOrder("");
        int bookedOrder = welcomeDataModel.getNumberOfBookedUnSyncOrder();
//        int receipt = database.NumberOfReceipt("");
        int receipt = welcomeDataModel.getNumberOfReceipt();
//        int deliveredOrder = database.NumberOfDeliveredUnSynecOrder("");
        int deliveredOrder = welcomeDataModel.getNumberOfDeliveredUnSynecOrder();
//        int returnedOrder = database.NumberOfReturnedOrders("");
        int returnedOrder = welcomeDataModel.getNumberOfReturnedOrders();

        if(assignOrder > 0 || bookedOrder > 0 || receipt > 0 || deliveredOrder > 0 || returnedOrder > 0){
            returnValue = 1;
        }
        return returnValue;
    }
}
