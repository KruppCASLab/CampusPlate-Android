package com.example.campusplate_android.Model;

import android.annotation.SuppressLint;
import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.campusplate_android.Model.Types.User;
import com.example.campusplate_android.ServiceClient;

import org.json.JSONObject;

public class UserModel {
    private static UserModel sharedInstance;
    private Context context;

    public interface AddUpdateUserCompletionHandler {
        void success();
        void error(int errorCode);
    }
    public interface getUserCompletionHandler {
        void success(User user);
        void error(int errorCode);
    }
    public interface deleteUserCompletionHandler {
        void success();
        void error(int errorCode);
    }
    private UserModel(Context ctx) {
        this.context = ctx;
    }

    static synchronized public UserModel getSharedInstance(Context ctx) {
        if (sharedInstance == null) {
            sharedInstance = new UserModel(ctx);
        }
        return sharedInstance;
    }

    public void addUser(Object object, final AddUpdateUserCompletionHandler completionHandler) {
        ServiceClient serviceClient = ServiceClient.getInstance();

        serviceClient.post("Users", object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                completionHandler.success();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                completionHandler.error(1);
            }
        });
    }
}



