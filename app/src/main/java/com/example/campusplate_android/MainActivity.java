package com.example.campusplate_android;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements LocationListener, LocationManager.LocationManagerDelegate {

    LocationManager locationManager;
    String provider;

    public static void main(String[] args) {
        System.out.println("Hello world!");

        LocationManager.getInstance().setDelegate(new MainActivity());
        LocationManager.getInstance().startMonitoring();

    }

    @Override
    public void receiveLocation(double lat, double lng) {
        System.out.printf("%f, %f\n", lat, lng);
        Log.i("Testing", "Got into receiveLocation");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ServiceClient.getInstance(this.getApplicationContext());

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_events, R.id.navigation_alllistings, R.id.navigation_mylistings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        //Location stuff

        /*ActivityCompat.requestPermissions(this,new String[]
                {Manifest.permission.ACCESS_COARSE_LOCATION}, 1);*/

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            // Location already given
            getUserLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {  // Replace with your request code
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted. You can perform your operation here.
                    getUserLocation();
                } else {
                    Toast.makeText(this, "Location permission is required to use this feature.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }

    }

    private void getUserLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        receiveLocation(latitude, longitude);
                        // Do something with the latitude and longitude
                        Log.d("Location", "Latitude: " + latitude + ", Longitude: " + longitude);
                    } else {
                        // Handle the case where the location is null
                        Log.d("Location", "Location is null");
                    }
                })
                .addOnFailureListener(this, e -> {
                    // Handle failure to get location
                    Log.e("Location", "Error getting location", e);
                });
    }



    @Override
    protected void onResume() {
        super.onResume();

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    public void showUpButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void hideUpButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void onLocationChanged(Location location) {

        Double lat = location.getLatitude();
        Double lng = location.getLongitude();

        Log.i("Location info: Lat", lat.toString());
        Log.i("Location info: Lng", lng.toString());

    }
}
