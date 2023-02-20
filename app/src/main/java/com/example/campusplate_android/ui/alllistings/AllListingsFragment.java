package com.example.campusplate_android.ui.alllistings;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.example.campusplate_android.CredentialManager;
import com.example.campusplate_android.MainActivity;
import com.example.campusplate_android.Model.FoodStopsModel;
import com.example.campusplate_android.Model.ListingModel;
import com.example.campusplate_android.Model.ReservationModel;
import com.example.campusplate_android.Model.Types.FoodStop;
import com.example.campusplate_android.Model.Types.Listing;
import com.example.campusplate_android.Model.Types.Reservation;
import com.example.campusplate_android.R;
import com.example.campusplate_android.SharedPreferencesManager;
import com.example.campusplate_android.SignUpActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class AllListingsFragment extends Fragment implements OnMapReadyCallback {

    private ListingModel listingModel;
    private FoodStopsModel foodStopsModel;
    private ReservationModel reservationModel;
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
    public interface CompletionHandler{
        void success(List<FoodStop> foodStops, List<Listing> listings, List<Reservation> reservations);
        void error(VolleyError volleyError);
    }
    public AllListingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        foodStopsModel = FoodStopsModel.getSharedInstance();
        reservationModel = ReservationModel.getSharedInstance();
        listingModel = ListingModel.getSharedInstance(mActivity.getApplicationContext());    // why does this get shared instance differ
        final  View view = inflater.inflate(R.layout.fragment_all_listings, container, false);
        RecyclerView recycler = view.findViewById(R.id.view_recycler_all_listings);

        view.findViewById(R.id.addListingButton).setVisibility(View.INVISIBLE);
        foodStopsModel.getFoodStopManager(new FoodStopsModel.getCompletionHandler() {

            @Override
            public void success(List<FoodStop> foodStops) {
                if(!foodStops.isEmpty()){
                    view.findViewById(R.id.addListingButton).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void error(VolleyError error) {
                // forget about it
            }
        });




        // Set the adapter
        Context context = view.getContext();
        if (mColumnCount <= 1) {
            recycler.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recycler.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        adapter = new AllListingsRecyclerViewAdapter(listingModel.getAllListings(), foodStopsModel.getCachedFoodStops(), mListener);

        recycler.setAdapter(adapter);

        ((MainActivity) mActivity).startProgressBar();
        adapter.isClickable = false;



        recycler.addItemDecoration(new DividerItemDecoration(recycler.getContext(), 0));

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        fetchTimelineAsync(new CompletionHandler() {
            @Override

            public void success(List<FoodStop> foodStops, List<Listing> listings, List<Reservation> reservations) {

                try {
                    ((MainActivity) mActivity).stopProgressBar();
                    adapter.isClickable = true;
                } catch (NullPointerException exception) {
                    //TODO: Do something with exception
                }
                adapter.setListings(listings);
                adapter.setFoodstops(foodStops);
                adapter.notifyDataSetChanged();

            }


            @Override
            public void error(VolleyError volleyError) {

                //TODO: allow alert dialog to send user back to sign up screen & check for 401- network response
                if(volleyError.networkResponse.statusCode == 401){
                    //TODO: Make alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(R.string.dialogue_message)
                    .setTitle(R.string.alert_title);

                    builder.setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                            Intent intent = new Intent(mActivity.getApplicationContext(), SignUpActivity.class);
                            SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(getActivity().getApplicationContext().getSharedPreferences("CampusPlate", Context.MODE_PRIVATE));
                            final CredentialManager credentialManager = new CredentialManager(sharedPreferencesManager);
                            credentialManager.removeUserCredentials();
                            startActivity(intent);

                        }

                    });
                    AlertDialog dialog = builder.create();
                        dialog.show();
                }
            }
        });

        swipeContainer = view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchTimelineAsync(new CompletionHandler() {
                    @Override
                    public void success(List<FoodStop> foodStops, List<Listing> listings, List<Reservation> reservations) {
                        adapter.setListings(listings); // is this supposed to show listing in swipe container
                        adapter.notifyDataSetChanged();
                        swipeContainer.setRefreshing(false);
                    }

                    @Override
                    public void error(VolleyError volleyError) {

                    }
                });
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        view.findViewById(R.id.addListingButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View root = view;
                Navigation.findNavController(root).navigate(R.id.action_navigation_alllistings_to_navigation_addlisting);
            }
        });


        return view;
    }

    private void fetchTimelineAsync(final CompletionHandler completionHandler) { // what does this do fetch time
        foodStopsModel.getFoodStops(new FoodStopsModel.getCompletionHandler() {
            @Override
            public void success(final List<FoodStop> foodStops) {
                listingModel.getListings(new ListingModel.GetListingsCompletionHandler() {
                    @Override
                    public void receiveListings(final List<Listing> listings) {
                        reservationModel.getReservations(new ReservationModel.getCompletionHandler() {
                            @Override
                            public void success(List<Reservation> reservations) {
                                if(map != null){
                                    drawMap(map, foodStops);

                                }
                                completionHandler.success(foodStops, listings, reservations);
                            }
                            @Override
                            public void error(VolleyError error) {
                                completionHandler.error(error);
                            }
                        });
                    }
                });
            }

            @Override
            public void error(VolleyError error) {
                completionHandler.error(error);
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

    public void drawMap(GoogleMap map, List<FoodStop> foodStops){

        Bundle foodStopsAndListingBundle = new Bundle();
        for (int i = 0; i <foodStops.size(); i++) {
            FoodStop foodStop = foodStops.get(i);

            map.addMarker(new MarkerOptions()
                    .position(new LatLng(foodStop.lat, foodStop.lng))
                    .title(foodStop.name));

        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(false);

            LatLng latLng = new LatLng(41.3711, -81.8478);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.5f));
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
