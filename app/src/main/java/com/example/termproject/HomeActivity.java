//package com.example.termproject;
//
//import android.os.Bundle;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.DefaultItemAnimator;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.ArrayList;
//
//public class HomeActivity extends AppCompatActivity {
//    private ArrayList<Event> eventList;
//    private RecyclerView recyclerView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_explore);
//
//        Fragment mapFragment = new MapsFragment();
//        getSupportFragmentManager()
//                .beginTransaction().replace(R.id.map_frame_layout, mapFragment).commit();
//
//        recyclerView = findViewById(R.id.homePageRecyclerView);
//        eventList = new ArrayList<>();
//
////        setupEventInfo();
//        setAdapter();
//    }
//
//    private void setAdapter() {
//        recyclerAdapter adapter = new recyclerAdapter(this.eventList);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(adapter);
//    }
//
//    private void setupEventInfo() {
//        this.eventList = EventListActivity.getDummyEvents();
//    }
//}
