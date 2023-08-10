package detrack.zaryansgroup.com.detrack.activity.activites;

import androidx.appcompat.app.ActionBar;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import detrack.zaryansgroup.com.detrack.activity.Adapter.CustomerDilogListAdapter;
import detrack.zaryansgroup.com.detrack.activity.Adapter.EmptyBottleRecyclerViewAdapter;
import detrack.zaryansgroup.com.detrack.activity.Adapter.StockListAdapter;
import detrack.zaryansgroup.com.detrack.activity.Map.GPSTracker;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyRouteModel.RouteModel;
import detrack.zaryansgroup.com.detrack.activity.Model.DeliveryInfo;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.DeliveryItemModel;
import detrack.zaryansgroup.com.detrack.activity.Model.RegisterCustomerModel.RegisterdCustomerModel;
import detrack.zaryansgroup.com.detrack.activity.Service.CompanyInfoService;
import detrack.zaryansgroup.com.detrack.activity.Service.GPSService;
import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;
import detrack.zaryansgroup.com.detrack.activity.viewmodels.TakeOrder_ViewModel;
import detrack.zaryansgroup.com.detrack.activity.utilites.ConnectionDetector;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import detrack.zaryansgroup.com.detrack.R;


public class TakeOrder extends AppCompatActivity {
    ListView order_item_listView, itemSerachLV;
    //    ZEDTrackDB db;
    private StockListAdapter listAdapter;
    ImageButton btnMenu;
    ListView customerLV;
    NumberPicker np;
    ArrayList<DeliveryItemModel> list = new ArrayList<>();
    ArrayList<RegisterdCustomerModel> Reg_Customer_list = new ArrayList<>();
    ArrayList<RegisterdCustomerModel> filteredList = new ArrayList<>();
    ArrayList<DeliveryItemModel> filteredItemList = new ArrayList<>();
    ArrayList<RouteModel> routelist = new ArrayList<>();
    TextView dateTv, totalBill, discountPercentage, grossTV, discountTotalTV, gstTotalTv, NetTotalTV, dicountPerTV, orderNetTotal;
    DatePickerDialog dpd;
    Button btnRegisterCustomer;
    RegisterdCustomerModel RCusotmerModel = new RegisterdCustomerModel();
    EditText customerSerachTV, itemSerachET, etPieceQty;
    String query, QtyType = "Piece", FocType = "Percentage";
    RadioButton percentageRB;
    Spinner routeCodeSP, routeNameSP;
    CustomerDilogListAdapter adapter;
    ImageButton filterImagBtn;
    LinearLayout calendrLL, saveLL, discountLL, currentuserLL;
    String lat = "0.0", lng = "0.0", CashMode = "Cash";
    HorizontalScrollView horizontalScrollView;
    ToggleButton cashModeTB;
    boolean IsFound = false;
    EditText etPiecePrice;
    LinearLayout totalAndDiscountLinear;
    AlarmManager alarm;
    PendingIntent pintent;
    BroadcastReceiver broadcastReceiver;
    DeliveryInfo deliveryInfo;
    String addOrder = "";
    String isNew;
    TakeOrder_ViewModel takeOrder_viewModel;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_order);


        addOrder = getIntent().getStringExtra("addOrder");
        if(addOrder == null) addOrder = "";
        setUpActionBar(getSupportActionBar());
        InitilizeAlaram();

        order_item_listView = findViewById(R.id.order_listview);
        dateTv = findViewById(R.id.dateTextView);
        totalBill = findViewById(R.id.totalbillTV);
        discountTotalTV = findViewById(R.id.discountTotalTV);
        gstTotalTv = findViewById(R.id.gstTotalTv);
        NetTotalTV = findViewById(R.id.NetTotalTV);
        dicountPerTV = findViewById(R.id.dicountPerOrderTV);
        orderNetTotal = findViewById(R.id.orderNet);
        discountPercentage = findViewById(R.id.discountTV);
        grossTV = findViewById(R.id.grossTv);
        btnRegisterCustomer = findViewById(R.id.btnRegisterCustomer);
        cashModeTB = findViewById(R.id.cashMode);
        currentuserLL = findViewById(R.id.currentuserIB);
        filterImagBtn = findViewById(R.id.filterImagBtn);
        calendrLL = findViewById(R.id.calander);
        saveLL = findViewById(R.id.saveLL);
        discountLL = findViewById(R.id.discountIB);
        horizontalScrollView = findViewById(R.id.scorllview);
//        db = new ZEDTrackDB(this);
        takeOrder_viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(TakeOrder_ViewModel.class);

        CustomeListObserver();
        CutomerByIdObserver();
        DeliveryOrderItemObserver();
        OderListObserver();
        RouteListObserver();
        takeOrder_viewModel.getSqlRouteList();
        takeOrder_viewModel.getSqlCustomerData(null);
//        Reg_Customer_list = db.getSQLiteRegisterCustomerInfo(null);

//        RCusotmerModel = Reg_Customer_list.get(0);
        btnRegisterCustomer.setText("Select Customer");
        totalAndDiscountLinear = findViewById(R.id.totalAndDiscountLinear);
        if (new SharedPrefs(this).getView().equals("secondView")) {
            totalAndDiscountLinear.setVisibility(View.GONE);
        }

        if (addOrder.equals("update")) {
            deliveryInfo = (DeliveryInfo) getIntent().getSerializableExtra("deliverInfo");
            isNew = getIntent().getStringExtra("isNew");
            takeOrder_viewModel.loadSqlSelectedCustomer(deliveryInfo.getCustomer_id());
//            RCusotmerModel = db.getCustomerById(deliveryInfo.getCustomer_id());

            btnRegisterCustomer.setEnabled(false);
        }



        order_item_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long id) {

                SelectQuantityDiloge(position, adapterView);

            }
        });

//        order_item_listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                // SetPriceDialog(position);
//                return true;
//            }
//        });
        final Calendar c = Calendar.getInstance();
        dateTv.setText(Utility.getCurrentDate() + " " + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":00");

        dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, final int year,
                                          final int monthOfYear, final int dayOfMonth) {
                        new TimePickerDialog(TakeOrder.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                c.set(Calendar.MINUTE, minute);
                                Utility.logCatMsg("The choosen one " + c.getTime());
                                dateTv.setText(Utility.getCurrentDate() + " " + hourOfDay + ":" + minute + ":00");
                            }
                        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false).show();
                    }
                }, c.get(Calendar.YEAR), (c.get(Calendar.MONTH)), c.get(Calendar.DAY_OF_MONTH));
        calendrLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpd.show();
            }
        });

        saveLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IsFound = false;
                for (DeliveryItemModel model : list) {
                    if (model.getPcs_qty() > 0 || model.getPac_qty() > 0 || model.getCtn_qty() > 0) {
                        IsFound = true;
                        break;
                    }
                }
                //if (Float.parseFloat(totalBill.getText().toString()) > 0)
                if (IsFound) {
                    if (btnRegisterCustomer.getText().toString().equals("Select Customer")) {

                        Toast.makeText(TakeOrder.this, "Select Customer first", Toast.LENGTH_SHORT).show();
                    } else {
                        SaveOrder();
                    }
                } else {
                    Utility.Toast(TakeOrder.this, "No Item Selected!");
                }


            }
        });
        cashModeTB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    CashMode = "Credit";
                else
                    CashMode = "Cash";
            }
        });
        btnRegisterCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Customer_Dilog();
            }
        });
        discountLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectDiscountDiloge();
            }
        });
    }

    private void SaveOrder() {

        if (lat.equals("0.0")) {
            RCusotmerModel.setPobLng("0.0");
            RCusotmerModel.setPobLat("0.0");
        }
        RCusotmerModel.setDate(dateTv.getText().toString());
        RCusotmerModel.setTotalbill(NetTotalTV.getText().toString());
        RCusotmerModel.setPercentage_discount(discountPercentage.getText().toString());
        RCusotmerModel.setGrossTotal(grossTV.getText().toString());
        RCusotmerModel.setNetTotal(orderNetTotal.getText().toString());
        RCusotmerModel.setCashMode(CashMode);
        RCusotmerModel.setOrderNumber(System.currentTimeMillis() + "");
        RCusotmerModel.setSerialNo("A001");
        RCusotmerModel.setDeliveryStatus("Returned");
//        if (seriesNo.getText().equals(""))
//            RCusotmerModel.setSerialNo("AA");
//        else
//            RCusotmerModel.setSerialNo(seriesNo.getText() + "");

        if (addOrder.equals("true")) {

            if (RCusotmerModel.getDeliveryStatus().equals("Delivered") && ifEmptyBottleExists()) {
                //todo open empty bottle dialog
                createEmptyBottleDialog();
            } else {
//                                db.insertRunTimeOrderDetails(filteredItemList, RCusotmerModel);
                takeOrder_viewModel.insertRunTimeOrderDetails(filteredItemList,RCusotmerModel);
//                Utility.HideKeyBoard(view, TakeOrder.this);
                Intent intent = new Intent(TakeOrder.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            Toast.makeText(TakeOrder.this, "Order Updated", Toast.LENGTH_SHORT).show();
//                            if (db.deleteOrder(deliveryInfo.getDelivery_id() + "", "True"))
            if (takeOrder_viewModel.deleteOrder(deliveryInfo.getDelivery_id() + "", "True")){
//                                db.insertRunTimeOrderDetails(filteredItemList, RCusotmerModel);
                takeOrder_viewModel.insertRunTimeOrderDetails(filteredItemList,RCusotmerModel);
                Intent intent = new Intent(TakeOrder.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private void RouteListObserver() {
        takeOrder_viewModel.getSqlroutelist().observe(this,
                new Observer<ArrayList<RouteModel>>() {
                    @Override
                    public void onChanged(ArrayList<RouteModel> routeModels) {
                        routelist = routeModels;
                    }
                });

    }

    private void OderListObserver() {
        takeOrder_viewModel.getDeliveryItems().observe(this,
                new Observer<ArrayList<DeliveryItemModel>>() {
                    @Override
                    public void onChanged(ArrayList<DeliveryItemModel> deliveryItemModels) {
                        list = deliveryItemModels;

                        if (addOrder.equals("update")) {
                            takeOrder_viewModel.loadSqlSelectedItem(String.valueOf(deliveryInfo.getDelivery_id()), isNew);
                        }
                        Fill_listtView_From_SqliteDB();
                    }
                });
    }

    private void DeliveryOrderItemObserver() {
        takeOrder_viewModel.getSelctedItem().observe(this,
                new Observer<ArrayList<DeliveryItemModel>>() {
                    @Override
                    public void onChanged(ArrayList<DeliveryItemModel> deliveryItemModels) {
                        filteredItemList = deliveryItemModels;
                        Log.d("items","itm="+filteredItemList.size());
                        Fill_listtView_From_SqliteDB();
                    }
                });
    }

    private void CutomerByIdObserver() {
        takeOrder_viewModel.getSelectedCustomer().observe(this,
                new Observer<RegisterdCustomerModel>() {
                    @Override
                    public void onChanged(RegisterdCustomerModel registerdCustomerModel) {
                        RCusotmerModel = registerdCustomerModel;
                        Log.d("takeorderdata","="+RCusotmerModel.getName());
                        takeOrder_viewModel.loadSqlDeliveryItems();

                    }
                });
    }

    private void CustomeListObserver() {
        takeOrder_viewModel.getSqlCustomerData().observe(this,
                new Observer<ArrayList<RegisterdCustomerModel>>() {
                    @Override
                    public void onChanged(ArrayList<RegisterdCustomerModel> registerdCustomerModels) {
                        Reg_Customer_list = registerdCustomerModels;
                        filteredList = registerdCustomerModels;
                        RCusotmerModel = Reg_Customer_list.get(0);
                        btnRegisterCustomer.setText(RCusotmerModel.getName());
                        if(customerLV != null)
                        {
                            adapter = new CustomerDilogListAdapter(TakeOrder.this, Reg_Customer_list);
                            customerLV.setAdapter(adapter);
                            customerLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                                    RegisterdCustomerModel model = (RegisterdCustomerModel) parent.getItemAtPosition(position);
                                    RCusotmerModel = model;
                                    btnRegisterCustomer.setText(model.getName());
                                    takeOrder_viewModel.loadSqlSelectedCustomer(RCusotmerModel.getCustomer_id());
                                    Utility.HideKeyBoard(view, TakeOrder.this);
                                    alertDialog.dismiss();
                                }
                            });
                        }




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
                Intent intent = new Intent(this, NewUserActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
                break;
            }
            case R.id.actionSyncCompanyCustomerInfo: {
                if (ConnectionDetector.isConnectingToInternet(this)) {
                    startService(new Intent(this, CompanyInfoService.class));
                    Utility.Toast(this, "Getting customer info...");
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
                tvAppVersion.setText("App Version: " + WelcomeActivity.versionName);
                dialog.show();
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

    @Override
    protected void onResume() {
        super.onResume();

        if (broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

//                    Fill_listtView_From_SqliteDB();
                }
            };

            registerReceiver(broadcastReceiver, new IntentFilter("dataUpdated"));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {

            unregisterReceiver(broadcastReceiver);
        }
    }

    private void Fill_listtView_From_SqliteDB() {



//        list = db.getCompanyItem();
        if (addOrder.equals("update")) {
//            filteredItemList = db.getSQLiteOrderDeliveryItems(String.valueOf(deliveryInfo.getDelivery_id()), isNew);
            Log.d("filteredItemList", String.valueOf(filteredItemList.size()));
            for (int i = 0; i < list.size(); i++) {
                for (int j = 0; j < filteredItemList.size(); j++) {
                    if (list.get(i).getName().equals(filteredItemList.get(j).getName())) {
                        Log.d("itemFound", filteredItemList.get(j).getName());
                        filteredItemList.get(j).setIsSelected(true);
                        list.set(i, filteredItemList.get(j));
                    }
                }
            }
            findTotal();
        }

//        for (int i = 0; i < summeryList.size(); i++) {
//            summeryList.get(i).setCtn_qty(0);
//            summeryList.get(i).setPac_qty(0);
//            summeryList.get(i).setPcs_qty(0);
//        }
        Utility.logCatMsg("Feed items from DB Size " + list.size());

        if (list.size() > 0) {
            listAdapter = new StockListAdapter(TakeOrder.this, list);
            order_item_listView.setAdapter(listAdapter);
        } else {
            Utility.Toast(TakeOrder.this, "No Item Found");
        }
    }


    private void setUpActionBar(ActionBar actionBar) {
        LinearLayout mainLayout = findViewById(R.id.mainLayout);
        View v = getLayoutInflater().inflate(R.layout.actionbar_view, mainLayout, false);
        EditText serach = v.findViewById(R.id.actionBarSerachET);
        //serach.setVisibility(View.VISIBLE);
        btnMenu = v.findViewById(R.id.btnMenu);
        btnMenu.setVisibility(View.GONE);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.appbluegrey)));
        actionBar.setCustomView(v);
        if (addOrder.equals("true")) {
            ((TextView) v.findViewById(R.id.actionBarTextView)).setText("Sales Order");
        } else {
            ((TextView) v.findViewById(R.id.actionBarTextView)).setText("Return Order");
        }


    }


    private void SelectQuantityDiloge(final int position, final AdapterView adapterView) {
        DeliveryItemModel model = (DeliveryItemModel) adapterView.getItemAtPosition(position);
        final int list_postion = listAdapter.getListPosition(model);
        final View view = getLayoutInflater().inflate(R.layout.select_qty_layout2, null);
        np = view.findViewById(R.id.number_picker);

        etPieceQty = view.findViewById(R.id.etPieceQty);
        etPiecePrice = view.findViewById(R.id.etNewPiecePrice);
        etPiecePrice.setText(String.valueOf((int) list.get(position).getWSCtnPrice()));

        etPieceQty.requestFocus();
        try {
            if (list.get(list_postion).getPcs_qty() > 0) {
                etPieceQty.setText(list.get(list_postion).getPcs_qty() + "");
            }
        } catch (Exception e) {
            Utility.logCatMsg("Error in " + e.getMessage());
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TakeOrder.this);
        View view1 = getLayoutInflater().inflate(R.layout.dialog_custom_title, null);
        TextView tvCustomTitle = view1.findViewById(R.id.tvCustomTitle);
        tvCustomTitle.setText("Select Qty");

        alertDialogBuilder.setCustomTitle(view1);
        alertDialogBuilder
                .setCancelable(false)
                .setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Utility.logCatMsg("third condition");
                        list.get(list_postion).setIsSelected(true);
                        list.get(list_postion).setQtyType(QtyType);
                        list.get(list_postion).setFocType(FocType);
                        float picesPrice = Float.parseFloat(etPiecePrice.getText().toString());
                        list.get(position).setWSCtnPrice(picesPrice);
                        setQtyValuesOfList(list_postion, 1);
                        //  summeryList.get(list_postion).setNetTotalRetailPrice((summeryList.get(list_postion).getNetTotalRetailPrice() - summeryList.get(list_postion).getItem_discount()));

                        DeliveryItemModel selectedItem = list.get(list_postion);
                        Log.d("itemDetail", selectedItem.getName() + "\n" + selectedItem.getCode() + "\n" + selectedItem.getPcs_qty());
                        try {
                            if (filteredItemList.size() > 0) {
                                if (filteredItemList.get(position).getTitle().equals(selectedItem.getTitle())) {
                                    filteredItemList.remove(position);
                                }
                            }
                        } catch (IndexOutOfBoundsException ex) {
                            ex.printStackTrace();
                        }
                        filteredItemList.add(selectedItem);
                        listAdapter.notifyDataSetChanged();
                        Utility.HideKeyBoard(view, TakeOrder.this);
                        dialog.dismiss();
                        findTotal();

                        HideKeyBord();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                        HideKeyBord();
                    }
                });
        alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        alertDialog.show();

    }



    private void SelectDiscountDiloge() {
        View view = getLayoutInflater().inflate(R.layout.select_percentage, null);
        np = view.findViewById(R.id.number_picker);
        np.setMinValue(0);
        np.setMaxValue(100);
        np.setWrapSelectorWheel(true);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TakeOrder.this);
        alertDialogBuilder.setTitle("Select percentage");
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

    private void Alert_Dilog() {
        GPSTracker gps = new GPSTracker(TakeOrder.this);
        final View view = getLayoutInflater().inflate(R.layout.save_order_dailog_layout, null);
        final CheckBox cb = view.findViewById(R.id.pobCB);
        final TextView latlngTV = view.findViewById(R.id.latlngTV);
        TextView orderStatusTv = view.findViewById(R.id.orderStatusTv);
        final TextView seriesNo = view.findViewById(R.id.seriesNo);
        RadioGroup StatusRG = view.findViewById(R.id.OrderStatusRG);
        RadioButton bookingRadioButton = view.findViewById(R.id.BookingRB);
        RadioButton deliveredRadioButton = view.findViewById(R.id.deliveredRB);
        if (WelcomeActivity.designation.equals("Order Booker")) {
            deliveredRadioButton.setVisibility(View.GONE);
            bookingRadioButton.setChecked(true);
        } else if (WelcomeActivity.designation.equals("Delivery Boy")) {
            bookingRadioButton.setVisibility(View.GONE);
        }

        RCusotmerModel.setDeliveryStatus("Delivered");
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
//
//        if (addOrder.equals("false")) {
//            StatusRG.setVisibility(View.GONE);
//            orderStatusTv.setText("Order Status: \t Returned");
//            RCusotmerModel.setDeliveryStatus("Returned");
//        }
        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            if (latitude > 0.0) {
                lat = latitude + "";
                lng = longitude + "";
                latlngTV.setText("Your Current Location is " + latitude + " , " + longitude);
                RCusotmerModel.setPobLat(lat);
                RCusotmerModel.setPobLng(lng);
                cb.setVisibility(View.GONE);
            } else
                latlngTV.setText("Please try again.");
        } else {
            gps.showSettingsAlert();
        }

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                RCusotmerModel.setPobLat("");
                RCusotmerModel.setPobLng("");
                if (cb.isChecked()) {
                    GPSTracker gps = new GPSTracker(TakeOrder.this);
                    if (gps.canGetLocation()) {
                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();
                        if (latitude > 0.0) {
                            lat = latitude + "";
                            lng = longitude + "";
                            RCusotmerModel.setPobLat(lat + "");
                            RCusotmerModel.setPobLng(lng + "");
                            latlngTV.setText("Your Current Location is " + latitude + " , " + longitude);
                            cb.setVisibility(View.GONE);
                        } else
                            latlngTV.setText("Please try again.");
                    } else
                        gps.showSettingsAlert();
                    cb.setChecked(false);
                }
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(TakeOrder.this);
        builder.setTitle("Save order")
                .setView(view)
                .setMessage("Saving " + dateTv.getText().toString() + " date order ?")
                .setCancelable(false)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (lat.equals("0.0")) {
                            RCusotmerModel.setPobLng("0.0");
                            RCusotmerModel.setPobLat("0.0");
                        }
                        RCusotmerModel.setDate(dateTv.getText().toString());
                        RCusotmerModel.setTotalbill(NetTotalTV.getText().toString());
                        RCusotmerModel.setPercentage_discount(discountPercentage.getText().toString());
                        RCusotmerModel.setGrossTotal(grossTV.getText().toString());
                        RCusotmerModel.setNetTotal(orderNetTotal.getText().toString());
                        RCusotmerModel.setCashMode(CashMode);
                        RCusotmerModel.setOrderNumber(System.currentTimeMillis() + "");
                        if (seriesNo.getText().equals(""))
                            RCusotmerModel.setSerialNo("AA");
                        else
                            RCusotmerModel.setSerialNo(seriesNo.getText() + "");

                        if (addOrder.equals("true")) {

                            if (RCusotmerModel.getDeliveryStatus().equals("Delivered") && ifEmptyBottleExists()) {
                                //todo open empty bottle dialog
                                createEmptyBottleDialog();
                            } else {
//                                db.insertRunTimeOrderDetails(filteredItemList, RCusotmerModel);
                                takeOrder_viewModel.insertRunTimeOrderDetails(filteredItemList,RCusotmerModel);
                                Utility.HideKeyBoard(view, TakeOrder.this);
                                Intent intent = new Intent(TakeOrder.this, WelcomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Toast.makeText(TakeOrder.this, "Order Updated", Toast.LENGTH_SHORT).show();
//                            if (db.deleteOrder(deliveryInfo.getDelivery_id() + "", "True"))
                            if (takeOrder_viewModel.deleteOrder(deliveryInfo.getDelivery_id() + "", "True")){
//                                db.insertRunTimeOrderDetails(filteredItemList, RCusotmerModel);
                                takeOrder_viewModel.insertRunTimeOrderDetails(filteredItemList,RCusotmerModel);
                                Utility.HideKeyBoard(view, TakeOrder.this);
                                Intent intent = new Intent(TakeOrder.this, WelcomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
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


    private void createEmptyBottleDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.empty_bottle_dialog);
        RecyclerView emptyBottleRecyclerView = dialog.findViewById(R.id.emptyBottleRecyclerView);
        TextView ivClose = dialog.findViewById(R.id.btn_cancel);
        TextView ivSave = dialog.findViewById(R.id.btnok);

        EmptyBottleRecyclerViewAdapter adapter = new EmptyBottleRecyclerViewAdapter(this, filteredItemList);
        emptyBottleRecyclerView.setAdapter(adapter);
        emptyBottleRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        ivSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                for (int i = 0; i < filteredItemList.size(); i++) {
                    Log.d("emptyBottles", filteredItemList.get(i).getTitle() + "   " + filteredItemList.get(i).getEmptyBottles());
                }

//                db.insertRunTimeOrderDetails(filteredItemList, RCusotmerModel);
                takeOrder_viewModel.insertRunTimeOrderDetails(filteredItemList,RCusotmerModel);
                Utility.HideKeyBoard(view, TakeOrder.this);
                Intent intent = new Intent(TakeOrder.this, WelcomeActivity.class);
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


    private void Customer_Dilog() {


        View view = getLayoutInflater().inflate(R.layout.customer_diloag_layout, null);
        routeCodeSP = view.findViewById(R.id.route_code);
        routeNameSP = view.findViewById(R.id.route_name);
        fillSpinner();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TakeOrder.this);
        alertDialogBuilder.setTitle("Select Customer");
        alertDialogBuilder
                .setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        alertDialog = alertDialogBuilder.create();
        customerSerachTV = view.findViewById(R.id.inputSearch);
        customerLV = view.findViewById(R.id.customer_list_view);
        adapter = new CustomerDilogListAdapter(TakeOrder.this, Reg_Customer_list);
        customerLV.setAdapter(adapter);
        customerLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                RegisterdCustomerModel model = (RegisterdCustomerModel) parent.getItemAtPosition(position);
                RCusotmerModel = model;
                btnRegisterCustomer.setText(model.getName());
                takeOrder_viewModel.loadSqlSelectedCustomer(RCusotmerModel.getCustomer_id());
                Utility.HideKeyBoard(view, TakeOrder.this);
                alertDialog.dismiss();
            }
        });

//        openSearchBar();
        alertDialog.show();
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
            customerLV.setAdapter(new CustomerDilogListAdapter(TakeOrder.this, filteredList));
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

    private ArrayList<DeliveryItemModel> performItemSearch(ArrayList<DeliveryItemModel> modal, String query) {
        String[] queryByWords = query.toLowerCase().split("\\s+");
        ArrayList<DeliveryItemModel> filter = new ArrayList<>();
        for (int i = 0; i < modal.size(); i++) {
            DeliveryItemModel data = modal.get(i);
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
    public void onBackPressed() {
        if (Float.parseFloat(totalBill.getText().toString()) > 0)
            LeaveScreenDialog();
        else {
            finish();
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
        }
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
                if(position > 0)
                {
                    Reg_Customer_list.clear();
                    takeOrder_viewModel.getSqlCustomerData(routelist.get(position).getRoute_id() + "");
//                Reg_Customer_list = db.getSQLiteRegisterCustomerInfo(routelist.get(position).getRoute_id() + "");
//                    adapter = new CustomerDilogListAdapter(TakeOrder.this, Reg_Customer_list);
//                    customerLV.setAdapter(adapter);
                }

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
                    takeOrder_viewModel.getSqlCustomerData(null);
                else
                    takeOrder_viewModel.getSqlCustomerData(routelist.get(position - 1).getRoute_id() + "");
//                    Reg_Customer_list = db.getSQLiteRegisterCustomerInfo(routelist.get(position - 1).getRoute_id() + "");
//                adapter = new CustomerDilogListAdapter(TakeOrder.this, Reg_Customer_list);
//                customerLV.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setQtyValuesOfList(int list_postion, int flage) {
        list.get(list_postion).setCtn_qty(0);
        list.get(list_postion).setPac_qty(0);
        list.get(list_postion).setPcs_qty(0);
        list.get(list_postion).setTotalwholeSalePrice(0);
        list.get(list_postion).setTotalCostPrice(0);
        list.get(list_postion).setTotalRetailPrice(0);
        list.get(list_postion).setDelivered_Quantity(0);
        list.get(list_postion).setItemTotalDeliverQtyInPieces(0);

        if (flage == 1) {
            int ctnQty = 0, packQty = 0, pieceQty = 0;
            if (!etPieceQty.getText().toString().equals(""))
                pieceQty = Integer.parseInt(etPieceQty.getText().toString());
            int ctnSize = list.get(list_postion).getCtnSize();
            int packSize = list.get(list_postion).getPackSize();
            int totalQtyInPieces = (ctnQty * ctnSize * packSize) + (packQty * packSize) + pieceQty;
            Utility.logCatMsg("Total Quantity in Pieces " + totalQtyInPieces);
            list.get(list_postion).setItemTotalDeliverQtyInPieces(totalQtyInPieces);
            list.get(list_postion).setItemTotalActualQtyInPieces(totalQtyInPieces);
            list.get(list_postion).setCtn_qty(ctnQty);
            list.get(list_postion).setPac_qty(packQty);
            list.get(list_postion).setPcs_qty(pieceQty);

            list.get(list_postion).setDelivered_Quantity(packQty + packQty + pieceQty);
            float ctnWSPrice = list.get(list_postion).getRetailPiecePrice();
            float totalCtnWSPrice = ctnQty * ctnWSPrice;
            float ctnCostPrice = list.get(list_postion).getCostCtnPrice();
            float totalctnCostPrice = ctnQty * ctnCostPrice;
            float ctnRetailPrice = list.get(list_postion).getRetailCtnPrice();
            float totalctnRetailPrice = ctnQty * ctnRetailPrice;
            float packWSPrice = list.get(list_postion).getWSPackPrice();
            float totalPackWSPrice = packQty * packWSPrice;
            float packCostPrice = list.get(list_postion).getCostPackPrice();
            float totalPackCostPrice = packQty * packCostPrice;
            float packRetailPrice = list.get(list_postion).getRetailPackPrice();
            float totalPackRetailPrice = packQty * packRetailPrice;
            float PiecesWSPrice = list.get(list_postion).getWSPiecePrice();
            float totalPiecesWSPrice = pieceQty * PiecesWSPrice;
            float PiecesCostPrice = list.get(list_postion).getCostPiecePrice();
            float totalPiecesCostPrice = pieceQty * PiecesCostPrice;
            float PiecesRetailPrice = list.get(list_postion).getWSCtnPrice();
            float totalPiecesRetailPrice = pieceQty * PiecesRetailPrice;
            float sumOfWholeSalePrice = ctnWSPrice + packWSPrice + PiecesWSPrice;
            float sumOfTotalWholeSalePrice = totalCtnWSPrice + totalPackWSPrice + totalPiecesWSPrice;
            float sumOfCostPrice = ctnCostPrice + packCostPrice + PiecesCostPrice;
            float sumOfTotalCostPrice = totalctnCostPrice + totalPackCostPrice + totalPiecesCostPrice;
            float sumOfRetailPrice = ctnRetailPrice + packRetailPrice + PiecesRetailPrice;
            float sumOfTotalRetailPrice = totalPiecesRetailPrice;
            list.get(list_postion).setTotalwholeSalePrice(sumOfTotalWholeSalePrice);  // this is handel on insertion time
            list.get(list_postion).setTotalCostPrice(sumOfTotalCostPrice);
            list.get(list_postion).setTotalRetailPrice(sumOfTotalRetailPrice);
            list.get(list_postion).setDisplayPrice(0);
            list.get(list_postion).setItemGstPer(Utility.GST_PERCENT);
            float gst = Utility.GST_PERCENT;
            float gstPercent = 0;
            if (gst != 0) {
                gstPercent = gst / 100.0f;
            }
            float gstValue = gstPercent * sumOfTotalRetailPrice;
            list.get(list_postion).setItemGstValue(gstValue);
            list.get(list_postion).setNetTotalRetailPrice((list.get(list_postion).getTotalRetailPrice()));
            Utility.logCatMsg("Gst Value " + list.get(list_postion).getItemGstValue());
            Utility.logCatMsg("Net Total Bill  " + list.get(list_postion).getNetTotalRetailPrice());
        }

    }


    private void HideKeyBord() {
        try {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            Utility.logCatMsg("Hide Key board Error : " + e.getMessage());
        }
    }

    public void findTotal() {
        float gross = 0, discount = 0, total = 0, gst = 0, net = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isSelected()) {
                gross = gross + (list.get(i).getPcs_qty() * list.get(i).getWSCtnPrice());
                discount = discount + list.get(i).getItem_discount();
                total = total + (list.get(i).getTotalRetailPrice() - list.get(i).getItem_discount());
                gst = gst + list.get(i).getItemGstValue();
            }
        }
        net = gross - discount;
        grossTV.setText(gross + "");
        totalBill.setText(total + "");
        discountTotalTV.setText(discount + "");
        gstTotalTv.setText(gst + "");
        NetTotalTV.setText(net + "");
        float disNo = Float.parseFloat(discountPercentage.getText().toString());
        float dis = (disNo / 100) * net;
        dicountPerTV.setText(Math.round(disNo) + " % = " + String.format("%.2f", dis));
        RCusotmerModel.setDiscount(dis);//todo error of null
        float result = net - dis;
        orderNetTotal.setText(result + "");

    }

    private void LeaveScreenDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TakeOrder.this);
        builder.setTitle("Exit!")
                .setMessage("Do you want to save this order?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Alert_Dilog();
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        overridePendingTransition(R.anim.left_in, R.anim.right_out);
                    }
                }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private boolean ifEmptyBottleExists() {
        int flagValue = 0;
        for (int i = 0; i < filteredItemList.size(); i++) {
            if (filteredItemList.get(i).getEmptyFlag()) {
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
