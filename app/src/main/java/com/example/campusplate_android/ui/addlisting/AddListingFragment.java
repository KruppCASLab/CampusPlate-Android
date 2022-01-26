package com.example.campusplate_android.ui.addlisting;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.util.EthiopicCalendar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.campusplate_android.MainActivity;
import com.example.campusplate_android.Model.FoodStopsModel;
import com.example.campusplate_android.Model.ListingModel;
import com.example.campusplate_android.Model.Types.FoodStop;
import com.example.campusplate_android.Model.Types.Listing;
import com.example.campusplate_android.R;
import com.example.campusplate_android.ui.Select;
import com.example.campusplate_android.ui.viewlisting.ImageCapturer;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class AddListingFragment extends Fragment {
    ImageCapturer imageCapturer = new ImageCapturer();
    private ListingModel listingModel;
    private FoodStopsModel foodStopsModel;
    FoodStop selectedFoodStop;
    private Context mActivity;
    String[] listItems;
    boolean[]checkedItems;
    final AddListingFragment fragment = this;
    ArrayList<Integer> selectedItems = new ArrayList<>();
   String encodeImage = null;

    public static AddListingFragment newInstance() {
        return new AddListingFragment();
    }

    ImageView foodImage;



    //TODO: don't allow to add listing unless all fields that are required // photo is optional alert dialog make sure fields aren't empty
    //TODO: Make sure that data is valid // private method (boolean) to check is data is valid
    //TODO: only want to use constructor with image if picture is taken otherwise use other constructor

    private boolean isFormValid(View view){
        EditText quantityView = view.findViewById(R.id.editText_addQuantity);
        EditText descriptionView = view.findViewById(R.id.description);
        EditText listingWeightView = view.findViewById(R.id.listingWeight);
        EditText expirationDateView = view.findViewById(R.id.expirationDate);
        EditText titleView = view.findViewById(R.id.editText_addTitle);

        boolean formIsValid = true;
        // check and see if data is valid and data types match
        if(quantityView.getText().toString().isEmpty()){
            formIsValid = false;
        }else if(titleView.getText().toString().isEmpty()){
            formIsValid = false;
        }else if(descriptionView.getText().toString().isEmpty()){
            formIsValid = false;
        }else if(listingWeightView.getText().toString().isEmpty()){
            formIsValid = false;
        }else if(expirationDateView.getText().toString().isEmpty()){
            formIsValid = false;
        }
        return formIsValid;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 100){
            //capture image
            Bitmap capture = (Bitmap) data.getExtras().get("data"); // null object reference
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            capture.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bytes = stream.toByteArray();
            this.encodeImage = Base64.encodeToString(bytes,Base64.DEFAULT);
            foodImage.setImageBitmap(capture);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }




    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_add_listing, container, false);
        listingModel = ListingModel.getSharedInstance(mActivity.getApplicationContext());
        foodStopsModel = FoodStopsModel.getSharedInstance();
        foodImage = view.findViewById(R.id.foodImage);



        final EditText titleView = view.findViewById(R.id.editText_addTitle);
        final EditText quantityView = view.findViewById(R.id.editText_addQuantity);
        final EditText descriptionView = view.findViewById(R.id.description);
        final EditText expirationDateView = view.findViewById(R.id.expirationDate);
        final EditText listingWeightView = view.findViewById(R.id.listingWeight);


//        view.findViewById(R.id.allergyButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String[] allergens = {"shell fish", "peanuts", "dairy"};
//                Select select = new Select("Select Allergens", allergens, true, new Select.SelectComplete() {
//                    @Override
//                    public void didSelectItems(List<Integer> items) {
//                    }
//                });
//                select.show(fragment.getContext());
//            }
//        });

        view.findViewById(R.id.location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<FoodStop> foodStopsList = new ArrayList<>();
                foodStopsModel.getFoodStopManager(new FoodStopsModel.getCompletionHandler() {
                    @Override
                    public void success(List<FoodStop> foodStops) {
                        foodStopsList.clear();
                        foodStopsList.addAll(foodStops);
                        String[] locations = new String[foodStops.size()];
                        for(int i = 0; i < foodStops.size(); i++){
                            FoodStop foodStop = foodStops.get(i);
                            locations[i] = foodStop.name;
                        }

                        Select select = new Select("Select Location", locations, false, new Select.SelectComplete() {
                            @Override
                            public void didSelectItems(List<Integer> items) {
                                fragment.selectedFoodStop = foodStopsList.get(items.get(0));
                                view.findViewById(R.id.button_post).setEnabled(true);
                            }
                        });
                        select.show(fragment.getContext());
                    }

                    @Override
                    public void error(VolleyError error) {
                        //TODO: Provide handling for error
                    }
                });
            }
        });


        view.findViewById(R.id.addImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageCapturer.captureImage(mActivity.getApplicationContext(),fragment);
            }
        });

        final View fragmentView = view;
        view.findViewById(R.id.button_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) mActivity).startProgressBar();

                int foodStopId = selectedFoodStop.foodStopId;

                int days = Integer.parseInt(expirationDateView.getText().toString());

                long expirationTime = days * 86400 + System.currentTimeMillis()/ 1000;

                if(isFormValid(fragmentView)){
                    Listing listing = new Listing(titleView.getText().toString(),descriptionView.getText().toString(), foodStopId, Integer.parseInt(quantityView.getText().toString()), fragment.encodeImage,Integer.parseInt(listingWeightView.getText().toString()), expirationTime);
                    listingModel.postListing(new ListingModel.PostListingCompletionHandler() {
                        @Override
                        public void postListing() {
                            Toast.makeText(mActivity, "Listing Added!", Toast.LENGTH_SHORT).show();
                            ((MainActivity) mActivity).stopProgressBar();
                            Navigation.findNavController(fragmentView).navigate(R.id.navigation_alllistings);
                            // make sure navigate back to all listing
                        }
                    }, listing);

                }
                else{

                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle("Error");
                    dialog.setMessage("Please make sure all fields are complete");
                    dialog.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            ((MainActivity) mActivity).stopProgressBar();
                        }
                    });
                    dialog.show();
                }
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
