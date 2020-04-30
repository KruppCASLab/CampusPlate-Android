package com.example.campusplate_android.ui.viewlisting;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.campusplate_android.MainActivity;
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
        View view = inflater.inflate(R.layout.fragment_viewlisting, container, false);
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
            Toast.makeText(mActivity, "There was an error.", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(view).navigate(R.id.action_navigation_viewlisting_pop);
        }

        view.findViewById(R.id.button_pickUpItem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedPickUpButton(view);
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof Activity){
            mActivity = context;
        }
    }

    public void clickedPickUpButton(View view){
        final View root = view;
        new AlertDialog.Builder(mActivity)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Pick Up Item")
                .setMessage("Are you sure you want to pick up this item?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        Location currentLocation = ((MainActivity) mActivity).getCurrentLocation(); //TODO: Use this to block pickup if too far away
                        listingModel.deleteListing(new ListingModel.DeleteListingCompletionHandler() {
                            @Override
                            public void deleteListing() {
                                Toast.makeText(mActivity, "Successfully Picked Up Item!", Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(root).navigate(R.id.action_navigation_viewlisting_pop);
                            }
                        }, listing.listingId);
                    }
                })
                .setNegativeButton("No", null)
                .show();
        //TODO: Show a confirmation screen
        //TODO: Instead of deleting item in database, set its status to picked up/inactive
    }
}
