package detrack.zaryansgroup.com.detrack.activity.Model.DistributorModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DistributorModel {

    @SerializedName("ContactId")
    @Expose
    private int ContactId;

    @SerializedName("Title")
    @Expose
    private String Title;

    @SerializedName("Name")
    @Expose
    private String Name;

    @SerializedName("FatherName")
    @Expose
    private String FatherName;

    @SerializedName("Address")
    @Expose
    private String Address;

    @SerializedName("City")
    @Expose
    private String City;

    @SerializedName("Country")
    @Expose
    private String Country;

    @SerializedName("DeliveryAddress")
    @Expose
    private String DeliveryAddress;

    @SerializedName("ContactGroup")
    @Expose
    private String ContactGroup;


    @SerializedName("Phone")
    @Expose
    private String Phone;

    @SerializedName("Phone2")
    @Expose
    private String Phone2;

    @SerializedName("Cell")
    @Expose
    private String Cell;

    @SerializedName("Fax")
    @Expose
    private String Fax;

    @SerializedName("Email")
    @Expose
    private String Email;

    @SerializedName("AreaId")
    @Expose
    private int AreaId;

    @SerializedName("DivisionId")
    @Expose
    private int DivisionId;

    @SerializedName("ZoneId")
    @Expose
    private int ZoneId;

    @SerializedName("CreditLimit")
    @Expose
    private String CreditLimit;

    @SerializedName("PercentageDisc1")
    @Expose
    private String PercentageDisc1;

    @SerializedName("PercentageDisc2")
    @Expose
    private String PercentageDisc2;

    @SerializedName("PercentageDisc3")
    @Expose
    private String PercentageDisc3;


    @SerializedName("Longitude")
    @Expose
    private String Longitude;

    @SerializedName("Latitude")
    @Expose
    private String Latitude;

    @SerializedName("Address1")
    @Expose
    private String Address1;

    @SerializedName("Address2")
    @Expose
    private String Address2;

    @SerializedName("RouteId")
    @Expose
    private int RouteId;

    @SerializedName("CompanySiteID")
    @Expose
    private int CompanySiteID;

    @SerializedName("CompanyID")
    @Expose
    private int CompanyID;

    @SerializedName("ContactCode")
    @Expose
    private String ContactCode;

    @SerializedName("ContactType")
    @Expose
    private String ContactType;

    @SerializedName("ContactTypeId")
    @Expose
    private int ContactTypeId;

    @SerializedName("SalesMode")
    @Expose
    private String SalesMode;

    @SerializedName("ImageName")
    @Expose
    private String ImageName;

    @SerializedName("DistributerId")
    @Expose
    private int DistributerId;

    @SerializedName("isDistributer")
    @Expose
    private String IsDistributer;



    public DistributorModel(int contactId, String title, String name, String fatherName, String address, String city, String country, String deliveryAddress, String contactGroup, String phone, String phone2, String cell, String fax, String email, int areaId, int divisionId, int zoneId, String creditLimit, String percentageDisc1, String percentageDisc2, String percentageDisc3, String longitude, String latitude, String address1, String address2, int routeId, int companySiteID, int companyID, String contactCode, String contactType, int contactTypeId, String salesMode, String imageName, int distributerId, String isDistributer) {
        ContactId = contactId;
        Title = title;
        Name = name;
        FatherName = fatherName;
        Address = address;
        City = city;
        Country = country;
        DeliveryAddress = deliveryAddress;
        ContactGroup = contactGroup;
        Phone = phone;
        Phone2 = phone2;
        Cell = cell;
        Fax = fax;
        Email = email;
        AreaId = areaId;
        DivisionId = divisionId;
        ZoneId = zoneId;
        CreditLimit = creditLimit;
        PercentageDisc1 = percentageDisc1;
        PercentageDisc2 = percentageDisc2;
        PercentageDisc3 = percentageDisc3;
        Longitude = longitude;
        Latitude = latitude;
        Address1 = address1;
        Address2 = address2;
        RouteId = routeId;
        CompanySiteID = companySiteID;
        CompanyID = companyID;
        ContactCode = contactCode;
        ContactType = contactType;
        ContactTypeId = contactTypeId;
        SalesMode = salesMode;
        ImageName = imageName;
        DistributerId = distributerId;
        IsDistributer = isDistributer;

    }


    public DistributorModel() {
    }

    public String getIsDistributer() {
        return IsDistributer;
    }

    public void setIsDistributer(String isDistributer) {
        IsDistributer = isDistributer;
    }

    public int getContactId() {
        return ContactId;
    }

    public void setContactId(int contactId) {
        ContactId = contactId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getFatherName() {
        return FatherName;
    }

    public void setFatherName(String fatherName) {
        FatherName = fatherName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getDeliveryAddress() {
        return DeliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        DeliveryAddress = deliveryAddress;
    }

    public String getContactGroup() {
        return ContactGroup;
    }

    public void setContactGroup(String contactGroup) {
        ContactGroup = contactGroup;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPhone2() {
        return Phone2;
    }

    public void setPhone2(String phone2) {
        Phone2 = phone2;
    }

    public String getCell() {
        return Cell;
    }

    public void setCell(String cell) {
        Cell = cell;
    }

    public String getFax() {
        return Fax;
    }

    public void setFax(String fax) {
        Fax = fax;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public int getAreaId() {
        return AreaId;
    }

    public void setAreaId(int areaId) {
        AreaId = areaId;
    }

    public int getDivisionId() {
        return DivisionId;
    }

    public void setDivisionId(int divisionId) {
        DivisionId = divisionId;
    }

    public int getZoneId() {
        return ZoneId;
    }

    public void setZoneId(int zoneId) {
        ZoneId = zoneId;
    }

    public String getCreditLimit() {
        return CreditLimit;
    }

    public void setCreditLimit(String creditLimit) {
        CreditLimit = creditLimit;
    }

    public String getPercentageDisc1() {
        return PercentageDisc1;
    }

    public void setPercentageDisc1(String percentageDisc1) {
        PercentageDisc1 = percentageDisc1;
    }

    public String getPercentageDisc2() {
        return PercentageDisc2;
    }

    public void setPercentageDisc2(String percentageDisc2) {
        PercentageDisc2 = percentageDisc2;
    }

    public String getPercentageDisc3() {
        return PercentageDisc3;
    }

    public void setPercentageDisc3(String percentageDisc3) {
        PercentageDisc3 = percentageDisc3;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getAddress1() {
        return Address1;
    }

    public void setAddress1(String address1) {
        Address1 = address1;
    }

    public String getAddress2() {
        return Address2;
    }

    public void setAddress2(String address2) {
        Address2 = address2;
    }

    public int getRouteId() {
        return RouteId;
    }

    public void setRouteId(int routeId) {
        RouteId = routeId;
    }

    public int getCompanySiteID() {
        return CompanySiteID;
    }

    public void setCompanySiteID(int companySiteID) {
        CompanySiteID = companySiteID;
    }

    public int getCompanyID() {
        return CompanyID;
    }

    public void setCompanyID(int companyID) {
        CompanyID = companyID;
    }

    public String getContactCode() {
        return ContactCode;
    }

    public void setContactCode(String contactCode) {
        ContactCode = contactCode;
    }

    public String getContactType() {
        return ContactType;
    }

    public void setContactType(String contactType) {
        ContactType = contactType;
    }

    public int getContactTypeId() {
        return ContactTypeId;
    }

    public void setContactTypeId(int contactTypeId) {
        ContactTypeId = contactTypeId;
    }

    public String getSalesMode() {
        return SalesMode;
    }

    public void setSalesMode(String salesMode) {
        SalesMode = salesMode;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }

    public int getDistributerId() {
        return DistributerId;
    }

    public void setDistributerId(int distributerId) {
        DistributerId = distributerId;
    }
}
