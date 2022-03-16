package com.parkking.zujal.parkking.Adapter;

/**
 * Created by zujal on 1/04/2018.
 */


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.parkking.zujal.parkking.BackgroundWorker;
import com.parkking.zujal.parkking.SendMail;
import com.parkking.zujal.parkking.Session;
import com.parkking.zujal.parkking.models.CarparkerInfo;
import com.parkking.zujal.parkking.models.ReservationInfo;
import com.parkking.zujal.parkking.R;
import com.parkking.zujal.parkking.adminpage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.bumptech.glide.Glide;

/**
 * Created by Belal on 7/15/2017.
 */

public class ReservationAdapterAdmin extends RecyclerView.Adapter<ReservationAdapterAdmin.ReservationAdminViewHolder> {

    private static final long START_TIME_IN_MILLIS = 7200000;
    private List<ReservationInfo> heroList2;
    private Context context;
    private String resstate;
    private CountDownTimer mCountDownTimer;
    Session session;
    String namein;
    String clientnumber;

    Date ddate;
    private boolean mTimerRunning;


    private static int currentPosition = 0;

    public ReservationAdapterAdmin(List<ReservationInfo> heroList2, Context context) {
        this.heroList2 = heroList2;
        this.context = context;
    }

    @Override
    public ReservationAdminViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout_reservationsadmin, parent, false);
        return new ReservationAdminViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ReservationAdminViewHolder holder, final int position) {

        ReservationInfo hero = heroList2.get(position);

        holder.arraynumber.setText(String.valueOf(position + 1));
        holder.textViewName.setText(hero.getreservationid());
        holder.textViewRealName.setText(hero.getcusid());
        holder.textViewTeam.setText(hero.getreceiptnum());
        holder.textViewFirstAppearance.setText("$" + hero.getpricepaid());
        holder.resnum.setText(hero.getreservationid());
        holder.textViewspotid.setText(hero.getSpotid());
        holder.textViewspotname.setText(hero.getSpotname());
        holder.textViewspotaddress.setText(hero.getSpotaddress());
        holder.textviewaddress.setText(hero.getSpotaddress());
        holder.starttime.setText(hero.getSpotstarttime().substring(0, hero.getSpotstarttime().length() - 3));
        holder.endtime.setText(hero.getSpotendtime().substring(0, hero.getSpotendtime().length() - 3));


        if (Integer.valueOf(hero.getreservation_state()) == 1)
        {
            resstate = "Started";
            holder.timleftrow.setVisibility(View.VISIBLE);
            holder.cancelbutid.setVisibility(View.GONE);
        }

        else if (Integer.valueOf(hero.getreservation_state()) == 3)
        {
            resstate = "Reserved for future";
            holder.timleftrow.setVisibility(View.GONE);
            holder.cancelbutid.setVisibility(View.VISIBLE);
        }

        else
        {
            resstate = "Canceled";
            holder.cancelbutid.setVisibility(View.GONE);
            holder.timleftrow.setVisibility(View.GONE);
        }


        if (holder.timleftrow.getVisibility() == View.GONE) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            holder.resstatebox.setLayoutParams(lp);
            holder.receiptbox.setLayoutParams(lp);
            holder.timeoppo.setLayoutParams(lp);
        }

        else
        {
            LinearLayout.LayoutParams dp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            holder.resstatebox.setLayoutParams(dp);
            holder.receiptbox.setLayoutParams(dp);
            holder.timeoppo.setLayoutParams(dp);
        }


        holder.textViewreservationState.setText(resstate);


        ViewCompat.setRotation(holder.imageView, 0);

        holder.imageView.clearAnimation();


        //if the position is equals to the item position which is to be expanded


        final String dbdate = hero.getSpotstarttime();

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ddate = null;
        try {
            ddate = format.parse(dbdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //    System.out.println(ddate+"llllllllllllllloooooooooo");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String comdate = dateFormat.format(date);
        //  Log.d("comparetime",comdate);

        long diff = date.getTime() - ddate.getTime();


        final Long[] realtime = {START_TIME_IN_MILLIS - diff};
     //   Log.d("answertime", Long.toString(realtime[0]));


        holder.progressBarCircle.setMax((int) 7200000 / 1000);
        holder.progressBarCircle.setProgress((int) 7200000 / 1000);

        if (holder.ctimer != null) {
            holder.ctimer.cancel();
        }

        holder.ctimer = new CountDownTimer(realtime[0], 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                realtime[0] = millisUntilFinished;
                final int SECOND = 1000;
                final int MINUTE = 60 * SECOND;
                final int HOUR = 60 * MINUTE;
                final int DAY = 24 * HOUR;

                long ms = realtime[0];
                StringBuffer text = new StringBuffer("");
                if (ms > DAY) {
                    text.append(ms / DAY).append(":");
                    ms %= DAY;
                }
                if (ms > HOUR) {
                    text.append(ms / HOUR).append(":");
                    ms %= HOUR;
                }
                if (ms > MINUTE) {
                    text.append(ms / MINUTE).append(":");
                    ms %= MINUTE;
                }
                if (ms > SECOND) {
                    text.append(ms / SECOND).append("");
                }

                holder.timer.setText(text.toString());
                holder.progressBarCircle.setProgress((int) (realtime[0] / 1000));
               // Log.e("dd", String.valueOf((realtime[0] / 1000)));

            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                holder.progressBarCircle.setMax((int) 7200000 / 1000);
                holder.progressBarCircle.setProgress((int) 7200000 / 1000);
                holder.timer.setText("Time up");
                ReservationInfo hero = heroList2.get(position);
                String ressid = hero.getreservationid();
                String spottid = hero.getSpotid();
                String pricepaid = hero.getpricepaid();

                //   sendCancellationEmail(position);
                //reloding the list
//                notifyDataSetChanged();

            }
        }.start();

        mTimerRunning = true;


        if (currentPosition != position)
        {
            if(holder.scrollon.isSelected()==true)
            {
                holder.scrollon.setVisibility(View.VISIBLE);
                holder.linearLayout.setVisibility(View.VISIBLE);
                holder.spotdetails.setVisibility(View.VISIBLE);
                holder.scrollon.setBackgroundColor(Color.parseColor("#55C7EC"));
                holder.spotdet.setVisibility(View.GONE);
            //    Toast.makeText(context, "ggselected", Toast.LENGTH_LONG).show();
            }
            else
            {
                //  holder.linearLayout.setVisibility(View.VISIBLE);
            //    holder.linearLayout.setVisibility(View.GONE);
                holder.scrollon.setVisibility(View.VISIBLE);
                holder.spotdet.setVisibility(View.VISIBLE);
                holder.spotdetails.setVisibility(View.GONE);
                holder.scrollon.setBackgroundColor(Color.parseColor("#ffffff"));
           //     Toast.makeText(context, "ggnotselected", Toast.LENGTH_LONG).show();
            }

        }


        if (currentPosition == position)
        {

            holder.scrollon.setVisibility(View.VISIBLE);
            holder.linearLayout.setVisibility(View.VISIBLE);
            holder.spotdetails.setVisibility(View.VISIBLE);
            holder.spotdet.setVisibility(View.GONE);

            if(holder.scrollon.isSelected())
            {
                holder.scrollon.setVisibility(View.VISIBLE);
                holder.linearLayout.setVisibility(View.VISIBLE);
                holder.spotdetails.setVisibility(View.VISIBLE);
                holder.scrollon.setBackgroundColor(Color.parseColor("#55C7EC"));
                holder.spotdet.setVisibility(View.GONE);
           //     Toast.makeText(context, "selected", Toast.LENGTH_LONG).show();
            }
            else
            {
                //  holder.linearLayout.setVisibility(View.VISIBLE);
                holder.scrollon.setVisibility(View.VISIBLE);
            //    holder.linearLayout.setVisibility(View.GONE);
                holder.spotdet.setVisibility(View.GONE);
                holder.spotdetails.setVisibility(View.VISIBLE);
                holder.scrollon.setBackgroundColor(Color.parseColor("#55C7EC"));
          //      Toast.makeText(context, "notselected", Toast.LENGTH_LONG).show();
            }
        }




     //   holder.scrollon.setVisibility(View.VISIBLE);
        holder.scrollon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //getting the position of the item to expand it
                currentPosition = position;
                //holder.linearLayout.setVisibility(View.GONE);
                if (currentPosition == position) {
                    //creating an animation
                    // Animation slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down);

                    if (holder.linearLayout.getVisibility() == View.VISIBLE) {
                        holder.linearLayout.setVisibility(View.GONE);
                        holder.scrollon.setBackgroundColor(Color.parseColor("#ffffff"));
                        ViewCompat.setRotation(holder.imageView, 0);
                        holder.spotdetails.setVisibility(View.GONE);
                        holder.spotdet.setVisibility(View.VISIBLE);
                 //       Toast.makeText(context, "hide", Toast.LENGTH_LONG).show();

                    } else if (holder.linearLayout.getVisibility() == View.GONE) {


                        holder.linearLayout.setVisibility(View.VISIBLE);
                        holder.spotdetails.setVisibility(View.VISIBLE);
                        holder.spotdet.setVisibility(View.GONE);

                        holder.scrollon.setBackgroundColor(Color.parseColor("#55C7EC"));
                        ViewCompat.setRotation(holder.imageView, 180);
                  //      Toast.makeText(context, "show", Toast.LENGTH_LONG).show();

                    }

                }


            }
        });

        holder.cancelbutid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Do you want to cancel the reservation?");
                alertDialog.setMessage("Do you confirm cancellation for client? ");

                alertDialog.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ReservationInfo hero = heroList2.get(position);
                        String ressid = hero.getreservationid();
                        String spottid = hero.getSpotid();
                        String pricepaid = hero.getpricepaid();
                        //getting the position of the item to expand it
                        currentPosition = position;
                        String jj = String.valueOf(currentPosition);
                       // Toast.makeText(context, ressid, Toast.LENGTH_LONG).show();

                        String type = "cancelres";
                        BackgroundWorker backgroundWorker = new BackgroundWorker(context);
                        backgroundWorker.execute(type, ressid, spottid, pricepaid);

                        sendCancellationEmail(position);
                        //reloding the list
                        notifyDataSetChanged();

                        Intent refresh = new Intent(context, adminpage.class);
                        context.startActivity(refresh);//Start the same Activity

                    }
                });

                AlertDialog dialog = alertDialog.create();

                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#55C7EC"));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#55C7EC"));

            }
        });

    }

    @Override
    public int getItemCount() {
        return heroList2.size();
    }

    @Override
    public  long getItemId(int position){
        return  position;
    }

    @Override
    public  int getItemViewType(int position){
        return  position;
    }

    class ReservationAdminViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewRealName, textViewTeam, textViewFirstAppearance, textViewreservationState, textViewspotid, textViewspotname, textViewspotaddress, textviewaddress;
        TextView starttime, endtime, resnum;
        TextView arraynumber;
        TextView timer;
        ImageView imageView;
        LinearLayout linearLayout;
        LinearLayout scrollon;
        Button cancelbutid;
        FrameLayout timleftrow, spotreservedate;
        LinearLayout resstatebox, receiptbox, timeoppo, spotdetails, spotdet;
        ProgressBar progressBarCircle;
        CountDownTimer ctimer;

        ReservationAdminViewHolder(View itemView) {
            super(itemView);
            context=itemView.getContext();
            timer = (TextView) itemView.findViewById(R.id.timeradmin);
            textViewName = (TextView) itemView.findViewById(R.id.textViewNameeeeadmin);
            textViewRealName = (TextView) itemView.findViewById(R.id.textViewRealNameadmin);
            textViewTeam = (TextView) itemView.findViewById(R.id.textViewTeamadmin);
            textViewFirstAppearance = (TextView) itemView.findViewById(R.id.textViewFirstAppearanceadmin);
            textViewreservationState = (TextView) itemView.findViewById(R.id.textreservationstateadmin);

            textViewspotid = (TextView) itemView.findViewById(R.id.spotidadmin);
            textViewspotname = (TextView) itemView.findViewById(R.id.spotnameadmin);
            textViewspotaddress = (TextView) itemView.findViewById(R.id.spotaddressadmin);
            starttime = (TextView) itemView.findViewById(R.id.starttimeadmin);
            endtime = (TextView) itemView.findViewById(R.id.endtimeadmin);


            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayoutadmin);
            spotdetails = (LinearLayout) itemView.findViewById(R.id.spotdetailsadmin);
            spotdet = (LinearLayout) itemView.findViewById(R.id.spotdetadmin);

            scrollon = (LinearLayout) itemView.findViewById(R.id.scrollonadmin);
            cancelbutid = (Button) itemView.findViewById(R.id.cancelbutidadmin);
            textviewaddress = (TextView) itemView.findViewById(R.id.textViewaddressadmin);
            imageView = (ImageView) itemView.findViewById(R.id.arrow_idadmin);
            arraynumber = (TextView) itemView.findViewById(R.id.arraynumberadmin);
            timleftrow = (FrameLayout) itemView.findViewById(R.id.timleftrowadmin);
            resstatebox = (LinearLayout) itemView.findViewById(R.id.resstateboxadmin);
            receiptbox = (LinearLayout) itemView.findViewById(R.id.receiptboxadmin);
            timeoppo = (LinearLayout) itemView.findViewById(R.id.timeoppoadmin);
            progressBarCircle = (ProgressBar) itemView.findViewById(R.id.progressBarCircleadmin);
            resnum = (TextView) itemView.findViewById(R.id.resnumadmin);
        }
    }

    private void sendCancellationEmail(final int position) {
        //Getting content for email

        ReservationInfo hero = heroList2.get(position);

        clientnumber =hero.getcusid().toString();


        String uri = String
                .format("http://parkkinz.com/androidphpfiles/getinfofromid.php",
                        clientnumber);


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


                                ReservationInfo hero = heroList2.get(position);
                                String recnum = hero.getreceiptnum();
                                String spotadd = hero.getSpotaddress();
                                String pricepaid = hero.getpricepaid();

                                //getting the position of the item to expand it
                                currentPosition = position;

                                String email = carparker.getParkeremail().toString();
                                String subject = "Your Reservation has been canceled using ParkKing";
                                String message = "Your Reservation has been cancelled. Your Receipt number was " + recnum + " for the location " + spotadd + " for the price of $" + pricepaid;

                                //Creating SendMail object
                                SendMail sm = new SendMail(context, email, subject, message);

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

                }) {

            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", clientnumber);
                return params;
            }

            ;
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(myReq);

    }
}