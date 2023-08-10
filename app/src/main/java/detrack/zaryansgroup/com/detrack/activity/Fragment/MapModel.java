package detrack.zaryansgroup.com.detrack.activity.Fragment;

import com.google.android.gms.maps.model.LatLng;

public class MapModel {
    private String CustomerName;
    private String VisitStatus;
    private int CustomerId;
    private LatLng hLatLng;


    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getVisitStatus() {
        return VisitStatus;
    }

    public void setVisitStatus(String visitStatus) {
        VisitStatus = visitStatus;
    }

    public int getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(int customerId) {
        CustomerId = customerId;
    }

    public LatLng gethLatLng() {
        return hLatLng;
    }

    public void sethLatLng(LatLng hLatLng) {
        this.hLatLng = hLatLng;
    }


}
