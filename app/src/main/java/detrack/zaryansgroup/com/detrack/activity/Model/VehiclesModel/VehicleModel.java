package detrack.zaryansgroup.com.detrack.activity.Model.VehiclesModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hussainjawad on 12/7/2016.
 */
public class VehicleModel {

    @SerializedName("id")
    @Expose
    int id;
    @SerializedName("RegNo")
    @Expose
    String regNo;
    Boolean isSelected = false;

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }
}
