package com.example.campusplate_android.Model.Types;

import android.widget.ImageView;

public class Listing implements Comparable<Listing> {
    public int listingId;
    public int userId;
    public String title;
    public String description;
    public int foodStopId;
    public int creationTime;
    public int quantity;

    public String foodImage;
    //TODO: Add image


    public Listing(Listing listing){
        this.listingId = listing.listingId;
        this.userId = listing.userId;
        this.title= listing.title;
        this.description = listing.description;
        this.foodStopId = listing.foodStopId;
        this.creationTime = listing.creationTime;
        this.quantity = listing.quantity;
    }
    public Listing(int listingId, int userId,int foodStopId, String title, String description, int creationTime, int quantity) {
        this.listingId = listingId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.foodStopId = foodStopId;
        this.creationTime = creationTime;
        this.quantity = quantity;
    }

    // omitting userId
    public Listing(String title, String description, int foodStopId, int quantity, String foodImage) {
        this.title = title;
        this.description = description;
        this.foodStopId = foodStopId;
        this.quantity = quantity;
        this.foodImage = foodImage;
    }

    @Override
    public int compareTo(Listing listing) {
        return Integer.compare(listing.creationTime, this.creationTime);
    }
}
