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
    public Reservation(int listingId, int quantity, int userId, int status, int code, long timeCreated, long timeExpired, int reservationId) {
        this.listingId = listingId;
        this.quantity = quantity;
        this.userId = userId;
        this.status = status;
        this.code = code;
        this.timeCreated = timeCreated;
        this.timeExpired = timeExpired;
        this.reservationId = reservationId;
    }


    public Reservation(int listingId, int quantity) {
        this.listingId = listingId;
        this.quantity = quantity;

    }
}
