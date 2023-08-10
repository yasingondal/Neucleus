package detrack.zaryansgroup.com.detrack.activity.Model.CustomerPriceModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerPriceModel {


    @SerializedName("CustomerId")
    @Expose
    int CustomerId;
    @SerializedName("ItemId")
    @Expose
    int ItemId;
    @SerializedName("Price")
    @Expose
    float Price;

    public CustomerPriceModel(){

    }

    public CustomerPriceModel(int customerId, int itemId, float price) {
        CustomerId = customerId;
        ItemId = itemId;
        Price = price;
    }

    public int getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(int customerId) {
        CustomerId = customerId;
    }

    public int getItemId() {
        return ItemId;
    }

    public void setItemId(int itemId) {
        ItemId = itemId;
    }

    public float getPrice() {
        return Price;
    }

    public void setPrice(float price) {
        Price = price;
    }
}
