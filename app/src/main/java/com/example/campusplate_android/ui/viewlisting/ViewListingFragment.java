package com.example.campusplate_android.ui.viewlisting;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.campusplate_android.Model.ListingModel;
import com.example.campusplate_android.Model.Types.Listing;
import com.example.campusplate_android.R;

public class ViewListingFragment extends Fragment {

    private Listing listing;
    private ListingModel listingModel;
    private Context mActivity;

    public static ViewListingFragment newInstance() {
        return new ViewListingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_listing_fragment, container, false);
        listingModel = ListingModel.getSharedInstance(mActivity.getApplicationContext());

        TextView title = view.findViewById(R.id.textView_displayTitle);
        TextView locationDescription = view.findViewById(R.id.textView_displayLocationDescription);
        TextView quantity = view.findViewById(R.id.textView_displayQuantity);
        TextView creationTime = view.findViewById(R.id.textView_displayCreationTime);

        if(getArguments() != null){
            listing = listingModel.getListing(getArguments().getInt("index"));
            title.setText(listing.title);
            locationDescription.setText(listing.locationDescription);
            quantity.setText(Integer.toString(listing.quantity));
            creationTime.setText(Integer.toString(listing.creationTime));
        }
        else{
            //Show error, go back to AllListingsFragment
        }

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof Activity){
            mActivity = context;
        }
    }
}
