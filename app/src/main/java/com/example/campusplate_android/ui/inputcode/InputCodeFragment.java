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

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.campusplate_android.Credential;
import com.example.campusplate_android.CredentialManager;
import com.example.campusplate_android.MainActivity;
import com.example.campusplate_android.Model.ListingModel;
import com.example.campusplate_android.Model.Types.User;
import com.example.campusplate_android.Model.UserModel;
import com.example.campusplate_android.R;
import com.example.campusplate_android.Session;
import com.example.campusplate_android.SharedPreferencesManager;

public class InputCodeFragment extends Fragment {

    private Context mActivity;
    private String username;

    public static InputCodeFragment newInstance() {
        return new InputCodeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input_code, container, false);

        username = getArguments().getString("username");

        final ProgressBar progressBar = view.findViewById(R.id.progressBarInputCode);
        final Button submitButton = view.findViewById(R.id.button_submitCode);

        view.findViewById(R.id.button_submitCode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                submitButton.setEnabled(false);
                final EditText  inputCode = requireActivity().findViewById(R.id.editText_inputCode);

                SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(getActivity().getApplicationContext().getSharedPreferences("CampusPlate", Context.MODE_PRIVATE));
                final CredentialManager credentialManager = new CredentialManager(sharedPreferencesManager);

                final User user = new User(username,Integer.parseInt(inputCode.getText().toString()));
                    UserModel.getSharedInstance().updateUser(user, new UserModel.UpdateUserCompletionHandler() {

                    @Override
                    public void success(final String token) {
                        progressBar.setVisibility(View.INVISIBLE);
                        submitButton.setEnabled(true);
                        Credential account = new Credential(username, token);
                        credentialManager.storeUserCredentials(account);
                        Session.getInstance().setCredential(account);

                        Intent intent = new Intent(mActivity.getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(mActivity.getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void error(int errorCode) {
                        progressBar.setVisibility(View.INVISIBLE);
                        submitButton.setEnabled(true);
                            Toast.makeText(mActivity.getApplicationContext(), "Incorrect Pin: " + errorCode, Toast.LENGTH_SHORT).show();
                    }
                });
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
