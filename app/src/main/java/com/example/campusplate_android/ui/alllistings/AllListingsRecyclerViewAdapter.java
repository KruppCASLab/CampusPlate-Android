package com.example.campusplate_android.ui.alllistings;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.campusplate_android.Model.Types.FoodStop;
import com.example.campusplate_android.Model.Types.Listing;
import com.example.campusplate_android.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.example.campusplate_android.ui.alllistings.AllListingsFragment.OnListFragmentInteractionListener;

public class AllListingsRecyclerViewAdapter extends RecyclerView.Adapter<AllListingsRecyclerViewAdapter.ViewHolder> {

    private List<Listing> mValues;
    private List<FoodStop> mStops;

    private final OnListFragmentInteractionListener mListener;
    public boolean isClickable = true;

    public void setListings(List<Listing> listings) {
        mValues = listings;
    }
    public void setFoodstops(List<FoodStop> foodstops) {
        mStops = foodstops;
    }
    public AllListingsRecyclerViewAdapter(List<Listing> items,List<FoodStop> foodStops ,OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        mStops = foodStops;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listing_row, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final int pos = position;
        Date date = new Date(mValues.get(position).creationTime *1000L);

        long diff = Math.abs(System.currentTimeMillis() - mValues.get(position).creationTime *1000L);
        long time = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);


        holder.mItem = mValues.get(position);

        holder.mTitleView.setText(mValues.get(position).title);
        //TODO:Update to value once determined
        holder.mLocationDescriptionView.setText(String.format("%s", time));
        for(int i = 0; i < mStops.size(); i++){
            String hexColor = "#" + mStops.get(i).hexColor;
            if(mValues.get(position).foodStopId == mStops.get(i).foodStopId){
                holder.mline.setColorFilter(Color.parseColor(hexColor));
            }
        }


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isClickable) {
                    //if (null != mListener) {
                    Bundle bundle = new Bundle();

                    bundle.putInt("index", pos);
                    bundle.putInt("listingId", mValues.get(pos).listingId);
                    bundle.putInt("foodStopId",mValues.get(pos).foodStopId);
                    bundle.putInt("index", pos);

                    Navigation.findNavController(view).navigate(R.id.action_navigation_alllistings_to_navigation_viewlisting, bundle);

                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    //mListener.onListFragmentInteraction(holder.mItem);
                    //}
                    //TODO: Am not currently using mListener (not using the activity as the callbacks interface). This may change.
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
        public final TextView mLocationDescriptionView;
        public Listing mItem;
        public FoodStop mStop;
        public final ImageView mline;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = view.findViewById(R.id.item_title);
            mLocationDescriptionView = view.findViewById(R.id.item_date);
            mline = view.findViewById(R.id.indicator_line);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mLocationDescriptionView.getText() + "'";
        }
    }
}
