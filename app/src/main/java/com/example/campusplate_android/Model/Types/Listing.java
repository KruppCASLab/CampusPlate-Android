package com.example.campusplate_android.Model.Types;

public class Listing implements Comparable<Listing> {
    public int listingId;
    public int userId;
    public String title;
    public String locationDescription;
    public double lat;
    public double lng;
    public int creationTime;
    public int quantity;
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

    public Listing(int listingId, int userId, String title, String locationDescription, double lat, double lng, int creationTime, int quantity) {
        this.listingId = listingId;
        this.userId = userId;
        this.title = title;
        this.locationDescription = locationDescription;
        this.lat = lat;
        this.lng = lng;
        this.creationTime = creationTime;
        this.quantity = quantity;
    }

    public Listing(int userId, String title, int quantity, double lat, double lng) {  // For posting
        this.userId = userId;
        this.title = title;
        this.quantity = quantity;
        this.lat = lat;
        this.lng = lng;
    }

    public Listing(String title, int quantity) {  // For editing
        this.title = title;
        this.quantity = quantity;
    }

    public Listing(Listing listingToCopy) { // For editing
        this.listingId = listingToCopy.listingId;
        this.userId = listingToCopy.userId;
        this.title = listingToCopy.title;
        this.locationDescription = listingToCopy.locationDescription;
        this.lat = listingToCopy.lat;
        this.lng = listingToCopy.lng;
        this.creationTime = listingToCopy.creationTime;
        this.quantity = listingToCopy.quantity;
    }

    @Override
    public int compareTo(Listing listing) {
        return Integer.compare(listing.creationTime, this.creationTime);
    }
}
