package com.example.campusplate_android;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationRequest;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.example.campusplate_android.Model.Types.FoodStop;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class LocationManager {
    public interface LocationManagerDelegate {
        void receiveLocation(double lat, double lng);
    }

    private LocationManagerDelegate delegate = null;
    private static LocationManager locationManager = null;
    private Activity activity = null;

    public boolean isInRange(double currentLat, double currentLng, FoodStop foodStop) {
        return true;
    }

    synchronized public void setDelegate(LocationManagerDelegate delegate) {
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

    synchronized static public LocationManager getInstance(Activity activity) {
        locationManager = getInstance();
        locationManager.activity = activity;
        return locationManager;
    }

    public void getUserLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.activity);
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(this.activity, location -> {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();

                        if (delegate != null) {
                            delegate.receiveLocation(latitude, longitude);
                        }

                        // Do something with the latitude and longitude
                        Log.d("Location", "Latitude: " + latitude + ", Longitude: " + longitude);
                    } else {
                        // Handle the case where the location is null
                        Log.d("Location", "Location is null");
                    }
                })
                .addOnFailureListener(this.activity, e -> {
                    // Handle failure to get location
                    Log.e("Location", "Error getting location", e);
                });
    }
}
