package detrack.zaryansgroup.com.detrack.activity.activites;

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
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.util.ArrayList;
import java.util.Calendar;

import detrack.zaryansgroup.com.detrack.activity.Adapter.ReceiptListAdapter;
import detrack.zaryansgroup.com.detrack.activity.Map.GPSTracker;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.DeliveryItemModel;
import detrack.zaryansgroup.com.detrack.activity.Model.ReceiptModel;
import detrack.zaryansgroup.com.detrack.activity.SQLlite.ZEDTrackDB;
import detrack.zaryansgroup.com.detrack.activity.Service.CompanyInfoService;
import detrack.zaryansgroup.com.detrack.activity.Service.GPSService;
import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;
import detrack.zaryansgroup.com.detrack.activity.retrofit.Api_Reto;
import detrack.zaryansgroup.com.detrack.activity.utilites.ConnectionDetector;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import detrack.zaryansgroup.com.detrack.R;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class ReceiptActivity extends AppCompatActivity implements TabActivity.SendMultipleSelectionEventToRuntimeActivity{
    ListView listView;
    ArrayList<ReceiptModel> list,filteredList;
    ArrayList<ReceiptModel> templist;

    SharedPrefs prefs;
    ImageButton btnMenu;
    LinearLayout multipleSelectionLL;
    EditText mSearchEt;
    ReceiptListAdapter listAdapter;
    ImageView cancelIV, deleteIV, sendIV;
    TextView selectAllTV;
    ProgressDialog progressDialog;
    String query="";
    TextView actionbar;
    AlarmManager alarm;
    PendingIntent pintent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);



        setUpActionBar(getSupportActionBar());
        InitilizeAlaram();
        actionbar.setText("Receipts");
        listView = findViewById(R.id.listview);
        mSearchEt = findViewById(R.id.serachET1);
        mSearchEt.setFocusable(false);
        multipleSelectionLL = findViewById(R.id.multipleSelectionLL);
        prefs = new SharedPrefs(ReceiptActivity.this);
        cancelIV = findViewById(R.id.cancelIV);
        deleteIV = findViewById(R.id.deleteIV);
        sendIV = findViewById(R.id.sendIV);
        selectAllTV = findViewById(R.id.selectAllTV);
        selectAllTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectAllTV.getText().toString().equals("Select All")) {
                    selectAllTV.setText("UnSelect All");
                    boolean isOrderSent = false;
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setIsCBCheck(true);
                        if (list.get(i).getIsSync() == 1)
                            isOrderSent = true;
                    }
                    if (isOrderSent)
                        sendIV.setVisibility(View.GONE);
                    else
                        sendIV.setVisibility(View.VISIBLE);
                } else {

                    selectAllTV.setText("Select All");
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setIsCBCheck(false);
                    }
                    sendIV.setVisibility(View.VISIBLE);
                }
                listAdapter.notifyDataSetChanged();

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
                deleteOrederDiloag();
            }
        });
        sendIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               boolean isSelect=false;
                for(int i=0;i<list.size();i++)
                    if (list.get(i).isCBCheck())
                    {isSelect=true;
                    break;
                    }
                if(isSelect)
                syncReceiptDiloag();
                else
                    Utility.Toast(ReceiptActivity.this,"Select Receipt first");
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox cb = parent.findViewById(R.id.checkBox);
                if (cb.getVisibility() == View.VISIBLE) {
                    if (list.get(position).isCBCheck()) {
                        list.get(position).setIsCBCheck(false);
                    } else {
                        list.get(position).setIsCBCheck(true);
                    }
                    boolean isOrderSent = false;
                    for (int i = 0; i < list.size(); i++)
                        if (list.get(i).getIsSync() == 1 && list.get(i).isCBCheck())
                            isOrderSent = true;

                    if (isOrderSent)
                        sendIV.setVisibility(View.GONE);
                    else
                        sendIV.setVisibility(View.VISIBLE);
                    listAdapter.notifyDataSetChanged();
                } else
                    Utility.Toast(ReceiptActivity.this, "Press Long to see more option");

            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mSearchEt.setVisibility(View.GONE);
                multipleSelectionLL.setVisibility(View.VISIBLE);
                listAdapter.isChecked(true);
                listAdapter.notifyDataSetChanged();
                return false;
            }
        });
        String searchText = mSearchEt.getText().toString();
        mSearchEt.setText(searchText);
        openSearchBar();
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
                Intent intent = new Intent(ReceiptActivity.this, NewUserActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
                break;
            }
            case R.id.actionSyncCompanyCustomerInfo:{
                if (ConnectionDetector.isConnectingToInternet(ReceiptActivity.this)) {
                    startService(new Intent(ReceiptActivity.this, CompanyInfoService.class));
                    Utility.Toast(ReceiptActivity.this, "Syncing Started...");
                } else {
                    Utility.Toast(ReceiptActivity.this, "Check network connection and try again");
                }
                break;
            }
            case R.id.actionAddSalesOrder:{
                Intent intent = new Intent(ReceiptActivity.this, TakeOrder.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
                break;
            }
            case R.id.actionSettings:{
                Intent intent = new Intent(ReceiptActivity.this, SettingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            }
            case R.id.actionAddSalesReturn:{

                Intent intent = new Intent(ReceiptActivity.this, ReturnOrderSearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            }
            case R.id.enableLocation:{
                if(item.getTitle().toString().equals("Enable Location")){
                    item.setTitle("Disable Location");
                    GPSTracker gps = new GPSTracker(ReceiptActivity.this);
                    if (ConnectionDetector.isConnectingToInternet(ReceiptActivity.this)) {
                        if (gps.canGetLocation()) {
                            Utility.Toast(ReceiptActivity.this, "Location Enable Successfully");
                            startservice();
                        } else {
                            Utility.Toast(ReceiptActivity.this, "Enable your GPS first and try again..");
                            //gps.showSettingsAlert();
                        }
                    } else
                        Utility.Toast(ReceiptActivity.this, "Check network connection and try again");
                    break;
                }
                else{

                    Utility.Toast(ReceiptActivity.this, "Location Disable Successfully");
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
              break;
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
        mSearchEt.setVisibility(View.VISIBLE);
        multipleSelectionLL.setVisibility(View.GONE);
        list = new ArrayList<>();
        if(WelcomeActivity.showHistory){
            list = new ZEDTrackDB(ReceiptActivity.this).getTodaysReceipts("");
        }
        else {
//            list = new ZEDTrackDB(ReceiptActivity.this).getTodaysReceipts(Utility.getCurrentDate());
           final Calendar c = Calendar.getInstance();
           String date = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH);
            list = new ZEDTrackDB(ReceiptActivity.this).getTodaysReceipts(date);
        }
        Log.d("receiptListSize",list.size()+"");
        listAdapter= new ReceiptListAdapter(ReceiptActivity.this, list);
        listView.setAdapter(listAdapter);
        if (list.size() > 0) {
            listView.setVisibility(View.VISIBLE);
            TextView discription = findViewById(R.id.demotextTV);
            discription.setVisibility(View.GONE);
        }
        super.onResume();
    }

    @Override
    public void MultipleSelectionEventCapturedInRuntimeActivity() {
        if (list.size() > 0) {
            multipleSelectionLL.setVisibility(View.VISIBLE);
            mSearchEt.setVisibility(View.GONE);
            listAdapter.isChecked(true);
            listAdapter.notifyDataSetChanged();
        }
    }

    //new SyncReceipt API implemented
    public void syncReceipt(String url, final int position){
        progressDialog.show();

        Observable<String> hResponseObservable = Api_Reto.getRetrofit().getRetrofit_services().sendReceiptToServer(url);
        CompositeDisposable hCompositeDisposable = new CompositeDisposable();

        Disposable hAddNewOrderDisposable = hResponseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                            if (response != null) {

                                try {
                                    JSONObject parentObject = new JSONObject(response);
                                    JSONArray tableArray = parentObject.getJSONArray("Table");
                                    if(tableArray.length() > 0){

                                        for(int i = 0; i < tableArray.length(); i++){

                                            JSONObject object = (JSONObject) tableArray.get(i);
                                            templist.get(position).setServerID(object.getInt("ReceiptId"));
                                            templist.get(position).setPreviousBalnc(object.getInt("PreviousBalance"));
                                            templist.get(position).setBalance(object.getInt("Balance"));
                                            templist.get(position).setServerID(object.getInt("ReceiptId"));
                                            templist.get(position).setIsSync(1);
                                            if (new ZEDTrackDB(ReceiptActivity.this).UpdateReceipt(templist.get(position)) == 1) {

                                                progressDialog.incrementProgressBy(1);
                                                listAdapter.notifyDataSetChanged();
                                            } else {
                                                Utility.logCatMsg("Receipt ID " + templist.get(position).getId() + "Not Updated");
                                                Utility.Toast(ReceiptActivity.this, "Failed");
                                            }
                                        }

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    progressDialog.dismiss();
                                }

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(ReceiptActivity.this, "Oops Please Try again", Toast.LENGTH_SHORT).show();
                            }


                        } /*OnNext*/,
                        throwable -> {
                            progressDialog.dismiss();
                            Toast.makeText(ReceiptActivity.this, "Oops Please Try again", Toast.LENGTH_SHORT).show();
                        }/*On Error*/,
                        () -> {
                            progressDialog.dismiss();
                            hCompositeDisposable.dispose();

                        }/*On Complete*/

                );

        hCompositeDisposable.add(hAddNewOrderDisposable);




        // Shifted from Volley to Retrofit...
//        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//
//                progressDialog.dismiss();
//                try {
//                    JSONObject parentObject = new JSONObject(response);
//                    JSONArray tableArray = parentObject.getJSONArray("Table");
//                    if(tableArray.length() > 0){
//
//                        for(int i = 0; i < tableArray.length(); i++){
//
//                            JSONObject object = (JSONObject) tableArray.get(i);
//                            templist.get(position).setServerID(object.getInt("ReceiptId"));
//                            templist.get(position).setPreviousBalnc(object.getInt("PreviousBalance"));
//                            templist.get(position).setBalance(object.getInt("Balance"));
//                            templist.get(position).setServerID(object.getInt("ReceiptId"));
//                            templist.get(position).setIsSync(1);
//                            if (new ZEDTrackDB(ReceiptActivity.this).UpdateReceipt(templist.get(position)) == 1) {
//
//                                progressDialog.incrementProgressBy(1);
//                                listAdapter.notifyDataSetChanged();
//                            } else {
//                                Utility.logCatMsg("Receipt ID " + templist.get(position).getId() + "Not Updated");
//                                Utility.Toast(ReceiptActivity.this, "Failed");
//                            }
//                        }
//
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                progressDialog.dismiss();
//
//            }
//        });
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        progressDialog.show();
//        queue.add(request);

    }


    private void syncReceiptDiloag() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ReceiptActivity.this);
        String title = "Save Receipts?",
                massage = "Are you sure to Save these Receipts on server..!";
        builder.setTitle(title)
                .setMessage(massage)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        multipleSelectionLL.setVisibility(View.GONE);
                        mSearchEt.setVisibility(View.VISIBLE);
                        listAdapter.isChecked(false);
                        listAdapter.notifyDataSetChanged();
                        templist = new ArrayList<ReceiptModel>();
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).isCBCheck())
                                templist.add(list.get(i));
                        }
                        if (templist.size() > 0) {
                            Log.d("tempList",templist.size()+"");
                            progressDialog = new ProgressDialog(ReceiptActivity.this);
                            progressDialog.setTitle("Uploading Data...");
                            progressDialog.setMessage("Please Wait...");
                            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            progressDialog.setProgress(0);
                            progressDialog.setCancelable(false);
                            progressDialog.setMax(templist.size());
                            for (int i = 0; i < templist.size(); i++) {
                                Log.d("receiptModel",templist.get(i).toString());
                                if (ConnectionDetector.isConnectingToInternet(ReceiptActivity.this)) {

                                    syncReceipt(Utility.BASE_LIVE_URL+"api/Receipts/SyncCustomerReceipts?ReceiptDate="+templist.get(i).getDate()+
                                            "&SalesmanId="+prefs.getEmployeeID()+"&CustomerId="+templist.get(i).getCustomerID()+"&AmountPaid="+
                                            (int)templist.get(i).getAmountPaid()+"&CompanySiteID="+prefs.getCompanySiteID()+"&CompanyID="+
                                            prefs.getCompanyID()+"&createdby="+prefs.getUserName()+"&source=Mobile"+"&BankID="+templist.get(i).getCashDepositedBankId(), i);

                                } else {
                                    Utility.Toast(ReceiptActivity.this, "Check network connection and try again");
                                }
                            }
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

    private void deleteOrederDiloag() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete")
                .setMessage("Are you sure to delete this?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).isCBCheck()) {
                                if (new ZEDTrackDB(ReceiptActivity.this).DeleteReceipt(list.get(i).getId()) == 1) {
                                    Utility.logCatMsg(list.get(i).getId() + "Receipt id deleted");
                                } else {
                                    Utility.Toast(ReceiptActivity.this, "Not Deleted");
                                }
                            }
                        }

                        onResume();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void openSearchBar() {
        mSearchEt.addTextChangedListener(new SearchWatcher());
        mSearchEt.setHint("Search....");
        mSearchEt.setHintTextColor(Color.WHITE);
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
            Utility.logCatMsg("Text Change Listener " + query);
            filteredList = performSearch(list, query);
            listAdapter = new ReceiptListAdapter(ReceiptActivity.this, filteredList);
            listView.setAdapter(listAdapter);
        }
    }

    private ArrayList<ReceiptModel> performSearch(ArrayList<ReceiptModel> modal, String query) {
        String[] queryByWords = query.toLowerCase().split("\\s+");
        ArrayList<ReceiptModel> filter = new ArrayList<>();
        for (int i = 0; i < modal.size(); i++) {
            ReceiptModel data = modal.get(i);
            String name = data.getCustomerName().toLowerCase();
            Utility.logCatMsg("Search query :" + name);
            for (String word : queryByWords) {
                int numberOfMatches = queryByWords.length;
                if (name.contains(word)) {
                    numberOfMatches--;
                    Utility.logCatMsg("Match " + name + " " + word);
                } else {
                    break;
                }
                if (numberOfMatches == 0) {
                    filter.add(data);
                }
            }
        }
        return filter;
    }

    private void setUpActionBar(ActionBar actionBar) {

        LinearLayout mainLayout = findViewById(R.id.mainLayout);
        View v = getLayoutInflater().inflate(R.layout.actionbar_view, mainLayout, false);
        actionbar= v.findViewById(R.id.actionBarTextView);
        btnMenu = v.findViewById(R.id.btnMenu);
        btnMenu.setVisibility(View.GONE);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.appbluegrey)));
        actionBar.setCustomView(v);
    }
}


