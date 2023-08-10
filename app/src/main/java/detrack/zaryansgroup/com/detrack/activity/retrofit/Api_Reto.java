package detrack.zaryansgroup.com.detrack.activity.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Api_Reto {

    private static Api_Reto instance = null;
    public static Retrofit retrofit = null;
    public static RetrofitService retrofit_services = null;


    private Api_Reto() {
    }

//    public static Api_Reto getImagesRetrofit() {
//        if (instance == null) {
//            instance = new Api_Reto();
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(Utility.BASE_LIVE_URL)
//                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                    .build();
//            retrofit_services = retrofit.create(RetrofitService.class);
//        }
//        return instance;
//    }


    public static Api_Reto getRetrofit() {
        if (instance == null) {

            Gson gson = new GsonBuilder().setPrettyPrinting()
                    .setLenient()
                    .create();

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(logging);

            instance = new Api_Reto();
            retrofit = new Retrofit.Builder()
                    .baseUrl(Utility.BASE_LIVE_URL)
                    .client(httpClient.build())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            retrofit_services = retrofit.create(RetrofitService.class);
        }
        return instance;
    }


    public static Api_Reto getMapRetrofit() {
        if (instance == null) {
            instance = new Api_Reto();
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://maps.googleapis.com/maps/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            retrofit_services = retrofit.create(RetrofitService.class);
        }
        return instance;
    }


    public RetrofitService getRetrofit_services() {
        return retrofit_services;
    }

}
