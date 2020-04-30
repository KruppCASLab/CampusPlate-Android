package com.example.campusplate_android.ui.mylistings;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusplate_android.MainActivity;
import com.example.campusplate_android.Model.ListingModel;
import com.example.campusplate_android.Model.Types.Listing;
import com.example.campusplate_android.R;

import java.util.List;

public class MyListingsFragment extends Fragment {

    private ListingModel listingModel;
    private OnListFragmentInteractionListener mListener;
    private Context mActivity;
    private int mColumnCount = 1;
    private MyListingsRecyclerViewAdapter adapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        listingModel = ListingModel.getSharedInstance(mActivity.getApplicationContext());
        View view = inflater.inflate(R.layout.fragment_my_listings, container, false);
        RecyclerView recycler = view.findViewById(R.id.view_recycler_my_listings);

        // Set the adapter
        Context context = view.getContext();
        if (mColumnCount <= 1) {
            recycler.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recycler.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        final MyListingsRecyclerViewAdapter adapter = new MyListingsRecyclerViewAdapter(listingModel.getUserListings(-1), mListener);
        //TODO: Get user's ID and put in there

        recycler.setAdapter(adapter);

        ((MainActivity) mActivity).findViewById(R.id.progressBar).setVisibility(View.VISIBLE);

        listingModel.getListings(new ListingModel.GetListingsCompletionHandler() {
            @Override
            public void receiveListings(List<Listing> listings) {
                try {
                    ((MainActivity) mActivity).findViewById(R.id.progressBar).setVisibility(View.GONE);
                } catch (NullPointerException exception) {
                    //TODO: Do something with exception
                }
                adapter.setListings(listingModel.getUserListings(-1));
                //TODO: Get user's ID and put in there

                adapter.notifyDataSetChanged();
            }
        });
        recycler.addItemDecoration(new DividerItemDecoration(recycler.getContext(), DividerItemDecoration.VERTICAL));

        view.findViewById(R.id.button_add_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_navigation_mylistings_to_addListing);
            }
        });
        return view;
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Listing item);
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
}