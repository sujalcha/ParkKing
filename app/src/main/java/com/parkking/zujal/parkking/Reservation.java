package com.parkking.zujal.parkking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.parkking.zujal.parkking.Adapter.ReservationAdapter;
import com.parkking.zujal.parkking.models.ReservationInfo;

public class Reservation extends MainActivity {

    TextView ReserveStat;
    Button SearchLocationbut;
    Session Session;
    String loginname;


   // private static final long START_TIME_IN_MILLIS = 600000;

    private TextView mTextViewCountDown;
    private Button mButtonStartPause;
    private Button mButtonReset;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private long mTimeLeftInMillis;
    private long mEndTime;

    final String URL_GET_DATA = "http://parkkinz.com/androidphpfiles/reservationpage.php";
    RecyclerView recyclerView;
    ReservationAdapter adapter;
    List<ReservationInfo> heroList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);


        SearchLocationbut=(Button)findViewById(R.id.gotomapbutid);
        SearchLocationbut.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent= new Intent("com.parkking.zujal.parkking.Search");
                        startActivity(intent);
                    }
                }
        );

        Session=new Session(this);

        ReserveStat=(TextView)findViewById(R.id.Restextid) ;
        HashMap<String, String> user = Session.getUserDetails();
        loginname= user.get(Session.KEY_NAME);
     //   String type = "reservationpage";
    //   BackgroundWorker backgroundWorker = new BackgroundWorker(this);
       // backgroundWorker.execute(type,loginname);



        heroList = new ArrayList<>();

        loadHeroes();
        recyclerView = (RecyclerView)findViewById(R.id.recylcerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }



    private void loadHeroes() {

        String num1 = loginname;
        String uri = String
                .format("http://parkkinz.com/androidphpfiles/reservationpage.php",
                        num1);
        //Toast.makeText(Reservation.this,num1, Toast.LENGTH_LONG).show();

        StringRequest myReq = new StringRequest(Request.Method.POST,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);

                                ReservationInfo hero = new ReservationInfo(
                                        obj.getString("reservationid"),
                                        obj.getString("cusid"),
                                        obj.getString("receiptnum"),
                                        obj.getString("pricepaid"),
                                        obj.getString("reservation_state"),
                                        obj.getString("spot_id"),
                                        obj.getString("spot_name"),
                                        obj.getString("spot_address"),
                                        obj.getString("spot_starttime"),
                                        obj.getString("spot_endtime")
                                );

                                Log.d("sdad",hero.toString());
                                heroList.add(hero);
                            }

                            adapter = new ReservationAdapter(heroList, getApplicationContext());
                            adapter.getItemCount();
                           // Toast.makeText(Reservation.this, String.valueOf(adapter.getItemCount()), Toast.LENGTH_LONG).show();

                            if(adapter.getItemCount()<1){
                                ReserveStat.setText("No Reservation");
                                ReserveStat.setText("Make Reservation");
                            }

                            else{
                                ReserveStat.setText("Current Reservations");
                                SearchLocationbut.setText("Book Again");
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


    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);

        editor.apply();

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

     //   mTimeLeftInMillis = prefs.getLong("millisLeft", START_TIME_IN_MILLIS);
        mTimerRunning = prefs.getBoolean("timerRunning", false);

        updateCountDownText();


        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();

            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                mTimerRunning = false;
                updateCountDownText();
            }
            else
            {
                startTimer();
            }


        }
    }

    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;

            }
        }.start();

        mTimerRunning = true;

    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        // mTextViewCountDown.setText(timeLeftFormatted);

        //Log.d("tagres", timeLeftFormatted);
    }


}