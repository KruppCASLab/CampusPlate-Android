package com.example.campusplate_android.ui.viewlisting;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.campusplate_android.LocationManager;
import com.example.campusplate_android.MainActivity;
import com.example.campusplate_android.Model.FoodStopsModel;
import com.example.campusplate_android.Model.ListingModel;
import com.example.campusplate_android.Model.ReservationModel;
import com.example.campusplate_android.Model.Types.FoodStop;
import com.example.campusplate_android.Model.Types.Listing;
import com.example.campusplate_android.Model.Types.Reservation;
import com.example.campusplate_android.R;

import org.w3c.dom.Text;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.Timer;

public class ViewListingFragment extends Fragment implements LocationManager.LocationManagerDelegate {

    private Listing listing;
    private ListingModel listingModel;
    private FoodStopsModel foodStopsModel;
    private ReservationModel reservationModel;
    private Context mActivity;
    private Reservation reservation;
    private int quantityNumber = 0;
    public Timer timer;
    public TimerTask timerTask;
    public TextView button_pickup;
    private ColorStateList list;

    private String foodStopType = "unmanaged";
    private boolean isReservable = false;
    private FoodStop foodStop;

    private double lastSensedLat, lastSensedLng;

    public static ViewListingFragment newInstance() {
        return new ViewListingFragment();
    }

    public ViewListingFragment() {

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_view_listing, container, false);
        listingModel = ListingModel.getSharedInstance(mActivity.getApplicationContext());
        foodStopsModel = FoodStopsModel.getSharedInstance();
        reservationModel = ReservationModel.getSharedInstance();

        timer = new Timer();

        button_pickup = view.findViewById(R.id.button_pickUpItem);

        list = button_pickup.getBackgroundTintList();


        LocationManager.getInstance().setDelegate(this);

        timerTask = new TimerTask() {
            @Override
            public void run() {
                LocationManager.getInstance().getUserLocation();
                Log.i("Testing", "Got Location");

                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Log.i("Permission", "Now has precise permissions");
                    button_pickup.setEnabled(true);
                }
            }
        };

        timer.scheduleAtFixedRate(timerTask, 0, 1000);

        final Bundle foodStopBundle = new Bundle();
        TextView title = view.findViewById(R.id.textView_displayTitle);
        final TextView locationDescription = view.findViewById(R.id.textView_displayLocationDescription);
        TextView datePosted = view.findViewById(R.id.date_posted);
        final ImageView image = view.findViewById(R.id.listingImage);
        TextView viewItemDescription = view.findViewById(R.id.viewItemDescription);
        final TextView quantityToPickUp = view.findViewById(R.id.quantityToPickUp);
        Button increase = view.findViewById(R.id.increase);
        Button decrease = view.findViewById(R.id.decrease);

        if (getArguments() != null) {
            int listingId = getArguments().getInt("listingId");
            int foodStopId = getArguments().getInt("foodStopId");


            List<FoodStop> foodStops = foodStopsModel.getCachedFoodStops();
            List<Listing> listings = listingModel.getAllListings();
            listing = getListingsViewListings(listingId, listings);
            foodStop = getFoodStopViewListing(listing, foodStops);

            this.foodStopType = foodStop.type;

            if (foodStopType.equals("unmanaged")) {
                button_pickup.setText("Retrieve");
            } else if (foodStopType.equals("managed")) {
                button_pickup.setText("Reserve");
            }

            if (!isReservable) {
                button_pickup.setVisibility(View.GONE);
            }

            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                button_pickup.setEnabled(false);
                Toast.makeText(requireContext(), "Location permission is required to use this feature.", Toast.LENGTH_LONG).show();
                button_pickup.getBackground().setTint(Color.GRAY);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    }
                }, 5000);
            }

            title.setText(listing.title);
            quantityToPickUp.setText(calculateQuantity(1, listing.quantityRemaining));

            foodStopBundle.putInt("foodStopId", listing.foodStopId);

            increase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    quantityToPickUp.setText(calculateQuantity(1, listing.quantityRemaining));

                }
            });
            decrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    quantityToPickUp.setText(calculateQuantity(-1, listing.quantityRemaining));


                }
            });


            Date date = new Date(listing.creationTime * 1000L);

            long diff = Math.abs(System.currentTimeMillis() - listing.creationTime * 1000L);
            long time = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            datePosted.setText(String.format("Posted %d days ago", time));
            locationDescription.setText(foodStop.name);
            viewItemDescription.setText(listing.description);

            listingModel.getListingImage(listing.listingId, new ListingModel.GetListingImageCompletionHandler() {
                @Override
                public void success(Bitmap imageData) {
                    image.setImageBitmap(imageData);
                }

                @Override
                public void error(int errorCode) {

                }
            });


        } else {
            Toast.makeText(mActivity, "There was an error.", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(view).navigate(R.id.action_navigation_viewlisting_pop);
        }

        view.findViewById(R.id.button_pickUpItem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View root = view;
                new AlertDialog.Builder(mActivity)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle((foodStopType.equals("unmanaged")) ? "Retrieve Item" : "Reserve Item")
                        .setCancelable(false)
                        .setMessage((foodStopType.equals("unmanaged")) ? "Are you sure you want to retrieve this item?" : "Are you sure you want to reserve this item?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                String value = String.valueOf(quantityNumber);
                                if (foodStopType.equals("managed")) {
                                    reservation = new Reservation(listing.listingId, Integer.parseInt(value));
                                }
                                else {
                                    reservation = new Reservation(listing.listingId, Integer.parseInt(value), lastSensedLat, lastSensedLng);
                                }
                                reservationModel.addReservation(reservation, new ReservationModel.postCompletionHandler() {
                                    @Override
                                    public void success(int code, int status) {
                                        if (status == 0) {
                                            quantityNumber = 0;
                                            foodStopBundle.putInt("code", code);
                                            Navigation.findNavController(root).navigate(R.id.navigation_lisitingConfirmation, foodStopBundle);
                                        } else if (status == 1) {
                                            Toast.makeText(mActivity, "Quantity greater than available amount", Toast.LENGTH_LONG).show();
                                        } else if (status == 2) {
                                            Toast.makeText(mActivity, "Listing no longer exists", Toast.LENGTH_LONG).show();
                                        } else if (status == 3) {
                                            //quantityNumber = 0;
                                            Navigation.findNavController(root).navigate(R.id.listingConfirmationRetrieval, foodStopBundle);
                                        }
                                        else if (status == 4) {
                                            Toast.makeText(mActivity, "Please make sure you are near the food stop", Toast.LENGTH_LONG).show();
                                        }


                                    }

                                    @Override
                                    public void error(VolleyError error) {
                                        Toast.makeText(mActivity, R.string.networkError, Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        })

                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }
        });
        return view;
    }

    private String calculateQuantity(int number, int totalQuantity) {
        if (quantityNumber + number >= 0 && quantityNumber + number <= totalQuantity) {
            this.quantityNumber += number;
        }
        if (totalQuantity == 0) {
            return "0/0";
        }

        return String.format("%d/%d", quantityNumber, totalQuantity);

    }

    private FoodStop getFoodStopViewListing(Listing listing, List<FoodStop> foodStops) {
        for (int i = 0; i < foodStops.size(); i++) {
            FoodStop foodStop = foodStops.get(i);
            if (listing.foodStopId == foodStop.foodStopId) {
                return foodStops.get(i);
            }
        }
        return null;
    }

    private Listing getListingsViewListings(int listingId, List<Listing> listings) {
        for (int i = 0; i < listings.size(); i++) {
            if (listingId == listings.get(i).listingId) {
                return listings.get(i);
            }
        }
        return null;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = context;
        }
    }

    @Override
    public void receiveLocation(double lat, double lng) {
        if (foodStop.type.equals("unmanaged")) {
            this.lastSensedLat = lat;
            this.lastSensedLng = lng;

            double threshold = 0.0004;


            if ((lat < (foodStop.lat + threshold)) && (lat > (foodStop.lat - threshold)) &&
                    (lng < (foodStop.lng + threshold)) && (lng > (foodStop.lng - threshold))) {
                getActivity().runOnUiThread(() -> {
                    button_pickup.setEnabled(true);
                });
            } else {
                getActivity().runOnUiThread(() -> {
                    button_pickup.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                    button_pickup.setEnabled(false);
                });
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    @Override
    public void onPause() {
        super.onPause();

        timer.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();

        timerTask = new TimerTask() {
            @Override
            public void run() {
                LocationManager.getInstance().getUserLocation();
                Log.i("Testing", "Got Location 2");

                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Log.i("Permission", "Now has precise permissions");
//                    getActivity().runOnUiThread(() -> {
//                        button_pickup.setEnabled(true);
//                        button_pickup.setBackgroundTintList(list);
//                        //button_pickup.getResources().getColorStateList(R.color.buttonColorPrimary);
//                    });
                }
            }
        };
        if (foodStop.type.equals("unmanaged")) {
            timer.scheduleAtFixedRate(timerTask, 0, 1000);
        }
    }

//    @Override
//    public void onConfigurationChanged() {
//        super.onConfigurationChanged();
//
//    }
}


