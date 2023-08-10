package detrack.zaryansgroup.com.detrack.activity.utilites;

import android.content.Context;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import detrack.zaryansgroup.com.detrack.activity.activites.WelcomeActivity;
import detrack.zaryansgroup.com.detrack.activity.Model.DeliveryInfo;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.DeliveryItemModel;
import detrack.zaryansgroup.com.detrack.activity.SQLlite.ZEDTrackDB;
import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;

public class SendReturnOrder {

    ArrayList<DeliveryItemModel> itemList;
    Context context;
    ZEDTrackDB db;
    DeliveryInfo deliverInfo;
    SharedPrefs prefs;
    int id;
//    private String jsonString = "{" +
//            "  \"SalesReturn\": {" +
//            "    \"OrderId\": 1," +
//            "    \"CustomerId\": 581," +
//            "    \"VehicleId\": 10," +
//            "    \"SalesReturnNo\": \"1\"," +
//            "    \"OrderNo\": null," +
//            "    \"OrderDateTime\": null," +
//            "    \"SalesMode\": \"Cash\"," +
//            "    \"Status\": \"Returned\"," +
//            "    \"Description\": \"sales Return for text Entry\"," +
//            "    \"TotalQuantity\": 1," +
//            "    \"TotalAmount\": 25," +
//            "    \"DiscountPercentage\": 0," +
//            "    \"Discount\": 0," +
//            "    \"NetTotal\": 25," +
//            "    \"DeliveryDateTime\": null," +
//            "    \"DeliveryAddress\": null," +
//            "    \"RefusedReason\": null," +
//            "    \"CancelledReason\": null," +
//            "    \"RejectedReason\": null," +
//            "    \"Orderby\": null," +
//            "    \"ReturnedDateTime\": \"2018-08-13T14:51:47\"," +
//            "    \"Returnedby\": null," +
//            "    \"Receivedby\": null," +
//            "    \"POBLatitude\": null," +
//            "    \"POBLongitude\": null," +
//            "    \"PODLatitude\": null," +
//            "    \"PODLongitude\": null," +
//            "    \"BookedByTrackingId\": 587," +
//            "    \"AssignedToTrackingId\": 587," +
//            "    \"RouteId\": 11," +
//            "    \"CategoryId\": 0," +
//            "    \"CreatedOn\": \"2018-08-13T14:54:05.703\"," +
//            "    \"CreatedBy\": \"united\"," +
//            "    \"UpdatedOn\": null," +
//            "    \"UpdatedBy\": null," +
//            "    \"CompanySiteId\": 4," +
//            "    \"CompanyId\": 4," +
//            "    \"SyncFlag\": true," +
//            "    \"AppId\": 2" +
//            "  }," +
//            "  \"SalesReturnDetails\": [" +
//            "    {" +
//            "      \"OrderDetailId\": 1," +
//            "      \"OrderId\": 1," +
//            "      \"ItemId\": 9," +
//            "      \"CostCtnPrice\": 1080," +
//            "      \"CostPackPrice\": 180," +
//            "      \"CostPiecePrice\": 15," +
//            "      \"WSCtnPrice\": 1440," +
//            "      \"WSPackPrice\": 240," +
//            "      \"WSPiecePrice\": 20," +
//            "      \"RetailCtnPrice\": 1800," +
//            "      \"RetailPackPrice\": 300," +
//            "      \"RetailPiecePrice\": 25," +
//            "      \"DisplayPrice\": 30," +
//            "      \"CtnQuantity\": 0," +
//            "      \"PackQuantity\": 0," +
//            "      \"PcsQuantity\": 1," +
//            "      \"TotalQuantity\": 1," +
//            "      \"TotalCost\": 15," +
//            "      \"TotalWholesale\": 20," +
//            "      \"TotalRetail\": 25," +
//            "      \"GSTValue\": 0," +
//            "      \"GSTPercentage\": 0," +
//            "      \"FOCQty\": 0," +
//            "      \"FOCValue\": 0," +
//            "      \"FOCPercentage\": 0," +
//            "      \"DiscountPercentage\": 0," +
//            "      \"Discount\": 0," +
//            "      \"NetAmount\": 25," +
//            "      \"RouteId\": 11," +
//            "      \"CategoryId\": 0," +
//            "      \"CreatedOn\": \"2018-08-13T14:54:05.703\"," +
//            "      \"CreatedBy\": \"united\"," +
//            "      \"UpdatedOn\": null," +
//            "      \"UpdatedBy\": null," +
//            "      \"CompanySiteId\": 4," +
//            "      \"CompanyId\": 4," +
//            "      \"SyncFlag\": true," +
//            "      \"AppId\": 2" +
//            "    }" +
//            "  ]" +
//            "}";
//
//    JSONObject myJsonObject;
//
//    {
//        try {
//            myJsonObject = new JSONObject(jsonString);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    public SendReturnOrder(Context context, int id){
        this.context = context;
        itemList = new ArrayList<>();
        db = new ZEDTrackDB(context);
        prefs = new SharedPrefs(context);
        this.id = id;
        deliverInfo = db.getSelectedSQLiteOrderDelivery(id, "True");
    }

    public  JSONObject convertToJsonArray(int id) {
        JSONObject parentObject = new JSONObject();
        JSONObject orderObject = new JSONObject();
        try {
            orderObject.put("OrderId", 1);
            orderObject.put("CustomerId", deliverInfo.getCustomer_id());
            orderObject.put("VehicleId",prefs.getVehicleID());
            orderObject.put("SalesReturnNo","1");
            orderObject.put("OrderNo",JSONObject.NULL);
            orderObject.put("OrderDateTime",JSONObject.NULL);
            orderObject.put("SalesMode","Cash");
            orderObject.put("Status","Returned");
            if(!deliverInfo.getDelivery_description().equals("")) {
                orderObject.put("Description", deliverInfo.getDelivery_description());
            }
            else{
                orderObject.put("Description", "test");
            }
            orderObject.put("TotalQuantity",Math.abs(deliverInfo.getTotal_qty()));
            orderObject.put("TotalAmount",Math.abs(Float.parseFloat(deliverInfo.getTotal_Bill())));
            orderObject.put("DiscountPercentage",Integer.parseInt(deliverInfo.getPercentageDiscount()));
            orderObject.put("Discount",Math.abs(Float.parseFloat(deliverInfo.getDiscount())));
            orderObject.put("NetTotal",Math.abs(Float.parseFloat(deliverInfo.getNetTotal())));
            orderObject.put("DeliveryDateTime",JSONObject.NULL);
            orderObject.put("DeliveryAddress",JSONObject.NULL);
            orderObject.put("RefusedReason",JSONObject.NULL);
            orderObject.put("CancelledReason",JSONObject.NULL);
            orderObject.put("RejectedReason",JSONObject.NULL);
            orderObject.put("Orderby",prefs.getUserName());
            orderObject.put("ReturnedDateTime",deliverInfo.getDelivery_date());
            orderObject.put("Returnedby",JSONObject.NULL);
            orderObject.put("Receivedby",JSONObject.NULL);
            orderObject.put("POBLatitude",JSONObject.NULL);
            orderObject.put("POBLongitude",JSONObject.NULL);
            orderObject.put("PODLatitude",JSONObject.NULL);
            orderObject.put("PODLongitude",JSONObject.NULL);
            orderObject.put("BookedByTrackingId",1);
            orderObject.put("AssignedToTrackingId",1);
            orderObject.put("RouteId",deliverInfo.getRoute());
            orderObject.put("CategoryId",deliverInfo.getCategoryId());
            orderObject.put("CreatedOn",deliverInfo.getDelivery_date());
            orderObject.put("CreatedBy",prefs.getUserName());
            orderObject.put("UpdatedOn",JSONObject.NULL);
            orderObject.put("UpdatedBy",JSONObject.NULL);
            orderObject.put("CompanySiteId",Integer.parseInt(prefs.getCompanySiteID()));
            orderObject.put("CompanyId",Integer.parseInt(prefs.getCompanyID()));
            orderObject.put("SyncFlag",true);
            orderObject.put("AppId",2);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //making json array of items in order
        itemList = db.getSQLiteOrderDeliveryItems(String.valueOf(id), "True",false);
        if (itemList.size() > 0) {

            JSONObject object;
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < itemList.size(); i++) {
                object = new JSONObject();
                DeliveryItemModel model = itemList.get(i);
                try {
                    object.put("OrderDetailId",1);
                    object.put("OrderId", 1);
                    object.put("ItemId", model.getItem_id());
                    object.put("CostCtnPrice", model.getCostCtnPrice());
                    object.put("CostPackPrice", model.getCostPackPrice());
                    object.put("CostPiecePrice", model.getCostPiecePrice());
                    object.put("WSCtnPrice", model.getRetailPiecePrice());
                    object.put("WSPackPrice", model.getWSPackPrice());
                    object.put("WSPiecePrice", model.getWSPiecePrice());
                    object.put("RetailCtnPrice", model.getRetailCtnPrice());
                    object.put("RetailPiecePrice", model.getWSCtnPrice());
                    object.put("RetailPackPrice", model.getRetailPackPrice());
                    object.put("DisplayPrice", model.getDisplayPrice());
                    object.put("CtnQuantity", Math.abs(model.getCtn_qty()));
                    object.put("PackQuantity", Math.abs(model.getPac_qty()));
                    object.put("PcsQuantity", Math.abs(model.getPcs_qty()));
                    object.put("TotalQuantity", Math.abs(model.getTotal_Quantity()));
                    object.put("TotalCost", Math.abs(model.getTotalCostPrice()));
                    object.put("TotalWholesale", Math.abs(model.getTotalwholeSalePrice()));
                    object.put("TotalRetail", Math.abs(model.getTotalRetailPrice()));
                    object.put("GSTValue", Math.abs(model.getItemGstValue()));
                    object.put("GSTPercentage", Math.abs(model.getItemGstPer()));
                    object.put("FOCQty", Math.abs(model.getFoc_qty()));
                    object.put("FOCValue", Math.abs(model.getFoc_value()));
                    object.put("FOCPercentage", Math.abs(model.getFoc_percentage()));
                    object.put("DiscountPercentage", 0);
                    object.put("Discount", Math.abs(model.getItem_discount()));
                    object.put("NetAmount", Math.abs(model.getNetTotalRetailPrice()));
                    object.put("RouteId", model.getRoute_id());
                    object.put("CategoryId", model.getCategoryId());
                    object.put("CreatedOn", deliverInfo.getDelivery_date());
                    object.put("CreatedBy", prefs.getUserName());
                    object.put("UpdatedOn", deliverInfo.getDelivery_date());
                    object.put("UpdatedBy", JSONObject.NULL);
                    object.put("CompanySiteId", prefs.getCompanySiteID());
                    object.put("CompanyId", prefs.getCompanyID());
                    object.put("SyncFlag", true);
                    object.put("AppId", 2);

                    jsonArray.put(object);
                } catch (JSONException e) {
                }
            }
            try {
                parentObject.put("SalesReturn", orderObject);
                parentObject.put("SalesReturnDetails", jsonArray);
                Log.d("itemArray", parentObject.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return parentObject;
    }


    //Add new return order api
    public void addNewSalesReturn(String url) {
        Toast.makeText(context, "Sending order", Toast.LENGTH_SHORT).show();
        Log.d("returnUrl", url +"\n\n"+convertToJsonArray(id));


        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, url, convertToJsonArray(id),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("saleReturnResponse", response.toString());
                        try {
                            String status = response.getString("status");
                            String message = response.getString("message");
                            if(status.equals("success")){
                                //todo update order status to send to server
                                Utility.Toast(context, "Successful...");
                                db.UpdateSynChanges(deliverInfo.getDelivery_id());
                                Intent intent = new Intent(context, WelcomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                            }
                            else{
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("saleReturnError", error.getMessage()+"");
                if (error instanceof ServerError) {
                    Toast.makeText(context, "Server error occurred", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(context, "Connection timeout error", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                String creds = String.format("%s:%s", "ZedDeliveryAdmin", "Z3d7nuW8aei");
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.NO_WRAP);

                params.put("X-ApiKey","S+4M8X;->iK4T>8mR7x]3)(|=%HkwvCY4aoPqrH][`Q?=dI^)XDPnKBbZYu&Uo+");
                params.put("Authorization", auth);
                Utility.logCatMsg(params.toString());
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request_json);
    }
}
