package detrack.zaryansgroup.com.detrack.activity.activites;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Calendar;

import detrack.zaryansgroup.com.detrack.activity.Adapter.ItemsDilogListAdapter;
import detrack.zaryansgroup.com.detrack.activity.Adapter.OrderItemAdapterTest;
import detrack.zaryansgroup.com.detrack.activity.Adapter.StockListAdapter;
import detrack.zaryansgroup.com.detrack.activity.Map.GPSTracker;
import detrack.zaryansgroup.com.detrack.activity.Model.DeliveryInfo;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.DeliveryItemModel;
import detrack.zaryansgroup.com.detrack.activity.Model.RegisterCustomerModel.RegisterdCustomerModel;
import detrack.zaryansgroup.com.detrack.activity.SQLlite.ZEDTrackDB;
import detrack.zaryansgroup.com.detrack.activity.Service.CompanyInfoService;
import detrack.zaryansgroup.com.detrack.activity.Service.GPSService;
import detrack.zaryansgroup.com.detrack.activity.viewmodels.SelectProduct_viewModel;
import detrack.zaryansgroup.com.detrack.activity.utilites.ConnectionDetector;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import detrack.zaryansgroup.com.detrack.R;
import timber.log.Timber;

public class SelectProductActivity extends AppCompatActivity {



    RecyclerView lvOrder;
    RegisterdCustomerModel getSelectedCustomer;
    public DeliveryInfo deliveryInfo;
    LinearLayout searchItem;
    ArrayList<DeliveryItemModel> list = new ArrayList<>();
    ArrayList<DeliveryItemModel> selectedItemList;
    ZEDTrackDB db;
    EditText itemSerachET;
    ListView itemSerachLV;
    ArrayList<DeliveryItemModel> filteredItemList;
    private StockListAdapter listAdapter;
//    ItemListNewAdapter selectedItemListAdapter;
    OrderItemAdapterTest selectedItemListAdapter;
    NumberPicker np;
    RadioButton percentageRB;
    EditText focET, ctnQtyET, packQtyET, picesQtyET, etPiecePrice, etPackPrice, etCtnPrice;
    String FocType = "Percentage", QtyType = "Piece";
    public static boolean addOrder;
//    TextView actionbar;
    ImageButton btnMenu;
    ImageView companyLogo;
    MaterialButton btnProductOk;

    AlarmManager alarm;
    PendingIntent pintent;
    SelectProduct_viewModel selectProduct_viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_product);


        Timber.d("Hi Muhammad Yaseen");




        selectProduct_viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(SelectProduct_viewModel.class);

        initialization();
        initObserver();
        initData();
        InitilizeAlaram();
    }

    private void initData() {
        //filling list
        selectProduct_viewModel.loadSqlDeliveryItems();

        if (addOrder) {
            getSelectedCustomer = (RegisterdCustomerModel) getIntent().getSerializableExtra("selectedCustomer");
        } else {
            deliveryInfo = (DeliveryInfo) getIntent().getSerializableExtra("deliverInfo");
            selectProduct_viewModel.loadSqlSelectedCustoemr(deliveryInfo.getCustomer_id());
//            getSelectedCustomer = db.getCustomerById(deliveryInfo.getCustomer_id());
            String isNew = getIntent().getStringExtra("isNew");
            selectProduct_viewModel.loadSqlSelectedItem(String.valueOf(deliveryInfo.getDelivery_id()),isNew);
//            selectedItemList = db.getSQLiteOrderDeliveryItems(String.valueOf(deliveryInfo.getDelivery_id()), isNew);
            selectedItemListAdapter = new OrderItemAdapterTest(this, selectedItemList);
            recyclerClickListners();
            lvOrder.setLayoutManager(new LinearLayoutManager(this));
            lvOrder.setAdapter(selectedItemListAdapter);

            if (getSelectedCustomer != null) {

                Timber.d("Customer Distr id "+getSelectedCustomer.getDistributorId());
                Timber.d("Customer sub Distr id "+getSelectedCustomer.getSubDistributorId());
            }
        }
    }

    private void initObserver() {
        selectProduct_viewModel.getDeliveryItems().observe(this, new Observer<ArrayList<DeliveryItemModel>>() {
            @Override
            public void onChanged(ArrayList<DeliveryItemModel> deliveryItemModels) {
                list = deliveryItemModels;

                        }
        });

        selectProduct_viewModel.getSelectedCustomer().observe(this, new Observer<RegisterdCustomerModel>() {
            @Override
            public void onChanged(RegisterdCustomerModel registerdCustomerModel) {
                getSelectedCustomer = registerdCustomerModel;
            }
        });

        selectProduct_viewModel.getSelctedItem().observe(this, new Observer<ArrayList<DeliveryItemModel>>() {
            @Override
            public void onChanged(ArrayList<DeliveryItemModel> deliveryItemModels) {
                selectedItemList = deliveryItemModels;
                selectedItemListAdapter = new OrderItemAdapterTest(SelectProductActivity.this, selectedItemList);
                recyclerClickListners();
                lvOrder.setAdapter(selectedItemListAdapter);

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


    private void initialization() {

        addOrder = getIntent().getBooleanExtra("addOrder",false);
        db = new ZEDTrackDB(this);
        filteredItemList = new ArrayList<>();
        selectedItemList = new ArrayList<>();


        lvOrder = findViewById(R.id.lvOrder);
        btnProductOk = findViewById(R.id.btnProductOk);
        searchItem = findViewById(R.id.searchItem);


        searchItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchItem_Dilog();
            }
        });

        btnProductOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(selectedItemList.size() > 0)) {
                    Toast.makeText(SelectProductActivity.this, "Select Product First", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SelectProductActivity.this);
                    View view = getLayoutInflater().inflate(R.layout.dialog_custom_title, null);
                    TextView tvCustomTitle = view.findViewById(R.id.tvCustomTitle);
                    tvCustomTitle.setText("Order Confirmation");
                    alertDialogBuilder.setCustomTitle(view)
                            .setMessage("Do you want to confirm this order?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent intent = new Intent(SelectProductActivity.this, TotalBillActivity.class);
                                    intent.putExtra("selectedItemList", selectedItemList);
                                    intent.putExtra("RCCustomer", getSelectedCustomer);
                                    intent.putExtra("deliverInfo",deliveryInfo);
//                                    deliveryInfo = null;
                                    startActivity(intent);
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                }
            }
        });





        selectedItemListAdapter = new OrderItemAdapterTest(this, selectedItemList);
        lvOrder.setLayoutManager(new LinearLayoutManager(this));
        recyclerClickListners();
//
//        lvOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                if(addOrder) {
//                    SelectQuantityDiloge(position, parent);
//                }
//                else{
//                    updateQuantityDiloge(position, parent);
//                }
//            }
//        });


//        lvOrder.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
//                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SelectProductActivity.this);
//                alertDialogBuilder.setTitle("Delete Item")
//                        .setMessage("Do you want to delete this item?")
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                selectedItemList.remove(position);
//                                selectedItemListAdapter.notifyDataSetChanged();
//
//                            }
//                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                }).show();
//                return true;
//            }
//        });

    }

    private void recyclerClickListners() {
        selectedItemListAdapter.setOnItemClick(new OrderItemAdapterTest.OnItemClick() {
            @Override
            public void onClick(int position, View view) {
                if(addOrder) {
                    SelectQuantityDiloge(position);
                }
                else{
                    updateQuantityDiloge(position);
                }
            }
        });

        selectedItemListAdapter.setOnItemLongClick(new OrderItemAdapterTest.OnItemLongClick() {
            @Override
            public void onLongClick(final int position, View view) {


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SelectProductActivity.this);
                alertDialogBuilder.setTitle("Delete Item")
                        .setMessage("Do you want to delete this item?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                selectedItemList.remove(position);
                                selectedItemListAdapter.notifyDataSetChanged();

                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();

            }
        });
    }

    private void SearchItem_Dilog() {
        listAdapter = new StockListAdapter(SelectProductActivity.this, list);
//        selectedItemListAdapter = new ItemListNewAdapter(SelectProductActivity.this, selectedItemList);
        Log.d("selecteditemList","s="+selectedItemList.size());
        View view = getLayoutInflater().inflate(R.layout.item_search_diloag_layout, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SelectProductActivity.this);
        View view1 = getLayoutInflater().inflate(R.layout.dialog_custom_title, null);
        TextView tvCustomTitle = view1.findViewById(R.id.tvCustomTitle);
        tvCustomTitle.setText("Search Item");
        alertDialogBuilder.setCustomTitle(view1);
        alertDialogBuilder
                .setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        itemSerachET = view.findViewById(R.id.inputSearch);
        itemSerachLV = view.findViewById(R.id.item_list_view);
        //list is the list of all items
        ItemsDilogListAdapter adapter = new ItemsDilogListAdapter(SelectProductActivity.this, list);
        itemSerachLV.setAdapter(adapter);
        itemSerachLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                SelectQuantityDiloge(position);
                alertDialog.dismiss();
            }
        });
        openItemSearchBar();
        alertDialog.show();
    }


    private void openItemSearchBar() {
        itemSerachET.addTextChangedListener(new SearchItemListWatcher());
        itemSerachET.setHint("Search....");
    }

    private class SearchItemListWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence c, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence c, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String query = itemSerachET.getText().toString();
            filteredItemList = performItemSearch(list, query);
            itemSerachLV.setAdapter(new ItemsDilogListAdapter(SelectProductActivity.this, filteredItemList));
        }
    }

    private ArrayList<DeliveryItemModel> performItemSearch(ArrayList<DeliveryItemModel> modal, String query) {
        String[] queryByWords = query.toLowerCase().split("\\s+");
        ArrayList<DeliveryItemModel> filter = new ArrayList<>();
        for (int i = 0; i < modal.size(); i++) {
            DeliveryItemModel data = modal.get(i);
            String name = data.getName().toLowerCase();
            String code = data.getCode();
            Utility.logCatMsg("Search query :" + name);
            for (String word : queryByWords) {
                int numberOfMatches = queryByWords.length;
                if (name.contains(word) || code.contains(word)) {
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

    private void SelectQuantityDiloge(final int position) {
//        DeliveryItemModel model = (DeliveryItemModel) adapterView.getItemAtPosition(position);
        DeliveryItemModel model = list.get(position);
//        if(lis!=null){
//            if(selectedItemList.size() > 0){
//                 model = selectedItemList.get(position);
//            }else {
//                 model = list.get(position);
//            }
//        }else {
//
//        }

//
//        final int list_postion = listAdapter.getListPosition(model);
        final int list_postion = position;
        final View view = getLayoutInflater().inflate(R.layout.select_qty_layout, null);
        np = view.findViewById(R.id.number_picker);

        RadioGroup focrg = view.findViewById(R.id.RGSelectFoc);
        percentageRB = view.findViewById(R.id.precentageRB);
        focET = view.findViewById(R.id.focET);
        ctnQtyET = view.findViewById(R.id.ctnQtyET);
        packQtyET = view.findViewById(R.id.pakQtyET);
        picesQtyET = view.findViewById(R.id.piecesQtyET);
        etCtnPrice = view.findViewById(R.id.etCtnPrice);
        etPackPrice = view.findViewById(R.id.etPackPrice);
        etPiecePrice = view.findViewById(R.id.etPiecePrice);

        picesQtyET.requestFocus();
        etPiecePrice.setText(String.valueOf(model.getWSCtnPrice()));
        etPackPrice.setText(String.valueOf(model.getRetailPackPrice()));
        etCtnPrice.setText(String.valueOf(model.getRetailCtnPrice()));

        focET.addTextChangedListener(new FocValidation());
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

            if (list.get(list_postion).getFoc() > 0) {
                focET.setText(list.get(list_postion).getFoc() + "");
            }
            if (list.get(list_postion).getCtn_qty() > 0) {
                ctnQtyET.setText(list.get(list_postion).getCtn_qty() + "");
            }
            if (list.get(list_postion).getPac_qty() > 0) {
                packQtyET.setText(list.get(list_postion).getPac_qty() + "");
            }
            if (list.get(list_postion).getPcs_qty() > 0) {
                picesQtyET.setText(list.get(list_postion).getPcs_qty() + "");
            }
        } catch (Exception e) {
            Utility.logCatMsg("Error in " + e.getMessage());
        }
        Utility.logCatMsg("Qty " + list.get(list_postion).getQtyType());

        if (list.get(list_postion).getFocType() != null) {
            if (list.get(list_postion).getFocType().equals("Qty")) {
                RadioButton rb = view.findViewById(R.id.qtyRB);
                rb.setChecked(true);
            } else if (list.get(list_postion).getFocType().equals("Value")) {
                RadioButton rb = view.findViewById(R.id.valueRB);
                rb.setChecked(true);
            } else if (list.get(list_postion).getFocType().equals("Percentage")) {
                RadioButton rb = view.findViewById(R.id.precentageRB);
                rb.setChecked(true);
            }
        } else {
            list.get(list_postion).setFocType("Percentage");
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SelectProductActivity.this);
        View view1 = getLayoutInflater().inflate(R.layout.dialog_custom_title, null);
        TextView tvCustomTitle = view1.findViewById(R.id.tvCustomTitle);



        tvCustomTitle.setText("Select Qty");

        alertDialogBuilder.setCustomTitle(view1);
        alertDialogBuilder
                .setCancelable(false)
                .setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        try {
                            float crtnPrice = Float.parseFloat(etCtnPrice.getText().toString());

                            float packPrice = Float.parseFloat(etPackPrice.getText().toString());

                            float picesPrice = Float.parseFloat(etPiecePrice.getText().toString());

                            if (crtnPrice != 0 && packPrice != 0) {
                                list.get(position).setRetailCtnPrice(crtnPrice);
                                list.get(position).setRetailPackPrice(packPrice);
                                list.get(position).setWSCtnPrice(picesPrice);
                                ///
                                setQtyValuesOfList(position, 0);
                                setFocValuesOfList(position, 0);
                                list.get(position).setSelectedValue("0");
                                list.get(position).setItem_discount(0);
                                list.get(position).setTotalRetailPrice(0);
                                list.get(position).setItemGstValue(0);
                                list.get(position).setNetTotalRetailPrice(0);
                                list.get(position).setIsSelected(false);
                                Toast.makeText(SelectProductActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                                ///
                                dialog.dismiss();
                            }
                        } catch (Exception e) {
                        }

                        if (ctnQtyET.getText().length() == 0 && packQtyET.getText().length() == 0 && picesQtyET.getText().length() == 0) {
                            Utility.logCatMsg("Second Condition");
                            list.get(list_postion).setIsSelected(false);
                            list.get(list_postion).setSelectedValue("0");
                            list.get(list_postion).setCtn_qty(0);
                            list.get(list_postion).setPac_qty(0);
                            list.get(list_postion).setPcs_qty(0);
                            list.get(list_postion).setQtyType(QtyType);
                            list.get(list_postion).setFocType(FocType);
                            setQtyValuesOfList(list_postion, 0);
                        } else {
                            Utility.logCatMsg("third condition");
                            list.get(list_postion).setIsSelected(true);
                            list.get(list_postion).setSelectedValue(ctnQtyET.getText().toString());

                            Log.d("itemqunaityadded",ctnQtyET.getText().toString());

                            list.get(list_postion).setQtyType(QtyType);
                            Log.d("itemqunaityadded",QtyType);
                            list.get(list_postion).setFocType(FocType);
                            Log.d("itemqunaityadded",FocType);
                            setQtyValuesOfList(list_postion, 1);
                        }

                        if (focET.getText().toString().equals("0") || focET.getText().length() == 0) {
                            list.get(list_postion).setFoc(0);
                            setFocValuesOfList(list_postion, 0);
                        } else {
                            setFocValuesOfList(list_postion, 1);
                            list.get(list_postion).setFoc(Integer.parseInt(focET.getText().toString()));
                        }
                        list.get(list_postion).setNetTotalRetailPrice((list.get(list_postion).getNetTotalRetailPrice() - list.get(list_postion).getItem_discount()));
                        DeliveryItemModel selectedItem = list.get(list_postion);
                        try {
                            if (selectedItemList.size() > 0) {
                                for(int i = 0; i < selectedItemList.size(); i++){
                                    if (selectedItemList.get(i).getName().equals(selectedItem.getName())) {
                                        Log.d("conditionMeet","true");
                                        selectedItemList.remove(i);
                                    }
                                }

                            }
                        } catch (IndexOutOfBoundsException ex) {
                            ex.printStackTrace();
                        }
                        selectedItemList.add(selectedItem);
                        lvOrder.setAdapter(selectedItemListAdapter);
//                        lvOrder.setDividerHeight(5);
                        selectedItemListAdapter.notifyDataSetChanged();
                        Utility.HideKeyBoard(view, SelectProductActivity.this);
                        dialog.dismiss();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        alertDialog.show();

    }

    private void updateQuantityDiloge(final int position) {
//        DeliveryItemModel model = (DeliveryItemModel) adapterView.getItemAtPosition(position);
        DeliveryItemModel model = selectedItemList.get(position);
//        final int list_postion = selectedItemListAdapter.getListPosition(model);
        final int list_postion = position;
        final View view = getLayoutInflater().inflate(R.layout.select_qty_layout, null);
        np = view.findViewById(R.id.number_picker);

        RadioGroup focrg = view.findViewById(R.id.RGSelectFoc);
        percentageRB = view.findViewById(R.id.precentageRB);
        focET = view.findViewById(R.id.focET);
        ctnQtyET = view.findViewById(R.id.ctnQtyET);
        packQtyET = view.findViewById(R.id.pakQtyET);
        picesQtyET = view.findViewById(R.id.piecesQtyET);
        etCtnPrice = view.findViewById(R.id.etCtnPrice);
        etPackPrice = view.findViewById(R.id.etPackPrice);
        etPiecePrice = view.findViewById(R.id.etPiecePrice);
        RadioButton qtyRB = view.findViewById(R.id.qtyRB);
        RadioButton valueRB = view.findViewById(R.id.valueRB);
        RadioButton percentageRB = view.findViewById(R.id.precentageRB);

        picesQtyET.requestFocus();
        etPiecePrice.setText(String.valueOf(model.getWSCtnPrice()));
        etPackPrice.setText(String.valueOf(model.getRetailPackPrice()));
        etCtnPrice.setText(String.valueOf(model.getRetailCtnPrice()));
        try {

            //check if foc is given
            if (selectedItemList.get(list_postion).getFocType().equals("Qty")) {

                focET.setText(String.valueOf(selectedItemList.get(list_postion).getFoc_qty()));
                Log.d("focValue", selectedItemList.get(list_postion).getFoc_qty() + "");
                qtyRB.setChecked(true);
                FocType = "Qty";
            } else if (selectedItemList.get(list_postion).getFocType().equals("Value")) {
                focET.setText(String.valueOf(selectedItemList.get(list_postion).getFoc_value()));
                Log.d("focValue", selectedItemList.get(list_postion).getFoc_value() + "");
                valueRB.setChecked(true);
                FocType = "Value";
            } else if (selectedItemList.get(list_postion).getFocType().equals("Percentage")) {
                focET.setText(String.valueOf(selectedItemList.get(list_postion).getFoc_percentage()));
                Log.d("focValue", selectedItemList.get(list_postion).getFoc_percentage() + "");
                percentageRB.setChecked(true);
                FocType = "Percentage";
            }
        }catch (NullPointerException ex){
            ex.printStackTrace();

            //check if foc is given
            if (selectedItemList.get(list_postion).getFoc_qty() > 0) {
                qtyRB.setChecked(true);
                focET.setText(String.valueOf(selectedItemList.get(list_postion).getFoc_qty()));
                Log.d("focValue", selectedItemList.get(list_postion).getFoc_qty() + "");
                FocType = "Qty";
            } else if (selectedItemList.get(list_postion).getFoc_value() > 0) {
                valueRB.setChecked(true);
                focET.setText(String.valueOf(selectedItemList.get(list_postion).getFoc_value()));
                Log.d("focValue", selectedItemList.get(list_postion).getFoc_value() + "");
                FocType = "Value";

            } else if (selectedItemList.get(list_postion).getFoc_percentage() > 0) {
                percentageRB.setChecked(true);
                focET.setText(String.valueOf(selectedItemList.get(list_postion).getFoc_percentage()));
                Log.d("focValue", selectedItemList.get(list_postion).getFoc_percentage() + "");
                FocType = "Percentage";

            }


        }

        focET.addTextChangedListener(new FocValidation());
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

            if (selectedItemList.get(list_postion).getFoc() > 0) {
                focET.setText(selectedItemList.get(list_postion).getFoc() + "");
            }
            if (selectedItemList.get(list_postion).getCtn_qty() > 0) {
                ctnQtyET.setText(selectedItemList.get(list_postion).getCtn_qty() + "");
            }
            if (selectedItemList.get(list_postion).getPac_qty() > 0) {
                packQtyET.setText(selectedItemList.get(list_postion).getPac_qty() + "");
            }
            if (selectedItemList.get(list_postion).getPcs_qty() > 0) {
                picesQtyET.setText(selectedItemList.get(list_postion).getPcs_qty() + "");
            }
        } catch (Exception e) {
            Utility.logCatMsg("Error in " + e.getMessage());
        }


        if (selectedItemList.get(list_postion).getFocType() != null) {
            if (selectedItemList.get(list_postion).getFocType().equals("Qty")) {
                RadioButton rb = view.findViewById(R.id.qtyRB);
                rb.setChecked(true);
            } else if (selectedItemList.get(list_postion).getFocType().equals("Value")) {
                RadioButton rb = view.findViewById(R.id.valueRB);
                rb.setChecked(true);
            } else if (selectedItemList.get(list_postion).getFocType().equals("Percentage")) {
                RadioButton rb = view.findViewById(R.id.precentageRB);
                rb.setChecked(true);
            }
        } else {
            selectedItemList.get(list_postion).setFocType("Percentage");
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SelectProductActivity.this);
        View view1 = getLayoutInflater().inflate(R.layout.dialog_custom_title, null);
        TextView tvCustomTitle = view1.findViewById(R.id.tvCustomTitle);
        tvCustomTitle.setText("Select Qty and FOC");

        alertDialogBuilder.setCustomTitle(view1);
        alertDialogBuilder
                .setCancelable(false)
                .setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        try {
                            float crtnPrice = Float.parseFloat(etCtnPrice.getText().toString());
                            float packPrice = Float.parseFloat(etPackPrice.getText().toString());
                            float picesPrice = Float.parseFloat(etPiecePrice.getText().toString());
                            if (crtnPrice != 0 && packPrice != 0) {
                                selectedItemList.get(position).setRetailCtnPrice(crtnPrice);
                                selectedItemList.get(position).setRetailPackPrice(packPrice);
                                selectedItemList.get(position).setWSCtnPrice(picesPrice);
                                ///
                                updateQtyValuesOfList(position, 0);
                                updateFocValuesOfList(position, 0);
                                selectedItemList.get(position).setSelectedValue("0");
                                selectedItemList.get(position).setItem_discount(0);
                                selectedItemList.get(position).setTotalRetailPrice(0);
                                selectedItemList.get(position).setItemGstValue(0);
                                selectedItemList.get(position).setNetTotalRetailPrice(0);
                                selectedItemList.get(position).setIsSelected(false);
                                ///
                                dialog.dismiss();
                            }
                        } catch (Exception e) {
                        }

                        if (ctnQtyET.getText().length() == 0 && packQtyET.getText().length() == 0 && picesQtyET.getText().length() == 0) {
                            Utility.logCatMsg("Second Condition");
                            selectedItemList.get(list_postion).setIsSelected(false);
                            selectedItemList.get(list_postion).setSelectedValue("0");
                            selectedItemList.get(list_postion).setCtn_qty(0);
                            selectedItemList.get(list_postion).setPac_qty(0);
                            selectedItemList.get(list_postion).setPcs_qty(0);
                            selectedItemList.get(list_postion).setQtyType(QtyType);
                            selectedItemList.get(list_postion).setFocType(FocType);
                            updateQtyValuesOfList(list_postion, 0);
                        } else {
                            Utility.logCatMsg("third condition");
                            selectedItemList.get(list_postion).setIsSelected(true);
                            selectedItemList.get(list_postion).setSelectedValue(ctnQtyET.getText().toString());
                            selectedItemList.get(list_postion).setQtyType(QtyType);
                            selectedItemList.get(list_postion).setFocType(FocType);
                            updateQtyValuesOfList(list_postion, 1);
                        }

                        if (focET.getText().toString().equals("0") || focET.getText().length() == 0) {
                            selectedItemList.get(list_postion).setFoc(0);
                            updateFocValuesOfList(list_postion, 0);
                        } else {
                            updateFocValuesOfList(list_postion, 1);
                            selectedItemList.get(list_postion).setFoc(Integer.parseInt(focET.getText().toString()));
                        }
                        selectedItemList.get(list_postion).setNetTotalRetailPrice((list.get(list_postion).getNetTotalRetailPrice() - list.get(list_postion).getItem_discount()));
                        DeliveryItemModel selectedItem = selectedItemList.get(list_postion);
                        Log.d("itemDetail", selectedItem.getName() + "\n" + selectedItem.getCode() + "\n" + selectedItem.getPcs_qty());
                        try {
                            if (selectedItemList.size() > 0) {
                                if (selectedItemList.get(position).getTitle().equals(selectedItem.getTitle())) {
                                    selectedItemList.remove(position);
                                }
                            }
                        } catch (IndexOutOfBoundsException ex) {
                            ex.printStackTrace();
                        }
                        selectedItemList.add(selectedItem);
                        lvOrder.setAdapter(selectedItemListAdapter);
//                        lvOrder.setDividerHeight(5);
                        selectedItemListAdapter.notifyDataSetChanged();
                        Utility.HideKeyBoard(view, SelectProductActivity.this);
                        dialog.dismiss();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
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
                    if (focvalue >= 100)
                        focET.setText("0");
                } catch (Exception e) {
                    Utility.logCatMsg("Error in FocValidation" + e.getMessage());
                }
            }
        }
    }

    private void updateQtyValuesOfList(int list_postion, int flage) {

        if (SelectCustomerActivity.addOrder.equals("false")) {
            selectedItemList.get(list_postion).setCtn_qty(0);
            selectedItemList.get(list_postion).setPac_qty(0);
            selectedItemList.get(list_postion).setPcs_qty(0);
            selectedItemList.get(list_postion).setTotalwholeSalePrice(0);
            selectedItemList.get(list_postion).setTotalCostPrice(0);
            selectedItemList.get(list_postion).setTotalRetailPrice(0);
            selectedItemList.get(list_postion).setDelivered_Quantity(0);
            selectedItemList.get(list_postion).setItemTotalDeliverQtyInPieces(0);

            if (flage == 1) {
                int ctnQty = 0, packQty = 0, pieceQty = 0;
                if (!ctnQtyET.getText().toString().equals(""))
                    ctnQty = Integer.parseInt(ctnQtyET.getText().toString()) * -1;
                if (!packQtyET.getText().toString().equals(""))
                    packQty = Integer.parseInt(packQtyET.getText().toString()) * -1;
                if (!picesQtyET.getText().toString().equals(""))
                    pieceQty = Integer.parseInt(picesQtyET.getText().toString()) * -1;
                int ctnSize = selectedItemList.get(list_postion).getCtnSize();
                int packSize = selectedItemList.get(list_postion).getPackSize();
                int totalQtyInPieces = (ctnQty * ctnSize * packSize) + (packQty * packSize) + pieceQty;
                Utility.logCatMsg("Total Quantity in Pieces " + totalQtyInPieces);
                selectedItemList.get(list_postion).setItemTotalDeliverQtyInPieces(totalQtyInPieces);
                selectedItemList.get(list_postion).setItemTotalActualQtyInPieces(totalQtyInPieces);
                selectedItemList.get(list_postion).setCtn_qty(ctnQty);
                selectedItemList.get(list_postion).setPac_qty(packQty);
                selectedItemList.get(list_postion).setPcs_qty(pieceQty);

                selectedItemList.get(list_postion).setDelivered_Quantity(packQty + packQty + pieceQty);
                float ctnWSPrice = selectedItemList.get(list_postion).getRetailPiecePrice();
                float totalCtnWSPrice = ctnQty * ctnWSPrice;
                float ctnCostPrice = selectedItemList.get(list_postion).getCostCtnPrice();
                float totalctnCostPrice = ctnQty * ctnCostPrice;
                float ctnRetailPrice = selectedItemList.get(list_postion).getRetailCtnPrice();
                float totalctnRetailPrice = ctnQty * ctnRetailPrice;
                float packWSPrice = selectedItemList.get(list_postion).getWSPackPrice();
                float totalPackWSPrice = packQty * packWSPrice;
                float packCostPrice = selectedItemList.get(list_postion).getCostPackPrice();
                float totalPackCostPrice = packQty * packCostPrice;
                float packRetailPrice = selectedItemList.get(list_postion).getRetailPackPrice();
                float totalPackRetailPrice = packQty * packRetailPrice;
                float PiecesWSPrice = selectedItemList.get(list_postion).getWSPiecePrice();
                float totalPiecesWSPrice = pieceQty * PiecesWSPrice;
                float PiecesCostPrice = selectedItemList.get(list_postion).getCostPiecePrice();
                float totalPiecesCostPrice = pieceQty * PiecesCostPrice;
                float PiecesRetailPrice = selectedItemList.get(list_postion).getWSCtnPrice();
                float totalPiecesRetailPrice = pieceQty * PiecesRetailPrice;
                float sumOfWholeSalePrice = ctnWSPrice + packWSPrice + PiecesWSPrice;
                float sumOfTotalWholeSalePrice = totalCtnWSPrice + totalPackWSPrice + totalPiecesWSPrice;
                float sumOfCostPrice = ctnCostPrice + packCostPrice + PiecesCostPrice;
                float sumOfTotalCostPrice = totalctnCostPrice + totalPackCostPrice + totalPiecesCostPrice;
                float sumOfRetailPrice = ctnRetailPrice + packRetailPrice + PiecesRetailPrice;
                float sumOfTotalRetailPrice = totalctnRetailPrice + totalPackRetailPrice + totalPiecesRetailPrice;
                selectedItemList.get(list_postion).setTotalwholeSalePrice(sumOfTotalWholeSalePrice);  // this is handel on insertion time
                selectedItemList.get(list_postion).setTotalCostPrice(sumOfTotalCostPrice);
                selectedItemList.get(list_postion).setTotalRetailPrice(sumOfTotalRetailPrice);
                selectedItemList.get(list_postion).setDisplayPrice(0);
                selectedItemList.get(list_postion).setItemGstPer(Utility.GST_PERCENT);
                float gst = Utility.GST_PERCENT;
                float gstPercent = 0;
                if (gst != 0) {
                    gstPercent = gst / 100.0f;
                }
                float gstValue = gstPercent * sumOfTotalRetailPrice;
                selectedItemList.get(list_postion).setItemGstValue(gstValue);
                selectedItemList.get(list_postion).setNetTotalRetailPrice((selectedItemList.get(list_postion).getTotalRetailPrice() + selectedItemList.get(list_postion).getItemGstValue()));
                Utility.logCatMsg("Gst Value " + selectedItemList.get(list_postion).getItemGstValue());
                Utility.logCatMsg("Net Total Bill  " + selectedItemList.get(list_postion).getNetTotalRetailPrice());
            }
        } else {
            selectedItemList.get(list_postion).setCtn_qty(0);
            selectedItemList.get(list_postion).setPac_qty(0);
            selectedItemList.get(list_postion).setPcs_qty(0);
            selectedItemList.get(list_postion).setTotalwholeSalePrice(0);
            selectedItemList.get(list_postion).setTotalCostPrice(0);
            selectedItemList.get(list_postion).setTotalRetailPrice(0);
            selectedItemList.get(list_postion).setDelivered_Quantity(0);
            selectedItemList.get(list_postion).setItemTotalDeliverQtyInPieces(0);

            if (flage == 1) {
                int ctnQty = 0, packQty = 0, pieceQty = 0;
                if (!ctnQtyET.getText().toString().equals(""))
                    ctnQty = Integer.parseInt(ctnQtyET.getText().toString());
                if (!packQtyET.getText().toString().equals(""))
                    packQty = Integer.parseInt(packQtyET.getText().toString());
                if (!picesQtyET.getText().toString().equals(""))
                    pieceQty = Integer.parseInt(picesQtyET.getText().toString());
                int ctnSize = selectedItemList.get(list_postion).getCtnSize();
                int packSize = selectedItemList.get(list_postion).getPackSize();
                int totalQtyInPieces = (ctnQty * ctnSize * packSize) + (packQty * packSize) + pieceQty;
                Utility.logCatMsg("Total Quantity in Pieces " + totalQtyInPieces);
                selectedItemList.get(list_postion).setItemTotalDeliverQtyInPieces(totalQtyInPieces);
                selectedItemList.get(list_postion).setItemTotalActualQtyInPieces(totalQtyInPieces);
                selectedItemList.get(list_postion).setCtn_qty(ctnQty);
                selectedItemList.get(list_postion).setPac_qty(packQty);
                selectedItemList.get(list_postion).setPcs_qty(pieceQty);

                selectedItemList.get(list_postion).setDelivered_Quantity(packQty + packQty + pieceQty);
                float ctnWSPrice = selectedItemList.get(list_postion).getRetailPiecePrice();
                float totalCtnWSPrice = ctnQty * ctnWSPrice;
                float ctnCostPrice = selectedItemList.get(list_postion).getCostCtnPrice();
                float totalctnCostPrice = ctnQty * ctnCostPrice;
                float ctnRetailPrice = selectedItemList.get(list_postion).getRetailCtnPrice();
                float totalctnRetailPrice = ctnQty * ctnRetailPrice;
                float packWSPrice = selectedItemList.get(list_postion).getWSPackPrice();
                float totalPackWSPrice = packQty * packWSPrice;
                float packCostPrice = selectedItemList.get(list_postion).getCostPackPrice();
                float totalPackCostPrice = packQty * packCostPrice;
                float packRetailPrice = selectedItemList.get(list_postion).getRetailPackPrice();
                float totalPackRetailPrice = packQty * packRetailPrice;
                float PiecesWSPrice = selectedItemList.get(list_postion).getWSPiecePrice();
                float totalPiecesWSPrice = pieceQty * PiecesWSPrice;
                float PiecesCostPrice = selectedItemList.get(list_postion).getCostPiecePrice();
                float totalPiecesCostPrice = pieceQty * PiecesCostPrice;
                float PiecesRetailPrice = selectedItemList.get(list_postion).getWSCtnPrice();
                float totalPiecesRetailPrice = pieceQty * PiecesRetailPrice;
                float sumOfWholeSalePrice = ctnWSPrice + packWSPrice + PiecesWSPrice;
                float sumOfTotalWholeSalePrice = totalCtnWSPrice + totalPackWSPrice + totalPiecesWSPrice;
                float sumOfCostPrice = ctnCostPrice + packCostPrice + PiecesCostPrice;
                float sumOfTotalCostPrice = totalctnCostPrice + totalPackCostPrice + totalPiecesCostPrice;
                float sumOfRetailPrice = ctnRetailPrice + packRetailPrice + PiecesRetailPrice;
                float sumOfTotalRetailPrice = totalctnRetailPrice + totalPackRetailPrice + totalPiecesRetailPrice;
                selectedItemList.get(list_postion).setTotalwholeSalePrice(sumOfTotalWholeSalePrice);  // this is handel on insertion time
                selectedItemList.get(list_postion).setTotalCostPrice(sumOfTotalCostPrice);
                selectedItemList.get(list_postion).setTotalRetailPrice(sumOfTotalRetailPrice);
                selectedItemList.get(list_postion).setDisplayPrice(0);
                selectedItemList.get(list_postion).setItemGstPer(Utility.GST_PERCENT);
                float gst = Utility.GST_PERCENT;
                float gstPercent = 0;
                if (gst != 0) {
                    gstPercent = gst / 100.0f;
                }
                float gstValue = gstPercent * sumOfTotalRetailPrice;
                selectedItemList.get(list_postion).setItemGstValue(gstValue);
                selectedItemList.get(list_postion).setNetTotalRetailPrice((selectedItemList.get(list_postion).getTotalRetailPrice() + selectedItemList.get(list_postion).getItemGstValue()));
                Utility.logCatMsg("Gst Value " + selectedItemList.get(list_postion).getItemGstValue());
                Utility.logCatMsg("Net Total Bill  " + selectedItemList.get(list_postion).getNetTotalRetailPrice());
            }
        }


    }

    private void setQtyValuesOfList(int list_postion, int flage) {

        if (SelectCustomerActivity.addOrder.equals("false")) {
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
                if (!ctnQtyET.getText().toString().equals(""))
                    ctnQty = Integer.parseInt(ctnQtyET.getText().toString()) * -1;
                if (!packQtyET.getText().toString().equals(""))
                    packQty = Integer.parseInt(packQtyET.getText().toString()) * -1;
                if (!picesQtyET.getText().toString().equals(""))
                    pieceQty = Integer.parseInt(picesQtyET.getText().toString()) * -1;
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
                float sumOfTotalRetailPrice = totalctnRetailPrice + totalPackRetailPrice + totalPiecesRetailPrice;
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
                list.get(list_postion).setNetTotalRetailPrice((list.get(list_postion).getTotalRetailPrice() + list.get(list_postion).getItemGstValue()));
                Utility.logCatMsg("Gst Value " + list.get(list_postion).getItemGstValue());
                Utility.logCatMsg("Net Total Bill  " + list.get(list_postion).getNetTotalRetailPrice());
            }
        } else {
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
                if (!ctnQtyET.getText().toString().equals(""))
                    ctnQty = Integer.parseInt(ctnQtyET.getText().toString());
                if (!packQtyET.getText().toString().equals(""))
                    packQty = Integer.parseInt(packQtyET.getText().toString());
                if (!picesQtyET.getText().toString().equals(""))
                    pieceQty = Integer.parseInt(picesQtyET.getText().toString());
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
                float sumOfTotalRetailPrice = totalctnRetailPrice + totalPackRetailPrice + totalPiecesRetailPrice;
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
                list.get(list_postion).setNetTotalRetailPrice((list.get(list_postion).getTotalRetailPrice() + list.get(list_postion).getItemGstValue()));
                Utility.logCatMsg("Gst Value " + list.get(list_postion).getItemGstValue());
                Utility.logCatMsg("Net Total Bill  " + list.get(list_postion).getNetTotalRetailPrice());
            }
        }


    }

    private void setFocValuesOfList(int list_postion, int flage) {
        list.get(list_postion).setFoc_percentage(0);
        list.get(list_postion).setFoc_qty(0);
        list.get(list_postion).setFoc_value(0);
        list.get(list_postion).setItem_discount(0);
        if (flage == 1) {
            if (list.get(list_postion).getFocType() != null) {
                if (SelectCustomerActivity.addOrder.equals("false")) {

                    if (list.get(list_postion).getFocType().toString().equals("Qty")) {
                        list.get(list_postion).setFoc_qty(Integer.parseInt(focET.getText().toString()) * -1);
                    } else if (list.get(list_postion).getFocType().toString().equals("Value")) {
                        int focValue = Integer.parseInt(focET.getText().toString()) * -1;
                        list.get(list_postion).setFoc_value(focValue);
                        list.get(list_postion).setItem_discount(focValue);
                    } else if (list.get(list_postion).getFocType().toString().equals("Percentage")) {
                        Float focValue = Float.parseFloat(focET.getText().toString()) * -1;
                        Float discountInValue = (focValue / 100.0f) * list.get(list_postion).getTotalRetailPrice();
                        Utility.logCatMsg(focValue + "FOC Percent % of " + list.get(list_postion).getTotalRetailPrice() + "in value Is = " + discountInValue);
                        list.get(list_postion).setFoc_value(Math.round(discountInValue));
                        list.get(list_postion).setItem_discount(Math.round(discountInValue));
                        list.get(list_postion).setFoc_percentage(Integer.parseInt(focET.getText().toString()) * -1);
                    }
                } else {
                    if (list.get(list_postion).getFocType().toString().equals("Qty")) {
                        list.get(list_postion).setFoc_qty(Integer.parseInt(focET.getText().toString()));
                    } else if (list.get(list_postion).getFocType().toString().equals("Value")) {
                        int focValue = Integer.parseInt(focET.getText().toString());
                        list.get(list_postion).setFoc_value(focValue);
                        list.get(list_postion).setItem_discount(focValue);
                    } else if (list.get(list_postion).getFocType().toString().equals("Percentage")) {
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

    private void updateFocValuesOfList(int list_postion, int flage) {
        selectedItemList.get(list_postion).setFoc_percentage(0);
        selectedItemList.get(list_postion).setFoc_qty(0);
        selectedItemList.get(list_postion).setFoc_value(0);
        selectedItemList.get(list_postion).setItem_discount(0);
        if (flage == 1) {
            if (selectedItemList.get(list_postion).getFocType() != null) {
                if (SelectCustomerActivity.addOrder.equals("false")) {

                    if (selectedItemList.get(list_postion).getFocType().toString().equals("Qty")) {
                        selectedItemList.get(list_postion).setFoc_qty(Integer.parseInt(focET.getText().toString()) * -1);
                    } else if (selectedItemList.get(list_postion).getFocType().toString().equals("Value")) {
                        int focValue = Integer.parseInt(focET.getText().toString()) * -1;
                        selectedItemList.get(list_postion).setFoc_value(focValue);
                        selectedItemList.get(list_postion).setItem_discount(focValue);
                    } else if (selectedItemList.get(list_postion).getFocType().toString().equals("Percentage")) {
                        Float focValue = Float.parseFloat(focET.getText().toString()) * -1;
                        Float discountInValue = (focValue / 100.0f) * selectedItemList.get(list_postion).getTotalRetailPrice();
                        Utility.logCatMsg(focValue + "FOC Percent % of " + selectedItemList.get(list_postion).getTotalRetailPrice() + "in value Is = " + discountInValue);
                        selectedItemList.get(list_postion).setFoc_value(Math.round(discountInValue));
                        selectedItemList.get(list_postion).setItem_discount(Math.round(discountInValue));
                        selectedItemList.get(list_postion).setFoc_percentage(Integer.parseInt(focET.getText().toString()) * -1);
                    }
                } else {
                    if (selectedItemList.get(list_postion).getFocType().toString().equals("Qty")) {
                        selectedItemList.get(list_postion).setFoc_qty(Integer.parseInt(focET.getText().toString()));
                    } else if (selectedItemList.get(list_postion).getFocType().toString().equals("Value")) {
                        int focValue = Integer.parseInt(focET.getText().toString());
                        selectedItemList.get(list_postion).setFoc_value(focValue);
                        selectedItemList.get(list_postion).setItem_discount(focValue);
                    } else if (selectedItemList.get(list_postion).getFocType().toString().equals("Percentage")) {
                        Float focValue = Float.parseFloat(focET.getText().toString());
                        Float discountInValue = (focValue / 100.0f) * selectedItemList.get(list_postion).getTotalRetailPrice();
                        Utility.logCatMsg(focValue + "FOC Percent % of " + selectedItemList.get(list_postion).getTotalRetailPrice() + "in value Is = " + discountInValue);
                        selectedItemList.get(list_postion).setFoc_value(Math.round(discountInValue));
                        selectedItemList.get(list_postion).setItem_discount(Math.round(discountInValue));
                        selectedItemList.get(list_postion).setFoc_percentage(Integer.parseInt(focET.getText().toString()));
                    }

                }
            }
        }
    }

}
