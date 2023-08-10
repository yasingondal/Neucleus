package detrack.zaryansgroup.com.detrack.activity.activites

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.directions.route.*
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.*
import detrack.zaryansgroup.com.detrack.R
import detrack.zaryansgroup.com.detrack.activity.Adapter.CustomerDilogListAdapter
import detrack.zaryansgroup.com.detrack.activity.Model.CompanyRouteModel.RouteModel
import detrack.zaryansgroup.com.detrack.activity.Model.GeoCodingAddress.GeoCodeResult
import detrack.zaryansgroup.com.detrack.activity.Model.GeocodeAddressModel
import detrack.zaryansgroup.com.detrack.activity.Model.RegisterCustomerModel.RegisterdCustomerModel
import detrack.zaryansgroup.com.detrack.activity.SQLlite.ZEDTrackDB
import detrack.zaryansgroup.com.detrack.activity.retrofit.Api_Reto
import detrack.zaryansgroup.com.detrack.activity.utilites.MapWrapperLayout
import detrack.zaryansgroup.com.detrack.activity.utilites.OnInfoWindowElemTouchListener
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility
import detrack.zaryansgroup.com.detrack.activity.viewmodels.SelectCustomer_ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList


class MapsActivity2 : AppCompatActivity(), OnMapReadyCallback, RoutingListener {

    private var mMap: GoogleMap? = null
    var markerPoints: ArrayList<LatLng>? = null
    private var mSelectCustomer_viewModel: SelectCustomer_ViewModel? = null
    lateinit var spRouteName: Spinner
    lateinit var spCustomerName: Spinner
    lateinit var li_customer: LinearLayout
    private var routelist = ArrayList<RouteModel>()
    private var hRegisterCustomerList = ArrayList<RegisterdCustomerModel>()
    private var spinnerPostion = 0
    var startModel: RegisterdCustomerModel? = null
    var db: ZEDTrackDB? = null
    lateinit var mapFragment: SupportMapFragment
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var isGpsEnabled: Boolean = false
    private var mLocationPermissionGranted: Boolean = false
    private var currenLocation: Location? = null

    private var infoTitle: TextView? = null
    private var infoSnippet: TextView? = null
    private var infoButton: Button? = null
    private var infoTitleListener: OnInfoWindowElemTouchListener? = null
    private lateinit var mapWrapperLayout: MapWrapperLayout
    private var infoWindow: ViewGroup? = null
    private val mapApiKey: String = "AIzaSyDqQ_qabCg4qIh92L4j7sMEw7PgsHhSbvU"
    private lateinit var placesClient: PlacesClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps2)
        mSelectCustomer_viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
        )[SelectCustomer_ViewModel::class.java]


        Places.initialize(applicationContext, mapApiKey)
// Create a new Places client instance.

// Create a new Places client instance.
        placesClient = Places.createClient(this)
        spRouteName = findViewById(R.id.spRouteName_map)
        spCustomerName = findViewById(R.id.spCustomerName_map)
        li_customer = findViewById(R.id.li_customer)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        mapWrapperLayout = findViewById(R.id.map_relative_layout);

        //initializing other objects
        routelist = ArrayList()
        db = ZEDTrackDB(this)
        hRegisterCustomerList = ArrayList()
        hRegisterCustomerList = db!!.getSQLiteRegisterCustomerInfo(null)
        startModel = RegisterdCustomerModel()
        startModel!!.name = "Select Customer"
        li_customer.visibility = GONE

        SpinnerDataObserver()
        lastLocation()
    }


    private fun lastLocation() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            mLocationPermissionGranted = false;
            return
        }
        mLocationPermissionGranted = true;

        val task: Task<Location> = fusedLocationClient.lastLocation
        task.addOnSuccessListener {

            if (it != null) {
                currenLocation = it;
//                Toast.makeText(this,""+ currenLocation?.getLatitude()+"" +
//                        " "+currenLocation?.getLongitude(),
//                        Toast.LENGTH_SHORT).show();

                val marker = setMarker(
                    currenLocation!!.latitude,
                    currenLocation!!.longitude,
                    "My Location",
                    "simple",
                )

                Timber.d("marker is $marker")

                animateCam(
                    LatLng(currenLocation!!.latitude, currenLocation!!.longitude),
                    15f,
                    true,
                    true
                )
            }
        }


    }

    private fun SpinnerDataObserver() {
        mSelectCustomer_viewModel!!.sqlroutelist.observe(this, { data ->
            routelist = data
            var fRouteList = ArrayList<String>()
            fRouteList.add("AllRoutes")
            for (i in routelist.indices) {
                fRouteList.add(routelist[i].route_name)
                Log.d(
                    "routelist_details",
                    "code=" + routelist.get(i).getRoute_code() + ": id=" + routelist.get(i)
                        .getRoute_id()
                )
                Utility.logCatMsg(
                    "Route List code " + routelist.get(i).getRoute_code() + " Route List Name " +
                            routelist.get(i).getRoute_name()
                )
            }
            val adapter_routeName = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, fRouteList
            )
            adapter_routeName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spRouteName.adapter = adapter_routeName


        })
        mSelectCustomer_viewModel!!.sqlCustomerinfo.observe(this, { registerdCustomerModels ->
            hRegisterCustomerList.clear()
            hRegisterCustomerList = registerdCustomerModels
            if (spinnerPostion == 0) {
                hRegisterCustomerList.add(0, startModel)
            }
            val adapter = CustomerDilogListAdapter(this, hRegisterCustomerList)
            spCustomerName.adapter = adapter

            if (spinnerPostion != 0) mMap?.clear()

            if (spinnerPostion != 0 && hRegisterCustomerList.size > 0) {
                val routePoints = ArrayList<LatLng>()
                routePoints.add(LatLng(currenLocation!!.latitude, currenLocation!!.longitude))
                mMap?.clear()


                hRegisterCustomerList.forEach { registerdCustomerModels ->
                    if (registerdCustomerModels.lat.isNotEmpty() && registerdCustomerModels.lng.isNotEmpty()) {


                        val latLng = LatLng(
                            registerdCustomerModels.lat.toDouble(),
                            registerdCustomerModels.lng.toDouble(),
                        )


                        routePoints.add(latLng)
                        val addressdata = getAddress(latLng)


                        if (addressdata?.fulladdres?.isEmpty() == true) {
                            if (registerdCustomerModels.address.isNotEmpty()) {
                                addressdata.fulladdres = registerdCustomerModels.address
                            } else
                                addressdata.fulladdres = registerdCustomerModels.address1
                        }

                        hCheckifCustomerVisiter(registerdCustomerModels)

                    }


                }

                if (routePoints.size < 2) {
                    Utility.Toast(this, "Customer Location not found.")
                    return@observe
                }


                val tempLat = 33.6844
                val tempLon = 73.0479

                val slatLng =
//                    LatLng(Reg_Customer_list[0].lat.toDouble(), Reg_Customer_list[0].lng.toDouble())
                    LatLng(tempLat, tempLon)

//                val lat: Double = 33.6673478398729
//                val lng: Double = 73.05153458465588
                val lat: Double = tempLat
                val lng: Double = tempLon

                val llt = "$lat,$lng"
//                "40.714224,-73.961452"
//                33.6665° N, 73.0516° E
//                33.6673478398729, 73.05153458465588
                geoCodeAddress(llt, "AIzaSyDqQ_qabCg4qIh92L4j7sMEw7PgsHhSbvU");
//                getPlaces("ChIJlS6Oc3CV3zgR6vWNE3B_Sy0")

                animateCam(slatLng, 12f, true, true)
                //api key = AIzaSyDZ_p4u3jwRZYsX9t1TuDj0BgmUtKc6qu4
                //map key AIzaSyAF5qnoQk50JpO8nR6SbbF75s45w88q8wE
                val routing = Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .key("AIzaSyDqQ_qabCg4qIh92L4j7sMEw7PgsHhSbvU")
                    .waypoints(routePoints)
                    .build()
                routing.execute()

            } else if (spinnerPostion != 0) {
                Toast.makeText(this, "There are no customer at this route.", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun hCheckifCustomerVisiter(registerdCustomerModel: RegisterdCustomerModel) {

        db?.hCheckIfCustomerVisited(registerdCustomerModel.customer_id)?.also { hIsVisited ->
            val marker = hGetMarker(
                lat = registerdCustomerModel.lat.toDouble(),
                lng = registerdCustomerModel.lng.toDouble(),
                title = registerdCustomerModel.name,
                address = registerdCustomerModel.address,
                hIsVisited = hIsVisited
            )

            mMap?.addMarker(marker)

        }


//
//        if (Reg_Customer_list[i].isSave.toInt() == 1) {
//
//
//            marker = hSetServedMarker(Reg_Customer_list[i])
//
//        } else {
//            marker = setMarker(
//                Reg_Customer_list[i].lat.toDouble(),
//                Reg_Customer_list[i].lng.toDouble(),
//                Reg_Customer_list[i].name,
//                "simple"
//            )
//        }
//
//        marker?.let {
//            if (addressdata != null) {
//                setMarkerInfo(it, addressdata)
//            }
//        }

    }

    private fun hSetServedMarker(registerdCustomerModel: RegisterdCustomerModel): Marker? {


        val latLng = LatLng(
            registerdCustomerModel.lat.toDouble(),
            registerdCustomerModel.lng.toDouble()
        )


        val markerOptions = MarkerOptions()
        markerOptions.title(registerdCustomerModel.title)
        markerOptions.position(latLng)
        val marker: Marker? = mMap?.addMarker(markerOptions)


        /*Todo: Set your custom icon here.*/
        val bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.back_alt)
        marker?.setIcon(bitmapDescriptor)
        return marker!!

    }

    private fun fillSpinner() {
        mSelectCustomer_viewModel!!.getSqlRouteList()
    }

    private fun SpinnersListner() {
        spRouteName.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                hRegisterCustomerList.clear()
                spinnerPostion = position
                if (position == 0) {
                    mSelectCustomer_viewModel!!.getSqlCustomerInfo(null)
                } else {
                    mSelectCustomer_viewModel!!.getSqlCustomerInfo(routelist[position - 1].route_id.toString() + "")
                }
                Timber.d("Spinner Called")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        spCustomerName.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
//                model = Reg_Customer_list[position]
//                if (model.getName() == "Select Customer") {
//                    tvCustomerName.setText("")
//                    tvCustomerAddress.setText("")
//                    tvCustomerMobile.setText("")
//                    tvCustomerRoute.setText("")
//                    tvCustomerCode.setText("")
//                } else {
//                    tvCustomerName.setText(model.getName())
//                    tvCustomerAddress.setText(model.getAddress())
//                    tvCustomerMobile.setText(model.getCell())
//                    tvCustomerRoute.setText(db!!.getRouteName(model.getRoute()))
//                    tvCustomerCode.setText(model.getCode())
//                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }


        infowindowInit()
        fillSpinner()
        SpinnersListner()


        //changing the location of default current lcato
        val locationButton =
            (mapFragment.view?.findViewById<View>(Integer.parseInt("1"))?.parent as View).findViewById<View>(
                Integer.parseInt("2")
            )
        val rlp = locationButton.layoutParams as RelativeLayout.LayoutParams
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
        rlp.addRule(RelativeLayout.ALIGN_PARENT_END, 0)
//        rlp.setMargins(10, 0, 0, 40)
        rlp.bottomMargin = 55
        rlp.marginStart = 30

        mMap!!.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap!!.isMyLocationEnabled = true
        mMap!!.uiSettings.isZoomControlsEnabled = true
        mMap!!.uiSettings.setAllGesturesEnabled(true)
        mMap!!.uiSettings.isCompassEnabled = true
        mMap!!.uiSettings.isRotateGesturesEnabled = true
        showOnlyCustomerLocation()//todo

        //moving camera to current location
        if (currenLocation != null) {

            setMarker(
                currenLocation!!.latitude,
                currenLocation!!.latitude,
                "My Location",
                "simple"
            );
            animateCam(
                LatLng(currenLocation!!.latitude, currenLocation!!.longitude),
                15f,
                true,
                true
            )
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun infowindowInit() {

        //MapWrapperLayout initialization
        // 39 - default marker height
        // 20 - offset between the default InfoWindow bottom edge and it's content bottom edge
        mapWrapperLayout.init(mMap, getPixelsFromDp(this, 39f + 20f));

        // We want to reuse the info window for all the markers,
        // so let's create only one class member instance
        infoWindow = layoutInflater.inflate(R.layout.infowindow, null) as ViewGroup?;
        infoTitle = infoWindow?.findViewById(R.id.title) as TextView
        infoSnippet = infoWindow?.findViewById(R.id.snippet) as TextView
        infoButton = infoWindow?.findViewById(R.id.infobtn) as Button

        // Setting custom OnTouchListener which deals with the pressed state
        // so it shows up
        // Setting custom OnTouchListener which deals with the pressed state
        // so it shows up
        infoTitleListener = object : OnInfoWindowElemTouchListener(
            infoButton,
            null, null
        ) {
            override fun onClickConfirmed(v: View, marker: Marker) {
                // Here we can perform some action triggered after clicking the button
                Toast.makeText(baseContext, marker.title + "'s button clicked!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        infoButton!!.setOnTouchListener(infoTitleListener)




        mMap?.setInfoWindowAdapter(object : InfoWindowAdapter {
            override fun getInfoWindow(marker: Marker): View? {
                return null
            }

            override fun getInfoContents(marker: Marker): View {

                Timber.d("Customer NAme is %s", marker.title);
                // Setting up the infoWindow with current's marker info
                infoTitle!!.text = marker.title
                infoSnippet!!.text = marker.snippet
                (infoTitleListener as OnInfoWindowElemTouchListener).setMarker(marker)

                // We must call this to set the current marker and infoWindow references
                // to the MapWrapperLayout
                mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow)
                return infoWindow!!
            }
        })
    }

    fun getPixelsFromDp(context: Context, dp: Float): Int {
        val scale: Float = context.getResources().getDisplayMetrics().density
        return (dp * scale + 0.5f).toInt()
    }


    private fun showOnlyCustomerLocation() {
//        markerPoints.clear()
//        map.clear()
//        tryagainBtn.setVisibility(View.INVISIBLE)
//        if (deliveryMapInfo != null && deliveryMapInfo.getLat() != "" && deliveryMapInfo.getLng() != "" && deliveryMapInfo.getLat() != "null" && deliveryMapInfo.getLng() != "null") {
//            val latitude: Double = deliveryMapInfo.getLat().toString().toDouble()
//            val longitude: Double = deliveryMapInfo.getLng().toString().toDouble()
//            Log.d("deliveryLatLng", "$latitude     $longitude")
//            val userLocation = LatLng(latitude, longitude)
//            val center = CameraUpdateFactory.newLatLng(LatLng(latitude, longitude))
//            val zoom = CameraUpdateFactory.zoomTo(15f)
//            mMap.moveCamera(center)
//            mMap.animateCamera(zoom)
//            setMarker(deliveryMapInfo.getLat().toString(), deliveryMapInfo.getLng().toString(), deliveryMapInfo.getName())
//
//            /*MarkerOptions options = new MarkerOptions();
//            options.position(userLocation);
//            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//           // map.addMarker(options);*/
//        } else {
//            Utility.Toast(this@MapsActivity, "No Location Found")
//        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1) {
            lastLocation()
        }
    }

    private fun setMarker(lat: Double, lng: Double, title: String, icon: String): Marker {
        val bitmapDescriptor1 =
            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
        val latLng = LatLng(lat, lng)
        val markerOptions = MarkerOptions()
        markerOptions.title(title)
        markerOptions.position(latLng)
        markerOptions?.icon(bitmapDescriptor1)

        val marker: Marker? = mMap?.addMarker(markerOptions)
        if (icon.equals("delivery")) {
            val bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.back_alt)
            marker?.setIcon(bitmapDescriptor)
        }


//        marker?.showInfoWindow()
//        mMap?.addMarker(MarkerOptions().position(latLng).title(title))


        return marker!!
    }


    private fun hGetMarker(
        lat: Double,
        lng: Double,
        title: String,
        address: String,
        hIsVisited: Boolean
    ): MarkerOptions {


        val markerOptions = MarkerOptions()
        markerOptions.apply {
            title(title + "\n" + address)
            position(LatLng(lat, lng))
            icon(
                when (hIsVisited) {
                    true -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                    false -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                }
            )
        }



        return markerOptions
    }

    private fun setMarkerInfo(marker: Marker, data: GeocodeAddressModel) {
        marker.snippet = "" + data.fulladdres + "\n" + data.knownName + "\n" + data.city
    }

    private fun animateCam(latLng: LatLng, v: Float, zoom: Boolean, animate: Boolean) {
        if (zoom && animate) {
            mMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, v))
        } else if (zoom) {
            mMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        } else if (animate) {
            mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, v))
        }

    }

    override fun onRoutingFailure(e: RouteException?) {

        Log.d("inmapdrawFail", "f=" + e.toString() + ":msg=" + e?.message)

    }

    override fun onRoutingStart() {
        Log.d("inmapdraws", "statrt")
    }

    override fun onRoutingSuccess(data: java.util.ArrayList<Route>?, p1: Int) {

        val points = ArrayList<LatLng>()
        var lineOptions: PolylineOptions? = null

        for (i in 0 until data!!.size) {
            lineOptions = PolylineOptions()
//            val path: List<HashMap<String, String>> = data.get(i)
            val path: List<LatLng> = data[i].points
            for (j in path.indices) {
//                val point = path[j]
//                val lat = point["lat"]!!.toDouble()
//                val lng = point["lng"]!!.toDouble()
//                val position = LatLng(lat, lng)
                val position = path[j]
                points.add(position)
            }
            lineOptions.addAll(points)
            lineOptions.width(12f)
            lineOptions.color(Color.BLUE)
            Log.d("inmapdraw", "in" + data.size)
        }

        // Drawing polyline in the Google Map for the i-th route

        // Drawing polyline in the Google Map for the i-th route
        Log.d("inmapdrawo", "in" + data.size)
        mMap!!.addPolyline(lineOptions)
    }

    override fun onRoutingCancelled() {
        Log.d("inmapdrawc", "cancel")
    }


    fun getPlaces(latLng: String) {

        // Define a Place ID.
        val placeId = "INSERT_PLACE_ID_HERE"

// Specify the fields to return.
//        val placeFields = listOf(Place.Field.ID, Place.Field.NAME)
        val placeFields = listOf(
            com.google.android.libraries.places.api.model.Place.Field.ID,
            com.google.android.libraries.places.api.model.Place.Field.NAME,
            com.google.android.libraries.places.api.model.Place.Field.PHOTO_METADATAS
        )

// Construct a request object, passing the place ID and fields array.
        val request = FetchPlaceRequest.newInstance(latLng, placeFields)
        Log.d("Places", "INPLace")
        placesClient.fetchPlace(request)
            .addOnSuccessListener { response: FetchPlaceResponse ->
                val place = response.place

                // Get the photo metadata.
                val metada = place.photoMetadatas
                if (metada == null || metada.isEmpty()) {
                    Log.w("photometa", "No photo metadata.")
                    return@addOnSuccessListener
                }
                val photoMetadata = metada.first()
                // Get the attribution text.
                val attributions = photoMetadata?.attributions
                // Create a FetchPhotoRequest.
                val photoRequest = FetchPhotoRequest.builder(photoMetadata)
                    .setMaxWidth(500) // Optional.
                    .setMaxHeight(300) // Optional.
                    .build()
                placesClient.fetchPhoto(photoRequest)
                    .addOnSuccessListener { fetchPhotoResponse: FetchPhotoResponse ->
                        val bitmap = fetchPhotoResponse.bitmap
                        Log.d("photofound", "in")
//                                imageView.setImageBitmap(bitmap)
                    }.addOnFailureListener { exception: Exception ->
                        if (exception is ApiException) {
                            Log.e("TAG", "Place not found: " + exception.message)
                            val statusCode = exception.statusCode
                        }
                    }


                Log.d("PlacesR", "Place found: ${place.name}")
            }.addOnFailureListener { exception: Exception ->
                if (exception is ApiException) {
                    Log.e("ErrorPlaces", "Place not found: ${exception.message}")
                    val statusCode = exception.statusCode

                }
            }


    }

    fun geoCodeAddress(latLng: String, key: String) {

        Api_Reto.getMapRetrofit().retrofit_services.GeoCodDetials(latLng, key)
            .enqueue(object : Callback<GeoCodeResult> {
                override fun onResponse(
                    call: Call<GeoCodeResult>,
                    response: Response<GeoCodeResult>
                ) {

                    val resultList = response.body()?.results
                    Log.d("placejsonres", response.body().toString())
                    Log.d("placejsonrest", response.body()?.status.toString())
                    Log.d("placejsonreR", "rsize=" + resultList?.size.toString() + "ad")
                    Log.d("placejsonresU", call.request().url.toString())

                    if (resultList == null) {
                        geoCodeAddress(latLng, key)
                    } else {
                        if (resultList.size > 0) {
                            val result = resultList[0]

                            placePicture(
                                result.placeId,
                                400,
                                "AIzaSyDqQ_qabCg4qIh92L4j7sMEw7PgsHhSbvU"
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<GeoCodeResult>, t: Throwable) {
                    Log.d("placejsonresFa", t.message.toString())
                }


            })

    }

    private fun placePicture(placeId: String?, maxwidth: Int, key: String) {

    }


    fun getAddress(latLng: LatLng): GeocodeAddressModel? {

        val geocodeAddressModel: GeocodeAddressModel
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses: List<Address>?
        val address: Address?
        var fulladdress = ""
        addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

        if (addresses.isNotEmpty()) {
            address = addresses[0]
            fulladdress =
                address.getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex

            val city = address.locality;
            val state = address.adminArea;
            val country = address.countryName;
            val postalCode = address.postalCode;
            val knownName = address.featureName; // Only if available else return NULL

            geocodeAddressModel =
                GeocodeAddressModel(city, state, country, postalCode, knownName, fulladdress)
            return geocodeAddressModel
        } else {
            fulladdress = "Location not found"
            return null
        }

    }


}