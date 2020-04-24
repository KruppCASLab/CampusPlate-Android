package com.example.campusplate_android.ui.alllistings;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.campusplate_android.Model.ListingModel;
import com.example.campusplate_android.Model.Types.Listing;
import com.example.campusplate_android.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class AllListingsFragment extends Fragment implements OnMapReadyCallback {

    public static ListingModel listingModel;
    private GoogleMap map;
    private OnListFragmentInteractionListener mListener;
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AllListingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        listingModel = ListingModel.getSharedInstance(this.getActivity().getApplicationContext());
        View view = inflater.inflate(R.layout.fragment_alllistings, container, false);
        RecyclerView recycler = view.findViewById(R.id.view_recycler_all_listings);

        // Set the adapter
        Context context = view.getContext();
        if (mColumnCount <= 1) {
            recycler.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recycler.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        final MyAllListingsRecyclerViewAdapter adapter = new MyAllListingsRecyclerViewAdapter(listingModel.getAllListings(), mListener);

        recycler.setAdapter(adapter);

        getActivity().findViewById(R.id.progressBar).setVisibility(View.VISIBLE);

        listingModel.getListings(new ListingModel.GetListingsCompletionHandler() {
            @Override
            public void receiveListings(List<Listing> listings) {
                try {
                    getActivity().findViewById(R.id.progressBar).setVisibility(View.GONE);
                } catch (NullPointerException exception) {
                    //TODO: Do something with exception
                }
                adapter.setListings(listings);
                adapter.notifyDataSetChanged();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        recycler.addItemDecoration(new DividerItemDecoration(recycler.getContext(), DividerItemDecoration.VERTICAL));

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Listing item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if(getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }

        else{
            map.setMyLocationEnabled(true);
            for (int i = 0; i < listingModel.getAllListings().size(); i++) {
                Listing listing = listingModel.getListing(i);
                map.addMarker(new MarkerOptions().position(new LatLng(listing.lat, listing.lng)).title(listing.title));
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                    return;
                } else {
                    this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                }
                return;
            }
        }
    }
}
