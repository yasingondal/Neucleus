package detrack.zaryansgroup.com.detrack.activity.activites;

import static detrack.zaryansgroup.com.detrack.activity.retrofit.RetroMethods.H_UPLOAD_IMAGE;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import detrack.zaryansgroup.com.detrack.R;
import detrack.zaryansgroup.com.detrack.activity.Adapter.GridAdapter;
import detrack.zaryansgroup.com.detrack.activity.Map.GPSTracker;
import detrack.zaryansgroup.com.detrack.activity.Model.ImagesModel;
import detrack.zaryansgroup.com.detrack.activity.SQLlite.ZEDTrackDB;
import detrack.zaryansgroup.com.detrack.activity.Service.CompanyInfoService;
import detrack.zaryansgroup.com.detrack.activity.Service.GPSService;
import detrack.zaryansgroup.com.detrack.activity.retrofit.ImageRespone;
import detrack.zaryansgroup.com.detrack.activity.retrofit.RetroMethods;
import detrack.zaryansgroup.com.detrack.activity.utilites.ConnectionDetector;
import detrack.zaryansgroup.com.detrack.activity.utilites.SendPod;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import timber.log.Timber;

public class AddImages extends AppCompatActivity implements RetroMethods.RetroResponseInterface {
    ZEDTrackDB db;
    GridView PodGridView, pobGridView;
    GridAdapter Pod_adapter, pob_adapter;
    ImageButton btnMenu;
    ListView menuList;
    int REQUEST_CAMERA = 0, SELECT_FILE = 1, orderId;
    Button pod_addimageBtn, pob_add_imagBtn;
    RetroMethods retroMethods;
    String a;

    List<ImagesModel> GrabImagesNamesForSyncing;

   String ImagesSynced = "false";

    public static String ImagesNames;

    String okhttpResponse = "false";
    int IntialCountImagesList;

    String filename, base64file, OrderID, PODImage, POB, CompanyID, CompanySiteID, CreatedOn, CreatedBy;
    String imagTag;


    ArrayList<ImagesModel> pod_imaglist, pob_imaglist;
    Button btnUploadImages;

    String imagType = null;
    ImageView zoomImgView;
    FrameLayout PodframeLayout;
    LinearLayout PobframeLayout;
    TextView noPobTV, noPodTV;
    AlarmManager alarm;
    PendingIntent pintent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_images);
        if (getSupportActionBar() != null) {
            setUpActionBar(getSupportActionBar());
        }
        InitilizeAlaram();
        runTimePermission();


        retroMethods = new RetroMethods(this);


        db = new ZEDTrackDB(AddImages.this);
        PodGridView = findViewById(R.id.gridLayout);
        pobGridView = findViewById(R.id.pobgridLayout);
        zoomImgView = findViewById(R.id.zoomImageView);
        PodframeLayout = findViewById(R.id.podFrameLayout);
        PobframeLayout = findViewById(R.id.pobFrameLayout);
        noPobTV = findViewById(R.id.no_pobTV);
        noPodTV = findViewById(R.id.no_podTV);
        btnUploadImages = findViewById(R.id.btnUploadImages);

        PodGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                selectOptions(position, view, "pod");
                return false;
            }
        });
        PodGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                zoomImgView.setVisibility(View.VISIBLE);
                PodGridView.setVisibility(View.GONE);
                zoomImgView.setImageDrawable(((ImageView) view.findViewById(R.id.img)).getDrawable());
            }
        });
        pobGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                selectOptions(position, view, "pob");
                return false;
            }
        });
        pobGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                zoomImgView.setVisibility(View.VISIBLE);
                PodGridView.setVisibility(View.GONE);
                zoomImgView.setImageDrawable(((ImageView) view.findViewById(R.id.img)).getDrawable());
            }
        });
        pod_addimageBtn = findViewById(R.id.AddImgBtn);
        pob_add_imagBtn = findViewById(R.id.AddPobImgBtn);
        pod_imaglist = new ArrayList<>();
        pob_imaglist = new ArrayList<>();

        Intent intent = getIntent();

        orderId = Integer.parseInt(intent.getStringExtra("OrderId"));

        imagType = getIntent().getStringExtra("imgType");


        if (getIntent().getStringExtra("imgType").toString().equals("POD")) {
            PobframeLayout.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.weight = 1.0f;
            PodframeLayout.setLayoutParams(params);
            pod_addimageBtn.setHeight(40);

        }


        fillGridview();
        fillPOBGridview();
        pod_addimageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagType = "POD";
                selectImage();
            }
        });


        pob_add_imagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagType = "POB";
                selectImage();
            }
        });


        btnUploadImages.setOnClickListener(v -> {
            hCreateUploadList();
        });

    }

    private void hCreateUploadList() {

        List<ImagesModel> hImageModelList = new ArrayList<>();

        if (pod_imaglist.size() > 0) {
            hImageModelList.addAll(pod_imaglist);
        }
        if (pob_imaglist.size() > 0) {
            hImageModelList.addAll(pob_imaglist);
        }

        if (hImageModelList.size() > 0) {
            hUploadList(hImageModelList);
        } else {
            Toast.makeText(AddImages.this, "There are No Images", Toast.LENGTH_SHORT).show();
        }

    }

    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteFormat = stream.toByteArray();
        return Base64.encodeToString(byteFormat, Base64.NO_WRAP);
    }

    public void UploadPodImages(File hImageFile, String hFilename) {

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
        Request request = new Request.Builder()
                .url(Utility.BASE_LIVE_URL+"/api/POD/Post")
                .method("POST", body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }


            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    okhttpResponse = "true";
                } else {

                    Timber.d("Uploaded success false");

                }

            }

        });



    }


    private void hUploadList(List<ImagesModel> hImageModelList) {

            for (int i = 0; i < hImageModelList.size(); i++) {
                filename = hImageModelList.get(i).getImage_name();
                File FilePath = readFileFromInternalStorage(filename);

                UploadPodImages(FilePath, filename);

                int a = db.UpdateImagesStatusToSycned((ArrayList<ImagesModel>) hImageModelList);
                if (a == 1) {
                    ImagesSynced = "true";
                } else {
                    Timber.d("not Update in Local Db");
                }

            }

            if(ImagesSynced.equalsIgnoreCase("true")){
                Toast.makeText(AddImages.this, "Images Synced Successfully", Toast.LENGTH_SHORT).show();
                ImagesSynced = "false";
            }

            GrabImagesNames();



//                Timber.d("FilePath is: "+FilePath);
//                String filePath = FilePath.getPath();
//                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
//
//                Timber.d("BitMap: " +bitmap+"");
//                if (bitmap != null) {
//                    //base64file = getEncoded64ImageStringFromBitmap(bitmap);
//                    base64file = "getEncoded64ImageStringFromBitmap(bitmap)";
//                } else {
//                    Toast.makeText(AddImages.this, "Bitmap is null", Toast.LENGTH_SHORT).show();
//                }
//
//                imagTag = hImageModelList.get(i).getImage_tag();
//                OrderID = String.valueOf(hImageModelList.get(i).getImage_order_id());
//                PODImage = filename;
//
//                CompanyID = new SharedPrefs(AddImages.this).getCompanyID();
//                CompanySiteID = new SharedPrefs(AddImages.this).getCompanySiteID();
//                CreatedBy = new SharedPrefs(AddImages.this).getUserName();
//                CreatedOn = Utility.getCurrentDate() + " + " + Utility.getCurrentTime();
//
//                String apirul = "http://appapi.zeddelivery.com/api/POD/UploadPodImage?";
//
//                addNewOrder(apirul + "filename=" + filename + "&img=" + base64file + "&imagTag=" + imagTag + "&OrderID=" + OrderID + "&PODImage=" + filename
//                        + "&POB=''" + "&CompanyID=" + CompanyID + "&CompanySiteID=" + CompanySiteID + "&CreatedOn=" + CreatedOn + "&CreatedBy=" + CreatedBy);

        }



    private void addNewOrder(String url) {
        Timber.d("urlis:" + url);
    }

    private File readFileFromInternalStorage(String image_name) {
        File mypath = new File(android.os.Environment.getExternalStorageDirectory() + "/ZEDDelivery/" + image_name);
        return mypath;
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
                Intent intent = new Intent(AddImages.this, NewUserActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
                break;
            }
            case R.id.actionSyncCompanyCustomerInfo: {
                if (ConnectionDetector.isConnectingToInternet(AddImages.this)) {
                    startService(new Intent(AddImages.this, CompanyInfoService.class));
                    Utility.Toast(AddImages.this, "Syncing Started...");
                } else {
                    Utility.Toast(AddImages.this, "Check network connection and try again");
                }
                break;
            }
            case R.id.actionAddSalesOrder: {
                Intent intent = new Intent(AddImages.this, TakeOrder.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
                break;
            }
            case R.id.actionSettings: {
                Intent intent = new Intent(AddImages.this, SettingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            }
            case R.id.actionAddSalesReturn: {

                Intent intent = new Intent(AddImages.this, ReturnOrderSearchActivity.class);
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
                    GPSTracker gps = new GPSTracker(AddImages.this);
                    if (ConnectionDetector.isConnectingToInternet(AddImages.this)) {
                        if (gps.canGetLocation()) {
                            Utility.Toast(AddImages.this, "Location Enable Successfully");
                            startservice();
                        } else {
                            Utility.Toast(AddImages.this, "Enable your GPS first and try again..");
                            //gps.showSettingsAlert();
                        }
                    } else
                        Utility.Toast(AddImages.this, "Check network connection and try again");
                    break;
                } else {
                    SpannableString spanString = new SpannableString("Enable Location");
                    spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0); //fix the color to white
                    item.setTitle(spanString);

                    Utility.Toast(AddImages.this, "Location Disable Successfully");
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    @Override
    public void onBackPressed() {
        if (zoomImgView.getVisibility() == View.VISIBLE) {
            zoomImgView.setVisibility(View.GONE);
            PodGridView.setVisibility(View.VISIBLE);
        } else {
            finish();
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
        }
    }

    private void selectOptions(final int position, final View view, final String imagtage) {
        final CharSequence[] options = {"Delete", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddImages.this);
        builder.setTitle("Select Option!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Delete")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddImages.this);
                    builder.setMessage("Are you sure you want to delete ?");
                    builder.setTitle("Alert");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int id = 0;
                            if (imagtage.equals("pod")) {
                                id = pod_imaglist.get(position).getImage_id();
                                if (db.deleteImage(id) == 1) {
                                    deleteFromGallery(pod_imaglist.get(position).getImage_name());
                                    fillGridview();
                                    fillPOBGridview();
                                } else {
                                    Utility.Toast(AddImages.this, "Image Cannot be Deleted");
                                }
                            } else {
                                id = pob_imaglist.get(position).getImage_id();
                                if (db.deleteImage(id) == 1) {
                                    deleteFromGallery(pob_imaglist.get(position).getImage_name());
                                    fillGridview();
                                    fillPOBGridview();
                                } else {
                                    Utility.Toast(AddImages.this, "Image Cannot be Deleted");
                                }
                            }

                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                } else if (options[item].equals("Zoom")) {
                    /*imgGrid.setVisibility(View.GONE);
                    largeLayout.setVisibility(View.VISIBLE);
                    largeView.setImageDrawable(((ImageView) view.findViewById(R.id.img)).getDrawable());*/
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File myDirectory = new File(Environment.getExternalStorageDirectory(), "ZEDDelivery");
        if (!myDirectory.exists()) {
            myDirectory.mkdirs();
        }
        String imag_name = System.currentTimeMillis() + ".jpg";
        File destination = new File(myDirectory, imag_name);
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }


        ImagesModel model = new ImagesModel();
        model.setImage_name(imag_name);
        model.setImage_order_id(orderId);
        if (imagType.equals("POD")) {
            model.setImage_tag("pod");
        } else {
            model.setImage_tag("pob");
        }
        model.setImag_is_synced("0");
        int j = db.insertImages(model);
        fillGridview();
        fillPOBGridview();
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Uri selectedImageUri = data.getData();
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(selectedImageUri, projection, null, null,
                null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        String selectedImagePath = cursor.getString(column_index);

        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 200;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(selectedImagePath, options);

        File myDirectory = new File(Environment.getExternalStorageDirectory(), "ZEDDelivery");
        if (!myDirectory.exists()) {
            myDirectory.mkdirs();
        }
        String imag_name = System.currentTimeMillis() + ".jpg";
        File destination = new File(myDirectory, imag_name);

        try {
            FileOutputStream out = new FileOutputStream(destination);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ImagesModel model = new ImagesModel();
        model.setImage_name(imag_name);
        model.setImage_order_id(orderId);
        if (imagType.equals("POD")) {
            model.setImage_tag("pod");
        } else {
            model.setImage_tag("pob");
        }
        model.setImag_is_synced("0");
        int j = db.insertImages(model);
        fillGridview();
        fillPOBGridview();

    }


    private void deleteFromGallery(String imageName) {
        File file = new File(android.os.Environment.getExternalStorageDirectory() + "/ZEDDelivery/" + imageName);
        if (file.exists()) {
            file.delete();
            Utility.logCatMsg("Image " + imageName + " deleted from directory");
        } else
            Utility.logCatMsg("File not Exist");
    }


    private void fillGridview() {
        pod_imaglist.clear();
        pod_imaglist = db.getImages(orderId, "pod");
        Pod_adapter = new GridAdapter(AddImages.this, pod_imaglist);
        PodGridView.setAdapter(Pod_adapter);
        if (pod_imaglist.size() > 0) {
            noPodTV.setVisibility(View.GONE);
        } else {
            noPodTV.setVisibility(View.VISIBLE);
        }
    }

    private void GrabImagesNames(){
        GrabImagesNamesForSyncing = db.getImagesNames(1,orderId);
        ImagesNames = GrabImagesNamesForSyncing.get(0).getImage_name();

        for(int i=1; i<GrabImagesNamesForSyncing.size(); i++)
        {
           ImagesNames = ImagesNames+"," +GrabImagesNamesForSyncing.get(i).getImage_name();
        }

        SendPod.rImagesNames = ImagesNames;


    }

    private void fillPOBGridview() {
        pob_imaglist.clear();
        pob_imaglist = db.getImages(orderId, "pob");
        pob_adapter = new GridAdapter(AddImages.this, pob_imaglist);
        pobGridView.setAdapter(pob_adapter);
        if (pob_imaglist.size() > 0) {
            noPobTV.setVisibility(View.GONE);
        } else {
            noPobTV.setVisibility(View.VISIBLE);
        }
    }


    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddImages.this);
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



    private void setUpActionBar(ActionBar actionBar) {
        LinearLayout mainLayout = findViewById(R.id.mainLayout);
        View v = getLayoutInflater().inflate(R.layout.actionbar_view, mainLayout, false);
        btnMenu = v.findViewById(R.id.btnMenu);
        btnMenu.setVisibility(View.GONE);
        TextView title = v.findViewById(R.id.actionBarTextView);

        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.appbluegrey)));
        actionBar.setCustomView(v);
        title.setText("Order Images");
    }

    @Override
    public void hOnDataRetrieved(int callType, boolean isValidResponse, Object response) {
        switch (callType) {
            case H_UPLOAD_IMAGE:
                if (isValidResponse) {
                    Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
//                    ImageRespone imageRespone = (ImageRespone) response;
//                    Timber.d("Imagme response data is %s  and image response message is %s", imageRespone.getData(), imageRespone.getMessage());
                } else {
                    Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show();
                }


                break;

        }

    }

    public class CheckableLayout extends FrameLayout implements Checkable {
        private boolean mChecked;

        public CheckableLayout(Context context) {
            super(context);
        }

        @SuppressWarnings("deprecation")
        public void setChecked(boolean checked) {
            mChecked = checked;
            setBackgroundDrawable(checked ? getResources().getDrawable(R.drawable.ic_menu) : null);
        }

        public boolean isChecked() {
            return mChecked;
        }

        public void toggle() {
            setChecked(!mChecked);
        }

    }

    public class MultiChoiceModeListener implements
            GridView.MultiChoiceModeListener {
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.setTitle("Select Items");
            mode.setSubtitle("One item selected");
            return true;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return true;
        }

        public void onDestroyActionMode(ActionMode mode) {
        }

        public void onItemCheckedStateChanged(ActionMode mode, int position,
                                              long id, boolean checked) {
            int selectCount = PodGridView.getCheckedItemCount();
            switch (selectCount) {
                case 1:
                    mode.setSubtitle("One item selected");
                    break;
                default:
                    mode.setSubtitle("" + selectCount + " items selected");
                    break;
            }
        }

    }

    private void runTimePermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                runTimePermission();
            }
        }
    }


}
