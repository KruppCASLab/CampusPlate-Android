package com.example.campusplate_android.ui.addlisting;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.campusplate_android.Credential;
import com.example.campusplate_android.MainActivity;
import com.example.campusplate_android.Model.FoodStopsModel;
import com.example.campusplate_android.Model.ListingModel;
import com.example.campusplate_android.Model.Types.FoodStop;
import com.example.campusplate_android.Model.Types.Listing;
import com.example.campusplate_android.R;
import com.example.campusplate_android.Session;
import com.example.campusplate_android.ui.Select;

import java.nio.channels.CompletionHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddListingFragment extends Fragment {

    private ListingModel listingModel;
    private FoodStopsModel foodStopsModel;
    private Context mActivity;
    String[] listItems;
    boolean[]checkedItems;
    ArrayList<Integer> selectedItems = new ArrayList<>();

    public static AddListingFragment newInstance() {
        return new AddListingFragment();
    }






    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_add_listing, container, false);
        listingModel = ListingModel.getSharedInstance(mActivity.getApplicationContext());


        final EditText titleView = view.findViewById(R.id.editText_addTitle);
        final EditText quantityView = view.findViewById(R.id.editText_addQuantity);
        final EditText descriptionView = view.findViewById(R.id.editTextTextPersonName);
        final Button allergy = view.findViewById(R.id.allergyButton);
        Credential credential = Session.getInstance().getCredential();
        final Fragment fragment = this;
        view.findViewById(R.id.allergyButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] allergens = {"shell fish", "peanuts", "dairy"};

                Select select = new Select("Select Allergens", allergens, true, new Select.SelectComplete() {
                    @Override
                    public void didSelectItems(String[] items) {

                    }
                });
                select.show(fragment.getContext());
            }
        });

        view.findViewById(R.id.location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] locations = {"Knowlton", "Ritter", "Macs"};
                Select select = new Select("Select Location", locations, false, new Select.SelectComplete() {
                    @Override
                    public void didSelectItems(String[] items) {
                        
                    }
                });
                select.show(fragment.getContext());
            }
        });

        view.findViewById(R.id.button_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) mActivity).startProgressBar();
                final View root = view;


                Location currentLocation = ((MainActivity) mActivity).getCurrentLocation();

                Listing listing = new Listing(titleView.getText().toString(),descriptionView.getText().toString(),1, Integer.parseInt(quantityView.getText().toString()));

                listingModel.postListing(new ListingModel.PostListingCompletionHandler() {
                    @Override
                    public void postListing() {
                        Toast.makeText(mActivity, "Listing Added!", Toast.LENGTH_SHORT).show();
                        ((MainActivity) mActivity).stopProgressBar();
                        Navigation.findNavController(root).navigate(R.id.navigation_alllistings);
                    }
                }, listing);
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
        if(context instanceof Activity){
            mActivity = context;
        }
    }
}
