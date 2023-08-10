package detrack.zaryansgroup.com.detrack.activity.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;

import detrack.zaryansgroup.com.detrack.activity.Model.BankModels.CompanyWiseBanksModel;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyRouteModel.RouteModel;
import detrack.zaryansgroup.com.detrack.activity.Model.ReceiptModel;
import detrack.zaryansgroup.com.detrack.activity.Model.RegisterCustomerModel.RegisterdCustomerModel;
import detrack.zaryansgroup.com.detrack.activity.Repo.MainRepo;

public class TakeNewReceipt_ViewModel extends AndroidViewModel {


    private LiveData<ArrayList<RegisterdCustomerModel>> mCustomeData;
    private LiveData<ArrayList<RouteModel>> mRoutelist;
    private LiveData<Boolean> mInsertedCheck;

    //For Banks Spinners
    private LiveData<ArrayList<CompanyWiseBanksModel>> VmCompanyWiseBankList;

    MainRepo mainRepo;
    public TakeNewReceipt_ViewModel(@NonNull Application application) {
        super(application);
        mainRepo = new MainRepo(application);
        mRoutelist = mainRepo.getSqlroutelist();
        mCustomeData = mainRepo.getSqlRegisterCustomerInfo();
        mInsertedCheck = mainRepo.checkIfInserted();

        //For banks
        VmCompanyWiseBankList = mainRepo.GetSqlBankDetails();

    }


    public void getSqlCustomerData(String routeid){
        mainRepo.getSqlRegisterCustomerInfo(routeid);
    }
    public void getSqlRouteList(){
        mainRepo.getSqlRouteList();
    }
    public void insertCustomerReceipt(ReceiptModel model){
        mainRepo.insertCustomerReciept(model);
    }

    public LiveData<ArrayList<RegisterdCustomerModel>> getSqlCustomerData() {
        return mCustomeData;
    }

    public LiveData<ArrayList<RouteModel>> getSqlroutelist() {
        return mRoutelist;
    }

    public LiveData<Boolean> checkInserted() {
        return mInsertedCheck;
    }

    //Bank Spinner work
    public void getSqlBanksList(){
        mainRepo.getSqlBankDetails();
    }

    public LiveData<ArrayList<CompanyWiseBanksModel>> getSqlBankslist() {
        return VmCompanyWiseBankList;
    }



}
