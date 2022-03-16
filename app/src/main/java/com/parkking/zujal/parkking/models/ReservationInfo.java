package com.parkking.zujal.parkking.models;

/**
 * Created by zujal on 1/04/2018.
 */


public class ReservationInfo {    private String reservationid;
    private String cusid;
    private String receiptnum;
    private String pricepaid;
    private String reservation_state;
    private String spotid;
    private String spotname;
    private String spotaddress;
    private String spotstarttime;
    private String spotendtime;

    public ReservationInfo(String reservationid, String cusid, String receiptnum, String pricepaid, String reservation_state, String spotid, String spotname, String spotaddress,String spotstarttime, String spotendtime) {
        this.reservationid = reservationid;
        this.cusid = cusid;
        this.receiptnum = receiptnum;
        this.pricepaid = pricepaid;
        this.reservation_state = reservation_state;
        this.spotid = spotid;
        this.spotname = spotname;
        this.spotaddress = spotaddress;
        this.spotstarttime = spotstarttime;
        this.spotendtime=spotendtime;
    }

    public String getSpotid() {
        return spotid;
    }

    public void setSpotid(String spotid) {
        this.spotid = spotid;
    }

    public String getSpotname() {
        return spotname;
    }

    public void setSpotname(String spotname) {
        this.spotname = spotname;
    }

    public String getSpotaddress() {
        return spotaddress;
    }

    public void setSpotaddress(String spotaddress) {
        this.spotaddress = spotaddress;
    }

    public String getreservationid() {
        return reservationid;
    }

    public String getcusid() {
        return cusid;
    }

    public String getreceiptnum() {
        return receiptnum;
    }

    public String getpricepaid() {
        return pricepaid;
    }

    public String getreservation_state() {
        return reservation_state;
    }

    public String getSpotstarttime() {
        return spotstarttime;
    }

    public void setSpotstarttime(String spotstarttime) {
        this.spotstarttime = spotstarttime;
    }
    public String getSpotendtime() {
        return spotendtime;
    }

    public void setSpotendtime(String spotendtime) {
        this.spotendtime = spotendtime;
    }
}