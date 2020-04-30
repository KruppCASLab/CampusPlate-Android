package com.example.campusplate_android.ui.inputemail;

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

public class InputEmailFragment extends Fragment {

    private Context mActivity;

    public static InputEmailFragment newInstance() {
        return new InputEmailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        SharedPreferences sp = mActivity.getSharedPreferences("prefs", 0);
        if (sp.getBoolean("logged",false)){
            Intent intent = new Intent(mActivity.getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        View view = inflater.inflate(R.layout.fragment_input_email, container, false);

        view.findViewById(R.id.button_sendCode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_inputEmailFragment_to_inputCodeFragment);
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
