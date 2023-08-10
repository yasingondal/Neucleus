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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import detrack.zaryansgroup.com.detrack.activity.Adapter.CustomerDilogListAdapter;
import detrack.zaryansgroup.com.detrack.activity.Adapter.ReturnedOrderRecyclerViewAdapter;
import detrack.zaryansgroup.com.detrack.activity.Map.GPSTracker;
import detrack.zaryansgroup.com.detrack.activity.Model.RegisterCustomerModel.RegisterdCustomerListModel;
import detrack.zaryansgroup.com.detrack.activity.Model.RegisterCustomerModel.RegisterdCustomerModel;
import detrack.zaryansgroup.com.detrack.activity.SQLlite.ZEDTrackDB;
import detrack.zaryansgroup.com.detrack.activity.Service.CompanyInfoService;
import detrack.zaryansgroup.com.detrack.activity.Service.GPSService;
import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;
import detrack.zaryansgroup.com.detrack.activity.viewmodels.ReturnedOrder_ViewModel;
import detrack.zaryansgroup.com.detrack.activity.utilites.ConnectionDetector;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import detrack.zaryansgroup.com.detrack.R;

public class ReturnOrderSearchActivity extends AppCompatActivity {
    TextView actionbar;
    ImageButton btnMenu;
    ImageView companyLogo;
    ZEDTrackDB db;
    ProgressDialog pd;
    RegisterdCustomerModel model;
    Spinner spReturnCustomerName;
    TextView tvSelectedCustomerName;
    Button btnDateFrom, btnDateTo, btnInvSearch;
    RecyclerView returnedOrderRecyclerView;
    EditText etInvSearch;
    private int mYear, mMonth, mDay;
    Button searchBtnText;
    LinearLayout searchItemLinearLayout;
    String date;
    int inv = 0;
    ArrayList<RegisterdCustomerModel> customerList;
    ArrayList<RegisterdCustomerModel> filteredCustomerList;
    List<RegisterdCustomerModel> returnedOrderList;
    SharedPrefs prefs;
    AlertDialog alertDialog;

    AlarmManager alarm;
    PendingIntent pintent;
    EditText itemSerachET;
    ListView itemSerachLV;
    ReturnedOrder_ViewModel returnedOrder_viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_order_search);
        setUpActionBar(getSupportActionBar());
        //init view model
        returnedOrder_viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(ReturnedOrder_ViewModel.class);

        initObserver();
        InitilizeAlaram();

        init();

    }

    private void initObserver() {
        returnedOrder_viewModel.getSelectedCustomer().observe(this, new Observer<ArrayList<RegisterdCustomerModel>>() {
            @Override
            public void onChanged(ArrayList<RegisterdCustomerModel> registerdCustomerModels) {
                customerList = registerdCustomerModels;
            }
        });

        returnedOrder_viewModel.getReturnedOrder().observe(this, new Observer<RegisterdCustomerListModel>() {
            @Override
            public void onChanged(RegisterdCustomerListModel registerdCustomerListModel) {
                returnedOrderList.addAll(registerdCustomerListModel.getTable());
                returnedOrderRecyclerView.setVisibility(View.VISIBLE);
                ReturnedOrderRecyclerViewAdapter adapter = new ReturnedOrderRecyclerViewAdapter(ReturnOrderSearchActivity.this, returnedOrderList);
                returnedOrderRecyclerView.setAdapter(adapter);
                returnedOrderRecyclerView.setLayoutManager(new LinearLayoutManager(ReturnOrderSearchActivity.this));
            }
        });
    }

    private void init(){

        searchBtnText = findViewById(R.id.searchBtnText);
        btnInvSearch = findViewById(R.id.btnInvSearch);
        etInvSearch = findViewById(R.id.etInvSearch);
        spReturnCustomerName = findViewById(R.id.spReturnCustomerName);
        searchItemLinearLayout = findViewById(R.id.searchItem);
        tvSelectedCustomerName = findViewById(R.id.tvSelectedCustomerName);
        btnDateFrom = findViewById(R.id.btnDateFrom);
        btnDateTo = findViewById(R.id.btnDateTo);
        returnedOrderList = new ArrayList<>();
        returnedOrderRecyclerView = findViewById(R.id.returnedOrderRecyclerView);
        db = new ZEDTrackDB(this);
        customerList = new ArrayList<>();
        filteredCustomerList = new ArrayList<>();

        getCustomerList();
        clickListener();
    }

    private void getCustomerList() {
//        customerList = db.getSQLiteRegisterCustomerInfo(null);
        returnedOrder_viewModel.getSqlCustomerInfo(null);
        CustomerDilogListAdapter adapter = new CustomerDilogListAdapter(ReturnOrderSearchActivity.this, customerList );
        spReturnCustomerName.setAdapter(adapter);
        spReturnCustomerName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                model = customerList.get(position);
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
                    Toast.makeText(ReturnOrderSearchActivity.this, "Select Valid Date Range", Toast.LENGTH_SHORT).show();
                }
                else if(btnDateTo.getText().toString().equals("To")){
                    Toast.makeText(ReturnOrderSearchActivity.this, "Select Valid Date Range", Toast.LENGTH_SHORT).show();
                }
                else if(tvSelectedCustomerName.getText().toString().equals("")){
                    Toast.makeText(ReturnOrderSearchActivity.this, "Select Customer First", Toast.LENGTH_SHORT).show();
                }
                else if(etInvSearch.getText().toString().trim().equals("")){
                    etInvSearch.setError("Enter Invoice number");
                }
                else{
                    if(!etInvSearch.getText().toString().equals("")){
                        inv = Integer.parseInt(etInvSearch.getText().toString());
                    }
                    else{
                        inv = 0;
                    }
                    String compare = compareDates(btnDateFrom.getText().toString(), btnDateTo.getText().toString());
                    if (compare.equals("Date1 is before Date2") || compare.equals("Date1 is equal Date2")) {
                        if (ConnectionDetector.isConnectingToInternet(ReturnOrderSearchActivity.this)) {
                            HashMap<String ,String> hashMap=new HashMap<String ,String>();
                            hashMap.put("CompanyId",new SharedPrefs(ReturnOrderSearchActivity.this).getCompanyID());
                            hashMap.put("OrderNo",String.valueOf(inv));
                            hashMap.put("CustomerID", String.valueOf(model.getCustomer_id()));
                            hashMap.put("Date1",btnDateFrom.getText().toString());
                            hashMap.put("Date2",btnDateTo.getText().toString());
                            returnedOrder_viewModel.getReturnedOrdre(hashMap);
                            //Todo check
//                            getReturnedOrders(Utility.BASE_LIVE_URL+"api/Order/GetOrders?CompanyId="
//                                    +new SharedPrefs(ReturnOrderSearchActivity.this).getCompanyID()+"&OrderNo="+inv+"&CustomerID="+
//                                    model.getCustomer_id()+"&Date1="+btnDateFrom.getText().toString()+"&Date2="+btnDateTo.getText().toString());
                            Log.d("returnedOrderUrl", Utility.BASE_LIVE_URL+"api/Order/GetOrders?CompanyId="+
                                    new SharedPrefs(ReturnOrderSearchActivity.this).getCompanyID()+"&OrderNo="+inv+"&CustomerID="
                                    +model.getCustomer_id()+"&Date1="+btnDateFrom.getText().toString()+"&Date2="+btnDateTo.getText().toString());
                        } else {
                            Toast.makeText(ReturnOrderSearchActivity.this, "Check network connection and try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(ReturnOrderSearchActivity.this, "Select Valid Date Range", Toast.LENGTH_SHORT).show();
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

    private void createDatePicker(final String flag) {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        //launch datepicker modal
        DatePickerDialog datePickerDialog = new DatePickerDialog(ReturnOrderSearchActivity.this,
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
                                    Toast.makeText(ReturnOrderSearchActivity.this, "Select Customer First", Toast.LENGTH_SHORT).show();
                                } else if (compare.equals("Date1 is before Date2") || compare.equals("Date1 is equal Date2")) {
                                    if (ConnectionDetector.isConnectingToInternet(ReturnOrderSearchActivity.this)) {
                                        //Todo call api
                                        getReturnedOrders(Utility.BASE_LIVE_URL+"api/Order/GetOrders?CompanyId="+new SharedPrefs(ReturnOrderSearchActivity.this).getCompanyID()+"&OrderNo=0&CustomerID="+model.getCustomer_id()+"&Date1="+btnDateFrom.getText().toString()+"&Date2="+btnDateTo.getText().toString());
                                        Log.d("returnedOrderUrl", Utility.BASE_LIVE_URL+"api/Order/GetOrders?CompanyId="+new SharedPrefs(ReturnOrderSearchActivity.this).getCompanyID()+"&OrderNo=0&CustomerID="+model.getCustomer_id()+"&Date1="+btnDateFrom.getText().toString()+"&Date2="+btnDateTo.getText().toString());
                                    } else {
                                        Toast.makeText(ReturnOrderSearchActivity.this, "Check network connection and try again", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(ReturnOrderSearchActivity.this, "Select Valid Date Range", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            btnDateTo.setText(date);
                            if (!btnDateFrom.getText().toString().equals("From")) {
                                String compare = compareDates(btnDateFrom.getText().toString(), btnDateTo.getText().toString());
                                if (tvSelectedCustomerName.getText().equals("")) {
                                    Toast.makeText(ReturnOrderSearchActivity.this, "Select Customer First", Toast.LENGTH_SHORT).show();
                                } else if (compare.equals("Date1 is before Date2") || compare.equals("Date1 is equal Date2")) {
                                    if (ConnectionDetector.isConnectingToInternet(ReturnOrderSearchActivity.this)) {
                                        //Todo call api
                                        getReturnedOrders(Utility.BASE_LIVE_URL+"api/Order/GetOrders?CompanyId="+new SharedPrefs(ReturnOrderSearchActivity.this).getCompanyID()+"&OrderNo=0&CustomerID="+model.getCustomer_id()+"&Date1="+btnDateFrom.getText().toString()+"&Date2="+btnDateTo.getText().toString());
                                        Log.d("returnedOrderUrl", Utility.BASE_LIVE_URL+"api/Order/GetOrders?CompanyId="+new SharedPrefs(ReturnOrderSearchActivity.this).getCompanyID()+"&OrderNo=0&CustomerID="+model.getCustomer_id()+"&Date1="+btnDateFrom.getText().toString()+"&Date2="+btnDateTo.getText().toString());
                                    } else {
                                        Toast.makeText(ReturnOrderSearchActivity.this, "Check network connection and try again", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(ReturnOrderSearchActivity.this, "Select Valid Date Range", Toast.LENGTH_SHORT).show();
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

    private void getReturnedOrders(String url){
        returnedOrderList.clear();
        pd = new ProgressDialog(this);
        pd.setMessage("Loading Data");
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                pd.dismiss();
                Log.d("returnedOrderResposne", response);
                try {
                    JSONObject parent = new JSONObject(response);
                    JSONArray tableArray = parent.getJSONArray("Table");
                    if(tableArray.length() > 0){
                        for(int i = 0; i < tableArray.length(); i++){
                            JSONObject child = tableArray.getJSONObject(i);
                            RegisterdCustomerModel model = new RegisterdCustomerModel();
                            model.setServerOrderId(child.getInt("OrderNo"));
                            model.setOrderNumber(child.getString("OrderId"));
                            model.setCustomer_id(child.getInt("CustomerId"));
                            model.setDate(child.getString("OrderDateTime").replace("T"," "));
                            model.setCashMode(child.getString("SalesMode"));
                            model.setDeliveryStatus(child.getString("Status"));
                            model.setTotalQty(child.getInt("TotalQuantity"));
                            model.setNetTotal(String.valueOf(child.getInt("NetTotal")));
                            model.setRoute(child.getInt("RouteId"));
                            model.setName(child.getString("Name"));
                            model.setLat(String.valueOf(child.getDouble("Latitude")));
                            model.setLng(String.valueOf(child.getDouble("Longitude")));
                            model.setAddress(child.getString("Address"));
                            model.setTotalbill(String.valueOf(child.getInt("NetTotal")));
                            returnedOrderList.add(model);
                        }

                        returnedOrderRecyclerView.setVisibility(View.VISIBLE);
                        ReturnedOrderRecyclerViewAdapter adapter = new ReturnedOrderRecyclerViewAdapter(ReturnOrderSearchActivity.this, returnedOrderList);
                        returnedOrderRecyclerView.setAdapter(adapter);
                        returnedOrderRecyclerView.setLayoutManager(new LinearLayoutManager(ReturnOrderSearchActivity.this));
                    }
                    else{
                        returnedOrderRecyclerView.setVisibility(View.GONE);
                        Toast.makeText(ReturnOrderSearchActivity.this, "No orders found", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Log.d("returnedOrderError",error.getMessage()+"");
                if (error instanceof ServerError) {
                    Toast.makeText(ReturnOrderSearchActivity.this, "Server error occurred", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(ReturnOrderSearchActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(ReturnOrderSearchActivity.this, "Connection timeout error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        pd.show();
        queue.add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    private void searchCustomerDialog() {
        customerList = db.getSQLiteRegisterCustomerInfo(null);
        CustomerDilogListAdapter adapter = new CustomerDilogListAdapter(ReturnOrderSearchActivity.this, customerList);
        View view = getLayoutInflater().inflate(R.layout.item_search_diloag_layout, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReturnOrderSearchActivity.this);
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
                model = customerList.get(position);
                tvSelectedCustomerName.setText(model.getName());
                if (!btnDateFrom.getText().toString().equals("From") && !btnDateTo.getText().toString().equals("To")) {
                    String compare = compareDates(btnDateFrom.getText().toString(), btnDateTo.getText().toString());
                    if (tvSelectedCustomerName.getText().equals("")) {
                        Toast.makeText(ReturnOrderSearchActivity.this, "Select Customer First", Toast.LENGTH_SHORT).show();
                    } else if (compare.equals("Date1 is before Date2")) {
                        if (ConnectionDetector.isConnectingToInternet(ReturnOrderSearchActivity.this)) {
                            //Todo call api
                            getReturnedOrders(Utility.BASE_LIVE_URL+"api/Order/GetOrders?CompanyId="+new SharedPrefs(ReturnOrderSearchActivity.this).getCompanyID()+"&OrderNo=0&CustomerID="+model.getCustomer_id()+"&Date1="+btnDateFrom.getText().toString()+"&Date2="+btnDateTo.getText().toString());
                            Log.d("returnedOrderUrl", Utility.BASE_LIVE_URL+"api/Order/GetOrders?CompanyId="+new SharedPrefs(ReturnOrderSearchActivity.this).getCompanyID()+"&OrderNo=0&CustomerID="+model.getCustomer_id()+"&Date1="+btnDateFrom.getText().toString()+"&Date2="+btnDateTo.getText().toString());
                        } else {
                            Toast.makeText(ReturnOrderSearchActivity.this, "Check network connection and try again", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(ReturnOrderSearchActivity.this, "Select Valid Date Range", Toast.LENGTH_SHORT).show();
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
        itemSerachET.addTextChangedListener(new ReturnOrderSearchActivity.SearchItemListWatcher());
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
            filteredCustomerList = performItemSearch(customerList, query);
            itemSerachLV.setAdapter(new CustomerDilogListAdapter(ReturnOrderSearchActivity.this, filteredCustomerList));
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.actionRegNewCustomer:{
                Intent intent = new Intent(this, NewUserActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
                break;
            }
            case R.id.actionSyncCompanyCustomerInfo:{
                if (ConnectionDetector.isConnectingToInternet(this)) {
                    startService(new Intent(this, CompanyInfoService.class));
                    Utility.Toast(this, "Syncing Started...");
                } else {
                    Utility.Toast(this, "Check network connection and try again");
                }
                break;
            }
            case R.id.actionAddSalesOrder:{
                Intent intent = new Intent(this, TakeOrder.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
                break;
            }
            case R.id.actionSettings:{
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            }
            case R.id.actionAddSalesReturn:{

                Intent intent = new Intent(this, ReturnOrderSearchActivity.class);
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
                    GPSTracker gps = new GPSTracker(this);
                    if (ConnectionDetector.isConnectingToInternet(this)) {
                        if (gps.canGetLocation()) {
                            Utility.Toast(this, "Location Enable Successfully");
                            startservice();
                        } else {
                            Utility.Toast(this, "Enable your GPS first and try again..");
                            //gps.showSettingsAlert();
                        }
                    } else
                        Utility.Toast(this, "Check network connection and try again");
                    break;
                }
                else{
                    SpannableString spanString = new SpannableString("Enable Location");
                    spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0); //fix the color to white
                    item.setTitle(spanString);

                    Utility.Toast(this, "Location Disable Successfully");
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

        LinearLayout mainLayout = findViewById(R.id.mainLayout);
        View v = getLayoutInflater().inflate(R.layout.actionbar_view, mainLayout, false);
        EditText serach= v.findViewById(R.id.actionBarSerachET);
        serach.setVisibility(View.VISIBLE);
        btnMenu = v.findViewById(R.id.btnMenu);
        btnMenu.setVisibility(View.GONE);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.appbluegrey)));
        actionBar.setCustomView(v);
        ((TextView)v.findViewById(R.id.actionBarTextView)).setText("Search Return Order");
        serach.setVisibility(View.GONE);

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }
}

