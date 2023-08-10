package detrack.zaryansgroup.com.detrack.activity.activites.DiscountPolicyWork;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class DiscountPolicyModel {

    @SerializedName("DiscountPolicyId")
    @Expose
     int     DiscountPolicyId;

    @SerializedName("Type")
    @Expose
     String  Type;

    @SerializedName("TypeId")
    @Expose
     int     TypeId;

    @SerializedName("DiscountType")
    @Expose
     String  DiscountType;

    @SerializedName("TargetValue")
    @Expose
     float   TargetValue;

    @SerializedName("TargetQty")
    @Expose
     int     TargetQty;

    @SerializedName("DiscountPercentage")
    @Expose
     float   DiscountPercentage;

    @SerializedName("DiscountValue")
    @Expose
     float   DiscountValue;

    @SerializedName("AdditionalDiscountPercentage")
    @Expose
     float   AdditionalDiscountPercentage;

    @SerializedName("AdditionalDiscountValue")
    @Expose
     float   AdditionalDiscountValue;

    @SerializedName("StopOtherDiscount")
    @Expose
     String  StopOtherDiscount;

    @SerializedName("IsClaimable")
    @Expose
     String  IsClaimable;

    @SerializedName("StartDate")
    @Expose
     String  StartDate;

    @SerializedName("EndDate")
    @Expose
     String  EndDate;

    @SerializedName("GroupDivId")
    @Expose
     int     GroupDivId;

    @SerializedName("DivId")
    @Expose
     int     DivId;

    @SerializedName("ItemId")
    @Expose
     int     ItemId;


    public DiscountPolicyModel(int discountPolicyId, String type, int typeId, String discountType, float targetValue, int targetQty, float discountPercentage, float discountValue, float additionalDiscountPercentage, float additionalDiscountValue, String stopOtherDiscount, String isClaimable, String startDate, String endDate, int groupDivId, int divId, int itemId) {
        DiscountPolicyId = discountPolicyId;
        Type = type;
        TypeId = typeId;
        DiscountType = discountType;
        TargetValue = targetValue;
        TargetQty = targetQty;
        DiscountPercentage = discountPercentage;
        DiscountValue = discountValue;
        AdditionalDiscountPercentage = additionalDiscountPercentage;
        AdditionalDiscountValue = additionalDiscountValue;
        StopOtherDiscount = stopOtherDiscount;
        IsClaimable = isClaimable;
        StartDate = startDate;
        EndDate = endDate;
        GroupDivId = groupDivId;
        DivId = divId;
        ItemId = itemId;
    }

    public DiscountPolicyModel() {

    }

    public int getDiscountPolicyId() {
        return DiscountPolicyId;
    }

    public void setDiscountPolicyId(int discountPolicyId) {
        DiscountPolicyId = discountPolicyId;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public int getTypeId() {
        return TypeId;
    }

    public void setTypeId(int typeId) {
        TypeId = typeId;
    }

    public String getDiscountType() {
        return DiscountType;
    }

    public void setDiscountType(String discountType) {
        DiscountType = discountType;
    }

    public float getTargetValue() {
        return TargetValue;
    }

    public void setTargetValue(float targetValue) {
        TargetValue = targetValue;
    }

    public int getTargetQty() {
        return TargetQty;
    }

    public void setTargetQty(int targetQty) {
        TargetQty = targetQty;
    }

    public float getDiscountPercentage() {
        return DiscountPercentage;
    }

    public void setDiscountPercentage(float discountPercentage) {
        DiscountPercentage = discountPercentage;
    }

    public float getDiscountValue() {
        return DiscountValue;
    }

    public void setDiscountValue(float discountValue) {
        DiscountValue = discountValue;
    }

    public float getAdditionalDiscountPercentage() {
        return AdditionalDiscountPercentage;
    }

    public void setAdditionalDiscountPercentage(float additionalDiscountPercentage) {
        AdditionalDiscountPercentage = additionalDiscountPercentage;
    }

    public float getAdditionalDiscountValue() {
        return AdditionalDiscountValue;
    }

    public void setAdditionalDiscountValue(float additionalDiscountValue) {
        AdditionalDiscountValue = additionalDiscountValue;
    }

    public String getStopOtherDiscount() {
        return StopOtherDiscount;
    }

    public void setStopOtherDiscount(String stopOtherDiscount) {
        StopOtherDiscount = stopOtherDiscount;
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

    public int getGroupDivId() {
        return GroupDivId;
    }

    public void setGroupDivId(int groupDivId) {
        GroupDivId = groupDivId;
    }

    public int getDivId() {
        return DivId;
    }

    public void setDivId(int divId) {
        DivId = divId;
    }

    public int getItemId() {
        return ItemId;
    }

    public void setItemId(int itemId) {
        ItemId = itemId;
    }
}
