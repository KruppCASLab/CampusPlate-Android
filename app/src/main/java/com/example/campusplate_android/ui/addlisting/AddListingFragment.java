package com.example.campusplate_android.ui.addlisting;

import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.ACTION_IMAGE_CAPTURE;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class AddListingFragment extends Fragment {
    ImageCapturer imageCapturer = new ImageCapturer();
    private ListingModel listingModel;
    private FoodStopsModel foodStopsModel;
    FoodStop selectedFoodStop;
    private Context mActivity;
    String[] listItems;
    boolean[] checkedItems;
    final AddListingFragment fragment = this;
    ArrayList<Integer> selectedItems = new ArrayList<>();
    String encodeImage = null;
    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private Button timeButton;
    private String date;
    private int year, month, day;
    private int hour, minute;
    private int currentMonth;
    private int currentDay;

    public static AddListingFragment newInstance() {
        return new AddListingFragment();
    }

    ImageView foodImage;

    //TODO: don't allow to add listing unless all fields that are required // photo is optional alert dialog make sure fields aren't empty
    //TODO: Make sure that data is valid // private method (boolean) to check is data is valid
    //TODO: only want to use constructor with image if picture is taken otherwise use other constructor

    private boolean isFormValid(View view) {
        EditText quantityView = view.findViewById(R.id.editText_addQuantity);
        EditText descriptionView = view.findViewById(R.id.description);
        EditText listingWeightView = view.findViewById(R.id.listingWeight);
        dateButton = (Button) getView().findViewById(R.id.datePickerButton);
        timeButton = (Button) getView().findViewById(R.id.timeButton);
        EditText titleView = view.findViewById(R.id.editText_addTitle);

        boolean formIsValid = true;
        // check and see if data is valid and data types match
        if (quantityView.getText().toString().isEmpty()) {
            formIsValid = false;
        } else if (titleView.getText().toString().isEmpty()) {
            formIsValid = false;
        } else if (descriptionView.getText().toString().isEmpty()) {
            formIsValid = false;
        } else if (listingWeightView.getText().toString().isEmpty()) {
            formIsValid = false;
        } else if (dateButton.getText().toString().isEmpty()) {
            formIsValid = false;
        }
        else if (timeButton.getText().toString().isEmpty()) {
            formIsValid = false;
        }
        return formIsValid;
    }

    private String getStartingDate()
    {
        Calendar cal = Calendar.getInstance();
        int startYear = cal.get(Calendar.YEAR);
        int startMonth = cal.get(Calendar.MONTH);
        month = startMonth + 1;
        currentMonth = month;
        int startDay = cal.get(Calendar.DAY_OF_MONTH) + 2;
        currentDay = startDay - 2;
        return makeDateString(day, month, year);
    }

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int newYear, int newMonth, int newDay)
            {
                day = newDay;
                month = newMonth + 1;
                year = newYear;
                date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(getActivity(), style, dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year)
    {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        return "JAN";
    }

    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }

    public void popTimePicker(View view)
    {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                hour = selectedHour;
                minute = selectedMinute;
                timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d",hour, minute));
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Bundle bundle = result.getData().getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] bytes = stream.toByteArray();
                encodeImage = android.util.Base64.encodeToString(bytes, Base64.DEFAULT);
                foodImage.setImageBitmap(bitmap);
            } else if (result.getResultCode() != RESULT_OK) {
                requestPermission.launch(Manifest.permission.CAMERA);

                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle bundle = result.getData().getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] bytes = stream.toByteArray();
                    encodeImage = android.util.Base64.encodeToString(bytes, Base64.DEFAULT);
                    foodImage.setImageBitmap(bitmap);
                }
            }
        }
    });

    private void launchCamera() {
        Intent intent = new Intent(ACTION_IMAGE_CAPTURE);
        activityResultLauncher.launch(intent);

    }


    ActivityResultLauncher<String> requestPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if (result == true) {
                launchCamera();
            } else if (result == false) {
                Toast.makeText(mActivity.getApplicationContext(), "Access Denied", Toast.LENGTH_LONG).show();
            }
        }
    });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_add_listing, container, false);
        listingModel = ListingModel.getSharedInstance(mActivity.getApplicationContext());
        foodStopsModel = FoodStopsModel.getSharedInstance();
        foodImage = view.findViewById(R.id.foodImage);
        initDatePicker();
        dateButton = view.findViewById(R.id.datePickerButton);
        dateButton.setText(getStartingDate());
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker(view);
            }
        });
        timeButton = view.findViewById(R.id.timeButton);
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popTimePicker(view);
            }
        });
        ProgressBar bar = view.findViewById(R.id.progressBar);
        bar.setVisibility(View.GONE);

        final EditText titleView = view.findViewById(R.id.editText_addTitle);
        final EditText quantityView = view.findViewById(R.id.editText_addQuantity);
        final EditText descriptionView = view.findViewById(R.id.description);
        dateButton = view.findViewById(R.id.datePickerButton);
        timeButton = view.findViewById(R.id.timeButton);
        final EditText listingWeightView = view.findViewById(R.id.listingWeight);

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
                        for (int i = 0; i < foodStops.size(); i++) {
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
                if (ContextCompat.checkSelfPermission(mActivity.getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    launchCamera();
                } else if (ContextCompat.checkSelfPermission(mActivity.getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermission.launch(Manifest.permission.CAMERA);
                } else if (ContextCompat.checkSelfPermission(mActivity.getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle("Access Denied");
                    dialog.setMessage("Access has been denied to use camera please go to setting to change permission so that picture can bee taken");
                    dialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    dialog.show();
                }
            }
        });


        final View fragmentView = view;
        view.findViewById(R.id.button_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bar.setVisibility(View.VISIBLE);
                bar.bringToFront();

                int foodStopId = selectedFoodStop.foodStopId;

                long daysToExpire = (month - currentMonth) * 30 + (day - currentDay);

                long expirationTime = daysToExpire * 86400 + (hour * 3600) + (minute * 60) + (System.currentTimeMillis() / 1000);
                //long expirationTime = days * 86400 + System.currentTimeMillis() / 1000; //change here

                if (isFormValid(fragmentView)) {
                    Listing listing = new Listing(titleView.getText().toString(), descriptionView.getText().toString(), foodStopId, Integer.parseInt(quantityView.getText().toString()), fragment.encodeImage, Float.parseFloat(listingWeightView.getText().toString()), expirationTime);
                    listingModel.postListing(new ListingModel.PostListingCompletionHandler() {
                        @Override
                        public void postListing() {
                            Toast.makeText(mActivity, "Listing Added!", Toast.LENGTH_SHORT).show();
                            bar.setVisibility(View.GONE);
                            fragmentView.findViewById(R.id.button_post).setEnabled(true);
                            Navigation.findNavController(fragmentView).navigate(R.id.navigation_alllistings);
                        }

                        @Override
                        public void error() {
                            fragmentView.findViewById(R.id.button_post).setEnabled(true);
                            Toast.makeText(mActivity, "Error. Try again.", Toast.LENGTH_SHORT).show();
                        }
                    }, listing);
                    fragmentView.findViewById(R.id.button_post).setEnabled(false);
                } else {
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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = context;
        }
    }
}
