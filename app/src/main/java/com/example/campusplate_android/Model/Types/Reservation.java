package com.example.campusplate_android.Model.Types;

public class Reservation {
    public  int listingId;
    public  int quantity;
    public  int userId;
    public  int status;
    public  int code;
    public  long timeCreated;
    public  long timeExpired;
    public  int reservationId;
    public Listing listing;

    public double lat, lng;


    public Reservation() {
        this.listingId = -1;
        this.quantity = 0;
    }

    public Reservation(int listingId, int quantity, int userId, int status, int code, long timeCreated, long timeExpired, Listing listing) {
        this.listingId = listingId;
        this.quantity = quantity;
        this.userId = userId;
        this.status = status;
        this.code = code;
        this.timeCreated = timeCreated;
        this.timeExpired = timeExpired;
        this.listing = listing;

    }
    public Reservation(int listingId, int quantity, int userId, int status, int code, long timeCreated, long timeExpired, int reservationId, Listing listing) {
        this.listingId = listingId;
        this.quantity = quantity;
        this.userId = userId;
        this.status = status;
        this.code = code;
        this.timeCreated = timeCreated;
        this.timeExpired = timeExpired;
        this.reservationId = reservationId;
        this.listing = listing;
    }


    public Reservation(int listingId, int quantity) {
        this.listingId = listingId;
        this.quantity = quantity;
    }

    public Reservation(int listingId, int quantity, double lat, double lng) {
        this.listingId = listingId;
        this.quantity = quantity;
        this.lat = lat;
        this.lng = lng;
        this.status = 3; // Retrieval status
    }
}
