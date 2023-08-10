package detrack.zaryansgroup.com.detrack.activity.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;

import detrack.zaryansgroup.com.detrack.activity.Model.BankModels.CompanyWiseBanksModel;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.DeliveryItemModel;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyRouteModel.RouteModel;
import detrack.zaryansgroup.com.detrack.activity.Model.RegisterCustomerModel.RegisterdCustomerModel;
import detrack.zaryansgroup.com.detrack.activity.Repo.MainRepo;

public class TakeOrder_ViewModel extends AndroidViewModel {

    MainRepo mainRepo;
    private LiveData<ArrayList<RegisterdCustomerModel>> mCustomeData;
    private LiveData<RegisterdCustomerModel> mSelectedCustomer;
    private LiveData<ArrayList<DeliveryItemModel> > mSelectedItem;
    private LiveData<ArrayList<DeliveryItemModel>> mDeleiveryitems;
    private LiveData<ArrayList<RouteModel>> mRoutelist;


    //Duplication assignment work bank by yaseen
    private LiveData<ArrayList<CompanyWiseBanksModel>> VmCompanyWiseBankList;

    public TakeOrder_ViewModel(@NonNull Application application) {
        super(application);
        mainRepo = new MainRepo(application);
        mCustomeData = mainRepo.getSqlRegisterCustomerInfo();
        mSelectedCustomer = mainRepo.getSqlSelectedCustomer();
        mSelectedItem = mainRepo.getSqlSelectedItem();
        mDeleiveryitems = mainRepo.getSqlDeliveryItems();
        mRoutelist = mainRepo.getSqlroutelist();

        //Assignment work M.Y
//        mOfflineBanksList = mainRepo.MR_getSqlBankDetailsList();

        //Assignment work by yaseen duplication
        // returning
        VmCompanyWiseBankList = mainRepo.GetSqlBankDetails();

    }



    public void loadSqlSelectedCustomer(int cid){
        mainRepo.loadSelectedCustomer(cid);
    }

    public void getSqlCustomerData(String routeid){
        mainRepo.getSqlRegisterCustomerInfo(routeid);
    }

    public void loadSqlSelectedItem(String id, String isnew){
        mainRepo.loadSelectedItem(id,isnew,false);
    }
    public void loadSqlDeliveryItems(){
        mainRepo.loadDeliveryItems();
    }
    public void getSqlRouteList(){
        mainRepo.getSqlRouteList();
    }

    public void insertRunTimeOrderDetails(ArrayList<DeliveryItemModel> list, RegisterdCustomerModel RS_model){
        mainRepo.insertRunTimeOrderDetails(list,RS_model);
    }
    public boolean deleteOrder(String delivery_id, String IsNew){
       return mainRepo.deleteOrder(delivery_id, IsNew);
    }

    public LiveData<ArrayList<RegisterdCustomerModel>> getSqlCustomerData() {
        return mCustomeData;
    }
    public LiveData<RegisterdCustomerModel> getSelectedCustomer(){
        return mSelectedCustomer;
    }
    public LiveData<ArrayList<DeliveryItemModel>> getSelctedItem(){
        return mSelectedItem;
    }
    public LiveData<ArrayList<DeliveryItemModel>> getDeliveryItems(){
        return mDeleiveryitems;
    }
    public LiveData<ArrayList<RouteModel>> getSqlroutelist() {
        return mRoutelist;
    }



    //Bank Spinner work
    public void getSqlBanksList(){
        mainRepo.getSqlBankDetails();
    }

    public LiveData<ArrayList<CompanyWiseBanksModel>> getSqlBankslist() {
        return VmCompanyWiseBankList;
    }

}
