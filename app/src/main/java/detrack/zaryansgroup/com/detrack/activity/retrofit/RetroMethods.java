package detrack.zaryansgroup.com.detrack.activity.retrofit;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.multidex.BuildConfig;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import timber.log.Timber;

public class RetroMethods {


    private final RetrofitService hApiService = Api_Reto.getRetrofit().getRetrofit_services();
    private RetroResponseInterface hRetroResponseInterface;

    CompositeDisposable hCompositeDisposable;
    public static final int H_UPLOAD_IMAGE = 1;

    //if there  will be more than 1 responses required we will use the below one...
    //public static final int Check_whate = 2;

    public RetroMethods(RetroResponseInterface hRetroResponseInterface) {
        this.hRetroResponseInterface = hRetroResponseInterface;
        hCompositeDisposable = new CompositeDisposable();
    }



    public void hUploadPodImages(Context context, File hImageFile, String hFilename) {
        Timber.d("hUploadPodImages");
        //Making Issue in .provider
//        Uri fileUri = FileProvider.getUriForFile(context, context.getPackageName() +
//                ".fileprovider", hImageFile);


        Uri fileUri = FileProvider.getUriForFile(Objects.requireNonNull(context),
                BuildConfig.APPLICATION_ID + ".provider", hImageFile);


        RequestBody hRequestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                        "filename",
                        hImageFile.getAbsolutePath(),
                        RequestBody.create(hImageFile, MediaType.parse(context.getContentResolver().getType(fileUri)))
                ).build();


        MultipartBody.Part body =
                MultipartBody.Part.createFormData("filename", hImageFile.getName(), hRequestBody);


//        Disposable subscribe = hApiService.hUpdateProfileImage(body)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        /*On Next*/
//                        imageRespone -> {
//                            Timber.d("Response is true");
//                            hRetroResponseInterface.hOnDataRetrieved(H_UPLOAD_IMAGE, true, imageRespone);
//                        },
//                        /*On Error*/
//                        throwable -> {
//                            Timber.d("Response is false %s", throwable.getMessage());
//                            hRetroResponseInterface.hOnDataRetrieved(
//                                    H_UPLOAD_IMAGE,
//                                    false,
//                                    "Eroor");
//                        }
//                );
//        hCompositeDisposable.add(subscribe);

    }


    public void hTestMethod(Context context, File hImageFile, String hFilename) {
        Timber.d("hUploadPodImages");


        Uri fileUri = FileProvider.getUriForFile(Objects.requireNonNull(context),
                BuildConfig.APPLICATION_ID + ".provider", hImageFile);


        RequestBody requestFile =
                RequestBody.create(
                        hImageFile,
                        MediaType.parse(context.getContentResolver().getType(fileUri))
                );

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("filename", hImageFile.getName(), requestFile);

//        // add another part within the multipart request
//        String descriptionString = "hello, this is description speaking";
//        RequestBody description =
//                RequestBody.create(
//                        okhttp3.MultipartBody.FORM, descriptionString);

        // finally, execute the request

        Disposable subscribe = hApiService.hTestMethod(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        /*On Next*/
                        imageRespone -> {
                            Timber.d("Response is true");
                            hRetroResponseInterface.hOnDataRetrieved(H_UPLOAD_IMAGE, true, imageRespone);
                        },
                        /*On Error*/
                        throwable -> {
                            Timber.d("Response is false %s", throwable.getMessage());
                            hRetroResponseInterface.hOnDataRetrieved(
                                    H_UPLOAD_IMAGE,
                                    false,
                                    "Eroor");
                        }
                );
        hCompositeDisposable.add(subscribe);


//        Call<ResponseBody> call = service.upload(description, body);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call,
//                                   Response<ResponseBody> response) {
//                Log.v("Upload", "success");
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.e("Upload error:", t.getMessage());
//            }
//        });
//    }

    }


    public void hTestMethod2(Context context, File hImageFile, String hFilename) {


//        Uri fileUri = FileProvider.getUriForFile(Objects.requireNonNull(context),
//                BuildConfig.APPLICATION_ID + ".provider", hImageFile);




        //Actual Working of Sending multi-part data startig from here..
       OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
//        MediaType mediaType = MediaType.parse("text/plain");

        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("filename", hImageFile.getName(),
                        RequestBody.create(
                                hImageFile,
                                MediaType.parse("application/octet-stream")
                        )
                )
                .build();
        Request request = new Request.Builder()
                .url("http://dwdapi.zeddelivery.com/api/POD/Post")
                .method("POST", body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Timber.d("onFailure");
                Timber.d("Failure issue:"+e.getMessage()+"");
            }



            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String s = response.toString();
                        Timber.d("Image sent %s", s);
                        Timber.d("Image Sent Successfully");

                } else {
                    Timber.d("Image erRROR  ");
                    Timber.d("Image Not Sent Successfully");

                }

            }
        });

    }

    //Actual Working of Sending multi-part data ending here


    public interface RetroResponseInterface {
        void hOnDataRetrieved(int callType, boolean isValidResponse, Object response);
    }

}
