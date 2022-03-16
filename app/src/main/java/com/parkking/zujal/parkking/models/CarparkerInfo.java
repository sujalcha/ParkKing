package com.parkking.zujal.parkking.models;

/**
 * Created by zujal on 22/04/2018.
 */

public class CarparkerInfo {

    String parkername;
    String parkerpassword;
    String parkernumber;
    String parkeremail;

    public String getParkername() {
        return parkername;
    }

    public void setParkername(String parkername) {
        this.parkername = parkername;
    }

    public String getParkerpassword() {
        return parkerpassword;
    }

    public void setParkerpassword(String parkerpassword) {
        this.parkerpassword = parkerpassword;
    }

    public String getParkernumber() {
        return parkernumber;
    }

    public void setParkernumber(String parkernumber) {
        this.parkernumber = parkernumber;
    }

    public String getParkeremail() {
        return parkeremail;
    }

    public void setParkeremail(String parkeremail) {
        this.parkeremail = parkeremail;
    }

    public CarparkerInfo(String parkername, String parkerpassword, String parkernumber, String parkeremail) {
        this.parkername = parkername;
        this.parkerpassword = parkerpassword;
        this.parkernumber = parkernumber;
        this.parkeremail = parkeremail;
    }

    public CarparkerInfo() {

    }


}
