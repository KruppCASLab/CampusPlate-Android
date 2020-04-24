package com.example.campusplate_android.ui.mylistings;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusplate_android.Model.ListingModel;
import com.example.campusplate_android.Model.Types.Listing;
import com.example.campusplate_android.R;
import com.example.campusplate_android.ui.alllistings.AllListingsFragment;
import com.example.campusplate_android.ui.alllistings.MyAllListingsRecyclerViewAdapter;

import java.util.List;

public class MyListingsFragment extends Fragment {

    public static ListingModel listingModel;
    private AllListingsFragment.OnListFragmentInteractionListener mListener;
    private int mColumnCount = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        listingModel = ListingModel.getSharedInstance(this.getActivity().getApplicationContext());
        View view = inflater.inflate(R.layout.fragment_mylistings, container, false);
        RecyclerView recycler = view.findViewById(R.id.view_recycler_my_listings);

        // Set the adapter
        Context context = view.getContext();
        if (mColumnCount <= 1) {
            recycler.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recycler.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        final MyAllListingsRecyclerViewAdapter adapter = new MyAllListingsRecyclerViewAdapter(listingModel.getUserListings(-1), mListener);
        //TODO: Get user's ID and put in there

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
                adapter.setListings(listingModel.getUserListings(-1));
                //TODO: Get user's ID and put in there

                adapter.notifyDataSetChanged();
            }
        });
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}