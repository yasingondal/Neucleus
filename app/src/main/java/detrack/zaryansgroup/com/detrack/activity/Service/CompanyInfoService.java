package detrack.zaryansgroup.com.detrack.activity.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

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

import detrack.zaryansgroup.com.detrack.activity.Model.BankModels.CompanyWiseBanksModel;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyRouteModel.RouteModel;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.DeliveryItemModel;
import detrack.zaryansgroup.com.detrack.activity.Model.DistributorModel.DistributorModel;
import detrack.zaryansgroup.com.detrack.activity.Model.Params;
import detrack.zaryansgroup.com.detrack.activity.Model.RegisterCustomerModel.RegisterdCustomerModel;
import detrack.zaryansgroup.com.detrack.activity.Model.VisitStatusesModel;
import detrack.zaryansgroup.com.detrack.activity.SQLlite.ZEDTrackDB;
import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;
import detrack.zaryansgroup.com.detrack.activity.activites.BonusPolicyWork.BonusPolicyModel;
import detrack.zaryansgroup.com.detrack.activity.activites.DiscountPolicyWork.DiscountPolicyModel;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import timber.log.Timber;

/**
 * Created by 6520 on 4/5/2016.
 */
public class CompanyInfoService extends Service {
    ZEDTrackDB db;

    SharedPrefs prefs;
    ArrayList<RegisterdCustomerModel> feedRegCustomerList;
    ArrayList<DeliveryItemModel> feedCompanyItemList;
    ArrayList<Params> paramsArrayList;
    ArrayList<RouteModel> routelist;
    ArrayList<VisitStatusesModel> rFeedVisitsList;



    ArrayList<DiscountPolicyModel> feedDiscountPolicyList;
    ArrayList<BonusPolicyModel> feedBonusPolicyList;

    ArrayList<DistributorModel> feedDistributorList;


    //Assignment work By M.Y
    ArrayList<CompanyWiseBanksModel> feedCompanywiseBanksList;


    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Utility.logCatMsg("Syncing start..");
        db=new ZEDTrackDB(this);
        prefs = new SharedPrefs(this);

        Utility.logCatMsg("Service Started");
        paramsArrayList = new ArrayList<>();
        feedRegCustomerList=new ArrayList<>();
        feedCompanyItemList=new ArrayList<>();
        routelist=new ArrayList<>();
        feedDiscountPolicyList = new ArrayList<>();
        feedBonusPolicyList = new ArrayList<>();
        feedCompanywiseBanksList = new ArrayList<>();

        feedDistributorList = new ArrayList<>();
        rFeedVisitsList = new ArrayList<>();

        prefs = new SharedPrefs(this);

        getRegisterCustomerInfoMethod(Utility.BASE_LIVE_URL+"api/Customer/GetRegisterCompanyCustomer?Company_id=" + prefs.getCompanyID() + "&CompanySiteID=" + prefs.getCompanySiteID()
        +"&UserId="+prefs.getUserId());
        rFetchVisitStatuses(Utility.BASE_LIVE_URL+"api/company/VisitStatuses");

        rFetchDiscountPolicyData(Utility.BASE_LIVE_URL+"api/Company/GetCompanyItemsPolicies?Company_id="+prefs.getCompanyID()+"&CompanySiteId="+prefs.getCompanySiteID());
        rFetchBonusPolicyData(Utility.BASE_LIVE_URL+"api/Company/GetCompanyItemsBonusPolicies?Company_id="+prefs.getCompanyID()+"&CompanySiteId="+prefs.getCompanySiteID());
        rFetchDistributorData(
                Utility.BASE_LIVE_URL+"api/Customer/GetRegisterCompanyCustomer?Company_id="
                        +prefs.getCompanyID()
                        +"&CompanySiteId="+prefs.getCompanySiteID()
                        +"&UserId="+prefs.getUserId()
                        +"&type=Distributor"

        );


        return super.onStartCommand(intent, flags, startId);

    }

    private void rFetchVisitStatuses(String url)
    {
        Timber.d("Visits Url "+url);
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject parentObject = new JSONObject(response);
                    JSONArray tableArray = parentObject.getJSONArray("Table");
                    if (tableArray.length() > 0) {
                        ParseJasonFeedVisits(tableArray);
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
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);


    }

    private void ParseJasonFeedVisits(JSONArray feedArray) {
        try {
            rFeedVisitsList.clear();
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                int StatusID = feedObj.getInt("StatusID");
                String VisitStatus = feedObj.getString("VisitStatus");
                rFeedVisitsList.add(new VisitStatusesModel(StatusID,VisitStatus));
            }



            if (db.insertVisitStatuses(rFeedVisitsList) != 0) {
                Timber.d("visit insertion true");
            }else {
                Timber.d("Visit Insertion False");
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Utility.logCatMsg("Feed item error" + e);
        }
    }



    private void rFetchDistributorData(String url) {
        Timber.d("Distributor Url "+url);
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject parentObject = new JSONObject(response);
                    JSONArray tableArray = parentObject.getJSONArray("Table");
                    if (tableArray.length() > 0) {
                        ParseJasonDistributor(tableArray);
                    }

                } catch (JSONException e) {
                    //Check Point
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Timber.d("apiresponseerror "+error.getMessage() + "no response comming from Distrib api");
                if (error instanceof ServerError) {
                    Timber.d("apiresponseerror "+error.getMessage() + "Server Error");
                } else if (error instanceof NetworkError) {
                    Timber.d("apiresponseerror "+error.getMessage() + "Network Error");
                } else if (error instanceof TimeoutError) {
                    Timber.d("apiresponseerror"+error.getMessage() + "Connection Timeout Error");
                }
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }


    private void ParseJasonDistributor(JSONArray tableArray) {

        feedDistributorList.clear();

        try {


            for (int i = 0; i < tableArray.length(); i++) {

                JSONObject feedObj = (JSONObject) tableArray.get(i);

                feedDistributorList.add(new DistributorModel(
                        feedObj.getInt("ContactId"),
                        feedObj.getString("Title"),
                        feedObj.getString("Name"),
                        feedObj.getString("FatherName"),
                        feedObj.getString("Address"),
                        feedObj.getString("City"),
                        feedObj.getString("Country"),
                        feedObj.getString("DeliveryAddress"),
                        feedObj.getString("ContactGroup"),
                        feedObj.getString("Phone"),
                        feedObj.getString("Phone2"),
                        feedObj.getString("Cell"),
                        feedObj.getString("Fax"),
                        feedObj.getString("Email"),
                        feedObj.getInt("AreaId"),
                        feedObj.getInt("DivisionId"),
                        feedObj.getInt("ZoneId"),
                        feedObj.getString("CreditLimit"),
                        feedObj.getString("PercentageDisc1"),
                        feedObj.getString("PercentageDisc2"),
                        feedObj.getString("PercentageDisc3"),
                        feedObj.getString("Longitude"),
                        feedObj.getString("Latitude"),
                        feedObj.getString("Address1"),
                        feedObj.getString("Address2"),
                        feedObj.getInt("RouteId"),
                        feedObj.getInt("CompanySiteID"),
                        feedObj.getInt("CompanyID"),
                        feedObj.getString("ContactCode"),
                        feedObj.getString("ContactType"),
                        feedObj.getInt("ContactTypeId"),
                        feedObj.getString("SalesMode"),
                        feedObj.getString("ImageName"),
                        feedObj.getInt("DistributerId"),
                        String.valueOf(feedObj.getBoolean("isDistributer"))

                ));

            }




        } catch (Exception e) {
            e.printStackTrace();
            Utility.logCatMsg("Feed item error" + e);
            Timber.d("ParseJasonDistributor exception is "+e.getMessage());
        }

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    private void getRegisterCustomerInfoMethod(String url) {

        Timber.d("getRegisterCustomerInfoMethod is running "+url);


        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject parentObject = new JSONObject(response);
                    JSONArray Table = parentObject.getJSONArray("Table");
                    if (Table.length() > 0) {
                        parseJsonFeedCustomers(Table);
                        getCompanyRoutesMethod(Utility.BASE_LIVE_URL+"api/Company/GetRegisterCompanyRoutes?Company_id=" + prefs.getCompanyID() + "&CompanySiteId=" + prefs.getCompanySiteID()
                        +"&UserId="+prefs.getUserId());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Timber.d("Customer error is "+e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
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
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void ParseJasonFeedDiscountPolicy(JSONArray tableArray) {

        try {

            feedDiscountPolicyList.clear();

            for (int i = 0; i < tableArray.length(); i++) {
                JSONObject feedObj = (JSONObject) tableArray.get(i);


                int     DiscountPolicyId = feedObj.getInt("DiscountPolicyId");
                String  Type = feedObj.getString("Type");
                int     TypeId = feedObj.getInt("TypeId"); ;
                String  DiscountType = feedObj.getString("DiscountType"); ;
                float   TargetValue = Float.parseFloat(String.valueOf(feedObj.getDouble("TargetValue")));
                int     TargetQty = feedObj.getInt("TargetQty");
                float   DiscountPercentage = Float.parseFloat(String.valueOf(feedObj.getDouble("DiscountPercentage")));
                float   DiscountValue = Float.parseFloat(String.valueOf(feedObj.getDouble("DiscountValue")));
                float   AdditionalDiscountPercentage =Float.parseFloat(String.valueOf(feedObj.getDouble("AdditionalDiscountPercentage")));
                float   AdditionalDiscountValue = Float.parseFloat(String.valueOf(feedObj.getDouble("AdditionalDiscountValue")));
                String  StopOtherDiscount = feedObj.getString("StopOtherDiscount");;
                String  IsClaimable = feedObj.getString("IsClaimable");;
                String  StartDate = feedObj.getString("StartDate");;
                String  EndDate = feedObj.getString("EndDate");;
                int     GroupDivId = feedObj.getInt("GroupDivId"); ;
                int     DivId = feedObj.getInt("DivId");;
                int     ItemId = feedObj.getInt("ItemId");


                feedDiscountPolicyList.add(new DiscountPolicyModel(
                        DiscountPolicyId,Type,TypeId,DiscountType,TargetValue,TargetQty,DiscountPercentage,DiscountValue,AdditionalDiscountPercentage,
                        AdditionalDiscountValue,StopOtherDiscount,IsClaimable,StartDate,EndDate,GroupDivId,DivId,ItemId
                ));

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
        RequestQueue queue = Volley.newRequestQueue(this);
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




        } catch (JSONException e) {
            e.printStackTrace();
            Utility.logCatMsg("Feed item error" + e);
            Timber.d("ParseJasonFeedDiscountPolicy exception is "+e.getMessage());
        }

    }


    public void getCompanyRoutesMethod(String url) {
        Timber.d(" getCompanyRoutesMethod : "+url);

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject parent = new JSONObject(response);
                    JSONArray tableArray = parent.getJSONArray("Table");
                    if (tableArray.length() > 0) {
                        parseJsonFeedCompanyRoute(tableArray);
                        getCompanyItemsMethod(Utility.BASE_LIVE_URL+"api/Company/GetCompanyItems?Company_id=" + prefs.getCompanyID()
                                +"&companysiteID="+prefs.getCompanySiteID());

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);


    }



    private void getCompanyItemsMethod(String url) {

        Timber.d("getCompanyItemsMethod : "+url);

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject parentObject = new JSONObject(response);
                    JSONArray tableArray = parentObject.getJSONArray("Table");
                    if (tableArray.length() > 0) {

                        Timber.d("table array length is "+tableArray.length());
                        parseJsonFeedCompanyItems(tableArray);
                        getRegisterBanksInfo(Utility.BASE_LIVE_URL+ "api/company/BankDetails?Company_id=" + prefs.getCompanyID() + "&Compnay_siteId=" + prefs.getCompanySiteID());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }



    //Assignment Work By Muhammad Yaseen
    private void getRegisterBanksInfo(String url) {

        Timber.d(" getRegisterBanksInfo : "+url);
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject parentObject = new JSONObject(response);
                    JSONArray tableArray = parentObject.getJSONArray("Table");
                    if (tableArray.length() > 0) {
                        parseJsonFeedCompanyWiseBanks(tableArray);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }

    //Workk By Yaseen
    private void parseJsonFeedCompanyWiseBanks(JSONArray feedArray) {
        try {
            feedCompanywiseBanksList.clear();
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                CompanyWiseBanksModel model = new CompanyWiseBanksModel();
                model.setBankID(feedObj.getInt("BankID"));
                model.setBankName(feedObj.getString("BankName"));
                model.setBankAccountNbr(feedObj.getString("BankAccountNbr"));
                model.setBankAccountType(feedObj.getString("BankAccountType"));
                model.setAddress(feedObj.getString("Address"));
                feedCompanywiseBanksList.add(model);

            }

            if(feedCompanywiseBanksList.size()>0) {
                db.insertCompanyWiseBankDetails(feedCompanywiseBanksList);
            }

        } catch (JSONException e) {

            e.printStackTrace();
            Utility.logCatMsg("Feed Register Customer error" + e);
        }

    }


    private void parseJsonFeedCustomers(JSONArray feedArray) {
        feedRegCustomerList.clear();
        try {
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                RegisterdCustomerModel model = new RegisterdCustomerModel();
                model.setCustomer_id(feedObj.getInt("ContactId"));
                model.setContactTypeId(feedObj.getInt("ContactTypeId"));
                model.setName(feedObj.getString("Name"));
                model.setAddress(feedObj.getString("Address"));
                model.setAddress1(feedObj.getString("Address1"));
                model.setCell(feedObj.getString("Phone"));
                model.setPhone(feedObj.getString("Phone2"));
                model.setCity(feedObj.getString("City"));
                model.setCountry(feedObj.getString("Country"));
                model.setLat(feedObj.getString("Latitude").trim());
                model.setLng(feedObj.getString("Longitude").trim());
                model.setRoute(feedObj.getInt("RouteId"));
                model.setSalesMode(feedObj.getString("SalesMode").trim());
                model.setImageName(feedObj.getString("ImageName"));
                feedRegCustomerList.add(model);

            }

        } catch (JSONException e) {

            e.printStackTrace();
            Timber.d("Error in customer is "+e.getMessage());
            Utility.logCatMsg("Feed Register Customer error" + e);
        }

    }




    private void parseJsonFeedCompanyItems(JSONArray feedArray) {


        try {
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                DeliveryItemModel model = new DeliveryItemModel();

                model.setItem_id(feedObj.getInt("Id"));
                model.setTitle(feedObj.getString("Title"));
                model.setCode(feedObj.getString("Code"));
                model.setName(feedObj.getString("Name"));
                model.setItemDetail(feedObj.getString("ItemDetail"));
                model.setTaxCode(feedObj.getString("TaxCode"));
                model.setImageName(feedObj.getString("ImageName"));
                model.setCostCtnPrice(Float.parseFloat(feedObj.getString("CostCtnPrice")));
                model.setCostPackPrice(Float.parseFloat(feedObj.getString("CostPackPrice")));
                model.setCostPiecePrice(Float.parseFloat(feedObj.getString("CostPiecePrice")));
                model.setRetailPiecePrice(Float.parseFloat(feedObj.getString("RetailPiecePrice")));
                model.setWSPackPrice(Float.parseFloat(feedObj.getString("WSPackPrice")));
                model.setWSPiecePrice(Float.parseFloat(feedObj.getString("WSPiecePrice")));
                model.setRetailCtnPrice(Float.parseFloat(feedObj.getString("RetailCtnPrice")));
                model.setRetailPackPrice(Float.parseFloat(feedObj.getString("RetailPackPrice")));
                model.setWSCtnPrice(Float.parseFloat(feedObj.getString("WSCtnPrice")));
                model.setDisplayPrice(Float.parseFloat(feedObj.getString("DisplayPrice")));
                model.setCtnSize(feedObj.getInt("CtnSize"));
                model.setPackSize(feedObj.getInt("PackSize"));
                model.setDivisionId(feedObj.getInt("DivisionId"));
                model.setBrandId(feedObj.getInt("BrandId"));
                model.setPackSize(feedObj.getInt("PriceId"));
                model.setPriceId(feedObj.getInt("PieceSize"));
                model.setUnitSize(feedObj.getInt("UnitSize"));
                model.setSKU(feedObj.getString("SKU"));

                feedCompanyItemList.add(model);

            }

        } catch (JSONException e) {

            e.printStackTrace();

            Timber.d("Exception in items is "+e.getMessage());
            Utility.logCatMsg("Feed CompanyItems error" + e);
        }

        //offline Work for all other data if api returning more than 0...
        if (feedRegCustomerList.size() > 0) {

            Timber.d("itemlist size is here > "+feedCompanyItemList.size());


            db.dropRunTimeOrderTables();
            db.createRunTimeOrderTables();
            db.insertCompanyCustomer(feedRegCustomerList, "False");

            if(feedDistributorList.size()>0){
                db.insertDistributers(feedDistributorList);
            }else{
                Timber.d("No data in feedDistributors List");
            }


            if(feedCompanyItemList.size()>0){
                Timber.d("Feed item list size is > 0 ");
                db.insertCompanyItem(feedCompanyItemList);
                Intent intent = new Intent("dataUpdated");
                Timber.d("Data Updated Successfully");
                Toast.makeText(getApplicationContext(), "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                sendBroadcast(intent);
            }

            if(feedDiscountPolicyList.size()>0)
            {
                if (db.insertDiscountPolciies(feedDiscountPolicyList) != 0) {
                    Timber.d("Discount insertion true (sync company info)");
                }else {
                    Timber.d("Discount Insertion False (company info) ");
                }
            }else{
                Timber.d("Discount Policy List have no data");
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
                Timber.d("Bonus Policy List have no data");
            }


            if(routelist.size()>0)
            {
                db.insertCompanyRoute(routelist);
            }

        }

    }

    private void parseJsonFeedCompanyRoute(JSONArray feedArray) {
        try {
            routelist.clear();

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                RouteModel model = new RouteModel();
                model.setRoute_id(feedObj.getInt("Route_Id"));
                model.setRoute_code(feedObj.getString("Code"));
                model.setRoute_name(feedObj.getString("Title"));
                model.setRoute_description(feedObj.getString("Descript"));
                routelist.add(model);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Utility.logCatMsg("Feed CompanyRoute error" + e);
        }

    }
}
