package com.example.campusplate_android.Model.Types;

import android.os.Parcel;
import android.os.Parcelable;

public class FoodStop implements Parcelable {
    public int foodStopId;
    public int foodStopNumber;
    public String name;
    public String description;
    public double lat;
    public double lng;
    public String hexColor;
    public String streetAddress;

    public FoodStop() {
        this.foodStopId = -1;
        this.name = null;
        this.description = null;
        this.lat = 0;
        this.lng = 0;
        this.foodStopNumber = -1;
        this.hexColor = "#";
        this.streetAddress = "";
    }

    public FoodStop(int foodStopId, String name, String description, double lat, double lng, int foodStopNumber, String hexColor, String streetAddress) {
        this.foodStopId = foodStopId;
        this.name = name;
        this.description = description;
        this.lat = lat;
        this.lng = lng;
        this.foodStopNumber = foodStopNumber;
        this.hexColor = hexColor;
        this.streetAddress = streetAddress;
    }

    protected FoodStop(Parcel in) {
        foodStopId = in.readInt();
        foodStopNumber = in.readInt();
        name = in.readString();
        description = in.readString();
        lat = in.readDouble();
        lng = in.readDouble();
        hexColor = in.readString();
        streetAddress = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(foodStopId);
        dest.writeInt(foodStopNumber);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
        dest.writeString(hexColor);
        dest.writeString(streetAddress);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FoodStop> CREATOR = new Creator<FoodStop>() {
        @Override
        public FoodStop createFromParcel(Parcel in) {
            return new FoodStop(in);
        }

        @Override
        public FoodStop[] newArray(int size) {
            return new FoodStop[size];
        }
    };
}




