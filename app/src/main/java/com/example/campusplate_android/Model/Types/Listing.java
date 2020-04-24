package com.example.campusplate_android.Model.Types;

public class Listing {
    public double listingId;
    public double userId;
    public String title;
    public String locationDescription;
    public double lat;
    public double lng;
    public double creationTime;
    public double quantity;
    //TODO: Add image

    public Listing() {
        this.listingId = -1;
        this.userId = -1;
        this.title = null;
        this.locationDescription = null;
        this.lat = 0;
        this.lng = 0;
        this.creationTime = 0;
        this.quantity = 0;
    }

    public Listing(double listingId, double userId, String title, String locationDescription, double lat, double lng, double creationTime, double quantity) {
        this.listingId = listingId;
        this.userId = userId;
        this.title = title;
        this.locationDescription = locationDescription;
        this.lat = lat;
        this.lng = lng;
        this.creationTime = creationTime;
        this.quantity = quantity;
    }

    public Listing(String title, double quantity) {  // For posting
        this.title = title;
        this.quantity = quantity;
    }
}
