package com.example.campusplate_android.ui.inputemail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.campusplate_android.Credential;
import com.example.campusplate_android.CredentialManager;
import com.example.campusplate_android.MainActivity;
import com.example.campusplate_android.Model.Types.User;
import com.example.campusplate_android.Model.UserModel;
import com.example.campusplate_android.R;
import com.example.campusplate_android.ServiceClient;
import com.example.campusplate_android.SharedPreferencesManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class InputEmailFragment extends Fragment {
    private Context mActivity;

    public static InputEmailFragment newInstance() {
        return new InputEmailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(getActivity().getApplicationContext().getSharedPreferences("CampusPlate", Context.MODE_PRIVATE));
        final CredentialManager credentialManager = new CredentialManager(sharedPreferencesManager);

        SharedPreferences sp = mActivity.getSharedPreferences("prefs", 0);
        if (credentialManager.getUsername() != null){
            Intent intent = new Intent(mActivity.getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        View view = inflater.inflate(R.layout.fragment_input_email, container, false);

        view.findViewById(R.id.button_sendCode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final EditText inputEmail = requireActivity().findViewById(R.id.editText_inputEmail);
                final String email = inputEmail.getText().toString();
                final Credential credential = new Credential(email);

                credentialManager.removeUserCredentials();

                User user = new User(inputEmail.getText().toString());
                UserModel.getSharedInstance().addUser(user, new UserModel.AddUpdateUserCompletionHandler() {
                    @Override
                    public void success() {
                        Bundle bundle = new Bundle();
                        bundle.putString("username", email);
                        Navigation.findNavController(view).navigate(R.id.action_inputEmailFragment_to_inputCodeFragment, bundle);
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
}
