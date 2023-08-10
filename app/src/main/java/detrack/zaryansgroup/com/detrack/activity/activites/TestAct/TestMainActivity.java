package detrack.zaryansgroup.com.detrack.activity.activites.TestAct;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
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
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import detrack.zaryansgroup.com.detrack.activity.Adapter.DeliveryListAdapter;
import detrack.zaryansgroup.com.detrack.activity.Map.GPSTracker;
import detrack.zaryansgroup.com.detrack.activity.Model.DeliveryInfo;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.DeliveryItemModel;
import detrack.zaryansgroup.com.detrack.activity.Model.Params;
import detrack.zaryansgroup.com.detrack.activity.SQLlite.ZEDTrackDB;
import detrack.zaryansgroup.com.detrack.activity.Service.CompanyInfoService;
import detrack.zaryansgroup.com.detrack.activity.Service.GPSService;
import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;
import detrack.zaryansgroup.com.detrack.activity.activites.AddImages;
import detrack.zaryansgroup.com.detrack.activity.activites.NewUserActivity;
import detrack.zaryansgroup.com.detrack.activity.activites.POD_DashBoard;
import detrack.zaryansgroup.com.detrack.activity.activites.ReturnOrderSearchActivity;
import detrack.zaryansgroup.com.detrack.activity.activites.SettingActivity;
import detrack.zaryansgroup.com.detrack.activity.activites.TabActivity;
import detrack.zaryansgroup.com.detrack.activity.activites.TakeOrder;
import detrack.zaryansgroup.com.detrack.activity.activites.WelcomeActivity;
import detrack.zaryansgroup.com.detrack.activity.utilites.ConnectionDetector;
import detrack.zaryansgroup.com.detrack.activity.utilites.SendPod;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import detrack.zaryansgroup.com.detrack.R;

public class TestMainActivity extends AppCompatActivity implements TabActivity.SendMultipleSelectionEventToPlaindOrderActivity {

    ListView dilervyJobListView;
//    menuList;
    private static DeliveryListAdapter listAdapter;
    ArrayList<DeliveryInfo> feedItems, filteredList;
    ArrayList<DeliveryItemModel> feedDeliveryItems;
    TextView demotextTV, selectAllTV;
    EditText mSearchEt;
    String query, allDelivery_id;
    SharedPrefs prefs;
    String menuListArray[] = {"Updated jobs", "Send Track ID", "Start Service", "Stop Service"};
    CharSequence optionlist[] = {"POD", "Deliver order", "Reject order", "Cancel order", "Refuse order", "Shop close", "Send order", "Cancel"};
    ZEDTrackDB db;
    boolean Alert_dilog_flag = false;
    CheckBox cb;
    PendingIntent pintent;
    AlarmManager alarm;
    static ArrayList<DeliveryInfo> list;
    LinearLayout multipleSelectionLL;
    ImageView cancelIV, deleteIV, sendIV;
    private ProgressDialog newOrderFileDilog;
//    TextView actionbar;
    ImageButton btnMenu;
    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_mainactivity);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.headercolor));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.headercolor));
        }
//        setUpActionBar(getSupportActionBar());
//        actionbar.setText("Assigned Order");
        gps = new GPSTracker(this);
        dilervyJobListView = findViewById(R.id.divery_job_list);
//        menuList = findViewById(R.id.menuList);
//        menuList.setAdapter(new ArrayAdapter<>(this, R.layout.simple_text_for_menu, menuListArray));
        feedItems = new ArrayList<>();
        filteredList = new ArrayList<>();
        feedDeliveryItems = new ArrayList<>();
        demotextTV = findViewById(R.id.demotextTV);
        selectAllTV = findViewById(R.id.selectAllTV);
        mSearchEt = findViewById(R.id.serachET);
        cb = findViewById(R.id.checkBox);
        prefs = new SharedPrefs(this);
        db = new ZEDTrackDB(TestMainActivity.this);
        multipleSelectionLL = findViewById(R.id.multipleSelectionLL);
        cancelIV = findViewById(R.id.cancelIV);
        deleteIV = findViewById(R.id.deleteIV);
        sendIV = findViewById(R.id.sendIV);
        newOrderFileDilog = new ProgressDialog(TestMainActivity.this);

        TabActivity.setSendMultipleSelectionEventToPlainOrderActivity(this);
        selectAllTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectAllTV.getText().toString().equals("Select All")) {
                    selectAllTV.setText("UnSelect All");
                    for (int i = 0; i < list.size(); i++)
                        list.get(i).setIsCBChecked(true);
                } else {
                    selectAllTV.setText("Select All");
                    for (int i = 0; i < list.size(); i++)
                        list.get(i).setIsCBChecked(false);
                }
                listAdapter.notifyDataSetChanged();
            }
        });
        dilervyJobListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox cb = parent.findViewById(R.id.checkBox);
                if (cb.getVisibility() == View.VISIBLE) {
                    if (list.get(position).isCBChecked()) {
                        list.get(position).setIsCBChecked(false);
                    } else {
                        list.get(position).setIsCBChecked(true);
                    }
                    listAdapter.notifyDataSetChanged();
                } else {
                    DeliveryInfo model = (DeliveryInfo) parent.getItemAtPosition(position);
                    if (list.get(position).getIsOrderRead() == 0 || list.get(position).getIsNewUpdate() == 1) {
                        list.get(position).setIsOrderRead(1);
                        list.get(position).setIsNewUpdate(0);
                        if (db.UpdateIsReadStatus(model) == 1) {
                            Utility.logCatMsg("Read Status Changed");
                            listAdapter.notifyDataSetChanged();
                        }
                    }
                    Intent intent = new Intent(TestMainActivity.this, POD_DashBoard.class);
                    Log.d("delivery_id", model.getServer_Delivery_id() + "    " + model.getDelivery_id());
                    intent.putExtra("Delivery_id", model.getDelivery_id() + "");
                    intent.putExtra("IsNew", "False");
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            }
        });
        dilervyJobListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                Select_Action_diloag(pos);
                return true;
            }
        });
        cancelIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multipleSelectionLL.setVisibility(View.GONE);
                mSearchEt.setVisibility(View.VISIBLE);
                listAdapter.isChecked(false);
                listAdapter.notifyDataSetChanged();
            }
        });
        deleteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean check = false;
                DeliveryInfo check_model = new DeliveryInfo();
                for (int i = 0; i < list.size(); i++) {
                    check_model = list.get(i);
                    if (check_model.isCBChecked()) {
                        check = true;
                        break;
                    }
                }
                if (check) {
                    Utility.Toast(TestMainActivity.this, "Planned Order can't be delete");
                    multipleSelectionLL.setVisibility(View.GONE);
                    mSearchEt.setVisibility(View.VISIBLE);
                    listAdapter.isChecked(false);
                    listAdapter.notifyDataSetChanged();
                }
            }
        });
        sendIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean check = false;
                DeliveryInfo check_model = new DeliveryInfo();
                for (int i = 0; i < list.size(); i++) {
                    check_model = list.get(i);
                    if (check_model.isCBChecked()) {
                        check = true;
                        break;
                    }
                }
                if (check)
                    saveOrdertoServerDiloag("Delivered");
            }
        });
        openSearchBar();
        InitilizeAlaram();
    /*    String searchText = mSearchEt.getText().toString();
        mSearchEt.setText(searchText);
        openSearchBar();
*/
    }

    private void saveOrdertoServerDiloag(final String status) {
        AlertDialog.Builder builder = new AlertDialog.Builder(TestMainActivity.this);
        String title = "Send Order?", massage = "";
        massage = "Send Order to server with " + status + " status.";
        builder.setTitle(title)
                .setMessage(massage)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (ConnectionDetector.isConnectingToInternet(TestMainActivity.this)) {
                            DeliveryInfo deliverInfo = new DeliveryInfo();
                            ArrayList<String> deliveryIdList = new ArrayList<String>();
                            Utility.logCatMsg("List size " + list.size());
                            for (int i = 0; i < list.size(); i++) {
                                deliverInfo = list.get(i);
                                if (deliverInfo.isCBChecked()) {
                                    deliveryIdList.add(deliverInfo.getDelivery_id() + "");
                                    Utility.logCatMsg("Delivery ID " + deliverInfo.getDelivery_id());
                                }
                            }
                            Utility.logCatMsg("Deliver id summeryList " + deliveryIdList.size());
                            SendPod sp = new SendPod(TestMainActivity.this, "False", deliveryIdList);
                            sp.SaveChangesToDataBase();
                            dialog.cancel();
                            onResume();
                        } else {
                            Utility.Toast(TestMainActivity.this, "Check network connection and try again");
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

    @Override
    protected void onResume() {
        mSearchEt.setVisibility(View.VISIBLE);
        multipleSelectionLL.setVisibility(View.GONE);
        String searchText = mSearchEt.getText().toString();
        if (!searchText.matches("")) {
            mSearchEt.setText(searchText);
            openSearchBar();
        }
        Fill_listtView_From_SqliteDB();
        super.onResume();
    }

    private void openSearchBar() {
        mSearchEt.addTextChangedListener(new SearchWatcher());
        mSearchEt.setHint("Search....");
//        mSearchEt.setHintTextColor(Color.WHITE);
    }

    @Override
    public void MultipleSelectionEventToPlaindOrderActivity() {
        if (list.size() > 0) {
            multipleSelectionLL.setVisibility(View.VISIBLE);
            mSearchEt.setVisibility(View.GONE);
            listAdapter.isChecked(true);
            listAdapter.notifyDataSetChanged();
        }
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
            query = mSearchEt.getText().toString();
            filteredList = performSearch(list, query);
            dilervyJobListView.setAdapter(new DeliveryListAdapter(TestMainActivity.this, filteredList));
        }
    }

    private ArrayList<DeliveryInfo> performSearch(ArrayList<DeliveryInfo> modal, String query) {
        String[] queryByWords = query.toLowerCase().split("\\s+");
        ArrayList<DeliveryInfo> filter = new ArrayList<>();
        if(modal == null ) return filter;
        for (int i = 0; i < modal.size(); i++) {
            DeliveryInfo data = modal.get(i);
            String name = data.getDeliver_to_name().toLowerCase();
            Utility.logCatMsg("Search query :" + name);
            for (String word : queryByWords) {
                int numberOfMatches = queryByWords.length;
                if (name.contains(word)) {
                    numberOfMatches--;
                    Utility.logCatMsg("Match " + name + " " + word);
                } else
                    break;
                if (numberOfMatches == 0) {
                    filter.add(data);
                }
            }
        }
        return filter;
    }

    //getTodayJob new api implemented
    public void getTodayJob(String url) {

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("jobResponse", response);
                try {
                    JSONObject parentObject = new JSONObject(response);
                    JSONArray tableArray = parentObject.getJSONArray("Table");
                    if (tableArray.length() > 0) {
                        demotextTV.setVisibility(View.GONE);
                        parseJsonFeed1(tableArray);

                        getJobItem("http://deliveryapi.zederp.net/api/Job/GetTodayJobItems?Delivery_id=" + allDelivery_id);
                        Log.d("itemUrl", "http://deliveryapi.zederp.net/api/Job/GetTodayJobItems?Delivery_id=" + allDelivery_id);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("jobError", error.getMessage() + "");
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    //getJobItems new api implemented
    public void getJobItem(String url) {
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("jobItmResponse", response);
                try {
                    JSONObject parentObject = new JSONObject(response);
                    JSONArray tableArray = parentObject.getJSONArray("Table");
                    if (tableArray.length() > 0) {
                        parseJsonFeedItems(tableArray);
                    } else {
                        Toast.makeText(TestMainActivity.this, "No items found", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("jobItmError", error.getMessage() + "");
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }

    private void parseJsonFeed1(JSONArray feedArray) {
        try {
            feedItems.clear();
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                DeliveryInfo model = new DeliveryInfo();
                model.setServer_Delivery_id(feedObj.getInt("OrderId"));
                if (i == 0)
                    allDelivery_id = feedObj.getInt("OrderId") + "";
                else
                    allDelivery_id += "," + feedObj.getInt("OrderId");
                model.setEmp_id(0);
                model.setCustomer_id(feedObj.getInt("CustomerId"));
                model.setVehicle_id(feedObj.getInt("VehicleId"));
                model.setOrderNumber(feedObj.getString("OrderNo"));
                model.setSerialNo(feedObj.getString("SerialNo").equals("null") ? "0" : feedObj.getString("SerialNo"));
                model.setDelivery_date(feedObj.getString("OrderDateTime"));
                model.setSalemode(feedObj.getString("SalesModel"));
                model.setPob_lat(feedObj.getString("POBLatitude").equals("null") ? "0.0" : feedObj.getString("POBLatitude"));
                model.setPob_lng(feedObj.getString("POBLongitude").equals("null") ? "0.0" : feedObj.getString("POBLongitude"));

                model.setCategoryId(feedObj.getInt("CategoryId"));
                model.setRoute(feedObj.getInt("RouteId"));
                model.setDelivery_status(feedObj.getString("Status"));
                model.setDelivery_description(feedObj.getString("Description").equals("null") ? "" : feedObj.getString("Description"));
                model.setTotal_qty(feedObj.getInt("TotalQuantity"));
             /*   model.setDelivery_start_time(feedObj.getString("Start_time"));
                model.setDelivery_end_time(feedObj.getString("End_time"));*/
                model.setDelivery_address(feedObj.getString("DeliveryAddress").equals("null") ? "" : feedObj.getString("DeliveryAddress"));
                model.setDelivery_to_mobile(feedObj.getString("Phone").equals("null") ? "0" : feedObj.getString("Phone"));

                model.setDeliver_to_name(feedObj.getString("Name").equals("null") ? "" : feedObj.getString("Name"));
                //   model.setAssign_to_TrackingNo(feedObj.getString("TrackingNo"));
                //     model.setIs_delivery_Reject(feedObj.getString("IsReject"));
                model.setRejected_Reason("");
                model.setReceivedBy("");
                model.setDeliver_lat(feedObj.getString("Latitude").equals("null") ? "0.0" : feedObj.getString("Latitude"));
                model.setDeliver_lng(feedObj.getString("Longitude").equals("null") ? "0.0" : feedObj.getString("Longitude"));
                model.setPod_lat("0.0");
                model.setPod_lng("0.0");
                model.setNote("");
                model.setTotal_Bill(feedObj.getString("TotalAmount").equals("null") ? "0" : feedObj.getString("TotalAmount"));
                model.setDiscount(feedObj.getString("Discount").equals("null") ? "0" : feedObj.getString("Discount"));
                model.setPercentageDiscount(feedObj.getString("DiscPercentage").equals("null") ? "0" : feedObj.getInt("DiscPercentage") + "");
                model.setNetTotal(feedObj.getString("NetTotal").equals("null") ? "0" : feedObj.getString("NetTotal"));
                feedItems.add(model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Utility.logCatMsg("Feed item error" + e);
        }
    }

    private void parseJsonFeedItems(JSONArray feedArray) {
        try {
            feedDeliveryItems.clear();
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                DeliveryItemModel model = new DeliveryItemModel();
                model.setOrder_item_id(feedObj.getInt("OrderId"));
                model.setServer_Item_Id(feedObj.getInt("OrderDetailId"));
                model.setName(feedObj.getString("Name"));
                model.setItem_id(feedObj.getInt("ItemId"));
                model.setCostCtnPrice(feedObj.getInt("CostCtnPrice"));
                model.setRetailPiecePrice(feedObj.getInt("WSCtnPrice"));
                model.setRetailCtnPrice(feedObj.getInt("RetailCtnPrice"));
                model.setCostPackPrice(feedObj.getInt("CostPackPrice"));
                model.setWSPackPrice(feedObj.getInt("WSPackPrice"));
                model.setRetailPackPrice(feedObj.getInt("RetailPackPrice"));
                model.setCostPiecePrice(feedObj.getInt("CostPiecePrice"));
                model.setWSPiecePrice(feedObj.getInt("WSPiecePrice"));
                model.setWSCtnPrice(feedObj.getInt("RetailPiecePrice"));
                model.setCtn_qty(feedObj.getInt("CtnQuantity"));
                model.setPac_qty(feedObj.getInt("PackQuantity"));
                model.setPcs_qty(feedObj.getInt("PcsQuantity"));
                model.setFoc_qty(feedObj.getInt("FOCQty"));
                model.setFoc_value(feedObj.getInt("FOCValue"));
                model.setItem_discount(feedObj.getInt("Discount"));
                model.setTotalCostPrice(feedObj.getInt("TotalCost"));
                model.setTotalwholeSalePrice(feedObj.getInt("TotalWholesale"));
                model.setTotalRetailPrice(feedObj.getInt("TotalRetail"));
                model.setNetTotalRetailPrice(feedObj.getInt("NetAmount"));
                model.setRoute_id(feedObj.getInt("RouteId"));
                model.setCategoryId(feedObj.getInt("CategoryId"));
                model.setTotal_Quantity(feedObj.getInt("TotalQuantity"));
                model.setReturn_Quantity(0);
                model.setRejected_Quantity(0);
                model.setDisplayPrice(feedObj.getInt("DisplayPrice"));
                model.setItemGstValue(feedObj.getInt("GSTValue"));
                model.setItemGstPer(feedObj.getInt("GSTPercentage"));
                model.setDelivered_Quantity(feedObj.getInt("TotalWholesale"));
                model.setRejectReason("");
                feedDeliveryItems.add(model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Utility.logCatMsg("Feed Delivery item error" + e);
        }
        if (feedItems.size() > 0) {
            // db.dropPlanOrderTables();
            // db.createPlanOrderTables();
            db.deletePlanOrderTableData();
            db.insertOrderDelivery(feedItems, "False");
            if (feedDeliveryItems.size() > 0) {
                db.insertOrderDeliveryItems(feedDeliveryItems, "False");
            }
            Fill_listtView_From_SqliteDB();
        } else {
            Utility.Toast(TestMainActivity.this, "No Job Available Today");
        }
    }

    private void Fill_listtView_From_SqliteDB() {
        list = new ArrayList<>();
        list = db.getSQLiteOrderDelivery("Inprogress");
        if (list.size() > 0) {
            demotextTV.setVisibility(View.GONE);
            listAdapter = new DeliveryListAdapter(this, list);
            dilervyJobListView.setAdapter(listAdapter);
            dilervyJobListView.setVisibility(View.VISIBLE);
        } else {
            demotextTV.setVisibility(View.VISIBLE);

        }
    }

    private void Alert_Dilog() {
        Alert_dilog_flag = false;
        AlertDialog.Builder builder = new AlertDialog.Builder(TestMainActivity.this);
        View view1 = getLayoutInflater().inflate(R.layout.dialog_custom_title, null);
        TextView tvCustomTitle = view1.findViewById(R.id.tvCustomTitle);
        tvCustomTitle.setText("Getting Job From Server?");
        builder.setCustomTitle(view1);
        builder.setMessage("Make sure you update all your changes to server otherwise you maybe lost it.")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (ConnectionDetector.isConnectingToInternet(TestMainActivity.this)) {
                            dilervyJobListView.setAdapter(null);
                            ArrayList<Params> parameters = new ArrayList<Params>();
                            Params p1 = new Params();
                            p1.setKey("TrackingNo");
                            p1.setValue(prefs.getEmployeeID() + ""); // user_id
                            parameters.add(p1);
                            getTodayJob("http://deliveryapi.zederp.net/api/Job/GetTodayJob?TrackingNo=" + prefs.getEmployeeID());
                            Log.d("JobUrl", "http://deliveryapi.zederp.net/api/Job/GetTodayJob?TrackingNo=" + prefs.getEmployeeID());
                            // new GetTodayJobs("GetTodayJob", "ZEDtrack.asmx", parameters).execute();
                            demotextTV.setVisibility(View.GONE);
                            dialog.cancel();
                        } else {
                            Utility.Toast(TestMainActivity.this, "Check network connection and try again");
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Alert_dilog_flag = false;
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void Select_Action_diloag(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view1 = getLayoutInflater().inflate(R.layout.dialog_custom_title, null);
        TextView tvCustomTitle = view1.findViewById(R.id.tvCustomTitle);
        tvCustomTitle.setText("Select Option");
        builder.setCustomTitle(view1);
        builder.setItems(optionlist, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (optionlist[item].equals("Cancel")) {
                    dialog.dismiss();
                } else if (optionlist[item].equals("POD")) {
                    Intent in = new Intent(TestMainActivity.this, AddImages.class);
                    in.putExtra("OrderId", list.get(pos).getDelivery_id() + "");
                    in.putExtra("imgType", "POD");
                    dialog.dismiss();
                    startActivity(in);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                } else if (optionlist[item].equals("Deliver order")) {

                    if (list.get(pos).getIsPod_sync().toString().equals("2")) {
                        Utility.Toast(TestMainActivity.this, "Cannot do this action Order is sent to server");
                    } else {
                        if (list.get(pos).getDelivery_status().equals("Delivered")) {
                            Utility.Toast(TestMainActivity.this, "Order status is already delivered");
                        }
                        else {

                            if (gps.canGetLocation()) {
                                double latitude = gps.getLatitude();
                                double longitude = gps.getLongitude();
                                if (latitude > 0.0) {

                                    list.get(pos).setDelivery_status("Delivered");
                                    list.get(pos).setIsSave("1");
                                    list.get(pos).setRejected_Reason("");
                                    list.get(pos).setRefused_Reason("");
                                    list.get(pos).setCancelledReason("");
                                    list.get(pos).setPod_lat(String.valueOf(latitude));
                                    list.get(pos).setPod_lng(String.valueOf(longitude));
                                    db.UpdateOrderStatus(list.get(pos));
                                    listAdapter.notifyDataSetChanged();

                                    Log.d("updatedValues", list.get(pos).getDelivery_status() + "\n" +
                                            list.get(pos).getIsSave() + "\n" + list.get(pos).getDelivery_date()+"\n"+list.get(pos).getPod_lat()
                                            +"\n"+list.get(pos).getPod_lng());

                                }
                                else {

                                    Toast.makeText(TestMainActivity.this, "Location not found...try again", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                gps.showSettingsAlert();
                            }
                        }
                    }
                } else if (optionlist[item].equals("Reject order")) {
                    if (list.get(pos).getIsPod_sync().toString().equals("2")) {
                        Utility.Toast(TestMainActivity.this, "Cannot do this action Order is sent to server");
                    } else {
                        list.get(pos).setDelivery_status("Rejected");
                        UserStatusDilogeReason("Enter Reject Reason", pos, "Rejected");
                    }
                } else if (optionlist[item].equals("Cancel order")) {
                    if (list.get(pos).getIsPod_sync().toString().equals("2")) {
                        Utility.Toast(TestMainActivity.this, "Cannot do this action Order is sent to server");
                    } else {
                        list.get(pos).setDelivery_status("Returned");
                        UserStatusDilogeReason("Enter Cancel Reason", pos, "Cancel");
                    }
                } else if (optionlist[item].equals("Refuse order")) {
                    if (list.get(pos).getIsPod_sync().toString().equals("2")) {
                        Utility.Toast(TestMainActivity.this, "Cannot do this action Order is sent to server");
                    } else {
                        list.get(pos).setDelivery_status("Returned");
                        UserStatusDilogeReason("Enter Refuse Reason", pos, "Refused");
                    }
                } else if (optionlist[item].equals("Shop close")) {
                    if (list.get(pos).getIsPod_sync().toString().equals("2")) {
                        Utility.Toast(TestMainActivity.this, "Cannot do this action Order is sent to server");
                    } else {
                        list.get(pos).setDelivery_status("Shop Returned");
                        list.get(pos).setRejected_Reason("");
                        list.get(pos).setRefused_Reason("");
                        list.get(pos).setCancelledReason("");
                        db.UpdateOrderStatus(list.get(pos));
                        listAdapter.notifyDataSetChanged();
                    }
                } else if (optionlist[item].equals("Send order")) {
                    if (list.get(pos).getIsPod_sync().toString().equals("2")) {
                        Utility.Toast(TestMainActivity.this, "Job Already Sent.");
                    } else {
                        if (!list.get(pos).getDelivery_status().equals("Inprogress")) {
                            list.get(pos).setIsCBChecked(true);
                            saveOrdertoServerDiloag(list.get(pos).getDelivery_status());
                        } else {
                            Utility.Toast(TestMainActivity.this, "Change order status first.");
                        }
                    }
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void InitilizeAlaram() {

        startService(new Intent(this, GPSService.class));
    }

    public static void updatePlainOrderListview(DeliveryInfo deliveryInfo) {
        if (list != null)
            for (int i = 0; i < list.size(); i++) {
                if (deliveryInfo.getDelivery_id() == list.get(i).getDelivery_id()) {
                    list.get(i).setDelivery_status(deliveryInfo.getDelivery_status());
                    list.get(i).setIsPod_sync(deliveryInfo.getIsPod_sync());
                    break;
                }
            }
        else
            Utility.logCatMsg("summeryList is null");
        listAdapter.notifyDataSetChanged();
    }

    private void UserStatusDilogeReason(String title, final int pos, final String tag) {
        View view = getLayoutInflater().inflate(R.layout.add_new_note, null);
        final EditText userText = view.findViewById(R.id.usernote);
        if (tag.equals("Cancel")) {
            userText.setText(list.get(pos).getCancelledReason());
        } else if (tag.equals("Refused")) {
            userText.setText(list.get(pos).getRefused_Reason());
        } else if (tag.equals("Rejected")) {
            userText.setText(list.get(pos).getRejected_Reason());
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TestMainActivity.this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder
                .setCancelable(false)
                .setView(view)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (tag.equals("Cancel")) {
                            list.get(pos).setCancelledReason(userText.getText() + "");
                            list.get(pos).setRejected_Reason("");
                            list.get(pos).setRefused_Reason("");
                        } else if (tag.equals("Refused")) {
                            list.get(pos).setRefused_Reason(userText.getText() + "");
                            list.get(pos).setRejected_Reason("");
                            list.get(pos).setCancelledReason("");
                        } else if (tag.equals("Rejected")) {
                            list.get(pos).setRejected_Reason(userText.getText() + "");
                            list.get(pos).setRefused_Reason("");
                            list.get(pos).setCancelledReason("");
                        }
                        db.UpdateOrderStatus(list.get(pos));
                        listAdapter.notifyDataSetChanged();
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

    private void setUpActionBar(ActionBar actionBar) {

        LinearLayout mainLayout = findViewById(R.id.mainLayout);
        View v = getLayoutInflater().inflate(R.layout.actionbar_view, mainLayout, false);
//        actionbar = v.findViewById(R.id.actionBarTextView);
        btnMenu = v.findViewById(R.id.btnMenu);
        btnMenu.setVisibility(View.GONE);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.drak_blue_hader)));
        actionBar.setCustomView(v);
    }

}
