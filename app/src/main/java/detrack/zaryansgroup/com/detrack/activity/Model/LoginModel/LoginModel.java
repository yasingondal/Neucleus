package detrack.zaryansgroup.com.detrack.activity.Model.LoginModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginModel {


    @SerializedName("CompanySiteID")
    @Expose
    private String companySiteID;
    @SerializedName("CompanyID")
    @Expose
    private String  companyID;
    @SerializedName("Username")
    @Expose
    private String username;
    @SerializedName("Password")
    @Expose
    private String password;
    @SerializedName("FName")
    @Expose
    private String fName;
    @SerializedName("LName")
    @Expose
    private String lName;
    @SerializedName("EmployeeID")
    @Expose
    private int employeeID;
    @SerializedName("EmployeeName")
    @Expose
    private String  employeeName;
    @SerializedName("UserId")
    @Expose
    private Integer userId;
    @SerializedName("ContactType")
    @Expose
    private String  contactType;

    @SerializedName("UserRole")
    @Expose
    private String  UserRole;

    @SerializedName("IsFOCRequired")
    @Expose
    private String  IsFOCRequired;


    public String getUserRole() {
        return UserRole;
    }

    public void setUserRole(String userRole) {
        UserRole = userRole;
    }

    public String getIsFOCRequired() {
        return IsFOCRequired;
    }

    public void setIsFOCRequired(String isFOCRequired) {
        IsFOCRequired = isFOCRequired;
    }

    public String getCompanySiteID() {
        return companySiteID;
    }

    public void setCompanySiteID(String companySiteID) {
        this.companySiteID = companySiteID;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }
}