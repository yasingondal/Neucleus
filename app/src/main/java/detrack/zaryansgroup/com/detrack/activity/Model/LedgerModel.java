package detrack.zaryansgroup.com.detrack.activity.Model;

public class LedgerModel {

    int No;
    String date;
    String description;
    Double debit;
    Double credit;
    Double balance;

    public int getNo() {
        return No;
    }

    public void setNo(int no) {
        No = no;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getDebit() {
        return debit;
    }

    public void setDebit(Double debit) {
        this.debit = debit;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

}
