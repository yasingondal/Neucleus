package detrack.zaryansgroup.com.detrack.activity.Model;

/**
 * Created by 6520 on 2/26/2016.
 */
public class MapModel {
    String Lat;
    String Lng;
    int Delivery_id;

    public int getDelivery_id() {
        return Delivery_id;
    }

    public void setDelivery_id(int delivery_id) {
        Delivery_id = delivery_id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    String Name;

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getLng() {
        return Lng;
    }

    public void setLng(String lng) {
        Lng = lng;
    }
}
