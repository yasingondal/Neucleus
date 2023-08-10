package detrack.zaryansgroup.com.detrack.activity.activites;
import android.Manifest;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import java.util.Calendar;
import detrack.zaryansgroup.com.detrack.R;
import detrack.zaryansgroup.com.detrack.activity.Map.GPSTracker;
import detrack.zaryansgroup.com.detrack.activity.Model.WelcomeModel.WelcomeDataModel;
import detrack.zaryansgroup.com.detrack.activity.SQLlite.ZEDTrackDB;
import detrack.zaryansgroup.com.detrack.activity.Service.CompanyInfoService;
import detrack.zaryansgroup.com.detrack.activity.Service.GPSService;
import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;
import detrack.zaryansgroup.com.detrack.activity.SyncDataServerToSqliteDB.SyncDataFromServer;
import detrack.zaryansgroup.com.detrack.activity.activites.TestAct.TestWelcome;
import detrack.zaryansgroup.com.detrack.activity.utilites.BadgeUtils;
import detrack.zaryansgroup.com.detrack.activity.utilites.ConnectionDetector;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import detrack.zaryansgroup.com.detrack.activity.viewmodels.Welcome_ViewModel;
import detrack.zaryansgroup.com.detrack.databinding.ActivityWelcomeBinding;
import timber.log.Timber;


public class WelcomeActivity extends AppCompatActivity {

    ZEDTrackDB db;
    ActivityWelcomeBinding mBinding;
    TextView actionbar;
    ImageButton btnMenu;
    ImageView companyLogo;


    public  static int rAssignedOrders,rReceipts,rDeliveredOrders,rReturnedOrders,rVisits;

    boolean isBack = false;

    AlarmManager alarm;
    PendingIntent pintent;


    public static boolean bookingFlag = false;
    public static String versionName = "";
    public static boolean showHistory;
    public static String designation = "";


    Welcome_ViewModel mWelcome_viewModel;
    WelcomeDataModel welcomeDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_welcome);
        db = new ZEDTrackDB(this);

        mWelcome_viewModel = new ViewModelProvider(this,
                (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(Welcome_ViewModel.class);
        mBinding.setWelcomeViewModel(mWelcome_viewModel);

        setUpActionBar(getSupportActionBar());


        Timber.d("The getDateForOrder => "+Utility.getDateForOrder()+"T"+Utility.getTimeUpdated());
        Timber.d("The getCurrentDateForV => "+Utility.getCurrentDateForV()+"T"+Utility.getTimeUpdated());


        mBinding.testview.setVisibility(View.GONE);
        runTimePermission();
        InitilizeAlaram();


        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        designation = new SharedPrefs(WelcomeActivity.this).getDesignation();

        Timber.d("The Designation is "+designation);



        BadgeUtils.setBadgeCount(this, 0, R.drawable.zedlog1);
        showHistory = false;

        //Manage Visibility of Welcome Screen For Order Booker Role...
        if (designation.equalsIgnoreCase("Order Booker")) {

            mBinding.lblBookingType.setText("Add New Booking");
            mBinding.assignOrderLL.setVisibility(View.GONE);
            mBinding.deliveredOrderLL.setVisibility(View.GONE);
            mBinding.returnedOrderLL.setVisibility(View.GONE);
            mBinding.receiptLL.setVisibility(View.GONE);
            mBinding.newCustomerReceiptLL.setVisibility(View.GONE);
            mBinding.salesReturnLinear.setVisibility(View.GONE);
            mBinding.newVisit.setVisibility(View.GONE);
            mBinding.reportLL.setVisibility(View.GONE);
            mBinding.linearReport.setVisibility(View.GONE);
            mBinding.ReportsTextView.setVisibility(View.GONE);
            mBinding.ViewBelowReports.setVisibility(View.GONE);

        }
    
        // Managing Visibility of Welcome Screen For Delivery Boy
        else if (designation.equalsIgnoreCase("Delivery Boy")) {
            mBinding.lblBookingType.setText("Add New Sale");
            mBinding.assignOrderLL.setVisibility(View.GONE);
            mBinding.bookedOrderLL.setVisibility(View.GONE);
            mBinding.linearReport.setVisibility(View.GONE);
            mBinding.ReportsTextView.setVisibility(View.GONE);
            mBinding.ViewBelowReports.setVisibility(View.GONE);
        }

        clickListners();
        obserVerWelcomedata();



        if (new SharedPrefs(WelcomeActivity.this).getEmployeeName() != null)
            // userName.setText(new SharedPrefs(WelcomeActivity.this).getEmployeeName());
            if (new SharedPrefs(WelcomeActivity.this).getCompanyName() != null)
                actionbar.setText(new SharedPrefs(WelcomeActivity.this).getCompanyName());
        if (new SharedPrefs(WelcomeActivity.this).getCompanyLogo() != null) {
            setCompanyLog();
        } else {
            Utility.Toast(this, "Logo is null");
        }

    }




    private void obserVerWelcomedata() {
        mWelcome_viewModel.getmWelcomeData().observe(this, new Observer<WelcomeDataModel>() {
            @Override
            public void onChanged(WelcomeDataModel data) {

                welcomeDataModel = data;

                rAssignedOrders = data.getNumberOfAssignOrder();
                rReceipts = data.getNumberOfReceipt();
                rDeliveredOrders = data.getNumberOfDeliveredUnSynecOrder();
                rReturnedOrders = data.getNumberOfReturnedOrders();
                rVisits = db.rGetUnSyncVisits();


                if (showHistory) {
                    mBinding.visitsCounter.setText(String.valueOf(data.getNumberOfVisitRecords()));
                    mBinding.assignCounter.setText(String.valueOf(data.getNumberOfAssignOrder()));
                    mBinding.bookedCounter.setText(String.valueOf(data.getNumberOfBookedUnSyncOrder()));
                    mBinding.receiptCounter.setText(String.valueOf(data.getNumberOfReceipt()));
                    mBinding.deliveredCounter.setText(String.valueOf(data.getNumberOfDeliveredUnSynecOrder()));
                    mBinding.returnCounter.setText(String.valueOf(data.getNumberOfReturnedOrders()));
                    mBinding.syncCounter.setText(String.valueOf(data.getNumberOfSyncOrder()));
                } else {
                    mBinding.visitsCounter.setText(String.valueOf(data.getNumberOfVisitRecords()));
                    mBinding.assignCounter.setText(String.valueOf(data.getNumberOfAssignOrder()));
                    mBinding.bookedCounter.setText(String.valueOf(data.getNumberOfBookedUnSyncOrder()));
                    mBinding.syncCounter.setText(String.valueOf(data.getNumberOfSyncOrder()));
                    mBinding.receiptCounter.setText(String.valueOf(data.getNumberOfReceipt()));
                    mBinding.deliveredCounter.setText(String.valueOf(data.getNumberOfDeliveredUnSynecOrder()));
                    mBinding.returnCounter.setText(String.valueOf(data.getNumberOfReturnedOrders()));
                }
            }
        });

    }


    private void clickListners() {

        mBinding.newVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, AddNewVisitActivity.class));
            }
        });


        mBinding.visitsLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, VisitsActivity.class));
            }
        });


        mBinding.visitsMapLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, VisitsMapActivity.class));
            }
        });

        mBinding.assignOrderLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                intent.putExtra("Position", "0");
                intent.putExtra("syncedOrder", "no");
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
        mBinding.bookedOrderLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookingFlag = true;
                Intent intent = new Intent(WelcomeActivity.this, ShowTakenOrderActivity.class);
                intent.putExtra("title", "Booked Order");
                intent.putExtra("syncedOrder", "no");
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });


        mBinding.viewAllCustomers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, View_Customers_Activity.class));
            }
        });


        mBinding.viewAllProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, view_all_products_activity.class));
            }
        });



        mBinding.deliveredOrderLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bookingFlag = false;
                Intent intent = new Intent(WelcomeActivity.this, ShowTakenOrderActivity.class);
                intent.putExtra("title", "Delivered Order");
                intent.putExtra("syncedOrder", "no");
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });


        mBinding.returnedOrderLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, ShowTakenOrderActivity.class);
                intent.putExtra("title", "Returned Order");
                intent.putExtra("syncedOrder", "no");
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });


        mBinding.syncedOrderLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, ShowTakenOrderActivity.class);
                intent.putExtra("title", "Synced Order");
                intent.putExtra("syncedOrder", "yes");
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
        mBinding.receiptLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, ReceiptActivity.class);
                intent.putExtra("Position", "2");
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
        mBinding.reportLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, DailySaleSummary.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });

        mBinding.newCustomerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, NewUserActivity.class));
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });


        mBinding.newCustomerReceiptLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, TakeNewReceiptsActivity.class));
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });


        mBinding.newOrderLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new SharedPrefs(WelcomeActivity.this).getView().equals("secondView")) {

//                    SelectProductActivity.deliveryInfo = null;
//                    startActivity(new Intent(WelcomeActivity.this, TakeOrder.class)
//                            .putExtra("addOrder","true"));

                    startActivity(new Intent(WelcomeActivity.this, SelectCustomerActivity.class)
                            .putExtra("updateLocation", "false")
                            .putExtra("addOrder", "true"));


                } else {

                    startActivity(new Intent(WelcomeActivity.this, SelectCustomerActivity.class)
                            .putExtra("updateLocation", "false")
                            .putExtra("addOrder", "true"));

                }
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });

        mBinding.salesReturnLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utility.salesReturnDialog(WelcomeActivity.this);
            }
        });

        mBinding.LogOutLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.logoutDialog(WelcomeActivity.this);
            }
        });

        mBinding.linearReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(WelcomeActivity.this, ReportsActivity.class);
                startActivity(intent);
            }
        });

        mBinding.mapmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(WelcomeActivity.this, MapsActivity2.class);
                startActivity(intent);

//                new SyncDataFromServer(WelcomeActivity.this).GetJobs();

            }
        });

        mBinding.testview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                Intent intent = new Intent(WelcomeActivity.this, TestWelcome.class);
                startActivity(intent);
//
//                Intent intent = new Intent(WelcomeActivity.this, UndeliveredActivity.class);
//                startActivity(intent);


            }
        });

    }

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

            case R.id.actionSyncCompanyInfo: {
                if (ConnectionDetector.isConnectingToInternet(WelcomeActivity.this)) {
                    startService(new Intent(WelcomeActivity.this, CompanyInfoService.class));
                    Utility.Toast(WelcomeActivity.this, "Syncing Started...");
                } else {
                    Utility.Toast(WelcomeActivity.this, "Check network connection and try again");
                }
                break;
            }


            //todo for New Work Of Menu
            case R.id.addSaleOrder: {

                if(new SharedPrefs(WelcomeActivity.this).getView().equals("secondView")){
                    startActivity(new Intent(WelcomeActivity.this, SelectCustomerActivity.class)
                            .putExtra("updateLocation", "false")
                            .putExtra("addOrder", "true"));
                }else{

                    startActivity(new Intent(WelcomeActivity.this, SelectCustomerActivity.class)
                            .putExtra("updateLocation", "false")
                            .putExtra("addOrder", "true"));

                }
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            }

            case R.id.salesReturnLinear:{
                startActivity(new Intent(WelcomeActivity.this, SelectCustomerActivity.class)
                        .putExtra("updateLocation", false).putExtra("addOrder", "false"));
                break;
            }

            case R.id.addNewCustomer:{
                startActivity(new Intent(WelcomeActivity.this, NewUserActivity.class));
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            }

            case R.id.addNewReceipt:{
                startActivity(new Intent(WelcomeActivity.this, TakeNewReceiptsActivity.class));
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            }


            //It will again sync all the orders from the server to local db..

                //Duplicate
//            case R.id.syncedOrderLL: {
//                new SyncDataFromServer(this).GetJobs();
//            }
//            break;

            case R.id.linearReport: {
                Intent intent = new Intent(WelcomeActivity.this, ReportsActivity.class);
                startActivity(intent);
            }
            break;

            case R.id.dailySaleClosing: {
                Intent intent = new Intent(WelcomeActivity.this, DailySaleSummary.class);
                startActivity(intent);
            }
            break;


            case R.id.actionUpdateLocation: {

                Intent intent = new Intent(WelcomeActivity.this, SelectCustomerActivity.class);
                intent.putExtra("updateLocation", true);
                startActivity(intent);
                break;
            }
            case R.id.actionSetting: {
                Intent intent = new Intent(WelcomeActivity.this, SettingActivity.class);
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

                Intent intent = new Intent(WelcomeActivity.this, ReturnOrderSearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            }
            case R.id.actionLogOut: {
//                logoutDialog(WelcomeActivity.this);

                Utility.logoutDialog(WelcomeActivity.this);
                break;
            }
            case R.id.actionEnableLocation: {
                if (item.getTitle().toString().equals("Enable Location")) {

                    item.setTitle("Disable Location");
                    GPSTracker gps = new GPSTracker(WelcomeActivity.this);
                    if (ConnectionDetector.isConnectingToInternet(WelcomeActivity.this)) {
                        if (gps.canGetLocation()) {
                            Utility.Toast(WelcomeActivity.this, "Location Enable Successfully");
                            startservice();
                        } else {
                            Utility.Toast(WelcomeActivity.this, "Enable your GPS first and try again..");
                            //gps.showSettingsAlert();
                        }
                    } else
                        Utility.Toast(WelcomeActivity.this, "Check network connection and try again");
                    break;
                } else {
                    item.setTitle("Enable Location");
                    Utility.Toast(WelcomeActivity.this, "Location Disable Successfully");
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
                Toast.makeText(this, "Previous Orders Data Loaded", Toast.LENGTH_SHORT).show();
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
        super.onResume();
    }

    private void setCompanyLog() {
        try {
            String base = new SharedPrefs(WelcomeActivity.this).getCompanyLogo();
            Log.d("companyLogo", base + "");
            byte[] imageAsBytes = Base64.decode(base.getBytes(), Base64.DEFAULT);
            mBinding.companylogo.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        } catch (Exception e) {
            Utility.logCatMsg("Error Company Logo" + e.getMessage());
        }
    }

    private void setUpActionBar(ActionBar actionBar) {

        ScrollView mainLayout = findViewById(R.id.mainLayout);
        View v = getLayoutInflater().inflate(R.layout.actionbar_view, mainLayout, false);
        actionbar = v.findViewById(R.id.actionBarTextView);
        btnMenu = v.findViewById(R.id.btnMenu);
        btnMenu.setVisibility(View.GONE);
        companyLogo = v.findViewById(R.id.companyLogo);
        try {
            String base = new SharedPrefs(WelcomeActivity.this).getCompanyLogo();
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
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.appbluegrey)));
        actionBar.setCustomView(v);
    }

    @Override
    public void onBackPressed() {
        if (isBack)
            finish();
        else {
            isBack = true;
            Utility.Toast(WelcomeActivity.this, "Press again to exit");
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
//        Intent intent = new Intent(WelcomeActivity.this, GPSService.class);
//        pintent = PendingIntent.getService(WelcomeActivity.this, 0, intent, 0);
//        alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    }

    private void runTimePermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
    }

//    private void logoutDialog(final Context context) {
//
//        Timber.d("logoutDialog 2");
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.dialog_custom_title, null);
//        TextView tvCustomTitle = view.findViewById(R.id.tvCustomTitle);
//        tvCustomTitle.setText("Logout");
//        builder.setCustomTitle(view);
//        builder.setMessage("Make sure you upload your orders to server, other wise you will lost it.")
//                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //new SharedPrefs(WelcomeActivity.this).setIsStayLogin(false);
//
//                        int assignFlag = Integer.parseInt(mBinding.assignCounter.getText().toString().trim());
//                        int bookedFlag = Integer.parseInt(mBinding.bookedCounter.getText().toString().trim());
//                        int deliveredFlag = Integer.parseInt(mBinding.deliveredCounter.getText().toString().trim());
//                        int receiptFlag = new ZEDTrackDB(WelcomeActivity.this).numberOfUnsyncReceipt(Utility.getCurrentDate());
//                        Log.d("receiptFlag", String.valueOf(receiptFlag));
//
//                        if (checkOrderHistory() > 0) {
//                            dialog.dismiss();
//                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WelcomeActivity.this);
//                            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                            View view = inflater.inflate(R.layout.dialog_custom_title, null);
//                            TextView tvCustomTitle = view.findViewById(R.id.tvCustomTitle);
//                            tvCustomTitle.setText("Sync Order First");
//                            alertDialogBuilder.setCustomTitle(view).setMessage("Some orders are not sent to the server, check order history....")
//                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            dialog.dismiss();
//                                        }
//                                    }).show();
//                        } else {
//
//                            new SharedPrefs(context).ClearPrefs();
//                            Intent intent = new Intent(context, RegisterActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);
//                            finish();
//                        }
//
//                    }
//                })
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }


    private int checkOrderHistory() {

        int assignOrder;
        int bookedOrder;
        int receipt;
        int deliveredOrder;
        int returnedOrder;
        int returnValue = 0;

        if(!String.valueOf(welcomeDataModel.getNumberOfAssignOrder()).equals(null))
        {
            assignOrder = welcomeDataModel.getNumberOfAssignOrder();
        }else{
            assignOrder = 0;
        }


        if (welcomeDataModel.getNumberOfBookedUnSyncOrder() < 0) {
            bookedOrder = welcomeDataModel.getNumberOfBookedUnSyncOrder();
        } else {
            bookedOrder = 0;
        }


        if (welcomeDataModel.getNumberOfReceipt() < 0) {
            receipt = welcomeDataModel.getNumberOfBookedUnSyncOrder();
        } else {
            receipt = 0;
        }


        if (welcomeDataModel.getNumberOfDeliveredUnSynecOrder() < 0) {
            deliveredOrder = welcomeDataModel.getNumberOfDeliveredUnSynecOrder();
        } else {
            deliveredOrder = 0;
        }


        if (welcomeDataModel.getNumberOfReturnedOrders() < 0) {
            returnedOrder = welcomeDataModel.getNumberOfReturnedOrders();
        } else {
            returnedOrder = 0;
        }


        if (assignOrder > 0 || bookedOrder > 0 || receipt > 0 || deliveredOrder > 0 || returnedOrder > 0) {
            returnValue = 1;
        }
        if (assignOrder > 0 ) {
            returnValue = 1;
        }
            return returnValue;
    }
}