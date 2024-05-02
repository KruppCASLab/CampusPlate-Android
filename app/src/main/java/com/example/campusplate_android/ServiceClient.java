package com.example.campusplate_android;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class ServiceClient {
    private static ServiceClient serviceClient;
    private Context context;
    private RequestQueue requestQueue;
    private String baseUrl = "https://mops.bw.edu/cp/rest.php/";

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
        String path = baseUrl + broker;

        BaseRequest baseRequest = new BaseRequest(Request.Method.GET, path, null, new Response.Listener<JSONObject>() {
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
        addRequest(baseRequest);
    }
    public void get(String broker,String param, final Response.Listener<JSONObject> listener, final Response.ErrorListener errorListener) {
        String path = baseUrl + broker + "/" + param;

        BaseRequest baseRequest = new BaseRequest(Request.Method.GET, path, null, new Response.Listener<JSONObject>() {
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
        addRequest(baseRequest);
    }


    public void get(String broker, int id, final Response.Listener<JSONObject> listener, final Response.ErrorListener errorListener) {
        String path ="";
        if(broker.equals("Listings")){
            String imageRoute = String.format("/%d/image", id);
             path = baseUrl + broker + imageRoute;}
        else{
             path = baseUrl + broker;
        }

        BaseRequest baseRequest = new BaseRequest(Request.Method.GET, path, null, new Response.Listener<JSONObject>() {
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
        addRequest(baseRequest);
    }




    public void delete(String broker, int id, final Response.Listener<JSONObject> listener, final Response.ErrorListener errorListener) {
        String path = baseUrl + broker.toLowerCase();

        JSONObject object = new JSONObject();
        try {
            object.put("id", id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        BaseRequest baseRequest = new BaseRequest(Request.Method.DELETE, path, null, new Response.Listener<JSONObject>() {
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

        addRequest(baseRequest);
    }

    public void patch(String broker, Object putObject, String id, final Response.Listener<JSONObject> listener, final Response.ErrorListener errorListener) {
        String path;
        int method = Request.Method.PATCH;

        path = baseUrl +  broker + "/" + id;

        Gson gson = new Gson();
        String json = gson.toJson(putObject);
        JSONObject object = new JSONObject();
        try {
            object = new JSONObject(json);
            //TODO: Set user id
            //TODO: Get location description from somewhere
        } catch (JSONException exception) {
            //TODO: Something with exception
            System.out.println(exception);
        }

        BaseRequest baseRequest = new BaseRequest(method, path, object, new Response.Listener<JSONObject>() {
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
        addRequest(baseRequest);
    }

    public void post(String broker, Object postObject, final Response.Listener<JSONObject> listener, final Response.ErrorListener errorListener) {
        String path = baseUrl + broker.toLowerCase();
        int method = Request.Method.POST;

        Gson gson = new Gson();
        String json = gson.toJson(postObject);
        JSONObject object = new JSONObject();
        try {
            object = new JSONObject(json);
            //TODO: Set user id
            //TODO: Get location description from somewhere
        } catch (JSONException exception) {
            int j = 5;
            //TODO: Something with exception
        }

        JsonObjectRequest request = new JsonObjectRequest(method, path, object, listener, errorListener);
        BaseRequest baseRequest = new BaseRequest(method, path, object, new Response.Listener<JSONObject>() {
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
        addRequest(baseRequest);
    }
}
