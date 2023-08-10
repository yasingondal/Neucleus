package detrack.zaryansgroup.com.detrack.activity.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.HashMap;

import detrack.zaryansgroup.com.detrack.activity.Model.RegisterCustomerModel.RegisterdCustomerListModel;
import detrack.zaryansgroup.com.detrack.activity.Model.RegisterCustomerModel.RegisterdCustomerModel;
import detrack.zaryansgroup.com.detrack.activity.Repo.MainRepo;

public class ReturnedOrder_ViewModel extends AndroidViewModel {

    MainRepo mainRepo;
    private LiveData<ArrayList<RegisterdCustomerModel>> mRegisterCustomerList;
    private LiveData<RegisterdCustomerListModel> mReturnedOrders;

    public ReturnedOrder_ViewModel(@NonNull Application application) {
        super(application);

        Log.d("checkData","null");
        mainRepo= new MainRepo(application);
        mRegisterCustomerList = mainRepo.getSqlRegisterCustomerInfo();
        mReturnedOrders = mainRepo.getReturnedOrders();
    }


    public void getSqlCustomerInfo(String routeid){
        mainRepo.getSqlRegisterCustomerInfo(routeid);
    }

    public void getReturnedOrdre(HashMap<String , String > hashMap){
        mainRepo.reqReturnedOrders(hashMap);
    }


    public LiveData<ArrayList<RegisterdCustomerModel>> getSelectedCustomer(){
        return mRegisterCustomerList;
    }
    public LiveData<RegisterdCustomerListModel> getReturnedOrder(){
        return mReturnedOrders;
    }
}
