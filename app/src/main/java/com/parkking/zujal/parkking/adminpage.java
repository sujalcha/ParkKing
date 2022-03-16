package com.parkking.zujal.parkking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.parkking.zujal.parkking.Adapter.ReservationAdapterAdmin;
import com.parkking.zujal.parkking.models.ReservationInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class adminpage extends MainActivity {

    TextView ReserveStat;
    Button SearchLocationbut;
    Session Session;
    String loginname;

    RecyclerView recyclerView;
    ReservationAdapterAdmin adapter;
    List<ReservationInfo> heroList2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminpage);

        SearchLocationbut=(Button)findViewById(R.id.gotomapbutid2);
        SearchLocationbut.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent= new Intent("com.parkking.zujal.parkking.adminReservationPage");
                        startActivity(intent);
                    }
                }
        );

        Session=new Session(this);

        ReserveStat=(TextView)findViewById(R.id.restextid2) ;
        HashMap<String, String> user = Session.getUserDetails();
        loginname= user.get(Session.KEY_NAME);

        heroList2 = new ArrayList<>();

        loadHeroes();
        recyclerView = (RecyclerView)findViewById(R.id.recylcerViewadmin);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }



    private void loadHeroes() {

       // String num1 = loginname;
        String uri = String
                .format("http://parkkinz.com/androidphpfiles/ownerreservationpage.php");
       // Toast.makeText(adminpage.this,num1, Toast.LENGTH_LONG).show();

        StringRequest myReq = new StringRequest(Request.Method.POST,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj2 = jsonArray.getJSONObject(i);

                                ReservationInfo ReservationInfo = new ReservationInfo(
                                        obj2.getString("reservationid"),
                                        obj2.getString("cusid"),
                                        obj2.getString("receiptnum"),
                                        obj2.getString("pricepaid"),
                                        obj2.getString("reservation_state"),
                                        obj2.getString("spot_id"),
                                        obj2.getString("spot_name"),
                                        obj2.getString("spot_address"),
                                        obj2.getString("spot_starttime"),
                                        obj2.getString("spot_endtime")
                                );

                                heroList2.add(ReservationInfo);
                            }

                            adapter = new ReservationAdapterAdmin(heroList2, getApplicationContext());
                            adapter.getItemCount();
                        //    Toast.makeText(adminpage.this, String.valueOf(adapter.getItemCount()), Toast.LENGTH_LONG).show();

                            if(adapter.getItemCount()<1){
                                ReserveStat.setText("No Reservation");
                                ReserveStat.setText("Make Reservation");
                            }

                            else{
                                ReserveStat.setText("Current Reservations");
                                SearchLocationbut.setText("Book For Client");
                            }
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }

                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(myReq);

    }



}