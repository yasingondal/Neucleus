package detrack.zaryansgroup.com.detrack.activity.Model;

public class GeocodeAddressModel {


    private String city;
    private String state;
    private String country;
    private String  postalCode;
    private String  knownName;
    private String  fulladdres;


    public GeocodeAddressModel(String city, String state, String country, String postalCode, String knownName,String fulladdres) {
        this.city = city;
        this.state = state;
        this.country = country;
        this.postalCode = postalCode;
        this.knownName = knownName;
        this.fulladdres = fulladdres;
    }

    public String getFulladdres() {
        return fulladdres;
    }

    public void setFulladdres(String fulladdres) {
        this.fulladdres = fulladdres;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getKnownName() {
        return knownName;
    }

    public void setKnownName(String knownName) {
        this.knownName = knownName;
    }
}
