package com.example.campusplate_android.Model.Types;

public class Reservation {
    int listingId;
    int quantity;
    int userId;
    int status;
    int code;
    long timeCreated;
    long timeExpired;


    public Reservation() {
        this.listingId = -1;
        this.quantity = 0;


    }

    public Reservation(int listingId, int quantity, int userId, int status, int code, long timeCreated, long timeExpired) {
        this.listingId = listingId;
        this.quantity = quantity;
        this.userId = userId;
        this.status = status;
        this.code = code;
        this.timeCreated = timeCreated;
        this.timeExpired = timeExpired;
    }

    public Reservation(int listingId, int quantity) {

        this.listingId = listingId;
        this.quantity = quantity;

    }
}
