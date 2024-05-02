package com.example.campusplate_android.Model;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.campusplate_android.Model.Types.FoodStop;
import com.example.campusplate_android.Model.Types.Reservation;
import com.example.campusplate_android.Model.Types.Listing;
import com.example.campusplate_android.Model.ListingFactory;
import com.example.campusplate_android.ServiceClient;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReservationModel {
    private static ReservationModel sharedInstance;
    private ArrayList<Reservation> reservations = new ArrayList<>();
    public interface getCompletionHandler{
        void success(List<Reservation> reservations);
        void error(VolleyError error);
    }
    public interface postCompletionHandler{
        void success(int code, int status);
        void error(VolleyError error);
    }
    private ReservationModel(){}

    public ArrayList<Reservation> getAllReservations() {
        return this.reservations;
    }

    static synchronized public ReservationModel getSharedInstance() {
        if (sharedInstance == null) {
            sharedInstance = new ReservationModel();
        }
        return sharedInstance;
    }

    public void getReservations(final ReservationModel.getCompletionHandler completionHandler) {
        ServiceClient serviceClient = ServiceClient.getInstance();
        this.reservations.clear();
        serviceClient.get("Reservations", new Response.Listener<JSONObject>() {
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
                        Double userId = (double) mapItem.get("userId");
                        Double listingId = (double) mapItem.get("listingId");
                        Double quantity = (double) mapItem.get("quantity");
                        Double status = (double) mapItem.get("status");
                        Double code = (double) mapItem.get("code");
                        Double timeCreated = (double) mapItem.get("timeCreated");
                        Double timeExpired = (double) mapItem.get("timeExpired");
                        Double  reservationId = (double) mapItem.get("reservationId");

                        ListingFactory factory = new ListingFactory();
                        Listing listingImage = factory.toListing((LinkedTreeMap) mapItem.get("listing"));


                        reservations.add(new Reservation(listingId.intValue(), quantity.intValue(), userId.intValue(), status.intValue(), code.intValue(), timeCreated.longValue(), timeExpired.longValue(), reservationId.intValue(), listingImage));
                    }
                    completionHandler.success(reservations);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }
    public void addReservation(Reservation reservation, final postCompletionHandler completionHandler){
        ServiceClient serviceClient = ServiceClient.getInstance();

        serviceClient.post("Reservations", reservation, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    int status = response.getInt("status");
                    if( status == 0 || status == 3) {
                        JSONObject jsonResponse = response.getJSONObject("data");

                        int code = jsonResponse.getInt("code");

                        Log.i("Status", String.valueOf(status));
                        Log.i("Data", String.valueOf(jsonResponse));


                        completionHandler.success(code, status);
                    }
                    else{
                        completionHandler.success(-1, status);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                int j = 5;
            }
        });

    }



}
