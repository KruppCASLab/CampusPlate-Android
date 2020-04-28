package com.example.campusplate_android;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.campusplate_android.Model.Types.Listing;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class ServiceClient {
    private static ServiceClient serviceClient;
    private Context context;
    private RequestQueue requestQueue;
    private String url = "https://mopsdev.bw.edu/food/rest.php/";

    private ServiceClient(Context ctx) {
        this.context = ctx;
    }

    synchronized public static ServiceClient getInstance(Context ctx) {
        if (serviceClient == null) {
            serviceClient = new ServiceClient(ctx);
        }
        return serviceClient;
    }

    synchronized public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public void addRequest(Request request) {
        this.getRequestQueue().add(request);
    }

    public void get(String broker, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String path = url + broker.toLowerCase();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, path, null, listener, errorListener);

        this.getRequestQueue().add(request);
    }

    public void delete(String broker, int id, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String path = url + broker.toLowerCase();

        JSONObject object = new JSONObject();
        try {
            object.put("id", id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, path, object, listener, errorListener);

        this.getRequestQueue().add(request);
    }

    public void put(String broker, Listing listing, int id, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String path = url + broker.toLowerCase();
        int method = Request.Method.POST;
        if (id != -1) {
            path = path + "/" + id;
            method = Request.Method.PUT;
        }

        Gson gson = new Gson();
        String json = gson.toJson(listing);
        JSONObject object = new JSONObject();
        try {
            object = new JSONObject(json);
            if(method == Request.Method.POST){
                object.remove("listingId"); //TODO: Set this in Listing object not here
                object.put("locationDescription", "EMACS");
            }
            //TODO: Set user id
            //TODO: Get location description from somewhere
        } catch (JSONException exception) {
            //TODO: Something with exception
        }

        JsonObjectRequest request = new JsonObjectRequest(method, "https://mopsdev.bw.edu/~etimko16/WebServiceAssignment/rest.php/Listing/" + id, object, listener, errorListener); //TODO: Change this back to Krupp's web services after his PUT works

        ///JsonObjectRequest request = new JsonObjectRequest(method, path, object, listener, errorListener);

        this.getRequestQueue().add(request);
    }

    public void post(String broker, Listing listing, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        this.put(broker, listing, -1, listener, errorListener);
    }


}
