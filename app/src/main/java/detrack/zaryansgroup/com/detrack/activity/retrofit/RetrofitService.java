package detrack.zaryansgroup.com.detrack.activity.retrofit;


import java.util.HashMap;
import java.util.Map;

import detrack.zaryansgroup.com.detrack.activity.Model.CompanyDetailsModel.CompanyDetailsListModel;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyItemsModel.CompanyItemListModel;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyRouteModel.CompanyRouteListModel;
import detrack.zaryansgroup.com.detrack.activity.Model.CustomerPriceModel.CustomerPriceListModel;
import detrack.zaryansgroup.com.detrack.activity.Model.GeoCodingAddress.GeoCodeResult;
import detrack.zaryansgroup.com.detrack.activity.Model.LoginModel.LoginListModel;
import detrack.zaryansgroup.com.detrack.activity.Model.PreviousAmountModel.PreviousListModel;
import detrack.zaryansgroup.com.detrack.activity.Model.RegisterCustomerModel.RegisterdCustomerListModel;
import detrack.zaryansgroup.com.detrack.activity.Model.VehiclesModel.VehicleListModel;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface RetrofitService {


//    https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=CnRtAAAATLZNl354RwP_9UKbQ_5Psy40texXePv4oAlgP4qNEkdIrkyse7rPXYGd9D_Uj1rVsQdWT4oRz4QrYAJNpFX7rzqqMlZw2h2E2y5IKMUZ7ouD_SlcHxYq1yL4KbKUv3qtWgTK0A6QbGh87GB3sscrHRIQiG2RrmU_jF4tENr9wGS_YxoUSSDrYjWmrNfeEHSGSc3FyhNLlBU&key=AIzaSyDqQ_qabCg4qIh92L4j7sMEw7PgsHhSbvU


    @GET("api/place/photo")
    Call<GeoCodeResult> placesPhoto(@Query("maxwidth") int mWidth, @Query("key") String key);

    @GET("api/geocode/json")
    Call<GeoCodeResult> GeoCodDetials(@Query("latlng") String latlng, @Query("key") String key);

    @GET("api/Authenticate/AuthenticateUser")
    Call<LoginListModel> isValidUser(@QueryMap Map<String, String> param);

    @GET("api/Receipts/GetCustomerPreviousReceiptsAmount2")
    Call<PreviousListModel> getCustomerPerviousAmount(@Query("CustomerId") int customerid);

    @GET("api/GCM/SetGCMID")
    Call<ResponseBody> firebaseTokenRegistration(@QueryMap Map<String, String> param);

    @GET("api/Customer/GetRegisterCompanyCustomer")
    Call<RegisterdCustomerListModel> getRegisterCustomerInfo(@Query("Company_id") String c_id, @Query("CompanySiteID") String c_i, @Query("UserId") int UserId);

    @GET("api/Company/GetRegisterCompanyRoutes")
    Call<CompanyRouteListModel> getCompanyRoutes(@Query("Company_id") String c_id, @Query("CompanySiteID") String c_i, @Query("UserId") int UserId);

    @GET("api/Company/CompanyDetails")
    Call<CompanyDetailsListModel> getCompanyDetails(@Query("Company_id") String c_id);

    @GET("api/Company/GetCompanyItems")
    Call<CompanyItemListModel> getCompanyItems(@Query("Company_id") String c_id, @Query("companysiteID") String c_sid);

    @GET("api/Vehicle/getCompanyVehicles")
    Call<VehicleListModel> getCompanyVehicles(@Query("Company_id") String c_i, @Query("Compnay_siteId") String c_sid);

    @GET("api/customer/updateCustomerLatLong")
    Call<ResponseBody> updateCustomerLocation(@Query("CustomerId") String cid, @Query("Longitude") String lng,
                                              @Query("Latitude") String lat);

    @GET("api/Order/GetOrders")
    Call<RegisterdCustomerListModel> getReturnedOrders(@QueryMap Map<String, String> param);


    @POST("api/Order/insertJobPod")
    Call<ResponseBody> updateOrder(@QueryMap Map<String, String> param);

    @GET("api/Customer/GetCustomerProductPrices")
    Call<CustomerPriceListModel> getCustomerPrice(@Query("companyID") String companyID, @Query("companySiteID") String companySiteID);

    @Multipart
    @POST("api/POD/Post")
    Observable<String> hUpdateProfileImage(
            @Part("description") RequestBody description,
            @Part MultipartBody.Part file
    );


    @Multipart
    @POST("api/POD/Post")
    Observable<String> hTestMethod(
            @Part MultipartBody.Part file
    );


    @POST("api/Order/addNewOrderV3")
    Observable<String> hMakeNewOrder(@Body HashMap hashMap);


    @GET
    Observable<String> sendReceiptToServer(@Url String url);

    @POST
    Observable<String> addNewCustomer(@Url String url);

    @POST
    Observable<String> addNewVisit(@Url String url);
    


}

