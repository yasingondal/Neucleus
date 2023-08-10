package detrack.zaryansgroup.com.detrack.activity.utilites;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.DeliveryItemModel;
import detrack.zaryansgroup.com.detrack.activity.Model.DeliveryInfo;
import detrack.zaryansgroup.com.detrack.activity.Model.Params;
import detrack.zaryansgroup.com.detrack.activity.SQLlite.ZEDTrackDB;
import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;
import detrack.zaryansgroup.com.detrack.activity.activites.ShowTakenOrderActivity;
import detrack.zaryansgroup.com.detrack.activity.activites.WelcomeActivity;
import detrack.zaryansgroup.com.detrack.activity.retrofit.Api_Reto;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import timber.log.Timber;

/**
 * Created by 6520 on 2/26/2016.
 */
public class SendPod {

    Context context;
    ZEDTrackDB db;
    DeliveryInfo deliverInfo;
    boolean check;
    String urgentOrderStatus = "false";
    String IsNew;
    String Server_deliver_id; // when new order create then hi
    SharedPrefs prefs;
    boolean saveorderfalg = false;
    private ProgressDialog progressDialog;
    ArrayList<String> deliveyIdList;
    int deliveryIdListIndex = 0;

    HashMap<String, String> paramsMap1;

    public static int RecieveCashDepositedBankId;
    public static String rImagesNames;
    public static boolean resyncStatus = false;


    HashMap<String, String> paramsMap;
    ArrayList<DeliveryItemModel> itemlist;

    public SendPod(Context context, String IsNew, ArrayList<String> deliveyIdList) {
        this.context = context;
        deliverInfo = new DeliveryInfo();
        this.IsNew = IsNew;
        this.deliveyIdList = deliveyIdList;
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Uploading Data...");
        progressDialog.setMessage("Please Wait...");
        check = false;
        progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgress(0);
        progressDialog.setCancelable(false);
        progressDialog.setMax(deliveyIdList.size());
        paramsMap = new HashMap<>();
        paramsMap1 = new HashMap<>();

    }


    public void SaveChangesToDataBase() {
        db = new ZEDTrackDB(context);
        prefs = new SharedPrefs(context);
        if (IsNew.equals("True")) {

            SaveOrder(deliveyIdList.get(deliveryIdListIndex));

        } else {
            SaveOrder(deliveyIdList.get(deliveryIdListIndex));

        }
    }


    private HashMap<String, String> GetPODDataParamList() {


        HashMap<String, String> paramsMap = new HashMap<>();

        ArrayList<DeliveryItemModel> deliveryItemList = new ArrayList<DeliveryItemModel>();

        deliveryItemList = db.getSQLiteOrderDeliveryItems(deliveyIdList.get(deliveryIdListIndex), IsNew, false);


        String OrderDetailIdList = "", FOCQtyList = "", FOCValueList = "", FOCPercentageList = "", DiscountList = "", TotalQuantityList = "",
                TotalCostList = "", TotalWholesaleList = "", TotalRetailList = "", RejectCtnQtyList = "",
                RejectPackQtyList = "", RejectPcsQtyList = "", DeliverCtnQtyList = "", DeliverPackQtyList = "", DeliverPcsQtyList = "",
                NetAmountList = "", GSTPercentageList = "", GSTValueList = "", DisplayPriceList = "", ItemIdList = "", CostCtnPriceList = "",
                CostPackPriceList = "", CostPiecePriceList = "", WSPiecePriceList = "", WSPackPriceList = "", WSCtnPriceList = "", RetailPiecePriceList = "",
                RetailCtnPriceList = "", RetailPackPriceList = "", EmptyBottleList = "", CategoryIdList = "", RouteId = "";




        String res = "";
        saveorderfalg = true;
        for (int i = 0; i < deliveryItemList.size(); i++) {


            res = deliveryItemList.get(i).getServer_Item_Id() + ",";
            OrderDetailIdList = OrderDetailIdList + res;

            res = deliveryItemList.get(i).getFoc_qty() + ",";
            FOCQtyList = FOCQtyList + res;

            res = deliveryItemList.get(i).getFoc_value() + ",";
            FOCValueList = FOCValueList + res;

            res = deliveryItemList.get(i).getFoc_percentage() + ",";
            FOCPercentageList = FOCPercentageList + res;

            res = deliveryItemList.get(i).getItem_discount() + ",";
            DiscountList = DiscountList + res;

            res = deliveryItemList.get(i).getTotal_Quantity() + ",";
            TotalQuantityList = TotalQuantityList + res;

            res = deliveryItemList.get(i).getTotalCostPrice() + ",";
            TotalCostList = TotalCostList + res;

            res = deliveryItemList.get(i).getTotalwholeSalePrice() + ",";
            TotalWholesaleList = TotalWholesaleList + res;

            res = deliveryItemList.get(i).getTotalRetailPrice() + ",";

            TotalRetailList = TotalRetailList + res;

            res = deliveryItemList.get(i).getDisplayPrice() + ",";
            DisplayPriceList = DisplayPriceList + res;

            res = deliveryItemList.get(i).getItemGstValue() + ",";

            GSTValueList = GSTValueList + res;

            res = deliveryItemList.get(i).getItemGstPer() + ",";
            GSTPercentageList = GSTPercentageList + res;

            res = deliveryItemList.get(i).getReject_ctn_qty() + ",";
            RejectCtnQtyList = RejectCtnQtyList + res;

            res = deliveryItemList.get(i).getReject_pac_qty() + ",";
            RejectPackQtyList = RejectPackQtyList + res;

            res = deliveryItemList.get(i).getReject_pcs_qty() + ",";
            RejectPcsQtyList = RejectPcsQtyList + res;


            res = deliveryItemList.get(i).getDeliver_ctn_qty() + ",";
            DeliverCtnQtyList = DeliverCtnQtyList + res;

            res = deliveryItemList.get(i).getDeliver_pac_qty() + ",";
            DeliverPackQtyList = DeliverPackQtyList + res;

            res = deliveryItemList.get(i).getDeliver_pcs_qty() + ",";
            DeliverPcsQtyList = DeliverPcsQtyList + res;

            res = deliveryItemList.get(i).getNetTotalRetailPrice() + ",";
            NetAmountList = NetAmountList + res;

            //for item ids
            res = Math.abs(deliveryItemList.get(i).getItem_id()) + ",";
            ItemIdList = ItemIdList + res;


            res = Math.abs(deliveryItemList.get(i).getCostCtnPrice()) + ",";
            CostCtnPriceList = CostCtnPriceList + res;

            res = Math.abs(deliveryItemList.get(i).getCostPackPrice()) + ",";
            CostPackPriceList = CostPackPriceList + res;

            res = Math.abs(deliveryItemList.get(i).getCostPiecePrice()) + ",";
            CostPiecePriceList = CostPiecePriceList + res;

            res = Math.abs(deliveryItemList.get(i).getRetailPiecePrice()) + ",";
            WSCtnPriceList = WSCtnPriceList + res;

            res = Math.abs(deliveryItemList.get(i).getWSPackPrice()) + ",";
            WSPackPriceList = WSPackPriceList + res;

            res = Math.abs(deliveryItemList.get(i).getWSPiecePrice()) + ",";
            WSPiecePriceList = WSPiecePriceList + res;

            res = Math.abs(deliveryItemList.get(i).getRetailCtnPrice()) + ",";
            RetailCtnPriceList = RetailCtnPriceList + res;

            res = Math.abs(deliveryItemList.get(i).getRetailPackPrice()) + ",";
            RetailPackPriceList = RetailPackPriceList + res;

            res = Math.abs(deliveryItemList.get(i).getWSCtnPrice()) + ",";
            RetailPiecePriceList = RetailPiecePriceList + res;

            res = Math.abs(deliveryItemList.get(i).getEmptyBottles()) + ",";
            EmptyBottleList = EmptyBottleList + res;

            res = Math.abs(deliveryItemList.get(i).getCategoryId()) + ",";
            CategoryIdList = CategoryIdList + res;

            if (deliveryItemList.get(i).getServer_Item_Id() != 0)
                RouteId = String.valueOf(deliveryItemList.get(i).getRoute_id());
        }


        try {

            Params p1  = new Params();
            Params p2  = new Params();
            Params p3  = new Params();
            Params p4  = new Params();
            Params p5  = new Params();
            Params p6  = new Params();
            Params p7  = new Params();
            Params p8  = new Params();
            Params p9  = new Params();
            Params p10 = new Params();
            Params p11 = new Params();
            Params p12 = new Params();
            Params p13 = new Params();
            Params p14 = new Params();
            Params p15 = new Params();
            Params p16 = new Params();
            Params p17 = new Params();
            Params p18 = new Params();
            Params p19 = new Params();
            Params p20 = new Params();
            Params p21 = new Params();
            Params p22 = new Params();
            Params p23 = new Params();
            Params p24 = new Params();
            Params p25 = new Params();
            Params p26 = new Params();
            Params p27 = new Params();
            Params p28 = new Params();
            Params p29 = new Params();
            Params p30 = new Params();
            Params p31 = new Params();
            Params p32 = new Params();
            Params p33 = new Params();
            Params p34 = new Params();
            Params p35 = new Params();
            Params p36 = new Params();
            Params p37 = new Params();
            Params p38 = new Params();
            Params p39 = new Params();
            Params p40 = new Params();
            Params p41 = new Params();
            Params p42 = new Params();
            Params p43 = new Params();
            Params p44 = new Params();
            Params p45 = new Params();
            Params p46 = new Params();
            Params p47 = new Params();
            Params p48 = new Params();
            Params p49 = new Params();
            Params p50 = new Params();
            Params p51 = new Params();
            Params p52 = new Params();
            Params p53 = new Params();
            Params p54 = new Params();
            Params p55 = new Params();
            Params p56 = new Params();


            //For Bonus/Disc Policies
            Params p57 = new Params();
            Params p58 = new Params();
            Params p59 = new Params();
            Params p60 = new Params();
            Params p61 = new Params();
            Params p62 = new Params();
            Params p63 = new Params();




            p1.setKey("OrderId");
            p1.setValue(String.valueOf(deliverInfo.getServer_Delivery_id())); //server user_id

            p3.setKey("SalesMode");
            p3.setValue(deliverInfo.getSalemode() + "");

            p4.setKey("Status");
            p4.setValue(deliverInfo.getDelivery_status() + "");

            p5.setKey("TotalQuantity");
            p5.setValue(deliverInfo.getTotal_qty() + "");

            p6.setKey("TotalAmount");
            p6.setValue(deliverInfo.getTotal_Bill() + "");

            p7.setKey("Discount");
            p7.setValue(deliverInfo.getDiscount() + "");

            p8.setKey("NetTotal");
            p8.setValue(deliverInfo.getNetTotal() + "");

            p9.setKey("RefusedReason");
            if (deliverInfo.getRefusedReason() == null)
                p9.setValue("");
            else
                p9.setValue(deliverInfo.getRefusedReason().equals("null") ? "" : deliverInfo.getRefusedReason() + "");

            p10.setKey("CancelledReason");
            p10.setValue(deliverInfo.getCancelledReason().equals("null") ? "" : deliverInfo.getCancelledReason() + "");

            p11.setKey("RejectedReason");
            if (deliverInfo.getDelivery_status().equals("Rejected")) {
                p11.setValue(deliverInfo.getRejected_Reason());
            } else {
                p11.setValue("No Reason");

            }

            p12.setKey("Receivedby");
            p12.setValue(deliverInfo.getReceivedBy() + "");

            p13.setKey("PODLongitude");
            p13.setValue(deliverInfo.getPod_lng() + "");

            p14.setKey("PODLatitude");
            p14.setValue(deliverInfo.getPod_lat() + "");

            p15.setKey("UpdatedBy");
            p15.setValue(prefs.getUserName() + "");

            p16.setKey("OrderDetailIdList");
            p16.setValue(OrderDetailIdList);


            p17.setKey("FOCQtyList");
            p17.setValue(FOCQtyList);

            p18.setKey("FOCValueList");
            p18.setValue(FOCValueList);

            p19.setKey("FOCPercentageList");
            p19.setValue(FOCPercentageList);

            p20.setKey("DiscountList");
            p20.setValue(DiscountList);

            p21.setKey("TotalQuantityList");
            p21.setValue(TotalQuantityList);


            p22.setKey("TotalCostList");
            p22.setValue(TotalCostList);
            paramsMap.put("TotalCostList", TotalCostList);

            p23.setKey("TotalWholesaleList");
            p23.setValue(TotalWholesaleList);

            p24.setKey("TotalRetailList");
            p24.setValue(TotalRetailList);

            p25.setKey("RejectCtnQtyList");
            p25.setValue(RejectCtnQtyList);

            p26.setKey("RejectPackQtyList");
            p26.setValue(RejectPackQtyList);

            p27.setKey("RejectPcsQtyList");
            p27.setValue(RejectPcsQtyList);

            p28.setKey("DeliverCtnQtyList");
            p28.setValue(DeliverCtnQtyList);

            p29.setKey("DeliverPackQtyList");
            p29.setValue(DeliverPackQtyList);

            p30.setKey("DeliverPcsQtyList");
            p30.setValue(DeliverPcsQtyList);

            p31.setKey("NetAmountList");
            p31.setValue(NetAmountList);

            p32.setKey("GSTPercentageList");
            p32.setValue(GSTPercentageList);

            p33.setKey("GSTValueList");
            p33.setValue(GSTValueList);

            p34.setKey("DisplayPriceList");
            p34.setValue(DisplayPriceList);

            p35.setKey("DiscountPercentage");
            p35.setValue(deliverInfo.getPercentageDiscount() + "");

            p36.setKey("CtnQuantityList");
            p36.setValue(DeliverCtnQtyList);

            p37.setKey("PackQuantityList");
            p37.setValue(DeliverPackQtyList);

            p38.setKey("PcsQuantityList");
            p38.setValue(DeliverPcsQtyList);

            p39.setKey("CostCtnPriceList");
            p39.setValue(CostCtnPriceList);

            p40.setKey("CostPackPriceList");
            p40.setValue(CostPackPriceList);

            p41.setKey("CostPiecePriceList");
            p41.setValue(CostPiecePriceList);

            p42.setKey("WSPiecePriceList");
            p42.setValue(WSPiecePriceList);

            p43.setKey("WSPackPriceList");
            p43.setValue(WSPackPriceList);

            p44.setKey("WSCtnPriceList");
            p44.setValue(WSCtnPriceList);

            p45.setKey("RetailCtnPriceList");
            p45.setValue(RetailCtnPriceList);

            p46.setKey("RetailPackPriceList");
            p46.setValue(RetailPackPriceList);

            p47.setKey("RetailPiecePriceList");
            p47.setValue(RetailPiecePriceList);

            p48.setKey("CompanySiteID");
            p48.setValue(String.valueOf(prefs.getCompanySiteID()));

            p49.setKey("CompanyID");
            p49.setValue(String.valueOf(prefs.getCompanyID()));

            p50.setKey("CreatedBy");
            p50.setValue(prefs.getUserName());

            p51.setKey("EmptyBottleList");
            p51.setValue(EmptyBottleList);

            p52.setKey("CategoryIdList");
            p52.setValue(CategoryIdList);

            p53.setKey("ItemIdList");
            p53.setValue(ItemIdList);

            p54.setKey("RouteId");
            p54.setValue(RouteId);

            p55.setKey("TotalGSTValue");
            p55.setValue(deliverInfo.getGst());





            if (RecieveCashDepositedBankId > 0) {
                p56.setKey("BankID");
                p56.setValue(String.valueOf(RecieveCashDepositedBankId));
            } else {
                p56.setKey("BankID");
                p56.setValue("");
            }


            paramsMap.put(p1.getKey(), p1.getValue());
            paramsMap.put(p2.getKey(), p1.getValue());
            paramsMap.put(p3.getKey(), p3.getValue());
            paramsMap.put(p4.getKey(), p4.getValue());
            paramsMap.put(p5.getKey(), p5.getValue());
            paramsMap.put(p6.getKey(), p6.getValue());
            paramsMap.put(p7.getKey(), p7.getValue());
            paramsMap.put(p8.getKey(), p8.getValue());
            paramsMap.put(p9.getKey(), p9.getValue());
            paramsMap.put(p10.getKey(), p10.getValue());
            paramsMap.put(p11.getKey(), p11.getValue());
            paramsMap.put(p12.getKey(), p12.getValue());
            paramsMap.put(p13.getKey(), "0.0");
            paramsMap.put(p14.getKey(), "0.0");
            paramsMap.put(p15.getKey(), p15.getValue());
            paramsMap.put(p16.getKey(), p16.getValue());
            paramsMap.put(p17.getKey(), p17.getValue());
            paramsMap.put(p18.getKey(), p18.getValue());
            paramsMap.put(p19.getKey(), p19.getValue());
            paramsMap.put(p20.getKey(), p20.getValue());
            paramsMap.put(p21.getKey(), p21.getValue());
            paramsMap.put(p22.getKey(), p22.getValue());
            paramsMap.put(p23.getKey(), p23.getValue());
            paramsMap.put(p24.getKey(), p24.getValue());
            paramsMap.put(p25.getKey(), p25.getValue());
            paramsMap.put(p26.getKey(), p26.getValue());
            paramsMap.put(p27.getKey(), p27.getValue());
            paramsMap.put(p28.getKey(), p28.getValue());
            paramsMap.put(p29.getKey(), p29.getValue());
            paramsMap.put(p30.getKey(), p30.getValue());
            paramsMap.put(p31.getKey(), p31.getValue());
            paramsMap.put(p32.getKey(), p32.getValue());
            paramsMap.put(p33.getKey(), p33.getValue());
            paramsMap.put(p34.getKey(), p34.getValue());
            paramsMap.put(p35.getKey(), p35.getValue());
            paramsMap.put(p36.getKey(), p36.getValue());
            paramsMap.put(p37.getKey(), p37.getValue());
            paramsMap.put(p38.getKey(), p38.getValue());
            paramsMap.put(p39.getKey(), p39.getValue());
            paramsMap.put(p40.getKey(), p40.getValue());
            paramsMap.put(p41.getKey(), p41.getValue());
            paramsMap.put(p42.getKey(), p42.getValue());
            paramsMap.put(p43.getKey(), p43.getValue());
            paramsMap.put(p44.getKey(), p44.getValue());
            paramsMap.put(p45.getKey(), p45.getValue());
            paramsMap.put(p46.getKey(), p46.getValue());
            paramsMap.put(p47.getKey(), p47.getValue());
            paramsMap.put(p48.getKey(), p48.getValue());
            paramsMap.put(p49.getKey(), p49.getValue());
            paramsMap.put(p50.getKey(), p50.getValue());
            paramsMap.put(p51.getKey(), p51.getValue());
            paramsMap.put(p52.getKey(), p52.getValue());
            paramsMap.put(p53.getKey(), p53.getValue());
            paramsMap.put(p54.getKey(), p54.getValue());
            paramsMap.put(p55.getKey(), p55.getValue());
            paramsMap.put(p56.getKey(), p56.getValue());


        } catch (Exception e) {
            Utility.logCatMsg("Error " + e.getMessage());
        }
        Utility.logCatMsg("..................");
        return paramsMap;
    }


    private void SaveOrder(String Delivery_id) {



        itemlist = db.getSQLiteOrderDeliveryItems(Delivery_id, "True", false);
        deliverInfo = db.getSelectedSQLiteOrderDelivery(Integer.parseInt(deliveyIdList.get(deliveryIdListIndex)), IsNew);
        urgentOrderStatus = deliverInfo.getIsUrgentOrderStatus();



        String ItemIdList = "", CostCtnPriceList = "", CostPackPriceList = "", CostPiecePriceList = "", PackCostPriceList = "", PackWholesalePriceList = "", PackRetailPriceList = "", PiecesCostPriceList = "", PiecesWholesalePriceList = "", PiecesRetailPriceList = "", CtnQuantityList = "", PackQuantityList = "", PcsQuantityList = "", FOCQtyList = "", FOCValueList = "", FOCPercentageList = "",
                item_DiscountList = "", TotalQuanityList = "", TotalCostList = "", TotalwholesaleList = "", TotalRetailList = "", CategoryIdList = "",
                WSCtnPriceList = "", WSPackPriceList = "", WSPiecePriceList = "", RetailCtnPriceList = "",
                RetailPackPriceList = "", RetailPiecePriceList = "", DisplayPriceList = "", GSTValueList = "",
                GSTPercentageList = "", RejectCtnQtyList = "", RejectPackQtyList = "", RejectPcsQtyList = "", DeliverCtnQtyList = "", DeliverPackQtyList = "",
                DeliverPcsQtyList = "", NetAmountList = "", emptyBottles = "",
                //new For Bonus/Discount Policy
                DiscountPolicyIdList ="", DiscPercentageList="", DiscPolicyList="",BonusPolicyIdList="",BonusQtyList="",BonusGSTList="",BonusItemList="";


        String res;
        saveorderfalg = true;
        for (int i = 0; i < itemlist.size(); i++) {


            //todo here also need to add ItemIncentiveId as well...
            //here for setting data inside variables for discount/bonus policies
            res = itemlist.get(i).getDiscountPolicyId()+",";
            DiscountPolicyIdList = DiscountPolicyIdList + res;

            res = itemlist.get(i).getDiscPercentage()+",";
            DiscPercentageList = DiscPercentageList + res;

            res = itemlist.get(i).getDiscountPolicyValue()+",";
            DiscPolicyList = DiscPolicyList + res;

            res = itemlist.get(i).getBonusPolicyId()+",";
            BonusPolicyIdList = BonusPolicyIdList + res;

            res = itemlist.get(i).getBonusQty()+",";
            BonusQtyList = BonusQtyList + res;

            res = itemlist.get(i).getBonusItemsGst()+",";
            BonusGSTList = BonusGSTList + res;

            res = itemlist.get(i).getBonusIncentiveItemId()+",";
            BonusItemList = BonusItemList + res;

            // Ending here


            res = itemlist.get(i).getItem_id() + ",";
            ItemIdList = ItemIdList + res;

//            res = itemlist.get(i).getCtn_qty() + ",";
            res = 0 + ",";
            CtnQuantityList = CtnQuantityList + res;

            res = itemlist.get(i).getPac_qty() + ",";
            PackQuantityList = PackQuantityList + res;

            res = itemlist.get(i).getCtn_qty() + ",";
            PcsQuantityList = PcsQuantityList + res;

            res = itemlist.get(i).getFoc_qty() + ",";
            FOCQtyList = FOCQtyList + res;

            res = itemlist.get(i).getFoc_value() + ",";
            FOCValueList = FOCValueList + res;

            res = itemlist.get(i).getFoc_percentage() + ",";
            FOCPercentageList = FOCPercentageList + res;

            res = itemlist.get(i).getCategoryId() + ",";
            CategoryIdList = CategoryIdList + res;


            res = itemlist.get(i).getCostCtnPrice() + ",";
            CostCtnPriceList = CostCtnPriceList + res;

            res = itemlist.get(i).getCostPackPrice() + ",";
            CostPackPriceList = CostPackPriceList + res;

            res = itemlist.get(i).getCostPiecePrice() + ",";
            CostPiecePriceList = CostPiecePriceList + res;


            res = itemlist.get(i).getCostPackPrice() + ",";
            PackCostPriceList = PackCostPriceList + res;

            res = itemlist.get(i).getWSPackPrice() + ",";
            PackWholesalePriceList = PackWholesalePriceList + res;

            res = itemlist.get(i).getRetailPackPrice() + ",";
            PackRetailPriceList = PackRetailPriceList + res;


            res = itemlist.get(i).getCostPiecePrice() + ",";
            PiecesCostPriceList = PiecesCostPriceList + res;

            res = itemlist.get(i).getWSPiecePrice() + ",";
            PiecesWholesalePriceList = PiecesWholesalePriceList + res;

            res = itemlist.get(i).getWSCtnPrice() + ",";
            PiecesRetailPriceList = PiecesRetailPriceList + res;

            res = itemlist.get(i).getItem_discount() + ",";
            item_DiscountList = item_DiscountList + res;

            res = Math.abs(itemlist.get(i).getTotal_Quantity()) + ",";
            TotalQuanityList = TotalQuanityList + res;

            res = itemlist.get(i).getTotalCostPrice() + ",";
            TotalCostList = TotalCostList + res;

            res = itemlist.get(i).getTotalwholeSalePrice() + ",";
            TotalwholesaleList = TotalwholesaleList + res;

            res = itemlist.get(i).getTotalRetailPrice() + ",";
            TotalRetailList = TotalRetailList + res;


            res = itemlist.get(i).getRetailPiecePrice() + ",";
            WSCtnPriceList = WSCtnPriceList + res;

            res = itemlist.get(i).getWSPackPrice() + ",";
            WSPackPriceList = WSPackPriceList + res;

            res = itemlist.get(i).getWSPiecePrice() + ",";
            WSPiecePriceList = WSPiecePriceList + res;

            res = itemlist.get(i).getRetailCtnPrice() + ",";
            RetailCtnPriceList = RetailCtnPriceList + res;

            res = itemlist.get(i).getRetailPackPrice() + ",";
            RetailPackPriceList = RetailPackPriceList + res;

            res = itemlist.get(i).getRetailPiecePrice() + ",";
            RetailPiecePriceList = RetailPiecePriceList + res;

            res = itemlist.get(i).getDisplayPrice() + ",";
            DisplayPriceList = DisplayPriceList + res;

            res = itemlist.get(i).getItemGstValue() + ",";
            GSTValueList = GSTValueList + res;

            res = itemlist.get(i).getItemGstPer() + ",";
            GSTPercentageList = GSTPercentageList + res;

            res = Math.abs(itemlist.get(i).getReject_ctn_qty()) + ",";
            RejectCtnQtyList = RejectCtnQtyList + res;

            res = Math.abs(itemlist.get(i).getReject_pac_qty()) + ",";
            RejectPackQtyList = RejectPackQtyList + res;

            res = Math.abs(itemlist.get(i).getReject_pcs_qty()) + ",";
            RejectPcsQtyList = RejectPcsQtyList + res;


            res = itemlist.get(i).getDeliver_ctn_qty() + ",";
            DeliverCtnQtyList = DeliverCtnQtyList + res;

            res = itemlist.get(i).getDeliver_pac_qty() + ",";
            DeliverPackQtyList = DeliverPackQtyList + res;

            res = itemlist.get(i).getDeliver_pcs_qty() + ",";
            DeliverPcsQtyList = DeliverPcsQtyList + res;

            res = itemlist.get(i).getNetTotalRetailPrice() + ",";
            NetAmountList = NetAmountList + res;

            res = itemlist.get(i).getEmptyBottles() + ",";
            emptyBottles = emptyBottles + res;


        }



        paramsMap.put("EmployeeId", String.valueOf(prefs.getEmployeeID()));
        paramsMap.put("CustomerId", String.valueOf(deliverInfo.getCustomer_id()));
        paramsMap.put("VehicleId", String.valueOf(prefs.getVehicleID()));
        paramsMap.put("OrderDateTime", deliverInfo.getDelivery_date());
        paramsMap.put("SalesModel", String.valueOf(deliverInfo.getSalemode()));


        if (String.valueOf(deliverInfo.getSalemode()).equalsIgnoreCase("Cash")) {
            paramsMap.put("BankID", String.valueOf(RecieveCashDepositedBankId));
        } else {
            paramsMap.put("BankID", "0");
        }


        paramsMap.put("Status", deliverInfo.getDelivery_status());
        paramsMap.put("Description", "test");
        paramsMap.put("TotalQuantity", String.valueOf(Math.abs(deliverInfo.getTotal_qty())));
        paramsMap.put("TotalAmount", String.valueOf(Math.abs(Float.parseFloat(deliverInfo.getTotal_Bill()))));
        paramsMap.put("Discount", String.valueOf(deliverInfo.getDiscount()));
        paramsMap.put("NetTotal", String.valueOf(Math.abs(Float.parseFloat(deliverInfo.getNetTotal()))));
        paramsMap.put("DeliveryDateTime", deliverInfo.getDelivery_date());


        paramsMap.put("DeliveryAddress", "test");

        try {
            paramsMap.put("Orderby", URLEncoder.encode(deliverInfo.getDeliver_to_name(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        paramsMap.put("POBLongitude", String.valueOf(deliverInfo.getPob_lng()));
        paramsMap.put("POBLatitude", String.valueOf(deliverInfo.getPob_lat()));

        paramsMap.put("BookbyTrackingID", String.valueOf(prefs.getEmployeeID()));

        if (deliverInfo.getDelivery_status().equals("Booking"))
            paramsMap.put("AssignToTrackingID", "0");
        else paramsMap.put("AssignToTrackingID", String.valueOf(prefs.getEmployeeID()));
        paramsMap.put("RouteId", String.valueOf(deliverInfo.getRoute()));
        paramsMap.put("CompanySiteID", String.valueOf(prefs.getCompanySiteID()));
        paramsMap.put("CompanyID", String.valueOf(prefs.getCompanyID()));
        paramsMap.put("CreatedBy", prefs.getUserName());
        paramsMap.put("ItemIdList", ItemIdList);
        paramsMap.put("CostCtnPriceList", CostCtnPriceList);
        paramsMap.put("CostPackPriceList", CostPackPriceList);
        paramsMap.put("CostPiecePriceList", CostPiecePriceList);
        paramsMap.put("CtnQuantityList", CtnQuantityList);
        paramsMap.put("PackQuantityList", PackQuantityList);
        paramsMap.put("PcsQuantityList", PcsQuantityList);
        paramsMap.put("FOCQtyList", FOCQtyList);
        paramsMap.put("FOCValueList", FOCValueList);
        paramsMap.put("FOCPercentageList", FOCPercentageList);
        paramsMap.put("item_DiscountList", item_DiscountList);
        paramsMap.put("TotalQuanityList", PcsQuantityList);
        paramsMap.put("TotalCostList", TotalCostList);
        paramsMap.put("TotalwholesaleList", TotalwholesaleList);
        paramsMap.put("TotalRetailList", TotalRetailList);
        paramsMap.put("CategoryIdList", CategoryIdList);
        paramsMap.put("WSCtnPriceList", WSCtnPriceList);
        paramsMap.put("WSPackPriceList", WSPackPriceList);
        paramsMap.put("WSPiecePriceList", WSPiecePriceList);
        paramsMap.put("RetailCtnPriceList", RetailCtnPriceList);
        paramsMap.put("RetailPackPriceList", RetailPackPriceList);
        paramsMap.put("RetailPiecePriceList", RetailPiecePriceList);
        paramsMap.put("DisplayPriceList", DisplayPriceList);
        paramsMap.put("GSTValueList", GSTValueList);
        paramsMap.put("GSTPercentageList", GSTPercentageList);
        paramsMap.put("RejectCtnQtyList", RejectCtnQtyList);
        paramsMap.put("RejectPackQtyList", RejectPackQtyList);
        paramsMap.put("RejectPcsQtyList", RejectPcsQtyList);
        paramsMap.put("DeliverCtnQtyList", DeliverCtnQtyList);
        paramsMap.put("DeliverPackQtyList", DeliverPackQtyList);
        paramsMap.put("DeliverPcsQtyList", DeliverPcsQtyList);
        paramsMap.put("NetAmountList", NetAmountList);
        paramsMap.put("DiscountPercentage", String.valueOf(deliverInfo.getPercentageDiscount()));
        paramsMap.put("SerialNo", String.valueOf(deliverInfo.getSerialNo()));
        paramsMap.put("categoryId", String.valueOf(deliverInfo.getCategoryId()));
        paramsMap.put("EmptyCountList", emptyBottles);
        paramsMap.put("TotalGSTValue", deliverInfo.getGst());

        //Bonus/Disc Lists

        paramsMap.put("DiscountPolicyIdList",DiscountPolicyIdList);
        paramsMap.put("DiscPercentageList",DiscPercentageList);
        paramsMap.put("DiscPolicyList",DiscPolicyList);
        paramsMap.put("BonusPolicyIdList",BonusPolicyIdList);
        paramsMap.put("BonusItemList",BonusItemList);
        paramsMap.put("BonusQtyList",BonusQtyList);
        paramsMap.put("BonusGSTList",BonusGSTList);



        Timber.d("The Display Price is "+DisplayPriceList);



        //          todo New Params JSON work
        //make 2 dates dynamic

        Timber.d("The Date is "+paramsMap.get("OrderDateTime"));


        paramsMap1.put("EmployeeId",paramsMap.get("EmployeeId"));
        paramsMap1.put("CustomerId",paramsMap.get("CustomerId"));
        paramsMap1.put("VehicleId","1");
        paramsMap1.put("OrderNo","0");
        paramsMap1.put("SerialNo",paramsMap.get("SerialNo"));
        paramsMap1.put("OrderDateTime",deliverInfo.getDelivery_date());
        paramsMap1.put("SalesMode",paramsMap.get("SalesModel"));
        paramsMap1.put("Status",paramsMap.get("Status"));
        paramsMap1.put("Description","test");
        paramsMap1.put("TotalQuantity",paramsMap.get("TotalQuantity"));
        paramsMap1.put("TotalAmount",paramsMap.get("TotalAmount"));
        paramsMap1.put("Discount",paramsMap.get("Discount"));
        paramsMap1.put("DiscountPercentage",paramsMap.get("DiscountPercentage"));
        paramsMap1.put("NetTotal",paramsMap.get("NetTotal"));
        paramsMap1.put("DeliveryDateTime",deliverInfo.getDelivery_date());
        paramsMap1.put("DeliveryAddress",paramsMap.get("DeliveryAddress"));
        paramsMap1.put("Orderby",paramsMap.get("Orderby"));
        paramsMap1.put("POBLongitude",paramsMap.get("POBLongitude"));
        paramsMap1.put("POBLatitude",paramsMap.get("POBLatitude"));
        paramsMap1.put("BookbyTrackingID",paramsMap.get("BookbyTrackingID"));
        paramsMap1.put("AssignToTrackingID",paramsMap.get("AssignToTrackingID"));
        paramsMap1.put("RouteId",paramsMap.get("RouteId"));
        paramsMap1.put("categoryId",paramsMap.get("categoryId"));
        paramsMap1.put("CompanySiteID",paramsMap.get("CompanySiteID"));
        paramsMap1.put("CompanyID",paramsMap.get("CompanyID"));
        paramsMap1.put("CreatedBy",paramsMap.get("CreatedBy"));
        paramsMap1.put("ItemIdList",paramsMap.get("ItemIdList"));
        paramsMap1.put("CostCtnPriceList",paramsMap.get("CostCtnPriceList"));
        paramsMap1.put("CostPackPriceList",paramsMap.get("CostPackPriceList"));
        paramsMap1.put("CostPiecePriceList",paramsMap.get("CostPiecePriceList"));
        paramsMap1.put("CtnQuantityList",paramsMap.get("CtnQuantityList"));
        paramsMap1.put("PackQuantityList",paramsMap.get("PackQuantityList"));
        paramsMap1.put("PcsQuantityList",paramsMap.get("PcsQuantityList"));
        paramsMap1.put("FOCQtyList",paramsMap.get("FOCQtyList"));
        paramsMap1.put("FOCValueList",paramsMap.get("FOCValueList"));
        paramsMap1.put("FOCPercentageList",paramsMap.get("FOCPercentageList"));
        paramsMap1.put("item_DiscountList",paramsMap.get("item_DiscountList"));
        paramsMap1.put("TotalQuanityList",paramsMap.get("TotalQuanityList"));
        paramsMap1.put("EmptyCountList",emptyBottles);
        paramsMap1.put("TotalCostList",paramsMap.get("TotalCostList"));
        paramsMap1.put("TotalwholesaleList",paramsMap.get("TotalwholesaleList"));
        paramsMap1.put("TotalRetailList",paramsMap.get("TotalRetailList"));
        paramsMap1.put("CategoryIdList",paramsMap.get("CategoryIdList"));
        paramsMap1.put("WSCtnPriceList",paramsMap.get("WSCtnPriceList"));
        paramsMap1.put("WSPackPriceList",paramsMap.get("WSPackPriceList"));
        paramsMap1.put("WSPiecePriceList",paramsMap.get("WSPiecePriceList"));
        paramsMap1.put("RetailCtnPriceList",paramsMap.get("RetailCtnPriceList"));
        paramsMap1.put("RetailPackPriceList",paramsMap.get("RetailPackPriceList"));
        paramsMap1.put("RetailPiecePriceList",paramsMap.get("RetailPiecePriceList"));
        paramsMap1.put("DisplayPriceList",paramsMap.get("DisplayPriceList"));
        paramsMap1.put("GSTValueList",paramsMap.get("GSTValueList"));
        paramsMap1.put("GSTPercentageList",paramsMap.get("GSTPercentageList"));
        paramsMap1.put("RejectCtnQtyList",paramsMap.get("RejectCtnQtyList"));
        paramsMap1.put("RejectPackQtyList",paramsMap.get("RejectPackQtyList"));
        paramsMap1.put("RejectPcsQtyList",paramsMap.get("RejectPcsQtyList"));
        paramsMap1.put("DeliverCtnQtyList",paramsMap.get("DeliverCtnQtyList"));
        paramsMap1.put("DeliverPackQtyList",paramsMap.get("DeliverPackQtyList"));
        paramsMap1.put("DeliverPcsQtyList",paramsMap.get("DeliverPcsQtyList"));
        paramsMap1.put("NetAmountList",paramsMap.get("NetAmountList"));
        paramsMap1.put("TotalGSTValue",deliverInfo.getGst());
        paramsMap1.put("BankID",paramsMap.get("BankID"));
        paramsMap1.put("IsUrgent",urgentOrderStatus);
        paramsMap1.put("PODAttachmentList",rImagesNames);
        paramsMap1.put("DiscountPolicyIdList",paramsMap.get("DiscountPolicyIdList"));
        paramsMap1.put("DiscPercentageList",paramsMap.get("DiscPercentageList"));
        paramsMap1.put("DiscountList",paramsMap.get("DiscPolicyList"));
        paramsMap1.put("BonusPolicyIdList",paramsMap.get("BonusPolicyIdList"));
        paramsMap1.put("BonusQtyList",paramsMap.get("BonusQtyList"));
        paramsMap1.put("BonusGSTList",paramsMap.get("BonusGSTList"));
        paramsMap1.put("IncentiveItemIdList",paramsMap.get("BonusItemList"));
        paramsMap1.put("DistributorId",String.valueOf(deliverInfo.getDistributorId()));
        paramsMap1.put("SubDistributorId",String.valueOf(deliverInfo.getSubDistributorId()));
        paramsMap1.put("resync",String.valueOf(resyncStatus));



        Timber.d("---------------------------Disc/Bonus-------------------------------");

        Timber.d("Value of DiscountPolicyIdList is: "+ DiscountPolicyIdList);
        Timber.d("Value of DiscPercentageList is: "+DiscPercentageList);
        Timber.d("Value of DiscPolicyList is: "+DiscPolicyList);
        Timber.d("Value of BonusPolicyIdList is: "+ BonusPolicyIdList);
        Timber.d("Value of BonusItemList is: "+ BonusItemList);
        Timber.d("Value of BonusQtyList is: "+BonusQtyList);
        Timber.d("Value of BonusGSTList is: "+ BonusGSTList);

        Timber.d("---------------------------Disc/Bonus-------------------------------");


        String api;
        String orderno = "0";


//        todo for later update case handling
//        if (deliverInfo.getFromserver().equalsIgnoreCase("1")) {
//            Timber.d("updated case");
//            updateOrder();
//
//        } else {


//        Return order handling code...
            if (deliverInfo.getDelivery_status().equals("Returned")) {
                progressDialog.show();

                Timber.d("Returned case is running");

                api = "api/SalesReturn/addNewSalesReturn";
                String hSaleReturnUrl = Utility.BASE_LIVE_URL + api + "?EmployeeId=" + paramsMap.get("EmployeeId") + "&CustomerId=" + paramsMap.get("CustomerId")
                        + "&VehicleId=" + paramsMap.get("VehicleId") + "&OrderNo=" + orderno + "&SerialNo=" + paramsMap.get("SerialNo")
                        + "&OrderDateTime=" + paramsMap.get("OrderDateTime").replace(" ", "+")
                        + "&SalesMode=" + paramsMap.get("SalesModel") + "&Status=" + paramsMap.get("Status")
                        + "&Description=" + paramsMap.get("Description") + "&TotalQuantity=" + paramsMap.get("TotalQuantity")
                        + "&TotalAmount=" + paramsMap.get("TotalAmount") + "&Discount=" + paramsMap.get("Discount")
                        + "&DiscountPercentage=" + paramsMap.get("DiscountPercentage") + "&NetTotal=" + paramsMap.get("NetTotal")
                        + "&DeliveryDateTime=" + paramsMap.get("DeliveryDateTime").replace(" ", "+")
                        + "&DeliveryAddress=" + paramsMap.get("DeliveryAddress") + "&Orderby=" + paramsMap.get("Orderby")
                        + "&POBLongitude=" + paramsMap.get("POBLongitude") + "&POBLatitude=" + paramsMap.get("POBLatitude")
                        + "&BookbyTrackingID=" + paramsMap.get("BookbyTrackingID") + "&AssignToTrackingID=" + paramsMap.get("AssignToTrackingID")
                        + "&RouteId=" + paramsMap.get("RouteId") + "&categoryId=" + paramsMap.get("categoryId")
                        + "&CompanySiteID=" + paramsMap.get("CompanySiteID") + "&CompanyID=" + paramsMap.get("CompanyID")
                        + "&CreatedBy=" + paramsMap.get("CreatedBy") + "&ItemIdList=" + paramsMap.get("ItemIdList")
                        + "&CostCtnPriceList=" + paramsMap.get("CostCtnPriceList") + "&CostPackPriceList=" + paramsMap.get("CostPackPriceList")
                        + "&CostPiecePriceList=" + paramsMap.get("CostPiecePriceList") + "&CtnQuantityList=" + paramsMap.get("CtnQuantityList")
                        + "&PackQuantityList=" + paramsMap.get("PackQuantityList") + "&PcsQuantityList=" + paramsMap.get("PcsQuantityList")
                        + "&FOCQtyList=" + paramsMap.get("FOCQtyList") + "&FOCValueList=" + paramsMap.get("FOCValueList")
                        + "&FOCPercentageList=" + paramsMap.get("FOCPercentageList") + "&item_DiscountList=" + paramsMap.get("item_DiscountList")
                        + "&TotalQuanityList=" + paramsMap.get("TotalQuanityList") + "&TotalCostList=" + paramsMap.get("TotalCostList")
                        + "&TotalwholesaleList=" + paramsMap.get("TotalwholesaleList") + "&TotalRetailList=" + paramsMap.get("TotalRetailList")
                        + "&CategoryIdList=" + paramsMap.get("CategoryIdList") + "&WSCtnPriceList=" + paramsMap.get("WSCtnPriceList")
                        + "&WSPackPriceList=" + paramsMap.get("WSPackPriceList") + "&WSPiecePriceList=" + paramsMap.get("WSPiecePriceList")
                        + "&RetailCtnPriceList=" + paramsMap.get("RetailCtnPriceList") + "&RetailPackPriceList=" + paramsMap.get("RetailPackPriceList")
                        + "&RetailPiecePriceList=" + paramsMap.get("RetailPiecePriceList") + "&DisplayPriceList=" + paramsMap.get("DisplayPriceList")
                        + "&GSTValueList=" + paramsMap.get("GSTValueList") + "&GSTPercentageList=" + paramsMap.get("GSTPercentageList")
                        + "&RejectCtnQtyList=" + paramsMap.get("RejectCtnQtyList") + "&RejectPackQtyList=" + paramsMap.get("RejectPackQtyList")
                        + "&RejectPcsQtyList=" + paramsMap.get("RejectPcsQtyList") + "&DeliverCtnQtyList=" + paramsMap.get("DeliverCtnQtyList")
                        + "&DeliverPackQtyList=" + paramsMap.get("DeliverPackQtyList") + "&DeliverPcsQtyList=" + paramsMap.get("DeliverPcsQtyList")
                        + "&NetAmountList=" + paramsMap.get("NetAmountList") + "&OrderIDList=" + deliverInfo.getOrderNumber()
                        + "&EmptyCountList=" + emptyBottles + "&TotalGSTValue=" + deliverInfo.getGst()
                        + "&GrossAmountList=" + paramsMap.get("NetAmountList") + "&DiscountedAmountList=" + paramsMap.get("NetAmountList")
                        + "&IncludingSTAmountList=" + paramsMap.get("NetAmountList") + "&ASTValueList=0" + "&ASTPercentageList=0";


                Observable<String> hResponseObservable = Api_Reto.getRetrofit().getRetrofit_services().hMakeNewOrder(GetPODDataParamList());
                CompositeDisposable hCompositeDisposable = new CompositeDisposable();

                Disposable hAddNewOrderDisposable = hResponseObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                                    if (response != null) {
                                        try {
                                            JSONArray parentArray = new JSONArray(response);


                                            if (!(parentArray.length() > 0)) {
                                                updateStatusToDB();
                                            } else {

                                                db.UpdateOrderStatus(deliverInfo);
                                                ArrayList<DeliveryItemModel> Itemlist = new ArrayList<>();
                                                JSONObject object = parentArray.getJSONObject(0);
                                                String status = object.getString("status");

                                                if (status.equalsIgnoreCase("Success")) {

                                                    JSONArray orderArray = object.getJSONArray("Order");
                                                    if (orderArray.length() > 0) {
                                                        JSONObject orderObject = orderArray.getJSONObject(0);
                                                        Server_deliver_id = String.valueOf(orderObject.getInt("OrderId"));

                                                    }


                                                    JSONArray orderDetailArray = object.getJSONArray("OrderDetail");

                                                    // For Updating Server Id...
                                                    if (orderDetailArray.length() > 0) {
                                                        for (int i = 0; i < Itemlist.size(); i++) {
                                                            JSONObject orderDetailObject = orderDetailArray.getJSONObject(i);
                                                            DeliveryItemModel model = new DeliveryItemModel();
                                                            model.setOrder_item_id(Itemlist.get(i).getOrder_item_id());
                                                            model.setServer_Item_Id(orderDetailObject.getInt("OrderDetailId"));
                                                            db.UpdateServerItemID(model);
                                                        }
                                                    }



                                                    db.UpdateSynChanges(deliverInfo.getDelivery_id());
                                                    deliverInfo.setIsPod_sync("2");
                                                    if (IsNew.equals("True")) {
                                                        ShowTakenOrderActivity.updateTakenOrderListview(deliverInfo); // updating the listview
                                                    }

                                                    deliveryIdListIndex++;

                                                    if (deliveryIdListIndex < deliveyIdList.size()) {
                                                        progressDialog.incrementProgressBy(1);

                                                        if (IsNew.equals("True")) {

                                                            SaveOrder(deliveyIdList.get(deliveryIdListIndex));
                                                        }
                                                    } else {

                                                        Toast.makeText(context, "Order Sync Successful", Toast.LENGTH_SHORT).show();
                                                        db.UpdateSynChanges(deliverInfo.getDelivery_id());
                                                        progressDialog.dismiss();
                                                        Intent intent = new Intent(context, WelcomeActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        context.startActivity(intent);

                                                    }

                                                }

                                            }
                                        } catch (Exception e) {
                                            e.getMessage();

                                            updateStatusToDB();
                                            progressDialog.dismiss();
                                        }
                                    } else {

                                        updateStatusToDB();
                                        // pb.setVisibility(View.GONE);
                                        progressDialog.dismiss();
                                    }


                                } /*OnNext*/,
                                throwable -> {
                                    progressDialog.dismiss();
                                    if (throwable instanceof ServerError) {

                                    } else if (throwable instanceof NetworkError) {

                                        Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show();
                                    } else if (throwable instanceof TimeoutError) {

                                        Toast.makeText(context, "Connection timeout error", Toast.LENGTH_SHORT).show();
                                    }


                                }/*On Error*/,
                                () -> {

                                    hCompositeDisposable.dispose();

                                }/*On Complete*/

                        );

                hCompositeDisposable.add(hAddNewOrderDisposable);



            } else {

                Timber.d("Booking case is running");

                progressDialog.show();
                api = "api/Order/addNewOrderV2";

// code commented of API call
//                String rAddNewSaleOrder = Utility.BASE_LIVE_URL + api + "?EmployeeId=" + paramsMap.get("EmployeeId") +
//                        "&CustomerId=" + paramsMap.get("CustomerId") + "&VehicleId=1" +
//                        "&OrderNo=" + orderno + "&SerialNo=" + paramsMap.get("SerialNo") + "&OrderDateTime=" +
//                        paramsMap.get("OrderDateTime").replace(" ", "+") + "&SalesMode=" +
//                        paramsMap.get("SalesModel") + "&Status=" + paramsMap.get("Status") + "&Description=" +
//                        paramsMap.get("Description") + "&TotalQuantity=" + paramsMap.get("TotalQuantity") + "&TotalAmount=" +
//                        paramsMap.get("TotalAmount") + "&Discount=" + paramsMap.get("Discount") + "&DiscountPercentage=" +
//                        paramsMap.get("DiscountPercentage") + "&NetTotal=" + paramsMap.get("NetTotal") + "&DeliveryDateTime=" +
//                        paramsMap.get("DeliveryDateTime").replace(" ", "+") + "&DeliveryAddress=" +
//                        paramsMap.get("DeliveryAddress") + "&Orderby=" + paramsMap.get("Orderby") + "&POBLongitude=" +
//                        paramsMap.get("POBLongitude") + "&POBLatitude=" + paramsMap.get("POBLatitude") + "&BookbyTrackingID=" +
//                        paramsMap.get("BookbyTrackingID") + "&AssignToTrackingID=" + paramsMap.get("AssignToTrackingID") + "&RouteId=" +
//                        paramsMap.get("RouteId") + "&categoryId=" + paramsMap.get("categoryId") + "&CompanySiteID=" +
//                        paramsMap.get("CompanySiteID") + "&CompanyID=" + paramsMap.get("CompanyID") + "&CreatedBy=" +
//                        paramsMap.get("CreatedBy") + "&ItemIdList="+paramsMap.get("ItemIdList")  + "&CostCtnPriceList=" +
//                        paramsMap.get("CostCtnPriceList") + "&CostPackPriceList=" + paramsMap.get("CostPackPriceList") + "&CostPiecePriceList=" +
//                        paramsMap.get("CostPiecePriceList") + "&CtnQuantityList=" + paramsMap.get("CtnQuantityList") + "&PackQuantityList=" +
//                        paramsMap.get("PackQuantityList") + "&PcsQuantityList=" + paramsMap.get("PcsQuantityList") + "&FOCQtyList=" +
//                        paramsMap.get("FOCQtyList") + "&FOCValueList=" + paramsMap.get("FOCValueList") + "&FOCPercentageList=" +
//                        paramsMap.get("FOCPercentageList") + "&item_DiscountList=" + paramsMap.get("item_DiscountList") + "&TotalQuanityList=" +
//                        paramsMap.get("TotalQuanityList") + "&TotalCostList=" + paramsMap.get("TotalCostList") + "&TotalwholesaleList=" +
//                        paramsMap.get("TotalwholesaleList") + "&TotalRetailList=" + paramsMap.get("TotalRetailList") + "&CategoryIdList=" +
//                        paramsMap.get("CategoryIdList") + "&WSCtnPriceList=" + paramsMap.get("WSCtnPriceList") + "&WSPackPriceList=" +
//                        paramsMap.get("WSPackPriceList") + "&WSPiecePriceList=" + paramsMap.get("WSPiecePriceList") + "&RetailCtnPriceList=" +
//                        paramsMap.get("RetailCtnPriceList") + "&RetailPackPriceList=" + paramsMap.get("RetailPackPriceList") + "&RetailPiecePriceList=" +
//                        paramsMap.get("RetailPiecePriceList") + "&DisplayPriceList=" + paramsMap.get("DisplayPriceList") + "&GSTValueList=" +
//                        paramsMap.get("GSTValueList") + "&GSTPercentageList=" + paramsMap.get("GSTPercentageList") + "&RejectCtnQtyList=" +
//                        paramsMap.get("RejectCtnQtyList") + "&RejectPackQtyList=" + paramsMap.get("RejectPackQtyList") + "&RejectPcsQtyList=" +
//                        paramsMap.get("RejectPcsQtyList") + "&DeliverCtnQtyList=" + paramsMap.get("DeliverCtnQtyList") + "&DeliverPackQtyList=" +
//                        paramsMap.get("DeliverPackQtyList") + "&DeliverPcsQtyList=" + paramsMap.get("DeliverPcsQtyList") + "&NetAmountList=" +
//                        paramsMap.get("NetAmountList") + "&EmptyCountList=" + emptyBottles + "&TotalGSTValue=" + deliverInfo.getGst() + "&BankID=" +
//                        paramsMap.get("BankID") + "&IsUrgent=" + urgentOrderStatus + "&PODAttachmentList=" + rImagesNames
//                        + "&DiscountPolicyIdList=" + paramsMap.get("DiscountPolicyIdList")
//                        + "&DiscPercentageList=" + paramsMap.get("DiscPercentageList")
//                        + "&DiscountList=" + paramsMap.get("DiscPolicyList")
//                        + "&BonusPolicyIdList=" + paramsMap.get("BonusPolicyIdList")
//                        + "&BonusQtyList=" + paramsMap.get("BonusQtyList")
//                        + "&BonusGSTList=" + paramsMap.get("BonusGSTList")
//                        + "&IncentiveItemIdList=" + paramsMap.get("BonusItemList")
//                        + "&DistributorId=" + deliverInfo.getDistributorId()
//                        + "&SubDistributorId=" + deliverInfo.getSubDistributorId()
//                        + "&resync="+resyncStatus;
//
//                Timber.d("Send order url is "+rAddNewSaleOrder);

                Timber.d("Payload is "+paramsMap1);

                Observable<String> hResponseObservable = Api_Reto.getRetrofit().getRetrofit_services().hMakeNewOrder(paramsMap1);
                CompositeDisposable hCompositeDisposable = new CompositeDisposable();

                Disposable hAddNewOrderDisposable = hResponseObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                                    if (response != null) {

                                        Timber.d("Not null case is running");

                                        try {

                                            JSONArray parentArray = new JSONArray(response);

                                                ArrayList<DeliveryItemModel> Itemlist = new ArrayList<>();
                                                JSONObject object = parentArray.getJSONObject(0);
                                                String status = object.getString("status");


                                                if (status.equalsIgnoreCase("Success")) {

                                                    Timber.d("The case is "+status+" and its running inside of success block");

                                                    db.UpdateOrderStatus(deliverInfo);
                                                    updateStatusToDB();

                                                    JSONArray orderArray = object.getJSONArray("Order");
                                                    if (orderArray.length() > 0) {
                                                        JSONObject orderObject = orderArray.getJSONObject(0);
                                                        Server_deliver_id = String.valueOf(orderObject.getInt("OrderId"));

                                                    }

                                                    JSONArray orderDetailArray = object.getJSONArray("OrderDetail");


                                                    // For Updating Server Id...
                                                    if (orderDetailArray.length() > 0) {
                                                        for (int i = 0; i < Itemlist.size(); i++) {
                                                            JSONObject orderDetailObject = orderDetailArray.getJSONObject(i);
                                                            DeliveryItemModel model = new DeliveryItemModel();
                                                            model.setOrder_item_id(Itemlist.get(i).getOrder_item_id());
                                                            model.setServer_Item_Id(orderDetailObject.getInt("OrderDetailId"));
                                                            db.UpdateServerItemID(model);
                                                        }
                                                    }

                                                    db.UpdateSynChanges(deliverInfo.getDelivery_id());
                                                    deliverInfo.setIsPod_sync("2");
                                                    if (IsNew.equals("True")) {
                                                        ShowTakenOrderActivity.updateTakenOrderListview(deliverInfo); // updating the listview
                                                    }

                                                    deliveryIdListIndex++;

                                                    if (deliveryIdListIndex < deliveyIdList.size()) {
                                                        progressDialog.incrementProgressBy(1);

                                                        if (IsNew.equals("True")) {

                                                            SaveOrder(deliveyIdList.get(deliveryIdListIndex));
                                                        }
                                                    } else {
                                                        Toast.makeText(context, "Order Sync Successful", Toast.LENGTH_SHORT).show();
                                                        db.UpdateSynChanges(deliverInfo.getDelivery_id());
                                                        progressDialog.dismiss();
                                                        Intent intent = new Intent(context, WelcomeActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        context.startActivity(intent);

                                                    }

                                                }else{
                                                    progressDialog.dismiss();
                                                    Toast.makeText(context, "Try again Order not synced Successfully", Toast.LENGTH_SHORT).show();
                                                }



                                        } catch (Exception e) {
                                            e.getMessage();
//                                          updateStatusToDB();
                                            progressDialog.dismiss();
                                        }
                                    } else {

//                                        updateStatusToDB();
                                        // pb.setVisibility(View.GONE);
                                        Timber.d("Response null case is running");
                                        progressDialog.dismiss();
                                    }


                                } /*OnNext*/,
                                throwable -> {
                                    progressDialog.dismiss();
                                    Timber.d("The Server error is "+throwable.getMessage());
                                        Toast.makeText(context, "Error From Server please retry  ", Toast.LENGTH_SHORT).show();

                                }/*On Error*/,
                                () -> {

                                    hCompositeDisposable.dispose();

                                }/*On Complete*/

                        );

                hCompositeDisposable.add(hAddNewOrderDisposable);


            }
//        } //commented for update case handling for later

    }


//    Extra Codes Commented that are not under usage

//    private void addNewOrder(String url) {
//        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                progressDialog.dismiss();
//
//                if (response != null) {
//                    try {
//
//
//                        JSONArray parentArray = new JSONArray(response);
//
//                        if (!(parentArray.length() > 0)) {
//                            Timber.d(" parentArray ! > 0 case is running...");
//                            //he is making sure there should be no change in local db record..
//                            updateStatusToDB();
//                        } else {
//                            Timber.d(" parentArray > 0 case is running...");
//
//                            db.UpdateOrderStatus(deliverInfo);
//
//                            ArrayList<DeliveryItemModel> Itemlist = new ArrayList<>();
//                            JSONObject object = parentArray.getJSONObject(0);
//                            String status = object.getString("status");
//                            if (status.equalsIgnoreCase("Success")) {
//
//                                JSONArray orderArray = object.getJSONArray("Order");
//
//                                if (orderArray.length() > 0) {
//                                    JSONObject orderObject = orderArray.getJSONObject(0);
//                                    Server_deliver_id = String.valueOf(orderObject.getInt("OrderId"));
//
//                                }
//
//                                JSONArray orderDetailArray = object.getJSONArray("OrderDetail");
//
//
//                                // For Updating Server Id...
//                                if (orderDetailArray.length() > 0) {
//                                    for (int i = 0; i < Itemlist.size(); i++) {
//                                        JSONObject orderDetailObject = orderDetailArray.getJSONObject(i);
//                                        DeliveryItemModel model = new DeliveryItemModel();
//                                        model.setOrder_item_id(Itemlist.get(i).getOrder_item_id());
//                                        model.setServer_Item_Id(orderDetailObject.getInt("OrderDetailId"));
//                                        db.UpdateServerItemID(model);
//                                    }
//                                }
//
//                                db.UpdateSynChanges(deliverInfo.getDelivery_id());
//
//                                deliverInfo.setIsPod_sync("2");
//                                if (IsNew.equals("True")) {
//                                    ShowTakenOrderActivity.updateTakenOrderListview(deliverInfo); // updating the listview
//                                }
//
//                                deliveryIdListIndex++;
//
//                                if (deliveryIdListIndex < deliveyIdList.size()) {
//                                    progressDialog.incrementProgressBy(1);
//
//                                    if (IsNew.equals("True")) {
//
//                                        Utility.logCatMsg("SaveOreder Call");
//                                        SaveOrder(deliveyIdList.get(deliveryIdListIndex));
//                                    }
//                                } else {
//                                    Toast.makeText(context, "Order Sync Successful", Toast.LENGTH_SHORT).show();
//
//                                    db.UpdateSynChanges(deliverInfo.getDelivery_id());
//                                    progressDialog.dismiss();
//                                    Intent intent = new Intent(context, WelcomeActivity.class);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    context.startActivity(intent);
//
//                                }
//
//                            }
//
//                            // new PODInfo("insertJobPod", "ZEDtrack.asmx", GetPODDataParamList()).execute();
//                        }
//                    } catch (Exception e) {
//                        e.getMessage();
//                        Utility.logCatMsg("User Error " + e);
//                        updateStatusToDB();
//                        Utility.Toast(context, "Order Not Send");
//                        progressDialog.dismiss();
//                    }
//                } else {
//                    Utility.logCatMsg("****** NULL ******");
//                    Utility.Toast(context, "Server Error");
//                    updateStatusToDB();
//                    // pb.setVisibility(View.GONE);
//                    progressDialog.dismiss();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                progressDialog.dismiss();
//                Log.d("newOrder", error.getMessage() + "");
//                if (error instanceof ServerError) {
//                    Toast.makeText(context, "Server error occurred", Toast.LENGTH_SHORT).show();
//                } else if (error instanceof NetworkError) {
//                    Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show();
//                } else if (error instanceof TimeoutError) {
//                    Toast.makeText(context, "Connection timeout error", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }) {
//            @Override
//            public String getBodyContentType() {
//                return "application/json; charset=utf-8";
//            }
//        };
//
//        RequestQueue queue = Volley.newRequestQueue(context);
//        progressDialog.show();
//        queue.add(request);
//
//    }

    private void updateOrder() {

        Timber.d("Update order case is running");

        progressDialog.show();
        Api_Reto.getRetrofit().getRetrofit_services().updateOrder(paramsMap1)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

//                        MainActivity.updatePlainOrderListview(deliverInfo);
                        db.UpdateOrderStatus(deliverInfo);
                        db.UpdateSynChanges(deliverInfo.getDelivery_id());
                        progressDialog.dismiss();
                        Intent intent = new Intent(context, WelcomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(context, "Error !" + t.toString(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }


    private void updateStatusToDB() {

        deliverInfo = db.getSelectedSQLiteOrderDelivery(Integer.parseInt(deliveyIdList.get(deliveryIdListIndex)), IsNew);
        int j = db.UpdateOrderStatus(deliverInfo);

        if (j == 1) {
            Utility.logCatMsg("Record Updated in SQLite");

        } else {
            Utility.logCatMsg("Record Not Updated in SQLite");
        }
    }

}
