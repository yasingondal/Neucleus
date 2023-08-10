package detrack.zaryansgroup.com.detrack.activity.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;

import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.DeliveryItemModel;
import detrack.zaryansgroup.com.detrack.activity.Model.DailySummeryModel;
import detrack.zaryansgroup.com.detrack.activity.Repo.MainRepo;

public class DailySaleSummary_ViewModel extends AndroidViewModel {



    LiveData<ArrayList<DailySummeryModel>> summarList;
    LiveData<ArrayList<DailySummeryModel>> hReciptList;
    private LiveData<ArrayList<DeliveryItemModel> > mSelectedItem;
    LiveData<Float> totalReceipt;
    MainRepo mainRepo;
    public DailySaleSummary_ViewModel(@NonNull Application application) {
        super(application);
        mainRepo = new MainRepo(application);
        summarList = mainRepo.getdailySaleSummary();
        hReciptList = mainRepo.getReceiptList();
        totalReceipt = mainRepo.getTotalReceipt();
        mSelectedItem = mainRepo.getSqlSelectedItem();
    }


    public void loadDailySaleSummary(String date, String des){
        mainRepo.loadDailySaleSummary(date,des);
    }
    public void loadReceiptList(String date){
        mainRepo.loadReceiptList(date);
    }

    public void loadTotalReceipt(String date){
        mainRepo.loadReceiptList(date);
    }

    public void loadSqlSelectedItem(String id, String isnew,Boolean forc){
        mainRepo.loadSelectedItem(id,isnew,forc);
    }

    public LiveData<ArrayList<DailySummeryModel>> getSaleSummaryList() {
        return summarList;
    }


    public LiveData<ArrayList<DailySummeryModel>> gethReciptList() {
        return hReciptList;
    }

    public LiveData<Float> gettotalReceipt() {
        return totalReceipt;
    }
    public LiveData<  ArrayList<DeliveryItemModel> > getSelctedItem(){
        return mSelectedItem;
    }


}
