package detrack.zaryansgroup.com.detrack.activity.activites;

import android.Manifest;
import androidx.appcompat.app.ActionBar;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import detrack.zaryansgroup.com.detrack.activity.Adapter.items_list_in_an_order_adapter;
import detrack.zaryansgroup.com.detrack.activity.Map.GPSTracker;
import detrack.zaryansgroup.com.detrack.activity.Model.BankModels.CompanyWiseBanksModel;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.DeliveryItemModel;
import detrack.zaryansgroup.com.detrack.activity.Model.DeliveryInfo;
import detrack.zaryansgroup.com.detrack.activity.Model.ImagesModel;
import detrack.zaryansgroup.com.detrack.activity.SQLlite.ZEDTrackDB;
import detrack.zaryansgroup.com.detrack.activity.Service.CompanyInfoService;
import detrack.zaryansgroup.com.detrack.activity.Service.GPSService;
import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;
import detrack.zaryansgroup.com.detrack.activity.activites.SecondView.SelectProductActivitySecond;
import detrack.zaryansgroup.com.detrack.activity.utilites.ConnectionDetector;
import detrack.zaryansgroup.com.detrack.activity.utilites.SendPod;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import detrack.zaryansgroup.com.detrack.R;
import timber.log.Timber;

public class POD_DashBoard extends AppCompatActivity implements View.OnClickListener {
    Button itemBtn, noteBtn, recevied_by, discount_btn,btn_view_order_details,btn_dismiss_items_dialogue;
    CharSequence[] rejected_items = {"Sender Not Around", "Wrong Address", "Goods Not Ready", "Vehicle Breakdown", "Bad Weather", "Restore", "Cancel"};

    //All the action buttons at the Bottom of the Activity..
    ImageButton captureImgBtn, backImgBtn, sendPodImgBtn, rejectImgBtn, callImgBtn, MapImgBtn;
    ZEDTrackDB db;
    DeliveryInfo deliverInfo;
    TextView Order_addressTV, ServerOrderSatusTV, Order_To_nameTV, OrderSatusTV, Order_timeTV,
            InstructionTV, totalbill, perdiscount, net_total, latlngTV, tvGSt,distributorName,subDistributorName;

    LinearLayout llSubDistView;


    RecyclerView rv_itemslist;
    items_list_in_an_order_adapter items_list_in_an_order_Adapter;

    TextView TxtBankName,tv_orderugentstatus;
    CardView CardBankName;
    String CashDepositedBankName;
    String OrderStatus;
    public static int CashDepositedBankId = 0;
    CompanyWiseBanksModel bankdetailsList;

    int OrderId;

    TextView orderNo;

    SharedPrefs prefs;
    String Delivery_id, IsNew, Discount_percentage;
    ListView menuList;
    ImageButton btnMenu;
    NumberPicker np;
    CheckBox cb;

    List<DeliveryItemModel> orderitemsList;
    RadioGroup rdGroup;
    String designation;

    //Sales Modes...
    RadioButton rdCash, rdCredit;
    AlarmManager alarm;
    PendingIntent pintent;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pod__dash_board);




        orderitemsList = new ArrayList<>();
        if (getActionBar() != null) {
            setUpActionBar(getSupportActionBar());
        }



        InitilizeAlaram();

        designation =  new SharedPrefs(POD_DashBoard.this).getDesignation();

        orderNo = findViewById(R.id.orderNo);

        Order_addressTV = findViewById(R.id.addressTV);
        Order_To_nameTV = findViewById(R.id.order_To);


        llSubDistView = findViewById(R.id.llSubDistView);
        distributorName = findViewById(R.id.distributorName);
        subDistributorName = findViewById(R.id.subDistributorName);

        rdGroup = findViewById(R.id.rdGroup);
        rdCash = findViewById(R.id.rdCash);
        rdCredit = findViewById(R.id.rdCredit);
        TxtBankName = findViewById(R.id.txtBankName);
        CardBankName = findViewById(R.id.cardBankName);
        tv_orderugentstatus = findViewById(R.id.tv_orderugentstatus);

        OrderSatusTV = findViewById(R.id.statusTV);
        ServerOrderSatusTV = findViewById(R.id.server_statusTV);
        Order_timeTV = findViewById(R.id.dilevery_collection_time);
        InstructionTV = findViewById(R.id.insrection);
        totalbill = findViewById(R.id.totalbill);
        perdiscount = findViewById(R.id.perdiscount);
        net_total = findViewById(R.id.net_total);
        latlngTV = findViewById(R.id.podlatlng);
        itemBtn = findViewById(R.id.itemBtn);
        noteBtn = findViewById(R.id.user_note);
        recevied_by = findViewById(R.id.received_by);
        discount_btn = findViewById(R.id.discount_btn);
        btn_view_order_details = findViewById(R.id.btn_view_order_details);
        captureImgBtn = findViewById(R.id.cameraImgBtn);
        backImgBtn = findViewById(R.id.backImgBtn);
        sendPodImgBtn = findViewById(R.id.okbImgBtn);
        rejectImgBtn = findViewById(R.id.cancleImgBtn);
        callImgBtn = findViewById(R.id.callImgBtn);
        MapImgBtn = findViewById(R.id.mapImgBtn);
        cb = findViewById(R.id.currentlacotionPodCB);
        tvGSt = findViewById(R.id.tvgst);


        db = new ZEDTrackDB(POD_DashBoard.this);
        deliverInfo = new DeliveryInfo();
        prefs = new SharedPrefs(this);
        itemBtn.setOnClickListener(this);
        captureImgBtn.setOnClickListener(this);
        backImgBtn.setOnClickListener(this);
        sendPodImgBtn.setOnClickListener(this);
        rejectImgBtn.setOnClickListener(this);
        callImgBtn.setOnClickListener(this);
        MapImgBtn.setOnClickListener(this);
        noteBtn.setOnClickListener(this);
        recevied_by.setOnClickListener(this);
        discount_btn.setOnClickListener(this);



        //DelieveryId of an an order...
        Delivery_id = getIntent().getStringExtra("Delivery_id");
        CashDepositedBankId = getIntent().getIntExtra("BankId",0);

        gLoadItemsList();

        btn_view_order_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(POD_DashBoard.this);

                dialog.setTitle("Order Items Information");
                Window window = dialog.getWindow();
                window.setGravity(Gravity.TOP);

                //For Removing Background of Popup Dialogue...
                dialog.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
                dialog.setContentView(R.layout.pod_custom_single_order_view);

                rv_itemslist = dialog.findViewById(R.id.rv_itemslist);
                btn_dismiss_items_dialogue = dialog.findViewById(R.id.btn_dismiss_view_order_dialogue);

                BtnDismissDialogueTrigger(dialog);

                LinearLayoutManager mLayoutManager = new LinearLayoutManager(dialog.getContext());
                rv_itemslist.setLayoutManager(mLayoutManager);

                items_list_in_an_order_Adapter = new items_list_in_an_order_adapter( orderitemsList,POD_DashBoard.this);
                if(orderitemsList.size()>0){
                    rv_itemslist.setAdapter(items_list_in_an_order_Adapter);
                }
                else{
                    Toast.makeText(POD_DashBoard.this, "No Data Found in item", Toast.LENGTH_SHORT).show();
                }

                dialog.show();
            }
        });


        IsNew = getIntent().getStringExtra("IsNew");
        Utility.logCatMsg("Delivery_id :" + Delivery_id);

        Utility.logCatMsg("IsNew :" + IsNew);
        if (IsNew.equals("False")) {
            cb.setVisibility(View.VISIBLE);
        }
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb.isChecked()) {
                    GPSTracker gps = new GPSTracker(POD_DashBoard.this);
                    if (gps.canGetLocation()) {
                        Timber.d("Location getted");

                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();


                        if (latitude > 0.0) {
                            latlngTV.setText("Your POD lat lng is " + latitude + " , " + longitude);
                            deliverInfo.setPod_lng(longitude + "");
                            deliverInfo.setPod_lat(latitude + "");

                            db.UpdatePODLatLng(deliverInfo);
                            cb.setVisibility(View.GONE);
                        } else {
                            latlngTV.setText("Please try again.");
                        }
                    } else {
                        Timber.d("Cant be able to get location");
                        gps.showSettingsAlert();
                    }
                    cb.setChecked(false);
                }
            }
        });


    }



    private void BtnDismissDialogueTrigger(Dialog dialog) {

        btn_dismiss_items_dialogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }


    private void gLoadItemsList() {
        orderitemsList = db.grabOrderItems(Integer.parseInt(Delivery_id));
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

    private void Reject_Order_diloag() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view1 = getLayoutInflater().inflate(R.layout.dialog_custom_title, null);
        TextView tvCustomTitle = view1.findViewById(R.id.tvCustomTitle);
        tvCustomTitle.setText("Select Reason");
        builder.setCustomTitle(view1);
        builder.setItems(rejected_items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (rejected_items[item].equals("Cancel")) {
                    dialog.dismiss();
                } else if (rejected_items[item].equals("Restore")) {
                    if (deliverInfo.getDelivery_status().equals("Rejected")) {
                        deliverInfo.setDelivery_status("Inprogress");
                        deliverInfo.setRejected_Reason("");
                        deliverInfo.setRefused_Reason("");
                        deliverInfo.setCancelledReason("");
                        int j = db.UpdateOrderStatus(deliverInfo);
                        if (j == 1) {
                            Utility.logCatMsg("Record Updated in SQLite");
                            Toast.makeText(POD_DashBoard.this, "Order Restore Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Utility.logCatMsg("Record Not Updated in SQLite");
                        }
                        FillOrderDetails();
                    }
                } else {
                    deliverInfo.setRejected_Reason(rejected_items[item].toString());
                    deliverInfo.setRefused_Reason("");
                    deliverInfo.setCancelledReason("");
                    deliverInfo.setDelivery_status("Rejected");
                    int j = db.UpdateOrderStatus(deliverInfo);
                    if (j == 1) {
                        Utility.logCatMsg("Record Updated in SQLite");
                        Toast.makeText(POD_DashBoard.this, "Order Rejected Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Utility.logCatMsg("Record Not Updated in SQLite");
                    }
                    FillOrderDetails();
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void Phone_Action_diloag() {
        CharSequence[] phone_action_items = {"Call +" + deliverInfo.getDelivery_to_mobile(), "Text +" + deliverInfo.getDelivery_to_mobile(), "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view1 = getLayoutInflater().inflate(R.layout.dialog_custom_title, null);
        TextView tvCustomTitle = view1.findViewById(R.id.tvCustomTitle);
        tvCustomTitle.setText("Select Phone Action");
        builder.setCustomTitle(view1);
        builder.setItems(phone_action_items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                    if (item == 0) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + deliverInfo.getDelivery_to_mobile()));
                        if (ActivityCompat.checkSelfPermission(POD_DashBoard.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        startActivity(intent);
                    }
                    if (item == 1) {
                        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                        sendIntent.setData(Uri.parse("sms:" + deliverInfo.getDelivery_to_mobile()));
                        startActivity(sendIntent);
                    }

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cameraImgBtn:
                Intent in = new Intent(POD_DashBoard.this, AddImages.class);
                in.putExtra("OrderId", deliverInfo.getDelivery_id() + "");
                if (IsNew.toString().equals("True"))
                    in.putExtra("imgType", "POB");
                else
                    in.putExtra("imgType", "POD");
                startActivity(in);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            case R.id.okbImgBtn:

                if (deliverInfo.getIsPod_sync().toString().equals("2")) {
                    if (deliverInfo.getDelivery_status().equals("Delivered") && !IsNew.equals("Return"))
                        Utility.Toast(POD_DashBoard.this, "Order is already sent with Delivered status");
                    else if (deliverInfo.getDelivery_status().equals("Returned"))
                        Utility.Toast(POD_DashBoard.this, "Order is already sent with Return status");
                    else if (deliverInfo.getDelivery_status().equals("Booking") && IsNew.equals("True")){
                        deliverInfo.setResync(true);
                        saveOrdertoServerDiloag();
                    }
//                        Utility.Toast(POD_DashBoard.this, "Order is already sent with Booking status");
                } else {
                    if (deliverInfo.getDelivery_status().equals("Booking") && IsNew.equals("True")) {
                        Utility.logCatMsg("Is Sync" + deliverInfo.getIsPod_sync());
                        SelectListActionDiloag();
                    } else {
                        deliverDlilog();
                    }
                }
                break;
            case R.id.cancleImgBtn:
                if (deliverInfo.getDelivery_status().equals("Delivered")) {
                    Utility.Toast(POD_DashBoard.this, "Cannot do this action ");
                } else if (deliverInfo.getDelivery_status().equals("Returned")) {
                    Utility.Toast(POD_DashBoard.this, "Cannot do this action ");
                } else if (deliverInfo.getIsPod_sync().toString().equals("2")) {
                    Utility.Toast(POD_DashBoard.this, "Cannot do this action because order is sent");
                } else {
                    Reject_Order_diloag();
                }
                break;
            case R.id.backImgBtn:
                onBackPressed();
                break;
            case R.id.callImgBtn:
                int PERMISSION_ALL = 1;
                String[] PERMISSIONS = {Manifest.permission.CALL_PHONE};

                if(!hasPermissions(this, PERMISSIONS)){
                    ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
                }

                if(deliverInfo.getDelivery_to_mobile().length()>7 &&
                        deliverInfo.getDelivery_to_mobile().length()<=12
                ){
                    Phone_Action_diloag();
                }else
                {
                    Toast.makeText(POD_DashBoard.this, "Customer Phone is not Available", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.mapImgBtn:

                Log.d("orderCoords", deliverInfo.getDeliver_lat() + "   " + deliverInfo.getDeliver_lng());
                Log.d("directionUrl","http://maps.google.com/maps?daddr=34.0140299,71.5678618");
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr="+deliverInfo.getDeliver_lat()+","+deliverInfo.getDeliver_lng()));
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            case R.id.user_note:
                if (deliverInfo.getIsPod_sync().toString().equals("2"))
                    Utility.Toast(POD_DashBoard.this, "Cannot do this action because order is sent");
                else
                    UserNoteDiloge();

                break;
            case R.id.received_by:
                if (deliverInfo.getIsPod_sync().toString().equals("2"))
                    Utility.Toast(POD_DashBoard.this, "Cannot do this action because order is sent");
                else
                    ReciverPersonDiloge();

                break;
            case R.id.discount_btn:
                if (deliverInfo.getIsPod_sync().equals("2") && !IsNew.equals("Return")) {
                    Utility.Toast(POD_DashBoard.this, "Cannot change discount because order is sent");
                } else if (deliverInfo.getDelivery_status().equals("Returned")) {
                    Utility.Toast(POD_DashBoard.this, "Cannot change discount because order is Return");
                } else
                    SelectDiscountDiloge();
                break;
            case R.id.itemBtn: {

                int distId = db.GetDistributerId(deliverInfo.getServer_Delivery_id());
                int subdistId = db.GetSubDistributerId(deliverInfo.getServer_Delivery_id());

                deliverInfo.setDistributorId(distId);
                deliverInfo.setSubDistributorId(subdistId);

                if ((deliverInfo.getDelivery_status().equals("Delivered") ||
                        deliverInfo.getDelivery_status().equals("Booking")) && deliverInfo.getIsPod_sync().toString().equals("2")){

                    Toast.makeText(this, "Order already sent", Toast.LENGTH_SHORT).show();
                }
                else{

                    if (new SharedPrefs(POD_DashBoard.this).getView().equals("secondView")) {
                        Timber.d("Update case is SecondView ==> "+new SharedPrefs(POD_DashBoard.this).getView()+" and isNew is "+IsNew);


                        Intent intent1 = new Intent(POD_DashBoard.this, SelectProductActivitySecond.class);
                        intent1.putExtra("addOrder","update");
                        intent1.putExtra("deliverInfo",deliverInfo);
                        intent1.putExtra("isNew",IsNew);
                        startActivity(intent1);

                    }
                    else{

                        Intent intent1 = new Intent(POD_DashBoard.this, SelectProductActivity.class);
                        intent1.putExtra("addOrder","update");
                        intent1.putExtra("deliverInfo",deliverInfo);
                        intent1.putExtra("isNew",IsNew);
                        startActivity(intent1);
                    }
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }

            }
            break;
            default:
                break;
        }
    }

    private void saveOrdertoServerDiloag() {

        AlertDialog.Builder builder = new AlertDialog.Builder(POD_DashBoard.this);
        String title = "Save Order?";
        String massage = "Save Order to server with Booking status & POBs ?";
        builder.setTitle(title)
                .setMessage(massage)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (ConnectionDetector.isConnectingToInternet(POD_DashBoard.this)) {
                            deliverInfo.setRejected_Reason("");
                            deliverInfo.setRefused_Reason("");
                            deliverInfo.setCancelledReason("");
                            if (deliverInfo.getDelivery_status().equals("Booking")) {

//                                int j = db.UpdateOrderStatus(deliverInfo);
//                                db.UpdateSynChanges(deliverInfo.getDelivery_id());


                                ArrayList<String> deliveryIdList = new ArrayList<String>();

                                // Sending DelieveryId and BankId towards SendPod class for using inside hashmap...
                                deliveryIdList.add(Delivery_id);

                                SendPod.RecieveCashDepositedBankId = CashDepositedBankId;
                                SendPod.resyncStatus = deliverInfo.isResync();

                                SendPod sp = new SendPod(POD_DashBoard.this, IsNew, deliveryIdList);
                                sp.SaveChangesToDataBase();

                            }
                            dialog.cancel();
                            // onResume();
                        } else {
                            Utility.Toast(POD_DashBoard.this, "Check network connection and try again");
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deliverDlilog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(POD_DashBoard.this);
        String massage = "Save Order to server with " + deliverInfo.getDelivery_status() + " status and PODs ?";
        if (deliverInfo.getDelivery_status().equals("Booking")) {
            deliverInfo.setDelivery_status("Delivered");
            massage = "Save Order to server with " + deliverInfo.getDelivery_status() + " status PODs & POBs ?";
        } else if (deliverInfo.getDelivery_status().equals("Inprogress")) {
            deliverInfo.setDelivery_status("Delivered");
            massage = "Save Order to server with " + deliverInfo.getDelivery_status() + " status & PODs?";
        }

        View view1 = getLayoutInflater().inflate(R.layout.dialog_custom_title, null);
        TextView tvCustomTitle = view1.findViewById(R.id.tvCustomTitle);
        tvCustomTitle.setText("Save Order");
        builder.setCustomTitle(view1);
        builder.setMessage(massage)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (ConnectionDetector.isConnectingToInternet(POD_DashBoard.this)) {


                            SendPod.RecieveCashDepositedBankId = CashDepositedBankId;

                            deliverInfo.setRejected_Reason("");
                            deliverInfo.setRefused_Reason("");
                            deliverInfo.setCancelledReason("");
                            ArrayList<String> deliveryIdList = new ArrayList<String>();
                            deliveryIdList.add(Delivery_id);
                            Log.d("deliverid", IsNew);
                            Log.d("deliveryst","="+ deliverInfo.getDelivery_status());

//                            if(deliverInfo.getDelivery_status().equals("Returned")){
//                                //todo send returned order
//                                Log.d("sendOrder", "return");
//                                SendReturnOrder sendReturnOrder = new SendReturnOrder(POD_DashBoard.this,Integer.parseInt(Delivery_id));
//                                sendReturnOrder.addNewSalesReturn(Utility.BASE_SALES_RETURN_URL+"api/SalesReturn/Create");
//                                Log.d("return JsonObject", sendReturnOrder.convertToJsonArray(Integer.parseInt(Delivery_id)).toString());
//                            }
//                            else{
                                db.UpdateOrderStatus(deliverInfo);
                                Log.d("sendOrder", "order");
                                SendPod sp = new SendPod(POD_DashBoard.this, IsNew, deliveryIdList);
                                sp.SaveChangesToDataBase();
//                            }

                            dialog.cancel();
                            // onResume();
                        } else {
                            Utility.Toast(POD_DashBoard.this, getString(R.string.networkErrorMsg));
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void SelectListActionDiloag() {
      /*  final CharSequence[] item = {"Booking", "Delivered"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Status");
        builder.setItems(item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    saveOrdertoServerDiloag();
                } else if (which == 1) {
                    deliverDlilog();
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();*/
        
        if (deliverInfo.getDelivery_status().equals("Booking"))
            saveOrdertoServerDiloag();
        else if (deliverInfo.getDelivery_status().equals("Delivered"))
            deliverDlilog();
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
        title.setText("Delivery Details");
    }

    @Override
    protected void onResume() {
        FillOrderDetails();
        Utility.logCatMsg("OnResume method calling");

        if (deliverInfo.getSalemode().equals("Cash")) {

            ((RadioButton) rdGroup.getChildAt(0)).setChecked(true);
            rdCredit.setEnabled(false);

            //Bank Work By Muhammad Yaseen...
            if(CashDepositedBankId!=0)
            {
                CardBankName.setVisibility(View.VISIBLE);
            }
            else
            {
                CardBankName.setVisibility(View.GONE);
            }



        } else {
            //Bank work
            ((RadioButton) rdGroup.getChildAt(1)).setChecked(true);
            rdCash.setEnabled(false);
            CardBankName.setVisibility(View.GONE);
        }
        if (!deliverInfo.getIsPod_sync().equals("1")) {
            rdCredit.setEnabled(false);
            rdCash.setEnabled(false);
        } else {
            rdCredit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        deliverInfo.setSalemode("Credit");
                        db.updateSaleMode(deliverInfo);
                    }
                }
            });

            rdCash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        deliverInfo.setSalemode("Cash");
                        db.updateSaleMode(deliverInfo);
                    }
                }
            });
        }
        super.onResume();
    }



    private void UserNoteDiloge() {
        View view = getLayoutInflater().inflate(R.layout.add_new_note, null);
        final EditText userText = view.findViewById(R.id.usernote);
        if (deliverInfo.getNote() != null)
            userText.setText(deliverInfo.getNote());
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(POD_DashBoard.this);
        View view1 = getLayoutInflater().inflate(R.layout.dialog_custom_title, null);
        TextView tvCustomTitle = view1.findViewById(R.id.tvCustomTitle);
        tvCustomTitle.setText("Add Note");
        alertDialogBuilder.setCustomTitle(view1);
        alertDialogBuilder
                .setCancelable(false).setView(view)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (deliverInfo.getDelivery_status().equals("Delivered") && !IsNew.equals("Return")) {
                            Utility.Toast(POD_DashBoard.this, "Cannot change note because order is Delivered");
                        } else if (deliverInfo.getDelivery_status().equals("Returned"))
                            Utility.Toast(POD_DashBoard.this, "Cannot change note because order is Returned");
                        else {
                            if (!userText.getText().equals("")) {
                                deliverInfo.setNote(userText.getText().toString());
                                if (db.UpdateNote(deliverInfo) == 1)
                                    Utility.logCatMsg("Note updated successfully in SQLite");
                            }
                        }
                        dialog.dismiss();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void ReciverPersonDiloge() {
        View view = getLayoutInflater().inflate(R.layout.receviedby_layout, null);
        final EditText receverText = view.findViewById(R.id.recevername);
        if (deliverInfo.getReceivedBy() != null) {
            receverText.setText(deliverInfo.getReceivedBy());
            Utility.logCatMsg("Received By" + deliverInfo.getReceivedBy());
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(POD_DashBoard.this);
        View view1 = getLayoutInflater().inflate(R.layout.dialog_custom_title, null);
        TextView tvCustomTitle = view1.findViewById(R.id.tvCustomTitle);
        tvCustomTitle.setText("Receiver Name");
        alertDialogBuilder.setCustomTitle(view1);
        alertDialogBuilder
                .setCancelable(false)
                .setView(view)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (deliverInfo.getDelivery_status().equals("Delivered") && !IsNew.equals("Return")) {
                            if (!receverText.getText().equals(""))
                                Utility.Toast(POD_DashBoard.this, "Cannot change receiver name because order is Delivered");
                        } else if (deliverInfo.getDelivery_status().equals("Returned")) {
                            if (!receverText.getText().equals(""))
                                Utility.Toast(POD_DashBoard.this, "Cannot change receiver name because order is Returned");
                        } else {
                            if (!receverText.getText().equals("")) {
                                deliverInfo.setReceivedBy(receverText.getText().toString());
                                if (db.UpdateRecivedBy(deliverInfo) == 1) {
                                    Utility.logCatMsg("ReceiverPerson updated successfully in SQLite");
                                    Utility.Toast(POD_DashBoard.this, "Saved..");
                                }
                            } else
                                Utility.Toast(POD_DashBoard.this, "Wrong Input");
                        }
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





    private void FillOrderDetails() {
        deliverInfo = db.getSelectedSQLiteOrderDelivery(Integer.parseInt(Delivery_id), IsNew);


        bankdetailsList = db.getDelieveryOrderBankInfo(CashDepositedBankId);
        CashDepositedBankName = bankdetailsList.getBankName();
        TxtBankName.setText(CashDepositedBankName);



        OrderId = deliverInfo.getDelivery_id();


        OrderStatus = deliverInfo.getIsUrgentOrderStatus()+"";

        if(OrderStatus.equalsIgnoreCase("urgent"))
        {
            tv_orderugentstatus.setText("Urgent Order");
        }else{
            tv_orderugentstatus.setText("Normal Order");
        }


        Log.d("DeliveryInfo", deliverInfo.getDelivery_status() + "");

        String distName = db.getDistributorName(deliverInfo.getDistributorId());
        String subDistName = db.getSubDistributorName(deliverInfo.getSubDistributorId());

        Timber.d("The dis "+distName+" and sub dist "+subDistName);


        distributorName.setText(distName);

        if(deliverInfo.getSubDistributorId()>0){
            subDistributorName.setText(subDistName);
        }else{
            llSubDistView.setVisibility(View.GONE);
        }


        Order_addressTV.setText(deliverInfo.getDelivery_address());
        Order_To_nameTV.setText(deliverInfo.getDeliver_to_name());
        orderNo.setText(deliverInfo.getOrderNumber());



        if(designation.equalsIgnoreCase("Order Booker")){
            totalbill.setText("--");
        }else{
            totalbill.setText(deliverInfo.getTotal_Bill() + " "+prefs.getCurrency());
        }


        if(deliverInfo.getGst() != null && deliverInfo.getNetTotal() != null)
        {
            float gstper = ( Float.parseFloat(deliverInfo.getGst()) / Float.parseFloat( deliverInfo.getNetTotal())) * 100;
//            tvGSt.setText(gstper + " % = " + deliverInfo.getGst() + " "+prefs.getCurrency());

            float tempgst =  Float.parseFloat(deliverInfo.getGst());

            Float FixingDecimal = (float) Double.parseDouble(new DecimalFormat("##.##").format(tempgst));

            if(designation.equalsIgnoreCase("Order Booker")){
                tvGSt.setText("--");
            }else{
                tvGSt.setText(FixingDecimal + " "+prefs.getCurrency());
            }

        }


        if(designation.equalsIgnoreCase("Order Booker")){
            perdiscount.setText("--");
        }else {
            perdiscount.setText(deliverInfo.getPercentageDiscount() + "% = " + deliverInfo.getDiscount() + " "+prefs.getCurrency());
        }



        if(designation.equalsIgnoreCase("Order Booker")){
            net_total.setText("--");
        }else{
            net_total.setText((Float.parseFloat(deliverInfo.getNetTotal())
                    - Float.parseFloat(deliverInfo.getDiscount()))
                    +" "+new SharedPrefs(this).getCurrency());
        }


        if (IsNew.equals("False")) {
            Order_timeTV.setText(deliverInfo.getDelivery_start_time() + " TO " + deliverInfo.getDelivery_end_time());
        } else {
            Order_timeTV.setText("");
        }
        if (deliverInfo.getDelivery_status().equals("Rejected")) {
            OrderSatusTV.setTextColor(Color.RED);
        }
        if (deliverInfo.getIsPod_sync().toString().equals("1")) {
            ServerOrderSatusTV.setVisibility(View.VISIBLE);
            ServerOrderSatusTV.setTextColor(Color.parseColor("#E55B3C"));
            if (IsNew.equals("True"))
                ServerOrderSatusTV.setText("Order Not Send");
            else
                ServerOrderSatusTV.setText("POD Not Send");
        } else if (deliverInfo.getIsPod_sync().toString().equals("2")) {
            ServerOrderSatusTV.setVisibility(View.VISIBLE);
            ServerOrderSatusTV.setTextColor(Color.parseColor("#728C00")); //green color
            if (IsNew.equals("True"))
                ServerOrderSatusTV.setText("Order Sent");
            else
                ServerOrderSatusTV.setText("POD Sent");
        } else {
            ServerOrderSatusTV.setVisibility(View.GONE);
        }
        OrderSatusTV.setText(deliverInfo.getDelivery_status());
        InstructionTV.setText(deliverInfo.getDelivery_description());
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void SelectDiscountDiloge() {
        View view = getLayoutInflater().inflate(R.layout.select_percentage, null);
        np = view.findViewById(R.id.number_picker);
        np.setMinValue(0);
        np.setMaxValue(100);
        np.setWrapSelectorWheel(true);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(POD_DashBoard.this);
        View view1 = getLayoutInflater().inflate(R.layout.dialog_custom_title, null);
        TextView tvCustomTitle = view1.findViewById(R.id.tvCustomTitle);
        tvCustomTitle.setText("Select Percentage");
        alertDialogBuilder.setCustomTitle(view1);
        alertDialogBuilder
                .setCancelable(false)
                .setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        float netTotal;
                        Discount_percentage = np.getValue() + "";
                        float discountPercentage = Float.parseFloat(Discount_percentage.trim());
                        float totalBill = Float.parseFloat(deliverInfo.getNetTotal().trim());
                        float discountValue = (discountPercentage / 100) * totalBill;

                        netTotal = totalBill - discountValue;

                        if(designation.equalsIgnoreCase("Order Booker")){
                            perdiscount.setText("--");
                        }else{
                            perdiscount.setText(Discount_percentage + " % = " + discountValue + " "+prefs.getCurrency());
                        }


                        if(designation.equalsIgnoreCase("Order Booker")){
                            net_total.setText("--");
                        }else{
                            net_total.setText(netTotal + " "+prefs.getCurrency());
                        }


                        deliverInfo.setPercentageDiscount(Discount_percentage);
                        deliverInfo.setDiscount(discountValue + "");
                        if (db.UpdatePercentageBill(deliverInfo) == 1) {
                            Utility.logCatMsg("Note updated successfully in SQLite");
                        }
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
}




