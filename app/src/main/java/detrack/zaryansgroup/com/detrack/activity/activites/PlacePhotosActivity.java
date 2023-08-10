package detrack.zaryansgroup.com.detrack.activity.activites;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import detrack.zaryansgroup.com.detrack.R;

public class PlacePhotosActivity extends AppCompatActivity {

    public static final String TAG = "PlacePhotosActivity";
    private static final int PLACE_PICKER_REQ_CODE = 2;

    private GeoDataClient geoDataClient;
    private TextView placeName;
    private ImageView placeImage;
    private Button nextPhoto;
    private Button prevPhoto;

    private List<PlacePhotoMetadata> photosDataList;
    private int currentPhotoIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.placephotolayout);
//
//        Toolbar tb = findViewById(R.id.toolbar);
//        setSupportActionBar(tb);
//        tb.setSubtitle("Place Photos");

        placeName = findViewById(R.id.place_name);
        placeImage = findViewById(R.id.place_image);

        Button placePicker = findViewById(R.id.pick_place);
        placePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlacePicker();
            }
        });

        nextPhoto = findViewById(R.id.next);
        nextPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextPhoto();
            }
        });

        prevPhoto = findViewById(R.id.prev);
        prevPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prevPhoto();
            }
        });

        geoDataClient = Places.getGeoDataClient(this, null);

        showPlacePicker();
    }

    private void showPlacePicker() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQ_CODE);
        } catch (Exception e) {
            Log.e(TAG, e.getStackTrace().toString());
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQ_CODE && resultCode == RESULT_OK) {
            Place place =
                    PlacePicker.getPlace(this, data);
            placeName.setText(place.getName());
            Log.d(TAG, "selected place " + place.getName());
            getPhotoMetadata(place.getId());
        }
    }
    private void getPhotoMetadata(String placeId) {

        final Task<PlacePhotoMetadataResponse> photoResponse =
                geoDataClient.getPlacePhotos(placeId);

        photoResponse.addOnCompleteListener
                (new OnCompleteListener<PlacePhotoMetadataResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
                        currentPhotoIndex = 0;
                        photosDataList = new ArrayList<>();
                        PlacePhotoMetadataResponse photos = task.getResult();
                        PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();

                        Log.d(TAG, "number of photos "+photoMetadataBuffer.getCount());

                        for(PlacePhotoMetadata photoMetadata : photoMetadataBuffer){
                            photosDataList.add(photoMetadataBuffer.get(0).freeze());
                        }

                        photoMetadataBuffer.release();

                        displayPhoto();
                    }
                });
    }
    private void getPhoto(PlacePhotoMetadata photoMetadata){
        Task<PlacePhotoResponse> photoResponse = geoDataClient.getPhoto(photoMetadata);
        photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
                PlacePhotoResponse photo = task.getResult();
                Bitmap photoBitmap = photo.getBitmap();
                Log.d(TAG, "photo "+photo.toString());

                placeImage.invalidate();
                placeImage.setImageBitmap(photoBitmap);
            }
        });
    }
    private void nextPhoto(){
        currentPhotoIndex++;
        displayPhoto();
    }
    private void prevPhoto(){
        currentPhotoIndex--;
        displayPhoto();
    }
    private void displayPhoto(){
        Log.d(TAG, "index "+currentPhotoIndex);
        Log.d(TAG, "size "+photosDataList.size());
        if(photosDataList.isEmpty() || currentPhotoIndex > photosDataList.size() - 1){
            return;
        }
        getPhoto(photosDataList.get(currentPhotoIndex));
        setButtonVisibility();
    }
    private void setButtonVisibility(){
        if(currentPhotoIndex == 0){
            prevPhoto.setEnabled(false);
            if(photosDataList.size() < 2){
                nextPhoto.setEnabled(false);
            }
        }else{
            prevPhoto.setEnabled(true);
            nextPhoto.setEnabled(true);
            if(currentPhotoIndex == photosDataList.size()-1){
                nextPhoto.setEnabled(false);
            }
        }
    }
}