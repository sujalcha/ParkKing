package com.parkking.zujal.parkking.models;

/**
 * Created by zujal on 18/03/2018.
 */

public class PlaceInfo {

    private String type;
    private String name;


    public PlaceInfo(String type, String name) {
        this.type=type;
        this.name = name;

    }

    public PlaceInfo() {

    }
    public String gettype() {
        return type;
    }

    public void settype(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    @Override
    public String toString() {
        return "PlaceInfo{" +
                "type='" + type + '\'' +
                "name='" + name + '\'' +
                '}';
    }
}