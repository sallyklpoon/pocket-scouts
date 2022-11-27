package com.example.termproject;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class EventFragment extends Fragment {
    private FirebaseFirestore db;
    Context context;
    private String currentUser;

    private RecyclerView recyclerView;
    private ArrayList<Event> userEvents = new ArrayList<>();
    CircularProgressIndicator progressIndicator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        recyclerView = view.findViewById(R.id.eventPageRecyclerView);
        progressIndicator = view.findViewById(R.id.eventsProgressIndicator);
        context = getContext();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getUid();
        db = FirebaseFirestore.getInstance();

        loadUserEvents(view);

        return view;
    }

    private void loadUserEvents(View view) {
        progressIndicator.setVisibility(View.VISIBLE);
        ArrayList<String> eventIds = new ArrayList<>();

        Query eventConfirmationQuery = db.collection("event_confirmation").whereEqualTo("user_id", currentUser);

        eventConfirmationQuery.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document: task.getResult()) {
                        String eventId = Objects.requireNonNull(document.getData().get("event_id")).toString();
                        eventIds.add(eventId);
                    }
                    if (!eventIds.isEmpty()) {
                        findEventsById(eventIds);
                    }
                    findHostEvents(view);
                } else {
                    Toast.makeText(context, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void findEventsById(ArrayList<String> eventIds) {
        Query allEventsQuery = db.collection("event").whereIn(FieldPath.documentId(), eventIds);

        allEventsQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document: task.getResult()) {
                    String id = document.getId();
                    String name = (String) document.get("name");
                    String description = (String) document.get("description");
                    Date date = ((Timestamp) Objects.requireNonNull(document.get("date"))).toDate();
                    Double latitude = (Double) document.get("latitude");
                    Double longitude = (Double) document.get("longitude");
                    String hostId = (String) document.get("host_id");
                    Long attendeeLimit = (Long) document.get("attendee_limit");
                    Double hostRating = (Double) document.get("hostRating");
                    List<String> ratings = (List<String>)  document.get("ratings");
                    if (ratings == null) {
                        ratings = new ArrayList<String>();
                    }
                    Long iconType = (Long) document.get("icon_type");
                    Event event = new Event(id, name, description, iconType, date, latitude, longitude, hostId, attendeeLimit, hostRating, ratings);
                    userEvents.add(event);
                }
            } else {
                Toast.makeText(context, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void findHostEvents(View view) {
        Query userHostedEventsQuery = db.collection("event").whereEqualTo("host_id", currentUser);

        userHostedEventsQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document: task.getResult()) {
                    Event event = createEventByDocument(document);
                    userEvents.add(event);
                }
                loadUserEventCardsRecycler(view);
            } else {
                Toast.makeText(context, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Event createEventByDocument(QueryDocumentSnapshot document) {
        String id = document.getId();
        String name = (String) document.get("name");
        String description = (String) document.get("description");
        Date date = ((Timestamp) Objects.requireNonNull(document.get("date"))).toDate();
        Double latitude = (Double) document.get("latitude");
        Double longitude = (Double) document.get("longitude");
        String hostId = (String) document.get("host_id");
        Long attendeeLimit = (Long) document.get("attendee_limit");
        Double hostRating = document.getDouble("hostRating");
        List<String> ratings = (List<String>) document.get("ratings");
        if (ratings == null) {
           ratings = new ArrayList<String>();
        }
        Long iconType = (Long) document.get("icon_type");
        return new Event(id, name, description, iconType, date, latitude, longitude, hostId, attendeeLimit, hostRating, ratings);
    }

    private void loadUserEventCardsRecycler(View view) {
        progressIndicator.setVisibility(View.GONE);

        this.userEvents.sort(Comparator.comparing(Event::getDate));
        Collections.reverse(this.userEvents);
        recyclerAdapter adapter = new recyclerAdapter(this.userEvents, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        if (this.userEvents.isEmpty()) {
            TextView noEventsText = view.findViewById(R.id.no_events_eventsFragment);
            noEventsText.setVisibility(View.VISIBLE);
        }
    }
}
