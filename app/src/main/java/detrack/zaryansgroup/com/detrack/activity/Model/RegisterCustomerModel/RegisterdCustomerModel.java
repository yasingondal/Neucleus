package detrack.zaryansgroup.com.detrack.activity.Model.RegisterCustomerModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by 6520 on 4/5/2016.
 */
public class RegisterdCustomerModel implements Serializable {


    @SerializedName("Name")
    @Expose
    String Name;
    @SerializedName("Title")
    @Expose
    String Title;
    @SerializedName("Address")
    @Expose
    String Address;
    @SerializedName("Address1")
    @Expose
    String Address1;
    @SerializedName("Phone2")
    @Expose
    String Phone;
    @SerializedName("Phone")
    @Expose
    String Cell;
    @SerializedName("Latitude")
    @Expose
    String lat;
    @SerializedName("Longitude")
    @Expose
    String lng;
    @SerializedName("City")
    @Expose
    String City;
    @SerializedName("Country")
    @Expose
    String Country;
    String IsSave;
    String totalbill;
    String percentage_discount;
    String grossTotal;
    @SerializedName("ContactCode")
    @Expose
    String code;
    String deliveryStatus;
    String CashMode;

    @SerializedName("ImageName")
    @Expose
    String ImageName;

    @SerializedName("SalesMode")
    @Expose
    String SalesMode;


    @SerializedName("DistributorId")
    @Expose
    int DistributorId;

    @SerializedName("SubDistributorId")
    @Expose
    int SubDistributorId;

    public int getDistributorId() {
        return DistributorId;
    }

    public void setDistributorId(int distributorId) {
        DistributorId = distributorId;
    }

    public int getSubDistributorId() {
        return SubDistributorId;
    }

    public void setSubDistributorId(int subDistributorId) {
        SubDistributorId = subDistributorId;
    }

    Float Discount;
    String netTotal;
    String pobLat;
    String pobLng;
    String OrderNumber;
    String SerialNo;
    @SerializedName("RouteId")
    @Expose
    int Route;
    String date;
    @SerializedName("ContactId")
    @Expose
    int Customer_id;
    String podLat;
    String podLng;
    int invNo;
    int totalQty;
    int serverOrderId;

    int CashDepositedBankId;


    //For Discount
    @SerializedName("ContactTypeId")
    @Expose
    int ContactTypeId;

    public int getContactTypeId() {
        return ContactTypeId;
    }

    public void setContactTypeId(int contactTypeId) {
        ContactTypeId = contactTypeId;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }

    public int getCashDepositedBankId() {
        return CashDepositedBankId;
    }

    public void setCashDepositedBankId(int cashDepositedBankId) {
        CashDepositedBankId = cashDepositedBankId;
    }


    public String getSalesMode() {
        return SalesMode;
    }

    public void setSalesMode(String salesMode) {
        SalesMode = salesMode;
    }


    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getServerOrderId() {
        return serverOrderId;
    }

    public void setServerOrderId(int serverOrderId) {
        this.serverOrderId = serverOrderId;
    }

    public int getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(int totalQty) {
        this.totalQty = totalQty;
    }

    public int getInvNo() {
        return invNo;
    }

    public void setInvNo(int invNo) {
        this.invNo = invNo;
    }

    public String getPodLat() {
        return podLat;
    }

    public void setPodLat(String podLat) {
        this.podLat = podLat;
    }

    public String getPodLng() {
        return podLng;
    }

    public void setPodLng(String podLng) {
        this.podLng = podLng;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getGrossTotal() {
        return grossTotal;
    }

    public void setGrossTotal(String grossTotal) {
        this.grossTotal = grossTotal;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getCashMode() {
        return CashMode;
    }

    public void setCashMode(String cashMode) {
        CashMode = cashMode;
    }

    public Float getDiscount() {
        return Discount;
    }

    public void setDiscount(Float discount) {
        Discount = discount;
    }

    public String getPobLng() {
        return pobLng;
    }

    public void setPobLng(String pobLng) {
        this.pobLng = pobLng;
    }

    public String getPobLat() {
        return pobLat;
    }

    public void setPobLat(String pobLat) {
        this.pobLat = pobLat;
    }

    public String getOrderNumber() {
        return OrderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        OrderNumber = orderNumber;
    }

    public String getSerialNo() {
        return SerialNo;
    }

    public void setSerialNo(String serialNo) {
        SerialNo = serialNo;
    }

    public int getRoute() {
        return Route;
    }

    public void setRoute(int route) {
        Route = route;
    }
//
    public String getTotalbill() {
        return totalbill;
    }

    public void setTotalbill(String totalbill) {
        this.totalbill = totalbill;
    }

    public String getPercentage_discount() {
        return percentage_discount;
    }

    public void setPercentage_discount(String percentage_discount) {
        this.percentage_discount = percentage_discount;
    }

    public String getNetTotal() {
        return netTotal;
    }

    public void setNetTotal(String netTotal) {
        this.netTotal = netTotal;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getAddress1() {
        return Address1;
    }

    public void setAddress1(String address1) {
        Address1 = address1;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getCell() {
        return Cell;
    }

    public void setCell(String cell) {
        Cell = cell;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String company_id) {
        City = company_id;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String company_site_id) {
        Country = company_site_id;
    }

    public String getIsSave() {
        return IsSave;
    }

    public void setIsSave(String isSave) {
        IsSave = isSave;
    }

    public int getCustomer_id() {
        return Customer_id;
    }

    public void setCustomer_id(int customer_id) {
        Customer_id = customer_id;
    }
}
