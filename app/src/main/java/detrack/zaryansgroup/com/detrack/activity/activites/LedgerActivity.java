package detrack.zaryansgroup.com.detrack.activity.activites;

import androidx.appcompat.app.ActionBar;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import detrack.zaryansgroup.com.detrack.activity.Adapter.CustomerDilogListAdapter;
import detrack.zaryansgroup.com.detrack.activity.Adapter.LedgerRecyclerViewAdapter;
import detrack.zaryansgroup.com.detrack.activity.Map.GPSTracker;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.DeliveryItemModel;
import detrack.zaryansgroup.com.detrack.activity.Model.LedgerModel;
import detrack.zaryansgroup.com.detrack.activity.Model.RegisterCustomerModel.RegisterdCustomerModel;
import detrack.zaryansgroup.com.detrack.activity.SQLlite.ZEDTrackDB;
import detrack.zaryansgroup.com.detrack.activity.Service.CompanyInfoService;
import detrack.zaryansgroup.com.detrack.activity.Service.GPSService;
import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;
import detrack.zaryansgroup.com.detrack.activity.utilites.ConnectionDetector;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import detrack.zaryansgroup.com.detrack.R;

public class LedgerActivity extends AppCompatActivity {
    TextView actionbar;
    ImageButton btnMenu;
    ImageView companyLogo;
    ZEDTrackDB db;
    ProgressDialog pd;

    Spinner spLedgerCustomerName;
    TextView tvSelectedCustomerName;
    Button btnDateFrom, btnDateTo, btnInvSearch;
    EditText etInvSearch;
    ArrayList<RegisterdCustomerModel> Reg_Customer_list;
    private int mYear, mMonth, mDay;
    String date;
    int inv = 0;
    RegisterdCustomerModel model;
    Button searchBtnText;

    ArrayList<DeliveryItemModel> list;
    EditText itemSerachET;
    ListView itemSerachLV;
    ArrayList<RegisterdCustomerModel> filteredCustomerList;

    TextView tvDebitTotal, tvCreditTotal, tvBalanceTotal;
    RecyclerView ledgerRecyclerView;
    List<LedgerModel> ledgerModelList;


    LinearLayout searchItemLinearLayout;
    AlertDialog alertDialog;
    AlarmManager alarm;
    PendingIntent pintent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ledger);
        setUpActionBar(getSupportActionBar());
        actionbar.setText("Ledger");
        InitilizeAlaram();
        initialization();

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
                Intent intent = new Intent(LedgerActivity.this, NewUserActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
                break;
            }
            case R.id.actionSyncCompanyCustomerInfo: {
                if (ConnectionDetector.isConnectingToInternet(LedgerActivity.this)) {
                    startService(new Intent(LedgerActivity.this, CompanyInfoService.class));
                    Utility.Toast(LedgerActivity.this, "Syncing Started...");
                } else {
                    Utility.Toast(LedgerActivity.this, "Check network Connection and try again");
                }
                break;
            }
            case R.id.actionAddSalesOrder: {
                Intent intent = new Intent(LedgerActivity.this, TakeOrder.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
                break;
            }
            case R.id.actionSettings: {
                Intent intent = new Intent(LedgerActivity.this, SettingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            }
            case R.id.actionAddSalesReturn: {

                Intent intent = new Intent(LedgerActivity.this, ReturnOrderSearchActivity.class);
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
                    GPSTracker gps = new GPSTracker(LedgerActivity.this);
                    if (ConnectionDetector.isConnectingToInternet(LedgerActivity.this)) {
                        if (gps.canGetLocation()) {
                            Utility.Toast(LedgerActivity.this, "Location Enable Successfully");
                            startservice();
                        } else {
                            Utility.Toast(LedgerActivity.this, "Enable your GPS first and try again..");
                            //gps.showSettingsAlert();
                        }
                    } else
                        Utility.Toast(LedgerActivity.this, "Check network Connection and try again");
                    break;
                } else {
                    SpannableString spanString = new SpannableString("Enable Location");
                    spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0); //fix the color to white
                    item.setTitle(spanString);

                    Utility.Toast(LedgerActivity.this, "Location Disable Successfully");
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

    private void setUpActionBar(ActionBar actionBar) {

        ScrollView mainLayout = findViewById(R.id.mainLayout);
        View v = getLayoutInflater().inflate(R.layout.actionbar_view, mainLayout, false);
        actionbar = v.findViewById(R.id.actionBarTextView);
        btnMenu = v.findViewById(R.id.btnMenu);
        btnMenu.setVisibility(View.GONE);
        companyLogo = v.findViewById(R.id.companyLogo);
        try {
            String base = new SharedPrefs(LedgerActivity.this).getCompanyLogo();
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

    private void initialization() {
        searchBtnText = findViewById(R.id.searchBtnText);
        btnInvSearch = findViewById(R.id.btnInvSearch);
        etInvSearch = findViewById(R.id.etInvSearch);
        spLedgerCustomerName = findViewById(R.id.spLedgerCustomerName);
        searchItemLinearLayout = findViewById(R.id.searchItem);
        tvSelectedCustomerName = findViewById(R.id.tvSelectedCustomerName);
        tvDebitTotal = findViewById(R.id.tvDebitTotal);
        tvCreditTotal = findViewById(R.id.tvCreditTotal);
        tvBalanceTotal = findViewById(R.id.tvBalanceTotal);
        ledgerRecyclerView = findViewById(R.id.ledgerRecyclerView);
        btnDateFrom = findViewById(R.id.btnDateFrom);
        btnDateTo = findViewById(R.id.btnDateTo);
        Reg_Customer_list = new ArrayList<>();
        ledgerModelList = new ArrayList<>();
        db = new ZEDTrackDB(this);
        getCustomerList();
        clickListener();
    }

    private void getCustomerList() {
        Reg_Customer_list = db.getSQLiteRegisterCustomerInfo(null);
        CustomerDilogListAdapter adapter = new CustomerDilogListAdapter(LedgerActivity.this, Reg_Customer_list);
        spLedgerCustomerName.setAdapter(adapter);
        spLedgerCustomerName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                model = Reg_Customer_list.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void clickListener() {

        btnDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDatePicker("from");
            }
        });
        btnDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDatePicker("to");
            }
        });
        btnInvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(btnDateFrom.getText().toString().equals("From")){
                    Toast.makeText(LedgerActivity.this, "Select Valid Date Range", Toast.LENGTH_SHORT).show();
                }
                else if(btnDateTo.getText().toString().equals("To")){
                    Toast.makeText(LedgerActivity.this, "Select Valid Date Range", Toast.LENGTH_SHORT).show();
                }
                else if(tvSelectedCustomerName.getText().toString().equals("")){
                    Toast.makeText(LedgerActivity.this, "Select Customer First", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(!etInvSearch.getText().toString().equals("")){
                        inv = Integer.parseInt(etInvSearch.getText().toString());
                    }
                    else{
                        inv = 0;
                    }
                    String compare = compareDates(btnDateFrom.getText().toString(), btnDateTo.getText().toString());

                    if (compare.equals("Date1 is before Date2")) {
                        if (ConnectionDetector.isConnectingToInternet(LedgerActivity.this)) {
//                            getCustomerLedger(Utility.BASE_LIVE_URL +"api/customer/GetCustomerLedger?CustomerId=" + model.getCustomer_id() + "&Dated1=" + btnDateFrom.getText().toString().trim() + "&Dated2=" + btnDateTo.getText().toString().trim() + "&CompanyId=" + new SharedPrefs(LedgerActivity.this).getCompanyID()+"&No="+inv+"&TransType=ALL");
                            getCustomerLedger(Utility.BASE_LIVE_URL +"api/customer/GetCustomerLedger?CustomerId=" + model.getCustomer_id() + "&Dated1=" + btnDateFrom.getText().toString().trim() + "&Dated2=" + btnDateTo.getText().toString().trim() + "&CompanyId=" + new SharedPrefs(LedgerActivity.this).getCompanyID()+"&No="+inv+"&TransType=ALL");
                            Log.d("ledgerUrl", Utility.BASE_LIVE_URL +"api/customer/GetCustomerLedger?CustomerId=" + model.getCustomer_id() + "&Dated1=" + btnDateFrom.getText().toString().trim() + "&Dated2=" + btnDateTo.getText().toString().trim() + "&CompanyId=" + new SharedPrefs(LedgerActivity.this).getCompanyID()+"&No="+inv+"&TransType=ALL");
                        } else {
                            Toast.makeText(LedgerActivity.this, "Check network connection and try again", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else{
                        Toast.makeText(LedgerActivity.this, "Select Valid Date Range", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        searchBtnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchCustomerDialog();
            }
        });

    }

    private void getLedger() {
        if (!btnDateFrom.getText().toString().equals("From") && !btnDateTo.getText().toString().equals("To")) {
            String compare = compareDates(btnDateFrom.getText().toString(), btnDateTo.getText().toString());
            if (tvSelectedCustomerName.getText().equals("")) {
                Toast.makeText(LedgerActivity.this, "Select Customer First", Toast.LENGTH_SHORT).show();
            } else if (compare.equals("Date1 is before Date2")) {
                if (ConnectionDetector.isConnectingToInternet(LedgerActivity.this)) {
                    getCustomerLedger(Utility.BASE_LIVE_URL +"api/customer/GetCustomerLedger?CustomerId=" + model.getCustomer_id() + "&Dated1=" + btnDateFrom.getText().toString().trim() + "&Dated2=" + btnDateTo.getText().toString().trim() + "&CompanyId=" + new SharedPrefs(LedgerActivity.this).getCompanyID()+"&No="+inv+"&TransType=ALL");
                    Log.d("ledgerUrl", Utility.BASE_LIVE_URL +"api/customer/GetCustomerLedger?CustomerId=" + model.getCustomer_id() + "&Dated1=" + btnDateFrom.getText().toString().trim() + "&Dated2=" + btnDateTo.getText().toString().trim() + "&CompanyId=" + new SharedPrefs(LedgerActivity.this).getCompanyID()+"&No="+inv+"&TransType=ALL");
                } else {
                    Toast.makeText(LedgerActivity.this, "Check network connection and try again", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(LedgerActivity.this, "Select Valid Date Range", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void createDatePicker(final String flag) {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        //launch datepicker modal
        DatePickerDialog datePickerDialog = new DatePickerDialog(LedgerActivity.this,
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
                            btnDateFrom.setText(date);
                            if (!btnDateTo.getText().toString().equals("To")) {
                                String compare = compareDates(btnDateFrom.getText().toString(), btnDateTo.getText().toString());
                                if (tvSelectedCustomerName.getText().equals("")) {
                                    Toast.makeText(LedgerActivity.this, "Select Customer First", Toast.LENGTH_SHORT).show();
                                } else if (compare.equals("Date1 is before Date2")) {
                                    if (ConnectionDetector.isConnectingToInternet(LedgerActivity.this)) {
                                        getCustomerLedger(Utility.BASE_LIVE_URL +"api/customer/GetCustomerLedger?CustomerId=" + model.getCustomer_id() + "&Dated1=" + btnDateFrom.getText().toString().trim() + "&Dated2=" + btnDateTo.getText().toString().trim() + "&CompanyId=" + new SharedPrefs(LedgerActivity.this).getCompanyID()+"&No="+inv+"&TransType=ALL");
                                        Log.d("ledgerUrl", Utility.BASE_LIVE_URL +"api/customer/GetCustomerLedger?CustomerId=" + model.getCustomer_id() + "&Dated1=" + btnDateFrom.getText().toString().trim() + "&Dated2=" + btnDateTo.getText().toString().trim() + "&CompanyId=" + new SharedPrefs(LedgerActivity.this).getCompanyID()+"&No="+inv+"&TransType=ALL");
                                    } else {
                                        Toast.makeText(LedgerActivity.this, "Check network connection and try again", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(LedgerActivity.this, "Select Valid Date Range", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            btnDateTo.setText(date);
                            if (!btnDateFrom.getText().toString().equals("From")) {
                                String compare = compareDates(btnDateFrom.getText().toString(), btnDateTo.getText().toString());
                                if (tvSelectedCustomerName.getText().equals("")) {
                                    Toast.makeText(LedgerActivity.this, "Select Customer First", Toast.LENGTH_SHORT).show();
                                } else if (compare.equals("Date1 is before Date2")) {
                                    if (ConnectionDetector.isConnectingToInternet(LedgerActivity.this)) {
                                        getCustomerLedger(Utility.BASE_LIVE_URL +"api/customer/GetCustomerLedger?CustomerId=" + model.getCustomer_id() + "&Dated1=" + btnDateFrom.getText().toString().trim() + "&Dated2=" + btnDateTo.getText().toString().trim() + "&CompanyId=" + new SharedPrefs(LedgerActivity.this).getCompanyID()+"&No="+inv+"&TransType=ALL");
                                        Log.d("ledgerUrl", Utility.BASE_LIVE_URL +"api/customer/GetCustomerLedger?CustomerId=" + model.getCustomer_id() + "&Dated1=" + btnDateFrom.getText().toString().trim() + "&Dated2=" + btnDateTo.getText().toString().trim() + "&CompanyId=" + new SharedPrefs(LedgerActivity.this).getCompanyID()+"&No="+inv+"&TransType=ALL");
                                    } else {
                                        Toast.makeText(LedgerActivity.this, "Check network connection and try again", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(LedgerActivity.this, "Select Valid Date Range", Toast.LENGTH_SHORT).show();
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

    private void searchCustomerDialog() {
        Reg_Customer_list = db.getSQLiteRegisterCustomerInfo(null);
        CustomerDilogListAdapter adapter = new CustomerDilogListAdapter(LedgerActivity.this, Reg_Customer_list);
        View view = getLayoutInflater().inflate(R.layout.item_search_diloag_layout, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LedgerActivity.this);
        View view1 = getLayoutInflater().inflate(R.layout.dialog_custom_title, null);
        TextView tvCustomTitle = view1.findViewById(R.id.tvCustomTitle);
        tvCustomTitle.setText("Search Customer");
        alertDialogBuilder.setCustomTitle(view1);
        alertDialogBuilder
                .setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });
        alertDialog = alertDialogBuilder.create();
        itemSerachET = view.findViewById(R.id.inputSearch);
        itemSerachLV = view.findViewById(R.id.item_list_view);
        itemSerachLV.setAdapter(adapter);
        itemSerachLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                model = Reg_Customer_list.get(position);
                tvSelectedCustomerName.setText(model.getName());
                if (!btnDateFrom.getText().toString().equals("From") && !btnDateTo.getText().toString().equals("To")) {
                    String compare = compareDates(btnDateFrom.getText().toString(), btnDateTo.getText().toString());
                    if (tvSelectedCustomerName.getText().equals("")) {
                        Toast.makeText(LedgerActivity.this, "Select Customer First", Toast.LENGTH_SHORT).show();
                    } else if (compare.equals("Date1 is before Date2")) {
                        if (ConnectionDetector.isConnectingToInternet(LedgerActivity.this)) {
                            getCustomerLedger(Utility.BASE_LIVE_URL +"api/customer/GetCustomerLedger?CustomerId=" + model.getCustomer_id() + "&Dated1=" + btnDateFrom.getText().toString().trim() + "&Dated2=" + btnDateTo.getText().toString().trim() + "&CompanyId=" + new SharedPrefs(LedgerActivity.this).getCompanyID()+"&No="+inv+"&TransType=ALL");
                            Log.d("ledgerUrl", Utility.BASE_LIVE_URL +"api/customer/GetCustomerLedger?CustomerId=" + model.getCustomer_id() + "&Dated1=" + btnDateFrom.getText().toString().trim() + "&Dated2=" + btnDateTo.getText().toString().trim() + "&CompanyId=" + new SharedPrefs(LedgerActivity.this).getCompanyID()+"&No="+inv+"&TransType=ALL");
                        } else {
                            Toast.makeText(LedgerActivity.this, "Check network connection and try again", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(LedgerActivity.this, "Select Valid Date Range", Toast.LENGTH_SHORT).show();
                    }
                }


                alertDialog.dismiss();
            }
        });
        TextView tvsearchItemName = view.findViewById(R.id.searchItemName);
        tvsearchItemName.setVisibility(View.GONE);
        TextView tvsearchItemCode = view.findViewById(R.id.searchItemCode);
        tvsearchItemCode.setVisibility(View.GONE);
        TextView tvsearchItemPrice = view.findViewById(R.id.searchItemPrice);
        tvsearchItemPrice.setVisibility(View.GONE);

        openItemSearchBar();
        alertDialog.show();
    }

    private void openItemSearchBar() {
        itemSerachET.addTextChangedListener(new SearchItemListWatcher());
        itemSerachET.setHint("Search....");
    }

    private class SearchItemListWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence c, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence c, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String query = itemSerachET.getText().toString();
            filteredCustomerList = performItemSearch(Reg_Customer_list, query);
            itemSerachLV.setAdapter(new CustomerDilogListAdapter(LedgerActivity.this, filteredCustomerList));
            itemSerachLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    model = filteredCustomerList.get(position);
                    tvSelectedCustomerName.setText(model.getName());
                    alertDialog.dismiss();
                }
            });

        }
    }

    private ArrayList<RegisterdCustomerModel> performItemSearch(ArrayList<RegisterdCustomerModel> modal, String query) {
        String[] queryByWords = query.toLowerCase().split("\\s+");
        ArrayList<RegisterdCustomerModel> filter = new ArrayList<>();
        for (int i = 0; i < modal.size(); i++) {
            RegisterdCustomerModel data = modal.get(i);
            String name = data.getName().toLowerCase();
            Utility.logCatMsg("Search query :" + name);
            for (String word : queryByWords) {
                int numberOfMatches = queryByWords.length;
                if (name.contains(word)) {
                    numberOfMatches--;
                    Utility.logCatMsg("Match " + name + " " + word);
                } else {
                    break;
                }
                // They all match.
                if (numberOfMatches == 0) {
                    filter.add(data);
                }
            }
        }
        return filter;
    }


    private void getCustomerLedger(String url) {
        ledgerModelList = new ArrayList<>();
        pd = new ProgressDialog(this);
        pd.setMessage("Loading Data");
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try {

                    JSONObject parentObject = new JSONObject(response);
                    JSONArray TableArray = parentObject.getJSONArray("Table");

   
                    if (TableArray.length() > 0) {
                        for (int i = 0; i < TableArray.length(); i++) {

                            JSONObject object = TableArray.getJSONObject(i);
                            LedgerModel model = new LedgerModel();
                            if(!object.getString("InvoiceNo").equalsIgnoreCase("null")) model.setNo(Integer.parseInt(object.getString("InvoiceNo")));
                            model.setDate(object.getString("Dated"));
                            model.setDescription(object.getString("TransDescription"));
                            model.setDebit(object.getDouble("TransDebitAmount"));
                            model.setCredit(object.getDouble("TransCreditAmount"));

                       
                            model.setBalance(object.getDouble("TransDebitAmount")-
                                    object.getDouble("TransCreditAmount"));
                            ledgerModelList.add(model);
                        }

                        LedgerRecyclerViewAdapter adapter = new LedgerRecyclerViewAdapter(LedgerActivity.this, ledgerModelList);
                        ledgerRecyclerView.setAdapter(adapter);
                        ledgerRecyclerView.setLayoutManager(new LinearLayoutManager(LedgerActivity.this));
                        calculateTotal();
                    } else {
                        Toast.makeText(LedgerActivity.this, "No Ledger Found", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("getledger","="+e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                pd.dismiss();
                if (error instanceof ServerError) {
                    Toast.makeText(LedgerActivity.this, "Server error occurred", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(LedgerActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(LedgerActivity.this, "Connection timeout error", Toast.LENGTH_SHORT).show();
                }
                Log.d("ledgerError", String.valueOf(error.getMessage()));
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        pd.show();
        queue.add(request);
    }

    private void calculateTotal() {

        Double debitSum = 0.0, creditSum = 0.0, balanceSum = 0.0;
        for (int i = 0; i < ledgerModelList.size(); i++) {

            LedgerModel model = ledgerModelList.get(i);
            debitSum = debitSum + model.getDebit();
            creditSum = creditSum + model.getCredit();
           
        }

        balanceSum = debitSum - creditSum;
        tvBalanceTotal.setText(String.valueOf(balanceSum));
        tvCreditTotal.setText(String.valueOf(creditSum));
        tvDebitTotal.setText(String.valueOf(debitSum));
    }

}
