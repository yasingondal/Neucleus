package detrack.zaryansgroup.com.detrack.activity.Model.BankModels;


public class CompanyWiseBanksModel {

  private   int BankID;
  private   String BankName;
  private   String BankAccountNbr;
  private   String BankAccountType;
  private   String Address;

    public CompanyWiseBanksModel() {

    }

    public CompanyWiseBanksModel(int bankID, String bankName, String bankAccountNbr, String bankAccountType, String address) {
        BankID = bankID;
        BankName = bankName;
        BankAccountNbr = bankAccountNbr;
        BankAccountType = bankAccountType;
        Address = address;
    }

    public int getBankID() {
        return BankID;
    }

    public void setBankID(int bankID) {
        BankID = bankID;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }

    public String getBankAccountNbr() {
        return BankAccountNbr;
    }

    public void setBankAccountNbr(String bankAccountNbr) {
        BankAccountNbr = bankAccountNbr;
    }

    public String getBankAccountType() {
        return BankAccountType;
    }

    public void setBankAccountType(String bankAccountType) {
        BankAccountType = bankAccountType;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }


}
