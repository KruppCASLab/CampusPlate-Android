package com.example.campusplate_android;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        ServiceClient.getInstance(this.getApplicationContext());

        setContentView(R.layout.activity_main);
        stopProgressBar();
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_events, R.id.navigation_alllistings, R.id.navigation_mylistings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);


        //Location stuff






    }

    public void startProgressBar() {
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.bringToFront();
    }

    public void stopProgressBar() {
        findViewById(R.id.progressBar).setVisibility(View.GONE);
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
}
