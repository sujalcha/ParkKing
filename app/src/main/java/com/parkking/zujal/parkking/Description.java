package com.parkking.zujal.parkking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.parkking.zujal.parkking.models.CarparkerInfo;
import com.parkking.zujal.parkking.models.SpotInfo;
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import belka.us.androidtoggleswitch.widgets.ToggleSwitch;

import static com.parkking.zujal.parkking.clientCheck.PREFS_NAME;

public class Description extends MainActivity {


    SimpleDateFormat simpleDateFormat;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private long mTimeLeftInMillis;
    private long mEndTime;

    private Button logintorevBut;
    private Button PayBut;
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

    SingleDateAndTimePickerDialog.Builder singleBuilder;
    SimpleDateFormat simpleTimeFormat;
    int reservationstate;
    Date resstartdate;
    boolean resfuturetype;
    String comdate;
    String newcomdate;
    TableRow spotreservedate;
    Switch button;
    TextView textview;
    ToggleSwitch toogleswitch;
    int position;
    ToggleSwitch toggleSwitch;
    TextView starttime;
    TextView endtime;
    FrameLayout datepicker;
    String clientName;
    SharedPreferences share;

    TextView spot_id, spot_name, spot_address, spot_available, spot_price, spot_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);


        share = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        clientName = share.getString("name", "defaultName");
      //  Toast.makeText(this, clientName, Toast.LENGTH_SHORT).show();

        this.simpleDateFormat = new SimpleDateFormat("EEE d MMM HH:mm", Locale.getDefault());
        this.simpleTimeFormat = new SimpleDateFormat("hh:mm aa", Locale.getDefault());

       // Goback=(Button)findViewById(R.id.gobackmapid);
  //      spotavailablerow =(TableRow) findViewById(R.id.spotavailablerow);
        spot_id = (TextView)findViewById(R.id.spot_id);
        spot_name = (TextView)findViewById(R.id.spot_name);
        spot_address = (TextView)findViewById(R.id.spot_address);
        spot_cancel = (TextView) findViewById(R.id.spot_cancel);
        spot_available = (TextView)findViewById(R.id.spot_available);
        spot_price = (TextView) findViewById(R.id.spot_price);
      //  Timertext = (TextView) findViewById(R.id.TimerText);
        toogleswitch = (ToggleSwitch) findViewById(R.id.toogleswitch);
        //spotreservedate=(TableRow)findViewById(R.id.spotreservedate);
//        spotreservedate.setVisibility(View.GONE);
         toggleSwitch=(ToggleSwitch)findViewById(R.id.toogleswitch);
        starttime=(TextView)findViewById(R.id.starttime);
        endtime=(TextView)findViewById(R.id.endtime);
        datepicker=(FrameLayout)findViewById(R.id.datepicker);
        logintorevBut=(Button)findViewById(R.id.cancelspot);
        PayBut=(Button)findViewById(R.id.Paybutid);

        session=new Session(this);



        // Toast.makeText(Description.this,namein, Toast.LENGTH_LONG).show();
        if(!session.loggedin()){
            datepicker.setVisibility(View.GONE);
            toogleswitch.setVisibility(View.GONE);
            PayBut.setVisibility(View.GONE);

        }


        if(session.loggedin()){
            logintorevBut.setVisibility(View.GONE);

        }


        position= toggleSwitch.getCheckedTogglePosition();


        if (position==0)
        {
            reservationstate=1;
            String st=String.valueOf(position);
        //    Toast.makeText(Description.this, st, Toast.LENGTH_LONG).show();

            resstartdate= new Date();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            comdate=dateFormat.format(resstartdate);
            // Log.d("olddate",comdate);

            Date newDate = new Date(resstartdate.getTime() + 2*3600*1000);
            // Date newDate = new Date(resstartdate.getTime() + 60*1000);
            newcomdate=dateFormat.format(newDate);
            // Log.d("newdate",newcomdate);

            starttime.setText(comdate);
            endtime.setText(newcomdate);
        }



        toggleSwitch.setOnToggleSwitchChangeListener(new ToggleSwitch.OnToggleSwitchChangeListener(){

            @Override
            public void onToggleSwitchChangeListener(int position, boolean isChecked) {
                String st=String.valueOf(position);
                if (position==0)
                {
                    reservationstate=1;

             //       Toast.makeText(Description.this, reservationstate, Toast.LENGTH_LONG).show();

                    resstartdate= new Date();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    comdate=dateFormat.format(resstartdate);
                    // Log.d("olddate",comdate);

                    Date newDate = new Date(resstartdate.getTime() + 2*3600*1000);
                    // Date newDate = new Date(resstartdate.getTime() + 60*1000);
                    newcomdate=dateFormat.format(newDate);
                    // Log.d("newdate",newcomdate);

                    starttime.setText(comdate);
                    endtime.setText(newcomdate);


                }

                if(position==1)
                {

                    reservationstate=3;
                    resfuturetype=true;


                  //  Toast.makeText(Description.this, String.valueOf(reservationstate), Toast.LENGTH_LONG).show();

                    final Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.DAY_OF_MONTH, 30);
                    calendar.set(Calendar.MONTH, 0);
                    calendar.set(Calendar.YEAR, 1991);
                    calendar.set(Calendar.HOUR_OF_DAY, 11);
                    calendar.set(Calendar.MINUTE, 13);

                    final Date defaultDate = calendar.getTime();

                    singleBuilder = new SingleDateAndTimePickerDialog.Builder(Description.this)
                            // .bottomSheet()
                            //.curved()

                            //.backgroundColor(Color.BLACK)
                            .mainColor(Color.parseColor("#55C7EC"))

                            .displayHours(true)
                            .displayMinutes(true)
                            // .defaultDate(defaultDate)

                            .mustBeOnFuture()

                            //.minutesStep(15)
                            //.mustBeOnFuture()
                            //  .defaultDate(defaultDate)
                            // .minDateRange(minDate)
                            // .maxDateRange(maxDate)

                            .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                                @Override
                                public void onDisplayed(SingleDateAndTimePicker picker) {

                                }
                            })

                            .title("Choose Date And Time ")
                            .listener(new SingleDateAndTimePickerDialog.Listener() {
                                @Override
                                public void onDateSelected(Date datee) {

                                    resstartdate= datee;
                                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    comdate=dateFormat.format(resstartdate);
                                    // Log.d("olddate",comdate);

                                    Date newDate = new Date(resstartdate.getTime() + 2*3600*1000);
                                    // Date newDate = new Date(resstartdate.getTime() + 60*1000);
                                    newcomdate=dateFormat.format(newDate);
                                    // Log.d("newdate",newcomdate);
                                    starttime.setText(comdate);
                                    endtime.setText(newcomdate);
                                }
                            });
                    singleBuilder.display();

           //         Toast.makeText(Description.this, st, Toast.LENGTH_LONG).show();
                }
            }
        });


        Bundle bundle = getIntent().getExtras();

        lattittude = bundle.getString("lat");
        longitude = bundle.getString("lng");

        Log.e("ddd", lattittude+"     "+longitude );

        String num1 = lattittude;
        String num2 = longitude;
        String uri = String
                .format("http://parkkinz.com/androidphpfiles/findspot.php",
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

                                SpotInfo spotDescription = new SpotInfo(
                                        obj.getString("spot_id"),
                                        obj.getString("spot_name"),
                                        obj.getString("spot_address"),
                                        obj.getString("spot_cancel"),
                                        obj.getString("spot_price"),
                                        obj.getString("spot_available")
                                );

                                spot_id.setText(spotDescription.getSpot_id());
                                spot_name.setText(spotDescription.getSpot_name());
                                spot_address.setText(spotDescription.getSpot_address());
                                String cancelspot =spotDescription.getSpot_cancel();
                                if(cancelspot.equals("1"))
                                {spot_cancel.setText("Closed");
                                   // spotavailablerow.setVisibility(View.GONE);

                                    Toast.makeText(Description.this,"The spot is closed at the moment.", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    spot_cancel.setText("Open");
                                }

                                spot_price.setText("$"+spotDescription.getSpot_price());
                                spot_available.setText(spotDescription.getSpot_available());

                                PayBut.setText("PAY $"+spotDescription.getSpot_price());
                                //Toast.makeText(Description.this,spotDescription.getSpot_address(), Toast.LENGTH_LONG).show();

                                Integer spotavailable=Integer.parseInt(spotDescription.getSpot_available().toString());
                                if(spotavailable<1){

                                   // Toast.makeText(Description.this,"Parking Spot Full", Toast.LENGTH_LONG).show();

                                }
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







       // PayBut.setVisibility(View.GONE);




        OnClickButtonListener();
    }

    public void OnClickButtonListener(){

        logintorevBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent("com.parkking.zujal.parkking.LogIn");
                startActivity(intent);
            }
        });





        PayBut.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        RestoDb();

                        sendreservationEmail();

                        //    mTimeLeftInMillis = START_TIME_IN_MILLIS;
                        //   startTimer();
                        //startTimer();

                        // startService(new Intent(Description.this, BroadcastService.class));
                        //  Log.i("yel", "Started service");

                        //String spotid=spot_id.getText().toString();
                        // countdownTimer countingdown=new countdownTimer(Description.this,Receiptid,spotid);
                        //countingdown.startTimer();

                        //  Res.setReservationid(locationname);
                        //   Res.setAddress(locationaddress);

                        //Toast.makeText(Description.this,list.toString(), Toast.LENGTH_LONG).show();
                        // Toast.makeText(Description.this,"Reservation Added", Toast.LENGTH_LONG).show();

                    }
                }
        );

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
        priceid=spot_price.getText().toString().substring(1);
        String spotid = spot_id.getText().toString();
        String spotname= spot_name.getText().toString();
        spotaddress= spot_address.getText().toString();

        String reservationst=String.valueOf(reservationstate);


        if(namein.equals("admin")){
            namein = clientName;

            String type = "reserveclient";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute(type,namein,Receiptid,priceid,reservationst,spotid,spotname,spotaddress,comdate,newcomdate);

        }else{

            String type = "reservation";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute(type,namein,Receiptid,priceid,reservationst,spotid,spotname,spotaddress,comdate,newcomdate);

        }

    }


    private void sendreservationEmail() {
        //Getting content for email
        HashMap<String, String> user = session.getUserDetails();
        namein= user.get(session.KEY_NAME);
        // String type = "getuserdetails";
        // BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        //backgroundWorker.execute(type,loginname);
        String uri;

        if(namein.equals("admin")){

            namein = clientName;

            uri = String
                    .format("http://parkkinz.com/androidphpfiles/getuserdetails.php",
                            namein);

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
                                    String cusname = carparker.getParkername().toString();
                                    String subject="";
                                    String message="";
                                    if(reservationstate==1)
                                    {
                                        subject ="Your Reservation has been started using ParkKing";
                                        message = "Your Reservation has been started. Your Receipt number is "+Receiptid +" for the location " + spotaddress + " for the price of $" + priceid;

                                    }

                                    if(reservationstate==3)
                                    {
                                        subject ="Your Reservation has been booked using ParkKing";
                                        message = "Your Reservation has been made. Your Receipt number is "+Receiptid +" for the location " + spotaddress + " for the price of $" + priceid;

                                    }


                                    //Creating SendMail object
                                    SendMail sm = new SendMail(Description.this, email, subject, message);

                                    //Executing sendmail to send email
                                    sm.execute();
                                    share.edit().clear().commit();
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
                    params.put("user_name", namein);
                    return params;
                };
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(myReq);

        }else {

            uri = String
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
                                    String cusname = carparker.getParkername().toString();

                                    String subject ="Your Reservation has been booked using ParkKing";

                                    String message = "Your Reservation has been made. Your Receipt number is "+Receiptid +" for the location " + spotaddress + " for the price of $" + priceid;

                                    //Creating SendMail object
                                    SendMail sm = new SendMail(Description.this, email, subject, message);

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

        //Log.d("tagdes", timeLeftFormatted);
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

/*
    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        mTimeLeftInMillis = prefs.getLong("millisLeft", START_TIME_IN_MILLIS);
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
*/
}
