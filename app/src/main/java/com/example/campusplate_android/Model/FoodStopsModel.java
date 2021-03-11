package com.example.campusplate_android.Model;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.campusplate_android.Model.Types.FoodStop;
import com.example.campusplate_android.ServiceClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FoodStopsModel {
    private static FoodStopsModel sharedInstance;
    private ArrayList<FoodStop> foodStops = new ArrayList<>();
    public interface getCompletionHandler{
        void success(List<FoodStop> foodStops);
        void error(VolleyError error);
    }

    private FoodStopsModel(){}

    static synchronized public FoodStopsModel getSharedInstance() {
        if (sharedInstance == null) {
            sharedInstance = new FoodStopsModel();
        }
        return sharedInstance;
    }

    public void addFoodStop(FoodStop foodStop){
        this.foodStops.add(foodStop);
    }


    public ArrayList<FoodStop> getCachedFoodStops() {
        return foodStops;
    }



    public void getFoodStops(final getCompletionHandler completionHandler){
        ServiceClient serviceClient = ServiceClient.getInstance();
        this.foodStops.clear();
        serviceClient.get("FoodStops", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String stringResponse = response.toString();
                Gson gson = new Gson();
                Type map = new TypeToken<Map<String, Object>>() {
                }.getType();
                Map<String, Object> jsonMap = gson.fromJson(stringResponse, map);

                ArrayList data = (ArrayList) jsonMap.get("data");
                if (data != null) {
                    for (int i = 0; i < data.size(); i++) {
                        Map<String, Object> mapItem = (Map) data.get(i);
                        Double foodStopId = (double) mapItem.get("foodStopId");
                        Double lat = (double) mapItem.get("lat");
                        Double lng = (double) mapItem.get("lng");
                        Double foodStopNumber = (double) mapItem.get("foodStopNumber");
                        addFoodStop(new FoodStop(foodStopId.intValue(), (String) mapItem.get("name"),
                                (String) mapItem.get("description"), lat, lng, foodStopNumber.intValue(), (String) mapItem.get("hexColor"), (String) mapItem.get("streetAddress")));
                    }
                    completionHandler.success(foodStops);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

}
