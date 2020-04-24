package com.example.campusplate_android.ui.addlisting;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.campusplate_android.Model.ListingModel;
import com.example.campusplate_android.Model.Types.Listing;
import com.example.campusplate_android.R;

public class AddListingFragment extends Fragment {

    private ListingModel listingModel;

    public static AddListingFragment newInstance() {
        return new AddListingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_listing, container, false);
        listingModel = ListingModel.getSharedInstance(this.getActivity().getApplicationContext());

        view.findViewById(R.id.button_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Listing testListing = new Listing("bleep bloop", 5);
                listingModel.postListing(new ListingModel.PostListingCompletionHandler() {
                    @Override
                    public void postListing() {
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
}
