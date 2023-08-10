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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import detrack.zaryansgroup.com.detrack.activity.Adapter.QTYListAdapter;
import detrack.zaryansgroup.com.detrack.activity.Map.GPSTracker;
import detrack.zaryansgroup.com.detrack.activity.Model.DeliveryInfo;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.DeliveryItemModel;
import detrack.zaryansgroup.com.detrack.activity.Model.Params;
import detrack.zaryansgroup.com.detrack.activity.SQLlite.ZEDTrackDB;
import detrack.zaryansgroup.com.detrack.activity.Service.CompanyInfoService;
import detrack.zaryansgroup.com.detrack.activity.Service.GPSService;
import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;
import detrack.zaryansgroup.com.detrack.activity.ksoap.SendDataToService;
import detrack.zaryansgroup.com.detrack.activity.utilites.ConnectionDetector;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import detrack.zaryansgroup.com.detrack.R;

public class ReturnItemListActivity extends AppCompatActivity {
    private QTYListAdapter QtylistAdapter;
    private ArrayList<DeliveryItemModel> list;
    ListView itemListView;
    Button backbtn;
    ZEDTrackDB db;
    String Delivery_id, IsNew, deliveryStatus;
    CharSequence[] option_list = {"Change Item Qty", "Reject Item", "Restore Items", "Cancel"};
    CharSequence[] reject_reason_list = {"Wrong Type", "Wrong Size", "Goods Damaged", "Other", "Cancel"};
    CharSequence[] rejectOptionList = {"Return Full Order", "Return Items"};
    SharedPrefs prefs;
    ImageButton btnMenu;
    DeliveryInfo deliveryInfo;
    String FocType = "Percentage";
    RadioButton percentageRB;
    TextView tvRejectedOrReturned;
    EditText focET, ctnQtyET, packQtyET, picesQtyET, etSearch;
    ProgressBar pb;
    String returned = "false";
    int actualQty = 0;
    String type = "";//if it is cotton, pack or pieces
    AlarmManager alarm;
    PendingIntent pintent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_item_list);
        if (getActionBar() != null) {
            setUpActionBar(getSupportActionBar());
        }
        itemListView = findViewById(R.id.item_listview);
        InitilizeAlaram();
        tvRejectedOrReturned = findViewById(R.id.tvRejectedOrReturned);
        backbtn = findViewById(R.id.backBtn);
        etSearch = findViewById(R.id.etSearch);
        etSearch.setFocusable(false);
        deliveryInfo = new DeliveryInfo();
        db = new ZEDTrackDB(ReturnItemListActivity.this);
        prefs = new SharedPrefs(this);
        Delivery_id = getIntent().getStringExtra("Delivery_id");
        IsNew = getIntent().getStringExtra("IsNew");
        Utility.logCatMsg("Delivery_id :" + Delivery_id);
        deliveryStatus = getIntent().getStringExtra("deliveryStatus");
        returned = getIntent().getStringExtra("returned");

        if (returned.equals("true")) {
            tvRejectedOrReturned.setText("Returned Item");
        }

        deliveryInfo = db.getSelectedSQLiteOrderDelivery(Integer.parseInt(Delivery_id), IsNew);
        pb = findViewById(R.id.progressBar);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (IsNew.equals("Return")) {
                    returnOptionDialog(position);

                } else if (deliveryInfo.getDelivery_status().equals("Delivered") && deliveryInfo.getIsPod_sync().toString().equals("2")) {
                    Utility.Toast(ReturnItemListActivity.this, "Cannot do this action because order is Delivered to Server");
                } else if (deliveryInfo.getIsPod_sync().toString().equals("2")) {
                    Utility.Toast(ReturnItemListActivity.this, "Cannot do this action because order is sent");
                } else
                    Option_diloag(position);
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

    @Override
    protected void onResume() {
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
            Utility.Toast(ReturnItemListActivity.this, "No Items Found");
        }
        QtylistAdapter = new QTYListAdapter(this, list, IsNew, deliveryInfo.getDelivery_status());
        itemListView.setAdapter(QtylistAdapter);
    }

    private void getDataFromServer() {
        list = new ArrayList<>();
        ArrayList<Params> parameters = new ArrayList<Params>();
        Params p1 = new Params();
        p1.setKey("Dilevery_id");
        p1.setValue(Delivery_id); // user_id
        parameters.add(p1);
        new GetTodayJobsItems("GetTodayJobItems", "ZEDtrack.asmx", parameters).execute();
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
                } else if (item == 1) {
                    Reject_diloag(position);
                } else if (item == 2) {
                    RestoreSelectedItem(model);
                } else if (item == 3) {
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
                    ctnQtyET.setText(String.valueOf(model.getCtn_qty()));
                    packQtyET.setText(String.valueOf(model.getPac_qty()));
                    picesQtyET.setText(String.valueOf(model.getPcs_qty()));
                    model.setRejectReason(reject_reason_list[item].toString());
                    model.setRejected_Quantity(model.getTotal_Quantity());
                    model.setDelivered_Quantity(0);
                    model.setReturn_Quantity(0);
                    UpdateBill(position);
                    Utility.Toast(ReturnItemListActivity.this, reject_reason_list[item].toString());

                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void Reject_diloag(final int position, final int updatedQty) {
        final DeliveryItemModel model = list.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Reason");
        builder.setItems(reject_reason_list, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                if (item == 4) {
                    dialog.dismiss();
                } else {

                    Utility.Toast(ReturnItemListActivity.this, reject_reason_list[item].toString());
                    int subtractqty = model.getDelivered_Quantity() - updatedQty;
                    int addqty = updatedQty - model.getDelivered_Quantity();
                    Utility.logCatMsg("Total qty " + model.getTotal_Quantity() + " Deliver qty " + model.getDelivered_Quantity() + "update qty " + updatedQty);
                    if (updatedQty < model.getDelivered_Quantity()) {
                        subtractBill(subtractqty + "", model.getWSCtnPrice() + "");
                    }
                    if (updatedQty > model.getDelivered_Quantity()) {
                        addtBill(addqty + "", model.getWSCtnPrice() + "");
                    }
                    model.setRejectReason(reject_reason_list[item].toString());
                    model.setRejected_Quantity(model.getTotal_Quantity() - updatedQty);
                    model.setDelivered_Quantity(updatedQty);
                    model.setReturn_Quantity(0);
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

    private void returnOptionDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Return Type");
        builder.setItems(rejectOptionList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (rejectOptionList[item].equals("Return Full Order")) {

                    //Reject Full order
                    returnFullOrderDialog(position);
                } else {
                    ReturnItemDiloge(position);
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void returnFullOrderDialog(final int position) {

        final DeliveryItemModel model = list.get(position);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Return Full Order").setMessage("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        }).show();
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

                    Utility.Toast(ReturnItemListActivity.this, reject_reason_list[item].toString());
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

                    //summeryList.set(position,model);
                    updateReturndBill(position, updatedQty);
                    QtylistAdapter.notifyDataSetChanged();
//                    if (db.UpdateItemQty(model) == 1) {
//
//                        Log.d("recordUpdated : ","true");
//                    }
//                    else{
//                        Log.d("recordUpdated : ","false");
//                    }
//                    onResume();
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void updateReturndBill(int position, int newQty) {
        int ctnQty = 0, packQty = 0, pieceQty = 0;
        if (type.equals("coton")) {
            if (newQty <= list.get(position).getCtn_qty()) {
                ctnQty = newQty;
                list.get(position).setDeliver_ctn_qty(newQty);
                list.get(position).setReject_ctn_qty(list.get(position).getCtn_qty() - list.get(position).getDeliver_ctn_qty());
            }
        } else if (type.equals("pack")) {

            if (newQty <= list.get(position).getPac_qty()) {
                packQty = newQty;
                list.get(position).setDeliver_pac_qty(newQty);
                list.get(position).setReject_pac_qty(list.get(position).getPac_qty() - list.get(position).getDeliver_pac_qty());
            }

        } else if (type.equals("pieces")) {
            if (newQty <= list.get(position).getPcs_qty()) {
                pieceQty = newQty;
                list.get(position).setDeliver_pcs_qty(newQty);
                list.get(position).setReject_pcs_qty(list.get(position).getPcs_qty() - list.get(position).getDeliver_pcs_qty());
            }

        }


        int ctnSize = ctnQty;
        int packSize = packQty;

        int totalDeliverQtyInPieces = (ctnQty * ctnSize * packSize) + (packQty * packSize) + pieceQty;
        Utility.logCatMsg("Total Deliver Quantity in Pieces " + totalDeliverQtyInPieces);
        list.get(position).setItemTotalDeliverQtyInPieces(totalDeliverQtyInPieces);

        int ActualCtnQty = list.get(position).getCtn_qty();
        int ActualPackQty = list.get(position).getPac_qty();
        int ActualPcsQty = list.get(position).getPcs_qty();

        int totalActualQtyInPieces = (ActualCtnQty * ctnSize * packSize) + (ActualPackQty * packSize) + ActualPcsQty;
        list.get(position).setItemTotalActualQtyInPieces(totalActualQtyInPieces);
        Utility.logCatMsg("Total Actual Quantity in Pieces " + totalDeliverQtyInPieces);

        float ctnWSPrice = list.get(position).getRetailPiecePrice();
        float totalCtnWSPrice = ctnQty * ctnWSPrice;
        float ctnCostPrice = list.get(position).getCostCtnPrice();
        float totalctnCostPrice = ctnQty * ctnCostPrice;
        float ctnRetailPrice = list.get(position).getRetailCtnPrice();
        float totalctnRetailPrice = ctnQty * ctnRetailPrice;

        float packWSPrice = list.get(position).getWSPackPrice();
        float totalPackWSPrice = packQty * packWSPrice;
        float packCostPrice = list.get(position).getCostPackPrice();
        float totalPackCostPrice = packQty * packCostPrice;
        float packRetailPrice = list.get(position).getRetailPackPrice();
        float totalPackRetailPrice = packQty * packRetailPrice;

        float PiecesWSPrice = list.get(position).getWSPiecePrice();
        float totalPiecesWSPrice = pieceQty * PiecesWSPrice;
        float PiecesCostPrice = list.get(position).getCostPiecePrice();
        float totalPiecesCostPrice = pieceQty * PiecesCostPrice;
        float PiecesRetailPrice = list.get(position).getWSCtnPrice();
        float totalPiecesRetailPrice = pieceQty * PiecesRetailPrice;

        float sumOfWholeSalePrice = ctnWSPrice + packWSPrice + PiecesWSPrice;
        float sumOfTotalWholeSalePrice = totalCtnWSPrice + totalPackWSPrice + totalPiecesWSPrice;

        float sumOfCostPrice = ctnCostPrice + packCostPrice + PiecesCostPrice;
        float sumOfTotalCostPrice = totalctnCostPrice + totalPackCostPrice + totalPiecesCostPrice;

        float sumOfRetailPrice = ctnRetailPrice + packRetailPrice + PiecesRetailPrice;
        float sumOfTotalRetailPrice = totalctnRetailPrice + totalPackRetailPrice + totalPiecesRetailPrice;

        list.get(position).setTotalwholeSalePrice(sumOfTotalWholeSalePrice);  // this is handel on insertion time
        list.get(position).setTotalCostPrice(sumOfTotalCostPrice);
        list.get(position).setTotalRetailPrice(sumOfTotalRetailPrice);

        list.get(position).setItem_discount(0);
        list.get(position).setDisplayPrice(0);
        list.get(position).setItemGstPer(Utility.GST_PERCENT);
        float gst = list.get(position).getItemGstPer();
        float gstPercent = 0;
        if (gst != 0)
            gstPercent = gst / 100.0f;

        float netTotal = sumOfTotalRetailPrice + list.get(position).getItem_discount();
        float gstValue = gstPercent * netTotal;
        list.get(position).setItemGstValue(gstValue);

        list.get(position).setNetTotalRetailPrice(list.get(position).getTotalRetailPrice() + list.get(position).getItemGstValue());

    }

    private void ReturnItemDiloge(final int position) {
        final DeliveryItemModel model = list.get(position);
        View view = getLayoutInflater().inflate(R.layout.change_qty_layout, null);
        final EditText userText = view.findViewById(R.id.changeQty);
        final TextView qty = view.findViewById(R.id.qtyTV);
        final TextView massagetv = view.findViewById(R.id.massagetv);
        String title = "Enter returns quantity";

        if (deliveryStatus.equals("Returned")) {
            title = "Delivery delivery";
            userText.setVisibility(View.GONE);
            massagetv.setVisibility(View.GONE);
        }

        if (model.getCtn_qty() > 0) {
            Log.d("myQuantity : ", "Coton");
            actualQty = model.getCtn_qty();
            type = "coton";
        } else if (model.getPac_qty() > 0) {
            Log.d("myQuantity : ", "Pack");
            actualQty = model.getPac_qty();
            type = "pack";
        } else if (model.getPcs_qty() > 0) {
            Log.d("myQuantity : ", "pieces");
            actualQty = model.getPcs_qty();
            type = "pieces";
        }

        qty.setText("Actual delivered qty: " + actualQty);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReturnItemListActivity.this);
        alertDialogBuilder.setTitle(title);

        alertDialogBuilder
                .setCancelable(false)
                .setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (!isEmpty(userText)) {
                            int EnterNumber = Integer.parseInt(userText.getText().toString());
                            if (EnterNumber > actualQty) {
                                Utility.Toast(ReturnItemListActivity.this, "Error return qty is more then deliver qty ");

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

    private void subtractBill(String DeductQty, String ItemPrice) {
        DeliveryInfo deliverInfo = new DeliveryInfo();
        deliverInfo = db.getSelectedSQLiteOrderDelivery(Integer.parseInt(Delivery_id), IsNew);
        Float totalprice = Float.parseFloat(ItemPrice) * Float.parseFloat(DeductQty);
        Float percentageTotalPrice = Float.parseFloat(deliverInfo.getPercentageDiscount().toString()) / 100 * totalprice;
        Float totalSubtraction = totalprice - percentageTotalPrice;
        deliverInfo.setNetTotal((Float.parseFloat(deliverInfo.getNetTotal()) - totalSubtraction) + "");
        deliverInfo.setTotal_Bill((Float.parseFloat(deliverInfo.getTotal_Bill()) - totalprice) + "");
        db.UpdatePercentageBill(deliverInfo);
        Utility.Toast(ReturnItemListActivity.this, "Subtracting " + totalSubtraction);
    }

    private void addtBill(String DeductQty, String ItemPrice) {
        DeliveryInfo deliverInfo = new DeliveryInfo();
        deliverInfo = db.getSelectedSQLiteOrderDelivery(Integer.parseInt(Delivery_id), IsNew);
        Float totalprice = Float.parseFloat(ItemPrice) * Float.parseFloat(DeductQty);
        Float percentageTotalPrice = Float.parseFloat(deliverInfo.getPercentageDiscount().toString()) / 100 * totalprice;
        Float totalAddition = totalprice - percentageTotalPrice;
        deliverInfo.setNetTotal((Float.parseFloat(deliverInfo.getNetTotal()) + totalAddition) + "");
        deliverInfo.setTotal_Bill((Float.parseFloat(deliverInfo.getTotal_Bill()) + totalprice) + "");
        db.UpdatePercentageBill(deliverInfo);
        Utility.Toast(ReturnItemListActivity.this, "Adding " + totalAddition);
    }

    private void RestoreSelectedItem(DeliveryItemModel model) {
        int addqty = model.getTotal_Quantity() - model.getDelivered_Quantity();
        if (model.getTotal_Quantity() > model.getDelivered_Quantity()) {
            addtBill(addqty + "", model.getWSCtnPrice() + "");
        }
        model.setRejectReason("null");
        model.setRejected_Quantity(0);
        model.setReturn_Quantity(0);
        model.setDelivered_Quantity(model.getTotal_Quantity());
        if (db.UpdateItemQty(model) == 1) {
            Utility.logCatMsg("Record Updated in SQLite");
        } else {
            Utility.logCatMsg("Record Not Updated in SQLite");
        }
        onResume();

    }

    private void ReturnSelectedItem(DeliveryItemModel model, int returnQty) {
        int addqty = model.getDelivered_Quantity() - returnQty;
        /*if (model.getQuantity() > model.getDelivered_Quantity()) {
            addtBill(addqty + "", model.getPrice() + "");
        }*/
        model.setRejectReason("null");
        model.setRejected_Quantity(0);
        model.setReturn_Quantity(0);
        model.setDelivered_Quantity(model.getTotal_Quantity());
        if (db.UpdateItemQty(model) == 1) {
            Utility.logCatMsg("Record Updated in SQLite");
        } else {
            Utility.logCatMsg("Record Not Updated in SQLite");
        }
        onResume();

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
        ctnQtyET.addTextChangedListener(new CtnQtnValidation(postion));
        packQtyET.addTextChangedListener(new PackQtyValidation(postion));
        picesQtyET.addTextChangedListener(new PieceQtyValidation(postion));

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
            Utility.logCatMsg("Foc value " + list.get(postion).getFoc());
            Utility.logCatMsg("Qty value " + list.get(postion).getSelectedValue());
            focET.setText(list.get(postion).getFoc() + "");
            ctnQtyET.setText(list.get(postion).getDeliver_ctn_qty() + "");
            packQtyET.setText(list.get(postion).getDeliver_pac_qty() + "");
            picesQtyET.setText(list.get(postion).getDeliver_pcs_qty() + "");
        } catch (Exception e) {
            Utility.logCatMsg("Error in " + e.getMessage());
        }
        Utility.logCatMsg("Qty " + list.get(postion).getQtyType());
        if (list.get(postion).getFocType() != null) {
            if (list.get(postion).getFocType().toString().equals("Qty")) {
                RadioButton rb = view.findViewById(R.id.qtyRB);
                rb.setChecked(true);
            } else if (list.get(postion).getFocType().toString().equals("Value")) {
                RadioButton rb = view.findViewById(R.id.valueRB);
                rb.setChecked(true);
            } else if (list.get(postion).getFocType().toString().equals("Percentage")) {
                RadioButton rb = view.findViewById(R.id.precentageRB);
                rb.setChecked(true);
            }
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReturnItemListActivity.this);
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
                picesQtyET.setText(list.get(postion).getDeliver_pcs_qty() + "");
                Utility.Toast(ReturnItemListActivity.this, "Wrong Qty,Plz enter exact Qty");
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
                    packQtyET.setText(list.get(postion).getDeliver_pac_qty() + "");
                    Utility.Toast(ReturnItemListActivity.this, "Wrong Qty,Plz enter exact Qty");
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
                    ctnQtyET.setText(list.get(postion).getDeliver_ctn_qty() + "");
                    Utility.Toast(ReturnItemListActivity.this, "Wrong Qty,Plz enter exact Qty");
                }
            } catch (Exception e) {
                Utility.logCatMsg("Error : " + e.getMessage());
            }

        }
    }

    private void UpdateBill(int list_postion) {

        Log.d("type :", type);
        int ctnQty = 0, packQty = 0, pieceQty = 0;
        if (!ctnQtyET.getText().toString().equals(""))
            ctnQty = Integer.parseInt(ctnQtyET.getText().toString());
        if (!packQtyET.getText().toString().equals(""))
            packQty = Integer.parseInt(packQtyET.getText().toString());
        if (!picesQtyET.getText().toString().equals(""))
            pieceQty = Integer.parseInt(picesQtyET.getText().toString());

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

        Utility.logCatMsg("Total Cost Price " + list.get(list_postion).getTotalCostPrice());
        Utility.logCatMsg("Total WS Price " + list.get(list_postion).getTotalwholeSalePrice());
        Utility.logCatMsg("Total Retail Price " + list.get(list_postion).getTotalRetailPrice());

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
        Utility.logCatMsg("Display price " + list.get(list_postion).getDisplayPrice());
        Utility.logCatMsg("Gst Value " + list.get(list_postion).getItemGstValue());
        Utility.logCatMsg("Net Total Bill  " + list.get(list_postion).getNetTotalRetailPrice());

        if (db.UpdateItemQty(list.get(list_postion)) == 1) {
            Utility.logCatMsg("Record Updated in SQLite");
        }
        onResume();
    }

    private class GetTodayJobsItems extends SendDataToService {

        public GetTodayJobsItems(String methodName, String className, ArrayList<Params> list) {
            super(ReturnItemListActivity.this, methodName, className, list);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            if (aVoid != null) {
                try {
                    if (aVoid.equals("[]")) {
                        pb.setVisibility(View.GONE);
                        Utility.logCatMsg("NO Items found of this jobs");
                    } else {
                        parseJsonFeedItems(new JSONArray(aVoid));
                    }
                } catch (Exception e) {
                    e.getMessage();
                    Utility.logCatMsg("User Error " + e);
                }
            } else {
                Utility.logCatMsg("****** NULL ******");
            }

        }
    }

    private void parseJsonFeedItems(JSONArray feedArray) {
        try {
            list.clear();
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                DeliveryItemModel model = new DeliveryItemModel();
                model.setOrder_item_id(feedObj.getInt("OrderId"));
                model.setServer_Item_Id(feedObj.getInt("OrderDetailId"));
                model.setName(feedObj.getString("Name"));
                model.setItem_id(feedObj.getInt("ItemId"));

                model.setCostCtnPrice(feedObj.getLong("CostCtnPrice"));
                model.setRetailPiecePrice(feedObj.getLong("WSCtnPrice"));
                model.setRetailCtnPrice(feedObj.getLong("RetailCtnPrice"));

                model.setCostPackPrice(feedObj.getLong("CostPackPrice"));
                model.setWSPackPrice(feedObj.getLong("WSPackPrice"));
                model.setRetailPackPrice(feedObj.getLong("RetailPackPrice"));

                model.setCostPiecePrice(feedObj.getLong("CostPiecePrice"));
                model.setWSPiecePrice(feedObj.getLong("WSPiecePrice"));
                model.setWSCtnPrice(feedObj.getLong("RetailPiecePrice"));

                model.setCtn_qty(feedObj.getInt("CtnQuantity"));
                model.setPac_qty(feedObj.getInt("PackQuantity"));
                model.setPcs_qty(feedObj.getInt("PcsQuantity"));
                model.setFoc_qty(feedObj.getInt("FOCQty"));
                model.setFoc_value(feedObj.getInt("FOCValue"));
                model.setItem_discount(feedObj.getLong("Discount"));
                model.setTotalCostPrice(feedObj.getLong("TotalCost"));
                model.setTotalwholeSalePrice(feedObj.getLong("TotalWholesale"));
                model.setTotalRetailPrice(feedObj.getLong("TotalRetail"));

                model.setNetTotalRetailPrice(feedObj.getLong("NetAmount"));

                model.setRoute_id(feedObj.getInt("RouteId"));
                model.setCategoryId(feedObj.getInt("CategoryId"));
                model.setTotal_Quantity(feedObj.getInt("TotalQuantity"));
                Log.d("updateTotalQty : ", String.valueOf(model.getTotal_Quantity()));
                // extra fileds
                model.setReturn_Quantity(0);
                model.setRejected_Quantity(0);

                model.setDisplayPrice(feedObj.getLong("DisplayPrice"));
                model.setItemGstValue(feedObj.getLong("GSTValue"));
                model.setItemGstPer(feedObj.getLong("GSTPercentage"));

                if (!feedObj.getString("TotalWholesale").equals("null"))
                    model.setDelivered_Quantity(feedObj.getInt("TotalWholesale"));
                else
                    model.setDelivered_Quantity(0);

                model.setRejectReason("");
                list.add(model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Utility.logCatMsg("Feed Delivery item error" + e);
        }
        if (list.size() < 1) {
            Utility.Toast(ReturnItemListActivity.this, "No Items Found");
        }
        Log.d("returnOrderParameter : ", list.toString() + "\n\n" + IsNew + "\n\n\n" + deliveryStatus);
        QtylistAdapter = new QTYListAdapter(this, list, IsNew, deliveryStatus);
        itemListView.setAdapter(QtylistAdapter);
        itemListView.setDividerHeight(8);
    }
}
