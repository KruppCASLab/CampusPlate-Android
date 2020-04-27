package com.example.campusplate_android.ui.viewlisting;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.campusplate_android.R;

public class ViewListingFragment extends Fragment {

    public static ViewListingFragment newInstance() {
        return new ViewListingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_listing_fragment, container, false);

        return view;
    }
}
