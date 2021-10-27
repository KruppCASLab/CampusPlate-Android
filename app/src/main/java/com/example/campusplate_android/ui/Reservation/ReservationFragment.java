package com.example.campusplate_android.ui.Reservation;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.example.campusplate_android.Model.FoodStopsModel;
import com.example.campusplate_android.Model.ListingModel;
import com.example.campusplate_android.Model.ReservationModel;
import com.example.campusplate_android.Model.Types.FoodStop;
import com.example.campusplate_android.Model.Types.Listing;
import com.example.campusplate_android.Model.Types.Reservation;
import com.example.campusplate_android.R;
import com.example.campusplate_android.dummy.DummyContent;
import com.example.campusplate_android.ui.alllistings.AllListingsFragment;
import com.example.campusplate_android.ui.mylistings.MyListingsFragment;

import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class ReservationFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private ReservationModel reservationModel;
    private MyReservationRecyclerViewAdapter adapter;
  //  private OnListFragmentInteractionListener mlistner;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */


    public ReservationFragment() {
    }



    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ReservationFragment newInstance(int columnCount) {
        ReservationFragment fragment = new ReservationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        reservationModel = ReservationModel.getSharedInstance();

        final View view = inflater.inflate(R.layout.fragment_reservation_, container, false);
        RecyclerView recycler = view.findViewById(R.id.view_recycler_reservations);

        // Set the adapter

            Context context = view.getContext();

            if (mColumnCount <= 1) {
                recycler.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recycler.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            adapter = new MyReservationRecyclerViewAdapter(reservationModel.getAllReservations());// what needs to be added
            recycler.setAdapter(adapter);









        return view;
    }

//    public interface OnListFragmentInteractionListener {
//        void onListFragmentInteraction(Reservation item);
//    }

}