package com.example.campusplate_android;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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

    synchronized public static ServiceClient getInstance(Context ctx){
        if(serviceClient == null){
            serviceClient = new ServiceClient(ctx);
        }
        return serviceClient;
    }

    synchronized public RequestQueue getRequestQueue(){
        if(requestQueue == null){
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

        this.requestQueue.add(request);
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

        this.requestQueue.add(request);
    }

    public void put(String broker, final String listingString, int id, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        String path = url + broker.toLowerCase();
        int method = Request.Method.POST;
        if (id != -1) {
            path = path + "/" + id;
            method = Request.Method.PUT;
        }
        //JSONObject convertedObject = new Gson().fromJson(listingString, JSONObject.class);

        //JsonObjectRequest request = new JsonObjectRequest(method, path, convertedObject, listener, errorListener);
        StringRequest request = new StringRequest(method, path, listener, errorListener){
            @Override
            public byte[] getBody() throws AuthFailureError{
                return listingString.getBytes();
            }
        };
        //listingString,

        this.requestQueue.add(request);
    }

    /*public void post(String broker, Listing listing, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        this.put(broker, data, -1, listener, errorListener);
    }*/

}
