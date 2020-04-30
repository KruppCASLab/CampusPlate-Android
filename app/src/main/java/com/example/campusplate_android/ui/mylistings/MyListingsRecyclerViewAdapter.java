package com.example.campusplate_android.ui.mylistings;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.campusplate_android.Model.Types.Listing;
import com.example.campusplate_android.R;

import java.util.List;

public class MyListingsRecyclerViewAdapter extends RecyclerView.Adapter<MyListingsRecyclerViewAdapter.ViewHolder> {

    private List<Listing> mValues;
    private final MyListingsFragment.OnListFragmentInteractionListener mListener;
    public boolean isClickable = true;

    public void setListings(List<Listing> listings) {
        mValues = listings;
    }

    public MyListingsRecyclerViewAdapter(List<Listing> items, MyListingsFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
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
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(mValues.get(position).title);
        holder.mLocationDescriptionView.setText(mValues.get(position).locationDescription);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isClickable) {
                    //if (null != mListener) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("index", pos);

                    Navigation.findNavController(view).navigate(R.id.action_navigation_mylistings_to_navigation_editlisting, bundle);

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

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = view.findViewById(R.id.item_title);
            mLocationDescriptionView = view.findViewById(R.id.item_location_description);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mLocationDescriptionView.getText() + "'";
        }
    }
}
