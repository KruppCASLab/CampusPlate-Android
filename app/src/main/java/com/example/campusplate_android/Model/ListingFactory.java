package com.example.campusplate_android.Model;

import com.google.gson.internal.LinkedTreeMap;
import com.example.campusplate_android.Model.Types.Listing;


public class ListingFactory {
    public static Listing toListing(LinkedTreeMap map) {
        Double listingId = (double) map.get("listingId");
        Double foodStopId = (double) map.get("foodStopId");
        Double userId = (double) map.get("userId");
        String title = (String) map.get("title");
        String description = (String) map.get("description");
        Double creationTime = (double) map.get("creationTime");
        Double expirationTime = (double) map.get("expirationTime");
        Double quantity = (double) map.get("quantity");
        Double weightOunces = (double) map.get("weightOunces");
        String image = (String) map.get("image");
        //Double quantityRemaining = (double) map.get("quantityRemaining");
        Listing listing = new Listing(listingId.intValue(), userId.intValue(), foodStopId.intValue(), title, description, creationTime.intValue(), quantity.intValue(), weightOunces.intValue(), 0);

        return listing;
    }
}
