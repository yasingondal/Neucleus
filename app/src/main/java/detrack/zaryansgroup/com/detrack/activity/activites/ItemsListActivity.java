package detrack.zaryansgroup.com.detrack.activity.activites;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import detrack.zaryansgroup.com.detrack.activity.Adapter.QTYListAdapter;
import detrack.zaryansgroup.com.detrack.activity.Map.GPSTracker;
import detrack.zaryansgroup.com.detrack.activity.Model.DeliveryInfo;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.DeliveryItemModel;
import detrack.zaryansgroup.com.detrack.activity.SQLlite.ZEDTrackDB;
import detrack.zaryansgroup.com.detrack.activity.Service.CompanyInfoService;
import detrack.zaryansgroup.com.detrack.activity.Service.GPSService;
import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;
import detrack.zaryansgroup.com.detrack.activity.SyncDataServerToSqliteDB.SyncDataFromServer;
import detrack.zaryansgroup.com.detrack.activity.utilites.ConnectionDetector;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import detrack.zaryansgroup.com.detrack.R;

public class ItemsListActivity extends AppCompatActivity {
    private QTYListAdapter QtylistAdapter;
    private ArrayList<DeliveryItemModel> list;
    ListView itemListView, menuList;

    ZEDTrackDB db;
    String Delivery_id, IsNew;
    CharSequence[] option_list = {"Change Item Qty", "Cancel"};
    CharSequence[] reject_reason_list = {"Wrong Type", "Wrong Size", "Goods Damaged", "Other", "Cancel"};
    SharedPrefs prefs;
    ImageButton btnMenu;
    DeliveryInfo deliveryInfo;
    String FocType = "Percentage";
    RadioButton percentageRB;
    EditText focET, ctnQtyET, packQtyET, picesQtyET, etPiecePrice, etPackPrice, etCtnPrice;
    TextView grossTv, discountTotalTV, totalbillTV, gstTotalTv, NetTotalTV, dicountPerTV, orderNet;
    AlarmManager alarm;
    PendingIntent pintent;

   public static String orderStatus = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_list);
        if (getActionBar() != null) {
            setUpActionBar(getSupportActionBar());
        }
        InitilizeAlaram();
        itemListView = findViewById(R.id.item_listview);
        deliveryInfo = new DeliveryInfo();
        db = new ZEDTrackDB(ItemsListActivity.this);

        discountTotalTV = findViewById(R.id.discountTotalTV);
        grossTv = findViewById(R.id.grossTv);
        totalbillTV = findViewById(R.id.totalbillTV);
        gstTotalTv = findViewById(R.id.gstTotalTv);
        NetTotalTV = findViewById(R.id.NetTotalTV);
        dicountPerTV = findViewById(R.id.dicountPerOrderTV);
        orderNet = findViewById(R.id.orderNet);

        prefs = new SharedPrefs(this);
        Delivery_id = getIntent().getStringExtra("Delivery_id");
        IsNew = getIntent().getStringExtra("IsNew");
        orderStatus = getIntent().getStringExtra("orderStatus");
        Utility.logCatMsg("Delivery_id :" + Delivery_id+"   "+IsNew);
        deliveryInfo = db.getSelectedSQLiteOrderDelivery(Integer.parseInt(Delivery_id), IsNew);

        Log.d("statusAndSync", deliveryInfo.getDelivery_status() + "      " + deliveryInfo.getIsSave());
        setMenuList();
        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (menuList.getVisibility() == View.VISIBLE) {
                    menuList.setVisibility(View.GONE);
                } else {
                    if (IsNew.equals("Return")) {
                        ReturnItemDiloge(position);
                    } else if (deliveryInfo.getDelivery_status().equals("Delivered") && deliveryInfo.getIsPod_sync().toString().equals("2")) {
                        Utility.Toast(ItemsListActivity.this, "Cannot do this action because order is Delivered to Server");
                    } else if (deliveryInfo.getIsPod_sync().toString().equals("2")) {
                        Utility.Toast(ItemsListActivity.this, "Cannot do this action because order is sent");
                    }
                    else {
                        Option_diloag(position);
                    }
                }
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
                Intent intent = new Intent(ItemsListActivity.this, NewUserActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
                break;
            }
            case R.id.actionSyncCompanyCustomerInfo:{
                if (ConnectionDetector.isConnectingToInternet(ItemsListActivity.this)) {
                    startService(new Intent(ItemsListActivity.this, CompanyInfoService.class));
                    Utility.Toast(ItemsListActivity.this, "Syncing Started...");
                } else {
                    Utility.Toast(ItemsListActivity.this, "Check network connection and try again");
                }
                break;
            }
            case R.id.actionAddSalesOrder:{
                Intent intent = new Intent(ItemsListActivity.this, TakeOrder.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
                break;
            }
            case R.id.actionSettings:{
                Intent intent = new Intent(ItemsListActivity.this, SettingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            }
            case R.id.actionAddSalesReturn:{

                Intent intent = new Intent(ItemsListActivity.this, ReturnOrderSearchActivity.class);
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
                    GPSTracker gps = new GPSTracker(ItemsListActivity.this);
                    if (ConnectionDetector.isConnectingToInternet(ItemsListActivity.this)) {
                        if (gps.canGetLocation()) {
                            Utility.Toast(ItemsListActivity.this, "Location Enable Successfully");
                            startservice();
                        } else {
                            Utility.Toast(ItemsListActivity.this, "Enable your GPS first and try again..");
                            //gps.showSettingsAlert();
                        }
                    } else
                        Utility.Toast(ItemsListActivity.this, "Check network connection and try again");
                    break;
                }
                else{
                    SpannableString spanString = new SpannableString("Enable Location");
                    spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0); //fix the color to white
                    item.setTitle(spanString);

                    Utility.Toast(ItemsListActivity.this, "Location Disable Successfully");
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

    private void setMenuList() {
        final String[] menuArray = new String[]{"Register New Customer", "Sync Company Info", "Take new Order", "Search return order"};
        menuList = findViewById(R.id.menuList);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(ItemsListActivity.this, R.layout.menu_list_row, menuArray);
        menuList.setAdapter(adapter);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuList.getVisibility() != View.VISIBLE) {
                    menuList.setVisibility(View.VISIBLE);
                } else {
                    menuList.setVisibility(View.GONE);
                }
            }
        });

        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String option = ((TextView) view).getText().toString();
                menuList.setVisibility(View.GONE);
                if (option.equals("Register New Customer")) {
                    Intent intent = new Intent(ItemsListActivity.this, NewUserActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    finish();
                } else if (option.equals("Sync Company Info")) {
                    if (ConnectionDetector.isConnectingToInternet(ItemsListActivity.this)) {
                        startService(new Intent(ItemsListActivity.this, CompanyInfoService.class));
                        new SyncDataFromServer(ItemsListActivity.this).GetJobs();
                        Utility.Toast(ItemsListActivity.this, "Syncing Start...");
                    } else {
                        Utility.Toast(ItemsListActivity.this, "Check network connection and try again");
                    }
                } else if (option.equals("Take new Order")) {
                    Intent intent = new Intent(ItemsListActivity.this, TakeOrder.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    finish();
                } else if (option.equals("Search return order")) {
                    Intent intent = new Intent(ItemsListActivity.this, ReturnOrderSearchActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        getDataFromSQLite();

        super.onResume();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    private void getDataFromSQLite() {
        list = new ArrayList<>();
        list = db.getSQLiteOrderDeliveryItems(Delivery_id, IsNew,false);
        if (list.size() < 1) {
            Utility.Toast(ItemsListActivity.this, "No Items Found");
        }
        QtylistAdapter = new QTYListAdapter(this, list, IsNew, deliveryInfo.getDelivery_status());
        itemListView.setAdapter(QtylistAdapter);
        findTotal();
    }

    private void setUpActionBar(ActionBar actionBar) {
        LinearLayout mainLayout = findViewById(R.id.mainLayout);
        View v = getLayoutInflater().inflate(R.layout.actionbar_view, mainLayout, false);
        btnMenu = v.findViewById(R.id.btnMenu);
        btnMenu.setVisibility(View.GONE);
        TextView title = v.findViewById(R.id.actionBarTextView);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.appbluegrey)));
        actionBar.setCustomView(v);
        title.setText("Delivery Items Details");

    }

    private void Option_diloag(final int position) {
        final DeliveryItemModel model = list.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Action");
        builder.setItems(option_list, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    SelectQuantityDiloge(position);
                } /*else if (item == 1) {
                    Reject_diloag(position);
                } *//*else if (item == 2) {
                    RestoreSelectedItem(model);
                } */ else if (item == 1) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void Reject_diloag(final int position) {
        final DeliveryItemModel model = list.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Reason");
        builder.setItems(reject_reason_list, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (item == 4) {
                    dialog.dismiss();
                } else {
                    ctnQtyET.setText(model.getCtn_qty() + "");
                    packQtyET.setText(model.getPac_qty() + "");
                    picesQtyET.setText(model.getPcs_qty() + "");
                    model.setRejectReason(reject_reason_list[item].toString());
                    model.setRejected_Quantity(model.getTotal_Quantity());
                    model.setDelivered_Quantity(0);
                    model.setReturn_Quantity(0);
                    UpdateBill(position);
                    Utility.Toast(ItemsListActivity.this, reject_reason_list[item].toString());

                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    private void ReturnReject_diloag(final int position, final int updatedQty) {
        final DeliveryItemModel model = list.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Reason");
        builder.setItems(reject_reason_list, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                if (item == 4) {
                    dialog.dismiss();
                } else {

                    Utility.Toast(ItemsListActivity.this, reject_reason_list[item].toString());
                    int subtractqty = (model.getDelivered_Quantity() + model.getReturn_Quantity()) - updatedQty;
                    int addqty = model.getDelivered_Quantity() + updatedQty;
                    if (updatedQty < model.getReturn_Quantity()) {
                        //    subtractBill(subtractqty + "", model.getPrice() + "");
                    }
                    if (updatedQty > model.getReturn_Quantity()) {
                        //  addtBill(addqty + "", model.getPrice() + "");

                    }

                    model.setRejectReason(reject_reason_list[item].toString());
                    model.setRejected_Quantity(model.getRejected_Quantity() + updatedQty);
                    model.setDelivered_Quantity(subtractqty);
                    model.setReturn_Quantity(updatedQty);

                    if (db.UpdateItemQty(model) == 1) {
                        Utility.logCatMsg("Record Updated in SQLite");
                    }
                    onResume();
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void ReturnItemDiloge(final int position) {
        final DeliveryItemModel model = list.get(position);
        View view = getLayoutInflater().inflate(R.layout.change_qty_layout, null);
        final EditText userText = view.findViewById(R.id.changeQty);
        final TextView qty = view.findViewById(R.id.qtyTV);
        final TextView massagetv = view.findViewById(R.id.massagetv);
        String title = "Enter returns quantity";
        if (deliveryInfo.getDelivery_status().equals("Returned")) {
            title = "Delivery delivery";
            userText.setVisibility(View.GONE);
            massagetv.setVisibility(View.GONE);
        }
        qty.setText("Updated delivered qty: " + model.getDelivered_Quantity() + " \n" +
                "Actual delivered qty: " + model.getActualDeliverd_Quantity());
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ItemsListActivity.this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder
                .setCancelable(false)
                .setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (!isEmpty(userText)) {
                            int EnterNumber = Integer.parseInt(userText.getText().toString());
                            if (EnterNumber > model.getActualDeliverd_Quantity()) {
                                Utility.Toast(ItemsListActivity.this, "Error return qty is more then deliver qty ");

                            } else {
                                ReturnReject_diloag(position, EnterNumber);
                            }
                            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        }

                    }
                })
                .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private boolean isEmpty(EditText myeditText) {
        return myeditText.getText().toString().trim().length() == 0;
    }


    private void SelectQuantityDiloge(final int postion) {
        View view = getLayoutInflater().inflate(R.layout.select_qty_layout, null);
        RadioGroup focrg = view.findViewById(R.id.RGSelectFoc);
        percentageRB = view.findViewById(R.id.precentageRB);
        focET = view.findViewById(R.id.focET);
        ctnQtyET = view.findViewById(R.id.ctnQtyET);
        packQtyET = view.findViewById(R.id.pakQtyET);
        picesQtyET = view.findViewById(R.id.piecesQtyET);
        focET.addTextChangedListener(new FocValidation());

        etCtnPrice = view.findViewById(R.id.etCtnPrice);
        etPackPrice = view.findViewById(R.id.etPackPrice);
        etPiecePrice = view.findViewById(R.id.etPiecePrice);

        picesQtyET.requestFocus();
        etPiecePrice.setText(String.valueOf(list.get(postion).getWSCtnPrice()));
        etPackPrice.setText(String.valueOf(list.get(postion).getRetailPackPrice()));
        etCtnPrice.setText(String.valueOf(list.get(postion).getRetailCtnPrice()));


        if (deliveryInfo.getIsSave().equals("1") && deliveryInfo.getDelivery_status().equals("Delivered")) {
            Log.d("ConditionMeet", "true");
            ctnQtyET.addTextChangedListener(new CtnQtnValidation(postion));
            packQtyET.addTextChangedListener(new PackQtyValidation(postion));
            picesQtyET.addTextChangedListener(new PieceQtyValidation(postion));
        }

        focrg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.qtyRB) {
                    FocType = "Qty";
                    Utility.logCatMsg("Qty");
                } else if (checkedId == R.id.valueRB) {
                    FocType = "Value";
                    Utility.logCatMsg("Value");
                } else if (checkedId == R.id.precentageRB) {
                    FocType = "Percentage";
                    Utility.logCatMsg("percentage");
                    try {
                        int focvalue = Integer.parseInt(focET.getText().toString());
                        if (focvalue >= 100) {
                            focET.setText("0");
                        }
                    } catch (Exception e) {
                        Utility.logCatMsg("Error in FocValidation" + e.getMessage());
                    }
                }
            }
        });
        try {
            Utility.logCatMsg("Foc value " + list.get(postion).getFoc_value());
            Utility.logCatMsg("Qty value " + list.get(postion).getFoc_qty());
            focET.setText(String.valueOf(list.get(postion).getFoc()));
            ctnQtyET.setText(String.valueOf(list.get(postion).getDeliver_ctn_qty()));
            packQtyET.setText(String.valueOf(list.get(postion).getDeliver_pac_qty()));
            picesQtyET.setText(String.valueOf(list.get(postion).getPcs_qty()));
        } catch (Exception e) {
            Utility.logCatMsg("Error in " + e.getMessage());
        }
        Utility.logCatMsg("FOC Qty " + list.get(postion).getFoc_qty());
        Utility.logCatMsg("FOC Value " + list.get(postion).getFoc_value());
        Utility.logCatMsg("FOC Percent" + list.get(postion).getFoc_percentage());
        if (list.get(postion).getFoc_percentage() != 0) {
            RadioButton rb = view.findViewById(R.id.precentageRB);
            rb.setChecked(true);
            focET.setText(list.get(postion).getFoc_percentage() + "");
        } else if (list.get(postion).getFoc_value() != 0) {
            RadioButton rb = view.findViewById(R.id.valueRB);
            rb.setChecked(true);
            focET.setText(list.get(postion).getFoc_value() + "");
        } else if (list.get(postion).getFoc_qty() != 0) {
            RadioButton rb = view.findViewById(R.id.qtyRB);
            rb.setChecked(true);
            focET.setText(list.get(postion).getFoc_qty() + "");
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ItemsListActivity.this);
        alertDialogBuilder.setTitle("Select Deliver Qty and FOC");
        alertDialogBuilder
                .setCancelable(false)
                .setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        UpdateBill(postion);
                        QtylistAdapter.notifyDataSetChanged();
                        dialog.dismiss();
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

    private class FocValidation implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (percentageRB.isChecked()) {
                try {
                    int focvalue = Integer.parseInt(focET.getText().toString());
                    if (focvalue >= 100) {
                        focET.setText("0");
                    }
                } catch (Exception e) {
                    Utility.logCatMsg("Error in FocValidation" + e.getMessage());
                }
            }

        }
    }

    private class PieceQtyValidation implements TextWatcher {
        int postion;

        PieceQtyValidation(int postion) {
            this.postion = postion;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (picesQtyET.getText().toString().equals("")) {

            } else if (Integer.parseInt(picesQtyET.getText().toString()) > list.get(postion).getPcs_qty()) {
                //picesQtyET.setText(summeryList.get(postion).getDeliver_pcs_qty() + "");
                picesQtyET.setText(String.valueOf(list.get(postion).getPcs_qty()));
                Utility.Toast(ItemsListActivity.this, "Wrong Qty,Plz enter exact Qty");
            }
        }
    }

    private class PackQtyValidation implements TextWatcher {
        int postion;

        PackQtyValidation(int postion) {
            this.postion = postion;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            try {
                if (packQtyET.getText().toString().equals("")) {

                } else if (Integer.parseInt(packQtyET.getText().toString()) > list.get(postion).getPac_qty()) {
                    //packQtyET.setText(summeryList.get(postion).getDeliver_pac_qty() + "");
                    packQtyET.setText(String.valueOf(list.get(postion).getPac_qty()));
                    Utility.Toast(ItemsListActivity.this, "Wrong Qty,Plz enter exact Qty");
                }
            } catch (Exception e) {
                Utility.logCatMsg("Error : " + e.getMessage());
            }
        }
    }

    private class CtnQtnValidation implements TextWatcher {
        int postion;

        CtnQtnValidation(int postion) {
            this.postion = postion;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            try {
                if (ctnQtyET.getText().toString().equals("")) {

                } else if (Integer.parseInt(ctnQtyET.getText().toString()) > list.get(postion).getCtn_qty()) {
                    //ctnQtyET.setText(summeryList.get(postion).getDeliver_ctn_qty() + "");
                    ctnQtyET.setText(String.valueOf(list.get(postion).getDeliver_ctn_qty()));
                    Utility.Toast(ItemsListActivity.this, "Wrong Qty,Plz enter exact Qty");
                }
            } catch (Exception e) {
                Utility.logCatMsg("Error : " + e.getMessage());
            }

        }
    }

    private void UpdateBill(int list_postion) {
        Log.d("statusAndSync", deliveryInfo.getDelivery_status() + "      " + deliveryInfo.getIsSave());
        int ctnQty = 0, packQty = 0, pieceQty = 0;
        if (!ctnQtyET.getText().toString().equals(""))
            ctnQty = Integer.parseInt(ctnQtyET.getText().toString());
        if (!packQtyET.getText().toString().equals(""))
            packQty = Integer.parseInt(packQtyET.getText().toString());
        if (!picesQtyET.getText().toString().equals(""))

            pieceQty = Integer.parseInt(picesQtyET.getText().toString());

        if (deliveryInfo.getDelivery_status().equals("Booking") && deliveryInfo.getIsSave().equals("1")) {

            Log.d("deliveryInfoStatus : ", deliveryInfo.getDelivery_status() + "     " + deliveryInfo.getIsSave());
            if (Integer.parseInt(ctnQtyET.getText().toString()) > 0) {
                list.get(list_postion).setCtn_qty(Integer.parseInt(ctnQtyET.getText().toString()));
            }
            if (Integer.parseInt(packQtyET.getText().toString()) > 0) {
                list.get(list_postion).setPac_qty(Integer.parseInt(packQtyET.getText().toString()));
            }
            if (Integer.parseInt(picesQtyET.getText().toString()) > 0) {
                list.get(list_postion).setPcs_qty(Integer.parseInt(picesQtyET.getText().toString()));
            }

        } else {

            if (Integer.parseInt(ctnQtyET.getText().toString()) <= list.get(list_postion).getCtn_qty()) {
                list.get(list_postion).setDeliver_ctn_qty(Integer.parseInt(ctnQtyET.getText().toString()));
                list.get(list_postion).setReject_ctn_qty(list.get(list_postion).getCtn_qty() - list.get(list_postion).getDeliver_ctn_qty());
            }
            if (Integer.parseInt(packQtyET.getText().toString()) <= list.get(list_postion).getPac_qty()) {
                list.get(list_postion).setDeliver_pac_qty(Integer.parseInt(packQtyET.getText().toString()));
                list.get(list_postion).setReject_pac_qty(list.get(list_postion).getPac_qty() - list.get(list_postion).getDeliver_pac_qty());
            }

            if (Integer.parseInt(picesQtyET.getText().toString()) <= list.get(list_postion).getPcs_qty()) {
                list.get(list_postion).setDeliver_pcs_qty(Integer.parseInt(picesQtyET.getText().toString()));
                list.get(list_postion).setReject_pcs_qty(list.get(list_postion).getPcs_qty() - list.get(list_postion).getDeliver_pcs_qty());
            }
        }


        int ctnSize = list.get(list_postion).getCtnSize();
        int packSize = list.get(list_postion).getPackSize();

        int totalDeliverQtyInPieces = (ctnQty * ctnSize * packSize) + (packQty * packSize) + pieceQty;
        Utility.logCatMsg("Total Deliver Quantity in Pieces " + totalDeliverQtyInPieces);
        list.get(list_postion).setItemTotalDeliverQtyInPieces(totalDeliverQtyInPieces);

        int ActualCtnQty = list.get(list_postion).getCtn_qty();
        int ActualPackQty = list.get(list_postion).getPac_qty();
        int ActualPcsQty = list.get(list_postion).getPcs_qty();

        int totalActualQtyInPieces = (ActualCtnQty * ctnSize * packSize) + (ActualPackQty * packSize) + ActualPcsQty;
        list.get(list_postion).setItemTotalActualQtyInPieces(totalActualQtyInPieces);
        Utility.logCatMsg("Total Actual Quantity in Pieces " + totalDeliverQtyInPieces);

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
        float sumOfTotalRetailPrice = totalctnRetailPrice + totalPackRetailPrice + totalPiecesRetailPrice;

        list.get(list_postion).setTotalwholeSalePrice(sumOfTotalWholeSalePrice);  // this is handel on insertion time
        list.get(list_postion).setTotalCostPrice(sumOfTotalCostPrice);
        list.get(list_postion).setTotalRetailPrice(sumOfTotalRetailPrice);

        list.get(list_postion).setItem_discount(0);
        list.get(list_postion).setDisplayPrice(0);
        list.get(list_postion).setItemGstPer(Utility.GST_PERCENT);
        float gst = list.get(list_postion).getItemGstPer();
        float gstPercent = 0;
        if (gst != 0)
            gstPercent = gst / 100.0f;

        float netTotal = sumOfTotalRetailPrice + list.get(list_postion).getItem_discount();
        float gstValue = gstPercent * netTotal;
        list.get(list_postion).setItemGstValue(gstValue);

        list.get(list_postion).setNetTotalRetailPrice(list.get(list_postion).getTotalRetailPrice() + list.get(list_postion).getItemGstValue());

        if (focET.getText().toString().equals("0") || focET.getText().length() == 0)
            setFocValuesOfList(list_postion, 0);
        else
            setFocValuesOfList(list_postion, 1);
        if (db.UpdateItemQty(list.get(list_postion)) == 1) {
            Utility.logCatMsg("Record Updated in SQLite");


            if (db.updateOrderQtyMasterStatusDelivered(Delivery_id, totalDeliverQtyInPieces, list.get(list_postion).getTotalRetailPrice()) == 1) {
                Log.d("masterQtyUpdated : ", "true");
            } else {
                Log.d("masterQtyUpdated : ", "false");
            }

        }


        onResume();
        findTotal();
    }

    private void findTotal() {
        float gross = 0;
        float discount = 0;
        float total = 0;
        float gst = 0;
        float net = 0;
        for (int i = 0; i < list.size(); i++) {
            gross = gross + list.get(i).getTotalRetailPrice();
            discount = discount + list.get(i).getItem_discount();
            total = total + (list.get(i).getTotalRetailPrice() - list.get(i).getItem_discount());
            gst = gst + list.get(i).getItemGstValue();
            net = net + (list.get(i).getNetTotalRetailPrice());
        }
        grossTv.setText(gross + "");
        totalbillTV.setText(total + "");
        discountTotalTV.setText(discount + "");
        gstTotalTv.setText(gst + "");
        NetTotalTV.setText(net + "");
        dicountPerTV.setText(deliveryInfo.getPercentageDiscount() + " % = " + deliveryInfo.getDiscount() + " "+prefs.getCurrency());
        float disNo = Float.parseFloat(deliveryInfo.getPercentageDiscount());
        float dis = (disNo / 100) * net;
        float result = net - dis;
        orderNet.setText(result + "");
        deliveryInfo.setDiscount(dis + "");
        deliveryInfo.setGrossTotalBill(gross + "");
        deliveryInfo.setTotal_Bill(net + "");
        if (!deliveryInfo.getIsPod_sync().toString().equals("2"))
            if (db.UpdatePercentageBill(deliveryInfo) != 0)
                Utility.logCatMsg("TblOrderConfirmMaster updated..");
    }

    private void setFocValuesOfList(int list_postion, int flage) {
        list.get(list_postion).setFoc_percentage(0);
        list.get(list_postion).setFoc_qty(0);
        list.get(list_postion).setFoc_value(0);
        list.get(list_postion).setItem_discount(0);
        if (flage == 1) {
            if (FocType != null && !FocType.equals("")) {
                if (FocType.equals("Qty")) {
                    list.get(list_postion).setFoc_qty(Integer.parseInt(focET.getText().toString()));
                } else if (FocType.equals("Value")) {
                    int focValue = Integer.parseInt(focET.getText().toString());
                    list.get(list_postion).setFoc_value(focValue);
                    list.get(list_postion).setItem_discount(focValue);
                } else if (FocType.equals("Percentage")) {
                    Float focValue = Float.parseFloat(focET.getText().toString());
                    Float discountInValue = (focValue / 100.0f) * list.get(list_postion).getTotalRetailPrice();
                    Utility.logCatMsg(focValue + "FOC Percent % of " + list.get(list_postion).getTotalRetailPrice() + "in value Is = " + discountInValue);
                    list.get(list_postion).setFoc_value(Math.round(discountInValue));
                    list.get(list_postion).setItem_discount(Math.round(discountInValue));
                    list.get(list_postion).setFoc_percentage(Integer.parseInt(focET.getText().toString()));
                }
            }
        }
    }
}
