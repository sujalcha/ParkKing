package com.parkking.zujal.parkking.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parkking.zujal.parkking.BackgroundWorker;
import com.parkking.zujal.parkking.Owner;
import com.parkking.zujal.parkking.R;
import com.parkking.zujal.parkking.SendMail;
import com.parkking.zujal.parkking.models.SpotInfo;

import java.util.List;

/**
 * Created by zujal on 9/04/2018.
 */

public class SpotAdapter extends RecyclerView.Adapter<SpotAdapter.SpotViewHolder> {


    private List<SpotInfo> spotlist;
    private Context context;
    private String resstate;
    String tempcancelstate;


    private static int currentPosition = 0;

    public SpotAdapter(List<SpotInfo> spotlist, Context context) {
        this.spotlist = spotlist;
        this.context = context;
    }

    @Override
    public SpotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout_spot, parent, false);
        return new SpotViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final SpotViewHolder holder, final int position) {

        SpotInfo ownerspotdescription = spotlist.get(position);
        holder.arraynumber.setText(String.valueOf(position + 1));
        holder.spotidhead.setText(ownerspotdescription.getSpot_id());
        holder.spotid.setText(ownerspotdescription.getSpot_id());
        holder.spotname.setText(ownerspotdescription.getSpot_name());
        holder.spotaddress.setText(ownerspotdescription.getSpot_address());
        holder.spotaddress2.setText(ownerspotdescription.getSpot_address());
        holder.spotlat.setText(ownerspotdescription.getSpot_lat());
        holder.spotlng.setText(ownerspotdescription.getSpot_lng());
        holder.spotavailable.setText(ownerspotdescription.getSpot_available());
        holder.spotprice.setText("$"+ownerspotdescription.getSpot_price());
        holder.spotrevenue.setText(ownerspotdescription.getSpot_revenue());
        holder.tempcancel=ownerspotdescription.getSpot_cancel();

        if(holder.tempcancel.equals("0"))
        {
            holder.spotcancel.setText("Open");
            holder.spotopenbut.setVisibility(View.GONE);
        }
        else{
            holder.spotcancel.setText("Close");
            holder.spotcancelbut.setVisibility(View.GONE);
        }
        holder.linearLayout.setVisibility(View.GONE);
        ViewCompat.setRotation(holder.imageView, 0);

        holder.imageView.clearAnimation();


        //if the position is equals to the item position which is to be expanded


        if (currentPosition != position)
        {
            if(holder.scrollon.isSelected()==true)
            {
                holder.scrollon.setVisibility(View.VISIBLE);
                holder.linearLayout.setVisibility(View.VISIBLE);
                holder.spotdetails.setVisibility(View.VISIBLE);
                holder.scrollon.setBackgroundColor(Color.parseColor("#55C7EC"));
                holder.spotdet.setVisibility(View.GONE);
          //      Toast.makeText(context, "ggselected", Toast.LENGTH_LONG).show();
            }
            else
            {
                //  holder.linearLayout.setVisibility(View.VISIBLE);
                //    holder.linearLayout.setVisibility(View.GONE);
                holder.scrollon.setVisibility(View.VISIBLE);
                holder.spotdet.setVisibility(View.VISIBLE);
                holder.spotdetails.setVisibility(View.GONE);
                holder.scrollon.setBackgroundColor(Color.parseColor("#ffffff"));
            //    Toast.makeText(context, "ggnotselected", Toast.LENGTH_LONG).show();
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
            //    Toast.makeText(context, "selected", Toast.LENGTH_LONG).show();
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


        holder.scrollon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //getting the position of the item to expand it
                currentPosition = position;

                if (currentPosition == position) {



                    if (holder.linearLayout.getVisibility() == View.VISIBLE) {
                        holder.linearLayout.setVisibility(View.GONE);
                        holder.scrollon.setBackgroundColor(Color.parseColor("#ffffff"));
                        ViewCompat.setRotation(holder.imageView, 0);
                        holder.spotdetails.setVisibility(View.GONE);
                        holder.spotdet.setVisibility(View.VISIBLE);
                    //    Toast.makeText(context, "hide", Toast.LENGTH_LONG).show();

                    } else if (holder.linearLayout.getVisibility() == View.GONE) {


                        holder.linearLayout.setVisibility(View.VISIBLE);
                        holder.spotdetails.setVisibility(View.VISIBLE);
                        holder.spotdet.setVisibility(View.GONE);

                        holder.scrollon.setBackgroundColor(Color.parseColor("#55C7EC"));
                        ViewCompat.setRotation(holder.imageView, 180);
                     //   Toast.makeText(context, "show", Toast.LENGTH_LONG).show();

                    }

                    //toggling visibility
                    // holder.linearLayout.setVisibility(View.VISIBLE);

                    //adding sliding effect
                    //  holder.linearLayout.startAnimation(slideDown);
                }

                //reloding the list
                //  notifyDataSetChanged();
            }
        });



        holder.spotcancelbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Do you want to cancel the spot?");
                alertDialog.setMessage("Do you confirm cancellation for the spot? ");

                alertDialog.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SpotInfo ownerspotdescription = spotlist.get(position);
                        String spotid=ownerspotdescription.getSpot_id();
                        //getting the position of the item to expand it
                        currentPosition = position;

                        String type = "spotcancel";
                        BackgroundWorker backgroundWorker = new BackgroundWorker(context);
                        backgroundWorker.execute(type,spotid);


                        //reloding the list
                        notifyDataSetChanged();
                        Intent refresh = new Intent(context, Owner.class);
                        context.startActivity(refresh);//Start the same Activity
                        ((Activity)context).finish();

                    }
                });

                AlertDialog dialog = alertDialog.create();

                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#55C7EC"));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#55C7EC"));

            }
        });

        holder.spotopenbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpotInfo ownerspotdescription = spotlist.get(position);
                String spotid=ownerspotdescription.getSpot_id();
                //getting the position of the item to expand it
                currentPosition = position;

                String type = "spotopen";
                BackgroundWorker backgroundWorker = new BackgroundWorker(context);
                backgroundWorker.execute(type,spotid);


                //reloding the list
                notifyDataSetChanged();
                Intent refresh = new Intent(context, Owner.class);
                context.startActivity(refresh);//Start the same Activity
            }
        });

        holder.sendreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SpotInfo ownerspotdescription = spotlist.get(position);
                currentPosition = position;
                String tempcancel=ownerspotdescription.getSpot_cancel();
                String tempcancelstate;
                if(tempcancel.equals("0"))
                {
                    tempcancelstate="Not canceled";
                }
                else{
                    tempcancelstate="Canceled";
                }

                String email = "zujalxtha@gmail.com";
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
                SendMail sm = new SendMail(context, email, subject, message);

                //Executing sendmail to send email
                sm.execute();

            }
        });

    }

    @Override
    public int getItemCount() {
        return spotlist.size();
    }

    class SpotViewHolder extends RecyclerView.ViewHolder {
        TextView spotidhead,spotid, spotname, spotaddress, spotlat, spotlng, spotavailable, spotprice, spotrevenue,spotcancel,spotaddress2,arraynumber;
        ImageView imageView;
        LinearLayout linearLayout;
        LinearLayout scrollon,spotdetails,spotdet;
        Button spotcancelbut;
        Button spotopenbut;
        Button sendreport;
        String tempcancel;

        SpotViewHolder(View itemView) {
            super(itemView);
            context=itemView.getContext();
            spotidhead= (TextView) itemView.findViewById(R.id.sspotidd);
            spotaddress2=(TextView) itemView.findViewById(R.id.sspotaddress2);
            spotid = (TextView) itemView.findViewById(R.id.sspotid);
            spotname = (TextView) itemView.findViewById(R.id.sspotname);
            spotaddress = (TextView) itemView.findViewById(R.id.sspotaddress);
            spotlat = (TextView) itemView.findViewById(R.id.sspotlat);
            spotlng = (TextView) itemView.findViewById(R.id.sspotlng);
            spotavailable = (TextView) itemView.findViewById(R.id.sspotavailable);
            spotprice = (TextView) itemView.findViewById(R.id.sspotprice);
            spotrevenue = (TextView) itemView.findViewById(R.id.sspotrevenue);
            spotcancel = (TextView) itemView.findViewById(R.id.sspotcancelation);
            spotcancelbut = (Button) itemView.findViewById(R.id.cancelspot);
            spotopenbut = (Button) itemView.findViewById(R.id.openspot);
            spotdetails=(LinearLayout)itemView.findViewById(R.id.spotdetails2);
            spotdet=(LinearLayout)itemView.findViewById(R.id.spotdet2);

            sendreport=(Button)itemView.findViewById(R.id.sendreportid);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.ownerlinearLayout);
            scrollon = (LinearLayout) itemView.findViewById(R.id.ownerscrollon);
            imageView = (ImageView) itemView.findViewById(R.id.ownerarrow_id);
            arraynumber=(TextView) itemView.findViewById(R.id.arraynumber);


        }
    }
}