package com.example.campusplate_android.ui.Reservation;

import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.campusplate_android.Model.FoodStopsModel;
import com.example.campusplate_android.Model.ListingModel;
import com.example.campusplate_android.Model.ReservationModel;
import com.example.campusplate_android.Model.Types.FoodStop;
import com.example.campusplate_android.Model.Types.Listing;
import com.example.campusplate_android.Model.Types.Reservation;
import com.example.campusplate_android.R;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;


public class MyReservationRecyclerViewAdapter extends RecyclerView.Adapter<MyReservationRecyclerViewAdapter.ViewHolder> {

    private  List<Reservation> reservations;

    public void setReservation(List<Reservation> reservations){
        this.reservations = reservations;
    }



    public MyReservationRecyclerViewAdapter(List<Reservation> reservations) {
        this.reservations = reservations;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reservation_row, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final int pos = position;

        holder.reservation = reservations.get(position);


        List<Listing> listings = ListingModel.getSharedInstance(null).getAllListings();
        List<FoodStop> foodStops =  FoodStopsModel.getSharedInstance().getCachedFoodStops();
        Reservation reservation = holder.reservation;
        Listing listing = null;
        FoodStop foodStop =null;

        for(Listing aListing: listings) {
            if (aListing.listingId == holder.reservation.listingId) {
                // We have the listing
                listing = aListing;

            }
        }

        for(FoodStop aFoodStop: foodStops){
            if(aFoodStop.foodStopId == listing.foodStopId){
                foodStop = aFoodStop;
            }
        }


        int count = 0;




        //TODO: get the mapping for values
        holder.mFoodStopTitle.setText(foodStop.name);
        holder.mDescriptionView.setText(listing.description);
        holder.mReservationAmount.setText(Integer.toString(reservation.quantity));
        holder.mReservationCode.setText(Integer.toString(reservation.code));

        long time = reservation.timeExpired - (new Date()).getTime() / 1000;
        long minutes = time/ 60;
        String format = "mm";



        holder.mExpirationTime.setText(String.format("%d minutes", (int) minutes));

        String hexColor = "#" + foodStop.hexColor;
        holder.mHexColor.setColorFilter(Color.parseColor(hexColor));


    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

       public final TextView mDescriptionView;
       public final TextView mFoodStopTitle;
       public final TextView mReservationAmount;
       public final TextView mExpirationTime;
       public final ImageView mHexColor;
       public final TextView mReservationCode;


        public Reservation reservation;
        public FoodStop mFoodStopItem;
        public Listing mListingItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

          mDescriptionView =  view.findViewById(R.id.reservationDescription);
          mFoodStopTitle = view.findViewById(R.id.ReservationfoodStopName);
          mReservationAmount = view.findViewById(R.id.reservationAmount);
          mExpirationTime = view.findViewById(R.id.expirationTime);
          mHexColor = view.findViewById(R.id.hexColor);
          mReservationCode = view.findViewById(R.id.reservationCode);

        }


    }
}