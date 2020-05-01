package com.example.campusplate_android.ui.inputcode;

import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import com.example.campusplate_android.MainActivity;
import com.example.campusplate_android.Model.ListingModel;
import com.example.campusplate_android.R;

public class InputCodeFragment extends Fragment {

    private Context mActivity;

    public static InputCodeFragment newInstance() {
        return new InputCodeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input_code, container, false);

        view.findViewById(R.id.textView_terms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTerms();
            }
        });

        view.findViewById(R.id.button_submitCode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Show dialogue with terms, accept terms
                //TODO: Get user ID assigned by the database and put it into sharedPreferences to access in MyListings
                SharedPreferences sp = mActivity.getSharedPreferences("prefs", 0);
                sp.edit().putBoolean("logged", true).apply();

                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
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

    public void showTerms() {
        new AlertDialog.Builder(mActivity)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Terms and Conditions")
                .setMessage(mActivity.getResources().getString(R.string.terms_of_service))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
