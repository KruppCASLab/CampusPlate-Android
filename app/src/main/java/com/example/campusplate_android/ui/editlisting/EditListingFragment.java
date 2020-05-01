package com.example.campusplate_android.ui.editlisting;

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
import android.widget.Switch;
import android.widget.Toast;

import com.example.campusplate_android.MainActivity;
import com.example.campusplate_android.Model.ListingModel;
import com.example.campusplate_android.Model.Types.Listing;
import com.example.campusplate_android.R;

public class EditListingFragment extends Fragment {

    private ListingModel listingModel;
    private Context mActivity;

    public static EditListingFragment newInstance() {
        return new EditListingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_listing, container, false);
        listingModel = ListingModel.getSharedInstance(mActivity.getApplicationContext());

        final Listing listingToEdit = listingModel.getListing(getArguments().getInt("index"));

        final EditText editTitle = view.findViewById(R.id.editText_editTitle);
        final EditText editQuantity = view.findViewById(R.id.editText_editQuantity);
        final Switch updateLocation = view.findViewById(R.id.switch_updateLocation);

        editTitle.setText(listingToEdit.title);
        editQuantity.setText(Integer.toString(listingToEdit.quantity));

        view.findViewById(R.id.button_submitEdit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View root = view;
                Listing listing = new Listing(listingToEdit);
                listing.title = editTitle.getText().toString();
                listing.quantity = Integer.parseInt(editQuantity.getText().toString());

                if(updateLocation.isChecked()){
                    Location currentLocation = ((MainActivity) mActivity).getCurrentLocation();
                    listing.lat = currentLocation.getLatitude();
                    listing.lng = currentLocation.getLongitude();
                }
                listingModel.editListing(new ListingModel.EditListingCompletionHandler() {
                    @Override
                    public void editListing() {
                        Toast.makeText(mActivity, "Listing Updated!", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(root).navigate(R.id.action_navigation_editlisting_pop);
                    }
                }, listing, listingToEdit.listingId);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = context;
        }
    }
}
