package com.example.campusplate_android.Model;

import com.example.campusplate_android.Model.Types.Listing;

import java.util.ArrayList;

public class ListingModel {
    private ArrayList<Listing> listings = new ArrayList<>();
    static private ListingModel sharedInstance = null;

    private ListingModel(){
        this.addListing(new Listing(-1, -1, "test name", "test description", 1, 1, 123, 1));
    }

    static synchronized public ListingModel getSharedInstance(){
        if (sharedInstance == null){
            sharedInstance = new ListingModel();
        }
        return sharedInstance;
    }

    public Listing getListing(int index){
        return this.listings.get(index);
    }
    public void addListing(Listing listing){
        this.listings.add(listing);
    }
    public void removeListing(Listing listing){
        this.listings.remove(listing);
    }
    public int getNumberOfListings(){
        return this.listings.size();
    }
    public ArrayList<Listing> getAllListings(){
        return this.listings;
    }

    public ArrayList<Listing> getUserListings(int userId){
        ArrayList<Listing> userListings = new ArrayList<>();
        for (int i = 0; i < this.listings.size(); i++){
            if (this.listings.get(i).userId == userId){
                userListings.add(this.listings.get(i));
            }
        }
        return userListings;
    }


}
