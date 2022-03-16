package com.parkking.zujal.parkking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.parkking.zujal.parkking.Adapter.SpotAdapter;
import com.parkking.zujal.parkking.models.SpotInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Owner extends MainActivity {

    Session Session;
    String ownerloginname;
    List<SpotInfo> spotlist;
    RecyclerView ownerrecyclerView;
    SpotAdapter adapter;
    Button mapview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);

        mapview=(Button)findViewById(R.id.mapview);
        Session=new Session(this);

        HashMap<String, String> user = Session.getUserDetails();
        ownerloginname= user.get(Session.KEY_NAME);

        //String type = "ownerpage";
        //BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        //backgroundWorker.execute(type,ownerloginname);

        spotlist = new ArrayList<>();

        loadspots();
        ownerrecyclerView = (RecyclerView)findViewById(R.id.ownerrecylcerView);
        ownerrecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mapview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent= new Intent("com.parkking.zujal.parkking.OwnerSearch");
                startActivity(intent);
            }
        });
    }



    private void loadspots() {

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
                            }

                            adapter = new SpotAdapter(spotlist, getApplicationContext());
                            adapter.getItemCount();
                          //  Toast.makeText(Owner.this, String.valueOf(adapter.getItemCount()), Toast.LENGTH_LONG).show();

                            if(adapter.getItemCount()<1){
                              //  ReserveStat.setText("No Reservation");
                              //  ReserveStat.setText("Make Reservation");
                            }

                            else{
                               // ReserveStat.setText("Current Reservations");
                               // SearchLocationbut.setText("Book Again");
                            }
                            ownerrecyclerView.setAdapter(adapter);

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

}
