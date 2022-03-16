package com.parkking.zujal.parkking;

/**
 * Created by zujal on 18/03/2018.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by User on 10/2/2017.
 */

public class CustomInfoWindowAdapterOwner implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;


    public CustomInfoWindowAdapterOwner(Context context) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.customer_infowindow_owner, null);
    }

    private void rendowWindowText(Marker marker, View view) {

        String title = marker.getTitle();
        TextView tvTitle = (TextView) view.findViewById(R.id.title);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.spotavailablelo2);
        LinearLayout linearLayout2 = (LinearLayout) view.findViewById(R.id.spotopenclose2);
        linearLayout2.setVisibility(View.GONE);
       linearLayout.setVisibility(View.GONE);

        TextView spotavailable = (TextView) view.findViewById(R.id.spotavailableid2);
        TextView spotopenclose = (TextView) view.findViewById(R.id.spotopencloseid2);

        if (!title.equals("")) {
            tvTitle.setText(title);
        }

        String snippet = marker.getSnippet();
        TextView tvSnippet = (TextView) view.findViewById(R.id.snippet2);


        if (!snippet.equals("")) {
            tvSnippet.setText(snippet);
        }

        //String string = "004-034556";


        // String Reserve = marker.getSnippet();
        Button viewInfo = (Button) view.findViewById(R.id.viewInfo);
        viewInfo.setVisibility(View.GONE);

        if (title.contains("Wilson Parking")) {
            viewInfo.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.VISIBLE);
            linearLayout2.setVisibility(View.VISIBLE);
        }

        if (snippet.contains("\n")) {
            String[] parts = snippet.split("\n");
            String part1 = parts[0]; // 004
            String part2 = parts[1]; // 034556
            String part3 = parts[2]; // 034556

            spotavailable.setText(part2);

            if (part2.equals("0")) {
             //   viewInfo.setVisibility(View.GONE);
            }

            if (part3.equals("0")) {
                spotopenclose.setText("Open");
            } else if (part3.equals("1")) {
                spotopenclose.setText("Close");
               // linearLayout.setVisibility(View.GONE);
             //   Reserver.setVisibility(View.GONE);
            }
        }
    }
    @Override
    public View getInfoWindow(Marker marker) {
        rendowWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        rendowWindowText(marker, mWindow);
        return mWindow;
    }
}