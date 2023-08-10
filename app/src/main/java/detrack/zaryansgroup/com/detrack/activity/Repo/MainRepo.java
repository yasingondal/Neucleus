package detrack.zaryansgroup.com.detrack.activity.Repo;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import detrack.zaryansgroup.com.detrack.activity.Model.BankModels.CompanyWiseBanksModel;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.DeliveryItemModel;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyRouteModel.RouteModel;
import detrack.zaryansgroup.com.detrack.activity.Model.DailySummeryModel;
import detrack.zaryansgroup.com.detrack.activity.Model.DeliveryInfo;
import detrack.zaryansgroup.com.detrack.activity.Model.DistributorModel.DistributorModel;
import detrack.zaryansgroup.com.detrack.activity.Model.ReceiptModel;
import detrack.zaryansgroup.com.detrack.activity.Model.RegisterCustomerModel.RegisterdCustomerListModel;
import detrack.zaryansgroup.com.detrack.activity.Model.RegisterCustomerModel.RegisterdCustomerModel;
import detrack.zaryansgroup.com.detrack.activity.Model.WelcomeModel.WelcomeDataModel;
import detrack.zaryansgroup.com.detrack.activity.retrofit.Api_Reto;
import detrack.zaryansgroup.com.detrack.activity.SQLlite.ZEDTrackDB;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MainRepo {

    private final ZEDTrackDB db;

    public MutableLiveData<ArrayList<RegisterdCustomerModel>> mCustomerdata = new MutableLiveData<>();
    public MutableLiveData<WelcomeDataModel> mwelcomeData = new MutableLiveData<>();
    public MutableLiveData<ArrayList<RouteModel>> mRoutelist = new MutableLiveData<>();
    public MutableLiveData<ArrayList<DistributorModel>> mDistributorslist = new MutableLiveData<>();

    //Bank Assignment work BY Yaseen
    public MutableLiveData<ArrayList<CompanyWiseBanksModel>> mCompanyWiseBankslist = new MutableLiveData<>();


    public MutableLiveData<ArrayList<DeliveryItemModel>> mDeliverylist = new MutableLiveData<>();
    public MutableLiveData<RegisterdCustomerModel> mSelectedCustomer = new MutableLiveData<>();
    public MutableLiveData<ArrayList<DeliveryItemModel>> mSelectedItemList = new MutableLiveData<>();
    public MutableLiveData<RegisterdCustomerListModel> mReturnedOrder = new MutableLiveData<>();
    public MutableLiveData<ArrayList<DailySummeryModel>> mDailySaleSummary = new MutableLiveData<>();
    public MutableLiveData<ArrayList<DailySummeryModel>> mReceiptList = new MutableLiveData<>();
    public MutableLiveData<Float> mTotalReceipt = new MutableLiveData<>();
    public MutableLiveData<Boolean> inserted = new MutableLiveData<>();
    public MutableLiveData<Boolean> mDeliverinfoStatus = new MutableLiveData<>();
    public MutableLiveData<DeliveryInfo> mdeliverInfo = new MutableLiveData<>();


    public MainRepo(Application application) {
        db = new ZEDTrackDB(application);
    }


    public void loadDailySaleSummary(String date, String des) {
        ArrayList<DailySummeryModel> mlist = db.getTodaySaleSummery(date, des);
        mDailySaleSummary.postValue(mlist);
    }

    public void loadReceiptList(String date) {
        ArrayList<DailySummeryModel> mlist = db.getReceiptList(date);
        Timber.d("POsting new value");
        mReceiptList.postValue(mlist);
    }

    public void loadTotalReceipt(String date) {
        Float aFloat = db.getTotalReceipt(date);
        mTotalReceipt.postValue(aFloat);
    }

    public void getSqlRegisterCustomerInfo(String routeid) {
        ArrayList<RegisterdCustomerModel> mlist = db.getSQLiteRegisterCustomerInfo(routeid);
        mCustomerdata.postValue(mlist);
    }

    public List<RegisterdCustomerModel> getSqlRegisterCustomerInfo1(String routeid) {
        ArrayList<RegisterdCustomerModel> mlist = db.getSQLiteRegisterCustomerInfo(routeid);
        mCustomerdata.postValue(mlist);
        return mlist;
    }


    public void getSqlRouteList() {
        ArrayList<RouteModel> mlist = db.getCompanyRoute();
        mRoutelist.postValue(mlist);
    }

    public void getSqlDistributorsList() {
        ArrayList<DistributorModel> distributorslist = db.getDistributors();
        mDistributorslist.postValue(distributorslist);
    }


    //Assignment work By YAseen
    public void getSqlBankDetails() {
        ArrayList<CompanyWiseBanksModel> sqlCompanywiseBanksList = db.GetCompanyWiseBankDetails();
        mCompanyWiseBankslist.postValue(sqlCompanywiseBanksList);
    }


    public void addNewSalesReturn(int d_id, String isNew) {
        addNewSalesReturnFunc(d_id, isNew);
    }

    private void addNewSalesReturnFunc(int d_id, String isNew) {
//        SendReturnOrder sendReturnOrder = new SendReturnOrder(this.getco,d_id);
//        sendReturnOrder.addNewSalesReturn(Utility.BASE_SALES_RETURN_URL+"api/SalesReturn/Create");
    }


    public void loadWelcomeData() {

        String vTempTodayDate = Utility.getCurrentDateForV();

        WelcomeDataModel welcomeDataModel = new WelcomeDataModel(db.NumberOfAssignOrder(), db.NumberOfBookedUnSyncOrder(""),
                db.NumberOfReceipt(""), db.NumberOfDeliveredUnSynecOrder(""), db.NumberOfReturnedOrders(""), db.NumberOfSyncOrder("")
                ,db.NumberOfVisitRecords(vTempTodayDate));
        mwelcomeData.postValue(welcomeDataModel);

    }

    public void loadDeliveryItems() {
        ArrayList<DeliveryItemModel> list = db.getCompanyItem();
        mDeliverylist.postValue(list);
    }


    public void loadSelectedCustomer(int cid) {
        RegisterdCustomerModel registerdCustomerModel = db.getCustomerById(cid);
        mSelectedCustomer.postValue(registerdCustomerModel);
    }

    public void loadSelectedItem(String id, String isNew, Boolean forclos) {
        ArrayList<DeliveryItemModel> mlist = db.getSQLiteOrderDeliveryItems(id, isNew, forclos);
        mSelectedItemList.postValue(mlist);
    }


    public void getSelectedSQLiteOrderDelivery(int did, String isNew) {

        DeliveryInfo deliveryInfo = db.getSelectedSQLiteOrderDelivery(did, isNew);
        mdeliverInfo.postValue(deliveryInfo);

    }

    public void reqReturnedOrders(HashMap<String, String> hashMap) {

        Api_Reto.getRetrofit().getRetrofit_services().getReturnedOrders(hashMap)
                .enqueue(new Callback<RegisterdCustomerListModel>() {
                    @Override
                    public void onResponse(Call<RegisterdCustomerListModel> call, Response<RegisterdCustomerListModel> response) {
                        mReturnedOrder.postValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<RegisterdCustomerListModel> call, Throwable t) {

                    }
                });

    }


    public void insertCustomerReciept(ReceiptModel model) {
        boolean check = db.insertCustomerReceipt(model);
        inserted.postValue(check);
    }

    public void insertRunTimeOrderDetails(ArrayList<DeliveryItemModel> list, RegisterdCustomerModel RS_model) {
        db.insertRunTimeOrderDetails(list, RS_model,"false");
    }

    public boolean deleteOrder(String delivery_id, String IsNew) {
        return db.deleteOrder(delivery_id, IsNew);
    }

    public LiveData<ArrayList<RegisterdCustomerModel>> getSqlRegisterCustomerInfo() {
        return mCustomerdata;
    }

    public LiveData<WelcomeDataModel> getWelcomeData() {
        return mwelcomeData;
    }

    public LiveData<ArrayList<RouteModel>> getSqlroutelist() {
        return mRoutelist;
    }

    public LiveData<ArrayList<DistributorModel>> getDistributorsList() {
        return mDistributorslist;
    }

    //Assignment Bank work By Yaseen
    public LiveData<ArrayList<CompanyWiseBanksModel>> GetSqlBankDetails() {
        return mCompanyWiseBankslist;
    }


    public LiveData<ArrayList<DeliveryItemModel>> getSqlDeliveryItems() {
        return mDeliverylist;
    }

    public LiveData<RegisterdCustomerModel> getSqlSelectedCustomer() {
        return mSelectedCustomer;
    }

    public LiveData<ArrayList<DeliveryItemModel>> getSqlSelectedItem() {
        return mSelectedItemList;
    }

    public LiveData<ArrayList<DailySummeryModel>> getdailySaleSummary() {
        return mDailySaleSummary;
    }

    public LiveData<ArrayList<DailySummeryModel>> getReceiptList() {
        return mReceiptList;
    }

    public LiveData<Float> getTotalReceipt() {
        return mTotalReceipt;
    }

    public LiveData<RegisterdCustomerListModel> getReturnedOrders() {
        return mReturnedOrder;
    }

    public LiveData<Boolean> getDeliveryInfo() {
        return mDeliverinfoStatus;
    }

    public LiveData<Boolean> checkIfInserted() {
        return inserted;
    }

    public LiveData<DeliveryInfo> getDeliveryInfoData() {
        return mdeliverInfo;
    }


}
