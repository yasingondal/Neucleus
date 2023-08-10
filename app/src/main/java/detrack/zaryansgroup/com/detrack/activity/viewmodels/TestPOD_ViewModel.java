package detrack.zaryansgroup.com.detrack.activity.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.DeliveryItemModel;
import detrack.zaryansgroup.com.detrack.activity.Model.DeliveryInfo;
import detrack.zaryansgroup.com.detrack.activity.Repo.MainRepo;
import detrack.zaryansgroup.com.detrack.activity.SQLlite.ZEDTrackDB;

public class TestPOD_ViewModel extends AndroidViewModel {


    ZEDTrackDB db;
    MainRepo mainRepo;
    LiveData<DeliveryInfo> mDeliveryinfo;
    private LiveData<ArrayList<DeliveryItemModel> > mSelectedItem;
    public TestPOD_ViewModel(@NonNull Application application) {
        super(application);
        db = new ZEDTrackDB(application);
        mainRepo = new MainRepo(application);
        mDeliveryinfo  = mainRepo.getDeliveryInfoData();
        mSelectedItem = mainRepo.getSqlSelectedItem();
    }



    public void updateLatLng(DeliveryInfo deliveryInfo){
        db.UpdatePODLatLng(deliveryInfo);
    }

    public void getSelectedSQLiteOrderDelivery(int did, String isNew){
        mainRepo.getSelectedSQLiteOrderDelivery(did,isNew);
    }

    public void loadOrdersById(String id, String isnew){
        mainRepo.loadSelectedItem(id,isnew,false);
    }

    public LiveData<DeliveryInfo> getDeliverinfoData(){
        return mDeliveryinfo;
    }

    public LiveData<ArrayList<DeliveryItemModel>> getOrderItems(){
        return mSelectedItem;
    }
}
