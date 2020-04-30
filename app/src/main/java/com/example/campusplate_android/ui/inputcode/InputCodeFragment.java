package com.example.campusplate_android.ui.inputcode;

import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.campusplate_android.MainActivity;
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

        view.findViewById(R.id.button_submitCode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Show dialogue with terms, accept terms
                //Move to main activity after accepting terms

                SharedPreferences sp = mActivity.getSharedPreferences("prefs", 0);
                sp.edit().putBoolean("logged", true).apply();

                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = context;
        }
    }
}
