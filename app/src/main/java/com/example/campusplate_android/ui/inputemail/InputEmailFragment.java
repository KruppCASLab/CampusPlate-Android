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
import com.example.campusplate_android.MainActivity;
import com.example.campusplate_android.R;
import com.example.campusplate_android.ServiceClient;

import org.json.JSONException;
import org.json.JSONObject;

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
                TextView inputEmail = view.findViewById(R.id.editText_inputEmail);

                JSONObject emailObject = new JSONObject();

                try {
                    emailObject.put("userName", inputEmail.getText().toString());
                }catch (JSONException error){
                    error.printStackTrace();

                }

                ServiceClient serviceClient = ServiceClient.getInstance(mActivity.getApplicationContext());
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://mopsdev.bw.edu/food/rest.php/users", emailObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(mActivity.getApplicationContext(), "Sent", Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mActivity.getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();

                    }
                });
                serviceClient.addRequest(request);

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
