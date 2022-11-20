package com.example.termproject;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class EventFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    Context context;

    private ArrayList<Event> eventList;
    private RecyclerView recyclerView;
    private ArrayList<Event> userEvents = new ArrayList<>();

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
        context = getContext();
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        loadUserEvents(view);

        return view;
    }

    private void loadUserEvents(View view) {
        // To test the code, uncomment the mockUserId and substitute
        // userId variable with mockUserId in eventConfirmationQuery
        // String mockUserId = "vkW6lNuyo4VX7QWo9XLiiEhI1bf2";

        String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        ArrayList<String> eventIds = new ArrayList<>();

        Query eventConfirmationQuery = db.collection("event_confirmation").whereEqualTo("user_id", userId);

        eventConfirmationQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document: task.getResult()) {
                        String eventId = Objects.requireNonNull(document.getData().get("event_id")).toString();
                        eventIds.add(eventId);
                    }
                    findEventsById(eventIds, view);
                } else {
                    Toast.makeText(context, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void findEventsById(ArrayList<String> eventIds, View view) {

        Query allEventsQuery = db.collection("event").whereIn(FieldPath.documentId(), eventIds);

        allEventsQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document: task.getResult()) {
                    String id = document.getId();
                    String name = (String) document.get("name");
                    String description = (String) document.get("description");
                    Date date = ((Timestamp) Objects.requireNonNull(document.get("date"))).toDate();
                    ArrayList<Double> location = (ArrayList<Double>) document.get("location");
                    String hostId = (String) document.get("host_id");
                    Long attendeeLimit = (Long) document.get("attendee_limit");

                    Event event = new Event(id, name, description, date, location, hostId, attendeeLimit);
                    userEvents.add(event);
                };
                loadUserEventCardsRecycler(view);
            }
        });
    }

    private void loadUserEventCardsRecycler(View view) {
        recyclerAdapter adapter = new recyclerAdapter(this.userEvents);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
}
