package detrack.zaryansgroup.com.detrack.activity.activites.BonusPolicyWork;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BonusPolicyModel {

    @SerializedName("BonusPolicesId")
    @Expose
    int BonusPolicesId;

    @SerializedName("TypeId")
    @Expose
    int TypeId;

    @SerializedName("TargetQty")
    @Expose
    int TargetQty;

    @SerializedName("Bonus")
    @Expose
    int Bonus;

    @SerializedName("ItemId")
    @Expose
    int ItemId;

    @SerializedName("IncentiveItemId")
    @Expose
    int  IncentiveItemId;


    @SerializedName("Type")
    @Expose
    String Type;

    @SerializedName("DiscountType")
    @Expose
    String DiscountType;


    @SerializedName("OurShare")
    @Expose
    float OurShare;


    @SerializedName("IsClaimable")
    @Expose
    String IsClaimable;

    @SerializedName("StartDate")
    @Expose
    String StartDate;

    @SerializedName("EndDate")
    @Expose
    String EndDate;

    @SerializedName("SubType")
    @Expose
    String SubType;

    @SerializedName("MultiItemsName")
    @Expose
    String MultiItemsName;

    @SerializedName("MultiItemsIdes")
    @Expose
    String MultiItemsIdes;


    public BonusPolicyModel(int bonusPolicesId, int typeId, int targetQty, int bonus, int itemId, int incentiveItemId, String type, String discountType, float ourShare, String isClaimable, String startDate, String endDate, String subType, String multiItemsName, String multiItemsIdes) {
        BonusPolicesId = bonusPolicesId;
        TypeId = typeId;
        TargetQty = targetQty;
        Bonus = bonus;
        ItemId = itemId;
        IncentiveItemId = incentiveItemId;
        Type = type;
        DiscountType = discountType;
        OurShare = ourShare;
        IsClaimable = isClaimable;
        StartDate = startDate;
        EndDate = endDate;
        SubType = subType;
        MultiItemsName = multiItemsName;
        MultiItemsIdes = multiItemsIdes;
    }

    public BonusPolicyModel() {

    }


    public int getBonusPolicesId() {
        return BonusPolicesId;
    }

    public void setBonusPolicesId(int bonusPolicesId) {
        BonusPolicesId = bonusPolicesId;
    }

    public int getTypeId() {
        return TypeId;
    }

    public void setTypeId(int typeId) {
        TypeId = typeId;
    }

    public int getTargetQty() {
        return TargetQty;
    }

    public void setTargetQty(int targetQty) {
        TargetQty = targetQty;
    }

    public int getBonus() {
        return Bonus;
    }

    public void setBonus(int bonus) {
        Bonus = bonus;
    }

    public int getItemId() {
        return ItemId;
    }

    public void setItemId(int itemId) {
        ItemId = itemId;
    }

    public int getIncentiveItemId() {
        return IncentiveItemId;
    }

    public void setIncentiveItemId(int incentiveItemId) {
        IncentiveItemId = incentiveItemId;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getDiscountType() {
        return DiscountType;
    }

    public void setDiscountType(String discountType) {
        DiscountType = discountType;
    }

    public float getOurShare() {
        return OurShare;
    }

    public void setOurShare(float ourShare) {
        OurShare = ourShare;
    }

    public String getIsClaimable() {
        return IsClaimable;
    }

    public void setIsClaimable(String isClaimable) {
        IsClaimable = isClaimable;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getSubType() {
        return SubType;
    }

    public void setSubType(String subType) {
        SubType = subType;
    }

    public String getMultiItemsName() {
        return MultiItemsName;
    }

    public void setMultiItemsName(String multiItemsName) {
        MultiItemsName = multiItemsName;
    }

    public String getMultiItemsIdes() {
        return MultiItemsIdes;
    }

    public void setMultiItemsIdes(String multiItemsIdes) {
        MultiItemsIdes = multiItemsIdes;
    }
}
