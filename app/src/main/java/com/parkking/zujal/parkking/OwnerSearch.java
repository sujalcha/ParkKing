package com.parkking.zujal.parkking;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.parkking.zujal.parkking.Adapter.SpotAdapter;
import com.parkking.zujal.parkking.models.PlaceInfo;
import com.parkking.zujal.parkking.models.SpotInfo;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.Double;

public class OwnerSearch extends MainActivity implements
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
    MenuItem login;
    MenuItem logout;
    MenuItem Reservation;
    String loginname;
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
    private Button Logoutbutton;
    private Button Loginbutton;
    private Button CurReservationbut;
    private Session session;;
    String ownerloginname;
    List<SpotInfo> spotlist;
    SpotAdapter adapter;
    RecyclerView ownerrecyclerView;
    String Address;
    String Address1;
    LatLng latLng;
    public String SpotAvailable;
    public String Spotopenclose;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ownersearch);

        mSearchText = (AutoCompleteTextView) findViewById(R.id.input_search);
        mGps = (ImageView) findViewById(R.id.ic_gps);
        //  mInfo = (ImageView) findViewById(R.id.place_info);

        session=new Session(this);


        HashMap<String, String> user = session.getUserDetails();
        ownerloginname= user.get(session.KEY_NAME);


        spotlist = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
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
    private void loadspot()
    {

        String num1 = ownerloginname;
        String uri = String
                .format("http://parkkinz.com/androidphpfiles/ownerspot.php",
                        num1);
        //Toast.makeText(Owner.this,num1, Toast.LENGTH_LONG).show();

        StringRequest myReq = new StringRequest(Request.Method.POST,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);

                                SpotInfo ownerspotdescription = new SpotInfo(
                                        obj.getString("spot_id"),
                                        obj.getString("spot_name"),
                                        obj.getString("spot_address"),
                                        obj.getString("spot_lat"),
                                        obj.getString("spot_lng"),
                                        obj.getString("spot_available"),
                                        obj.getString("spot_price"),
                                        obj.getString("spot_revenue"),
                                        obj.getString("spot_cancel")
                                );

                                spotlist.add(ownerspotdescription);
                                //String Latitude = "-33.862729";
                                //String Longitude = "151.207066";
                                Address = "Wilson Parking";
                                String Latitude = ownerspotdescription.getSpot_lat();
                                String Longitude = ownerspotdescription.getSpot_lng();
                                Address1 = ownerspotdescription.getSpot_address();
                                Log.d("addressWithDetails", Longitude + Latitude + Address1);

                                latitude = Double.parseDouble(ownerspotdescription.getSpot_lat());
                                // longitude = Double.parseDouble(Longitude);
                                longitude = Double.parseDouble(ownerspotdescription.getSpot_lng());
                                // String address = ownerspotdescription.getSpot_address();


                                MarkerOptions markerOptions = new MarkerOptions();

                                markerOptions.title(Address);

                                latLng = new LatLng(latitude, longitude);
                                markerOptions.position(latLng);
                                SpotAvailable=ownerspotdescription.getSpot_available();
                                Spotopenclose=ownerspotdescription.getSpot_cancel();
                             //   Toast.makeText(OwnerSearch.this,Address1+SpotAvailable, Toast.LENGTH_LONG).show();
                                Log.e("ddd",Address1 + SpotAvailable+Spotopenclose );
                                markerOptions.snippet(Address1+"\n"+SpotAvailable+"\n"+Spotopenclose);
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.logoblue));
                                mMap.addMarker(markerOptions);
                            }


                                mMap.setInfoWindowAdapter(new CustomInfoWindowAdapterOwner(OwnerSearch.this));


                               // mCurrLocationMarker.showInfoWindow();
                                //move map camera
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                                //   Toast.makeText(Search.this,"Your Current Location", Toast.LENGTH_LONG).show();

                                Log.d("onLocationChanged", String.format("latitude:%.3f longitude:%.3f", latitude, longitude));
                                Log.d("onLocationChanged", "Exit");




                            //  Toast.makeText(Owner.this, String.valueOf(adapter.getItemCount()), Toast.LENGTH_LONG).show();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }

                })
        {

            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_name", ownerloginname);
                return params;
            };
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(myReq);

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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



        loadspot();

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if(marker.getTitle().toString().equals("Your Location")){
                    Snackbar.make(getCurrentFocus(), "Your Location",Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    //  Toast.makeText(Search.this,marker.getTitle(), Toast.LENGTH_LONG).show();


                    //return true;
                }
                else if(marker.getTitle().toString().equals("Your Destination")){
                    // Toast.makeText(Search.this,marker.getTitle(), Toast.LENGTH_LONG).show();
                    Snackbar.make(getCurrentFocus(), "Your Destination",Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    //return true;
                }

                else if(marker.getTitle().toString().contains("Wilson Parking")){


                    Double latitude = marker.getPosition().latitude;
                    Double longitude = marker.getPosition().longitude;


                    NumberFormat formatter = new DecimalFormat("#0.000000");

                    String lat= formatter.format(latitude);
                    String lng= formatter.format(longitude);

                  //  Log.e("dgfdd",lat +"  "+ lng );
                    Intent desactivity = new Intent("com.parkking.zujal.parkking.Description_Owner");
                    desactivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    namemessage=marker.getTitle();
                    addressmessage=marker.getSnippet();
                    desactivity.putExtra("lat", lat);
                    desactivity.putExtra("lng", lng);
                    startActivity(desactivity);
                    finish();

                  //  Double currentlatitude = mCurrLocationMarker.getPosition().latitude;
                  //  Double currentlongitude = mCurrLocationMarker.getPosition().longitude;

                    double latti= Double.parseDouble(lat);
                    double longi= Double.parseDouble(lng);



                }


                else{

                    Double currentlatitude = mCurrLocationMarker.getPosition().latitude;
                    Double currentlongitude = mCurrLocationMarker.getPosition().longitude;

                    Double latitude = marker.getPosition().latitude;
                    Double longitude = marker.getPosition().longitude;



                }


            }
        });

        Log.d(TAG, "init: initializing");


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

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

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




    private void moveCamera(LatLng latLng, float zoom, PlaceInfo placeInfo){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );

        mMap.clear();

        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(OwnerSearch.this));

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

                mMarker = mMap.addMarker(options);
                //  mMarker.showInfoWindow();
                mMap.addPolygon(MapHelper.createPolygonWithCircle(OwnerSearch.this, latLng, 30));

                String url = getUrl(latLng.latitude, latLng.longitude, "parking");
                Object[] DataTransfer = new Object[2];
                DataTransfer[0] = mMap;
                DataTransfer[1] = url;
                Log.d("onClick", url);
               // GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
             //   getNearbyPlacesData.execute(DataTransfer);
                // Toast.makeText(Search.this,"Nearby Parking", Toast.LENGTH_LONG).show();

            }catch (NullPointerException e){
                Log.e(TAG, "moveCamera: NullPointerException: " + e.getMessage() );
            }
        }else{
            mMap.addMarker(new MarkerOptions().position(latLng));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));


    }

    private void moveCamera(LatLng latLng, float zoom, String title){
        mMap.clear();
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        // mMap.clear();


        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(OwnerSearch.this));
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
        mMap.addPolygon(MapHelper.createPolygonWithCircle(OwnerSearch.this, latLng, 30));
        mCurrLocationMarker.showInfoWindow();


    }






    /*
        --------------------------- google places API autocomplete suggestions -----------------
     */

    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


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



    public class MyUndoListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

        }
    }

}
