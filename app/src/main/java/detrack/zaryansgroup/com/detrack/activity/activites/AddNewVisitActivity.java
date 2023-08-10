package detrack.zaryansgroup.com.detrack.activity.activites;
import static detrack.zaryansgroup.com.detrack.R.color.green;
import static detrack.zaryansgroup.com.detrack.R.color.primary_dark_material_dark;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import detrack.zaryansgroup.com.detrack.R;
import detrack.zaryansgroup.com.detrack.activity.Adapter.CustomerDilogListAdapter;
import detrack.zaryansgroup.com.detrack.activity.Map.GPSTracker;
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyRouteModel.RouteModel;
import detrack.zaryansgroup.com.detrack.activity.Model.CustomerVisitedModel;
import detrack.zaryansgroup.com.detrack.activity.Model.RegisterCustomerModel.RegisterdCustomerModel;
import detrack.zaryansgroup.com.detrack.activity.Model.VisitStatusesModel;
import detrack.zaryansgroup.com.detrack.activity.SQLlite.DBHelper;
import detrack.zaryansgroup.com.detrack.activity.SQLlite.ZEDTrackDB;
import detrack.zaryansgroup.com.detrack.activity.SharedPreferences.SharedPrefs;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import detrack.zaryansgroup.com.detrack.activity.viewmodels.SelectCustomer_ViewModel;
import timber.log.Timber;


public class AddNewVisitActivity extends AppCompatActivity {

    GPSTracker tracker;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    LocationManager lm;

    Double longitude;
    Double latitude;

    int REQUEST_CAMERA = 0, SELECT_FILE = 1;


    SharedPrefs prefs;
    ZEDTrackDB db;
    ProgressDialog progressDialog;
    SelectCustomer_ViewModel mSelectCustomer_viewModel;

    boolean rVisitInsertionCheck;


    final Calendar myCalendar = Calendar.getInstance();

    Spinner   spRouteName;
    Spinner   spStatus;
    Spinner   spCustomerName;
    int       spinnerPostion;
    EditText  et_chooseDate, et_chooseTime;
    EditText  etSearchCustomer;
    TextView  tv_imageStatus;
    ImageView rIVChooseImage;


    ArrayList<RegisterdCustomerModel> rFilteredNameList;
    ArrayList<RouteModel> rRegisteredRoutesList;
    ArrayList<RegisterdCustomerModel> rRegisteredCustomersList;

    RegisterdCustomerModel startModel;
    ArrayList<VisitStatusesModel> rAllVisitedStatusList;
    List<String> rTempVisitCustomerList;
    List<String> RouteNameList;
    CustomerDilogListAdapter adapter;

    Button btnSave;



    String rSelectedDate, rSelectedTime, rSelectedCustomerName, rSelectedCustomerRoute, rSelectedCustomerStatus;
    int rSelectedStatusId, rSelectedRouteID, rSelectedCustomerID, CompanyID, CompanySiteID, SalesManID;
    int isSync = 0;

    String imag_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_visit);
        getSupportActionBar().setTitle("Add Visit");


        tracker = new GPSTracker(this);
        lm = (LocationManager) getSystemService(AddNewVisitActivity.LOCATION_SERVICE);

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {
            Timber.d("Gps Exception is "+ex.getMessage()+"");
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {
            Timber.d("Network Exception is "+ex.getMessage()+"");
        }

        if(!gps_enabled && !network_enabled) {
            // notify user
           Timber.d("Location is not turned on");
        }


        prefs = new SharedPrefs(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Wait");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Saving to Local Database");


        mSelectCustomer_viewModel = new ViewModelProvider(this,
                (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(SelectCustomer_ViewModel.class);

        startModel = new RegisterdCustomerModel();

        rAllVisitedStatusList = new ArrayList<>();
        rFilteredNameList = new ArrayList<>();
        rRegisteredRoutesList = new ArrayList<>();
        rRegisteredCustomersList = new ArrayList<>();
        rTempVisitCustomerList = new ArrayList<String>();
        RouteNameList = new ArrayList<>();

        db = new ZEDTrackDB(this);

//        rRegisteredCustomersList = db.getSQLiteRegisterCustomerInfo(null);


        initXml();
        runTimePermission();
        rCollectPreferencesData();
        SpinnerDataObserver();
        SpinnersListner();
        fillSpinner();
        rInitDate();
        rInitTime();
        rFillStatusSpinner();
        rCollectLatLong();


        rIVChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


        etSearchCustomer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence newText, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable newText) {
                rFilteredNameList.clear();
                rCustomersFilter(newText);
            }
        });



        spStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rSelectedStatusId = rAllVisitedStatusList.get(position).getStatusID();
                rSelectedCustomerStatus = rAllVisitedStatusList.get(position).getVisitStatus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (rSelectedRouteID == 0) {
                    Toast.makeText(AddNewVisitActivity.this, "Customer and Route Selection is Required", Toast.LENGTH_SHORT).show();
                } else {

                    progressDialog.show();
                    int LastRowId = db.getLastRowId(DBHelper.TBL_VisitedDetails);

                    if (LastRowId == -1) {
                        LastRowId = 1;
                    } else {
                        LastRowId = LastRowId + 1;
                    }

                    CustomerVisitedModel model = new CustomerVisitedModel();

                    model.setId(LastRowId);
                    model.setIsSync(isSync);
                    model.setCompanyID(CompanyID);
                    model.setCompanySiteID(CompanySiteID);
                    model.setRouteID(rSelectedRouteID);
                    model.setCustomerId(rSelectedCustomerID);
                    model.setVisitDate(et_chooseDate.getText().toString());
                    model.setVisitTime(et_chooseTime.getText().toString());
                    model.setStatusID(rSelectedStatusId);
                    model.setSalesmanID(SalesManID);
                    model.setVisitStatus(rSelectedCustomerStatus);
                    model.setRouteName(rSelectedCustomerRoute);
                    model.setCustomerName(rSelectedCustomerName);
                    model.setLatitude(latitude.toString());
                    model.setLongitude(longitude.toString());

                    if(imag_name!=null)
                    {
                        model.setImageName(imag_name);
                    }else{
                        model.setImageName("");
                    }

                    rVisitInsertionCheck = db.rInsertCurrentCustomerVisit(model);

                    if (rVisitInsertionCheck == true) {
                        progressDialog.dismiss();
                        Toast.makeText(AddNewVisitActivity.this, "Visit Saved Successfully", Toast.LENGTH_SHORT).show();
                        spRouteName.setSelection(0);
                        rIVChooseImage.setImageResource(R.drawable.ic_addimg);
                        tv_imageStatus.setText("Choose Image");
                        tv_imageStatus.setTextColor(ContextCompat.getColor(AddNewVisitActivity.this, primary_dark_material_dark));
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(AddNewVisitActivity.this, "Visit not Saved, Please Try again", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });


        et_chooseTime.setOnClickListener(v -> {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            String ampm = DateUtils.getAMPMString(mcurrentTime.get(Calendar.AM_PM));
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(AddNewVisitActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    et_chooseTime.setText(selectedHour + ":" + selectedMinute + " " + ampm);
                }
            }, hour, minute, true);

            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        });


        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };



        et_chooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddNewVisitActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

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



    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddNewVisitActivity.this);
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
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }


    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        rIVChooseImage.setImageBitmap(thumbnail);
        tv_imageStatus.setText("Image has been Selected");
        tv_imageStatus.setTextColor(ContextCompat.getColor(AddNewVisitActivity.this, green));


        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File myDirectory = new File(Environment.getExternalStorageDirectory(), "ZEDDelivery");
        if (!myDirectory.exists()) {
            myDirectory.mkdirs();
        }
        imag_name = System.currentTimeMillis() + ".jpg";
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
            Timber.d("Exception is here "+e.getMessage());
        }

    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Uri selectedImageUri = data.getData();

        rIVChooseImage.setImageURI(selectedImageUri);
        tv_imageStatus.setText("Image has been Selected");
        tv_imageStatus.setTextColor(ContextCompat.getColor(AddNewVisitActivity.this, green));

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
        imag_name = System.currentTimeMillis() + ".jpg";
        File destination = new File(myDirectory, imag_name);

        try {

            FileOutputStream out = new FileOutputStream(destination);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {

            Timber.d("Exception is here "+e.getMessage());
            e.printStackTrace();
        }


    }



    private void rCollectLatLong() {

            if (!tracker.canGetLocation()) {
                Toast.makeText(AddNewVisitActivity.this, "Please Turn On Location First", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddNewVisitActivity.this,WelcomeActivity.class);
                startActivity(intent);
            } else {
                latitude = tracker.getLatitude();
                longitude = tracker.getLongitude();
            }
    }


    private void rCustomersFilter(Editable newText) {

        for(int i = 0; i< rRegisteredCustomersList.size(); i++){
            if(
                    rRegisteredCustomersList.get(i).getName().trim().toLowerCase(Locale.ROOT).contains(newText)
              )
            {

                rFilteredNameList.add(rRegisteredCustomersList.get(i));
            }
        }


        this.adapter.rSearchCustomers(rFilteredNameList);
    }

    private void fillSpinner() {
        mSelectCustomer_viewModel.getSqlRouteList();

    }

    private void SpinnerDataObserver(){

        mSelectCustomer_viewModel.getSqlroutelist().observe(this, new Observer<ArrayList<RouteModel>>() {
            @Override
            public void onChanged(ArrayList<RouteModel> data) {
                rRegisteredRoutesList = data;

                final List<String> RouteNameList = new ArrayList<String>();

                RouteNameList.add("Select Route");

                for (int i = 0; i < rRegisteredRoutesList.size(); i++) {
                    RouteNameList.add(rRegisteredRoutesList.get(i).getRoute_name());
                }

                ArrayAdapter<String> adapter_routeName = new ArrayAdapter<String>(AddNewVisitActivity.this,
                        android.R.layout.simple_spinner_item, RouteNameList);
                adapter_routeName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spRouteName.setAdapter(adapter_routeName);
            }
        });

        mSelectCustomer_viewModel.getSqlCustomerinfo().observe(this, new Observer<ArrayList<RegisterdCustomerModel>>() {
            @Override
            public void onChanged(ArrayList<RegisterdCustomerModel> registerdCustomerModels) {

                rRegisteredCustomersList = registerdCustomerModels;
                adapter = new CustomerDilogListAdapter(AddNewVisitActivity.this, rRegisteredCustomersList);
                spCustomerName.setAdapter(adapter);
            }
        });
    }

    private void SpinnersListner() {

        spRouteName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                rRegisteredCustomersList.clear();
                spinnerPostion = position;


                if(spinnerPostion == 0)
                {
                    rSelectedRouteID = 0;

                }
                else {
                    rSelectedRouteID = rRegisteredRoutesList.get(spinnerPostion-1).getRoute_id();
                    rSelectedCustomerRoute = rRegisteredRoutesList.get(spinnerPostion-1).getRoute_name();
                }

                if(rSelectedRouteID == 0)
                {
                    mSelectCustomer_viewModel.getSqlCustomerInfo( null);
                }
                else{
                    mSelectCustomer_viewModel.getSqlCustomerInfo(rSelectedRouteID+"");
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spCustomerName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                RegisterdCustomerModel model = rRegisteredCustomersList.get(position);

                rSelectedCustomerName = model.getName();
                rSelectedCustomerID   =   model.getCustomer_id();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }




    private void rCollectPreferencesData() {
        CompanyID = Integer.parseInt(prefs.getCompanyID());
        CompanySiteID = Integer.parseInt(prefs.getCompanySiteID());
        SalesManID = Integer.parseInt(String.valueOf(prefs.getEmployeeID()));
    }


    private void rFillStatusSpinner() {

        rAllVisitedStatusList = db.rGetVisitStatusList();

        final List<String> rTempVisitStatusList = new ArrayList<String>();
        for (int i = 0; i < rAllVisitedStatusList.size(); i++) {
            rTempVisitStatusList.add(rAllVisitedStatusList.get(i).getVisitStatus());
        }

        ArrayAdapter<String> adapter_visited_status = new ArrayAdapter<String>(AddNewVisitActivity.this,
                android.R.layout.simple_spinner_item, rTempVisitStatusList);
        adapter_visited_status.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spStatus.setAdapter(adapter_visited_status);


    }


    private void rInitTime() {

        rSelectedTime = Utility.getTime();
        et_chooseTime.setText(rSelectedTime);
    }

    private void rInitDate() {
        rSelectedDate = getCurrentDate();
        et_chooseDate.setText(rSelectedDate+"");
    }

    private String getCurrentDate() {
        String currentDate;
        currentDate  = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        return currentDate;
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        et_chooseDate.setText(sdf.format(myCalendar.getTime()));
    }


    private void initXml() {
        spRouteName = findViewById(R.id.spRouteName);
        spCustomerName = findViewById(R.id.spvCustomerName);
        btnSave = findViewById(R.id.btnNext);
        spStatus= findViewById(R.id.spStatus);
        et_chooseDate = findViewById(R.id.et_chooseDate);
        et_chooseTime = findViewById(R.id.et_chooseTime);
        etSearchCustomer = findViewById(R.id.etSearchCustomer);
        tv_imageStatus = findViewById(R.id.tv_imageStatus);
        rIVChooseImage = findViewById(R.id.btnUploadImage);

    }


}