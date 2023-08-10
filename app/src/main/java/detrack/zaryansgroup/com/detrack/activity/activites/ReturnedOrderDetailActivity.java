package detrack.zaryansgroup.com.detrack.activity.activites;

import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import detrack.zaryansgroup.com.detrack.activity.Adapter.OrderDetailRecyclerViewAdapter;
import detrack.zaryansgroup.com.detrack.activity.Map.GPSTracker;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.DeliveryItemModel;
import detrack.zaryansgroup.com.detrack.activity.Model.RegisterCustomerModel.RegisterdCustomerModel;
import detrack.zaryansgroup.com.detrack.activity.SQLlite.ZEDTrackDB;
import detrack.zaryansgroup.com.detrack.activity.utilites.ConnectionDetector;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import detrack.zaryansgroup.com.detrack.R;

public class ReturnedOrderDetailActivity extends AppCompatActivity {

    RegisterdCustomerModel getMasterObject;
    RecyclerView returnOrderDetailRV;
    Button btnSaveReturn;
    GPSTracker tracker;

    ArrayList<DeliveryItemModel> itemModelList;
    ProgressDialog pd;
    ZEDTrackDB db;
    OrderDetailRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_returned_order_detail);

        init();
    }


    private void init(){

        getMasterObject = (RegisterdCustomerModel) getIntent().getSerializableExtra("orderMaster");
        Log.d("master",getMasterObject.getOrderNumber());
        itemModelList = new ArrayList<>();
        pd = new ProgressDialog(this);
        db = new ZEDTrackDB(this);
        tracker = new GPSTracker(this);
        btnSaveReturn = findViewById(R.id.btnSaveReturn);

        returnOrderDetailRV = findViewById(R.id.returnOrderDetailRV);
        if (ConnectionDetector.isConnectingToInternet(ReturnedOrderDetailActivity.this)) {
            //Todo call api
            getOrderDetail(Utility.BASE_LIVE_URL+"api/Order/GetOrderByID?OrderID="+getMasterObject.getOrderNumber());
            Log.d("orderDetailUrl",Utility.BASE_LIVE_URL+"api/Order/GetOrderByID?OrderID="+getMasterObject.getOrderNumber());

        } else {
            Toast.makeText(ReturnedOrderDetailActivity.this, "Check network connection and try again", Toast.LENGTH_SHORT).show();
        }
        btnSaveReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("new record", "deliveredQty: "+itemModelList.get(0).getDelivered_Quantity());
                getMasterObject.setDeliveryStatus("Returned");
                Log.d("deliveryStatus", getMasterObject.getDeliveryStatus());

                if(tracker.canGetLocation()){

                    getMasterObject.setPobLat(String.valueOf(tracker.getLatitude()));
                    getMasterObject.setPobLng(String.valueOf(tracker.getLongitude()));


                    adapter.insertRecord(itemModelList, getMasterObject);
                    Intent intent = new Intent(ReturnedOrderDetailActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(ReturnedOrderDetailActivity.this, "Turn on Gps and try again", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void getOrderDetail(String url){

        itemModelList.clear();
        pd.setMessage("Loading Detail");
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                pd.dismiss();
                Log.d("orderDetailResponse", response);
                try {
                    JSONObject parent = new JSONObject(response);
                    JSONArray tableArray = parent.getJSONArray("Table");
                    if(tableArray.length() > 0){
                        for(int i = 0; i < tableArray.length(); i++){
                            JSONObject child = tableArray.getJSONObject(i);
                            DeliveryItemModel model = new DeliveryItemModel();

                            model.setOrder_item_id(child.getInt("OrderDetailId"));
                            model.setItem_id(child.getInt("ItemId"));
                            model.setCostCtnPrice(child.getInt("CostCtnPrice"));
                            model.setCostPackPrice(child.getInt("CostPackPrice"));
                            model.setCostPiecePrice(child.getInt("CostPiecePrice"));
                            model.setRetailPiecePrice(child.getInt("WSCtnPrice"));
                            model.setWSPackPrice(child.getInt("WSPackPrice"));
                            model.setWSPiecePrice(child.getInt("WSPiecePrice"));
                            model.setRetailCtnPrice(child.getInt("RetailCtnPrice"));
                            model.setRetailPackPrice(child.getInt("RetailPackPrice"));
                            model.setWSCtnPrice(child.getInt("RetailPiecePrice"));
                            model.setDisplayPrice(child.getInt("DisplayPrice"));
                            model.setCtn_qty(child.getInt("CtnQuantity"));
                            model.setPac_qty(child.getInt("PackQuantity"));
                            model.setPcs_qty(child.getInt("PcsQuantity"));
                            model.setTotal_Quantity(child.getInt("TotalQuantity"));
                            model.setTotalCostPrice(child.getInt("TotalCost"));
                            model.setTotalwholeSalePrice(child.getInt("TotalWholesale"));
                            model.setTotalRetailPrice(child.getInt("TotalRetail"));
                            model.setItemGstValue(child.getInt("GSTValue"));
                            model.setItemGstPer(child.getInt("GSTPercentage"));
                            model.setFoc_qty(child.getInt("FOCQty"));
                            model.setFoc_value(child.getInt("FOCValue"));
                            model.setFoc_percentage(child.getInt("FOCPercentage"));
                            model.setFocType(child.getString("FOCType"));
                            model.setItem_discount(child.getInt("DiscPercentage"));
                            model.setNetTotalRetailPrice(child.getInt("NetAmount"));
                            model.setReject_ctn_qty(child.getInt("RejectCtnQty"));
                            model.setReject_pac_qty(child.getInt("RejectPackQty"));
                            model.setReject_pcs_qty(child.getInt("RejectPcsQty"));
                            model.setDeliver_ctn_qty(child.getInt("DeliverCtnQty"));
                            model.setDeliver_pac_qty(child.getInt("DeliverPackQty"));
                            model.setDeliver_pcs_qty(child.getInt("DeliverPcsQty"));
                            model.setCategoryId(child.getInt("CategoryId"));
                            model.setRoute_id(child.getInt("RouteId"));
                            model.setName(child.getString("Name"));
                            itemModelList.add(model);
                        }

                        adapter = new OrderDetailRecyclerViewAdapter(ReturnedOrderDetailActivity.this, itemModelList, getMasterObject);
                        returnOrderDetailRV.setAdapter(adapter);
                        returnOrderDetailRV.setLayoutManager(new LinearLayoutManager(ReturnedOrderDetailActivity.this));

                    }
                    else{
                        Toast.makeText(ReturnedOrderDetailActivity.this, "No items found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.d("returnorder,","incatch="+e.toString());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("OrderDetailError",error.getMessage()+"");
                pd.dismiss();
                if (error instanceof ServerError) {
                    Toast.makeText(ReturnedOrderDetailActivity.this, "Server error occurred", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(ReturnedOrderDetailActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(ReturnedOrderDetailActivity.this, "Connection timeout error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        pd.show();
        queue.add(request);

    }
}
