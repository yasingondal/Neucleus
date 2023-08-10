package detrack.zaryansgroup.com.detrack.activity.activites.TestAct;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import detrack.zaryansgroup.com.detrack.R;
import detrack.zaryansgroup.com.detrack.activity.Adapter.ItemListNewAdapter;
import detrack.zaryansgroup.com.detrack.activity.Adapter.OrderItemAdapterTest;
import detrack.zaryansgroup.com.detrack.activity.Adapter.Undelivered_Adapter;
import detrack.zaryansgroup.com.detrack.activity.Map.GPSTracker;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.DeliveryItemModel;
import detrack.zaryansgroup.com.detrack.activity.Model.DeliveryInfo;
import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;
import detrack.zaryansgroup.com.detrack.activity.activites.AddImages;
import detrack.zaryansgroup.com.detrack.activity.activites.POD_DashBoard;
import detrack.zaryansgroup.com.detrack.activity.activites.SelectProductActivity;
import detrack.zaryansgroup.com.detrack.activity.activites.TakeOrder;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import detrack.zaryansgroup.com.detrack.activity.viewmodels.TestPOD_ViewModel;
import detrack.zaryansgroup.com.detrack.activity.viewmodels.Welcome_ViewModel;
import detrack.zaryansgroup.com.detrack.databinding.TestActivityPodBinding;


public class TestPOD extends AppCompatActivity {


    TestPOD_ViewModel mViewmodel;
    TestActivityPodBinding mBinding;
    DeliveryInfo deliverInfo = new DeliveryInfo();
    String Delivery_id, IsNew, Discount_percentage;
    SharedPrefs prefs;
    ArrayList<DeliveryItemModel> orderList = new ArrayList<>();
    ArrayList<DeliveryItemModel> selectedItemList = new ArrayList<>();
    OrderItemAdapterTest mAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.test_activity_pod);
        mViewmodel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(TestPOD_ViewModel.class);
        mBinding.setPodViewModel(mViewmodel);

        prefs = new SharedPrefs(this);
        Delivery_id = getIntent().getStringExtra("Delivery_id");
        IsNew = getIntent().getStringExtra("IsNew");
        if (IsNew.equals("False")) {
            mBinding.currentlacotionPodCB.setVisibility(View.VISIBLE);
        }

        mViewmodel.getSelectedSQLiteOrderDelivery(Integer.parseInt(Delivery_id),IsNew);
        mViewmodel.loadOrdersById(Delivery_id,IsNew);
        observers();
        clickListners();

    }

    private void observers() {
        mViewmodel.getDeliverinfoData().observe(this, new Observer<DeliveryInfo>() {
            @Override
            public void onChanged(DeliveryInfo deliveryInfo) {
                deliverInfo = deliveryInfo;
                FillOrderDetails(deliveryInfo);
            }
        });

        mViewmodel.getOrderItems().observe(this, new Observer<ArrayList<DeliveryItemModel>>() {
            @Override
            public void onChanged(ArrayList<DeliveryItemModel> deliveryItemModels) {
                orderList = deliveryItemModels;
//                Log.d("orderlistsize","="+orderList.size());
                FillOrders();
            }
        });
    }

    private void FillOrders() {
        mAdapter = new OrderItemAdapterTest(this,orderList);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView.setAdapter(mAdapter);
    }

    private void FillOrderDetails(DeliveryInfo deliverInfo) {

        Log.d("DeliveryInfo", deliverInfo.getDelivery_status() + "");

        mBinding.addressTV.setText(deliverInfo.getDelivery_address());
        mBinding.orderTo.setText(deliverInfo.getDeliver_to_name());
        mBinding.totalbill.setText(deliverInfo.getTotal_Bill() + " "+prefs.getCurrency());
        mBinding.perdiscount.setText(deliverInfo.getPercentageDiscount() + "% = " + deliverInfo.getDiscount() + " "+prefs.getCurrency());
        mBinding.netTotal.setText((Float.parseFloat(deliverInfo.getTotal_Bill()) - Float.parseFloat(deliverInfo.getDiscount())) +" "+new SharedPrefs(this).getCurrency());
        if (IsNew.equals("False")) {
            mBinding.dileveryCollectionTime.setText(deliverInfo.getDelivery_start_time() + " TO " + deliverInfo.getDelivery_end_time());
        } else {
            mBinding.dileveryCollectionTime.setText("");
        }
        if (deliverInfo.getDelivery_status().equals("Rejected")) {
            mBinding.statusTV.setTextColor(Color.RED);
        }
        if (deliverInfo.getIsPod_sync().toString().equals("1")) {
            mBinding.serverStatusTV.setVisibility(View.VISIBLE);
            mBinding.serverStatusTV.setTextColor(Color.parseColor("#E55B3C"));
            if (IsNew.equals("True"))
                mBinding.serverStatusTV.setText("Order Not Send");
            else
                mBinding.serverStatusTV.setText("POD Not Send");
        } else if (deliverInfo.getIsPod_sync().toString().equals("2")) {
            mBinding.serverStatusTV.setVisibility(View.VISIBLE);
            mBinding.serverStatusTV.setTextColor(Color.parseColor("#728C00")); //green color
            if (IsNew.equals("True"))
                mBinding.serverStatusTV.setText("Order Sent");
            else
                mBinding.serverStatusTV.setText("POD Sent");
        } else {
            mBinding.serverStatusTV.setVisibility(View.GONE);
        }
        mBinding.statusTV.setText(deliverInfo.getDelivery_status());
        mBinding.instructionTv.setText(deliverInfo.getDelivery_description());
    }

    private void clickListners() {

        mBinding.currentlacotionPodCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mBinding.currentlacotionPodCB.isChecked()) {
                    GPSTracker gps = new GPSTracker(TestPOD.this);
                    if (gps.canGetLocation()) {
                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();
                        if (latitude > 0.0) {
                            mBinding.podlatlng.setText("Your POD lat lng is " + latitude + " , " + longitude);
                            deliverInfo.setPod_lng(longitude + "");
                            deliverInfo.setPod_lat(latitude + "");

                            mViewmodel.updateLatLng(deliverInfo);
                            mBinding.currentlacotionPodCB.setVisibility(View.GONE);
                        } else {
                            mBinding.podlatlng.setText("Please try again.");
                        }
                    } else {
                        gps.showSettingsAlert();
                    }
                    mBinding.currentlacotionPodCB.setChecked(false);
                }
            }
        });


        mBinding.tveditorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((deliverInfo.getDelivery_status().equals("Delivered") || deliverInfo.getDelivery_status().equals("Booking")) && deliverInfo.getIsPod_sync().toString().equals("2")){

                    Toast.makeText(TestPOD.this, " Order already sent", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (new SharedPrefs(TestPOD.this).getView().equals("secondView")) {
                        startActivity(new Intent(TestPOD.this, TakeOrder.class).putExtra("addOrder","update").putExtra("deliverInfo",deliverInfo).putExtra("isNew",IsNew));
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    }
                    else{
                        Intent intent1 = new Intent(TestPOD.this, SelectProductActivity.class);
                        intent1.putExtra("addOrder","update");
                        intent1.putExtra("deliverInfo",deliverInfo);
                        intent1.putExtra("isNew",IsNew);
                        startActivity(intent1);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    }
                }
            }
        });

        mBinding.tvusernote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deliverInfo.getIsPod_sync().toString().equals("2"))
                    Utility.Toast(TestPOD.this, "Cannot do this action because order is sent");
                else
                    UserNoteDiloge();
            }
        });
        
        mBinding.layoutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(TestPOD.this, AddImages.class);
                in.putExtra("OrderId", deliverInfo.getDelivery_id() + "");
                if (IsNew.toString().equals("True"))
                    in.putExtra("imgType", "POB");
                else
                    in.putExtra("imgType", "POD");
                startActivity(in);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });

    }


    private void UserNoteDiloge() {
        View view = getLayoutInflater().inflate(R.layout.add_new_note, null);
        final EditText userText = view.findViewById(R.id.usernote);
        if (deliverInfo.getNote() != null)
            userText.setText(deliverInfo.getNote());
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TestPOD.this);
        View view1 = getLayoutInflater().inflate(R.layout.dialog_custom_title, null);
        TextView tvCustomTitle = view1.findViewById(R.id.tvCustomTitle);
        tvCustomTitle.setText("Add Note");
        alertDialogBuilder.setCustomTitle(view1);
        alertDialogBuilder
                .setCancelable(false).setView(view)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (deliverInfo.getDelivery_status().equals("Delivered") && !IsNew.equals("Return")) {
                            Utility.Toast(TestPOD.this, "Cannot change note because order is Delivered");
                        } else if (deliverInfo.getDelivery_status().equals("Returned"))
                            Utility.Toast(TestPOD.this, "Cannot change note because order is Returned");
                        else {
                            if (!userText.getText().equals("")) {
                                deliverInfo.setNote(userText.getText().toString());
//                                if (db.UpdateNote(deliverInfo) == 1)
//                                    Utility.logCatMsg("Note updated successfully in SQLite");
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




}
