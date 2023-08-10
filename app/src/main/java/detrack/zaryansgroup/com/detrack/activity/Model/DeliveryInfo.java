package detrack.zaryansgroup.com.detrack.activity.Model;

import java.io.Serializable;

/**
 * Created by 6520 on 2/2/2016.
 */
public class DeliveryInfo implements Serializable{

    public int getCashDespositedBankId() {
        return CashDespositedBankId;
    }

    public void setCashDespositedBankId(int cashDespositedBankId) {
        CashDespositedBankId = cashDespositedBankId;
    }

    //By Yaseen
    int CashDespositedBankId;


    int Delivery_id, Server_Delivery_id;

    public String getIsUrgentOrderStatus() {
        return isUrgentOrderStatus;
    }

    public void setIsUrgentOrderStatus(String isUrgentOrderStatus) {
        this.isUrgentOrderStatus = isUrgentOrderStatus;
    }

    String isUrgentOrderStatus;



    int Total_qty;
    int Customer_id;
    int isOrderRead;
    String isSave;
    int vehicle_id;
    int emp_id;

    boolean resync;

    public boolean isResync() {
        return resync;
    }

    public void setResync(boolean resync) {
        this.resync = resync;
    }

    int DistributorId;
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

    String deliver_to_name;
    String delivery_address;
    String person_company;
    String delivery_status;
    String delivery_date;
    String delivery_start_time;
    String delivery_end_time;
    String delivery_to_mobile;
    String Is_delivery_Reject;
    String deliver_lat, deliver_lng;
    String isPod_sync;
    String Total_Bill;
    String grossTotalBill;
    String PercentageDiscount;
    String NetTotal;
    String Discount;
    boolean isCBChecked;
    String TrackingNo;
    String Rejected_Reason;
    String Refused_Reason;
    String Cancle_Reason;
    int isNewUpdate;
    String ReceivedBy;
    String pod_lat;
    String pod_lng;
    String note;
    String delivery_description;
    String pob_lat;
    String pob_lng, salemode;
    String OrderNumber;
    String SerialNo;
    String gst;
    String RefusedReason, CancelledReason;
    int categoryId;
    int Route;

    String fromserver = "0";

    public String getFromserver() {
        return fromserver;
    }

    public void setFromserver(String fromserver) {
        this.fromserver = fromserver;
    }

    public String  getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getIsSave() {
        return isSave;
    }

    public void setIsSave(String isSave) {
        this.isSave = isSave;
    }


    public int getIsNewUpdate() {
        return isNewUpdate;
    }

    public void setIsNewUpdate(int isNewUpdate) {
        this.isNewUpdate = isNewUpdate;
    }


    public int getIsOrderRead() {
        return isOrderRead;
    }

    public void setIsOrderRead(int isOrderRead) {
        this.isOrderRead = isOrderRead;
    }

    public int getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(int vehicle_id) {
        this.vehicle_id = vehicle_id;
    }


    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }


    public String getGrossTotalBill() {
        return grossTotalBill;
    }

    public void setGrossTotalBill(String grossTotalBill) {
        this.grossTotalBill = grossTotalBill;
    }

    public String getRefused_Reason() {
        return Refused_Reason;
    }

    public void setRefused_Reason(String refused_Reason) {
        Refused_Reason = refused_Reason;
    }

    public String getCancle_Reason() {
        return Cancle_Reason;
    }

    public void setCancle_Reason(String cancle_Reason) {
        Cancle_Reason = cancle_Reason;
    }

    public int getServer_Delivery_id() {
        return Server_Delivery_id;
    }

    public void setServer_Delivery_id(int server_Delivery_id) {
        Server_Delivery_id = server_Delivery_id;
    }

    public int getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(int emp_id) {
        this.emp_id = emp_id;
    }

    public String getSalemode() {
        return salemode;
    }

    public void setSalemode(String salemode) {
        this.salemode = salemode;
    }


    public String getSerialNo() {
        return SerialNo;
    }

    public void setSerialNo(String serialNo) {
        SerialNo = serialNo;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }


    public String getRefusedReason() {
        return RefusedReason;
    }

    public void setRefusedReason(String refusedReason) {
        RefusedReason = refusedReason;
    }

    public String getCancelledReason() {
        return CancelledReason;
    }

    public void setCancelledReason(String cancelledReason) {
        CancelledReason = cancelledReason;
    }

    public int getRoute() {
        return Route;
    }

    public void setRoute(int route) {
        Route = route;
    }



    public String getPob_lat() {
        return pob_lat;
    }

    public void setPob_lat(String pob_lat) {
        this.pob_lat = pob_lat;
    }

    public String getPob_lng() {
        return pob_lng;
    }

    public void setPob_lng(String pob_lng) {
        this.pob_lng = pob_lng;
    }

    public String getOrderNumber() {
        return OrderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        OrderNumber = orderNumber;
    }


    public boolean isCBChecked() {
        return isCBChecked;
    }

    public void setIsCBChecked(boolean isCBChecked) {
        this.isCBChecked = isCBChecked;
    }


    public String getTotal_Bill() {
        return Total_Bill;
    }

    public void setTotal_Bill(String total_Bill) {
        Total_Bill = total_Bill;
    }

    public String getPercentageDiscount() {
        return PercentageDiscount;
    }

    public void setPercentageDiscount(String percentageDiscount) {
        PercentageDiscount = percentageDiscount;
    }

    public String getNetTotal() {
        return NetTotal;
    }

    public void setNetTotal(String netTotal) {
        NetTotal = netTotal;
    }

    public int getCustomer_id() {
        return Customer_id;
    }

    public void setCustomer_id(int customer_id) {
        Customer_id = customer_id;
    }

    public DeliveryInfo() {
        isPod_sync = "0";
    }

    public String getIsPod_sync() {
        return isPod_sync;
    }

    public void setIsPod_sync(String isPod_sync) {
        this.isPod_sync = isPod_sync;
    }

    public String getDeliver_lat() {
        return deliver_lat;
    }

    public void setDeliver_lat(String deliver_lat) {
        this.deliver_lat = deliver_lat;
    }

    public String getDeliver_lng() {
        return deliver_lng;
    }

    public void setDeliver_lng(String deliver_lng) {
        this.deliver_lng = deliver_lng;
    }


    public String getTrackingNo() {
        return TrackingNo;
    }

    public void setAssign_to_TrackingNo(String trackingNo) {
        TrackingNo = trackingNo;
    }

    public String getPerson_company() {
        return person_company;
    }

    public void setPerson_company(String person_company) {
        this.person_company = person_company;
    }

    public int getDelivery_id() {
        return Delivery_id;
    }

    public void setDelivery_id(int delivery_id) {
        Delivery_id = delivery_id;
    }

    public int getTotal_qty() {
        return Total_qty;
    }

    public void setTotal_qty(int total_qty) {
        Total_qty = total_qty;
    }

    public String getDeliver_to_name() {
        return deliver_to_name;
    }

    public void setDeliver_to_name(String deliver_to_name) {
        this.deliver_to_name = deliver_to_name;
    }

    public String getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
    }

    public String getDelivery_status() {
        return delivery_status;
    }

    public void setDelivery_status(String delivery_status) {
        this.delivery_status = delivery_status;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getDelivery_start_time() {
        return delivery_start_time;
    }

    public void setDelivery_start_time(String delivery_start_time) {
        this.delivery_start_time = delivery_start_time;
    }

    public String getDelivery_end_time() {
        return delivery_end_time;
    }

    public void setDelivery_end_time(String delivery_end_time) {
        this.delivery_end_time = delivery_end_time;
    }

    public String getDelivery_to_mobile() {
        return delivery_to_mobile;
    }

    public void setDelivery_to_mobile(String delivery_to_mobile) {
        this.delivery_to_mobile = delivery_to_mobile;
    }

    public String getIs_delivery_Reject() {
        return Is_delivery_Reject;
    }

    public void setIs_delivery_Reject(String is_delivery_Reject) {
        Is_delivery_Reject = is_delivery_Reject;
    }

    public String getRejected_Reason() {
        return Rejected_Reason;
    }

    public void setRejected_Reason(String rejected_Reason) {
        Rejected_Reason = rejected_Reason;
    }

    public String getReceivedBy() {
        return ReceivedBy;
    }

    public void setReceivedBy(String receivedBy) {
        ReceivedBy = receivedBy;
    }

    public String getPod_lat() {
        return pod_lat;
    }

    public void setPod_lat(String pod_lat) {
        this.pod_lat = pod_lat;
    }

    public String getPod_lng() {
        return pod_lng;
    }

    public void setPod_lng(String pod_lng) {
        this.pod_lng = pod_lng;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDelivery_description() {
        return delivery_description;
    }

    public void setDelivery_description(String delivery_description) {
        this.delivery_description = delivery_description;
    }

    @Override
    public String toString() {
        return deliver_to_name;
    }
}
