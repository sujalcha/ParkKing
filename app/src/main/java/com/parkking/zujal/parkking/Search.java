package com.parkking.zujal.parkking;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parkking.zujal.parkking.models.PlaceInfo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Search extends MainActivity implements

        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    double latitude;
    double longitude;
    private int PROXIMITY_RADIUS = 1000;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    String  namemessage;
    String  addressmessage;
    LocationRequest mLocationRequest;

    Intent intent;

    private static final String TAG = "MapActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 14f;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));
    //widgets
    private AutoCompleteTextView mSearchText;
    private ImageView mGps;
    //vars
    private Boolean mLocationPermissionsGranted = false;
    private static final int PLACE_PICKER_REQUEST = 1;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlaceAutoCompleteAdapter mPlaceAutoCompleteAdapter;
    private PlaceInfo mPlace;
    private Marker mMarker;

    private Session session;
    String adminuser;
    Button btnPark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        mSearchText = (AutoCompleteTextView) findViewById(R.id.input_search);
        mGps = (ImageView) findViewById(R.id.ic_gps);
      //  mInfo = (ImageView) findViewById(R.id.place_info);

        session=new Session(this);
        HashMap<String, String> user = session.getUserDetails();
        adminuser = user.get(session.KEY_NAME);

        btnPark = (Button) findViewById(R.id.btnParking);

        if(adminuser != null){
            if (adminuser.equals("admin"))
            {
                btnPark.setVisibility(View.GONE);
            }
        } else {
            btnPark.setVisibility(View.VISIBLE);
        }

        //Check if Google Play Services Available or not
        if (!CheckGooglePlayServices()) {
            Log.d("onCreate", "Finishing test case since Google Play Services are not available");
            finish();
        }
        else {
            Log.d("onCreate","Google Play Services available.");
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;




        getDeviceLocation();
        //  mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }


        btnPark.setOnClickListener(new View.OnClickListener() {
            String Parking = "parking";
            @Override
            public void onClick(View v) {
                Log.d("onClick", "Button is Clicked");
                mMap.clear();
                String url = getUrl(latitude, longitude, Parking);
                Object[] DataTransfer = new Object[2];
                DataTransfer[0] = mMap;
                DataTransfer[1] = url;
                Log.d("onClick", url);
                GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData(Search.this);
                getNearbyPlacesData.execute(DataTransfer);

                Toast.makeText(Search.this,"Nearby Parking Spots", Toast.LENGTH_LONG).show();


                //      mMap.setOnMarkerClickListener(this);


                LatLng latLng = new LatLng(latitude, longitude);

                MarkerOptions options = new MarkerOptions()
                        .position(latLng)
                        .title("Your Location")
                        .snippet("This is your current location")
                       // .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.logoblue));


                mMap.addMarker(options);
                mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(Search.this));
                Circle circle;
                circle=mMap.addCircle(new CircleOptions()
                        .center(latLng)
                        .radius(1200)
                        .strokeWidth(1));
                      //  .strokeColor(Color.GRAY)
                      //  .fillColor(Color.argb(50,5, 0, 0)));


                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f));
             //   descript.setText(DataTransfer);

                mMap.addPolygon(MapHelper.createPolygonWithCircle(Search.this, latLng, 30));


            }
        });



        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if(marker.getTitle().toString().equals("Your Location"))
                {
                    Snackbar.make(getCurrentFocus(), "Your Location",Snackbar.LENGTH_LONG).setAction("Action", null).show();
                  //  Toast.makeText(Search.this,marker.getTitle(), Toast.LENGTH_LONG).show();


                    //return true;
                }
                else if(marker.getTitle().toString().equals("Your Destination"))
                {
                   // Toast.makeText(Search.this,marker.getTitle(), Toast.LENGTH_LONG).show();
                    Snackbar.make(getCurrentFocus(), "Your Destination",Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    //return true;
                }

                else if(marker.getTitle().toString().contains("Wilson Parking")){


                    if (marker.getSnippet().contains("\n")) {
                        String[] parts = marker.getSnippet().split("\n");
                        String part1 = parts[0]; // 004
                        String part2 = parts[1]; // 034556
                        String part3 = parts[2]; // 034556

                      //  Toast.makeText(Search.this,part2, Toast.LENGTH_LONG).show();

                        int spotavailable= Integer.parseInt(part2);

                        if (part3.equals("0")&& (spotavailable > 0)) {
                            // spotopenclose.setText("Open");

                            Double latitude = marker.getPosition().latitude;
                            Double longitude = marker.getPosition().longitude;

                            NumberFormat formatter = new DecimalFormat("#0.000000");

                            String lat = formatter.format(latitude);
                            String lng = formatter.format(longitude);
                            Log.d("dddddddddddddd", lat+lng);

                            Intent intent = new Intent("com.parkking.zujal.parkking.Description");
                            namemessage = marker.getTitle();
                            addressmessage = marker.getSnippet();
                            intent.putExtra("lat", lat);
                            intent.putExtra("lng", lng);
                            startActivity(intent);
                        } else if (part3.equals("1")) {
                            //  spotopenclose.setText("Close");
                             Toast.makeText(Search.this,"The spot is closed at the moment.\n Please look for other parking spots", Toast.LENGTH_LONG).show();

                        } else
                        {
                            Toast.makeText(Search.this,"The spot is full at the moment.\n Please look for other parking spots", Toast.LENGTH_LONG).show();

                        }

                    }

                }


                else

                {

                    Double latitudeee = marker.getPosition().latitude;
                    Double longitudeee = marker.getPosition().longitude;


                    Snackbar.make(getCurrentFocus(), "Distance to parking spot \n"+ GetDistanceFromCurrentPosition(latitude,longitude,latitudeee,longitudeee)+" meters",Snackbar.LENGTH_LONG).setAction("Action", null).show();

                }


            }
        });

        Log.d(TAG, "init: initializing");

        mSearchText.setOnItemClickListener(mAutocompleteClickListener);

        mPlaceAutoCompleteAdapter = new PlaceAutoCompleteAdapter(this, mGoogleApiClient,
                LAT_LNG_BOUNDS, null);

        mSearchText.setAdapter(mPlaceAutoCompleteAdapter);

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    //execute our method for searching
                    geoLocate();
                }

                return false;
            }
        });

        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked gps icon");
                getDeviceLocation();
            }
        });

    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                //.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(200);
        mLocationRequest.setFastestInterval(200);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    public Bitmap resizeBitmap(String drawableName,int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(drawableName, "drawable", getPackageName()));
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false);
    }

    private String getUrl(double latitude, double longitude, String nearbyPlace) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyATuUiZUkEc_UgHuqsBJa1oqaODI-3mLs0");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    @Override
    public void onConnectionSuspended(int i) {

    }



    @Override
    public void onLocationChanged(Location location) {


        Log.d("onLocationChanged", "entered");

       // mLastLocation = location;
     //   if (mCurrLocationMarker != null) {
    //        mCurrLocationMarker.remove();
     //   }

        //Place current location marker
        latitude = location.getLatitude();
        longitude = location.getLongitude();
      //  LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
      //  MarkerOptions markerOptions = new MarkerOptions();
      //  markerOptions.position(latLng);
      //  markerOptions.title("Your Location");
      //  markerOptions.snippet("This is your current location");
       // markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
     //  markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.logoblue));

      //  Circle circle;
      //  circle=mMap.addCircle(new CircleOptions()
     //           .center(latLng)
     //           .radius(1200)
     //           .strokeWidth(1));

      //  mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(Search.this));
       // mMap.addPolygon(MapHelper.createPolygonWithCircle(Search.this, latLng, 30));
    //    mCurrLocationMarker = mMap.addMarker(markerOptions);
    //   mCurrLocationMarker.showInfoWindow();
        //move map camera
       // mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
       // mMap.animateCamera(CameraUpdateFactory.zoomTo(14));

        Log.d("onLocationChanged", String.format("latitude:%.3f longitude:%.3f",latitude,longitude));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            Log.d("onLocationChanged", "Removing Location Updates");
        }
        Log.d("onLocationChanged", "Exit");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);

                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, place.getId());
                placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            }
        }
    }

    private void geoLocate(){
        Log.d(TAG, "geoLocate: geolocating");

        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(Search.this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage() );
        }

        if(list.size() > 0){
            Address address = list.get(0);

            Log.d(TAG, "geoLocate: found a location: " + address.toString());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();

            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM,
                    address.getAddressLine(0));
        }
    }

    private void getDeviceLocation(){

        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{


            final Task location = mFusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Log.d(TAG, "onComplete: Your location!");
                     //   Toast.makeText(Search.this, "Your current location", Toast.LENGTH_SHORT).show();
                        Location currentLocation = (Location) task.getResult();

                        moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                DEFAULT_ZOOM,
                                "Your Location");

                    }else{
                        Log.d(TAG, "onComplete: current location is null");
                        Toast.makeText(Search.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    private void moveCamera(LatLng latLng, float zoom, PlaceInfo placeInfo){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );

        mMap.clear();

        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(Search.this));

        if(placeInfo != null){
            try{
                mMap.clear();

                String snippet = placeInfo.getName();

                MarkerOptions options = new MarkerOptions()
                        .position(latLng)
                        .title(placeInfo.gettype())
                        .snippet(snippet)
                       // .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.logoblue));

                Circle circle;
                circle=mMap.addCircle(new CircleOptions()
                        .center(latLng)
                        .radius(1200)
                        .strokeWidth(1));



              //  mMarker.showInfoWindow();
                mMap.addPolygon(MapHelper.createPolygonWithCircle(Search.this, latLng, 30));

                String url = getUrl(latLng.latitude, latLng.longitude, "parking");
                Object[] DataTransfer = new Object[2];
                DataTransfer[0] = mMap;
                DataTransfer[1] = url;
                Log.d("onClick", url);
                GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData(Search.this);
                getNearbyPlacesData.execute(DataTransfer);
               // Toast.makeText(Search.this,"Nearby Parking Spots near \n" +placeInfo.getName(), Toast.LENGTH_LONG).show();
                mMarker = mMap.addMarker(options);


                Toast toast = Toast.makeText(this, "Nearby Parking Spots near \n" +placeInfo.getName(), Toast.LENGTH_SHORT);
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                if( v != null) v.setGravity(Gravity.CENTER);
                toast.show();

            }catch (NullPointerException e){
                Log.e(TAG, "moveCamera: NullPointerException: " + e.getMessage() );
            }
        }else{
            mMap.addMarker(new MarkerOptions().position(latLng));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        hideSoftKeyboard();
    }

    private void moveCamera(LatLng latLng, float zoom, String title){
        mMap.clear();
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
       // mMap.clear();


        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(Search.this));
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(title)
                .snippet("This is your current location")
              //  .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.logoblue));

        Circle circle;
        circle=mMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(1200)
                .strokeWidth(1));
        mCurrLocationMarker= mMap.addMarker(options);
        mMap.addPolygon(MapHelper.createPolygonWithCircle(Search.this, latLng, 30));
        mCurrLocationMarker.showInfoWindow();

        hideSoftKeyboard();
    }




    private void hideSoftKeyboard() {

        InputMethodManager im = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

    }

    /*
        --------------------------- google places API autocomplete suggestions -----------------
     */

    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            hideSoftKeyboard();

            final AutocompletePrediction item = mPlaceAutoCompleteAdapter.getItem(i);
            final String placeId = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);

            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if(!places.getStatus().isSuccess()){
                Log.d(TAG, "onResult: Place query did not complete successfully: " + places.getStatus().toString());
                places.release();
                return;
            }



            final Place place = places.get(0);


            try{
                mPlace = new PlaceInfo();
                mPlace.settype("Your Destination");
                mPlace.setName(place.getName().toString());

                Log.d(TAG, "onResult: place: " + mPlace.toString());
            }catch (NullPointerException e){
                Log.e(TAG, "onResult: NullPointerException: " + e.getMessage() );
            }

            moveCamera(new LatLng(place.getViewport().getCenter().latitude,
                    place.getViewport().getCenter().longitude), DEFAULT_ZOOM, mPlace);



            places.release();
        }
    };

    public static float GetDistanceFromCurrentPosition(double lat1,double lng1, double lat2, double lng2)
    {
        double earthRadius = 3958.75;

        double dLat = Math.toRadians(lat2 - lat1);

        double dLng = Math.toRadians(lng2 - lng1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
                * Math.sin(dLng / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double dist = earthRadius * c;

        int meterConversion = 1609;

        return new Float(dist * meterConversion).floatValue();

    }

    public class MyUndoListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            }
    }
}


