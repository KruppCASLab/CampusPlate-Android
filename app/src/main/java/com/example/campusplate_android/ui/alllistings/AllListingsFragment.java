package com.example.campusplate_android.ui.alllistings;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.campusplate_android.MainActivity;
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

    private ListingModel listingModel;
    private GoogleMap map;
    private SwipeRefreshLayout swipeContainer;
    private OnListFragmentInteractionListener mListener;
    private Context mActivity;
    private AllListingsRecyclerViewAdapter adapter;
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
        listingModel = ListingModel.getSharedInstance(mActivity.getApplicationContext());
        View view = inflater.inflate(R.layout.fragment_all_listings, container, false);
        RecyclerView recycler = view.findViewById(R.id.view_recycler_all_listings);

        // Set the adapter
        Context context = view.getContext();
        if (mColumnCount <= 1) {
            recycler.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recycler.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        adapter = new AllListingsRecyclerViewAdapter(listingModel.getAllListings(), mListener);

        recycler.setAdapter(adapter);

        ((MainActivity) mActivity).findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        adapter.isClickable = false;

        listingModel.getListings(new ListingModel.GetListingsCompletionHandler() {
            @Override
            public void receiveListings(List<Listing> listings) {
                try {
                    ((MainActivity) mActivity).findViewById(R.id.progressBar).setVisibility(View.GONE);
                    adapter.isClickable = true;
                } catch (NullPointerException exception) {
                    //TODO: Do something with exception
                }
                adapter.setListings(listings);
                adapter.notifyDataSetChanged();
            }
        });
        recycler.addItemDecoration(new DividerItemDecoration(recycler.getContext(), DividerItemDecoration.VERTICAL));

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        swipeContainer = view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchTimelineAsync();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return view;
    }

    private void fetchTimelineAsync() {
        listingModel.getListings(new ListingModel.GetListingsCompletionHandler() {
            @Override
            public void receiveListings(List<Listing> listings) {
                adapter.setListings(listings);
                adapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof Activity){
            mActivity = context;
        }
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
        void onListFragmentInteraction(Listing item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (mActivity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
        else {
            map.setMyLocationEnabled(true);
            for (int i = 0; i < listingModel.getAllListings().size(); i++) {
                Listing listing = listingModel.getListing(i);
                map.addMarker(new MarkerOptions().position(new LatLng(listing.lat, listing.lng)).title(listing.title));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay!
            } else {
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            }
        }
    }
}
