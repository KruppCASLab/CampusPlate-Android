package com.example.campusplate_android.ui.viewlisting;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.campusplate_android.MainActivity;
import com.example.campusplate_android.Model.FoodStopsModel;
import com.example.campusplate_android.Model.ListingModel;
import com.example.campusplate_android.Model.ReservationModel;
import com.example.campusplate_android.Model.Types.FoodStop;
import com.example.campusplate_android.Model.Types.Listing;
import com.example.campusplate_android.Model.Types.Reservation;
import com.example.campusplate_android.R;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ViewListingFragment extends Fragment {

    private Listing listing;
    private ListingModel listingModel;
    private FoodStopsModel foodStopsModel;
    private ReservationModel reservationModel;
    private Context mActivity;
    private Reservation reservation;

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

        final Bundle foodStopBundle = new Bundle();
        TextView title = view.findViewById(R.id.textView_displayTitle);
        final TextView locationDescription = view.findViewById(R.id.textView_displayLocationDescription);
        TextView datePosted = view.findViewById(R.id.date_posted);
        TextView timeLeft = view.findViewById(R.id.time_left);
        final ImageView image = view.findViewById(R.id.listingImage);
        final EditText quantity = view.findViewById(R.id.editText_quantityToPickUp);


        if(getArguments() != null){
            int listingId = getArguments().getInt("listingId");
            int foodStopId = getArguments().getInt("foodStopId");


            List<FoodStop> foodStops = foodStopsModel.getCachedFoodStops();
            List<Listing> listings = listingModel.getAllListings();
            listing = getListingsViewListings(listingId, listings);
            FoodStop foodStop = getFoodStopViewListing(listing, foodStops);



                title.setText(listing.title);
                foodStopBundle.putInt("foodStopId", listing.foodStopId);


            Date date = new Date(listing.creationTime*1000L);

            long diff = Math.abs(System.currentTimeMillis() - listing.creationTime*1000L);
            long time = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            datePosted.setText(String.format("Posted %d days ago", time));
            timeLeft.setText("\u26A0 Discarded in 3 days");
            locationDescription.setText(foodStop.name);

            listingModel.getListingImage(listing.listingId, new ListingModel.GetListingImageCompletionHandler() {
                @Override
                public void success(Bitmap imageData) {
                    image.setImageBitmap(imageData);
                }

                @Override
                public void error(int errorCode) {

                }
            });




        }
        else{
            Toast.makeText(mActivity, "There was an error.", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(view).navigate(R.id.action_navigation_viewlisting_pop);
        }

        view.findViewById(R.id.button_pickUpItem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) mActivity).startProgressBar();
                final View root = view;
                new AlertDialog.Builder(mActivity)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Pick Up Item")
                        .setMessage("Are you sure you want to pick up this item?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                String value = quantity.getText().toString();
                                reservation = new Reservation(listing.listingId, Integer.parseInt(value));
                                reservationModel.addReservation(reservation, new ReservationModel.postCompletionHandler() {
                                    @Override
                                    public void success(int code, int status) {
                                        if(status == 0) {
                                            Toast.makeText(mActivity.getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                            foodStopBundle.putInt("code", code);
                                            Navigation.findNavController(root).navigate(R.id.navigation_lisitingConfirmation, foodStopBundle);
                                        }
                                        else if(status == 1){
                                            Toast.makeText(mActivity, "Quantity greater than available amount", Toast.LENGTH_LONG).show();
                                        }
                                        else if(status == 2){
                                            Toast.makeText(mActivity, "Listing no longer exists", Toast.LENGTH_LONG).show();
                                        }
                                        ((MainActivity) mActivity).stopProgressBar();

                                    }

                                    @Override
                                    public void error(VolleyError error) {
                                        int j = 5;
                                    }
                                });




                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((MainActivity) mActivity).stopProgressBar();
                            }
                        })
                        .show();
            }
        });
        return view;
    }

    private FoodStop getFoodStopViewListing(Listing listing, List<FoodStop> foodStops){
        for (int i = 0; i <foodStops.size(); i++) {
            FoodStop foodStop = foodStops.get(i);
            if (listing.foodStopId == foodStop.foodStopId) {

                return foodStops.get(i);

            }
        }
        return null;
    }

    private Listing getListingsViewListings(int listingId, List<Listing> listings){
        for (int i = 0; i <listings.size(); i++) {
            if(listingId == listings.get(i).listingId){
                return listings.get(i);
            }


        }
        return null;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof Activity){
            mActivity = context;
        }
      }
    }

