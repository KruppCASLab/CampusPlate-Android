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

public class InputCodeFragment extends Fragment {

    private Context mActivity;
    private UserModel userModel;

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
                sp.edit().putInt("userId", -1).apply(); //TODO: Replace this

                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                final EditText  inputCode = requireActivity().findViewById(R.id.editText_inputCode);
                final CredentialManager credentialManager = CredentialManager.shared();
                final Credential cred = credentialManager.getCredential();

                User user = new User(cred.getUserName(),Integer.parseInt(inputCode.getText().toString()));
                userModel.updateUser(user, new UserModel.UpdateUserCompletionHandler() {
                    @Override
                    public void success(String token) {
                        Credential account = new Credential(cred.getUserName(), token);
                        credentialManager.saveCredential(account);
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
