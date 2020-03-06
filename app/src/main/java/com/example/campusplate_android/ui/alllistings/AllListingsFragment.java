package com.example.campusplate_android.ui.alllistings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.campusplate_android.R;

public class AllListingsFragment extends Fragment {

    private AllListingsViewModel allListingsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        allListingsViewModel =
                ViewModelProviders.of(this).get(AllListingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_alllistings, container, false);
        /*final TextView textView = root.findViewById(R.id.text_dashboard);
        allListingsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }
}