package detrack.zaryansgroup.com.detrack.activity.Model;

import java.util.ArrayList;

import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.DeliveryItemModel;

public class OrderItemsListModel {

   private ArrayList<DeliveryItemModel> mOrderItems;

    public OrderItemsListModel(ArrayList<DeliveryItemModel> mOrderItems) {
        this.mOrderItems = mOrderItems;
    }

    public ArrayList<DeliveryItemModel> getmOrderItems() {
        return mOrderItems;
    }

    public void setmOrderItems(ArrayList<DeliveryItemModel> mOrderItems) {
        this.mOrderItems = mOrderItems;
    }
}
