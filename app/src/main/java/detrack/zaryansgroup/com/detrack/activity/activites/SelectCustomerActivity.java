package detrack.zaryansgroup.com.detrack.activity.activites;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import detrack.zaryansgroup.com.detrack.activity.Adapter.CustomerDilogListAdapter;

import detrack.zaryansgroup.com.detrack.activity.Map.GPSTracker;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyRouteModel.RouteModel;
import detrack.zaryansgroup.com.detrack.activity.Model.DistributorModel.DistributorModel;
import detrack.zaryansgroup.com.detrack.activity.Model.RegisterCustomerModel.RegisterdCustomerModel;
import detrack.zaryansgroup.com.detrack.activity.Service.CompanyInfoService;
import detrack.zaryansgroup.com.detrack.activity.Service.GPSService;
import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;
import detrack.zaryansgroup.com.detrack.activity.activites.SecondView.SelectProductActivitySecond;
import detrack.zaryansgroup.com.detrack.activity.viewmodels.SelectCustomer_ViewModel;
import detrack.zaryansgroup.com.detrack.activity.utilites.ConnectionDetector;
import detrack.zaryansgroup.com.detrack.R;
import detrack.zaryansgroup.com.detrack.activity.SQLlite.ZEDTrackDB;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import timber.log.Timber;


public class SelectCustomerActivity extends AppCompatActivity {

    TextView tvCustomerCode, tvCustomerName, tvCustomerMobile, tvCustomerAddress, tvCustomerRoute;
    Button btnNext;
    EditText etSearchCustomer;

    boolean subdistributorCheck;


    Spinner spRouteName;
    Spinner spDistributorNames;
    Spinner spSubDistributorNames;
    LinearLayout llSubDistributorsList;
    TextView tvDistributerId, tvSubDistributerId;
    LinearLayout llSubDistributorsIds;

    Spinner spCustomerName;
    ArrayList<RouteModel> routelist;

    ArrayList<DistributorModel> distributorsList;
    ArrayList<DistributorModel> subDistributorsList;

    ArrayList<RegisterdCustomerModel> filteredList;
    ArrayList<RegisterdCustomerModel> Reg_Customer_list;
    ZEDTrackDB db;
    RegisterdCustomerModel customerDataModel, startModel;
    TextView actionbar;
    ImageButton btnMenu;
    ImageView companyLogo;
    AlarmManager alarm;
    PendingIntent pintent;
    boolean updateLocation;
    LinearLayout setLocationLinear;
    CheckBox cbSetLocation;
    TextView tvGetLatLng;

    String lat = "";
    String lng = "";
    private int spinnerPostion = 0;
    SharedPrefs sharedPrefs;

    public static String addOrder = "";
    SelectCustomer_ViewModel mSelectCustomer_viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_customer);
        setUpActionBar(getSupportActionBar());


        addOrder = getIntent().getStringExtra("addOrder");
        //init view model
        mSelectCustomer_viewModel = new ViewModelProvider(this,
                (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(SelectCustomer_ViewModel.class);
        actionbar.setText("Select Customer");
        sharedPrefs = new SharedPrefs(this);

        customerDataModel = new RegisterdCustomerModel();

        initializeWidgets();
        SpinnerDataObserver();
        fillSpinner();

        SpinnerDataObserverDistributors();
        SpinnersListnerDistributors();

        SpinnersListner();
        InitilizeAlaram();


    }

    private void SpinnersListnerDistributors() {
        spDistributorNames.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                tvDistributerId.setText(distributorsList.get(position).getContactId()+"");
                customerDataModel.setDistributorId(distributorsList.get(position).getContactId());


                fetchSubDistributors(distributorsList.get(position).getContactId());

                final List<String> subDistributorsNameList = new ArrayList<String>();
                for (int i = 0; i < subDistributorsList.size(); i++) {
                    subDistributorsNameList.add(subDistributorsList.get(i).getName());
                }


                ArrayAdapter<String> adapter_subDistributors = new ArrayAdapter<String>(SelectCustomerActivity.this,
                        android.R.layout.simple_spinner_item, subDistributorsNameList);
                adapter_subDistributors.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spSubDistributorNames.setAdapter(adapter_subDistributors);

                if(subDistributorsList.size()>0){
                    llSubDistributorsList.setVisibility(View.VISIBLE);
                    subdistributorCheck = true;
                }else{
                    llSubDistributorsList.setVisibility(View.GONE);
                    subdistributorCheck = false;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spSubDistributorNames.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                tvSubDistributerId.setText(subDistributorsList.get(position).getContactId()+"");
                customerDataModel.setSubDistributorId(Integer.parseInt(tvSubDistributerId.getText().toString()));


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private void fetchSubDistributors(int contactId) {

        subDistributorsList.clear();
        subDistributorsList = db.getsubDistributors(contactId);

    }

    private void SpinnerDataObserverDistributors() {
        mSelectCustomer_viewModel.getDistributerlist().observe(this, new Observer<ArrayList<DistributorModel>>() {
            @Override
            public void onChanged(ArrayList<DistributorModel> data) {

                distributorsList = data;

                final List<String> distributorsNameList = new ArrayList<String>();
                for (int i = 0; i < distributorsList.size(); i++) {
                    distributorsNameList.add(distributorsList.get(i).getName());
                }
                ArrayAdapter<String> adapter_distributors = new ArrayAdapter<String>(SelectCustomerActivity.this,
                        android.R.layout.simple_spinner_item, distributorsNameList);
                adapter_distributors.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spDistributorNames.setAdapter(adapter_distributors);

            }
        });
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

        ScrollView mainLayout = findViewById(R.id.mainLayout);
        View v = getLayoutInflater().inflate(R.layout.actionbar_view, mainLayout, false);
        actionbar = v.findViewById(R.id.actionBarTextView);
        btnMenu = v.findViewById(R.id.btnMenu);
        btnMenu.setVisibility(View.GONE);
        companyLogo = v.findViewById(R.id.companyLogo);

        try {

            String base = new SharedPrefs(SelectCustomerActivity.this).getCompanyLogo();

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

    private void initializeWidgets() {
        etSearchCustomer = findViewById(R.id.etSearchCustomer);
        spRouteName = findViewById(R.id.spRouteName);
        spDistributorNames = findViewById(R.id.spDistributorNames);
        spSubDistributorNames = findViewById(R.id.spSubDistributorNames);
        llSubDistributorsList = findViewById(R.id.llSubDistributorsList);
        spCustomerName = findViewById(R.id.spCustomerName);
        tvDistributerId = findViewById(R.id.tvDistributerId);
        tvSubDistributerId = findViewById(R.id.tvSubDistributerId);
        llSubDistributorsIds = findViewById(R.id.llSubDistributorsIds);
        btnNext = findViewById(R.id.btnNext);
        tvCustomerCode = findViewById(R.id.tvCustomerCode);
        tvCustomerName = findViewById(R.id.tvCustomerName);
        tvCustomerMobile = findViewById(R.id.tvCustomerMobile);
        tvCustomerAddress = findViewById(R.id.tvCustomerAddress);
        tvCustomerRoute = findViewById(R.id.tvCustomerRoute);
        setLocationLinear = findViewById(R.id.setLocationLinear);
        cbSetLocation = findViewById(R.id.cbSetLocation);
        tvGetLatLng = findViewById(R.id.tvGetLatLng);
        etSearchCustomer.addTextChangedListener(new SearchWatcher());

        updateLocation = getIntent().getBooleanExtra("updateLocation",false);
        llSubDistributorsIds.setVisibility(View.GONE);


        if(!updateLocation) {
            cbSetLocation.setVisibility(View.INVISIBLE);
        }else {
            btnNext.setText("Update Location");
        }
        cbSetLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (cbSetLocation.isChecked()) {
                    final GPSTracker gps = new GPSTracker(SelectCustomerActivity.this);
                    if (gps.canGetLocation()) {
                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();
                        Log.d("location",latitude+"");
                        cbSetLocation.setText("Your Current Location is " + latitude + " , " + longitude);
                        if (latitude > 0.0) {
                            lat = latitude + "";
                            lng = longitude + "";
                            cbSetLocation.setText("Your Current Location is " + latitude + " , " + longitude);
                        } else
                            cbSetLocation.setText("Please try again.");
                    } else
                    {
                        cbSetLocation.setChecked(false);
                        gps.showSettingsAlert();
                    }
                }
                else{
                    cbSetLocation.setText("User Current Location");
                }
            }
        });


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(updateLocation){

                    setLocationLinear.setVisibility(View.VISIBLE);
                    if(!cbSetLocation.isChecked()){
                        Toast.makeText(SelectCustomerActivity.this, "Set location first", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        //todo: update customer api
                        if(ConnectionDetector.isConnectingToInternet(SelectCustomerActivity.this)){
                            if(!(customerDataModel.getCustomer_id() >0)){
                                Toast.makeText(SelectCustomerActivity.this, "select customer first", Toast.LENGTH_SHORT).show();
                            }
                            else{
//                                updateCustomerLocation();
                                updateCustomerLocation(Utility.BASE_LIVE_URL+"api/customer/updateCustomerLatLong?CustomerId="+ customerDataModel.getCustomer_id()+"&Longitude="+lng+"&Latitude="+lat);
//                                Log.d("updateLocationUrl","http://deliveryapi.zederp.net/api/customer/updateCustomerLatLong?CustomerId="+model.getCustomer_id()+"&Longitude="+lng+"&Latitude="+lat);
                            }
                        }
                        else{
                            Toast.makeText(SelectCustomerActivity.this, "Check network connection and try again", Toast.LENGTH_SHORT).show();
                        }


                    }
                }else {
                    btnNext.setText("Next");
                    setLocationLinear.setVisibility(View.GONE);

                    Timber.d("the view is "+sharedPrefs.getView());


                    if (!tvCustomerName.getText().toString().equals("")) {
                        if(sharedPrefs.getView().equals("secondView")){



                               customerDataModel.setDistributorId(Integer.parseInt(tvDistributerId.getText().toString()));


                               if(subdistributorCheck){
                                   customerDataModel.setSubDistributorId(Integer.parseInt(tvSubDistributerId.getText().toString()));
                               }else{
                                   customerDataModel.setSubDistributorId(0);
                               }


                            Intent intent = new Intent(SelectCustomerActivity.this, SelectProductActivitySecond.class);
                            intent.putExtra("selectedCustomer", customerDataModel);
                            intent.putExtra("addOrder",true);
                            startActivity(intent);
                        }else {

                            Intent intent = new Intent(SelectCustomerActivity.this, SelectProductActivity.class);
                            intent.putExtra("selectedCustomer", customerDataModel);
                            intent.putExtra("addOrder",true);
                            startActivity(intent);
                        }

                    }
                    else{
                        Toast.makeText(SelectCustomerActivity.this, "Select Customer First", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        //initializing other objects
        routelist = new ArrayList<>();
        filteredList = new ArrayList<>();
        distributorsList = new ArrayList<>();
        subDistributorsList = new ArrayList<>();
        db = new ZEDTrackDB(this);
        Reg_Customer_list = new ArrayList<>();
        Reg_Customer_list = db.getSQLiteRegisterCustomerInfo(null);
        startModel = new RegisterdCustomerModel();
        startModel.setName("Select Customer");



        tvCustomerMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvCustomerMobile.getText()!=null)
                {
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",tvCustomerMobile.getText().toString(), null)));
                }
                else{
                    Toast.makeText(SelectCustomerActivity.this, "Customer's Contact Not Found", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void updateCustomerLocation(String url){
//
//        Api_Reto.getRetrofit().getRetrofit_services().updateCustomerLocation(String.valueOf(model.getCustomer_id()),lng,lat)
//                .enqueue(new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
////                        ConvertResponseToString convertResponseToString = new ConvertResponseToString();
////                        String responceSt = convertResponseToString.getString(response);
//
//                        String responceSt = new Gson().toJson(response);
//                        Log.d("updateLocationResponse",responceSt);
//                        Log.d("updateLocationeurl",call.request().url().toString());
//
//                        if(responceSt.equals("1")){
//                            Toast.makeText(SelectCustomerActivity.this, "Location Updated Successfully", Toast.LENGTH_SHORT).show();
//                        }
//                        else{
//                            Toast.makeText(SelectCustomerActivity.this, "Location not Updated", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                    }
//                });

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response.equals("1")){
                    Toast.makeText(SelectCustomerActivity.this, "Location Updated Successfully", Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(SelectCustomerActivity.this, "Location not Updated", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("updateLocationError",error.getMessage()+"");
                if (error instanceof ServerError) {
                    Toast.makeText(SelectCustomerActivity.this, "Server error occurred", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(SelectCustomerActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(SelectCustomerActivity.this, "Connection timeout error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }



    private void SpinnerDataObserver(){

        mSelectCustomer_viewModel.getSqlroutelist().observe(this, new Observer<ArrayList<RouteModel>>() {
            @Override
            public void onChanged(ArrayList<RouteModel> data) {

                routelist = data;

                final List<String> RouteNameList = new ArrayList<String>();
                for (int i = 0; i < routelist.size(); i++) {
                    RouteNameList.add(routelist.get(i).getRoute_name());
                    Log.d("routelist_details","code="+routelist.get(i).getRoute_code()+": id="+routelist.get(i).getRoute_id());
                    Utility.logCatMsg("Route List code " + routelist.get(i).getRoute_code() + " Route List Name " +
                            routelist.get(i).getRoute_name());
                }
                ArrayAdapter<String> adapter_routeName = new ArrayAdapter<String>(SelectCustomerActivity.this,
                        android.R.layout.simple_spinner_item, RouteNameList);
                adapter_routeName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spRouteName.setAdapter(adapter_routeName);

            }
        });

        mSelectCustomer_viewModel.getSqlCustomerinfo().observe(this, new Observer<ArrayList<RegisterdCustomerModel>>() {
            @Override
            public void onChanged(ArrayList<RegisterdCustomerModel> registerdCustomerModels) {

                Reg_Customer_list.clear();
                Reg_Customer_list = registerdCustomerModels;
                CustomerDilogListAdapter adapter = new CustomerDilogListAdapter(SelectCustomerActivity.this, Reg_Customer_list);
                spCustomerName.setAdapter(adapter);

            }
        });





    }

    private void SpinnersListner() {
        spRouteName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Timber.d("Spinner called");
                Reg_Customer_list.clear();
                spinnerPostion = position;
                mSelectCustomer_viewModel.getSqlCustomerInfo(routelist.get(position).getRoute_id() + "");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spCustomerName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                customerDataModel = Reg_Customer_list.get(position);



                if(customerDataModel.getName().equals("Select Customer")){
                    tvCustomerName.setText("");
                    tvCustomerAddress.setText("");
                    tvCustomerMobile.setText("");
                    tvCustomerRoute.setText("");
                    tvCustomerCode.setText("");
                }
                else{
                    tvCustomerName.setText(customerDataModel.getName());
                    tvCustomerAddress.setText(customerDataModel.getAddress());
                    tvCustomerMobile.setText(customerDataModel.getCell());
                    tvCustomerRoute.setText(db.getRouteName(customerDataModel.getRoute()));
                    tvCustomerCode.setText(customerDataModel.getCode());
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void fillSpinner() {

        mSelectCustomer_viewModel.getSqlRouteList();
        mSelectCustomer_viewModel.getSqlDistributorsList();

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


            String query = etSearchCustomer.getText().toString();

            filteredList = performSearch(Reg_Customer_list, query);


            spCustomerName.setAdapter(new CustomerDilogListAdapter(SelectCustomerActivity.this, filteredList));
            spCustomerName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



                    customerDataModel = filteredList.get(position);


                    tvCustomerName.setText(customerDataModel.getName());
                    tvCustomerAddress.setText(customerDataModel.getAddress());
                    tvCustomerMobile.setText(customerDataModel.getCell());
                    tvCustomerRoute.setText(db.getRouteName(customerDataModel.getRoute()));
                    tvCustomerCode.setText(customerDataModel.getCode());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
    }

    private ArrayList<RegisterdCustomerModel> performSearch(ArrayList<RegisterdCustomerModel> modal, String query) {
        String[] queryByWords = query.toLowerCase().split("\\s+");


        ArrayList<RegisterdCustomerModel> filter = new ArrayList<>();

        for (int i = 1; i < modal.size(); i++) {
            RegisterdCustomerModel data = modal.get(i);


            String name = data.getName().toLowerCase();
           // String code = data.getCode();
           // String codeL = data.getCode().toLowerCase();



            String route = db.getRouteName(data.getRoute());

            String phoneNumber = data.getCell();

            for (String word : queryByWords) {
                int numberOfMatches = queryByWords.length;
                if (name.contains(word) || phoneNumber.contains(word) ) {
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
}
