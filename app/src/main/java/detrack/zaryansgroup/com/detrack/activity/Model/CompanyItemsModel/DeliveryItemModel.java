package detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by 6520 on 2/17/2016.
 */
public class DeliveryItemModel implements Serializable{

    //Updated Model for adding Discount Policies
    @SerializedName("DivisionId")
    @Expose
    int DivisionId;

    @SerializedName("PriceId")
    @Expose
    int PriceId;

    @SerializedName("PieceSize")
    @Expose
    int PieceSize;

    @SerializedName("UnitSize")
    @Expose
    int UnitSize;

    @SerializedName("BrandId")
    @Expose
    int BrandId;

    @SerializedName("ActualDeliverd_Quantity")
    @Expose
    int ActualDeliverd_Quantity = 0;

    @SerializedName("Return_Quantity")
    @Expose
    int Return_Quantity = 0;

    @SerializedName("order_master_id")
    @Expose
    int order_master_id;

    @SerializedName("order_master_qty")
    @Expose
    int order_master_qty;

    @SerializedName("order_item_id")
    @Expose
    int order_item_id;

    @SerializedName("SKU")
    @Expose
    String SKU;

    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    //Params useful For Discount/Bonus Policy
    int   discountPolicyId;
    int    bonusPolicyId;


    float  bonusItemsGst;
    float  bonusGstPer;
    int    bonusQty;
    int    bonusIncentiveItemId;
    String bonusItemName;



    public float getBonusItemsGst() {
        return bonusItemsGst;
    }

    public void setBonusItemsGst(float bonusItemsGst) {
        this.bonusItemsGst = bonusItemsGst;
    }

    public float getBonusGstPer() {
        return bonusGstPer;
    }

    public void setBonusGstPer(float bonusGstPer) {
        this.bonusGstPer = bonusGstPer;
    }

    public String getBonusItemName() {
        return bonusItemName;
    }

    public void setBonusItemName(String bonusItemName) {
        this.bonusItemName = bonusItemName;
    }

    public int getBonusIncentiveItemId() {
        return bonusIncentiveItemId;
    }

    public void setBonusIncentiveItemId(int bonusIncentiveItemId) {
        this.bonusIncentiveItemId = bonusIncentiveItemId;
    }

    float discountPolicyValue;
    float DiscPercentage;


    public int getBonusPolicyId() {
        return bonusPolicyId;
    }

    public void setBonusPolicyId(int bonusPolicyId) {
        this.bonusPolicyId = bonusPolicyId;
    }

    public int getBonusQty() {
        return bonusQty;
    }

    public void setBonusQty(int bonusQty) {
        this.bonusQty = bonusQty;
    }

    public float getDiscPercentage() {
        return DiscPercentage;
    }

    public void setDiscPercentage(float discPercentage) {
        DiscPercentage = discPercentage;
    }


    //DISC/Bonus Params Ending here


    String RejectReason = "";
    String selectedValue = "";
    String QtyType = "",FocType = "";
    String Size;
    String IsASTPercent;
    String Unit;
    String IsSTPercent;
    String IsSTValue;

    float TotalwholeSalePrice = 0;
    float Price = 0;
    float ASTPercentage;
    float ASTValue;




    boolean isSelected ;

    int Route_id = 0,CategoryId = 0;
    int Delivery_id = 0;
    int Rejected_Quantity = 0;
    int Delivered_Quantity = 0;
    int Server_Item_Id = 0;
    int ctn_qty = 0;
    int pac_qty = 0;
    int pcs_qty = 0;
    int foc_qty = 0;
    int foc_value = 0;
    int foc_percentage = 0;
    int Deliver_ctn_qty = 0;
    int Deliver_pac_qty = 0;
    int Deliver_pcs_qty = 0;
    int Reject_ctn_qty = 0;
    int Reject_pac_qty = 0;
    int foc = 0;
    int Reject_pcs_qty = 0;


    @SerializedName("Title")
    @Expose
    String Title = "";
    @SerializedName("Id")
    @Expose
    int Item_id ;
    @SerializedName("Name")
    @Expose
    String Name = "";
    @SerializedName("Code")
    @Expose
    String Code = "";
    @SerializedName("ItemDetail")
    @Expose
    String ItemDetail = "";
    @SerializedName("CtnSize")
    @Expose
    int CtnSize = 0;
    int itemTotalDeliverQtyInPieces = 0;
    int itemTotalActualQtyInPieces = 0;

    @SerializedName("PackSize")
    @Expose
    int PackSize = 0;
    @SerializedName("CostCtnPrice")
    @Expose
    float CostCtnPrice = 0;
    @SerializedName("CostPackPrice")
    @Expose
    float  CostPackPrice = 0;
    @SerializedName("CostPiecePrice")
    @Expose
    float CostPiecePrice = 0;

    @SerializedName("RetailPiecePrice")
    @Expose
    float RetailPiecePrice = 0;

    @SerializedName("WSPackPrice")
    @Expose
    float WSPackPrice = 0;
    @SerializedName("WSPiecePrice")
    @Expose
    float WSPiecePrice = 0;

    @SerializedName("RetailCtnPrice")
    @Expose
    float RetailCtnPrice = 0;
    @SerializedName("RetailPackPrice")
    @Expose
    float RetailPackPrice = 0;
    @SerializedName("WSCtnPrice")
    @Expose
    float WSCtnPrice = 0;
    @SerializedName("DisplayPrice")
    @Expose
    float DisplayPrice = 0;
    float item_discount = 0;
    float TotalCostPrice = 0;
    @SerializedName("TotalRetail")
    @Expose
    float TotalRetailPrice = 0;
    float NetTotalRetailPrice = 0;
    @SerializedName("STValue")
    @Expose
    float ItemGstValue = 0;
    @SerializedName("STPercentage")
    @Expose
    float ItemGstPer = 0;
    int Total_Quantity = 0;
    @SerializedName("EmptyFlag")
    @Expose
    boolean emptyFlag ;
    int emptyBottles;
    @SerializedName("TaxCode")
    @Expose
    String  taxCode;


    @SerializedName("ImageName")
    @Expose
    String  ImageName;


    public int getDiscountPolicyId() {
        return discountPolicyId;
    }

    public void setDiscountPolicyId(int discountPolicyId) {
        this.discountPolicyId = discountPolicyId;
    }


    public float getDiscountPolicyValue() {
        return discountPolicyValue;
    }

    public void setDiscountPolicyValue(float discountPolicyValue) {
        this.discountPolicyValue = discountPolicyValue;
    }


    public int getDivisionId() {
        return DivisionId;
    }

    public void setDivisionId(int divisionId) {
        DivisionId = divisionId;
    }

    public int getPriceId() {
        return PriceId;
    }

    public void setPriceId(int priceId) {
        PriceId = priceId;
    }

    public int getPieceSize() {
        return PieceSize;
    }

    public void setPieceSize(int pieceSize) {
        PieceSize = pieceSize;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getIsSTPercent() {
        return IsSTPercent;
    }

    public void setIsSTPercent(String isSTPercent) {
        IsSTPercent = isSTPercent;
    }

    public String getIsSTValue() {
        return IsSTValue;
    }

    public void setIsSTValue(String isSTValue) {
        IsSTValue = isSTValue;
    }

    public float getASTPercentage() {
        return ASTPercentage;
    }

    public void setASTPercentage(float ASTPercentage) {
        this.ASTPercentage = ASTPercentage;
    }

    public float getASTValue() {
        return ASTValue;
    }

    public void setASTValue(float ASTValue) {
        this.ASTValue = ASTValue;
    }

    public String getIsASTPercent() {
        return IsASTPercent;
    }

    public void setIsASTPercent(String isASTPercent) {
        IsASTPercent = isASTPercent;
    }

    public int getUnitSize() {
        return UnitSize;
    }

    public void setUnitSize(int unitSize) {
        UnitSize = unitSize;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public int getBrandId() {
        return BrandId;
    }

    public void setBrandId(int brandId) {
        BrandId = brandId;
    }






    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public int getEmptyBottles() {
        return emptyBottles;
    }

    public void setEmptyBottles(int emptyBottles) {
        this.emptyBottles = emptyBottles;
    }

    public boolean getEmptyFlag() {
        return emptyFlag;
    }

    public void setEmptyFlag(boolean emptyFlag) {
        this.emptyFlag = emptyFlag;
    }

    public int getItemTotalActualQtyInPieces() {
        return itemTotalActualQtyInPieces;
    }

    public void setItemTotalActualQtyInPieces(int itemTotalActualQtyInPieces) {
        this.itemTotalActualQtyInPieces = itemTotalActualQtyInPieces;
    }


    public int getOrder_master_qty() {
        return order_master_qty;
    }

    public void setOrder_master_qty(int order_master_qty) {
        this.order_master_qty = order_master_qty;
    }


    public float getNetTotalRetailPrice() {
        return NetTotalRetailPrice;
    }

    public void setNetTotalRetailPrice(float netTotalRetailPrice) {
        NetTotalRetailPrice = netTotalRetailPrice;
    }

    public float getItemGstValue() {
        return ItemGstValue;
    }

    public void setItemGstValue(float itemGstValue) {
        ItemGstValue = itemGstValue;
    }

    public float getItemGstPer() {
        return ItemGstPer;
    }

    public void setItemGstPer(float itemGstPer) {
        ItemGstPer = itemGstPer;
    }
    public int getOrder_master_id() {
        return order_master_id;
    }

    public void setOrder_master_id(int order_master_id) {
        this.order_master_id = order_master_id;
    }

    public   DeliveryItemModel(DeliveryItemModel model){
        this.ActualDeliverd_Quantity=model.ActualDeliverd_Quantity;
        this.Return_Quantity=model.Return_Quantity;
        this.order_item_id=model.order_item_id;
        this.Item_id=model.Item_id;
        this.Name=model.Name;
        this.RejectReason=model.RejectReason;
        this.selectedValue=model.selectedValue;
        this.TotalwholeSalePrice=model.TotalwholeSalePrice;
        this. isSelected=model.isSelected;
        this.Route_id=model.Route_id;
        this.CategoryId=model.CategoryId;
        this. Price=model.Price;
        this.Delivery_id=model.Delivery_id;
        this.Rejected_Quantity=model.Rejected_Quantity;
        this.Delivered_Quantity=model.Delivered_Quantity;
        this.Server_Item_Id=model.Server_Item_Id;
        this.QtyType=model.QtyType;
        this.FocType=model.FocType;
        this.ctn_qty=model.ctn_qty;
        this.pac_qty=model.pac_qty;
        this.pcs_qty=model.pcs_qty;
        this.foc_qty=model.foc_qty;
        this.foc_value=model.foc_value;
        this.foc_percentage=model.foc_percentage;
        this.Deliver_ctn_qty=model.Deliver_ctn_qty;
        this.Deliver_pac_qty=model.Deliver_pac_qty;
        this.Deliver_pcs_qty=model.Deliver_pcs_qty;
        this.Reject_ctn_qty=model.Reject_ctn_qty;
        this.Reject_pac_qty=model.Reject_pac_qty;
        this. Reject_pcs_qty=model.Reject_pcs_qty;
        this. Title=model.Title;
        this. Code=model.Code;
        this. ItemDetail=model.ItemDetail;
        this. CtnSize=model.CtnSize;
        this.itemTotalDeliverQtyInPieces =model.itemTotalDeliverQtyInPieces;
        this. PackSize=model.PackSize;
        this. CostCtnPrice=model.CostCtnPrice;
        this.CostPackPrice=model.CostPackPrice;
        this.CostPiecePrice=model.CostPiecePrice;
        this.RetailPiecePrice =model.RetailPiecePrice;
        this.WSPackPrice=model.WSPackPrice;
        this.WSPiecePrice=model.WSPiecePrice;
        this.RetailCtnPrice=model.RetailCtnPrice;
        this.RetailPackPrice=model.RetailPackPrice;
        this.WSCtnPrice =model.WSCtnPrice;
        this.DisplayPrice=model.DisplayPrice;
        this.item_discount=model.item_discount;
        this.TotalCostPrice=model.TotalCostPrice;
        this. TotalRetailPrice=model.TotalRetailPrice;
        this. Total_Quantity=model.Total_Quantity;
        this.order_master_id = model.order_master_id;
        this.taxCode = model.taxCode;
    }

    public DeliveryItemModel() {
    }

    public int getReject_pcs_qty() {
        return Reject_pcs_qty;
    }

    public void setReject_pcs_qty(int reject_pcs_qty) {
        Reject_pcs_qty = reject_pcs_qty;
    }

    public int getReject_pac_qty() {
        return Reject_pac_qty;
    }

    public void setReject_pac_qty(int reject_pac_qty) {
        Reject_pac_qty = reject_pac_qty;
    }

    public int getReject_ctn_qty() {
        return Reject_ctn_qty;
    }

    public void setReject_ctn_qty(int reject_ctn_qty) {
        Reject_ctn_qty = reject_ctn_qty;
    }

    public int getDeliver_pcs_qty() {
        return Deliver_pcs_qty;
    }

    public void setDeliver_pcs_qty(int deliver_pcs_qty) {
        Deliver_pcs_qty = deliver_pcs_qty;
    }

    public int getDeliver_pac_qty() {
        return Deliver_pac_qty;
    }

    public void setDeliver_pac_qty(int deliver_pac_qty) {
        Deliver_pac_qty = deliver_pac_qty;
    }

    public int getDeliver_ctn_qty() {
        return Deliver_ctn_qty;
    }

    public void setDeliver_ctn_qty(int deliver_ctn_qty) {
        Deliver_ctn_qty = deliver_ctn_qty;
    }

    public int getItemTotalDeliverQtyInPieces() {
        return itemTotalDeliverQtyInPieces;
    }

    public void setItemTotalDeliverQtyInPieces(int itemTotalDeliverQtyInPieces) {
        this.itemTotalDeliverQtyInPieces = itemTotalDeliverQtyInPieces;
    }
    public int getPackSize() {
        return PackSize;
    }

    public void setPackSize(int packSize) {
        PackSize = packSize;
    }

    public int getCtnSize() {
        return CtnSize;
    }

    public void setCtnSize(int ctnSize) {
        CtnSize = ctnSize;
    }


    public int getTotal_Quantity() {
        return Total_Quantity;
    }

    public void setTotal_Quantity(int total_Quantity) {
        Total_Quantity = total_Quantity;
    }


    public float getItem_discount() {
        return item_discount;
    }

    public void setItem_discount(float item_discount) {
        this.item_discount = item_discount;
    }

    public float getTotalCostPrice() {
        return TotalCostPrice;
    }

    public void setTotalCostPrice(float totalCostPrice) {
        this.TotalCostPrice = totalCostPrice;
    }


    public float getTotalRetailPrice() {
        return TotalRetailPrice;
    }

    public void setTotalRetailPrice(float totalRetailPrice) {
        this.TotalRetailPrice = totalRetailPrice;
    }



    public int getItem_id() {
        return Item_id;
    }

    public void setItem_id(int item_id) {
        Item_id = item_id;
    }

    public int getOrder_item_id() {
        return order_item_id;
    }

    public void setOrder_item_id(int order_item_id) {
        this.order_item_id = order_item_id;
    }

    public int getRoute_id() {
        return Route_id;
    }

    public void setRoute_id(int route_id) {
        Route_id = route_id;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }
        public int getCtn_qty() {
        return ctn_qty;
    }

    public void setCtn_qty(int ctn_qty) {
        this.ctn_qty = ctn_qty;
    }

    public int getPac_qty() {
        return pac_qty;
    }

    public void setPac_qty(int pac_qty) {
        this.pac_qty = pac_qty;
    }

    public int getPcs_qty() {
        return pcs_qty;
    }

    public void setPcs_qty(int pcs_qty) {
        this.pcs_qty = pcs_qty;
    }

    public int getFoc_qty() {
        return foc_qty;
    }

    public void setFoc_qty(int foc_qty) {
        this.foc_qty = foc_qty;
    }

    public int getFoc_value() {
        return foc_value;
    }

    public void setFoc_value(int foc_value) {
        this.foc_value = foc_value;
    }

    public int getFoc_percentage() {
        return foc_percentage;
    }

    public void setFoc_percentage(int foc_percentage) {
        this.foc_percentage = foc_percentage;
    }


    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getItemDetail() {
        return ItemDetail;
    }

    public void setItemDetail(String itemDetail) {
        ItemDetail = itemDetail;
    }

    public float getCostCtnPrice() {
        return CostCtnPrice;
    }

    public void setCostCtnPrice(float costCtnPrice) {
        CostCtnPrice = costCtnPrice;
    }

    public float getCostPackPrice() {
        return CostPackPrice;
    }

    public void setCostPackPrice(float costPackPrice) {
        CostPackPrice = costPackPrice;
    }

    public float getCostPiecePrice() {
        return CostPiecePrice;
    }

    public void setCostPiecePrice(float costPiecePrice) {
        CostPiecePrice = costPiecePrice;
    }

    public float getRetailPiecePrice() {
        return RetailPiecePrice;
    }

    public void setRetailPiecePrice(float retailPiecePrice) {
        this.RetailPiecePrice = retailPiecePrice;
    }

    public float getWSPackPrice() {
        return WSPackPrice;
    }

    public void setWSPackPrice(float WSPackPrice) {
        this.WSPackPrice = WSPackPrice;
    }

    public float getWSPiecePrice() {
        return WSPiecePrice;
    }

    public void setWSPiecePrice(float WSPiecePrice) {
        this.WSPiecePrice = WSPiecePrice;
    }

    public float getRetailCtnPrice() {
        return RetailCtnPrice;
    }

    public void setRetailCtnPrice(float retailCtnPrice) {
        RetailCtnPrice = retailCtnPrice;
    }

    public float getRetailPackPrice() {
        return RetailPackPrice;
    }

    public void setRetailPackPrice(float retailPackPrice) {
        RetailPackPrice = retailPackPrice;
    }

    public float getWSCtnPrice() {
        return WSCtnPrice;
    }

    public void setWSCtnPrice(float WSCtnPrice) {
        this.WSCtnPrice = WSCtnPrice;
    }

    public float getDisplayPrice() {
        return DisplayPrice;
    }

    public void setDisplayPrice(float displayPrice) {
        DisplayPrice = displayPrice;
    }



    public String getFocType() {
        return FocType;
    }

    public void setFocType(String focType) {
        FocType = focType;
    }

    public String getQtyType() {
        return QtyType;
    }

    public void setQtyType(String qtyType) {
        QtyType = qtyType;
    }

    public int getFoc() {
        return foc;
    }

    public void setFoc(int foc) {
        this.foc = foc;
    }


    public int getServer_Item_Id() {
        return Server_Item_Id;
    }

    public void setServer_Item_Id(int server_Item_Id) {
        Server_Item_Id = server_Item_Id;
    }

    public int getActualDeliverd_Quantity() {
        return ActualDeliverd_Quantity;
    }

    public void setActualDeliverd_Quantity(int actualDeliverd_Quantity) {
        ActualDeliverd_Quantity = actualDeliverd_Quantity;
    }


    public int getReturn_Quantity() {
        return Return_Quantity;
    }

    public void setReturn_Quantity(int return_Quantity) {
        Return_Quantity = return_Quantity;
    }


    public float getTotalwholeSalePrice() {
        return TotalwholeSalePrice;
    }

    public void setTotalwholeSalePrice(float priceTotal) {
        this.TotalwholeSalePrice = priceTotal;
    }

    public String getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(String selectedValue) {
        this.selectedValue = selectedValue;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }



    public float getPrice() {
        return Price;
    }

    public void setPrice(float price) {
        Price = price;
    }

    public int getDelivery_id() {
        return Delivery_id;
    }

    public void setDelivery_id(int delivery_id) {
        Delivery_id = delivery_id;
    }

    public int getRejected_Quantity() {
        return Rejected_Quantity;
    }

    public void setRejected_Quantity(int rejected_Quantity) {
        Rejected_Quantity = rejected_Quantity;
    }

    public int getDelivered_Quantity() {
        return Delivered_Quantity;
    }

    public void setDelivered_Quantity(int delivered_Quantity) {
        Delivered_Quantity = delivered_Quantity;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getRejectReason() {
        return RejectReason;
    }

    public void setRejectReason(String rejectReason) {
        RejectReason = rejectReason;
    }

    @Override
    public String toString() {
        return Name;
    }
}
