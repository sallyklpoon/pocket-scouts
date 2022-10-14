package com.example.termproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NavBarFragment# newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavBarFragment extends Fragment {

    public NavBarFragment() {
        super(R.layout.fragment_navbar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_navbar, container, false);

        // target the navbar from inflated fragment
        BottomNavigationView navigation = view.findViewById(R.id.bottom_navigation);
        // set method defined from interface
        navigation.setOnItemSelectedListener(navigateToMenuItems);
        // return the view with the correct listener method implemented
        return view;
    }

    // this is the custom method we want to implement to interface NavigationBarView.OnItemSelectedListener
    private final BottomNavigationView.OnItemSelectedListener navigateToMenuItems
            = item -> {
                int selected = item.getItemId();
                if (selected == R.id.nav_events) {
                    onMyEventsClick();
                    return true;
                }
                if (selected == R.id.nav_explore) {
                    onExploreClick();
                    return true;
                }
                if (selected == R.id.nav_profile) {
                    onProfileClick();
                    return true;
                }
                return false;
            };

    public void onMyEventsClick() {
//        Intent toMyEvents = new Intent(this, );
//        startActivity(toMyEvents);
    }

    public void onExploreClick() {
//        Intent toExplore = new Intent(this, );
//        startActivity(toExplore);
    }

    public void onProfileClick() {
        Intent toProfile = new Intent(getActivity(), UserDashboardActivity.class);
        startActivity(toProfile);
    }
}