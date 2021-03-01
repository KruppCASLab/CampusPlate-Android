package com.example.campusplate_android.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.campusplate_android.ListingCompletionHandler;
import com.example.campusplate_android.Model.Types.Listing;
import com.example.campusplate_android.ServiceClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ListingModel {
    private ArrayList<Listing> listings = new ArrayList<>();
    private static ListingModel sharedInstance = null;
    private Context context;

    public interface GetListingCompletionHandler {
        void receiveListing(Listing listing);
    }

    public interface GetListingsCompletionHandler {
        void receiveListings(List<Listing> listings);
    }

    public interface PostListingCompletionHandler {
        void postListing();
    }

    public interface EditListingCompletionHandler {
        void editListing();
    }

    public interface DeleteListingCompletionHandler {
        void deleteListing();
    }

    public interface GetListingImageCompletionHandler extends ListingCompletionHandler {
        void success(Bitmap imageData);

    }
    private ListingModel(Context ctx) {
        this.context = ctx;
    }

    static synchronized public ListingModel getSharedInstance(Context ctx) {
        if (sharedInstance == null) {
            sharedInstance = new ListingModel(ctx);
        }
        return sharedInstance;
    }

    public Listing getListing(int index) {
        return this.listings.get(index);
    }

    public void addListing(Listing listing) {
        this.listings.add(listing);
    }

    public void removeListing(Listing listing) {
        this.listings.remove(listing);
    }

    public int getNumberOfListings() {
        return this.listings.size();
    }

    public ArrayList<Listing> getAllListings() {
        return this.listings;
    }

    public ArrayList<Listing> getUserListings(int userId) {
        ArrayList<Listing> userListings = new ArrayList<>();
        for (int i = 0; i < this.listings.size(); i++) {
            if (this.listings.get(i).userId == userId) {
                userListings.add(this.listings.get(i));
            }
        }
        return userListings;
    }

    public synchronized void getListings(final GetListingsCompletionHandler completionHandler) {
        ServiceClient client = ServiceClient.getInstance(context.getApplicationContext());
        this.listings.clear();
        client.get("Listings", new Response.Listener<JSONObject>() {
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
                        Double listingId = (double) mapItem.get("listingId");
                        Double userId = (double) mapItem.get("userId");
                        Double foodStopId = (double) mapItem.get("foodStopId");
                        Double creationTime = (double) mapItem.get("creationTime");
                        Double quantity = (double) mapItem.get("quantity");
                        addListing(new Listing(listingId.intValue(), userId.intValue(), foodStopId.intValue(), (String) mapItem.get("title"), (String) mapItem.get("Description"), creationTime.intValue(), quantity.intValue()));//new Date((int) (double) mapItem.get("creationTime")), listingModel.createNewLocation((double) mapItem.get("lat"), (double) mapItem.get("lng")),-1));
                    }
                }
                Collections.sort(listings);
                completionHandler.receiveListings(listings);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    public synchronized void getListingImages(int id, final GetListingImageCompletionHandler completionHandler){
        ServiceClient serviceClient = ServiceClient.getInstance();
        serviceClient.get("Listings", id, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String data = response.getString("data");
                    byte [] imagebytes = Base64.decode(data, Base64.DEFAULT);
                    Bitmap bmp = BitmapFactory.decodeByteArray(imagebytes, 0, imagebytes.length);


                    completionHandler.success(bmp);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    public synchronized void postListing(final PostListingCompletionHandler completionHandler, Listing listing) {
        ServiceClient client = ServiceClient.getInstance(context.getApplicationContext());
        client.post("listings", listing, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //TODO: Do something with response (confirmation?)
                completionHandler.postListing(); //TODO: This is empty, change?
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error. Try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public synchronized void editListing(final EditListingCompletionHandler completionHandler, Listing listing, int id) {
        ServiceClient client = ServiceClient.getInstance(context.getApplicationContext());
        client.patch("listings", listing, Integer.toString(id), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //TODO: Do something with response (confirmation?)
                completionHandler.editListing(); //TODO: This is empty, change?
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error. Try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public synchronized void deleteListing(final DeleteListingCompletionHandler completionHandler, int id) {
        ServiceClient client = ServiceClient.getInstance(context.getApplicationContext());
        client.delete("listings", id, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                //TODO: Do something with response (confirmation?)
                completionHandler.deleteListing(); //TODO: This is empty, change?
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error. Try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
