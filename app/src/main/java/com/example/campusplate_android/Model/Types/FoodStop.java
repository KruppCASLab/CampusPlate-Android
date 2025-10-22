package com.example.campusplate_android.Model.Types;

import android.os.Parcel;
import android.os.Parcelable;

public class FoodStop  {
    public int foodStopId;
    public boolean reservable;

    public boolean managed;
    public int foodStopNumber;
    public String name;
    public String description;
    public double lat;
    public double lng;
    public String hexColor;
    public String streetAddress;

    public FoodStop() {
        this.foodStopId = -1;
        this.reservable = false;
        this.managed = false;
        this.name = null;
        this.description = null;
        this.lat = 0;
        this.lng = 0;
        this.foodStopNumber = -1;
        this.hexColor = "#";
        this.streetAddress = "";
    }

    public FoodStop(int foodStopId, boolean reservable, boolean managed, String name, String description, double lat, double lng, int foodStopNumber, String hexColor, String streetAddress) {
        this.foodStopId = foodStopId;
        this.reservable = reservable;
        this.managed = managed;
        this.name = name;
        this.description = description;
        this.lat = lat;
        this.lng = lng;
        this.foodStopNumber = foodStopNumber;
        this.hexColor = hexColor;
        this.streetAddress = streetAddress;
    }


}




