package com.parkking.zujal.parkking.models;

/**
 * Created by zujal on 9/04/2018.
 */

public class SpotInfo {

    private String Spot_id;
    private String Spot_name;
    private String Spot_address;
    private String Spot_lat;
    private String Spot_lng;
    private String Spot_available;
    private String Spot_price;
    private String Spot_revenue;
    private String Spot_cancel;

    public SpotInfo(String spot_id, String spot_name, String spot_address, String spot_cancel, String spot_price, String spot_available) {
        Spot_id = spot_id;
        Spot_name = spot_name;
        Spot_address = spot_address;
        Spot_cancel = spot_cancel;
        Spot_available = spot_available;
        Spot_price = spot_price;
    }

    public SpotInfo(String spot_id, String spot_name, String spot_address, String spot_lat, String spot_lng, String spot_available, String spot_price, String spot_revenue, String spot_cancel) {
        Spot_id = spot_id;
        Spot_name = spot_name;
        Spot_address = spot_address;
        Spot_lat = spot_lat;
        Spot_lng = spot_lng;
        Spot_available = spot_available;
        Spot_price = spot_price;
        Spot_revenue = spot_revenue;
        Spot_cancel=spot_cancel;
    }


    public String getSpot_id() {
        return Spot_id;
    }

    public void setSpot_id(String spot_id) {
        Spot_id = spot_id;
    }

    public String getSpot_name() {
        return Spot_name;
    }

    public void setSpot_name(String spot_name) {
        Spot_name = spot_name;
    }

    public String getSpot_address() {
        return Spot_address;
    }

    public void setSpot_address(String spot_address) {
        Spot_address = spot_address;
    }

    public String getSpot_lat() {
        return Spot_lat;
    }

    public void setSpot_lat(String spot_lat) {
        Spot_lat = spot_lat;
    }

    public String getSpot_lng() {
        return Spot_lng;
    }

    public void setSpot_lng(String spot_lng) {
        Spot_lng = spot_lng;
    }

    public String getSpot_available() {
        return Spot_available;
    }

    public void setSpot_available(String spot_available) {
        Spot_available = spot_available;
    }

    public String getSpot_price() {
        return Spot_price;
    }

    public void setSpot_price(String spot_price) {
        Spot_price = spot_price;
    }

    public String getSpot_revenue() {
        return Spot_revenue;
    }

    public void setSpot_revenue(String spot_revenue) {
        Spot_revenue = spot_revenue;
    }

    public String getSpot_cancel() {
        return Spot_cancel;
    }

    public void setSpot_cancel(String spot_cancel) {
        Spot_cancel = spot_cancel;
    }



}
