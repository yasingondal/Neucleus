package detrack.zaryansgroup.com.detrack.activity.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import detrack.zaryansgroup.com.detrack.activity.Model.DeliveryInfo;
import detrack.zaryansgroup.com.detrack.activity.Repo.MainRepo;

public class POD_DashBoard_ViewModel extends AndroidViewModel {

    LiveData<DeliveryInfo> mDeliveryinfo;
    MainRepo mainRepo;
    public POD_DashBoard_ViewModel(@NonNull Application application) {
        super(application);
        mainRepo = new MainRepo(application);
    }



    public LiveData<DeliveryInfo> getDeliveryinfo() {
        return mDeliveryinfo;
    }
}
