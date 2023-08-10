package detrack.zaryansgroup.com.detrack.activity.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

import detrack.zaryansgroup.com.detrack.activity.Model.CompanyRouteModel.RouteModel;
import detrack.zaryansgroup.com.detrack.activity.Model.DistributorModel.DistributorModel;
import detrack.zaryansgroup.com.detrack.activity.Model.RegisterCustomerModel.RegisterdCustomerModel;
import detrack.zaryansgroup.com.detrack.activity.Repo.MainRepo;

public class SelectCustomer_ViewModel extends AndroidViewModel {

    MainRepo mainRepo;
    private LiveData<ArrayList<RegisterdCustomerModel>> mCustomeInfo;
    private LiveData<ArrayList<RouteModel>> mRoutelist;
    private LiveData<ArrayList<DistributorModel>> mDistributorsList;


    public SelectCustomer_ViewModel(@NonNull Application application) {
        super(application);
//
//        if(mainRepo != null){
//            return;
//        }
        Log.d("checkData","null");
        mainRepo= new MainRepo(application);
        mCustomeInfo = mainRepo.getSqlRegisterCustomerInfo();
        mRoutelist = mainRepo.getSqlroutelist();
        mDistributorsList = mainRepo.getDistributorsList();

    }



    public void getSqlCustomerInfo(String routeid){
        mainRepo.getSqlRegisterCustomerInfo(routeid);
    }

    public ArrayList<RegisterdCustomerModel> getSqlCustomerInfo1(String routeid){
        List<RegisterdCustomerModel> a = new ArrayList<>();
        a = mainRepo.getSqlRegisterCustomerInfo1(routeid);
        return (ArrayList<RegisterdCustomerModel>) a;
    }

    public void getSqlRouteList(){
        mainRepo.getSqlRouteList();
    }
    public void getSqlDistributorsList(){
        mainRepo.getSqlDistributorsList();
    }


    public LiveData<ArrayList<RegisterdCustomerModel>> getSqlCustomerinfo() {
        return mCustomeInfo;
    }
    public LiveData<ArrayList<RouteModel>> getSqlroutelist() {
        return mRoutelist;
    }

    public LiveData<ArrayList<DistributorModel>> getDistributerlist() {
        return mDistributorsList;
    }









}
