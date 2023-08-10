package detrack.zaryansgroup.com.detrack.activity.activites;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Calendar;
import detrack.zaryansgroup.com.detrack.R;
import detrack.zaryansgroup.com.detrack.activity.Adapter.DailySaleSummeryAdapter;
import detrack.zaryansgroup.com.detrack.activity.Adapter.TotalBillRecyclerViewAdapter;
import detrack.zaryansgroup.com.detrack.activity.Map.GPSTracker;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.DeliveryItemModel;
import detrack.zaryansgroup.com.detrack.activity.Model.DailySummeryModel;
import detrack.zaryansgroup.com.detrack.activity.Service.CompanyInfoService;
import detrack.zaryansgroup.com.detrack.activity.Service.GPSService;
import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;
import detrack.zaryansgroup.com.detrack.activity.utilites.ConnectionDetector;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import detrack.zaryansgroup.com.detrack.activity.viewmodels.DailySaleSummary_ViewModel;
import timber.log.Timber;



public class DailySaleSummary extends AppCompatActivity {
    ListView listView;
    ArrayList<DailySummeryModel> summeryList;

    String date;

    int iCounters;
    int cashsalescounter = 0;
    int creditcounter = 0;



    TextView totalqtyTv, totalAmountTv, totalCashTv, totalCreditTv, totalReceiptTv, netTotalCashTv, numberOfRecordTv, userName, dateTV;
    TextView tvTotalReturnOrderQty,tvTotalReturnOrderCash,tvTotalRecieptsQty,tvTotalRecivedCash;


    int rTotalRecieptsQty;

    float rTotalReturnAmountCredit =0f;
    float rTotalReturnAmountCash =0f;
    float rTotalCashSaleAmount =0f;
    float rTotalCreditSaleAmount =0f;
    float rTotalCashInHand=0f;


    int rReturnSalesCounter=0;
    int rSaleCounter=0;

    int totalCashRecept =0;
    AlarmManager alarm;
    PendingIntent pintent;
    ArrayList<DailySummeryModel> receiptList;
    DailySaleSummary_ViewModel dailySaleSummaryViewModel;
    float totalAmount = 0, totalCash = 0, totalCredit = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_sale_summary);
        setUpActionBar(getSupportActionBar());
        InitilizeAlaram();

        Timber.d("Daily Sales Summary is calling");


        final Calendar c = Calendar.getInstance();
        date = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH);

        listView = findViewById(R.id.listview);
        summeryList = new ArrayList<>();
        receiptList = new ArrayList<>();
        userName = findViewById(R.id.sale_man);
        dateTV = findViewById(R.id.date);


        totalqtyTv = findViewById(R.id.totalqty);
        totalAmountTv = findViewById(R.id.totalAmount);
        totalCashTv = findViewById(R.id.totalCash);
        totalCreditTv = findViewById(R.id.totalCredit);
        totalReceiptTv = findViewById(R.id.totalReceipt);
        netTotalCashTv = findViewById(R.id.netTotalCash);
        numberOfRecordTv = findViewById(R.id.numberOfRecord);


        tvTotalReturnOrderQty  = findViewById(R.id.tvReturnOrderQty);
        tvTotalReturnOrderCash = findViewById(R.id.tvReturnOrderCash);
        tvTotalRecieptsQty     = findViewById(R.id.tvRecieptsQty);
        tvTotalRecivedCash     = findViewById(R.id.tvrecivedCash);

        dateTV.setText(Utility.getCurrentDate());
        dailySaleSummaryViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(DailySaleSummary_ViewModel.class);

        initObservers();
        dailySaleSummaryViewModel.loadDailySaleSummary(Utility.getCurrentDate(), new SharedPrefs(this).getDesignation());
        dailySaleSummaryViewModel.loadReceiptList(date);


        listViewClickListner();

    }



    private void fillSummaryAdapter() {
        listView.setAdapter(new DailySaleSummeryAdapter(DailySaleSummary.this, summeryList));
        int totalqty = 0;
        totalCash = 0;
        totalAmount = 0;
        totalCredit = 0;


        for (int i = 0; i < summeryList.size(); i++) {

            totalqty = totalqty + summeryList.get(i).getItemQty();
            totalCash = totalCash + summeryList.get(i).getCash();
            totalAmount = totalAmount + summeryList.get(i).getAmount();
            totalCredit = totalCredit + summeryList.get(i).getCredit();

        }






        numberOfRecordTv.setText(summeryList.size() + "");
        userName.setText(new SharedPrefs(DailySaleSummary.this).getEmployeeName());


    }

    private void initObservers() {


        dailySaleSummaryViewModel.getSaleSummaryList().observe(this, dailySummeryModels ->
                {
                    for(iCounters=0; iCounters<dailySummeryModels.size();iCounters++)
                    {
                        if(dailySummeryModels.get(iCounters).getOrderStatus().equalsIgnoreCase("Returned"))
                        {
                            rReturnSalesCounter = rReturnSalesCounter+dailySummeryModels.get(iCounters).getItemQty();
                            rTotalReturnAmountCredit = rTotalReturnAmountCredit + dailySummeryModels.get(iCounters).getCredit();
                            rTotalReturnAmountCash = rTotalReturnAmountCash + dailySummeryModels.get(iCounters).getCash();

                        }else{

                            rSaleCounter = rSaleCounter+1;

                            if(dailySummeryModels.get(iCounters).getCash()!=0.0f
                                    &&
                               dailySummeryModels.get(iCounters).getCustomerSalesMode().equalsIgnoreCase("Cash")
                                    &&
                               dailySummeryModels.get(iCounters).getOrderStatus().equalsIgnoreCase("Delivered")
                            ){
                                cashsalescounter = cashsalescounter+dailySummeryModels.get(iCounters).getItemQty();
                                rTotalCashSaleAmount = rTotalCashSaleAmount +dailySummeryModels.get(iCounters).getCash();

                            }else{

                                creditcounter = creditcounter+dailySummeryModels.get(iCounters).getItemQty();
                                rTotalCreditSaleAmount = rTotalCreditSaleAmount +dailySummeryModels.get(iCounters).getCredit();

                            }

                        }
                    }

                    tvTotalReturnOrderQty.setText(rReturnSalesCounter+"");
                    totalqtyTv.setText((creditcounter+cashsalescounter) + "");

                    totalCashTv.setText(rTotalCashSaleAmount + " " + new SharedPrefs(this).getCurrency());
                    totalCreditTv.setText(rTotalCreditSaleAmount + " " + new SharedPrefs(this).getCurrency());
                    tvTotalReturnOrderCash.setText((rTotalReturnAmountCredit+rTotalReturnAmountCash+""));
                    totalAmountTv.setText(""+(rTotalCashSaleAmount+rTotalCreditSaleAmount));



                    summeryList = dailySummeryModels;


                }
        );


        dailySaleSummaryViewModel.gethReciptList().observe(this, dailySummeryModels -> {

                receiptList = dailySummeryModels;


                //Total receipts Amount + Quantity
                ReceiptQuanitityandCashCalculations(receiptList);



                totalReceiptTv.setText(totalCashRecept + " "+new SharedPrefs(this).getCurrency());
                tvTotalRecieptsQty.setText(rTotalRecieptsQty+"");
                tvTotalRecivedCash.setText(rTotalCashInHand+"");
                netTotalCashTv.setText((float) (rTotalCashInHand-rTotalReturnAmountCash)+"");


              if(receiptList !=null)
            {
                summeryList.addAll(receiptList);
            }
//
            fillSummaryAdapter();
        });


        dailySaleSummaryViewModel.getSelctedItem().observe(this, new Observer<ArrayList<DeliveryItemModel>>() {
            @Override
            public void onChanged(ArrayList<DeliveryItemModel> deliveryItemModels) {

                if (deliveryItemModels != null) {
                    if (deliveryItemModels.size() > 0)
                        summarydetailDialog(deliveryItemModels);
                }

            }
        });
    }

    private void ReceiptQuanitityandCashCalculations(ArrayList<DailySummeryModel> receiptList) {
        for(int i=0; i<receiptList.size();i++){


            int amountinqueue   = Integer.parseInt(receiptList.get(i).getReceipt());
             rTotalRecieptsQty = receiptList.size();

            totalCashRecept = totalCashRecept+amountinqueue;

        }


        rTotalCashInHand = (rTotalCashSaleAmount+totalCashRecept);




    }

    private void listViewClickListner() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int o_id = summeryList.get(position).getOrderId();

                dailySaleSummaryViewModel.loadSqlSelectedItem(String.valueOf(o_id), "", true);

            }
        });
    }


    private void summarydetailDialog(ArrayList<DeliveryItemModel> list) {


        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_salesummary, null);
        final Dialog dialog = new Dialog(this);
        
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(view);
        RecyclerView recyclerView = dialog.findViewById(R.id.list_salesummary);

        TotalBillRecyclerViewAdapter adapter = new TotalBillRecyclerViewAdapter(this, list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        TextView tv_close = dialog.findViewById(R.id.close);

        dialog.show();

        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
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
                Intent intent = new Intent(DailySaleSummary.this, NewUserActivity.class);
                startActivity(intent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
                finish();
                break;
            }
            case R.id.actionSyncCompanyCustomerInfo: {
                if (ConnectionDetector.isConnectingToInternet(DailySaleSummary.this)) {
                    startService(new Intent(DailySaleSummary.this, CompanyInfoService.class));
                    Utility.Toast(DailySaleSummary.this, "Syncing Started...");
                } else {
                    Utility.Toast(DailySaleSummary.this, "Check network connection and try again");
                }
                break;
            }
            case R.id.actionAddSalesOrder: {
                Intent intent = new Intent(DailySaleSummary.this, TakeOrder.class);
                startActivity(intent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
                finish();
                break;
            }
            case R.id.actionSettings: {
                Intent intent = new Intent(DailySaleSummary.this, SettingActivity.class);
                startActivity(intent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
                break;
            }
            case R.id.actionAddSalesReturn: {

                Intent intent = new Intent(DailySaleSummary.this, ReturnOrderSearchActivity.class);
                startActivity(intent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
                break;
            }
            case R.id.enableLocation: {
                if (item.getTitle().toString().equals("Enable Location")) {
                    SpannableString spanString = new SpannableString("Disable Location");
                    spanString.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, spanString.length(), 0); //fix the color to white
                    item.setTitle(spanString);


                    item.setTitle("Disable Location");
                    GPSTracker gps = new GPSTracker(DailySaleSummary.this);
                    if (ConnectionDetector.isConnectingToInternet(DailySaleSummary.this)) {
                        if (gps.canGetLocation()) {
                            Utility.Toast(DailySaleSummary.this, "Location Enable Successfully");
                            startservice();
                        } else {
                            Utility.Toast(DailySaleSummary.this, "Enable your GPS first and try again..");
                            //gps.showSettingsAlert();
                        }
                    } else
                        Utility.Toast(DailySaleSummary.this, "Check network connection and try again");
                    break;
                } else {
                    SpannableString spanString = new SpannableString("Enable Location");
                    spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0); //fix the color to white
                    item.setTitle(spanString);

                    Utility.Toast(DailySaleSummary.this, "Location Disable Successfully");
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

        LinearLayout mainLayout = findViewById(R.id.mainLayout);
        View v = getLayoutInflater().inflate(R.layout.actionbar_view, mainLayout, false);
        TextView actionbar = v.findViewById(R.id.actionBarTextView);
        ImageButton btnMenu = v.findViewById(R.id.btnMenu);
        ImageView companyLogo = v.findViewById(R.id.companyLogo);
        try {
            String base = new SharedPrefs(DailySaleSummary.this).getCompanyLogo();
            Log.d("companyLogo", base + "");
            byte[] imageAsBytes = new byte[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO) {
                imageAsBytes = Base64.decode(base.getBytes(), Base64.DEFAULT);
            }
            companyLogo.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        } catch (Exception e) {
            Utility.logCatMsg("Error Company Logo" + e.getMessage());
        }
        btnMenu.setVisibility(View.GONE);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionbar.setText(new SharedPrefs(DailySaleSummary.this).getCompanyName());
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.appbluegrey)));
        actionBar.setCustomView(v);

    }

}
