package com.example.campusplate_android.Model;

import android.content.Context;
import android.media.session.MediaSession;
import java.util.UUID;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.campusplate_android.Model.Types.User;
import com.example.campusplate_android.ServiceClient;


import org.json.JSONException;
import org.json.JSONObject;

public class UserModel {
    private static UserModel sharedInstance;

    public interface AddUpdateUserCompletionHandler {
        void success();
        void error(int errorCode);
    }

    public interface UpdateUserCompletionHandler {
        void success(String token);
        void error(int errorCode);
    }

    private UserModel() {
    }

    static synchronized public UserModel getSharedInstance() {
        if (sharedInstance == null) {
            sharedInstance = new UserModel();
        }
        return sharedInstance;
    }

    public void addUser(User user, final AddUpdateUserCompletionHandler completionHandler) {
        ServiceClient serviceClient = ServiceClient.getInstance();

        serviceClient.post("Users", user, new Response.Listener<JSONObject>() {
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

    public void updateUser(final User user, final UpdateUserCompletionHandler completionHandler){
        final ServiceClient serviceClient = ServiceClient.getInstance();
        serviceClient.patch("Users", user, user.getUserName(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //JSONObject getStatus = response.getJSONObject("Status");
                    if(response.getInt("status") != 0){
                        completionHandler.error(response.getInt("status"));
                    }
                    else {
                        JSONObject getGuid = response.getJSONObject("data");
                        String guid = getGuid.getString("GUID");
                        completionHandler.success(guid);
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                completionHandler.error(1);
            }
        });

    }
}



