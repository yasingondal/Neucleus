package detrack.zaryansgroup.com.detrack.activity.Model.CustomerPriceModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import detrack.zaryansgroup.com.detrack.activity.Model.CompanyRouteModel.RouteModel;

public class CustomerPriceListModel {

    @SerializedName("Table")
    @Expose
    private List<CustomerPriceModel> table = null;

    public List<CustomerPriceModel> getTable() {
        return table;
    }

    public void setTable(List<CustomerPriceModel> table) {
        this.table = table;
    }
}
