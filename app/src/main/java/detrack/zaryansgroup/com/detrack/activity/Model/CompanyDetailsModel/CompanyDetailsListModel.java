package detrack.zaryansgroup.com.detrack.activity.Model.CompanyDetailsModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CompanyDetailsListModel {

    @SerializedName("Table")
    @Expose
    private List<CompanyDetailsModel> table = null;

    public List<CompanyDetailsModel> getTable() {
        return table;
    }

    public void setTable(List<CompanyDetailsModel> table) {
        this.table = table;
    }
}
