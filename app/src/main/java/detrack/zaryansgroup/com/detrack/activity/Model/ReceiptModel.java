package detrack.zaryansgroup.com.detrack.activity.Model;

/**
 * Created by hussainjawad on 12/10/2016.
 */
public class ReceiptModel {
    int id,serverID,customerID,CashDepositedBankId;

    float amountPaid,balance,previousBalnc;
    String date,remarks,customerName;

    public String getCashDepositedBankName() {
        return CashDepositedBankName;
    }

    public void setCashDepositedBankName(String cashDepositedBankName) {
        CashDepositedBankName = cashDepositedBankName;
    }

    String CashDepositedBankName;

    boolean isCBCheck;

    public int getCashDepositedBankId() {
        return CashDepositedBankId;
    }

    public void setCashDepositedBankId(int cashDepositedBankId) {
        CashDepositedBankId = cashDepositedBankId;
    }

    public boolean isCBCheck() {
        return isCBCheck;
    }

    public void setIsCBCheck(boolean isCBCheck) {
        this.isCBCheck = isCBCheck;
    }

    public float getPreviousBalnc() {
        return previousBalnc;
    }

    public void setPreviousBalnc(float previousBalnc) {
        this.previousBalnc = previousBalnc;
    }

    public int getIsSync() {
        return IsSync;
    }

    public void setIsSync(int isSync) {
        IsSync = isSync;
    }

    int IsSync;



    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getServerID() {
        return serverID;
    }

    public void setServerID(int serverID) {
        this.serverID = serverID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public float getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(float amountPaid) {
        this.amountPaid = amountPaid;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return customerName+", "+amountPaid+", "+previousBalnc;
    }
}
