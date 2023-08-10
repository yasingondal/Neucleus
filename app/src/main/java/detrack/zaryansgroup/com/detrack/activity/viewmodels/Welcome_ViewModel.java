package detrack.zaryansgroup.com.detrack.activity.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import detrack.zaryansgroup.com.detrack.activity.Model.WelcomeModel.WelcomeDataModel;
import detrack.zaryansgroup.com.detrack.activity.Repo.MainRepo;

public class Welcome_ViewModel extends AndroidViewModel {

    private LiveData<WelcomeDataModel> mWelcomeData;
    MainRepo mainRepo;

   public Welcome_ViewModel(@NonNull Application application) {
        super(application);
       mainRepo= new MainRepo(application);
       mWelcomeData = mainRepo.getWelcomeData();
    }



    public void LoadWelcomeData(){
        mainRepo.loadWelcomeData();
    }

    public LiveData<WelcomeDataModel> getmWelcomeData() {
        return mWelcomeData;
    }

}
