package detrack.zaryansgroup.com.detrack.activity.Model.LoginModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginListModel {


        @SerializedName("Table")
        @Expose
        private List<LoginModel> table = null;

        public List<LoginModel> getTable() {
            return table;
        }

        public void setTable(List<LoginModel> table) {
            this.table = table;
        }




}
