package com.example.campusplate_android.Model.Types;

public class FoodStop {
    public int foodStopId, foodStopNumber;
    public String name;
    public String description;
    public double lat, lng;
    public String hexColor;
    public String streetAddress;

    public FoodStop() {
        this.foodStopId = -1;
        this.name = null;
        this.description = null;
        this.lat = 0;
        this.lng = 0;
        this.foodStopNumber = -1;
        this.hexColor = "#";
        this.streetAddress = "";
    }

    public FoodStop(int foodStopId, String name, String description, double lat, double lng, int foodStopNumber, String hexColor, String streetAddress) {
        this.foodStopId = foodStopId;
        this.name = name;
        this.description = description;
        this.lat = lat;
        this.lng = lng;
        this.foodStopNumber = foodStopNumber;
        this.hexColor = hexColor;
        this.streetAddress = streetAddress;
    }

}
