package com.example.campusplate_android.Model.Types;

public class FoodStops {
    public int foodStopId;
    public String name;
    public String description;
    public double lat, lng;

    public FoodStops() {
        this.foodStopId = -1;
        this.name = null;
        this.description = null;
        this.lat = 0;
        this.lng = 0;
    }

    public FoodStops(int foodStopId, String name, String description, double lat, double lng) {
        this.foodStopId = foodStopId;
        this.name = name;
        this.description = description;
        this.lat = lat;
        this.lng = lng;
    }

}
