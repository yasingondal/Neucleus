package detrack.zaryansgroup.com.detrack.activity.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;

import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.DeliveryItemModel;
import detrack.zaryansgroup.com.detrack.activity.Model.RegisterCustomerModel.RegisterdCustomerModel;
import detrack.zaryansgroup.com.detrack.activity.Repo.MainRepo;

public class SelectProduct_viewModel extends AndroidViewModel {

    private LiveData<ArrayList<DeliveryItemModel>> mDeleiveryitems;
    private LiveData<RegisterdCustomerModel> mSelectedCustomer;
    private LiveData<ArrayList<DeliveryItemModel> > mSelectedItem;
    MainRepo mainRepo;
    public SelectProduct_viewModel(@NonNull Application application) {
        super(application);
        mainRepo = new MainRepo(application);
        mDeleiveryitems = mainRepo.getSqlDeliveryItems();
        mSelectedCustomer = mainRepo.getSqlSelectedCustomer();
        mSelectedItem = mainRepo.getSqlSelectedItem();
    }

    public void loadSqlDeliveryItems(){
        mainRepo.loadDeliveryItems();
    }

    public void loadSqlSelectedCustoemr(int cid){
        mainRepo.loadSelectedCustomer(cid);
    }

    public void loadSqlSelectedItem(String id, String isnew){
        mainRepo.loadSelectedItem(id,isnew,false);
    }

    public LiveData<ArrayList<DeliveryItemModel>> getDeliveryItems(){
        return mDeleiveryitems;
    }

    public LiveData<RegisterdCustomerModel> getSelectedCustomer(){
        return mSelectedCustomer;
    }

    public LiveData<  ArrayList<DeliveryItemModel> > getSelctedItem(){
        return mSelectedItem;
    }
}
