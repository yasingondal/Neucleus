package detrack.zaryansgroup.com.detrack.activity.activites;

import androidx.appcompat.app.ActionBar;

import android.app.AlarmManager;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import detrack.zaryansgroup.com.detrack.activity.Adapter.LedgerRecyclerViewAdapter;
import detrack.zaryansgroup.com.detrack.activity.Map.GPSTracker;
import detrack.zaryansgroup.com.detrack.activity.Model.LedgerModel;
import detrack.zaryansgroup.com.detrack.activity.Service.CompanyInfoService;
import detrack.zaryansgroup.com.detrack.activity.Service.GPSService;
import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;
import detrack.zaryansgroup.com.detrack.activity.utilites.ConnectionDetector;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import detrack.zaryansgroup.com.detrack.R;

public class LedgerProceedActivity extends AppCompatActivity {

    String getToDate = "",getFromDate = "", companyId = "", getCustomerName = "";
    int getCustomerId;
    ProgressDialog pd;

    TextView tvLedgerCustomer, tvDateRange, tvDebitTotal, tvCreditTotal, tvBalanceTotal;
    RecyclerView ledgerRecyclerView;
    List<LedgerModel> ledgerModelList;

    TextView actionbar;
    ImageButton btnMenu;
    ImageView companyLogo;
    AlarmManager alarm;
    PendingIntent pintent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ledger_proceed);
        setUpActionBar(getSupportActionBar());
        actionbar.setText("Customer Ledger");
        InitilizeAlaram();

        getToDate = getIntent().getStringExtra("toDate");
        getFromDate = getIntent().getStringExtra("fromDate");
        getCustomerId = getIntent().getIntExtra("customerId",0);
        getCustomerName = getIntent().getStringExtra("customerName");

        companyId = new SharedPrefs(this).getCompanyID();
        initialization();
        if(ConnectionDetector.isConnectingToInternet(this)){
            getCustomerLedger(Utility.BASE_LIVE_URL+"api/customer/GetCustomerLedger?CustomerId="+getCustomerId+"&Dated1="+getFromDate+"&Dated2="+getToDate+"&CompanyId="+companyId);
            Log.d("ledgerUrl",Utility.BASE_LIVE_URL+"api/customer/GetCustomerLedger?CustomerId="+getCustomerId+"&Dated1="+getFromDate+"&Dated2="+getToDate+"&CompanyId="+companyId);
        }
        else{
            Toast.makeText(this, "Check network connection and try again", Toast.LENGTH_SHORT).show();
        }

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
                Intent intent = new Intent(LedgerProceedActivity.this, NewUserActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
                break;
            }
            case R.id.actionSyncCompanyCustomerInfo:{
                if (ConnectionDetector.isConnectingToInternet(LedgerProceedActivity.this)) {
                    startService(new Intent(LedgerProceedActivity.this, CompanyInfoService.class));
                    Utility.Toast(LedgerProceedActivity.this, "Syncing Started...");
                } else {
                    Utility.Toast(LedgerProceedActivity.this, "Check network connection and try again");
                }
                break;
            }
            case R.id.actionAddSalesOrder:{
                Intent intent = new Intent(LedgerProceedActivity.this, TakeOrder.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
                break;
            }
            case R.id.actionSettings:{
                Intent intent = new Intent(LedgerProceedActivity.this, SettingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            }
            case R.id.actionAddSalesReturn:{

                Intent intent = new Intent(LedgerProceedActivity.this, ReturnOrderSearchActivity.class);
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
                    GPSTracker gps = new GPSTracker(LedgerProceedActivity.this);
                    if (ConnectionDetector.isConnectingToInternet(LedgerProceedActivity.this)) {
                        if (gps.canGetLocation()) {
                            Utility.Toast(LedgerProceedActivity.this, "Location Enable Successfully");
                            startservice();
                        } else {
                            Utility.Toast(LedgerProceedActivity.this, "Enable your GPS first and try again..");
                            //gps.showSettingsAlert();
                        }
                    } else
                        Utility.Toast(LedgerProceedActivity.this, "Check network connection and try again");
                    break;
                }
                else{
                    SpannableString spanString = new SpannableString("Enable Location");
                    spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0); //fix the color to white
                    item.setTitle(spanString);

                    Utility.Toast(LedgerProceedActivity.this, "Location Disable Successfully");
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

    private void setUpActionBar(ActionBar actionBar) {

        ScrollView mainLayout = findViewById(R.id.mainLayout);
        View v = getLayoutInflater().inflate(R.layout.actionbar_view, mainLayout, false);
        actionbar = v.findViewById(R.id.actionBarTextView);
        btnMenu = v.findViewById(R.id.btnMenu);
        btnMenu.setVisibility(View.GONE);
        companyLogo = v.findViewById(R.id.companyLogo);
        try {
            String base = new SharedPrefs(LedgerProceedActivity.this).getCompanyLogo();
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

    private void initialization(){

        tvLedgerCustomer = findViewById(R.id.tvLedgerCustomer);
        tvDateRange = findViewById(R.id.tvDateRange);
        tvDebitTotal = findViewById(R.id.tvDebitTotal);
        tvCreditTotal = findViewById(R.id.tvCreditTotal);
        tvBalanceTotal = findViewById(R.id.tvBalanceTotal);
        ledgerRecyclerView = findViewById(R.id.ledgerRecyclerView);

        ledgerModelList = new ArrayList<>();

        tvLedgerCustomer.setText(getCustomerName);
        tvDateRange.setText(getFromDate +" to "+getToDate);
    }

    private void getCustomerLedger(String url){
        pd = new ProgressDialog(this);
        pd.setMessage("Loading Data");
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try {

                    JSONObject parentObject = new JSONObject(response);
                    JSONArray TableArray = parentObject.getJSONArray("Table");

                    if(TableArray.length() > 0){

                        for(int i = 0; i < TableArray.length(); i++){

                            JSONObject object = TableArray.getJSONObject(i);
                            LedgerModel model = new LedgerModel();
                            model.setNo(object.getInt("No"));
                            model.setDate(object.getString("Date"));
                            model.setDescription(object.getString("Description"));
                            model.setDebit(object.getDouble("TransDebitAmount"));
                            model.setCredit(object.getDouble("TransCreditAmount"));
                            model.setBalance(object.getDouble("TransDebitAmount")-
                                    object.getDouble("TransCreditAmount"));
                            ledgerModelList.add(model);
                        }

                        LedgerRecyclerViewAdapter adapter = new LedgerRecyclerViewAdapter(LedgerProceedActivity.this,ledgerModelList);
                        ledgerRecyclerView.setAdapter(adapter);
                        ledgerRecyclerView.setLayoutManager(new LinearLayoutManager(LedgerProceedActivity.this));
                        calculateTotal();
                    }
                    else{
                        Toast.makeText(LedgerProceedActivity.this, "No Ledger Found", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                pd.dismiss();
                if (error instanceof ServerError) {
                    Toast.makeText(LedgerProceedActivity.this, "Server error occurred", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(LedgerProceedActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(LedgerProceedActivity.this, "Connection timeout error", Toast.LENGTH_SHORT).show();
                }
                Log.d("ledgerError", String.valueOf(error.getMessage()));
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        pd.show();
        queue.add(request);
    }

    private void calculateTotal(){

        Double debitSum = 0.0, creditSum = 0.0, balanceSum = 0.0;
        for(int i = 0; i < ledgerModelList.size(); i++){

            LedgerModel model = ledgerModelList.get(i);
            debitSum = debitSum + model.getDebit();
            creditSum = creditSum + model.getCredit();
            balanceSum = model.getBalance();
        }

        tvBalanceTotal.setText(String.valueOf(balanceSum));
        tvCreditTotal.setText(String.valueOf(creditSum));
        tvDebitTotal.setText(String.valueOf(debitSum));
    }


}
