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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    Context context;

    private ArrayList<Event> eventList;
    private RecyclerView recyclerView;
    private ArrayList<Map<String, Object>> usersEvents = new ArrayList<>();

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

        setupDummyEventInfo();
        getUserEvents();
        Log.e("ALL EVENTS", String.valueOf(usersEvents));

        recyclerAdapter adapter = new recyclerAdapter(this.eventList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void setupDummyEventInfo() {
        this.eventList = getDummyEvents();
    }

    private void getUserEvents() {
        String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        String mockUserId = "vkW6lNuyo4VX7QWo9XLiiEhI1bf2";

        Query eventConfirmationQuery = db.collection("event_confirmation").whereEqualTo("user_id", mockUserId);

        eventConfirmationQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document: task.getResult()) {
                        String eventId = Objects.requireNonNull(document.getData().get("event_id")).toString();

                        db.collection("event").document(eventId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    usersEvents.add(task.getResult().getData());
                                    Log.e("EVENT DETAILS", String.valueOf(task.getResult().getData()));
                                } else {
                                    Toast.makeText(context, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } else {
                    Log.e("ERROR RETRIEVING USER'S EVENTS", String.valueOf(task.getException()));
                    Toast.makeText(context, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public static ArrayList<Event> getDummyEvents() {
        ArrayList<Event> dummy = new ArrayList<>();
        dummy.add(new Event("title 1", "asdfg"));
        dummy.add(new Event("title 2", "sad"));
        dummy.add(new Event("title 3", "thumbsupcathappy"));
        return dummy;
    }
}