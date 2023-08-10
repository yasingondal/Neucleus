package detrack.zaryansgroup.com.detrack.activity.Model;

/**
 * Created by hussainjawad on 12/1/2016.
 */
public class DailySummeryModel {
    private int orderId;
    private String CustomerName;
    private int ItemQty;
    private String custId;
    private float grossTotal;
    private String receipt;
    private float Amount;
    private float Cash;
    private float Credit;
    private String disc;
    private String orderStatus;
    private String customerSalesMode;

    public String getCustomerSalesMode() {
        return customerSalesMode;
    }

    public void setCustomerSalesMode(String customerSalesMode) {
        this.customerSalesMode = customerSalesMode;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }


    public float getGrossTotal() {
        return grossTotal;
    }

    public void setGrossTotal(float grossTotal) {
        this.grossTotal = grossTotal;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public int getItemQty() {
        return ItemQty;
    }

    public void setItemQty(int itemQty) {
        ItemQty = itemQty;
    }

    public float getAmount() {
        return Amount;
    }

    public void setAmount(float amount) {
        Amount = amount;
    }

    public float getCash() {
        return Cash;
    }

    public void setCash(float cash) {
        Cash = cash;
    }

    public float getCredit() {
        return Credit;
    }

    public void setCredit(float credit) {
        Credit = credit;
    }


    public String getDisc() {
        return disc;
    }

    public void setDisc(String disc) {
        this.disc = disc;
    }

    @Override
    public String toString() {
        return CustomerName;
    }
}
