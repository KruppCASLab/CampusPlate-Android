package com.example.campusplate_android;

import android.content.Context;
import android.util.Base64;
import android.util.JsonReader;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class BaseRequest extends JsonObjectRequest {
    private Context context;

    public BaseRequest(String url, @Nullable JSONObject jsonRequest, Response.Listener<JSONObject> listener, @Nullable Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Credential credential = Session.getInstance().getCredential();
        String cred = credential.getUserName() + ":" + credential.getPassWord();
        String basicAuthString = "Basic " + Base64.encodeToString(cred.getBytes(),0);

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", basicAuthString);

        return headers;

    }





}
