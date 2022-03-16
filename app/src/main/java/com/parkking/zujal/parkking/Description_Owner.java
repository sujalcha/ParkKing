package com.parkking.zujal.parkking;
// mandeep
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


import com.parkking.zujal.parkking.models.SpotInfo;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.parkking.zujal.parkking.models.CarparkerInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Description_Owner extends MainActivity {

    String lattittude="";
    String longitude="";
    private Session session;
    Random rand = null;
    Context context;


    TableRow spotavailablerow;
    String priceid;
    String Receiptid;
    String namein;
    String spotaddress;
    Button spotcancelbut;
    Button spotopenbut;
    Button sendreport;



    private List<SpotInfo> spotlist;
    private static int currentPosition = 0;

//    public Description_Owner(List<SpotInfo> spotlist) {
//        this.spotlist = spotlist;
//    }
    TextView spot_id, spot_name, spot_address, spot_available, spot_price, spot_cancel, spot_lat, spot_lng, spot_revenue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_owner);


     //   spotavailablerow =(TableRow) findViewById(R.id.spotavailablerow);
        spot_id = (TextView)findViewById(R.id.spot_id);
        spot_name = (TextView)findViewById(R.id.spot_name);
        spot_address = (TextView)findViewById(R.id.spot_address);
        spot_cancel = (TextView) findViewById(R.id.spot_cancel);
        spot_available = (TextView)findViewById(R.id.spot_available);
        spot_price = (TextView) findViewById(R.id.spot_price);
        spotcancelbut = (Button)findViewById(R.id.cancelspot);
        sendreport = (Button) findViewById(R.id.sendreportid2);
        spotopenbut = (Button) findViewById(R.id.openspot2);
        spot_lng = (TextView)findViewById(R.id.spot_lng2);
        spot_lat = (TextView) findViewById(R.id.spot_lat2);
        spot_revenue = (TextView)findViewById(R.id.spot_revenue);



        Bundle bundle = getIntent().getExtras();

        lattittude = bundle.getString("lat");
        longitude = bundle.getString("lng");

        Log.e("ddd", lattittude+"     "+longitude );

        String num1 = lattittude;
        String num2 = longitude;
        String uri = String
                .format("http://parkkinz.com/androidphpfiles/findspotowner.php",
                        num1,num2);
        //  Toast.makeText(Description.this,num1, Toast.LENGTH_LONG).show();

        StringRequest myReq = new StringRequest(Request.Method.POST,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);

                                final SpotInfo ownerspotdescription;
                                ownerspotdescription = new SpotInfo(
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

                                spot_id.setText(ownerspotdescription.getSpot_id());
                                spot_name.setText(ownerspotdescription.getSpot_name());
                                spot_address.setText(ownerspotdescription.getSpot_address());
                                spot_lat.setText(ownerspotdescription.getSpot_lat());
                                spot_lng.setText(ownerspotdescription.getSpot_lng());
                                Log.e("ayp",spot_lat.getText().toString());
                                Log.e("ayp",spot_lng.getText().toString());


                                OnClickButtonListener();

                                String cancelspot = ownerspotdescription.getSpot_cancel();

                         //       Toast.makeText(Description_Owner.this,cancelspot, Toast.LENGTH_LONG).show();

                                String tempcancel=ownerspotdescription.getSpot_cancel();
                                if(tempcancel.equals("0"))
                                {
                                    spotopenbut.setVisibility(View.GONE);
                                    spot_cancel.setText("Open");
                                }
                                else{
                                    spot_cancel.setText("Closed");
                                    spotcancelbut.setVisibility(View.GONE);
                                }



                                spot_price.setText("$"+ownerspotdescription.getSpot_price());
                                spot_available.setText(ownerspotdescription.getSpot_available());
                                spot_revenue.setText("$"+ownerspotdescription.getSpot_revenue());


                                Integer spotavailable=Integer.parseInt(ownerspotdescription.getSpot_available().toString());
                                if(spotavailable<1){

                                    Toast.makeText(Description_Owner.this,"Parking Spot Full", Toast.LENGTH_LONG).show();

                                }

                                spotcancelbut.setOnClickListener(new View.OnClickListener(){
                                    public void onClick(View v) {

                                        String spotid=ownerspotdescription.getSpot_id();

                                        String type = "spotcancel";
                                        BackgroundWorker backgroundWorker = new BackgroundWorker(Description_Owner.this);
                                        backgroundWorker.execute(type,spotid);

                                        Intent refresh = new Intent("com.parkking.zujal.parkking.OwnerSearch");
                                        startActivity(refresh);//Start the same Activity
                                        finish();
                                    }
                                });



                                spotopenbut.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String spotid=ownerspotdescription.getSpot_id();
                                        //getting the position of the item to expand it

                                        String type = "spotopen";
                                        BackgroundWorker backgroundWorker = new BackgroundWorker(Description_Owner.this);
                                        backgroundWorker.execute(type,spotid);

                                        Intent refresh = new Intent("com.parkking.zujal.parkking.OwnerSearch");
                                        startActivity(refresh);//Start the same Activity
                                        finish();
                                    }
                                });

                                sendreport.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {



                                        String tempcancel= ownerspotdescription.getSpot_cancel();
                                        String tempcancelstate;
                                        if(tempcancel.equals("0"))
                                        {
                                            tempcancelstate="Not canceled";
                                        }
                                        else{
                                            tempcancelstate="Canceled";
                                        }

                                        String email = "maharjanmandeep@gmail.com";
                                        String subject ="Report for parking spot: "+ownerspotdescription.getSpot_address();
                                        String message = "Your Report for the desired parking spot"+"\n"+
                                                "Spot ID: "+ownerspotdescription.getSpot_id()+"\n"+
                                                "Spot Revenue: "+ownerspotdescription.getSpot_name()+"\n"+
                                                "Spot Address: "+ownerspotdescription.getSpot_address()+"\n"+
                                                "Spot Lattitude: "+ownerspotdescription.getSpot_lat()+"\n"+
                                                "Spot Longitude: "+ownerspotdescription.getSpot_lng()+"\n"+
                                                "Spots Available: "+ownerspotdescription.getSpot_available()+"\n"+
                                                "Spot Price: $"+ownerspotdescription.getSpot_price()+"\n"+
                                                "Spot Revenue: "+ownerspotdescription.getSpot_revenue()+"\n"+
                                                "Spot Cancel: "+tempcancelstate;

                                        //Creating SendMail object
                                        SendMail sm = new SendMail(Description_Owner.this, email, subject, message);

                                        //Executing sendmail to send email
                                        sm.execute();


                                        Intent refresh = new Intent("com.parkking.zujal.parkking.OwnerSearch");
                                        startActivity(refresh);//Start the same Activity
                                        finish();

                                    }
                                });

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
                params.put("spot_lat", lattittude);
                params.put("spot_long", longitude);
                return params;
            };
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(myReq);





    }


    public void OnClickButtonListener(){


    }


    public static int randInt(int min, int max) {

        // NOTE: This will (intentionally) not run as written so that folks
        // copy-pasting have to think about how to initialize their
        // Random instance.  Initialization of the Random instance is outside
        // the main scope of the question, but some decent options are to have
        // a field that is initialized once and then re-used as needed or to
        // use ThreadLocalRandom (if using at least Java 1.7).
        //
        // In particular, do NOT do 'Random rand = new Random()' here or you
        // will get not very good / not very random results.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public void RestoDb(){
        Integer randomreceiptid=randInt(100000,500000);
        HashMap<String, String> user = session.getUserDetails();
        namein= user.get(session.KEY_NAME);
        Receiptid= randomreceiptid.toString();
        priceid=spot_price.getText().toString();
        String spotid = spot_id.getText().toString();
        String spotname= spot_name.getText().toString();
        spotaddress= spot_address.getText().toString();


        //Toast.makeText(Description.this,priceid, Toast.LENGTH_LONG).show();
        String type = "reservation";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type,namein,Receiptid,priceid,spotid,spotname,spotaddress);
    }


    private void sendreservationEmail() {
        //Getting content for email
        HashMap<String, String> user = session.getUserDetails();
        namein= user.get(session.KEY_NAME);
        // String type = "getuserdetails";
        // BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        //backgroundWorker.execute(type,loginname);

        String uri = String
                .format("http://parkkinz.com/androidphpfiles/getuserdetails.php",
                        loginname);


        StringRequest myReq = new StringRequest(Request.Method.POST,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);

                                CarparkerInfo carparker = new CarparkerInfo(
                                        obj.getString("cusname"),
                                        obj.getString("cuspassword"),
                                        obj.getString("cusphone"),
                                        obj.getString("cusemail")
                                );

                                String email = carparker.getParkeremail().toString();
                                //Toast.makeText(Description.this,email, Toast.LENGTH_LONG).show();
                                String subject ="Your Reservation has been book using ParkKing";
                                String message = "Your Reservation has been made. Your Receipt number is "+Receiptid +" for the location " + spotaddress + " for the price of $" + priceid;

                                //Creating SendMail object
                                SendMail sm = new SendMail(Description_Owner.this, email, subject, message);

                                //Executing sendmail to send email
                                sm.execute();
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
                params.put("user_name", loginname);
                return params;
            };
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(myReq);

    }


}
