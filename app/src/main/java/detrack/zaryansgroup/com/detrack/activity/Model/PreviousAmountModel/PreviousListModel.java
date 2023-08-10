package detrack.zaryansgroup.com.detrack.activity.Model.PreviousAmountModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import detrack.zaryansgroup.com.detrack.activity.Model.LoginModel.LoginModel;

public class PreviousListModel {


    @SerializedName("Table")
    @Expose
    private List<PreviousAmountModel> table = null;

    public List<PreviousAmountModel> getTable() {
        return table;
    }

    public void setTable(List<PreviousAmountModel> table) {
        this.table = table;
    }
}
