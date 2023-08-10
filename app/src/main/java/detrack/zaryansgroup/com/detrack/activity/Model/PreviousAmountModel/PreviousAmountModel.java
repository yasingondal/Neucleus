package detrack.zaryansgroup.com.detrack.activity.Model.PreviousAmountModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PreviousAmountModel {

    @SerializedName("PreviousAmount")
    @Expose
    private Double previousAmount;

    public Double getPreviousAmount() {
        return previousAmount;
    }

    public void setPreviousAmount(Double previousAmount) {
        this.previousAmount = previousAmount;
    }
}
