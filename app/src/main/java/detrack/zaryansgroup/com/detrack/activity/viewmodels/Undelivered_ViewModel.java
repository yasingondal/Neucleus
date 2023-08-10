package detrack.zaryansgroup.com.detrack.activity.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.DeliveryItemModel;
import detrack.zaryansgroup.com.detrack.activity.Model.DeliveryInfo;
import detrack.zaryansgroup.com.detrack.activity.Model.RegisterCustomerModel.RegisterdCustomerModel;
import detrack.zaryansgroup.com.detrack.activity.Repo.MainRepo;
import detrack.zaryansgroup.com.detrack.activity.SQLlite.ZEDTrackDB;

public class Undelivered_ViewModel extends AndroidViewModel {

    MainRepo mainRepo;
    private ZEDTrackDB db;

    private MutableLiveData<RegisterdCustomerModel> mSelectedCustomer = new MutableLiveData<>();
    private MutableLiveData<ArrayList<DeliveryInfo>> orderList = new MutableLiveData<>();
    private MutableLiveData<ArrayList<DeliveryItemModel> > orderItemList = new MutableLiveData<>();

    public Undelivered_ViewModel(@NonNull Application application) {
        super(application);
        mainRepo = new MainRepo(application);
        db = new ZEDTrackDB(application);


    }




    public void loadOrderItems(String id){
        ArrayList<DeliveryItemModel> mlist = db.getSQLiteOrderDeliveryItems(id, "isNew",false);
        orderItemList.postValue(mlist);
    }

    public LiveData<ArrayList<DeliveryItemModel> > getOrderItems(){
        return orderItemList;
    }

    public void loadOrder(String typ,String  date){

        ArrayList<DeliveryInfo> list = new ArrayList<>();
        if(date != null){
            list = db.getOrderDelivery(typ,date);
        }else {
            list = db.getSQLiteOrderDelivery(typ);
        }
        orderList.postValue(list);


    }
    public LiveData<ArrayList<DeliveryInfo>> getOrders(){
        return orderList;
    }

    public void loadCustomerByid(int cid){
        RegisterdCustomerModel registerdCustomerModel = db.getCustomerById(cid);
        mSelectedCustomer.postValue(registerdCustomerModel);

    }


    public LiveData<RegisterdCustomerModel> getCustomeByid(){
        return mSelectedCustomer;
    }
}
