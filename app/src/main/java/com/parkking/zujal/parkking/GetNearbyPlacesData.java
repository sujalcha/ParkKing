package com.parkking.zujal.parkking;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.parkking.zujal.parkking.models.PlaceInfo;
import com.parkking.zujal.parkking.models.SpotInfo;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zujal on 18/03/2018.
 */


public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

    String googlePlacesData;
    GoogleMap mMap;
    String url;
    private PlaceInfo mPlace;
    private Marker mMarker;
    private static final String TAG = "MapActivity";
    EditText spot_available;
    public String SpotAvailable;
    public String Spotopenclose;
    private Context mContext;

    public GetNearbyPlacesData (Context context){
        mContext = context;
    }

    @Override
    protected String doInBackground(Object... params) {
        try {
            Log.d("GetNearbyPlacesData", "doInBackground entered");
            mMap = (GoogleMap) params[0];
            url = (String) params[1];
            DownloadUrl downloadUrl = new DownloadUrl();
            googlePlacesData = downloadUrl.readUrl(url);
            Log.d("GooglePlacesReadTask", "doInBackground Exit");
        } catch (Exception e) {
            Log.d("GooglePlacesReadTask", e.toString());
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("GooglePlacesReadTask", "onPostExecute Entered");
        List<HashMap<String, String>> nearbyPlacesList = null;
        DataParser dataParser = new DataParser(mContext);
        nearbyPlacesList = dataParser.parse(result);
        ShowNearbyPlaces(nearbyPlacesList);

        Log.d("GooglePlacesReadTask", "onPostExecute Exit");
    }

    private void ShowNearbyPlaces(final List<HashMap<String, String>> nearbyPlacesList) {
        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            Log.d("onPostExecute", "Entered into showing locations");
            MarkerOptions markerOptions = new MarkerOptions();
            final HashMap<String, String> googlePlace = nearbyPlacesList.get(i);

            NumberFormat formater = new DecimalFormat("#0.000000");

            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));

            double lating= Double.parseDouble(formater.format(lat));
            double longing= Double.parseDouble(formater.format(lng));

            String placeName = googlePlace.get("place_name");
            final String vicinity = googlePlace.get("vicinity");
            String placeId = googlePlace.get("lat");


            final String let= formater.format(lating);
            final String leg= formater.format(longing);

            String latt = googlePlace.get("lat");
            String lngg = googlePlace.get("lng");

            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName);

        //    Log.e("ddde",placeName+"  "+vicinity+"  "+lating +"  "+ longing );
            markerOptions.snippet(vicinity);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.locoorange));
            Marker mm=mMap.addMarker(markerOptions);

            // markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));

            if(markerOptions.getTitle().toString().contains("Wilson Parking")) {

                mm.setVisible(false);
               // markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                Double currentlatitude = markerOptions.getPosition().latitude;
                Double currentlongitude = markerOptions.getPosition().longitude;
                NumberFormat formatter = new DecimalFormat("#0.000000");



                final String lt= formatter.format(currentlatitude);
                final String lg= formatter.format(currentlongitude);

                String num1 = lt;
                String num2 = lg;
                String uri = String
                        .format("http://parkkinz.com/androidphpfiles/findspot.php",
                                num1,num2);

                StringRequest myReq = new StringRequest(Request.Method.POST,
                        uri,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONArray jsonArray = new JSONArray(response);

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject obj = jsonArray.getJSONObject(i);

                                        SpotInfo spotDescription = new SpotInfo(
                                                obj.getString("spot_id"),
                                                obj.getString("spot_name"),
                                                obj.getString("spot_address"),
                                                obj.getString("spot_cancel"),
                                                obj.getString("spot_price"),
                                                obj.getString("spot_available")
                                        );

                                        MarkerOptions markerOptions = new MarkerOptions();
                                        String placeName = googlePlace.get("place_name");
                                        markerOptions.title(placeName);
                                        double lat = Double.parseDouble(googlePlace.get("lat"));
                                        double lng = Double.parseDouble(googlePlace.get("lng"));
                                        LatLng latLng = new LatLng(lat, lng);
                                        markerOptions.position(latLng);
                                        SpotAvailable=spotDescription.getSpot_available();
                                        Spotopenclose=spotDescription.getSpot_cancel();

                                        //Toast.makeText(mContext,vicinity+SpotAvailable, Toast.LENGTH_LONG).show();
                                       // Log.e("ddd",vicinity + SpotAvailable+Spotopenclose );
                                        markerOptions.snippet(vicinity+"\n"+SpotAvailable+"\n"+Spotopenclose);
                                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.locolblue));
                                        mMap.addMarker(markerOptions);
                                    }


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
                        params.put("spot_lat", lt);
                        params.put("spot_long", lg);
                        return params;
                    };
                };
                RequestQueue requestQueue = Volley.newRequestQueue(mContext);
                requestQueue.add(myReq);



            }


        }
       if(nearbyPlacesList.size()<1)
       {
           Log.e("ddde","There are no parking spots available");
       }
    }

}