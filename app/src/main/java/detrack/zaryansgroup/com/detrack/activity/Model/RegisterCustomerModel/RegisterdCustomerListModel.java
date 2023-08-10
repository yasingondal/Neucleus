package detrack.zaryansgroup.com.detrack.activity.Model.RegisterCustomerModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class RegisterdCustomerListModel {


    @SerializedName("Table")
    @Expose
    private List<RegisterdCustomerModel> table = null;

    public List<RegisterdCustomerModel> getTable() {
        return table;
    }

    public void setTable(List<RegisterdCustomerModel> table) {
        this.table = table;
    }

}
