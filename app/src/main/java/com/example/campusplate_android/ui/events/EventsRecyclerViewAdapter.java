package com.example.campusplate_android.ui.events;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.campusplate_android.Model.Types.Event;
import com.example.campusplate_android.R;

import java.util.List;
import com.example.campusplate_android.ui.events.EventsFragment.OnListFragmentInteractionListener;

public class EventsRecyclerViewAdapter extends RecyclerView.Adapter<EventsRecyclerViewAdapter.ViewHolder> {

    private List<Event> mValues;
    private final OnListFragmentInteractionListener mListener;

    public void setEvents(List<Event> events){
        mValues = events;
    }

    public EventsRecyclerViewAdapter(List<Event> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final int pos = position;
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(mValues.get(position).title);
        holder.mDescriptionView.setText(mValues.get(position).locationDescription);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if (null != mListener) {
                Bundle bundle = new Bundle();
                bundle.putInt("index", pos);

                //TODO: Navigate to view event
                //Navigation.findNavController(view).navigate(R.id.action_navigation_alllistings_to_navigation_viewlisting, bundle);

                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                //mListener.onListFragmentInteraction(holder.mItem);
                //}
                //TODO: Am not currently using mListener (not using the activity as the callbacks interface). This may change.
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
        public final TextView mDescriptionView;
        public Event mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = view.findViewById(R.id.textView_eventTitle);
            mDescriptionView = view.findViewById(R.id.textView_eventDescription);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mDescriptionView.getText() + "'";
        }
    }
}
