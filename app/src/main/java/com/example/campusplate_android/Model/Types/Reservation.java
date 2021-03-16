package com.example.campusplate_android.Model.Types;

public class Reservation {
    int listingId;
    int quantity;


    public Reservation() {
        this.listingId = -1;
        this.quantity = 0;


    }

    public Reservation( int listingId, int quantity) {

        this.listingId = listingId;
        this.quantity = quantity;

    }
}
