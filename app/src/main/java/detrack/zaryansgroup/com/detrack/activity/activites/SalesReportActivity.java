package detrack.zaryansgroup.com.detrack.activity.activites;

import androidx.appcompat.app.ActionBar;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import detrack.zaryansgroup.com.detrack.activity.Adapter.SalesReportRecyclerViewAdapter;
import detrack.zaryansgroup.com.detrack.activity.Map.GPSTracker;
import detrack.zaryansgroup.com.detrack.activity.Model.SalesModel;
import detrack.zaryansgroup.com.detrack.activity.Service.CompanyInfoService;
import detrack.zaryansgroup.com.detrack.activity.Service.GPSService;
import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;
import detrack.zaryansgroup.com.detrack.activity.utilites.ConnectionDetector;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import detrack.zaryansgroup.com.detrack.R;

public class SalesReportActivity extends AppCompatActivity {

    TextView tvSalesMan, tvTotalSalesqty, tvTotalSalesAmount,tvTotalSalesCash,tvTotalSalesCredit, tvNumberOfRecords;
    Button btnFrom, btnTo;
    RecyclerView salesReportRecyclerView;
    SalesReportRecyclerViewAdapter adapter;
    List<SalesModel> salesModelList;
    SharedPrefs prefs;

    AlertDialog alertDialog;
    AlarmManager alarm;
    PendingIntent pintent;

    TextView actionbar;
    ImageButton btnMenu;
    ImageView companyLogo;

    private int mYear, mMonth, mDay;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_report);

        setUpActionBar(getSupportActionBar());
        actionbar.setText("Sales Report");
        init();
        rInitDate();
        rLoadTodayReportSummary();
        clickListener();
        InitilizeAlaram();
    }

    private void rLoadTodayReportSummary() {
        getSalesReport(Utility.BASE_LIVE_URL+"api/Sales/GetSalesReport?Dated1="+date+"&Dated2="+date+"&SalesmanId="+prefs.getEmployeeID()+"&CompanyId="+prefs.getCompanyID());
    }

    private void rInitDate() {
        date = getCurrentDate();
        btnTo.setText(date+"");
        btnFrom.setText(date+"");
    }

    private String getCurrentDate() {
        String currentDate;
        currentDate  = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());
        return currentDate;
    }

    private void init(){

        tvSalesMan = findViewById(R.id.tvSalesMan);
        tvTotalSalesqty = findViewById(R.id.tvTotalSalesQty);
        tvTotalSalesAmount = findViewById(R.id.tvTotalSalesAmount);
        tvTotalSalesCash = findViewById(R.id.tvTotalSalesCash);
        tvTotalSalesCredit = findViewById(R.id.tvTotalSalesCredit);
        tvNumberOfRecords = findViewById(R.id.tvTotal);
        btnFrom = findViewById(R.id.btnFrom);
        btnTo = findViewById(R.id.btnTo);
        salesReportRecyclerView = findViewById(R.id.salesReportRecyclerView);
        prefs = new SharedPrefs(this);
        tvSalesMan.setText(prefs.getEmployeeName());
    }

    private void clickListener(){
        btnFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createDatePicker("from");

            }
        });
        btnTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDatePicker("to");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.actionRegNewCustomer: {
                Intent intent = new Intent(SalesReportActivity.this, NewUserActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
                break;
            }
            case R.id.actionSyncCompanyCustomerInfo: {
                if (ConnectionDetector.isConnectingToInternet(SalesReportActivity.this)) {
                    startService(new Intent(SalesReportActivity.this, CompanyInfoService.class));
                    Utility.Toast(SalesReportActivity.this, "Syncing Started...");
                } else {
                    Utility.Toast(SalesReportActivity.this, "Check network Connection and try again");
                }
                break;
            }
            case R.id.actionAddSalesOrder: {
                Intent intent = new Intent(SalesReportActivity.this, TakeOrder.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
                break;
            }
            case R.id.actionSettings: {
                Intent intent = new Intent(SalesReportActivity.this, SettingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            }
            case R.id.actionAddSalesReturn: {

                Intent intent = new Intent(SalesReportActivity.this, ReturnOrderSearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            }
            case R.id.enableLocation: {
                if (item.getTitle().toString().equals("Enable Location")) {
                    SpannableString spanString = new SpannableString("Disable Location");
                    spanString.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, spanString.length(), 0); //fix the color to white
                    item.setTitle(spanString);

                    item.setTitle("Disable Location");
                    GPSTracker gps = new GPSTracker(SalesReportActivity.this);
                    if (ConnectionDetector.isConnectingToInternet(SalesReportActivity.this)) {
                        if (gps.canGetLocation()) {
                            Utility.Toast(SalesReportActivity.this, "Location Enable Successfully");
                            startservice();
                        } else {
                            Utility.Toast(SalesReportActivity.this, "Enable your GPS first and try again..");
                            //gps.showSettingsAlert();
                        }
                    } else
                        Utility.Toast(SalesReportActivity.this, "Check network Connection and try again");
                    break;
                } else {
                    SpannableString spanString = new SpannableString("Enable Location");
                    spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0); //fix the color to white
                    item.setTitle(spanString);

                    Utility.Toast(SalesReportActivity.this, "Location Disable Successfully");
                    stopservice();
                    break;
                }
            }
            case R.id.actionAboutUs: {
                Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.aboutus_custom_dialog);
                TextView tvAppVersion = dialog.findViewById(R.id.tvAppVersion);
                tvAppVersion.setText("version" + WelcomeActivity.versionName);
                dialog.show();
                break;
            }
            case R.id.actionUserInfo: {
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

    //Sales report api
    private void getSalesReport(String url){
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading Sales Report");
        salesModelList = new ArrayList<>();
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("salesReport",response);
                pd.dismiss();
                try {
                    JSONObject parentObject = new JSONObject(response);
                    JSONArray tableArray = parentObject.getJSONArray("Table");
                    Log.d("salelists","="+tableArray.length());
                    if(tableArray.length() > 0){
                        for(int i = 0; i < tableArray.length(); i++){

                            SalesModel model = new SalesModel();
                            JSONObject childObject = tableArray.getJSONObject(i);
                            String salesDate = childObject.getString("OrderDate");
                            model.setSalesDate(salesDate);
                            model.setSalesAmount(childObject.getInt("Amount"));

                            model.setSalesDescription(childObject.getString("CustomerName"));
                            if(childObject.getString("InvNo") != null && !childObject.getString("InvNo").equalsIgnoreCase("null"))
                            {
                                model.setSalesInvNo((int) Double.parseDouble(childObject.getString("InvNo")));
                            }
                            model.setSalesMode(childObject.getString("SalesMode"));
                            model.setSalesQty(childObject.getInt("TotalQuantity"));
                            model.setSalesCash(childObject.getInt("CashSale"));
                            model.setSalesCredit(childObject.getInt("CreditSale"));
                            salesModelList.add(model);
                        }
//                        Log.d("lisSize","list="+String.valueOf(salesModelList.size()));
                        Log.d("salelistsL","sd="+salesModelList.size());
                        adapter = new SalesReportRecyclerViewAdapter(SalesReportActivity.this, salesModelList);
                        salesReportRecyclerView.setLayoutManager(new LinearLayoutManager(SalesReportActivity.this));
                        salesReportRecyclerView.setAdapter(adapter);

                        findTotal(salesModelList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("errorJson","="+e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Log.d("salesReportError",error.getMessage()+"");
                if (error instanceof ServerError) {
                    Toast.makeText(SalesReportActivity.this, "Server error occurred", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(SalesReportActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(SalesReportActivity.this, "Connection timeout error", Toast.LENGTH_SHORT).show();
                }

            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        pd.show();
        queue.add(request);
    }

    private void setUpActionBar(ActionBar actionBar) {

        ScrollView mainLayout = findViewById(R.id.mainLayout);
        View v = getLayoutInflater().inflate(R.layout.actionbar_view, mainLayout, false);
        actionbar = v.findViewById(R.id.actionBarTextView);
        btnMenu = v.findViewById(R.id.btnMenu);
        btnMenu.setVisibility(View.GONE);
        companyLogo = v.findViewById(R.id.companyLogo);
        try {
            String base = new SharedPrefs(SalesReportActivity.this).getCompanyLogo();
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

    private void createDatePicker(final String flag) {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        //launch datepicker modal
        DatePickerDialog datePickerDialog = new DatePickerDialog(SalesReportActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Log.d("DATE SELECTED", "DATE SELECTED " + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        String month = "";
                        if (monthOfYear + 1 < 10) {
                            month = "0" + (monthOfYear + 1);
                        } else {
                            month = String.valueOf(monthOfYear + 1);
                        }
                        date = month + "/" + dayOfMonth + "/" + year;
                        if (flag.equals("from")) {
                            btnFrom.setText(date);
                            if(!btnTo.getText().toString().equals("To")){
                                String compare = compareDates(btnFrom.getText().toString(), btnTo.getText().toString());
                                Log.d("compareFrom",compare);
                                if(compare.equals("Date1 is before Date2") || compare.equals("Date1 is equal Date2")){
                                    if(ConnectionDetector.isConnectingToInternet(SalesReportActivity.this)){

//                                        getSalesReport("http://deliveryapi.zederp.net/api/Sales/GetSalesReport?Dated1="+btnFrom.getText().toString().trim()+"&Dated2="+btnTo.getText().toString().trim()+"&SalesmanId="+prefs.getEmployeeID()+"&CompanyId="+prefs.getCompanyID());
                                        getSalesReport(Utility.BASE_LIVE_URL+"api/Sales/GetSalesReport?Dated1="+btnFrom.getText().toString().trim()+"&Dated2="+btnTo.getText().toString().trim()+"&SalesmanId="+prefs.getEmployeeID()+"&CompanyId="+prefs.getCompanyID());
                                        Log.d("salesReportUrl",Utility.BASE_LIVE_URL +"api/Sales/GetSalesReport?Dated1="+btnFrom.getText().toString().trim()+"&Dated2="+btnTo.getText().toString().trim()+"&SalesmanId="+prefs.getEmployeeID()+"&CompanyId="+prefs.getCompanyID());

                                    }
                                    else{
                                        Toast.makeText(SalesReportActivity.this, "Check network connection and try again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                        else {
                            btnTo.setText(date);
                            if(!btnFrom.getText().toString().equals("From")){
                                String compare = compareDates(btnFrom.getText().toString(), btnTo.getText().toString());
                                Log.d("compareFrom",compare);
                                if(compare.equals("Date1 is before Date2") || compare.equals("Date1 is equal Date2")){
                                    if(ConnectionDetector.isConnectingToInternet(SalesReportActivity.this)){
                                        getSalesReport(Utility.BASE_LIVE_URL+"api/Sales/GetSalesReport?Dated1="+btnFrom.getText().toString().trim()+"&Dated2="+btnTo.getText().toString().trim()+"&SalesmanId="+prefs.getEmployeeID()+"&CompanyId="+prefs.getCompanyID());
                                        Log.d("salesReportUrl",Utility.BASE_LIVE_URL+"api/Sales/GetSalesReport?Dated1="+btnFrom.getText().toString().trim()+"&Dated2="+btnTo.getText().toString().trim()+"&SalesmanId="+prefs.getEmployeeID()+"&CompanyId="+prefs.getCompanyID());
                                    }
                                    else{
                                        Toast.makeText(SalesReportActivity.this, "Check network connection and try again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else{
                                    Toast.makeText(SalesReportActivity.this, "Please Select Valid Date", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }

    private String compareDates(String d1, String d2) {
        String result = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Date date1 = sdf.parse(d1);
            Date date2 = sdf.parse(d2);

            Log.d("Date1", sdf.format(date1));
            Log.d("Date2", sdf.format(date2));
            if (date1.after(date2)) {
                Log.d("CompareDate", "Date1 is after Date2");
                result = "Date1 is after Date2";
            }
            if (date1.before(date2)) {
                Log.d("CompareDate", "Date1 is before Date2");
                result = "Date1 is before Date2";
            }
            if (date1.equals(date2)) {
                Log.d("CompareDate", "Date1 is equal Date2");
                result = "Date1 is equal Date2";
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void findTotal(List<SalesModel> salesModelList){

        int totalQty = 0;
        int totalAmount = 0;
        int totalCash = 0;
        int totalCredit = 0;
        for(int i = 0; i < salesModelList.size(); i++ ){
            SalesModel model = salesModelList.get(i);
            totalAmount = totalAmount + model.getSalesAmount();
            totalQty = totalQty + model.getSalesQty();
            totalCash = totalCash + model.getSalesCash();
            totalCredit = totalCredit + model.getSalesCredit();
        }
        tvTotalSalesqty.setText("Total Qty: "+totalQty);
        tvTotalSalesAmount.setText("Total Amount: "+totalAmount);
        tvTotalSalesCash.setText("Total Cash: "+totalCash);
        tvTotalSalesCredit.setText("Total Credit: "+totalCredit);
        tvNumberOfRecords.setText("Total Records: "+salesModelList.size());
    }
}
