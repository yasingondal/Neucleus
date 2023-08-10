package detrack.zaryansgroup.com.detrack.activity.activites;

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
import android.text.SpannableString;
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
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import detrack.zaryansgroup.com.detrack.R;
import detrack.zaryansgroup.com.detrack.activity.Adapter.EmptyBottleRecyclerViewAdapter;
import detrack.zaryansgroup.com.detrack.activity.Adapter.TotalBillRecyclerViewAdapter;
import detrack.zaryansgroup.com.detrack.activity.Map.GPSTracker;
import detrack.zaryansgroup.com.detrack.activity.Model.BankModels.CompanyWiseBanksModel;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.DeliveryItemModel;
import detrack.zaryansgroup.com.detrack.activity.Model.CustomerVisitedModel;
import detrack.zaryansgroup.com.detrack.activity.Model.DeliveryInfo;
import detrack.zaryansgroup.com.detrack.activity.Model.RegisterCustomerModel.RegisterdCustomerModel;
import detrack.zaryansgroup.com.detrack.activity.SQLlite.DBHelper;
import detrack.zaryansgroup.com.detrack.activity.SQLlite.ZEDTrackDB;
import detrack.zaryansgroup.com.detrack.activity.Service.CompanyInfoService;
import detrack.zaryansgroup.com.detrack.activity.Service.GPSService;
import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;
import detrack.zaryansgroup.com.detrack.activity.activites.SecondView.SelectProductActivitySecond;
import detrack.zaryansgroup.com.detrack.activity.utilites.ConnectionDetector;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import detrack.zaryansgroup.com.detrack.activity.viewmodels.TakeOrder_ViewModel;
import timber.log.Timber;

public class TotalBillActivity extends AppCompatActivity {

    RadioButton rbcash1, rbcredit1;

    String rCustomerSalesMode;

    ArrayList<DeliveryItemModel> selectedItemList;
    TextView tvBillCustomerName, tvBillCustomerAddress, tvBillDate, tvBillTime, tv_ctn, tv_gsttax, tv_discount, lbl_gstbottom;
    TextView discountPercentage, grossTV, discountTotalTV, NetTotalTV, dicountPerTV, orderNetTotal, tvItemGst, lbl_discount1, lbl_discount, tvTotalDisc;
    ImageButton btnMenu;
    ImageView companyLogo;
    ZEDTrackDB db;
    RegisterdCustomerModel RCusotmerModel;
    LinearLayout discountIB;
    MaterialButton saveLL;
    String lat = "0.0", lng = "0.0";


    RecyclerView totalBillRecyclerView;
    AlarmManager alarm;
    PendingIntent pintent;
    RadioGroup cashModeTB;
    TextView btnProductEdit;
    SharedPrefs sharedPrefs;
    DeliveryInfo deliveryInfo = null;

    //Assignment work by M.Y
    TakeOrder_ViewModel VmTakeOrder;
    private int spinnerPostion = 0;
    Spinner spBankName;
    ArrayList<CompanyWiseBanksModel> Bankslist;
    List<String> BankNameList;
    ArrayAdapter<String> adapter_BankNames;
    LinearLayout ChooseBankLayout;

    CheckBox checkboxurgent;
    String CheckBoxUrgentValue;

    String CashMode = "Credit";
    int BankID;

    String designation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_bill);


        VmTakeOrder = new ViewModelProvider(this,
                (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(TakeOrder_ViewModel.class);
        deliveryInfo = (DeliveryInfo) getIntent().getSerializableExtra("deliverInfo");


        // here is the previous code as sample if needed to rollback
//        VmTakeOrder = new ViewModelProvider(this,
//                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(TakeOrder_ViewModel.class);
//        deliveryInfo = (DeliveryInfo) getIntent().getSerializableExtra("deliverInfo");


        designation = new SharedPrefs(TotalBillActivity.this).getDesignation();


        initialization();
        findTotal();
        InitilizeAlaram();
        SpinnerBankDataObserver();
        FillSpinnerBank();
        SpinnerListenerBanks();
        HideDiscountForSalesPerson();
        SetValidationforSalesMode();
        controlPricesVisibilty();


    }

    private void controlPricesVisibilty() {

        if (designation.equalsIgnoreCase("Order Booker")) {
            grossTV.setVisibility(View.GONE);
            NetTotalTV.setVisibility(View.GONE);
            tvTotalDisc.setVisibility(View.GONE);
            orderNetTotal.setVisibility(View.GONE);
        }

    }

    private void UrgentOrderListener() {
        if (checkboxurgent.isChecked()) {
            CheckBoxUrgentValue = "True";


        } else {
            CheckBoxUrgentValue = "false";
        }
    }


    private void SetValidationforSalesMode() {

        rCustomerSalesMode = RCusotmerModel.getSalesMode();

        Timber.d("the sales mode is " + rCustomerSalesMode);

        ChooseBankLayout.setVisibility(View.GONE);

        if (rCustomerSalesMode.equalsIgnoreCase("Cash")) {
            rbcredit1.setVisibility(View.GONE);
            rbcash1.setVisibility(View.VISIBLE);
            rbcash1.setChecked(true);
            rCustomerSalesMode = "Cash";

        } else if (rCustomerSalesMode.equalsIgnoreCase("Credit")) {
            rbcash1.setVisibility(View.GONE);
            rbcredit1.setVisibility(View.VISIBLE);
            rbcredit1.setChecked(true);
            rCustomerSalesMode = "Credit";
        }
    }


    private void HideDiscountForSalesPerson() {


        if (!designation.equalsIgnoreCase("Admin")) {

            discountIB.setVisibility(View.GONE);
        }

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


    private void SpinnerBankDataObserver() {

        VmTakeOrder.getSqlBankslist().observe(this, new Observer<ArrayList<CompanyWiseBanksModel>>() {
            @Override
            public void onChanged(ArrayList<CompanyWiseBanksModel> data) {

                Bankslist = data;
                BankNameList = new ArrayList<String>();
                for (int i = 0; i < Bankslist.size(); i++) {
                    BankNameList.add(Bankslist.get(i).getBankName());

                }


                adapter_BankNames = new ArrayAdapter<String>(TotalBillActivity.this, android.R.layout.simple_spinner_item, BankNameList);
                adapter_BankNames.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spBankName.setAdapter(adapter_BankNames);
            }
        });
    }


    //Assignment work Yaseen duplication
    private void FillSpinnerBank() {
        VmTakeOrder.getSqlBanksList();
    }

    //Assignment work Yaseen duplication
    private void SpinnerListenerBanks() {
        spBankName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                //getting position
                BankID = Bankslist.get(position).getBankID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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

    private void initialization() {
        sharedPrefs = new SharedPrefs(this);
        selectedItemList = new ArrayList<>();

        selectedItemList = (ArrayList<DeliveryItemModel>) getIntent().getSerializableExtra("selectedItemList");

        RCusotmerModel = (RegisterdCustomerModel) getIntent().getSerializableExtra("RCCustomer");

        db = new ZEDTrackDB(this);


        spBankName = (Spinner) findViewById(R.id.spBankName1);
        ChooseBankLayout = (LinearLayout) findViewById(R.id.BankVisibility);
        checkboxurgent = (CheckBox) findViewById(R.id.checkboxurgent);

        //tvBillCustomerName = findViewById(R.id.tvBillCustomerName);

        tvBillCustomerName = findViewById(R.id.tvBillCustomerName);
        tvBillCustomerAddress = findViewById(R.id.tvBillCustomerAddress);
        tvBillDate = findViewById(R.id.tvBillDate);
        tvBillTime = findViewById(R.id.tvBillTime);
        discountIB = findViewById(R.id.discountIB);

        totalBillRecyclerView = findViewById(R.id.totalBillRecyclerView);
        discountTotalTV = findViewById(R.id.discountTotalTV);

        NetTotalTV = findViewById(R.id.NetTotalTV);
        dicountPerTV = findViewById(R.id.dicountPerOrderTV);

        orderNetTotal = findViewById(R.id.orderNet);
        discountPercentage = findViewById(R.id.discountTV);
        grossTV = findViewById(R.id.grossTv);
        saveLL = findViewById(R.id.saveLL);
        cashModeTB = findViewById(R.id.cashModeTB);
        btnProductEdit = findViewById(R.id.btnProductEdit);
        tv_ctn = findViewById(R.id.tv_ctn);
        tvItemGst = findViewById(R.id.tvItemGst);

        lbl_discount = findViewById(R.id.lbl_discount);
        lbl_discount1 = findViewById(R.id.lbl_discount1);

        tv_gsttax = findViewById(R.id.tv_gsttax);
        tv_discount = findViewById(R.id.tv_discount);
        lbl_gstbottom = findViewById(R.id.lbl_gstbottom);
        tvTotalDisc = findViewById(R.id.tvTotalDisc);

        if (sharedPrefs.getIsDiscountVisible().equalsIgnoreCase("false") &&
                sharedPrefs.getIsDiscountVisible().equalsIgnoreCase("false")) {

//            tv_gsttax.setVisibility(View.GONE);
            tv_discount.setVisibility(View.GONE);

            //todo change it again back to origional

             //  tvItemGst.setVisibility(View.GONE);
            //  lbl_gstbottom.setVisibility(View.GONE);

            lbl_discount.setVisibility(View.GONE);
            lbl_discount1.setVisibility(View.GONE);

            discountTotalTV.setVisibility(View.GONE);
            dicountPerTV.setVisibility(View.GONE);
        }


        TotalBillRecyclerViewAdapter adapter = new TotalBillRecyclerViewAdapter(this, selectedItemList);
        totalBillRecyclerView.setAdapter(adapter);
        totalBillRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        tvBillCustomerName.setText(RCusotmerModel.getName());

        tvBillCustomerAddress.setText(RCusotmerModel.getAddress());

        tvBillDate.setText("Date: " + Utility.getCurrentDate());
        tvBillTime.setText("Time: " + Utility.getTime());

        rbcash1 = findViewById(R.id.rbcash);
        rbcredit1 = findViewById(R.id.rbcredit);


        TextView tv_pcs = findViewById(R.id.tv_pcs);
        TextView tv_pck = findViewById(R.id.tv_pck);


        if (sharedPrefs.getView().equals("secondView")) {
            tv_pcs.setVisibility(View.GONE);
            tv_pck.setVisibility(View.GONE);
            tv_ctn.setText("Qty");
        }

//        AddFocItems();
        clickListener();
    }

//    private void AddFocItems() {
//        ArrayList<DeliveryItemModel> tempList = new ArrayList<>();
//        for (int i=0; i<selectedItemList.size(); i++){
//            if(selectedItemList.get(i).getFoc_qty() > 0){
//                int foc = selectedItemList.get(i).getFoc_qty();
//                int qty = selectedItemList.get(i).getCtn_qty();
//                new DeliveryItemModel();
//                DeliveryItemModel deliveryItemModelv = new DeliveryItemModel(selectedItemList.get(i));
//                deliveryItemModelv.setFoc_qty(0);
//                deliveryItemModelv.setItemGstPer(0f);
//                deliveryItemModelv.setItemGstValue(0f);
//                deliveryItemModelv.setWSCtnPrice(0f);
//                deliveryItemModelv.setDisplayPrice(0f);
//                deliveryItemModelv.setItem_discount(0f);
//                selectedItemList.get(i).setCtn_qty(qty - foc);
//                deliveryItemModelv.setCtn_qty(foc);
//                tempList.add(deliveryItemModelv);
//            }
//        }
//        selectedItemList.addAll(tempList);
//    }


    private void clickListener() {
        discountIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDiscountDiloge();
            }
        });

        saveLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UrgentOrderListener();
                placeOrderDilog();
            }
        });


        cashModeTB.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {

                    case R.id.rbcash:
                        CashMode = "Cash";
                        break;
                    case R.id.rbcredit:
                        CashMode = "Credit";
                        break;
                }

            }

        });


        btnProductEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void setUpActionBar(ActionBar actionBar) {

        ScrollView mainLayout = findViewById(R.id.mainLayout);
        View v = getLayoutInflater().inflate(R.layout.actionbar_view, mainLayout, false);
        btnMenu = v.findViewById(R.id.btnMenu);
        btnMenu.setVisibility(View.GONE);
        companyLogo = v.findViewById(R.id.companyLogo);
        try {
            String base = new SharedPrefs(TotalBillActivity.this).getCompanyLogo();
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


    private void findTotal() {
        float gross = 0, discount = 0, gst = 0, net = 0, rSpecialCustomerPrice = 0, discountedSum = 0.0f;

        for (int i = 0; i < selectedItemList.size(); i++) {

            discountedSum = discountedSum + selectedItemList.get(i).getDiscountPolicyValue();

            gst = gst + selectedItemList.get(i).getItemGstValue();
            discount = discount + selectedItemList.get(i).getItem_discount();
            if (selectedItemList.get(i).getTaxCode().equalsIgnoreCase("3rd") ||
                    selectedItemList.get(i).getTaxCode().equalsIgnoreCase("SR")) {

                rSpecialCustomerPrice = db.checkIfSpecialPriceExists(SelectProductActivitySecond.rCustomerId, selectedItemList.get(i).getItem_id());

                if (rSpecialCustomerPrice == 0.0f) {
                    gross = gross + (selectedItemList.get(i).getRetailPiecePrice() * selectedItemList.get(i).getCtn_qty());
                    net = net + (selectedItemList.get(i).getRetailPiecePrice() * selectedItemList.get(i).getCtn_qty());
                } else {
                    gross = gross + (rSpecialCustomerPrice * selectedItemList.get(i).getCtn_qty());
                    net = net + (rSpecialCustomerPrice * selectedItemList.get(i).getCtn_qty());
                }


            } else {


                rSpecialCustomerPrice = db.checkIfSpecialPriceExists(SelectProductActivitySecond.rCustomerId, selectedItemList.get(i).getItem_id());

                if (rSpecialCustomerPrice == 0.0f) {
                    gross = gross + (selectedItemList.get(i).getRetailPiecePrice() * selectedItemList.get(i).getCtn_qty());
                    net = net + (selectedItemList.get(i).getRetailPiecePrice() * selectedItemList.get(i).getCtn_qty());
                } else {
                    gross = gross + (rSpecialCustomerPrice * selectedItemList.get(i).getCtn_qty());
                    net = net + (rSpecialCustomerPrice * selectedItemList.get(i).getCtn_qty());
                }


            }

            selectedItemList.get(i).setDelivered_Quantity(selectedItemList.get(i).getCtn_qty());
        }


        grossTV.setText(Math.abs(gross) + "");
        discountTotalTV.setText(Math.abs(discount) + "");
        tvTotalDisc.setText(discountedSum + "");


        float disNo = Float.parseFloat(discountPercentage.getText().toString());
        float dis;
        if (sharedPrefs.getSalesDiscPolicy().equalsIgnoreCase("Before")) {
            dis = (disNo / 100) * (net + gst);
        } else {
            dis = (disNo / 100) * (net);
        }

        //Changing to positive
        dicountPerTV.setText(Math.round(disNo) + " % = " + String.format("%.2f", Math.abs(dis)));
        RCusotmerModel.setDiscount(Math.abs(dis));


        if (sharedPrefs.getWholeSalePriceTax().equalsIgnoreCase("Including Tax")) {
            Timber.d("Including Tax case is running");
            float TempSum = (float) Double.parseDouble(new DecimalFormat("##.##").format((net - gst) - discountedSum));

            NetTotalTV.setText(String.valueOf(Math.abs(TempSum)));


            // float TempNetTotal = (net - dis)-discountedSum;


            orderNetTotal.setText(String.valueOf(Math.abs(TempSum)));
            Float FixingDecimal = (float) Double.parseDouble(new DecimalFormat("##.##").format(gst));
            tvItemGst.setText(String.valueOf(Math.abs(FixingDecimal)));


        } else {

            Timber.d("Excluding Tax case is running");
            NetTotalTV.setText(String.valueOf((net + gst) - discountedSum));
            float tempnetTotal = (net + gst) - discountedSum;
            orderNetTotal.setText(String.valueOf(Math.abs(tempnetTotal)));
            tvItemGst.setText(String.valueOf(Math.abs(gst)));

        }


        //todo revert this if required as origional
//        if(sharedPrefs.getWholeSalePriceTax().equalsIgnoreCase("Including Tax")){
//            Timber.d("Including Tax case is running");
//            float TempSum = (float) Double.parseDouble(new DecimalFormat("##.##").format((net-gst)-discountedSum));
//            NetTotalTV.setText(String.valueOf(Math.abs(TempSum)));
//            float TempNetTotal = (net - dis)-discountedSum;
//            orderNetTotal.setText(String.valueOf(Math.abs(TempNetTotal)));
//
//            Float FixingDecimal = (float) Double.parseDouble(new DecimalFormat("##.##").format(gst));
//
//            tvItemGst.setText(String.valueOf(Math.abs(FixingDecimal)));
//        }else {
//            Timber.d("Excluding Tax case is running");
//            NetTotalTV.setText(String.valueOf((Math.abs(net-discountedSum))));
//            float tempnetTotal = (net + gst)-discountedSum;
//            orderNetTotal.setText(String.valueOf(Math.abs(tempnetTotal)));
//            tvItemGst.setText(String.valueOf(Math.abs(gst)));
//        }

    }

    private void selectDiscountDiloge() {
        View view = getLayoutInflater().inflate(R.layout.select_percentage, null);
        final NumberPicker np;
        np = view.findViewById(R.id.number_picker);
        np.setMinValue(0);
        np.setMaxValue(100);
        np.setWrapSelectorWheel(true);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TotalBillActivity.this);
        View view1 = getLayoutInflater().inflate(R.layout.dialog_custom_title, null);
        TextView tvCustomTitle = view1.findViewById(R.id.tvCustomTitle);
        tvCustomTitle.setText("Select percentage");
        alertDialogBuilder.setCustomTitle(view1);
        alertDialogBuilder
                .setCancelable(false)
                .setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        discountPercentage.setText(np.getValue() + "");
                        findTotal();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void placeOrderDilog() {
        int lastrowId = db.getLastRowId(DBHelper.TBL_ORDER_CONFIRM_CHILD) + 1;

        final GPSTracker gps = new GPSTracker(TotalBillActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.save_order_dailog_layout, null);
        final CheckBox cb = view.findViewById(R.id.pobCB);
        final TextView latlngTV = view.findViewById(R.id.latlngTV);
        final TextView seriesNo = view.findViewById(R.id.seriesNo);
        seriesNo.setText("AA0" + lastrowId);
        RadioGroup StatusRG = view.findViewById(R.id.OrderStatusRG);
        TextView orderStatusTv = view.findViewById(R.id.orderStatusTv);
        RadioButton bookingRadioButton = view.findViewById(R.id.BookingRB);
        final RadioButton deliveredRadioButton = view.findViewById(R.id.deliveredRB);
        if (WelcomeActivity.designation.equals("Order Booker")) {
            deliveredRadioButton.setVisibility(View.GONE);
            bookingRadioButton.setChecked(true);
            RCusotmerModel.setDeliveryStatus("Booking");
        } else if (WelcomeActivity.designation.equals("Delivery Boy")) {
            bookingRadioButton.setVisibility(View.GONE);
            RCusotmerModel.setDeliveryStatus("Delivered");
        } else {
            RCusotmerModel.setDeliveryStatus("Delivered");
        }


        StatusRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.BookingRB) {
                    RCusotmerModel.setDeliveryStatus("Booking");
                    Utility.logCatMsg("Delivery Status Booking");
                } else if (checkedId == R.id.deliveredRB) {
                    RCusotmerModel.setDeliveryStatus("Delivered");
                    Utility.logCatMsg("Delivery Status Delivered");
                }
            }
        });

        if (deliveryInfo != null) {

            if (deliveryInfo.getDelivery_status().equals("Inprogress")) {
                StatusRG.setVisibility(View.GONE);
                orderStatusTv.setText("Order Status: \t Inprogress");
                RCusotmerModel.setDeliveryStatus("Inprogress");
                RCusotmerModel.setOrderNumber("0");
            }
        }
        if (SelectCustomerActivity.addOrder.equals("false")) {
            StatusRG.setVisibility(View.GONE);

            //This code is running for Return type Total Bill
            orderStatusTv.setText("Order Status: \t Returned");
            RCusotmerModel.setDeliveryStatus("Returned");
            RCusotmerModel.setOrderNumber("0");
        } else {
            RCusotmerModel.setOrderNumber(System.currentTimeMillis() + "");
        }

        if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

//            double latitude = 33.6844;
//            double longitude = 73.0479;

            Timber.d("The vehcile id is " + sharedPrefs.getVehicleID());
            Timber.d("The vehcile name is " + sharedPrefs.getVehicleName());


            if (latitude > 0.0) {

                lat = latitude + "";
                lng = longitude + "";

                RCusotmerModel.setPobLat(lat);
                RCusotmerModel.setPobLng(lng);

                latlngTV.setText("Your Current Location is " + latitude + " , " + longitude);

                cb.setVisibility(View.GONE);
            } else
                latlngTV.setText("Please try again.");
        } else {
            gps.showSettingsAlert();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(TotalBillActivity.this);
        View view1 = getLayoutInflater().inflate(R.layout.dialog_custom_title, null);
        TextView tvCustomTitle = view1.findViewById(R.id.tvCustomTitle);
        tvCustomTitle.setText("Save Order");


        builder.setCustomTitle(view1)
                .setView(view)
                .setMessage("Saving " + tvBillDate.getText().toString() + " order?")
                .setCancelable(false)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Double.parseDouble(lat) > 0.0 && Double.parseDouble(lng) > 0.0) {
                            if (deliveredRadioButton.isChecked()) {
                                RCusotmerModel.setPobLat(String.valueOf(lat));
                                RCusotmerModel.setPobLng(String.valueOf(lng));

                            } else {

                                RCusotmerModel.setPobLat(String.valueOf(lat));
                                RCusotmerModel.setPobLng(String.valueOf(lng));
                            }

                            RCusotmerModel.setDate(Utility.getCurrentDate() + " " + Utility.getTime());

                            RCusotmerModel.setTotalbill(NetTotalTV.getText().toString());
                            RCusotmerModel.setPercentage_discount(discountPercentage.getText().toString());
                            RCusotmerModel.setGrossTotal(grossTV.getText().toString());
                            RCusotmerModel.setNetTotal(orderNetTotal.getText().toString());
                            RCusotmerModel.setSalesMode(rCustomerSalesMode);


                            if (seriesNo.getText().equals(""))
                                RCusotmerModel.setSerialNo("AA");
                            else
                                RCusotmerModel.setSerialNo(seriesNo.getText() + "");


                            if (SelectProductActivity.addOrder) {

                                Timber.d("first   case is runnung");

                                Utility.logCatMsg(RCusotmerModel.getDeliveryStatus() + "  \t" + ifEmptyBottleExists());
                                if (RCusotmerModel.getDeliveryStatus().equals("Delivered") && ifEmptyBottleExists()) {
                                    //todo open empty bottle dialog
                                    createEmptyBottleDialog();
                                } else {


                                    rInsertVisitRecordInLocalDb(RCusotmerModel);

                                    db.insertRunTimeOrderDetails(selectedItemList, RCusotmerModel, CheckBoxUrgentValue);
                                    Utility.HideKeyBoard(view, TotalBillActivity.this);
                                    Intent intent = new Intent(TotalBillActivity.this, WelcomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                            } else {

                                if (deliveryInfo != null)
                                    RCusotmerModel.setServerOrderId(deliveryInfo.getServer_Delivery_id());
                                if (db.deleteOrder(deliveryInfo.getDelivery_id() + "", "True")) {


                                    Timber.d("Running from else case");

                                    RCusotmerModel.setDistributorId(deliveryInfo.getDistributorId());
                                    RCusotmerModel.setSubDistributorId(deliveryInfo.getSubDistributorId());



//                                    if (!(RCusotmerModel.getDistributorId() > 0)) {
//
//                                        Timber.d("The not geater than zeero case running ");
//
//                                        int distId = db.GetDistributerId(deliveryInfo.getServer_Delivery_id());
//                                        RCusotmerModel.setDistributorId(distId);
//
//                                        int subDistId = db.GetSubDistributerId(deliveryInfo.getServer_Delivery_id());
//                                        RCusotmerModel.setSubDistributorId(subDistId);
//
//                                        Timber.d("The direct ids are "+distId+" and "+subDistId);
//                                        Timber.d("The indirect ids are "+deliveryInfo.getDistributorId()+" and "+deliveryInfo.getSubDistributorId());
//
//                                    }

                                    db.insertRunTimeOrderDetails(selectedItemList, RCusotmerModel, CheckBoxUrgentValue);
                                    Utility.HideKeyBoard(view, TotalBillActivity.this);
                                    Intent intent = new Intent(TotalBillActivity.this, WelcomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }


                        } else {
                            Toast.makeText(TotalBillActivity.this, "Current location not found...try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void rInsertVisitRecordInLocalDb(RegisterdCustomerModel rCusotmerModel) {


        CustomerVisitedModel model = new CustomerVisitedModel();

        int LastRowId = db.getLastRowId(DBHelper.TBL_VisitedDetails);
        String tempRouteName = db.rGrabRouteName(String.valueOf(RCusotmerModel.getRoute()));


        if (LastRowId == 0) {
            LastRowId = 1;
        } else {
            LastRowId = LastRowId + 1;
        }

        String rCollectedDateTime = rCusotmerModel.getDate();
        String[] separated = rCollectedDateTime.split(" ");

        String rTempDate = separated[0];
        String rTempTime = separated[1];
        String rTempAMPM = separated[2];

        model.setId(LastRowId);
        model.setLatitude(rCusotmerModel.getPobLat());
        model.setLongitude(rCusotmerModel.getPobLng());
        model.setRouteID(rCusotmerModel.getRoute());
        model.setRouteName(tempRouteName);
        model.setCustomerId(rCusotmerModel.getCustomer_id());
        model.setCustomerName(rCusotmerModel.getName());
        model.setIsSync(0);
        model.setStatusID(3);
        model.setVisitStatus("Booking");
        model.setSalesmanID(sharedPrefs.getEmployeeID());
        model.setCompanyID(Integer.parseInt(sharedPrefs.getCompanyID()));
        model.setCompanySiteID(Integer.parseInt(sharedPrefs.getCompanySiteID()));
        model.setVisitDate(rTempDate);
        model.setVisitTime(rTempTime + " " + rTempAMPM);

        //For Visit Entry in Local Database
        boolean check = db.rInsertCurrentCustomerVisit(model);
        if (check == true) {
            Timber.d("Success");

        } else {

            Timber.d("False");
        }

        Timber.d("Db visit insertion check is " + check);

    }


    private void createEmptyBottleDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.empty_bottle_dialog);
        RecyclerView emptyBottleRecyclerView = dialog.findViewById(R.id.emptyBottleRecyclerView);
        TextView ivClose = dialog.findViewById(R.id.btncancel);
        TextView ivSave = dialog.findViewById(R.id.btnok);

        EmptyBottleRecyclerViewAdapter adapter = new EmptyBottleRecyclerViewAdapter(this, selectedItemList);
        emptyBottleRecyclerView.setAdapter(adapter);
        emptyBottleRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        ivSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                for (int i = 0; i < selectedItemList.size(); i++) {
                    Log.d("emptyBottles", selectedItemList.get(i).getTitle() + "   " + selectedItemList.get(i).getEmptyBottles());
                }

                rInsertVisitRecordInLocalDb(RCusotmerModel);
                db.insertRunTimeOrderDetails(selectedItemList, RCusotmerModel, CheckBoxUrgentValue);
                Utility.HideKeyBoard(view, TotalBillActivity.this);
                Intent intent = new Intent(TotalBillActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private boolean ifEmptyBottleExists() {
        int flagValue = 0;
        for (int i = 0; i < selectedItemList.size(); i++) {
            if (selectedItemList.get(i).getEmptyFlag()) {
                flagValue++;
            }
        }

        if (flagValue > 0) {
            return true;
        } else {
            return false;
        }

    }

}
