package detrack.zaryansgroup.com.detrack.activity.Model.CompanyDetailsModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompanyDetailsModel {

    @SerializedName("Company_Id")
    @Expose
    private String company_id;
    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("Company")
    @Expose
    private String company;
    @Expose
    @SerializedName("CompanyTitle")
    private String companyTitle;
    @SerializedName("Logo")
    @Expose
    private String logo;

    @SerializedName("Currency")
    @Expose
    private String currency;

    @SerializedName("Layout")
    @Expose
    private String Layout;

    @SerializedName("GSTScheme")
    @Expose
    private String GSTScheme;
    @SerializedName("WholeSalePriceTax")
    @Expose
    private String WholeSalePriceTax;
    @SerializedName("CostPriceTax")
    @Expose
    private String CostPriceTax;
    @SerializedName("RetailPriceTax")
    @Expose
    private String RetailPriceTax;
    @SerializedName("SalesDiscPolicy")
    @Expose
    private String SalesDiscPolicy;


    @SerializedName("IsDiscountVisible")
    @Expose
    private String IsDiscountVisible;

    @SerializedName("IsGSTVisible")
    @Expose
    private String IsGSTVisible;


    public String getIsDiscountVisible() {
        return IsDiscountVisible;
    }

    public void setIsDiscountVisible(String isDiscountVisible) {
        IsDiscountVisible = isDiscountVisible;
    }

    public String getIsGSTVisible() {
        return IsGSTVisible;
    }

    public void setIsGSTVisible(String isGSTVisible) {
        IsGSTVisible = isGSTVisible;
    }

    public String getGSTScheme() {
        return GSTScheme;
    }

    public void setGSTScheme(String GSTScheme) {
        this.GSTScheme = GSTScheme;
    }

    public String getWholeSalePriceTax() {
        return WholeSalePriceTax;
    }

    public void setWholeSalePriceTax(String wholeSalePriceTax) {
        WholeSalePriceTax = wholeSalePriceTax;
    }

    public String getCostPriceTax() {
        return CostPriceTax;
    }

    public void setCostPriceTax(String costPriceTax) {
        CostPriceTax = costPriceTax;
    }

    public String getRetailPriceTax() {
        return RetailPriceTax;
    }

    public void setRetailPriceTax(String retailPriceTax) {
        RetailPriceTax = retailPriceTax;
    }

    public String getSalesDiscPolicy() {
        return SalesDiscPolicy;
    }

    public void setSalesDiscPolicy(String salesDiscPolicy) {
        SalesDiscPolicy = salesDiscPolicy;
    }

    public String getLayout() {
        return Layout;
    }

    public void setLayout(String layout) {
        Layout = layout;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompanyTitle() {
        return companyTitle;
    }

    public void setCompanyTitle(String companyTitle) {
        this.companyTitle = companyTitle;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
