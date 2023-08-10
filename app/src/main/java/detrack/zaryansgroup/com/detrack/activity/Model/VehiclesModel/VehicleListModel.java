package detrack.zaryansgroup.com.detrack.activity.Model.VehiclesModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VehicleListModel {

    @SerializedName("Table")
    @Expose
    private List<VehicleModel> table = null;

    public List<VehicleModel> getTable() {
        return table;
    }

    public void setTable(List<VehicleModel> table) {
        this.table = table;
    }
}
