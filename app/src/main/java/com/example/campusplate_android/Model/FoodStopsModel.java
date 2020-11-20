package com.example.campusplate_android.Model;

import com.android.volley.VolleyError;

public class FoodStopsModel {

    public interface getCompletionHandler{
        void success();
        void error(VolleyError error);
    }

}
