package com.example.campusplate_android.ui.addlisting;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.campusplate_android.MainActivity;
import com.example.campusplate_android.Model.ListingModel;
import com.example.campusplate_android.Model.Types.Listing;
import com.example.campusplate_android.R;

public class AddListingFragment extends Fragment {

    private ListingModel listingModel;
    private Context mActivity;

    public static AddListingFragment newInstance() {
        return new AddListingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_listing, container, false);
        listingModel = ListingModel.getSharedInstance(this.getActivity().getApplicationContext());

        final EditText titleView = view.findViewById(R.id.editText_addTitle);
        final EditText quantityView = view.findViewById(R.id.editText_addQuantity);
        //TODO: Require not empty

        view.findViewById(R.id.button_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Location currentLocation = ((MainActivity) mActivity).getCurrentLocation();
                Listing testListing = new Listing(titleView.getText().toString(), Integer.parseInt(quantityView.getText().toString()), currentLocation.getLatitude(), currentLocation.getLongitude());
                listingModel.postListing(new ListingModel.PostListingCompletionHandler() {
                    @Override
                    public void postListing() {
                        Toast.makeText(mActivity, "Listing Added!", Toast.LENGTH_SHORT).show();
                        //TODO: Pop here instead of after
                    }
                }, testListing);
                Navigation.findNavController(view).navigate(R.id.action_addListing_pop);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof Activity){
            mActivity = context;
        }
    }
}
