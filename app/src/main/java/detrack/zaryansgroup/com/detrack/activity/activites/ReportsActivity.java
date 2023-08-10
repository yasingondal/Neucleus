package detrack.zaryansgroup.com.detrack.activity.activites;

import androidx.appcompat.app.ActionBar;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import detrack.zaryansgroup.com.detrack.activity.Adapter.ReportsRecyclerViewAdapter;
import detrack.zaryansgroup.com.detrack.activity.Map.GPSTracker;
import detrack.zaryansgroup.com.detrack.activity.Model.ReportModel;
import detrack.zaryansgroup.com.detrack.activity.Service.CompanyInfoService;
import detrack.zaryansgroup.com.detrack.activity.Service.GPSService;
import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;
import detrack.zaryansgroup.com.detrack.activity.utilites.ConnectionDetector;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import detrack.zaryansgroup.com.detrack.R;

public class ReportsActivity extends AppCompatActivity {

    RecyclerView reportsRecyclerView;
    ReportsRecyclerViewAdapter adapter;
    List<ReportModel> reportModelList;

    TextView actionbar;
    ImageButton btnMenu;
    ImageView companyLogo;
    AlarmManager alarm;
    PendingIntent pintent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);


        setUpActionBar(getSupportActionBar());
        actionbar.setText("Reports");
        InitilizeAlaram();
        initialization();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.actionRegNewCustomer:{
                Intent intent = new Intent(ReportsActivity.this, NewUserActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
                break;
            }
            case R.id.actionSyncCompanyCustomerInfo:{
                if (ConnectionDetector.isConnectingToInternet(ReportsActivity.this)) {
                    startService(new Intent(ReportsActivity.this, CompanyInfoService.class));
                    Utility.Toast(ReportsActivity.this, "Syncing Started...");
                } else {
                    Utility.Toast(ReportsActivity.this, "Check network connection and try again");
                }
                break;
            }
            case R.id.actionAddSalesOrder:{
                Intent intent = new Intent(ReportsActivity.this, TakeOrder.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
                break;
            }
            case R.id.actionSettings:{
                Intent intent = new Intent(ReportsActivity.this, SettingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            }
            case R.id.actionAddSalesReturn:{

                Intent intent = new Intent(ReportsActivity.this, ReturnOrderSearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            }
            case R.id.enableLocation:{
                if(item.getTitle().toString().equals("Enable Location")){
                    SpannableString spanString = new SpannableString("Disable Location");
                    spanString.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, spanString.length(), 0); //fix the color to white
                    item.setTitle(spanString);

                    item.setTitle("Disable Location");
                    GPSTracker gps = new GPSTracker(ReportsActivity.this);
                    if (ConnectionDetector.isConnectingToInternet(ReportsActivity.this)) {
                        if (gps.canGetLocation()) {
                            Utility.Toast(ReportsActivity.this, "Location Enable Successfully");
                            startservice();
                        } else {
                            Utility.Toast(ReportsActivity.this, "Enable your GPS first and try again..");
                            //gps.showSettingsAlert();
                        }
                    } else
                        Utility.Toast(ReportsActivity.this, "Check network connection and try again");
                    break;
                }
                else{
                    SpannableString spanString = new SpannableString("Enable Location");
                    spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0); //fix the color to white
                    item.setTitle(spanString);

                    Utility.Toast(ReportsActivity.this, "Location Disable Successfully");
                    stopservice();
                    break;
                }
            }
            case R.id.actionAboutUs:{
                Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.aboutus_custom_dialog);
                TextView tvAppVersion = dialog.findViewById(R.id.tvAppVersion);
                tvAppVersion.setText("version"+WelcomeActivity.versionName);
                dialog.show();
                break;
            }
            case R.id.actionUserInfo:{
                Utility.userInfoDialog(this);
                break;
            }
            default: {
                return false;
            }
        }
        return true;
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

    private void initialization(){

        reportsRecyclerView = findViewById(R.id.reportsRecyclerView);
        reportModelList = new ArrayList<>();
        populateData();
    }

    private void populateData(){
        ReportModel model = new ReportModel();
        model.setReportImageId(R.drawable.sales_report);
        model.setReportTitle("Sales Report");
        reportModelList.add(model);

        model = new ReportModel();
        model.setReportImageId(R.drawable.ledger);
        model.setReportTitle("Customer Ledger");
        reportModelList.add(model);

        model = new ReportModel();
        model.setReportImageId(R.drawable.empty_bottle);
        model.setReportTitle("Empty Bottles");
        reportModelList.add(model);

        adapter = new ReportsRecyclerViewAdapter(this, reportModelList);
        reportsRecyclerView.setAdapter(adapter);
        reportsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setUpActionBar(ActionBar actionBar) {

        ScrollView mainLayout = findViewById(R.id.mainLayout);
        View v = getLayoutInflater().inflate(R.layout.actionbar_view, mainLayout, false);
        actionbar = v.findViewById(R.id.actionBarTextView);
        btnMenu = v.findViewById(R.id.btnMenu);
        btnMenu.setVisibility(View.GONE);
        companyLogo = v.findViewById(R.id.companyLogo);
        try {
            String base = new SharedPrefs(ReportsActivity.this).getCompanyLogo();
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
}
