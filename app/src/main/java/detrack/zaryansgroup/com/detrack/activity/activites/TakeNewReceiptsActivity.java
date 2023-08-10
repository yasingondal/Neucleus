package detrack.zaryansgroup.com.detrack.activity.activites;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import detrack.zaryansgroup.com.detrack.activity.Adapter.CustomerDilogListAdapter;


import detrack.zaryansgroup.com.detrack.activity.Map.GPSTracker;
import detrack.zaryansgroup.com.detrack.activity.Model.BankModels.CompanyWiseBanksModel;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyRouteModel.RouteModel;
import detrack.zaryansgroup.com.detrack.activity.Model.Params;
import detrack.zaryansgroup.com.detrack.activity.Model.PreviousAmountModel.PreviousAmountModel;
import detrack.zaryansgroup.com.detrack.activity.Model.PreviousAmountModel.PreviousListModel;
import detrack.zaryansgroup.com.detrack.activity.Model.ReceiptModel;
import detrack.zaryansgroup.com.detrack.activity.Model.RegisterCustomerModel.RegisterdCustomerModel;
import detrack.zaryansgroup.com.detrack.activity.retrofit.Api_Reto;
import detrack.zaryansgroup.com.detrack.activity.Service.CompanyInfoService;
import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;
import detrack.zaryansgroup.com.detrack.activity.viewmodels.TakeNewReceipt_ViewModel;
import detrack.zaryansgroup.com.detrack.activity.utilites.ConnectionDetector;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import detrack.zaryansgroup.com.detrack.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class TakeNewReceiptsActivity extends AppCompatActivity {

    //For Bank Work Option List of Banks
    Spinner spBankName;
    ArrayList<CompanyWiseBanksModel> Bankslist;
    List<String> BankNameList;
    ArrayAdapter<String> adapter_BankNames;
    int BankID;
    String BankName;


    Spinner routeCodeSP, routeNameSP;
    EditText customerSerachTV;
    ListView customerLV;
    CustomerDilogListAdapter adapter;
    TextView  PreviousAmount;
    ArrayList<RouteModel> routelist = new ArrayList<>();
    ArrayList<RegisterdCustomerModel> Reg_Customer_list = new ArrayList<>();
    ArrayList<RegisterdCustomerModel> filteredList = new ArrayList<>();
    RegisterdCustomerModel RCusotmerModel = null;
    LinearLayout customer_detailsLL, dateLL;
    String query = "", date = "";
//    ZEDTrackDB db;
    boolean IsSave = false;
    Button saveBtn, btnSync;
    TextView dateTV;
    EditText recevingAmount;
    AlarmManager alarm;
    PendingIntent pintent;
    Button btnCustomerName;
    TakeNewReceipt_ViewModel takeNewReceipt_viewModel;
    boolean inserted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipts);
        setUpActionBar(getSupportActionBar());


        takeNewReceipt_viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(TakeNewReceipt_ViewModel.class);
        CustomerListObserver();
        takeNewReceipt_viewModel.getSqlCustomerData(null); // load data
        takeNewReceipt_viewModel.getSqlRouteList();
        RouteListObserver();
        InsertedCheckObserver();

        //Bank Spinner work
        SpinnerBankIntializations();
        SpinnerBankDataObserver();
        FillSpinnerBank();
        SpinnerListenerBanks();



//        db = new ZEDTrackDB(this);
//        Reg_Customer_list = db.getSQLiteRegisterCustomerInfo(null);
        Log.d("reg_sizec",String.valueOf(Reg_Customer_list.size()));
        customer_detailsLL = findViewById(R.id.customer_detailsLL);
        dateLL = findViewById(R.id.dateLL);
        dateTV = findViewById(R.id.date);
        saveBtn = findViewById(R.id.saveBtn);
        btnCustomerName = findViewById(R.id.btnCustomerName);
        PreviousAmount = findViewById(R.id.PreviousAmount);
        recevingAmount = findViewById(R.id.recevingAmount);
        btnSync = findViewById(R.id.btnSync);
        final Calendar c = Calendar.getInstance();
        dateTV.setText(c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH) + " " + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":00");



//        date = Utility.getCurrentDate();

        date = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH);

        //Timber.d("date is "+date);


        btnCustomerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (ConnectionDetector.isConnectingToInternet(TakeNewReceiptsActivity.this))
                    Customer_Dilog();
                else
                    Utility.Toast(TakeNewReceiptsActivity.this, "No Internet Connection");
*/
                Customer_Dilog();
            }
        });
        dateLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.HideKeyBoard(v, TakeNewReceiptsActivity.this);
                if (!recevingAmount.getText().toString().equals("") || recevingAmount.length() != 0) {
                    if (IsSave) {
                        ReceiptModel model = new ReceiptModel();
                        model.setServerID(0);
                        model.setCustomerID(RCusotmerModel.getCustomer_id());
                        model.setCustomerName(RCusotmerModel.getName());
                        model.setRemarks("");
                        model.setDate(date);
                        model.setBalance(0);
                        model.setPreviousBalnc(0);
                        model.setIsSync(0);
                        float recAmount = Float.parseFloat(recevingAmount.getText().toString());
                        model.setAmountPaid(recAmount);

                        //Cash Deposited Bank work
                        model.setCashDepositedBankId(BankID);
                        model.setCashDepositedBankName(BankName);

                        takeNewReceipt_viewModel.insertCustomerReceipt(model);

                    } else
                        Utility.Toast(TakeNewReceiptsActivity.this, "Select Customer First");

                } else
                    Utility.Toast(TakeNewReceiptsActivity.this, "Select Receiving Amount First");

            }
        });


        btnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                syncData();
            }
        });


    }


    //Bank spinner work...
    private void SpinnerBankIntializations() {
        spBankName = (Spinner) findViewById(R.id.spBankName1);

    }

    private void SpinnerBankDataObserver() {
        takeNewReceipt_viewModel.getSqlBankslist().observe(this, new Observer<ArrayList<CompanyWiseBanksModel>>() {
            @Override
            public void onChanged(ArrayList<CompanyWiseBanksModel> data) {
                Bankslist = data;
                BankNameList = new ArrayList<String>();
                for (int i = 0; i < Bankslist.size(); i++) {
                    BankNameList.add(Bankslist.get(i).getBankName());
                    Log.d("bank_details","code="+Bankslist.get(i).getBankAccountNbr()+": id="+Bankslist.get(i).getBankID());
//                    Log.d("bank_detailsnames","=>"+RouteNameList1.get(i));
                }

                adapter_BankNames = new ArrayAdapter<String>(TakeNewReceiptsActivity.this, android.R.layout.simple_spinner_item, BankNameList);
                adapter_BankNames.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spBankName.setAdapter(adapter_BankNames);
            }
        });

    }

    private void FillSpinnerBank() {

        takeNewReceipt_viewModel.getSqlBanksList();
    }

    private void SpinnerListenerBanks() {

        spBankName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                //getting position
                BankID = Bankslist.get(position).getBankID();
                BankName = Bankslist.get(position).getBankName();

                Log.d("Bankidis", String.valueOf(BankID));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }




    private void syncData(){

        if (btnCustomerName.getText().toString().equals("Select Customer")) {
            Utility.Toast(TakeNewReceiptsActivity.this, "Select Customer First");
        }
        else if (!ConnectionDetector.isConnectingToInternet(TakeNewReceiptsActivity.this)) {
            Utility.Toast(TakeNewReceiptsActivity.this, "Check network connection and try again");
        } else {
//            Utility.HideKeyBoard(v, TakeNewReceiptsActivity.this);
            btnCustomerName.setText(RCusotmerModel.getName());
            IsSave = true;
            ArrayList<Params> parmeter = new ArrayList<Params>();
            Params p1 = new Params();
            p1.setKey("CustomerId");
            p1.setValue(String.valueOf(RCusotmerModel.getCustomer_id()));
            parmeter.add(p1);
            //todo reciept
            getCustomerPreviousAmount();
//                    getCustomerPreviousAmount("http://deliveryapi.zederp.net/api/Receipts/GetCustomerPreviousReceiptsAmount2?CustomerId=" + RCusotmerModel.getCustomer_id());
//                    getCustomerPreviousAmount(Utility.BASE_LIVE_URL +"api/Receipts/GetCustomerPreviousReceiptsAmount2?CustomerId=" + RCusotmerModel.getCustomer_id());
            Log.d("amountUrl", Utility.BASE_LIVE_URL +"api/Receipts/GetCustomerPreviousReceiptsAmount2?CustomerId=" + RCusotmerModel.getCustomer_id());
//                    new SendDataToService(TakeNewReceiptsActivity.this, "GetCustomerPreviousReceiptsAmount", "ZEDtrack.asmx", parmeter) {
//                        ProgressDialog pd;
//
//                        @Override
//                        protected void onPreExecute() {
//                            super.onPreExecute();
//                            pd = new ProgressDialog(TakeNewReceiptsActivity.this);
//                            pd.setMessage("Please wait");
//                            pd.show();
//                        }
//
//                        @Override
//                        protected void onPostExecute(String s) {
//                            super.onPostExecute(s);
//                            pd.dismiss();
//                            try {
//                                if (s != null) {
//                                    reg_cust_TV.setText(RCusotmerModel.getName());
//                                    IsSave = true;
//                                    JSONArray jsonArray = new JSONArray(s);
//                                    JSONObject object = (JSONObject) jsonArray.get(0);
//                                    String previousAmount = object.getString("PreviousAmount");
//                                    if (previousAmount.equals("0")) {
//                                        PreviousAmount.setText("0.0");
//                                    } else {
//                                        PreviousAmount.setText(previousAmount);
//                                    }
//                                } else {
//                                    IsSave = false;
//                                    reg_cust_TV.setText("Select Customer Name");
//                                    Utility.logCatMsg("Server Error");
//                                    Utility.Toast(TakeNewReceiptsActivity.this, "Failed Try again.");
//                                }
//                            } catch (Exception e) {
//                                IsSave = false;
//                                reg_cust_TV.setText("Select Customer Name");
//                                Utility.Toast(TakeNewReceiptsActivity.this, "Failed Try again.");
//                                Utility.logCatMsg("Error " + e.getMessage());
//                            }
//                        }
//                    }.execute();

        }
    }

    private void InsertedCheckObserver() {
        takeNewReceipt_viewModel.checkInserted().observe(this,
                new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        inserted = aBoolean;
                        if (inserted) {
                            Utility.Toast(TakeNewReceiptsActivity.this, "Record Saved");
                            finish();
                        } else {
                            Utility.logCatMsg("Record Not save in Sqlite");
                            Utility.Toast(TakeNewReceiptsActivity.this, "Failed");
                        }
                    }
                });
    }

    private void RouteListObserver() {
        takeNewReceipt_viewModel.getSqlroutelist().observe(this,
                new Observer<ArrayList<RouteModel>>() {
                    @Override
                    public void onChanged(ArrayList<RouteModel> routeModels) {
                        routelist = routeModels;
                    }
                });
    }

    private void CustomerListObserver() {
        takeNewReceipt_viewModel.getSqlCustomerData().observe(this,
                new Observer<ArrayList<RegisterdCustomerModel>>() {
                    @Override
                    public void onChanged(ArrayList<RegisterdCustomerModel> registerdCustomerModels) {
                        Reg_Customer_list.addAll(registerdCustomerModels);
                        if(Reg_Customer_list.size() > 0){
                            if(RCusotmerModel == null){
                                //setting this model only first time when this model is null
                                RCusotmerModel = Reg_Customer_list.get(0);
                            }
                            if(adapter !=null){
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

//        for (int i = 0; i < menu.size(); i++) {
//            MenuItem item = menu.getItem(i);
//            SpannableString spanString = new SpannableString(menu.getItem(i).getTitle().toString());
//            spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0); //fix the color to white
//            item.setTitle(spanString);
//        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.actionRegNewCustomer: {
                Intent intent = new Intent(this, NewUserActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
                break;
            }
            case R.id.actionSyncCompanyCustomerInfo: {
                if (ConnectionDetector.isConnectingToInternet(this)) {
                    startService(new Intent(this, CompanyInfoService.class));
                    Utility.Toast(this, "Syncing Started...");
                } else {
                    Utility.Toast(this, "Check network connection and try again");
                }
                break;
            }
            case R.id.actionAddSalesOrder: {
                Intent intent = new Intent(this, TakeOrder.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
                break;
            }
            case R.id.actionSettings: {
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            }
            case R.id.actionAddSalesReturn: {

                Intent intent = new Intent(this, ReturnOrderSearchActivity.class);
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
                } else {
                    SpannableString spanString = new SpannableString("Enable Location");
                    spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0); //fix the color to white
                    item.setTitle(spanString);

                    Utility.Toast(this, "Location Disable Successfully");
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

    private void setUpActionBar(ActionBar actionBar) {
        LinearLayout mainLayout = findViewById(R.id.mainLayout);
        View v = getLayoutInflater().inflate(R.layout.actionbar_view, mainLayout, false);
        TextView actionbar = v.findViewById(R.id.actionBarTextView);
        actionbar.setText("Add New Receipt");
        ImageButton btnMenu = v.findViewById(R.id.btnMenu);
        ImageView companyLogo = v.findViewById(R.id.companyLogo);
        btnMenu.setVisibility(View.GONE);
        try {
            String base = new SharedPrefs(TakeNewReceiptsActivity.this).getCompanyLogo();
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

    private void Customer_Dilog() {

        View view = getLayoutInflater().inflate(R.layout.customer_diloag_layout, null);
        routeCodeSP = view.findViewById(R.id.route_code);
        routeNameSP = view.findViewById(R.id.route_name);
        fillSpinner();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TakeNewReceiptsActivity.this);
        View view1= getLayoutInflater().inflate(R.layout.dialog_custom_title, null);
        TextView tvCustomTitle = view1.findViewById(R.id.tvCustomTitle);
        tvCustomTitle.setText("Select Customer");
        alertDialogBuilder.setCustomTitle(view1);
        alertDialogBuilder
                .setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        customerSerachTV = view.findViewById(R.id.inputSearch);
        customerLV = view.findViewById(R.id.customer_list_view);
        adapter = new CustomerDilogListAdapter(TakeNewReceiptsActivity.this, Reg_Customer_list);
        customerLV.setAdapter(adapter);
        customerLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                RegisterdCustomerModel model = (RegisterdCustomerModel) parent.getItemAtPosition(position);
                RCusotmerModel = model;

                Utility.HideKeyBoard(view, TakeNewReceiptsActivity.this);
                alertDialog.dismiss();

                btnCustomerName.setText(RCusotmerModel.getName());
                IsSave = true;
                syncData();


            }
        });
        openSearchBar();
        alertDialog.show();
    }

    private void fillSpinner() {
//        routelist = db.getCompanyRoute();

        List<String> RouteCodeList = new ArrayList<String>();
        List<String> RouteNameList = new ArrayList<String>();
        RouteCodeList.add("0");
        RouteNameList.add("All Routes");
        for (int i = 0; i < routelist.size(); i++) {
            RouteCodeList.add(routelist.get(i).getRoute_code());
            RouteNameList.add(routelist.get(i).getRoute_name());
            Utility.logCatMsg("Route List code " + routelist.get(i).getRoute_code() + " Route List Name " + routelist.get(i).getRoute_name());
        }
        ArrayAdapter<String> adapter_routeCode = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, RouteCodeList);
        adapter_routeCode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        routeCodeSP.setAdapter(adapter_routeCode);
        routeCodeSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Reg_Customer_list.clear();
//                Reg_Customer_listl;
                takeNewReceipt_viewModel.getSqlCustomerData(routelist.get(position).getRoute_id() + "");
//                Reg_Customer_list = db.getSQLiteRegisterCustomerInfo(routelist.get(position).getRoute_id() + "");
                adapter = new CustomerDilogListAdapter(TakeNewReceiptsActivity.this, Reg_Customer_list);
                customerLV.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> adapter_routeName = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, RouteNameList);
        adapter_routeName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        routeNameSP.setAdapter(adapter_routeName);
        routeNameSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Reg_Customer_list.clear();
                if (position == 0)
//                    Reg_Customer_list = db.getSQLiteRegisterCustomerInfo(null);
                takeNewReceipt_viewModel.getSqlCustomerData(null);
                else
                    takeNewReceipt_viewModel.getSqlCustomerData(routelist.get(position - 1).getRoute_id() + "");
//                    Reg_Customer_list = db.getSQLiteRegisterCustomerInfo(routelist.get(position - 1).getRoute_id() + "");
                adapter = new CustomerDilogListAdapter(TakeNewReceiptsActivity.this, Reg_Customer_list);
                customerLV.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void openSearchBar() {
        customerSerachTV.addTextChangedListener(new SearchWatcher());
        customerSerachTV.setHint("Search....");
    }

    private class SearchWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence c, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence c, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            query = customerSerachTV.getText().toString();
            filteredList = performSearch(Reg_Customer_list, query);
            customerLV.setAdapter(new CustomerDilogListAdapter(TakeNewReceiptsActivity.this, filteredList));
        }
    }

    private ArrayList<RegisterdCustomerModel> performSearch(ArrayList<RegisterdCustomerModel> modal, String query) {
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

    //get Previous Amount new API implemented


    private void getCustomerPreviousAmount() {



        Utility.setProgressDialog(this,"Loading","Please Wait");
        Utility.showProgressDialog();
        Api_Reto.getRetrofit().getRetrofit_services().getCustomerPerviousAmount(RCusotmerModel.getCustomer_id())
                .enqueue(new Callback<PreviousListModel>() {
                    @Override
                    public void onResponse(Call<PreviousListModel> call, Response<PreviousListModel> response) {


                        Log.d("amountResponseR", String.valueOf(response.body()));
                        Log.d("amountCodeR", String.valueOf(response.code()));
                        Log.d("amountUrlR", String.valueOf(call.request().url()));
                        if(response.body() != null){

                            PreviousListModel previousListModel = response.body();
                            List<PreviousAmountModel> table = previousListModel.getTable();
                            if (table.size() > 0) {
                                double amount = table.get(0).getPreviousAmount();
                                PreviousAmount.setText(String.valueOf(amount));
                                Toast.makeText(TakeNewReceiptsActivity.this, "Successfully Synced", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(TakeNewReceiptsActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                            }
                            Utility.hideProgressDialog();
                        }else {
                            Toast.makeText(TakeNewReceiptsActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                            Utility.hideProgressDialog();
                        }



                    }

                    @Override
                    public void onFailure(Call<PreviousListModel> call, Throwable t) {

                        Log.d("amountErrorR", t.getMessage() + "::"+t.toString());
                        Log.d("amountError", t.getMessage() + "");
                        if (t instanceof ServerError) {
                            Toast.makeText(TakeNewReceiptsActivity.this, "Server error occurred", Toast.LENGTH_SHORT).show();
                        } else if (t instanceof NetworkError) {
                            Toast.makeText(TakeNewReceiptsActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                        } else if (t instanceof TimeoutError) {
                            Toast.makeText(TakeNewReceiptsActivity.this, "Connection timeout error", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

}
