package com.example.campusplate_android.ui.events;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusplate_android.MainActivity;
import com.example.campusplate_android.Model.EventModel;
import com.example.campusplate_android.Model.Types.Event;
import com.example.campusplate_android.R;

import java.util.List;

public class EventsFragment extends Fragment {

    private EventsViewModel eventsViewModel;
    private EventModel eventModel;
    private Context mActivity;
    private int mColumnCount = 1;
    private EventsRecyclerViewAdapter adapter;
    private EventsFragment.OnListFragmentInteractionListener mListener;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        eventModel = EventModel.getSharedInstance(mActivity.getApplicationContext());
        RecyclerView recycler = view.findViewById(R.id.view_recycler_events);

        ((MainActivity) mActivity).hideUpButton();
        // Set the adapter
        Context context = view.getContext();
        if (mColumnCount <= 1) {
            recycler.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recycler.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        adapter = new EventsRecyclerViewAdapter(eventModel.getAllEvents(), mListener);

        recycler.setAdapter(adapter);

        ((MainActivity) mActivity).findViewById(R.id.progressBar).setVisibility(View.VISIBLE);

        eventModel.getEvents(new EventModel.GetEventsCompletionHandler() {
            @Override
            public void receiveEvents(List<Event> events) {
                try {
                    ((MainActivity) mActivity).findViewById(R.id.progressBar).setVisibility(View.GONE);
                } catch (NullPointerException exception) {
                    //TODO: Do something with exception
                }
                adapter.setEvents(events);
                adapter.notifyDataSetChanged();
            }
        });
        recycler.addItemDecoration(new DividerItemDecoration(recycler.getContext(), DividerItemDecoration.VERTICAL));

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Event item);
    }
}