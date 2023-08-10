package detrack.zaryansgroup.com.detrack.activity.Model.CompanyRouteModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CompanyRouteListModel {

    @SerializedName("Table")
    @Expose
    private List<RouteModel> table = null;

    public List<RouteModel> getTable() {
        return table;
    }

    public void setTable(List<RouteModel> table) {
        this.table = table;
    }
}
