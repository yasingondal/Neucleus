package detrack.zaryansgroup.com.detrack.activity.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import timber.log.Timber;

/**
 * Created by 6520 on 2/6/2016.
 */
public class SharedPrefs {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    public SharedPrefs(Context context) {
        prefs = context.getSharedPreferences("ZEDDeliveryPrefs", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void setSalesMode(String salesmodee) {
        editor.putString("salesmode", salesmodee);
        editor.commit();
    }

    public String GetSalesMode() {
        return prefs.getString("salesmode", "");
    }


    public void setCurrency(String currency) {
        editor.putString("currency", currency);
        editor.commit();
    }

    //For Foc
    public void SetIsFOCRequired(String IsFOCRequired) {
        editor.putString("focstatus", IsFOCRequired);
        editor.commit();
    }

    public String GetIsFOCRequired() {
        return prefs.getString("focstatus", "");
    }


    public void setGSTScheme(String string) {
        editor.putString("GSTScheme", string);
        editor.commit();
    }

    public String getGSTScheme() {
        return prefs.getString("GSTScheme", "");
    }

    public void setWholeSalePriceTax(String string) {
        editor.putString("WholeSalePriceTax", string);
        editor.commit();
    }

    public String getWholeSalePriceTax() {
        return prefs.getString("WholeSalePriceTax", "");
    }

    public void setCostPriceTax(String string) {
        editor.putString("CostPriceTax", string);
        editor.commit();
    }

    public String getCostPriceTax() {
        return prefs.getString("CostPriceTax", "");
    }

    public void setRetailPriceTax(String string) {
        editor.putString("RetailPriceTax", string);
        editor.commit();
    }

    public String getRetailPriceTax() {
        return prefs.getString("RetailPriceTax", "");
    }

    public void setSalesDiscPolicy(String string) {
        editor.putString("SalesDiscPolicy", string);
        editor.commit();
    }

    public String getSalesDiscPolicy() {
        return prefs.getString("SalesDiscPolicy", "");
    }

    public String getCurrency() {
        return prefs.getString("currency", "PKR");
    }


    public void setDesignation(String designation) {
        editor.putString("designation", designation);
        editor.commit();
    }

    public String getDesignation() {
        return prefs.getString("designation", "");
    }

    public void setCompanyID(String number) {
        editor.putString("companyId", number);
        editor.commit();
    }

    public void setView(String view) {
        editor.putString("view", view);
        editor.commit();
    }

    public String getView() {

        return prefs.getString("view", "");
    }



    //For Controlling Discount/Gst Visibility control in Total Bill Activity
    public void setIsDiscountVisible(String value) {
        editor.putString("IsDiscountVisible", value);
        editor.commit();
    }

    public String getIsDiscountVisible() {
        return prefs.getString("IsDiscountVisible", "false");
    }


    public void setIsGSTVisible(String value) {
        editor.putString("IsGSTVisible", value);
        editor.commit();
    }

    public String getIsGSTVisible() {
        return prefs.getString("IsGSTVisible", "false");
    }



    public void setVehicleName(String vehicleName) {
        editor.putString("vehicleName", vehicleName);
        editor.commit();
    }

    public String getVehicleName() {

        return prefs.getString("vehicleName", "");
    }

    public String getCompanyID() {
        return prefs.getString("companyId", null);
    }

    public void setCompanySiteID(String number) {
        editor.putString("companySiteId", number);
        editor.commit();
    }


    public void SetUserId(int id) {
        editor.putInt("UserId", id);
        editor.commit();
    }

    public int getUserId() {
        return prefs.getInt("UserId",0);
    }



    public String getCompanySiteID() {
        return prefs.getString("companySiteId", null);
    }

    public void setNotificationId(int number) {
        editor.putInt("notificationID", number);
        editor.commit();
    }

    public int GetNotificationId() {
        return prefs.getInt("notificationID", 0);
    }

    public void setVehicleID(int id) {
        editor.putInt("vehicleID", id);
        editor.commit();
    }

    public int getVehicleID() {
        return prefs.getInt("vehicleID", 2);
    }

    public void setEmployeeID(int id) {
        editor.putInt("empId", id);
        editor.commit();
    }

    public int getEmployeeID() {
        return prefs.getInt("empId", 0);
    }

    public void setUserName(String name) {
        editor.putString("userName", name);
        editor.commit();
    }

    public String getUserName() {
        return prefs.getString("userName", null);
    }

    public void setEmployeeName(String name) {
        editor.putString("EmpName", name);
        editor.commit();
    }

    public String getEmployeeName() {
        return prefs.getString("EmpName", "");
    }

    public void setCompanyName(String name) {
        editor.putString("CompanyName", name);
        editor.commit();
    }

    public String getCompanyName() {
        return prefs.getString("CompanyName", null);
    }

    public void setComapnyLogo(String name) {
        editor.putString("Companylogo", name);
        editor.commit();
    }

    public String getCompanyLogo() {
        return prefs.getString("Companylogo", null);
    }

    public void setUserPassword(String name) {
        editor.putString("userPassword", name);
        editor.commit();
    }

    public String getUserPassword() {
        return prefs.getString("userPassword", null);
    }

    public void setIsStayLogin(boolean login) {
        editor.putBoolean("islogin", login);
        editor.commit();
    }

    public boolean IsStayLogin() {
        return prefs.getBoolean("islogin", false);
    }

    public void ClearPrefs() {
        prefs.edit().clear().apply();
        Timber.d("All Preferences has been cleared");
    }

    public void hSetLogout(boolean hisLogout) {
        editor.putBoolean("hIsLogout", hisLogout);
        editor.apply();
    }


    public boolean hGetLogOut() {
        return prefs.getBoolean("hIsLogout", false);
    }
}
