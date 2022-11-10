package com.example.termproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EventListActivity extends AppCompatActivity {
    private ArrayList<Event> eventList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        recyclerView = findViewById(R.id.eventPageRecyclerView);
        eventList = new ArrayList<>();

        setupEventInfo();
        setAdapter();
    }

    private void setAdapter() {
        recyclerAdapter adapter = new recyclerAdapter(this.eventList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setupEventInfo() {
        this.eventList = getDummyEvents();
    }

    public static ArrayList<Event> getDummyEvents() {
        ArrayList<Event> dummy = new ArrayList<>();
        dummy.add(new Event("title 1", "asdfg"));
        dummy.add(new Event("title 2", "sad"));
        dummy.add(new Event("title 3", "thumbsupcathappy"));
        return dummy;
    }
}
