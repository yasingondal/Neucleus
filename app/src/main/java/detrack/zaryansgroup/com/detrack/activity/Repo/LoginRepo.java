package detrack.zaryansgroup.com.detrack.activity.Repo;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.HashMap;


import detrack.zaryansgroup.com.detrack.activity.Model.CompanyDetailsModel.CompanyDetailsListModel;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.CompanyItemListModel;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyRouteModel.CompanyRouteListModel;
import detrack.zaryansgroup.com.detrack.activity.Model.CustomerPriceModel.CustomerPriceListModel;
import detrack.zaryansgroup.com.detrack.activity.Model.LoginModel.LoginListModel;
import detrack.zaryansgroup.com.detrack.activity.Model.RegisterCustomerModel.RegisterdCustomerListModel;
import detrack.zaryansgroup.com.detrack.activity.Model.VehiclesModel.VehicleListModel;
import detrack.zaryansgroup.com.detrack.activity.retrofit.Api_Reto;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;


public class LoginRepo {

    private static LoginRepo instance;
    public MutableLiveData<LoginListModel> login_livedata = new MutableLiveData<>();
    public MutableLiveData<RegisterdCustomerListModel> customer_livedata = new MutableLiveData<>();
    public MutableLiveData<CompanyRouteListModel> company_Routes = new MutableLiveData<>();
    public MutableLiveData<CompanyDetailsListModel> company_details = new MutableLiveData<>();
    public MutableLiveData<CompanyItemListModel> company_Items = new MutableLiveData<>();
    public MutableLiveData<VehicleListModel> company_vehicles = new MutableLiveData<>();
    public MutableLiveData<CustomerPriceListModel> customerPriceList = new MutableLiveData<>();



    public static LoginRepo getInstance(){
        if(instance ==  null) {
            instance = new LoginRepo();
        }
        Log.d("checkInstance","not null");
        return instance;
    }



    public void ReqLogin(String username , String pass){

        HashMap<String ,String> hashMap=new HashMap<String ,String>();
        hashMap.put("UserName",username);
        hashMap.put("Password",pass);

        Api_Reto.getRetrofit().getRetrofit_services().isValidUser(hashMap).enqueue(new Callback<LoginListModel>() {
            @Override
            public void onResponse(Call<LoginListModel> call,
                                   Response<LoginListModel> response) {
                login_livedata.postValue(response.body());
                Timber.d("Login Response on basis of Credentials is positive");
            }

            @Override
            public void onFailure(Call<LoginListModel> call, Throwable t) {
                Log.d("loginresponce",t.toString());
                login_livedata.postValue(null);
            }
        });

    }

    public void reqCustomerInfo(String companyid, String companysiteid,int UserId){
        Api_Reto.getRetrofit().getRetrofit_services().getRegisterCustomerInfo(companyid,companysiteid,UserId)
                .enqueue(new Callback<RegisterdCustomerListModel>() {
                    @Override
                    public void onResponse(Call<RegisterdCustomerListModel> call, Response<RegisterdCustomerListModel> response) {
                        customer_livedata.postValue(response.body());
                        Timber.d("customers response: "+response.toString());
                    }

                    @Override
                    public void onFailure(Call<RegisterdCustomerListModel> call, Throwable t) {

                    }
                });

    } //Operating System is The way we can change one module to another

    public void reqCompanyRoutes(String companyid, String companysiteid,int UserId){
        Api_Reto.getRetrofit().getRetrofit_services().getCompanyRoutes(companyid,companysiteid,UserId)
                .enqueue(new Callback<CompanyRouteListModel>() {
                    @Override
                    public void onResponse(Call<CompanyRouteListModel> call, Response<CompanyRouteListModel> response) {
                        company_Routes.postValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<CompanyRouteListModel> call, Throwable t) {
                        Timber.d(t.getMessage()+"");
                    }
                });

    }

    public void reqCompanyDetails(String companyid){

        Api_Reto.getRetrofit().getRetrofit_services().getCompanyDetails(companyid)
                .enqueue(new Callback<CompanyDetailsListModel>() {
                    @Override
                    public void onResponse(Call<CompanyDetailsListModel> call, Response<CompanyDetailsListModel> response) {
                        company_details.postValue(response.body());
                        Timber.d("Company Vehicles +ve onResponse");
                    }

                    @Override
                    public void onFailure(Call<CompanyDetailsListModel> call, Throwable t) {

                    }
                });
    }

    public void reqComapnyItems(String companyid, String  companySid){

        Timber.d("new reqComapnyItems url is "+ Utility.BASE_LIVE_URL+"api/Company/GetCompanyItems?Company_id="+companyid+"&companysiteID"+companySid);

        Api_Reto.getRetrofit().getRetrofit_services().getCompanyItems(companyid, companySid)
                .enqueue(new Callback<CompanyItemListModel>() {
                    @Override
                    public void onResponse(Call<CompanyItemListModel> call, Response<CompanyItemListModel> response) {
                        company_Items.postValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<CompanyItemListModel> call, Throwable t) {

                    }
                });
    }

    public void reqComapnyVehicle(String cid, String csid){
        Api_Reto.getRetrofit().getRetrofit_services().getCompanyVehicles(cid,csid)
                .enqueue(new Callback<VehicleListModel>() {
                    @Override
                    public void onResponse(Call<VehicleListModel> call, Response<VehicleListModel> response) {
                        company_vehicles.postValue(response.body());

                    }

                    @Override
                    public void onFailure(Call<VehicleListModel> call, Throwable t) {
                        company_vehicles.postValue(null);
                    }
                });
    }

    public void reqCustomerPriceList(String companyID, String companySiteID){
        Timber.d("reqCustomerPriceList");
        Api_Reto.getRetrofit().getRetrofit_services().getCustomerPrice(companyID,companySiteID)
                .enqueue(new Callback<CustomerPriceListModel>() {
                    @Override
                    public void onResponse(Call<CustomerPriceListModel> call, Response<CustomerPriceListModel> response) {

                        try {
                            customerPriceList.postValue(response.body());
                        }
                        catch (Exception e)
                        {
                            Timber.d("Register Activity false exception");
                        }

                    }

                    @Override
                    public void onFailure(Call<CustomerPriceListModel> call, Throwable t) {


                    }
                });
    }


    public LiveData<LoginListModel> getLogindata() {
        return  login_livedata;
    }

    public LiveData<RegisterdCustomerListModel> getRegisteredCustomerInfo() {
        return  customer_livedata;
    }

    public LiveData<CompanyRouteListModel> getCompanyRoutes() {
        return  company_Routes;
    }

    public LiveData<CompanyDetailsListModel> getCompanyDetails() {
        return  company_details;
    }

    public LiveData<CompanyItemListModel> getCompanhyItems() {
        return  company_Items;
    }

    public LiveData<VehicleListModel> getCompanhyVehicles() {
        return  company_vehicles;
    }


    public LiveData<CustomerPriceListModel> getCustomerPriceList() {
        return customerPriceList;
    }
}




