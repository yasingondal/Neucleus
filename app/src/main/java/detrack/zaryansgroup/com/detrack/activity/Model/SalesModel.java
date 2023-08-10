package detrack.zaryansgroup.com.detrack.activity.Model;

public class SalesModel {

    String salesDate, salesDescription, salesMode;
    int salesInvNo;
    int salesAmount;
    int salesQty;
    int salesCredit;
    int salesCash;

    public int getSalesCredit() {
        return salesCredit;
    }

    public void setSalesCredit(int salesCredit) {
        this.salesCredit = salesCredit;
    }

    public int getSalesCash() {
        return salesCash;
    }

    public void setSalesCash(int salesCash) {
        this.salesCash = salesCash;
    }

    public String getSalesDate() {
        return salesDate;
    }

    public void setSalesDate(String salesDate) {
        this.salesDate = salesDate;
    }

    public String getSalesDescription() {
        return salesDescription;
    }

    public void setSalesDescription(String salesDescription) {
        this.salesDescription = salesDescription;
    }

    public String getSalesMode() {
        return salesMode;
    }

    public void setSalesMode(String salesMode) {
        this.salesMode = salesMode;
    }

    public int getSalesInvNo() {
        return salesInvNo;
    }

    public void setSalesInvNo(int salesInvNo) {
        this.salesInvNo = salesInvNo;
    }

    public int getSalesAmount() {
        return salesAmount;
    }

    public void setSalesAmount(int salesAmount) {
        this.salesAmount = salesAmount;
    }

    public int getSalesQty() {
        return salesQty;
    }

    public void setSalesQty(int salesQty) {
        this.salesQty = salesQty;
    }
}
