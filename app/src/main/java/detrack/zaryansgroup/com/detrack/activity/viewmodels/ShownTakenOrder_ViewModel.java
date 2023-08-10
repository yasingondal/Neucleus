package detrack.zaryansgroup.com.detrack.activity.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import detrack.zaryansgroup.com.detrack.activity.Model.DeliveryInfo;
import detrack.zaryansgroup.com.detrack.activity.Model.LoginModel.LoginListModel;
import detrack.zaryansgroup.com.detrack.activity.Repo.MainRepo;
import detrack.zaryansgroup.com.detrack.activity.SQLlite.ZEDTrackDB;

public class ShownTakenOrder_ViewModel extends AndroidViewModel {


    private ZEDTrackDB db;
    private MutableLiveData<ArrayList<DeliveryInfo>> mDeliveryinfoData = new MutableLiveData<>();
    private MutableLiveData<Integer> isDeleted = new MutableLiveData<>();
    private MutableLiveData<Integer> isUpdated = new MutableLiveData<>();
    MainRepo mainRepo;
    public ShownTakenOrder_ViewModel(@NonNull Application application) {
        super(application);
        db = new ZEDTrackDB(application);
        mainRepo = new MainRepo(application);
    }


    public void getOrderDelivery(String status, String date){
        ArrayList<DeliveryInfo> list = db.getOrderDelivery(status,date);
        mDeliveryinfoData.postValue(list);
    }
    public void getOrderDeliveryStatusDeliveredAndSynced(String date){
        ArrayList<DeliveryInfo> list = db.getOrderDeliveryStatusDeliveredAndSynced(date);
        mDeliveryinfoData.postValue(list);

    }


    public void deleteOrder(String delivery_id, String IsNew, int i){
        if(db.deleteOrder(delivery_id, IsNew)){
            isDeleted.postValue(i);
        }else {
            isDeleted.postValue(-1);
        }
    }

    public void UpdateOrderStatus(DeliveryInfo deliveryInfo, int pos){
        db.UpdateOrderStatus(deliveryInfo);
        isUpdated.postValue(pos);

    }


    public LiveData<ArrayList<DeliveryInfo>> getOrderDelivery(){
        return mDeliveryinfoData;
    }



    public LiveData<Integer> deleteOrder(){
        return isDeleted;
    }

    public LiveData<Integer> UpdateOrderStatus(){
        return  isUpdated;
    }

}
