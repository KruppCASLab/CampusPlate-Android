package com.example.campusplate_android.ui.listingconfirmation;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.campusplate_android.Model.FoodStopsModel;
import com.example.campusplate_android.Model.ListingModel;
import com.example.campusplate_android.Model.Types.FoodStop;
import com.example.campusplate_android.Model.Types.Listing;
import com.example.campusplate_android.R;
import com.example.campusplate_android.ui.alllistings.AllListingsRecyclerViewAdapter;

import java.util.List;

public class LisitingConfirmationFragment extends Fragment {

    private Listing listing;
    private ListingModel listingModel;
    private FoodStopsModel foodStopsModel;
    private Context mActivity;
    private AllListingsRecyclerViewAdapter adapter;

    private LisitingConfirmationViewModel mViewModel;

    public static LisitingConfirmationFragment newInstance() {
        return new LisitingConfirmationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.lisiting_confirmation_fragment, container, false);

        foodStopsModel = FoodStopsModel.getSharedInstance();

        final ImageView circle = view.findViewById(R.id.indicator_circle);
        final TextView number = view.findViewById(R.id.indicator_number);
        final TextView location = view.findViewById(R.id.foodStopName);
        final TextView address = view.findViewById(R.id.foodStopAddress);
        final TextView confCode = view.findViewById(R.id.confCode);

        if(getArguments() != null){
            final int foodStopId = getArguments().getInt("foodStopId");
            int code = getArguments().getInt("code");
            confCode.setText(String.format("%d", code));
            foodStopsModel.getFoodStops(new FoodStopsModel.getCompletionHandler() {
                @Override
                public void success(List<FoodStop> foodStops) {
                    for(int i = 0; i < foodStops.size(); i++){
                        FoodStop foodStop = foodStops.get(i);
                        if (foodStopId == foodStop.foodStopId){
                            String hexColor = "#" + foodStop.hexColor;
                            circle.setColorFilter(Color.parseColor(hexColor));
                            number.setTextColor(Color.parseColor(hexColor));
                            number.setText(String.format("%d", foodStop.foodStopNumber));
                            location.setText(foodStop.name);
                            address.setText(foodStop.streetAddress);

                        }

                    }
                }

                @Override
                public void error(VolleyError error) {

                }
            });
        }

        view.findViewById(R.id.doneButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_navigation_lisitingConfirmation_to_navigation_alllistings);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(LisitingConfirmationViewModel.class);
        // TODO: Use the ViewModel
    }

}