package com.example.campusplate_android.Model;

import android.content.Context;

import com.example.campusplate_android.Model.Types.Event;
import com.example.campusplate_android.ServiceClient;

import java.util.ArrayList;
import java.util.List;

public class EventModel {
    private static EventModel sharedInstance = null;
    private ArrayList<Event> events = new ArrayList<>();
    private Context context;

    public interface GetEventsCompletionHandler {
        void receiveEvents(List<Event> events);
    }

    private EventModel(Context ctx) {
        this.context = ctx;

        //Fake events
        addEvent(new Event("EMACS Tour", "A tour of the new Computer Science building!", "North Campus", 123456789));
        addEvent(new Event("EMACS Tour 2", "A second tour of the new Computer Science building!", "North Campus", 123456789));
        addEvent(new Event("EMACS Tour 3", "A third tour of the new Computer Science building!", "North Campus", 123456789));
        addEvent(new Event("EMACS Tour 4", "Is this too many tours?", "North Campus", 123456789));
    }

    static synchronized public EventModel getSharedInstance(Context ctx) {
        if (sharedInstance == null) {
            sharedInstance = new EventModel(ctx);
        }
        return sharedInstance;
    }

    private void addEvent(Event event){
        this.events.add(event);
    }

    public Event getEvent(int index){
        return this.events.get(index);
    }

    public void removeEvent(int index){
        this.events.remove(index);
    }

    public int getNumberOfEvents(){
        return this.events.size();
    }

    public ArrayList<Event> getAllEvents(){
        return this.events;
    }

    public synchronized void getEvents(final GetEventsCompletionHandler completionHandler){
        ServiceClient client = ServiceClient.getInstance(context.getApplicationContext());
        //Web services blah blah

        completionHandler.receiveEvents(this.getAllEvents());
    }
}
