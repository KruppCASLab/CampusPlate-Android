package com.example.campusplate_android.ui.listingconfirmation;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.campusplate_android.R;

public class LisitingConfirmationFragment extends Fragment {

    private LisitingConfirmationViewModel mViewModel;

    public static LisitingConfirmationFragment newInstance() {
        return new LisitingConfirmationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.lisiting_confirmation_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(LisitingConfirmationViewModel.class);
        // TODO: Use the ViewModel
    }

}