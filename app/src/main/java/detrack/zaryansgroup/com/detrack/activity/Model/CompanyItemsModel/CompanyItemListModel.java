package detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CompanyItemListModel {

    @SerializedName("Table")
    @Expose
    private List<DeliveryItemModel> table = null;

    public List<DeliveryItemModel> getTable() {
        return table;
    }

    public void setTable(List<DeliveryItemModel> table) {
        this.table = table;
    }
}
