package detrack.zaryansgroup.com.detrack.activity.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import detrack.zaryansgroup.com.detrack.R;
import detrack.zaryansgroup.com.detrack.activity.Model.CustomerVisitedModel;
import detrack.zaryansgroup.com.detrack.activity.Model.RegisterCustomerModel.RegisterdCustomerModel;
import detrack.zaryansgroup.com.detrack.activity.SQLlite.ZEDTrackDB;
import timber.log.Timber;


public class MapViewFragment extends Fragment {

    private GoogleMap hGoogleMap;
    private List<RegisterdCustomerModel> rAllCustomersList;
    private List<CustomerVisitedModel> rAllVisitedCustomerTodayList;

    private List<MapModel> hMapModelList;
    ZEDTrackDB hZedTrackDB;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rAllCustomersList = new ArrayList<>();
        rAllVisitedCustomerTodayList = new ArrayList<>();
        hMapModelList = new ArrayList<>();
        hZedTrackDB = new ZEDTrackDB(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.visit_markers_map_fragment, container, false);
        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        rCollectCustomersFromLocalDb();
        hInitMapView();


    }


    private void rCollectCustomersFromLocalDb() {
        rAllCustomersList = hZedTrackDB.getAllRegisterCustomers();
        rAllVisitedCustomerTodayList = hZedTrackDB.getAllVisitDetails();

        rAddVistsToMapModelList(rAllVisitedCustomerTodayList);
        rAddCustomersToMapModelList(rAllCustomersList);



    }

    private void rAddVistsToMapModelList(List<CustomerVisitedModel> rAllVisitedCustomerTodayList) {

        List<MapModel> mapList2 = new ArrayList<>();
        int runTill = rAllVisitedCustomerTodayList.size();

        for(int i=0;i<runTill;i++)
        {

            MapModel hMapModel = new MapModel();
            hMapModel.setCustomerId( rAllVisitedCustomerTodayList.get(i).getCustomerId());
            hMapModel.setCustomerName(rAllVisitedCustomerTodayList.get(i).getCustomerName());
            hMapModel.setVisitStatus("Visited");
            String hLat = rAllVisitedCustomerTodayList.get(i).getLatitude();
            String hLng = rAllVisitedCustomerTodayList.get(i).getLongitude();

            if ((hLat != null && !hLat.isEmpty())
                    &&
                    (hLng != null && !hLng.isEmpty()))
            {
                hMapModel.sethLatLng(
                        new LatLng(
                                Double.parseDouble(hLng),
                                Double.parseDouble(hLat)
                        ));
            }


            mapList2.add(hMapModel);

        }

        hMapModelList.addAll(mapList2);


    }

    private void rAddCustomersToMapModelList(List<RegisterdCustomerModel> rAllCustomersList) {

        rRemovCustomersFromListThatAreVisted(rAllCustomersList);

        List<MapModel> mapList1 = new ArrayList<>();
        for (RegisterdCustomerModel registerdCustomerModel : rAllCustomersList) {

            MapModel hMapModel = new MapModel();
            hMapModel.setCustomerId(registerdCustomerModel.getCustomer_id());
            hMapModel.setCustomerName(registerdCustomerModel.getName());
            String hLat = registerdCustomerModel.getLat();
            String hLng = registerdCustomerModel.getLng();
            if (
                    (hLat != null && !hLat.isEmpty())
                            && (hLng != null && !hLng.isEmpty())
            ) {
                hMapModel.sethLatLng(
                        new LatLng(
                                Double.parseDouble(hLng),
                                Double.parseDouble(hLat)

                        ));

            }

            hMapModel.setVisitStatus("notVisited");
            mapList1.add(hMapModel);
        }

        hMapModelList.addAll(mapList1);
    }

    private void rRemovCustomersFromListThatAreVisted(List<RegisterdCustomerModel> rAllCustomersList) {
        
        Timber.d("Customers List Before "+rAllCustomersList.size()+"");
        
        try {
            for(int i=0; i<rAllCustomersList.size();i++)
            {
                for(int j=0; j< hMapModelList.size(); j++){
                    if(rAllCustomersList.get(i).getCustomer_id() == hMapModelList.get(i).getCustomerId()){
                        rAllCustomersList.remove(rAllCustomersList.get(i));
                    }
                }
            }
        }catch (Exception e)
        {
            Timber.d("E get message"+e.getMessage());
        }

        Timber.d("Customers List after "+rAllCustomersList.size()+"");

    }


    private void hInitMapView() {

        SupportMapFragment hMapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapView);
        hMapFragment.getMapAsync(googleMap -> {

            hGoogleMap = googleMap;

            for (int i = 0; i < hMapModelList.size(); i++) {

                Timber.d("--------------------------");
                Timber.d("Name is "+hMapModelList.get(i).getCustomerName());
                Timber.d("Status is "+hMapModelList.get(i).getVisitStatus());
                Timber.d("latlng is "+hMapModelList.get(i).gethLatLng());
                Timber.d("---------------------------");

//                if(hMapModelList.get(i).getVisitStatus()!=null && hMapModelList.get(i).gethLatLng()!=null) {
//
//                    if (
//                            hMapModelList.get(i).getVisitStatus().equalsIgnoreCase("notVisited")
//                    ) {
//                            MarkerOptions markerOptions = new MarkerOptions();
//                            markerOptions.position(hMapModelList.get(i).gethLatLng());
//                            markerOptions.title(hMapModelList.get(i).getCustomerName()+"\n"+hMapModelList.get(i).getVisitStatus());
//                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//                            hGoogleMap.addMarker(markerOptions);
//
//                    } else {
//
//
//                            MarkerOptions markerOptions1 = new MarkerOptions();
//                            markerOptions1.position(hMapModelList.get(i).gethLatLng());
//                            markerOptions1.title(hMapModelList.get(i).getCustomerName()+"\n"+hMapModelList.get(i).getVisitStatus());
//                            markerOptions1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//                            hGoogleMap.addMarker(markerOptions1);
//                    }
//                }
//                else{
//
//                    Toast.makeText(getContext(), "Some Customers Location not Found", Toast.LENGTH_SHORT).show();
//                }

            }


            LatLng mapPointerOnStart = new LatLng(33.6844,73.0479 );

            CameraPosition cameraPosition = new CameraPosition.Builder().zoom(12).target(mapPointerOnStart).build();
            hGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        });

    }


    private MarkerOptions hGetMarkerOptions(MapModel mapModel) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(mapModel.gethLatLng());
        markerOptions.title(mapModel.getCustomerName());
        if (mapModel.getVisitStatus().equalsIgnoreCase("notVisited")) {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        } else {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        }
        return markerOptions;
    }


}
