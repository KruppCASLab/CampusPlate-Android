package com.example.campusplate_android.Model.Types;

public class Listing implements Comparable<Listing> {
    public int listingId;
    public int userId;
    public String title;
    public String description;
    public int foodStopId;
    public int creationTime;
    public int quantity;
    //TODO: Add image

    public Listing() {
        this.listingId = -1;
        this.userId = -1;
        this.title = null;
        this.description = null;
        this.foodStopId = -1;
        this.creationTime = 0;
        this.quantity = 0;
    }

    public Listing(int listingId, int userId, int foodStopId, String title, String locationDescription, int creationTime, int quantity) {
        this.listingId = listingId;
        this.userId = userId;
        this.title = title;
        this.description = locationDescription;
        this.foodStopId = foodStopId;
        this.creationTime = creationTime;
        this.quantity = quantity;
    }

    public Listing(int userId, String title, int quantity, int foodStopId) {  // For posting
        this.userId = userId;
        this.title = title;
        this.quantity = quantity;
        this.foodStopId = foodStopId;
    }

    public Listing(String title, int quantity) {  // For editing
        this.title = title;
        this.quantity = quantity;
    }

    public Listing(Listing listingToCopy) { // For editing
        this.listingId = listingToCopy.listingId;
        this.userId = listingToCopy.userId;
        this.title = listingToCopy.title;
        this.description = listingToCopy.description;
        this.foodStopId = listingToCopy.foodStopId;
        this.creationTime = listingToCopy.creationTime;
        this.quantity = listingToCopy.quantity;
    }

    @Override
    public int compareTo(Listing listing) {
        return Integer.compare(listing.creationTime, this.creationTime);
    }
}
