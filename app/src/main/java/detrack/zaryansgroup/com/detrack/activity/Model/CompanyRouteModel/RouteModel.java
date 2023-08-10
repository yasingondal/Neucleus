package detrack.zaryansgroup.com.detrack.activity.Model.CompanyRouteModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jawad on 8/15/2016.
 * updted 19 feb,2021 by Mubashir Murtaza
 */
public class RouteModel {

    @SerializedName("Route_Id")
    @Expose
    private int Route_id;
    @SerializedName("Title")
    @Expose
    private String Route_name;
    @SerializedName("Code")
    @Expose
    private String Route_code;
    @SerializedName("Descript")
    @Expose
    private String Route_description;

    public String getRoute_description() {
        return Route_description;
    }

    public void setRoute_description(String route_description) {
        Route_description = route_description;
    }


    public String getRoute_code() {
        return Route_code;
    }

    public void setRoute_code(String route_code) {
        Route_code = route_code;
    }

    public String getRoute_name() {
        return Route_name;
    }

    public void setRoute_name(String route_name) {
        Route_name = route_name;
    }

    public int getRoute_id() {
        return Route_id;
    }

    public void setRoute_id(int route_id) {
        Route_id = route_id;
    }


}
