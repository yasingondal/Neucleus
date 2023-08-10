package detrack.zaryansgroup.com.detrack.activity.activites;

import android.Manifest;
import androidx.appcompat.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import detrack.zaryansgroup.com.detrack.activity.Map.DirectionsJSONParser;
import detrack.zaryansgroup.com.detrack.activity.Map.GPSTracker;
import detrack.zaryansgroup.com.detrack.activity.Model.MapModel;
import detrack.zaryansgroup.com.detrack.activity.SQLlite.ZEDTrackDB;
import detrack.zaryansgroup.com.detrack.activity.utilites.ConnectionDetector;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;
import detrack.zaryansgroup.com.detrack.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap map;
    ArrayList<LatLng> markerPoints;
    TextView tvDistanceDuration;
    LatLng current_latlng, dest_latlng;
    ProgressBar pb;
    ArrayList<MapModel> mapModel;
    ZEDTrackDB db;
    String lat = "0.0", lng = "0.0", Delivery_id, IsNew;
    Button tryagainBtn;
    MapModel deliveryMapInfo;
    ImageButton btnMenu;
    ListView menuList;
    CharSequence[] map_option = {"Normal", "Satellite", "Terrain", "Hybrid", "Cancel"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        markerPoints = new ArrayList<LatLng>();
        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        pb = (ProgressBar) findViewById(R.id.progressBar);
        tryagainBtn = (Button) findViewById(R.id.tryagainBtn);
        fm.getMapAsync(this);
        mapModel = new ArrayList<>();
        db = new ZEDTrackDB(MapsActivity.this);
        deliveryMapInfo = new MapModel();

        Delivery_id = getIntent().getStringExtra("Delivery_id");
        IsNew = getIntent().getStringExtra("IsNew");

        Utility.logCatMsg("Delivery_id :" + Delivery_id);
        Utility.logCatMsg("IsNew :" + IsNew);


        deliveryMapInfo = db.getSQLiteOrderDeliveryMapLatLng(Integer.parseInt(Delivery_id));
//        setUpActionBar(getSupportActionBar());
        //todo supported action bar
//        setUpActionBar(((AppCompatActivity) fm.getActivity()).getSupportActionBar());
        // Enable MyLocation Button in the Map

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        setMenuList();

        tryagainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLatLong();

            }
        });

    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Utility.logCatMsg("Exception while downloading url" + e.toString());

        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private void setMarker(String lat, String lng, String title) {
        map.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng))).title(title));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setMyLocationEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setAllGesturesEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setRotateGesturesEnabled(true);
        showOnlyCustomerLocation();


    }


    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data

            parserTask.execute(result);

        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String duration = "";


            if (result.size() < 1) {
                Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) {    // Get distance from the summeryList
                        distance = (String) point.get("distance");
                        continue;
                    } else if (j == 1) {
                        duration = (String) point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(3);
                lineOptions.color(Color.RED);

            }
            map.addPolyline(lineOptions);
            pb.setVisibility(View.GONE);
        }
    }

    private void setUpActionBar(ActionBar actionBar) {
        FrameLayout mainLayout = (FrameLayout) findViewById(R.id.mainLayout);
        View v = getLayoutInflater().inflate(R.layout.actionbar_view, mainLayout, false);
        btnMenu = (ImageButton) v.findViewById(R.id.btnMenu);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.appbluegrey)));
        actionBar.setCustomView(v);
        ((TextView) v.findViewById(R.id.actionBarTextView)).setText("Map");
    }

    private void getLatLong() {
        markerPoints.clear();
        map.clear();
        if (ConnectionDetector.isConnectingToInternet(MapsActivity.this)) {
            GPSTracker gps = new GPSTracker(MapsActivity.this);
            // check if GPS enabled
            if (gps.canGetLocation()) {
                lat = gps.getLatitude() + "";
                lng = gps.getLongitude() + "";
                Utility.logCatMsg("current Lat" + gps.getLatitude() + " Current Log" + gps.getLatitude());
                if (lat.equals("0.0") && lng.equals("0.0")) {

                    Utility.Toast(MapsActivity.this, "Getting Error to find Current Location Plz Try again..");
                } else {
                    current_latlng = new LatLng(gps.getLatitude(), gps.getLongitude());
                    lat = lat.substring(0, 5);
                    Utility.logCatMsg("Current Split Lat " + lat);
                    lng = lng.substring(0, 5);
                    Utility.logCatMsg("Current Split Lng " + lng);
                    tryagainBtn.setVisibility(View.INVISIBLE);
                }

            } else {
                gps.showSettingsAlert();
                tryagainBtn.setVisibility(View.VISIBLE);
            }
            map.animateCamera(CameraUpdateFactory.zoomBy(15), 1000, null);
            map.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(gps.getLatitude(), gps.getLongitude())), 2000, null);// Current Loction
        } else {
            Utility.Toast(MapsActivity.this, "Check your internet connection and try again");
            tryagainBtn.setVisibility(View.VISIBLE);
        }

    }

    private void NearByMarketPlace() {
        getLatLong();
        mapModel.clear();
        if (!lat.equals("0.0") && !lng.equals("0.0")) {
            mapModel = db.getSQLiteOrderDeliveryMapInfo(lat, lng, IsNew);
            for (int i = 0; i < mapModel.size(); i++) {
                setMarker(mapModel.get(i).getLat(), mapModel.get(i).getLng(), mapModel.get(i).getName());
            }
            if (mapModel.size() == 0) {
                Utility.Toast(MapsActivity.this, "No Delivery Place Found..");
            }
        }

    }

    private void AllDelvieryPlaces() {
        mapModel.clear();
        mapModel = db.getSQLiteALLOrderDeliveryMapInfo(IsNew);
        tryagainBtn.setVisibility(View.INVISIBLE);
        for (int i = 0; i < mapModel.size(); i++) {
            if (!mapModel.get(i).getLat().equals("") && !mapModel.get(i).getLng().equals("")) {
                if (mapModel.get(i).getDelivery_id() == Integer.parseInt(Delivery_id)) {    // change marker of current customer later
                    setMarker(mapModel.get(i).getLat(), mapModel.get(i).getLng(), mapModel.get(i).getName());

                } else {
                    setMarker(mapModel.get(i).getLat(), mapModel.get(i).getLng(), mapModel.get(i).getName());
                }
            }
        }
        if (mapModel.size() == 0) {
            Utility.Toast(MapsActivity.this, "No Delivery Place Found..");
        }
    }

    private void from_current_to_customer_add() {
        getLatLong();

        if (lat.equals("0.0") && lng.equals("0.0")) {

            Utility.Toast(MapsActivity.this, "Getting Error to find Current Location Plz Try again..");
        } else {
            if (deliveryMapInfo != null) {
                if (!deliveryMapInfo.getLat().equals("") && !deliveryMapInfo.getLng().equals("")) {
                    Utility.logCatMsg("Lat " + deliveryMapInfo.getLat().toString() + "Lng " + deliveryMapInfo.getLng().toString());
                    double latitude = Double.parseDouble(deliveryMapInfo.getLat().toString());
                    double longitude = Double.parseDouble(deliveryMapInfo.getLng().toString());
                    dest_latlng = new LatLng(latitude, longitude);

                    markerPoints.add(current_latlng);
                    markerPoints.add(dest_latlng);
                    pb.setVisibility(View.VISIBLE);
                    MarkerOptions options = new MarkerOptions();
                    options.position(dest_latlng);
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    map.addMarker(options);
                    LatLng origin = markerPoints.get(0);
                    LatLng dest = markerPoints.get(1);
                    // Getting URL to the Google Directions API
                    String url = getDirectionsUrl(origin, dest);
                    DownloadTask downloadTask = new DownloadTask();
                    // Start downloading json data from Google Directions API
                    downloadTask.execute(url);
                } else {
                    Utility.Toast(MapsActivity.this, "No Location Found");
                }
            }
        }

    }

    private void showOnlyCustomerLocation() {
        markerPoints.clear();
        map.clear();
        tryagainBtn.setVisibility(View.INVISIBLE);

        if (deliveryMapInfo != null && !deliveryMapInfo.getLat().equals("") && !deliveryMapInfo.getLng().equals("") && !deliveryMapInfo.getLat().equals("null") && !deliveryMapInfo.getLng().equals("null")) {

            double latitude = Double.parseDouble(deliveryMapInfo.getLat().toString());
            double longitude = Double.parseDouble(deliveryMapInfo.getLng().toString());

            Log.d("deliveryLatLng", latitude + "     " + longitude);

            LatLng userLocation = new LatLng(latitude, longitude);

            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
            map.moveCamera(center);
            map.animateCamera(zoom);

            setMarker(deliveryMapInfo.getLat().toString(), deliveryMapInfo.getLng().toString(), deliveryMapInfo.getName());

            /*MarkerOptions options = new MarkerOptions();
            options.position(userLocation);
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
           // map.addMarker(options);*/
        } else {
            Utility.Toast(MapsActivity.this, "No Location Found");
        }
    }

    private void setMenuList() {
        final String[] menuArray = new String[]{deliveryMapInfo.getName() + " Place", "Nearest Market Places", "All Market Places", "Show RouteModel", "Current Location", "Map Option"};
        menuList = (ListView) findViewById(R.id.menuList);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(MapsActivity.this, R.layout.menu_list_row, menuArray);
        menuList.setAdapter(adapter);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuList.getVisibility() != View.VISIBLE) {
                    menuList.setVisibility(View.VISIBLE);
                } else {
                    menuList.setVisibility(View.GONE);
                }
            }
        });

        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String option = ((TextView) view).getText().toString();
                menuList.setVisibility(View.GONE);
                if (option.equals(deliveryMapInfo.getName() + " Place")) {
                    showOnlyCustomerLocation();
                } else if (option.equals("Nearest Market Places")) {
                    NearByMarketPlace();
                } else if (option.equals("All Market Places")) {
                    AllDelvieryPlaces();
                } else if (option.equals("Current Location")) {
                    getLatLong();
                } else if (option.equals("Show RouteModel")) {
                    from_current_to_customer_add();
                } else if (option.equals("Map Option")) {
                    Map_Option_diloag();
                }
            }
        });
    }

    private void Map_Option_diloag() {
        menuList.setVisibility(View.GONE);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Option");
        builder.setItems(map_option, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (map_option[item].equals("Normal")) {
                    map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                } else if (map_option[item].equals("Satellite")) {
                    map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                } else if (map_option[item].equals("Terrain")) {
                    map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                } else if (map_option[item].equals("Hybrid")) {
                    map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                } else {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }
}
