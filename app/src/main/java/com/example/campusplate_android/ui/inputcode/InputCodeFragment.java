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
import android.widget.EditText;
import android.widget.Toast;

import com.example.campusplate_android.Credential;
import com.example.campusplate_android.CredentialManager;
import com.example.campusplate_android.MainActivity;
import com.example.campusplate_android.Model.ListingModel;
import com.example.campusplate_android.Model.Types.User;
import com.example.campusplate_android.Model.UserModel;
import com.example.campusplate_android.R;
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

        view.findViewById(R.id.textView_terms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTerms();
            }
        });

        view.findViewById(R.id.button_submitCode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText  inputCode = requireActivity().findViewById(R.id.editText_inputCode);

                SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(getActivity().getApplicationContext().getSharedPreferences("CampusPlate", Context.MODE_PRIVATE));
                final CredentialManager credentialManager = new CredentialManager(sharedPreferencesManager);

                final User user = new User(username,Integer.parseInt(inputCode.getText().toString()));
                    UserModel.getSharedInstance().updateUser(user, new UserModel.UpdateUserCompletionHandler() {
                    @Override
                    public void success(final String token) {
                        Credential account = new Credential(username, token);
                        credentialManager.storeUserCredentials(username, token);

                        Intent intent = new Intent(mActivity.getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(mActivity.getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void error(int errorCode) {
                        Toast.makeText(mActivity.getApplicationContext(), "Error:" + errorCode, Toast.LENGTH_SHORT).show();
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
