package com.example.termproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ExploreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.explore_activity);

        // Add map
        Fragment mapFragment = new MapsFragment();
        getSupportFragmentManager()
                .beginTransaction().replace(R.id.map_frame_layout, mapFragment).commit();

        // Add event list
        EventListFragment eventListFragment = new EventListFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.event_list_fragment, eventListFragment).commit();

        // Add the navbar
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.nav_fragment, NavBarFragment.class, null);
        fragmentTransaction.commit();
    }
}
