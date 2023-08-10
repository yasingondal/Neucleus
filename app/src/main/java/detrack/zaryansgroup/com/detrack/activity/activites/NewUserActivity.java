package detrack.zaryansgroup.com.detrack.activity.activites;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import detrack.zaryansgroup.com.detrack.R;
import detrack.zaryansgroup.com.detrack.activity.Adapter.DropDownListAdapter;
import detrack.zaryansgroup.com.detrack.activity.Map.GPSTracker;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyRouteModel.RouteModel;
import detrack.zaryansgroup.com.detrack.activity.Model.DropDownModel;
import detrack.zaryansgroup.com.detrack.activity.Model.Params;
import detrack.zaryansgroup.com.detrack.activity.Model.RegisterCustomerModel.RegisterdCustomerModel;
import detrack.zaryansgroup.com.detrack.activity.SQLlite.ZEDTrackDB;
import detrack.zaryansgroup.com.detrack.activity.Service.CompanyInfoService;
import detrack.zaryansgroup.com.detrack.activity.Service.GPSService;
import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;
import detrack.zaryansgroup.com.detrack.activity.retrofit.Api_Reto;
import detrack.zaryansgroup.com.detrack.activity.utilites.ConnectionDetector;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import timber.log.Timber;

public class NewUserActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {
    Spinner DivisonSp, routeSp;


    String upload_URL = "https://qubat.000webhostapp.com/uploadImagecheck.php";
    JSONObject jsonObject;
    RequestQueue rQueue;


    boolean rFillImageName=false;

    String imag_name;
    File imageFile;


    Spinner CustomerTypeSp, TypeSp, CustomerSalesModeSp;
    ArrayList<String> CustomerTypeList;
    ArrayList<String> TypeList;
    ArrayList<String> PaymentMethodsList;
    String SelectedCustomerType; //ContactType...
    String SelectedType;  // Type...
    String CustomerPaymentMethod;
    ImageView CustomerProfileImage;
    int REQUEST_CAMERA = 0, SELECT_FILE = 1; //For Customer Image



    CheckBox isCurrentLocCB;
    double latitude, longitude;
    private String lat = "0.0", lng = "0.0", divisonID, routeID;
    ZEDTrackDB db;
    GoogleMap map;
    EditText username, address1, cell;
    Button savebtn;
    SharedPrefs prefs;
    ArrayList<DropDownModel> countryList, customrTypeList, divisionList, routeList;
    String AreaId, ZoneId;
    boolean isAreaAndZoneIdGet = false;
    ImageButton btnMenu;

    boolean isGpsEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    LocationManager locationManager;
    AlertDialog.Builder alertDialog;
    AlarmManager alarm;
    PendingIntent pintent;
    HashMap<String, String> userMap;
    final int IMAGE_MAX_SIZE = 250 * 250;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        fm.getMapAsync(this);



       InitilizeAlaram();
       runTimePermission();



        DivisonSp = findViewById(R.id.DivisonSp);
        routeSp = findViewById(R.id.routeSp);
        CustomerTypeSp = findViewById(R.id.CustomerTypeSp);
        TypeSp = findViewById(R.id.TypeSp);
        CustomerSalesModeSp = findViewById(R.id.CustomerSalesMode);
        CustomerProfileImage = findViewById(R.id.CustomerProfileImage);


        //Array List....
        CustomerTypeList = new ArrayList<>();
        TypeList = new ArrayList<>();
        PaymentMethodsList = new ArrayList<>();
        //For Initially Turn on auto Get Location.
//        isCurrentLocCB.setChecked(true);

        //Adding Hardcoded Data in Both Lists.
        AddCustomerTypesHardCoded();
        AddTypesHardCoded();
        AddCashModesHardCoded();

        CustomerTypesSpinnerListner();
        TypesSpinnerListner();
        CustomerPaymentMethodSpinnerListner();


        username = findViewById(R.id.user_name);
        address1 = findViewById(R.id.address1);
        cell = findViewById(R.id.cell);
        savebtn = findViewById(R.id.saveBtn);
        isCurrentLocCB = findViewById(R.id.CurrentLocationCB);
        prefs = new SharedPrefs(this);
        userMap = new HashMap<>();


        //hide image for later use

//        CustomerProfileImage.setImageDrawable(Drawable.createFromPath("/data/user/0/detrack.zaryansgroup.com.detrack/app_ZEDDelivery/1635496267519.jpg"));

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        } else {

            GetLocation();
        }


        db = new ZEDTrackDB(this);
        fillSpinner();
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.HideKeyBoard(getCurrentFocus(), NewUserActivity.this);
                if (username.getText().toString().trim().equals("") || address1.getText().toString().trim().equals("")
                        || cell.getText().toString().trim().equals("") || divisonID.trim().equals("0") || routeID.trim().equals("0")
                        || SelectedType.equals("0") || SelectedCustomerType.equals("0")
                        || CustomerPaymentMethod.equals("0")
                )
                    Check_Validation();
                else
                    saveCustomerDialgoue();
            }
        });

        getLatLong();


        CustomerProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectImage();
            }

        });


        isCurrentLocCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCurrentLocCB.isChecked()) {
                    try {
                        map.clear();
                        Location findme = map.getMyLocation();
                        latitude = findme.getLatitude();
                        longitude = findme.getLongitude();
                        lat = latitude + "";
                        lng = longitude + "";
                        Utility.Toast(NewUserActivity.this, latitude + " " + longitude);
                        LatLng newLatLng = new LatLng(latitude, longitude);
                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(newLatLng)
                                .title(newLatLng.toString());
                        map.addMarker(markerOptions);
                    } catch (Exception e) {
                        Utility.Toast(NewUserActivity.this, "Turn on your GPS first.");
                    }
                } else {
                    isCurrentLocCB.setChecked(false);
                    lat = "0.0";
                    lng = "0.0";
                }

            }
        });


        ArrayList<Params> parameterlist = new ArrayList<>();
        Params p1 = new Params();
        Params p2 = new Params();
        p1.setKey("Compnay_siteId");
        p1.setValue(prefs.getCompanySiteID());
        p2.setKey("Company_id");
        p2.setValue(prefs.getCompanyID());
        parameterlist.add(p1);
        parameterlist.add(p2);
        if (ConnectionDetector.isConnectingToInternet(NewUserActivity.this)) {
            // new GetCompanyDivision("GetCompanyDivision", "ZEDtrack.asmx", parameterlist).execute();
//            getCompanyDision("http://deliveryapi.zederp.net/api/Company/getCompanyDivision?Company_id=" + prefs.getCompanyID() + "&Compnay_siteId=" + prefs.getCompanySiteID());
            getCompanyDision(Utility.BASE_LIVE_URL + "api/Company/getCompanyDivision?Company_id=" + prefs.getCompanyID() + "&Compnay_siteId=" + prefs.getCompanySiteID());
            Log.d("divisionUrl", "http://deliveryapi.zederp.net/api/Company/getCompanyDivision?Company_id=" + prefs.getCompanyID() + "&Compnay_siteId=" + prefs.getCompanySiteID());
        } else {
            finish();
            Utility.Toast(NewUserActivity.this, "Check network connection and try again");
        }
    }


    private void runTimePermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    5);
        }
    }



    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(NewUserActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, (dialog, item) -> {
            if (items[item].equals("Take Photo")) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CAMERA);
            } else if (items[item].equals("Choose from Library")) {
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
            } else if (items[item].equals("Cancel")) {

                dialog.dismiss();
            }
        });
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            }

        }
    }

    public static Bitmap resizeImage(Bitmap realImage, float maxImageSize,
                                     boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    private void onCaptureImageResult(Intent data) {

        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);


        Glide.with(this)
                .load(thumbnail)
                .into(CustomerProfileImage);


        File myDirectory = new File(Environment.getExternalStorageDirectory(), "ZedDel");
        if (!myDirectory.exists()) {
            myDirectory.mkdirs();
        }


        imag_name = System.currentTimeMillis() + ".jpg";

        imageFile = new File(myDirectory, imag_name);
        FileOutputStream fo;
        try {
            imageFile.createNewFile();
            fo = new FileOutputStream(imageFile);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        rUploadProfileImageToServer(imageFile,imag_name);



    }


    private void onSelectFromGalleryResult(Intent data) {


//        imag_name = System.currentTimeMillis() + ".jpg";

        Uri selectedImageUri = data.getData();

        Glide.with(this)
                .load(selectedImageUri)
                .into(CustomerProfileImage);


        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(selectedImageUri, projection, null, null,
                null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        String imgPath = cursor.getString(column_index);

        imageFile = new File(cursor.getString(column_index));

        imag_name = imgPath.substring(imgPath.lastIndexOf("/")+1);


        rUploadProfileImageToServer(imageFile,imag_name);

    }




    public void rUploadProfileImageToServer(File hImageFile, String hFilename) {


        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("filename", hImageFile.getName(),
                        RequestBody.create(
                                hImageFile,
                                MediaType.parse("application/octet-stream")
                        )
                )
                .build();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(Utility.BASE_LIVE_URL+"api/POD/PostCustomerImage")
                .method("POST", body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Timber.d("on failure "+e.getMessage());
                rFillImageName = false;
            }


            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    Timber.d("Image Uploading Success");
                    rFillImageName = true;
                } else {

                    Timber.d("false else case");
                    rFillImageName = false;
                }

            }

        });



    }



    private void SendImageToOnlineServer(Bitmap bitmap1) {
        new Thread(() -> {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
            try {
                jsonObject = new JSONObject();
                String imgname = String.valueOf(Calendar.getInstance().getTimeInMillis());
                jsonObject.put("name", imgname);
                jsonObject.put("image", encodedImage);
            } catch (JSONException e) {
                Log.e("JSONObject Here", e.toString());
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, upload_URL, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            rQueue.getCache().clear();
                            Toast.makeText(getApplication(), "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(getApplication(), "Image Not Uploaded Successfully", Toast.LENGTH_SHORT).show();

                }
            });

            rQueue = Volley.newRequestQueue(NewUserActivity.this);
            rQueue.add(jsonObjectRequest);

        }).start();

    }
    public Bitmap hImageUriToBitmap(Uri hPhotoUri) {


        InputStream in;
        try {
            ContentResolver mContentResolver = getContentResolver();
            in = mContentResolver.openInputStream(hPhotoUri);

            // Decode image size
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            in.close();


            int scale = 1;
            while ((options.outWidth * options.outHeight) * (1 / Math.pow(scale, 2)) >
                    IMAGE_MAX_SIZE) {
                scale++;
            }

            Bitmap resultBitmap = null;
            in = mContentResolver.openInputStream(hPhotoUri);
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                options = new BitmapFactory.Options();
                options.inSampleSize = scale;
                resultBitmap = BitmapFactory.decodeStream(in, null, options);

                // resize to desired dimensions
                int height = resultBitmap.getHeight();
                int width = resultBitmap.getWidth();

                double y = Math.sqrt(IMAGE_MAX_SIZE
                        / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(resultBitmap, (int) x,
                        (int) y, true);
                resultBitmap.recycle();
                resultBitmap = scaledBitmap;

                System.gc();
            } else {
                resultBitmap = BitmapFactory.decodeStream(in);
            }
            in.close();

            return resultBitmap;
        } catch (IOException e) {
            Timber.d("Exception %s", e.getMessage());
            return null;
        }
    }




    private void TypesSpinnerListner() {
        TypeSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                SelectedType = adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void CustomerTypesSpinnerListner() {
        CustomerTypeSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                SelectedCustomerType = adapterView.getItemAtPosition(i).toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void CustomerPaymentMethodSpinnerListner() {
        CustomerSalesModeSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                CustomerPaymentMethod = adapterView.getItemAtPosition(i).toString();

                if (SelectedCustomerType.equalsIgnoreCase("Payment Method")) {
                    SelectedCustomerType = "0";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    private void AddCustomerTypesHardCoded() {
        String[] CustomerTypes = new String[]{"Unregistered", "Registered", "Retail Consumers"};
        CustomerTypeList.addAll(Arrays.asList(CustomerTypes));
        ArrayAdapter<String> CustomerTypesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, CustomerTypeList);
        CustomerTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CustomerTypeSp.setAdapter(CustomerTypesAdapter);

    }

    private void AddTypesHardCoded() {
        String[] Types = new String[]{"Secondary","Primary", "Online"};
        TypeList.addAll(Arrays.asList(Types));
        ArrayAdapter<String> TypesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, TypeList);
        TypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TypeSp.setAdapter(TypesAdapter);


    }

    private void AddCashModesHardCoded() {
        String[] PaymentMethods = new String[]{"Cash", "Credit"};
        PaymentMethodsList.addAll(Arrays.asList(PaymentMethods));
        ArrayAdapter<String> PaymentMethodsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, PaymentMethodsList);
        PaymentMethodsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CustomerSalesModeSp.setAdapter(PaymentMethodsAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.actionRegNewCustomer: {
                Intent intent = new Intent(NewUserActivity.this, NewUserActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
                break;
            }
            case R.id.actionSyncCompanyCustomerInfo: {
                if (ConnectionDetector.isConnectingToInternet(NewUserActivity.this)) {
                    startService(new Intent(NewUserActivity.this, CompanyInfoService.class));
                    Utility.Toast(NewUserActivity.this, "Syncing Started...");
                } else {
                    Utility.Toast(NewUserActivity.this, "Check network connection and try again");
                }
                break;
            }
            case R.id.actionAddSalesOrder: {
                Intent intent = new Intent(NewUserActivity.this, TakeOrder.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
                break;
            }
            case R.id.actionSettings: {
                Intent intent = new Intent(NewUserActivity.this, SettingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            }
            case R.id.actionAddSalesReturn: {

                Intent intent = new Intent(NewUserActivity.this, ReturnOrderSearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            }
            case R.id.enableLocation: {
                if (item.getTitle().toString().equals("Enable Location")) {
                    SpannableString spanString = new SpannableString("Disable Location");
                    spanString.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, spanString.length(), 0); //fix the color to white
                    item.setTitle(spanString);

                    item.setTitle("Disable Location");
                    GPSTracker gps = new GPSTracker(NewUserActivity.this);
                    if (ConnectionDetector.isConnectingToInternet(NewUserActivity.this)) {
                        if (gps.canGetLocation()) {
                            Utility.Toast(NewUserActivity.this, "Location Enable Successfully");
                            startservice();
                        } else {
                            Utility.Toast(NewUserActivity.this, "Enable your GPS first and try again..");
                            //gps.showSettingsAlert();
                        }
                    } else
                        Utility.Toast(NewUserActivity.this, "Check network connection and try again");
                    break;
                } else {
                    SpannableString spanString = new SpannableString("Enable Location");
                    spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0); //fix the color to white
                    item.setTitle(spanString);

                    Utility.Toast(NewUserActivity.this, "Location Disable Successfully");
                    stopservice();
                    break;
                }
            }
            case R.id.actionAboutUs: {
                Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.aboutus_custom_dialog);
                TextView tvAppVersion = dialog.findViewById(R.id.tvAppVersion);
                tvAppVersion.setText("version" + WelcomeActivity.versionName);
                dialog.show();
                break;
            }
            case R.id.actionUserInfo: {
                Utility.userInfoDialog(this);
                break;
            }
            default: {
                return false;
            }
        }
        return true;
    }

    private void startservice() {
        Calendar cal = Calendar.getInstance();
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 1 * 10 * 1000, pintent);
    }

    private void stopservice() {
        alarm.cancel(pintent);
    }

    private void InitilizeAlaram() {
        startService(new Intent(this, GPSService.class));
    }



    private void fillSpinner() {
        countryList = new ArrayList<>();
        DropDownModel countrymodel = new DropDownModel();
        countrymodel.setName("Pakistan");
        countrymodel.setId("1");
        countryList.add(countrymodel);
        customrTypeList = new ArrayList<>();
        DropDownModel customrType = new DropDownModel();
        customrType.setName("Select Type");
        customrType.setId("0");
        customrTypeList.add(customrType);
        customrType = new DropDownModel();
        customrType.setName("Wholesaler");
        customrType.setId("1");
        customrTypeList.add(customrType);

        customrType = new DropDownModel();
        customrType.setName("Retailer");
        customrType.setId("2");
        customrTypeList.add(customrType);
        customrType = new DropDownModel();
        customrType.setName("Key Outlet");
        customrType.setId("3");

        customrTypeList.add(customrType);
        customrType = new DropDownModel();
        customrType.setName("Other");
        customrType.setId("4");
        customrTypeList.add(customrType);

        DivisonSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    divisonID = divisionList.get(position).getId();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                try {
                    divisonID = divisionList.get(0).getId();
                }
                catch (Exception e){
                    Timber.d("Exception is "+e.getMessage());
                }

            }
        });

        routeList = new ArrayList<>();
        ArrayList<RouteModel> route = new ZEDTrackDB(NewUserActivity.this).getCompanyRoute();
        DropDownModel model;
        for (int i = 0; i < route.size(); i++) {

//            if (i == 0) {
//                model = new DropDownModel();
//                model.setName("Select Route");
//                model.setId("0");
//                routeList.add(model);
//            }



            model = new DropDownModel();
            model.setId(route.get(i).getRoute_id() + "");
            model.setName(route.get(i).getRoute_name());
            routeList.add(model);


        }


        routeSp.setAdapter(new DropDownListAdapter(NewUserActivity.this, routeList));
        routeSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                routeID = routeList.get(position).getId();
                if (ConnectionDetector.isConnectingToInternet(NewUserActivity.this)) {
                    ArrayList<Params> parameterlist = new ArrayList<>();
                    Params p1 = new Params();
                    Params p2 = new Params();
                    Params p3 = new Params();
                    p1.setKey("Compnay_siteId");
                    p1.setValue(prefs.getCompanySiteID());
                    p2.setKey("Company_id");
                    p2.setValue(prefs.getCompanyID());
                    p3.setKey("RouteId");
                    p3.setValue(routeID);
                    parameterlist.add(p1);
                    parameterlist.add(p2);
                    parameterlist.add(p3);
                    //new GetZoneAndAreaDetail("GetZoneAndAreaDetail", "ZEDtrack.asmx", parameterlist).execute();
//                        getZoneAndArea("http://deliveryapi.zederp.net/api/Company/getZoneAndArea?Company_id=" + prefs.getCompanyID() + "&Compnay_siteId=" + prefs.getCompanySiteID() + "&RouteID=" + routeID);
                    getZoneAndArea(Utility.BASE_LIVE_URL + "api/Company/getZoneAndArea?Company_id=" + prefs.getCompanyID() + "&Compnay_siteId=" + prefs.getCompanySiteID() + "&RouteID=" + routeID);
                    Log.d("zoneAreaURl", "http://deliveryapi.zederp.net/api/Company/getZoneAndArea?Company_id=" + prefs.getCompanyID() + "&Compnay_siteId=" + prefs.getCompanySiteID() + "&RouteID=" + routeID);

                } else {
                    routeSp.setSelection(0);
                    Utility.Toast(NewUserActivity.this, "Check network connection and try again");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//                routeID = routeList.get(0).getId();
            }
        });
    }

    //get zone and area detail new api
    public void getZoneAndArea(String url) {

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("zoneResponse", response);

                if (response != null) {
                    try {

                        JSONObject parentObject = new JSONObject(response);
                        JSONArray tableArray = parentObject.getJSONArray("Table");
                        if (!(tableArray.length() > 0)) {
                            routeSp.setSelection(0);
                        } else {
                            for (int i = 0; i < tableArray.length(); i++) {
                                JSONObject jsonObject = tableArray.getJSONObject(i);
                                AreaId = jsonObject.getString("area_id");
                                ZoneId = jsonObject.getString("ZoneId");
                                isAreaAndZoneIdGet = true;
                            }
                        }
                    } catch (Exception e) {
                        e.getMessage();
                        Utility.logCatMsg("User Error " + e);
                        Utility.Toast(NewUserActivity.this, "Failed try again.");
                        routeSp.setSelection(0);
                    }
                } else {
                    Utility.logCatMsg("****** NULL ******");
                    Utility.Toast(NewUserActivity.this, "Server error");
                    routeSp.setSelection(0);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("zoneError", error.getMessage() + "");
                if (error instanceof ServerError) {
                    Toast.makeText(NewUserActivity.this, "Server error occurred", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(NewUserActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(NewUserActivity.this, "Connection timeout error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }


    private void Check_Validation() {

        if (username.getText().toString().trim().equals("")) {
            username.setHintTextColor(getResources().getColor(R.color.orange_peel));
            username.setHint("Name required");
        }
        if (address1.getText().toString().trim().equals("")) {
            address1.setHintTextColor(getResources().getColor(R.color.orange_peel));
            address1.setHint("Address required");
        }
        if (cell.getText().toString().trim().equals("")) {
            cell.setHintTextColor(getResources().getColor(R.color.orange_peel));
            cell.setHint("Cell No required");
        }

        if(
                !divisonID.isEmpty() && !routeID.isEmpty() && !SelectedType.isEmpty()
                        && !SelectedCustomerType.isEmpty() && !CustomerPaymentMethod.isEmpty()
        )
        {

            if (divisonID.equals("0")) {
                Utility.Toast(NewUserActivity.this, "Select Division First");
            } else if (routeID.equals("0")) {
                Utility.Toast(NewUserActivity.this, "Select Route First");
            } else if (SelectedType.equals("0")) {
                Utility.Toast(NewUserActivity.this, "Type is Required");
            } else if (SelectedCustomerType.equals("0")) {
                Utility.Toast(NewUserActivity.this, "Customer Type is Required");
            } else if (CustomerPaymentMethod.equals("0")) {
                Utility.Toast(NewUserActivity.this, "Sales Mode is Required");
            }

        }

    }

    private void getLatLong() {
        GPSTracker gps = new GPSTracker(NewUserActivity.this);
        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            if (map != null) {
                map.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)), 3000, null);
            }
        }
    }



    private void saveCustomerDialgoue() {

        final AlertDialog d = new AlertDialog.Builder(NewUserActivity.this)
                .setMessage("Do you want to save this customer?")
                .setPositiveButton("Ok", null) //Set to null. We override the onclick
                .setNegativeButton("cancel", null)
                .setCancelable(true)
                .create();
        View view = getLayoutInflater().inflate(R.layout.dialog_custom_title, null);
        TextView tvCustomTitle = view.findViewById(R.id.tvCustomTitle);
        tvCustomTitle.setText("Save Customer");
        d.setCustomTitle(view);

        d.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
                Button NB = d.getButton(AlertDialog.BUTTON_NEGATIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Do somethin
                        d.dismiss();
                        SaveToServer();

                    }
                });
                NB.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                    }
                });
            }
        });
        d.show();
    }

//    private void uploadImage() {
//        if(imageFile!=null && imag_name!=null)
//        {
//
//            try {
//                new Thread(() -> {
//                    rUploadProfileImageToServer(imageFile, imag_name);
//                    if(rFillImageName == false)
//                    {
//                        tempImgName = "";
//                    }
//                    else{
//                        tempImgName = imag_name;
//                    }
//                }).start();
//            }catch (Exception e)
//            {
//                Timber.d("Exception message is "+e.getMessage());
//            }
//
//
//        }
//
//    }


    private void SaveToServer() {
        if (ConnectionDetector.isConnectingToInternet(NewUserActivity.this)) {

            if(imag_name==null){
                imag_name = "NoImage.png";
            }

            registerNewCustomer(Utility.BASE_LIVE_URL + "api/Customer/RegisterCompanyCustomer2?ContactType=" + SelectedType +
                    "&Name=" + username.getText().toString().trim() + "&Cell=" + cell.getText().toString().trim() +
                    "&Phone="+ cell.getText().toString().trim()  + "&Address=" + address1.getText().toString().trim() + "&AreaId=" + AreaId + "&DivisionId=" + divisonID + "&ZoneId="
                    + ZoneId + "&City=null" + "&Longitude=" + latitude + "&Latitude=" + longitude + "&RouteId="
                    + routeID + "&CompanySiteID=" + prefs.getCompanySiteID() + "&CompanyID=" + prefs.getCompanyID() + "&CreatedBy="
                    + prefs.getUserName()
                    + "&Title=" + username.getText().toString().trim() + "&CustomerType=0" + "&Type=" + SelectedCustomerType
                    + "&SalesMode=" + CustomerPaymentMethod + "&ImageName="+imag_name + "&img="+imag_name
            );


            Timber.d(Utility.BASE_LIVE_URL + "api/Customer/RegisterCompanyCustomer2?ContactType=" + SelectedType +
                    "&Name=" + username.getText().toString().trim() + "&Cell=" + cell.getText().toString().trim() +
                    "&Phone="+ cell.getText().toString().trim() + "&Address=" + address1.getText().toString().trim() + "&AreaId=" + AreaId + "&DivisionId=" + divisonID + "&ZoneId="
                    + ZoneId + "&City=null" + "&Longitude=" + latitude + "&Latitude=" + longitude + "&RouteId="
                    + routeID + "&CompanySiteID=" + prefs.getCompanySiteID() + "&CompanyID=" + prefs.getCompanyID() + "&CreatedBy="
                    + prefs.getUserName()
                    + "&Title=" + prefs.getCompanyName() + "&CustomerType=0" + "&Type=" + SelectedCustomerType
                    + "&SalesMode=" + CustomerPaymentMethod + "&ImageName="+imag_name + "&img="+imag_name
            );

        } else {
            Utility.Toast(NewUserActivity.this, "Check network connection and try again");
        }
    }

    public void registerNewCustomer(String url) {


        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(NewUserActivity.this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();


        Observable<String> hResponseObservable = Api_Reto.getRetrofit().getRetrofit_services().addNewCustomer(url);
        CompositeDisposable hCompositeDisposable = new CompositeDisposable();

        Disposable hAddNewOrderDisposable = hResponseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                            if (response != null) {

                                try {

                                    JSONObject parentObject = new JSONObject(response);
                                    JSONArray tableArray = parentObject.getJSONArray("Table");
                                    if (!(tableArray.length() > 0)) {
                                        Utility.Toast(NewUserActivity.this, "Not Registered ");
                                    } else {
                                        ArrayList<RegisterdCustomerModel> customerList = new ArrayList<>();
                                        for (int i = 0; i < tableArray.length(); i++) {
                                            JSONObject feedObj = (JSONObject) tableArray.get(i);
                                            RegisterdCustomerModel model = new RegisterdCustomerModel();
                                            model.setCustomer_id(feedObj.getInt("ContactId"));
                                            model.setName(feedObj.getString("Name").equals("null") ? "" : feedObj.getString("Name"));
                                            model.setAddress(feedObj.getString("Address").equals("") ? "" : feedObj.getString("Address"));
                                            model.setAddress1(feedObj.getString("Address1").equals("") ? "" : feedObj.getString("Address1"));
                                            model.setCell(feedObj.getString("Phone").equals("") ? "" : feedObj.getString("Phone"));
                                            model.setPhone(feedObj.getString("Phone2").equals("") ? "" : feedObj.getString("Phone2"));
                                            model.setCity(feedObj.getString("City").equals("null") ? "" : feedObj.getString("City"));
                                            model.setCountry(feedObj.getString("Country").equals("null") ? "" : feedObj.getString("Country"));
                                            model.setLat(feedObj.getString("Latitude").trim());
                                            model.setLng(feedObj.getString("Longitude").trim());
                                            model.setRoute(feedObj.getInt("RouteId"));
                                            model.setSalesMode(feedObj.getString("SalesMode").trim());
                                            model.setImageName(feedObj.getString("ImageName"));
                                            customerList.add(model);
                                        }
                                        new ZEDTrackDB(NewUserActivity.this).insertCompanyCustomer(customerList, "False");
                                        //startService(new Intent(NewUserActivity.this, CompanyInfoService.class));
                                        Utility.Toast(NewUserActivity.this, "Register successfully");
                                        clearTextField();
                                        progressDialog.dismiss();
                                    }
                                } catch (Exception e) {
                                    progressDialog.dismiss();
                                    Utility.Toast(NewUserActivity.this, "Not Register ");
                                    e.getMessage();
                                    Utility.logCatMsg("User Error " + e);
                                }

                            } else {
                                progressDialog.dismiss();
                                Utility.logCatMsg("****** NULL ******");
                                Utility.Toast(NewUserActivity.this, "Server error");
                            }


                        } /*OnNext*/,
                        throwable -> {
                            if (throwable instanceof ServerError) {
                                Toast.makeText(NewUserActivity.this, "Server error occurred", Toast.LENGTH_SHORT).show();
                            } else if (throwable instanceof NetworkError) {
                                Toast.makeText(NewUserActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                            } else if (throwable instanceof TimeoutError) {
                                Toast.makeText(NewUserActivity.this, "Connection timeout error", Toast.LENGTH_SHORT).show();
                            }
                        }/*On Error*/,
                        () -> {
                            progressDialog.dismiss();
                            hCompositeDisposable.dispose();

                        }/*On Complete*/

                );

        hCompositeDisposable.add(hAddNewOrderDisposable);




//        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                if (response != null) {
//                    try {
//
//                        JSONObject parentObject = new JSONObject(response);
//                        JSONArray tableArray = parentObject.getJSONArray("Table");
//                        if (!(tableArray.length() > 0)) {
//                            Utility.Toast(NewUserActivity.this, "Not Registered ");
//                        } else {
//                            ArrayList<RegisterdCustomerModel> customerList = new ArrayList<>();
//                            for (int i = 0; i < tableArray.length(); i++) {
//                                JSONObject feedObj = (JSONObject) tableArray.get(i);
//                                RegisterdCustomerModel model = new RegisterdCustomerModel();
//                                model.setCustomer_id(feedObj.getInt("ContactId"));
//                                model.setName(feedObj.getString("Name").equals("null") ? "" : feedObj.getString("Name"));
//                                model.setAddress(feedObj.getString("Address").equals("") ? "" : feedObj.getString("Address"));
//                                model.setAddress1(feedObj.getString("Address1").equals("") ? "" : feedObj.getString("Address1"));
//                                model.setCell(feedObj.getString("Phone").equals("") ? "" : feedObj.getString("Phone"));
//                                model.setPhone(feedObj.getString("Phone2").equals("") ? "" : feedObj.getString("Phone2"));
//                                model.setCity(feedObj.getString("City").equals("null") ? "" : feedObj.getString("City"));
//                                model.setCountry(feedObj.getString("Country").equals("null") ? "" : feedObj.getString("Country"));
//                                model.setLat(feedObj.getString("Latitude").trim());
//                                model.setLng(feedObj.getString("Longitude").trim());
//                                model.setRoute(feedObj.getInt("RouteId"));
//                                model.setSalesMode(feedObj.getString("SalesMode").trim());
//                                model.setImageName(feedObj.getString("ImageName"));
//                                customerList.add(model);
//                            }
//                            new ZEDTrackDB(NewUserActivity.this).insertCompanyCustomer(customerList, "False");
//                            //startService(new Intent(NewUserActivity.this, CompanyInfoService.class));
//                            Utility.Toast(NewUserActivity.this, "Register successfully");
//                            clearTextField();
//                            progressDialog.dismiss();
//                        }
//                    } catch (Exception e) {
//                        progressDialog.dismiss();
//                        Utility.Toast(NewUserActivity.this, "Not Register ");
//                        e.getMessage();
//                        Utility.logCatMsg("User Error " + e);
//                    }
//                } else {
//                    progressDialog.dismiss();
//                    Utility.logCatMsg("****** NULL ******");
//                    Utility.Toast(NewUserActivity.this, "Server error");
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                progressDialog.dismiss();
//                Log.d("registerError", error.getMessage() + "");
//                if (error instanceof ServerError) {
//                    Toast.makeText(NewUserActivity.this, "Server error occurred", Toast.LENGTH_SHORT).show();
//                } else if (error instanceof NetworkError) {
//                    Toast.makeText(NewUserActivity.this, "Network error", Toast.LENGTH_SHORT).show();
//                } else if (error instanceof TimeoutError) {
//                    Toast.makeText(NewUserActivity.this, "Connection timeout error", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        RequestQueue queue = Volley.newRequestQueue(this);
//        queue.add(request);
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            GetLocation();
        }
        else if (requestCode == 5) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                runTimePermission();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setAllGesturesEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setRotateGesturesEnabled(true);

//        map.addMarker(new MarkerOptions().position(new LatLng(33.7182, 73.0605)).title("Current Location"));
//        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(33.7182, 73.0605));
//        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
//        map.moveCamera(center);
//        map.animateCamera(zoom);

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                map.clear();
                Location location = new Location("User Location");
                location.setLatitude(point.latitude);
                location.setLongitude(point.longitude);
                location.setTime(new Date().getTime()); //Set time as current Date
                lat = location.getLatitude() + "";
                lng = location.getLongitude() + "";
                Utility.Toast(NewUserActivity.this, lat + " " + lng);

                //Convert Location to LatLng
                LatLng newLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                MarkerOptions markerOptions = new MarkerOptions()
                        .position(newLatLng)
                        .title(newLatLng.toString());

                map.addMarker(markerOptions);
            }
        });

    }

    @Override
    public void onLocationChanged(Location location) {

        map.clear();
        map.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("Current Location"));
        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        map.moveCamera(center);
        map.animateCamera(zoom);

        try {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            LatLng newLatLng = new LatLng(latitude, longitude);
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(newLatLng)
                    .title(newLatLng.toString());
            map.addMarker(markerOptions);
        } catch (Exception e) {
            Utility.Toast(NewUserActivity.this, "Turn on your GPS first.");
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void clearTextField() {
        username.setText("");
        address1.setText("");
        cell.setText("");
        map.clear();
        CustomerProfileImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_account));

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    //get company Division new Api
    public void getCompanyDision(String url) {

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("divisionResponse", response);
                Timber.d("Response is nill");
                if (response != null) {
                    try {
                        Timber.d("Response" + response);
                        JSONObject parentObject = new JSONObject(response);
                        JSONArray tableArray = parentObject.getJSONArray("Table");
                        if (!(tableArray.length() > 0)) {
                            Utility.Toast(NewUserActivity.this, "Failed try again.");
                            finish();
                        } else {
                            divisionList = new ArrayList<>();
                            DropDownModel divisionModel;
                            for (int i = 0; i < tableArray.length(); i++) {
                                JSONObject jsonObject = tableArray.getJSONObject(i);

//                                if(i==0){
//                                    divisionModel = new DropDownModel();
//                                    divisionModel.setName("Select Division");
//                                    divisionModel.setId("0");
//                                    divisionList.add(divisionModel);
//                                }
                                divisionModel = new DropDownModel();
                                divisionModel.setName(jsonObject.getString("Title"));
                                divisionModel.setId(jsonObject.getString("DivId"));
                                divisionList.add(divisionModel);
                            }
                            divisonID = divisionList.get(0).getId();


                            DivisonSp.setAdapter(new DropDownListAdapter(NewUserActivity.this, divisionList));

                        }
                    } catch (Exception e) {
                        e.getMessage();
                        Utility.logCatMsg("User Error " + e);
                        Utility.Toast(NewUserActivity.this, "Failed try again.");
                        finish();
                    }
                } else {
                    Timber.d("Else Case Running");
                    Utility.logCatMsg("****** NULL ******");
                    Utility.Toast(NewUserActivity.this, "Server error");
                    finish();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("divisionError", error.getMessage() + "");
                if (error instanceof ServerError) {
                    Toast.makeText(NewUserActivity.this, "Server error occurred", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(NewUserActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(NewUserActivity.this, "Connection timeout error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    public void GetLocation() {
        try {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGpsEnabled && !isNetworkEnabled) {
                alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("GPS problem");
                alertDialog.setCancelable(true);
                alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
                alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int which) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        dialog.cancel();
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                alertDialog.show();

            } else {
                canGetLocation = true;

                if (isGpsEnabled) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 100, this);
                }
                if (isNetworkEnabled) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 100, this);

                }
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

}