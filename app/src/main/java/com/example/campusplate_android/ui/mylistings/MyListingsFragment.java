package com.example.campusplate_android.ui.mylistings;

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

public class MyListingsFragment extends Fragment {

    private MyListingsViewModel myListingsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myListingsViewModel =
                ViewModelProviders.of(this).get(MyListingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_mylistings, container, false);
        /*final TextView textView = root.findViewById(R.id.text_notifications);
        myListingsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }
}