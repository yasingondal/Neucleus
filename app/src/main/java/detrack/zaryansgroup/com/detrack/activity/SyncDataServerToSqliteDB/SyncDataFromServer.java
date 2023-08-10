package detrack.zaryansgroup.com.detrack.activity.SyncDataServerToSqliteDB;

import android.content.Context;
import android.util.Log;

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
import java.util.List;

import detrack.zaryansgroup.com.detrack.activity.Model.BankModels.CompanyWiseBanksModel;
import detrack.zaryansgroup.com.detrack.activity.Model.DeliveryInfo;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.DeliveryItemModel;
import detrack.zaryansgroup.com.detrack.activity.Model.VisitStatusesModel;
import detrack.zaryansgroup.com.detrack.activity.SQLlite.ZEDTrackDB;
import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;
import detrack.zaryansgroup.com.detrack.activity.activites.BonusPolicyWork.BonusPolicyModel;
import detrack.zaryansgroup.com.detrack.activity.activites.DiscountPolicyWork.DiscountPolicyModel;
import detrack.zaryansgroup.com.detrack.activity.utilites.ConnectionDetector;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import timber.log.Timber;

/**
 * Created by 6520 on 4/12/2016.
 */
public class SyncDataFromServer {

    // Assignment demo work By M.Y
    ArrayList<CompanyWiseBanksModel> rFeedBanksDetailsList;

    ArrayList<DeliveryInfo> feedItems;
    ArrayList<DeliveryItemModel> feedDeliveryItems;

    ArrayList<DiscountPolicyModel> feedDiscountPolicyList;
    ArrayList<BonusPolicyModel> feedBonusPolicyList;

    Context context;
    SharedPrefs prefs;
    ZEDTrackDB db;
    String allDelivery_id;
    List<Integer> orderIdList;

    public SyncDataFromServer(Context context) {
        this.context = context;
        feedItems = new ArrayList<>();
        feedDeliveryItems = new ArrayList<>();
        feedDiscountPolicyList = new ArrayList<>();

        feedBonusPolicyList = new ArrayList<>();

        prefs = new SharedPrefs(context);
        db = new ZEDTrackDB(context);
        orderIdList = new ArrayList<>();

        //Assignment demo work By M.Y
        rFeedBanksDetailsList = new ArrayList<>();

    }


    public void  GetJobs() {
        if (ConnectionDetector.isConnectingToInternet(context)) {
            getTodayJob(Utility.BASE_LIVE_URL+"api/job/GetTodayJobList?ContactId=" + prefs.getEmployeeID() + "&orderId=0");
            FetchAllBanksCompanyWiseDetails(Utility.BASE_LIVE_URL+"api/company/BankDetails?Company_id=" + prefs.getCompanyID() + "&Compnay_siteId="+prefs.getCompanySiteID());
            rFetchDiscountPolicyData(Utility.BASE_LIVE_URL+"api/Company/GetCompanyItemsPolicies?Company_id="+prefs.getCompanyID()+"&CompanySiteId="+prefs.getCompanySiteID());
            rFetchBonusPolicyData(Utility.BASE_LIVE_URL+"api/Company/GetCompanyItemsBonusPolicies?Company_id="+prefs.getCompanyID()+"&CompanySiteId="+prefs.getCompanySiteID());
        }
    }


    private void rFetchDiscountPolicyData(String url) {
        Timber.d("Discount Policy Url "+url);
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject parentObject = new JSONObject(response);
                    JSONArray tableArray = parentObject.getJSONArray("DiscountPolicies");
                    if (tableArray.length() > 0) {
                        ParseJasonFeedDiscountPolicy(tableArray);
                    }

                } catch (JSONException e) {
                    //Check Point
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Timber.d("apiresponseerror "+error.getMessage() + "no response comming from bank api");
                if (error instanceof ServerError) {
                    Timber.d("apiresponseerror "+error.getMessage() + "Server Error");
                } else if (error instanceof NetworkError) {
                    Timber.d("apiresponseerror "+error.getMessage() + "Network Error");
                } else if (error instanceof TimeoutError) {
                    Timber.d("apiresponseerror"+error.getMessage() + "Connection Timeout Error");
                }
            }
        });
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }


    private void ParseJasonFeedDiscountPolicy(JSONArray tableArray) {

        try {

            int     DiscountPolicyId;
            String  Type;
            int     TypeId;
            String  DiscountType;
            float   TargetValue;
            int     TargetQty;
            float   DiscountPercentage;
            float   DiscountValue;
            float   AdditionalDiscountPercentage;
            float   AdditionalDiscountValue;
            String  StopOtherDiscount;
            String  IsClaimable;
            String  StartDate;
            String  EndDate;
            int     GroupDivId;
            int     DivId;
            int     ItemId;

            feedDiscountPolicyList.clear();

            for (int i = 0; i < tableArray.length(); i++) {
                JSONObject feedObj = (JSONObject) tableArray.get(i);

                     DiscountPolicyId = feedObj.getInt("DiscountPolicyId");
                     Type = feedObj.getString("Type");
                     TypeId = feedObj.getInt("TypeId"); ;
                     DiscountType = feedObj.getString("DiscountType"); ;
                     TargetValue = Float.parseFloat(String.valueOf(feedObj.getDouble("TargetValue")));
                     TargetQty = feedObj.getInt("TargetQty");
                     DiscountPercentage = Float.parseFloat(String.valueOf(feedObj.getDouble("DiscountPercentage")));
                     DiscountValue = Float.parseFloat(String.valueOf(feedObj.getDouble("DiscountValue")));
                     AdditionalDiscountPercentage =Float.parseFloat(String.valueOf(feedObj.getDouble("AdditionalDiscountPercentage")));
                     AdditionalDiscountValue = Float.parseFloat(String.valueOf(feedObj.getDouble("AdditionalDiscountValue")));
                     StopOtherDiscount = feedObj.getString("StopOtherDiscount");;
                     IsClaimable = feedObj.getString("IsClaimable");;
                     StartDate = feedObj.getString("StartDate");;
                     EndDate = feedObj.getString("EndDate");;
                     GroupDivId = feedObj.getInt("GroupDivId"); ;
                     DivId = feedObj.getInt("DivId");
                     ItemId = feedObj.getInt("ItemId");


                feedDiscountPolicyList.add(new DiscountPolicyModel(
                        DiscountPolicyId,Type,TypeId,DiscountType,TargetValue,TargetQty,DiscountPercentage,DiscountValue,AdditionalDiscountPercentage,
                        AdditionalDiscountValue,StopOtherDiscount,IsClaimable,StartDate,EndDate,GroupDivId,DivId,ItemId
                ));

            }



            if (db.insertDiscountPolciies(feedDiscountPolicyList) != 0) {
                Timber.d("Discount insertion true");
            }else {
                Timber.d("Discount Insertion False");
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Utility.logCatMsg("Feed item error" + e);
           Timber.d("ParseJasonFeedDiscountPolicy exception is "+e.getMessage());
        }
    }



    private void rFetchBonusPolicyData(String url) {
        Timber.d("Bonus Policy Url "+url);
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject parentObject = new JSONObject(response);
                    JSONArray tableArray = parentObject.getJSONArray("BonusPolicies");
                    if (tableArray.length() > 0) {
                        ParseJasonFeedBonusPolicy(tableArray);
                    }

                } catch (JSONException e) {
                    //Check Point
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Timber.d("apiresponseerror "+error.getMessage() + "no response comming from Bonus api");
                if (error instanceof ServerError) {
                    Timber.d("apiresponseerror "+error.getMessage() + "Server Error");
                } else if (error instanceof NetworkError) {
                    Timber.d("apiresponseerror "+error.getMessage() + "Network Error");
                } else if (error instanceof TimeoutError) {
                    Timber.d("apiresponseerror"+error.getMessage() + "Connection Timeout Error");
                }
            }
        });
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }



    private void ParseJasonFeedBonusPolicy(JSONArray tableArray) {

        feedBonusPolicyList.clear();

        try {

            int     BonusPolicesId;
            int     TypeId ;
            int     TargetQty;
            int     Bonus;
            int     IncentiveItemId;
            int     ItemId;
            String  Type;
            String  DiscountType;
            String  IsClaimable;
            String  StartDate;
            String  EndDate;
            String  SubType;
            String  MultiItemsName;
            String  MultiItemsIdes;
            float   OurShare;


            for (int i = 0; i < tableArray.length(); i++) {

                JSONObject feedObj = (JSONObject) tableArray.get(i);

                     BonusPolicesId = feedObj.getInt("BonusPolicesId");
                     TypeId = feedObj.getInt("TypeId");
                     TargetQty = feedObj.getInt("TargetQty");
                     Bonus = feedObj.getInt("Bonus");
                     IncentiveItemId = feedObj.getInt("IncentiveItemId");
                     ItemId = feedObj.getInt("ItemId");
                     Type = feedObj.getString("Type");
                     DiscountType = feedObj.getString("DiscountType");
                     IsClaimable = String.valueOf(feedObj.getBoolean("IsClaimable"));
                     StartDate = feedObj.getString("StartDate");
                     EndDate = feedObj.getString("EndDate");
                     MultiItemsName = feedObj.getString("MultiItemsName");
                     SubType = feedObj.getString("SubType");
                     MultiItemsIdes = feedObj.getString("MultiItemsIdes"); //done
                     OurShare = Float.parseFloat(String.valueOf(feedObj.getDouble("OurShare")));

                feedBonusPolicyList.add(new BonusPolicyModel(BonusPolicesId,TypeId,TargetQty,Bonus,ItemId,IncentiveItemId,Type,DiscountType,OurShare,IsClaimable,StartDate,EndDate,SubType,MultiItemsName,MultiItemsIdes));


            }



            if(feedBonusPolicyList.size() > 0)
            {
                if (db.insertBonusPolicies(feedBonusPolicyList) != 0) {
                    Timber.d("Bonus insertion true");
                }else {
                    Timber.d("Bonus Insertion False");
                }
            }
            else{
                Timber.d("Bonus Policy size have no data");
            }



        } catch (JSONException e) {
            e.printStackTrace();
            Utility.logCatMsg("Feed item error" + e);
            Timber.d("ParseJasonFeedDiscountPolicy exception is "+e.getMessage());
        }

    }


    private void FetchAllBanksCompanyWiseDetails(String BankApiUrl) {
        Timber.d("Banks Url "+BankApiUrl);

        StringRequest request = new StringRequest(BankApiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject parentObject = new JSONObject(response);
                    JSONArray tableArray = parentObject.getJSONArray("Table");
                    if (tableArray.length() > 0) {
                        ParseJasonFeedBanks(tableArray);
                    }

                } catch (JSONException e) {
                    //Check Point
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("apiresponseerror", error.getMessage() + "no response comming from bank api");
                if (error instanceof ServerError) {
                    Log.d("apiresponseerror", error.getMessage() + "Server Error");
                } else if (error instanceof NetworkError) {
                    Log.d("apiresponseerror", error.getMessage() + "Network Error");
                } else if (error instanceof TimeoutError) {
                    Log.d("apiresponseerror", error.getMessage() + "Connection Timeout Error");
                }
            }
        });
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }


    //Assignment work doing banks Module M.Y
    private void ParseJasonFeedBanks(JSONArray feedArray) {
        try {
            rFeedBanksDetailsList.clear();
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                int BankId = feedObj.getInt("BankID");
                String BankName = feedObj.getString("BankName");
                String BankAccNo = feedObj.getString("BankAccountNbr");
                String BankAccType = feedObj.getString("BankAccountType");
                String Address = feedObj.getString("Address");
                rFeedBanksDetailsList.add(new CompanyWiseBanksModel(BankId,BankName,BankAccNo,BankAccType,Address));

            }


            if (db.insertCompanyWiseBankDetails(rFeedBanksDetailsList) != 0) {
                Log.d("bankdatainserted", "true=>" + rFeedBanksDetailsList.size());
            }else {
                Log.d("bankdatainserted","Fail");
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Utility.logCatMsg("Feed item error" + e);
            Log.d("feedarrayissue", "=>"+e);
        }
    }



    private void getTodayJob(String url) {
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject parentObject = new JSONObject(response);
                    JSONArray tableArray = parentObject.getJSONArray("Table");
                    if (tableArray.length() > 0) {
                        parseJsonFeed1(tableArray);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("jobError", error.getMessage() + "");
                if (error instanceof ServerError) {
                    //Toast.makeText(context, "Server error occurred", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    //Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    //Toast.makeText(context, "Connection timeout error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }


    private void parseJsonFeed1(JSONArray feedArray) {
        try {
            feedItems.clear();
            for (int i = 0; i < feedArray.length(); i++) {

                JSONObject feedObj = (JSONObject) feedArray.get(i);
                DeliveryInfo model = new DeliveryInfo();
                model.setServer_Delivery_id(feedObj.getInt("OrderId"));

                allDelivery_id = feedObj.getInt("OrderId") + "";
                orderIdList.add(feedObj.getInt("OrderId"));
                model.setEmp_id(0);
                model.setCustomer_id(feedObj.getInt("CustomerId"));
                model.setVehicle_id(feedObj.getInt("VehicleId"));
                model.setOrderNumber(feedObj.getString("OrderNo"));
                model.setSerialNo(feedObj.getString("SerialNo").equals("null") ? "0" : feedObj.getString("SerialNo"));
                String dateTime = feedObj.getString("OrderDateTime");
                dateTime = dateTime.replace("T", " ");
                model.setDelivery_date(dateTime);
                model.setSalemode(feedObj.getString("SalesMode"));
                model.setPob_lat(String.valueOf(feedObj.getString("POBLatitude").equals("null") ? "0.0" : feedObj.getInt("POBLatitude")));
                model.setPob_lng(String.valueOf(feedObj.getString("POBLongitude").equals("null") ? "0.0" : feedObj.getInt("POBLongitude")));

                if (!feedObj.getString("CategoryId").equals("null"))
                    model.setCategoryId(feedObj.getInt("CategoryId"));
                else
                    model.setCategoryId(0);
                model.setRoute(feedObj.getInt("RouteId"));
                model.setDelivery_status(feedObj.getString("Status"));
                model.setDelivery_description(feedObj.getString("Description").equals("null") ? "" : feedObj.getString("Description"));
                if (feedObj.getString("TotalQuantity").equals("null"))
                    model.setTotal_qty(0);
                else
                    model.setTotal_qty(feedObj.getInt("TotalQuantity"));
                model.setDelivery_address(feedObj.getString("DeliveryAddress").equals("null") ? "" : feedObj.getString("DeliveryAddress"));
                model.setDelivery_to_mobile(feedObj.getString("Phone").equals("null") ? "0" : feedObj.getString("Phone"));

                //Bank Assignment work
                model.setCashDespositedBankId(feedObj.getInt("BankId"));

                model.setDeliver_to_name(feedObj.getString("Name").equals("null") ? "" : feedObj.getString("Name"));
                model.setRejected_Reason("");
                model.setReceivedBy("");
                model.setDeliver_lat(String.valueOf(feedObj.getString("Latitude").equals("null") ? "0.0" : feedObj.getInt("Latitude")));
                model.setDeliver_lng(String.valueOf(feedObj.getString("Longitude").equals("null") ? "0.0" : feedObj.getInt("Longitude")));
                model.setPod_lat("0.0");
                model.setPod_lng("0.0");
                model.setNote(null);
                model.setTotal_Bill(String.valueOf(feedObj.getInt("TotalAmount") == 0 ? "0" : feedObj.getInt("TotalAmount")));
                model.setDiscount(String.valueOf(feedObj.getInt("Discount") == 0 ? "0" : feedObj.getInt("Discount")));
                model.setPercentageDiscount(String.valueOf(feedObj.getInt("DiscPercentage") == 0 ? "0" : feedObj.getInt("DiscPercentage") + ""));
                model.setNetTotal(String.valueOf(feedObj.getInt("NetTotal") == 0 ? "0" : feedObj.getInt("NetTotal")));
                model.setGst(String.valueOf(feedObj.getDouble("GSTValue") == 0 ? "0" : feedObj.getDouble("GSTValue")));
                feedItems.add(model);

            }

            if (db.insertOrderDelivery(feedItems, "True") != 0) {
                Timber.d("ordersInserted is true    " + feedItems.size());
            }else {
                Log.d("serverorder","Fail");
            }

            if (orderIdList.size() > 0) {
                getJobItems(0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Utility.logCatMsg("Feed item error" + e);
        }
    }

    private void getJobItems(final int i) {
        String url = Utility.BASE_LIVE_URL+"api/Job/GetTodayJobItems?Delivery_id=" + orderIdList.get(i);
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (i != orderIdList.size() - 1)
                    getJobItems(i + 1);


                Log.d("ItemResponse", response);
                try {
                    JSONObject parentObject = new JSONObject(response);
                    JSONArray tableArray = parentObject.getJSONArray("Table");
                    if (tableArray.length() > 0) {
                        parseJsonFeedItems(tableArray);
                    } else {
                        Log.d("Item", "no item found for this job");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (i != orderIdList.size() - 1)
                    getJobItems(i + 1);
                if (error instanceof ServerError) {
                    //Toast.makeText(context, "Server error occurred", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    //Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    //Toast.makeText(context, "Connection timeout error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
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
                // extra fileds
                model.setReturn_Quantity(0);
                model.setRejected_Quantity(0);
                model.setDisplayPrice(feedObj.getInt("DisplayPrice"));
                model.setItemGstValue(feedObj.getInt("GSTValue"));
                model.setTaxCode(feedObj.getString("TaxCode"));
                Utility.logCatMsg("GST Value : " + model.getItemGstValue());
                model.setItemGstPer(feedObj.getInt("GSTPercentage"));
                if (!feedObj.getString("TotalWholesale").equals("null"))
                    model.setDelivered_Quantity(feedObj.getInt("TotalWholesale"));
                else
                    model.setDelivered_Quantity(0);

                model.setRejectReason("");
                feedDeliveryItems.add(model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Utility.logCatMsg("Feed Delivery item error" + e);
        }
        if (feedDeliveryItems.size() > 0) {
            for(int i = 0; i < feedDeliveryItems.size(); i++){
                //todo delete previous orders
                db.deleteOrderItems(String.valueOf(feedDeliveryItems.get(i).getOrder_item_id()),"True");
            }
        }

    }

}
