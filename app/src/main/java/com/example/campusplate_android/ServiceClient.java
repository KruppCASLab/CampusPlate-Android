package com.example.campusplate_android;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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

    synchronized public static ServiceClient getInstance() {
        if (serviceClient == null) {
            throw new RuntimeException("Service Client Uninitialized");
        }
        return serviceClient;
    }
    synchronized public static ServiceClient getInstance(Context ctx) {
        if (serviceClient == null) {
            serviceClient = new ServiceClient(ctx);
        }
        return serviceClient;
    }



    synchronized private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    private void addRequest(Request request) {
        this.getRequestQueue().add(request);
    }

    public void get(String broker, final Response.Listener<JSONObject> listener, final Response.ErrorListener errorListener) {
        String path = url + broker.toLowerCase();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, path, null, listener, errorListener);
        BaseRequest baseRequest = new BaseRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                    listener.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorListener.onErrorResponse(error);
            }
        });
        addRequest(request);
    }

    public void delete(String broker, int id, final Response.Listener<JSONObject> listener, final Response.ErrorListener errorListener) {
        String path = url + broker.toLowerCase();

        JSONObject object = new JSONObject();
        try {
            object.put("id", id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, "https://mopsdev.bw.edu/food/rest.php/users/" + id, object, listener, errorListener);  //TODO: Change this back to Krupp's URL
        BaseRequest baseRequest = new BaseRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                listener.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorListener.onErrorResponse(error);
            }
        });
        //JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, path, object, listener, errorListener);

        addRequest(request);
    }

    public void patch(String broker, Object putObject, String id, final Response.Listener<JSONObject> listener, final Response.ErrorListener errorListener) {
        String path;
        int method = Request.Method.PATCH;

            path = "https://mopsdev.bw.edu/food/rest.php/users/" + id;



        Gson gson = new Gson();
        String json = gson.toJson(putObject);
        JSONObject object = new JSONObject();
        try {
            object = new JSONObject(json);
            //TODO: Set user id
            //TODO: Get location description from somewhere
        } catch (JSONException exception) {
            //TODO: Something with exception
        }

        JsonObjectRequest request = new JsonObjectRequest(method, path, object, listener, errorListener);
        BaseRequest baseRequest = new BaseRequest(url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                listener.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorListener.onErrorResponse(error);
            }
        });
        addRequest(request);
    }

    public void post(String broker, Object postObject, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        //String path = url + broker.toLowerCase();
        int method = Request.Method.POST;

       String  path = "https://mopsdev.bw.edu/food/rest.php/users/";



        Gson gson = new Gson();
        String json = gson.toJson(postObject);
        JSONObject object = new JSONObject();
        try {
            object = new JSONObject(json);
            //TODO: Set user id
            //TODO: Get location description from somewhere
        } catch (JSONException exception) {
            //TODO: Something with exception
        }

        JsonObjectRequest request = new JsonObjectRequest(method, path, object, listener, errorListener);

        addRequest(request);
    }
}
