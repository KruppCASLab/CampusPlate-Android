package com.example.campusplate_android.Model.Types;

public class Event {
    public String title;
    public String description;
    public String locationDescription;
    public int time;

    public Event() {
    }

    public Event(String title, String description, String locationDescription, int time) {
        this.title = title;
        this.description = description;
        this.locationDescription = locationDescription;
        this.time = time;
    }
}
