package com.example.campusplate_android;

import com.example.campusplate_android.Model.Types.FoodStop;
public class LocationManager {
    double lat;
    double lng;
    public interface LocationManagerDelegate {
        void receiveLocation(double lat, double lng);
    }

    private static LocationManager locationManager = null;

    private LocationManagerDelegate delegate;

    public boolean isInRange(double currentLat, double currentLng, FoodStop foodStop) {
        return true;
    }

    public void setDelegate(LocationManagerDelegate delegate) {
        this.delegate = delegate;
    }

    public void startMonitoring() {
        this.delegate.receiveLocation(41.101234, -81.02334);
    }

    // Singleton Pattern
    private LocationManager() {

    }

    synchronized static public LocationManager getInstance() {
        if (locationManager == null) {
            locationManager = new LocationManager();
        }
        return locationManager;
    }
}

