package detrack.zaryansgroup.com.detrack.activity.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import detrack.zaryansgroup.com.detrack.activity.Model.CompanyDetailsModel.CompanyDetailsListModel;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.CompanyItemListModel;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyRouteModel.CompanyRouteListModel;
import detrack.zaryansgroup.com.detrack.activity.Model.CustomerPriceModel.CustomerPriceListModel;
import detrack.zaryansgroup.com.detrack.activity.Model.LoginModel.LoginListModel;
import detrack.zaryansgroup.com.detrack.activity.Model.RegisterCustomerModel.RegisterdCustomerListModel;
import detrack.zaryansgroup.com.detrack.activity.Model.VehiclesModel.VehicleListModel;
import detrack.zaryansgroup.com.detrack.activity.Repo.LoginRepo;


public class Response_Login_VM extends ViewModel {

    private LiveData<LoginListModel> mLogindata;
    private LiveData<RegisterdCustomerListModel> mCustomerInfo;
    private LiveData<CompanyRouteListModel> mCopanyRoutes;
    private LiveData<CompanyDetailsListModel> mCompanyDetails;
    private LiveData<CompanyItemListModel> mCompanyItems;
    private LiveData<VehicleListModel> mCompanyVehicle;
    private LiveData<CustomerPriceListModel> mCustomerPriceList;
    private LoginRepo mainRepo;
    private MutableLiveData<Boolean> misUpdating = new MutableLiveData<>();

    //here i will need to add LiveData ArrayList for getting all the banks

    public void init(){
        if(mLogindata != null){
            return;
        }
        Log.d("checkData","null");
        mainRepo= LoginRepo.getInstance();
        mLogindata = mainRepo.getLogindata();
        mCustomerInfo = mainRepo.getRegisteredCustomerInfo();
        mCopanyRoutes = mainRepo.getCompanyRoutes();
        mCompanyDetails = mainRepo.getCompanyDetails();
        mCompanyItems = mainRepo.getCompanhyItems();
        mCompanyVehicle = mainRepo.getCompanhyVehicles();
        mCustomerPriceList = mainRepo.getCustomerPriceList();

//        mRegisterCustomerInfo = mainRepo.getRegisterCustomerInfoData();

    }


    public void ReqLogin(String username , String pass){
        mainRepo.ReqLogin(username,pass);
    }

    public void ReqRegisterCustomerInfo(String companyId , String companySite, int UserId){
        mainRepo.reqCustomerInfo(companyId,companySite,UserId);
    }

    public void ReqCompanyRoutes(String companyId , String companySite,int UserId){
        mainRepo.reqCompanyRoutes(companyId,companySite,UserId);
    }

    public void ReqCompanyDetails(String companyId){
        mainRepo.reqCompanyDetails(companyId);
    }

    public void ReqCompanyItems(String companyId, String  companySid){
        mainRepo.reqComapnyItems(companyId,companySid);
    }


    public void ReqCompanyVehicles(String cid, String csid){
        mainRepo.reqComapnyVehicle(cid,csid);
    }
    public void ReqCustomerPriceList(String companyID, String companySiteID){
        mainRepo.reqCustomerPriceList(companyID,companySiteID);
    }


    public LiveData<LoginListModel> getLogindata() {
        return mLogindata;
    }

    public LiveData<CustomerPriceListModel> getCustomerPriceList() {
        return mCustomerPriceList;
    }

    public LiveData<RegisterdCustomerListModel> getRegisteredCustomerInfo() {
        return mCustomerInfo;
    }
    public LiveData<CompanyRouteListModel> getCompanyRoute() {
        return mCopanyRoutes;
    }

    public LiveData<CompanyDetailsListModel> getCompanyDetails() {
        return mCompanyDetails;
    }

    public LiveData<CompanyItemListModel> getCompanyItems() {
        return mCompanyItems;
    }

    public LiveData<VehicleListModel> getCompanyVehicles() {
        return mCompanyVehicle;
    }

    public LiveData<Boolean> getisupdating(){
        return misUpdating;
    }


}
