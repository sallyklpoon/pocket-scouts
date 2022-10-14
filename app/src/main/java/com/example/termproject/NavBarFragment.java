package com.example.termproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
        final View v = inflater.inflate(R.layout.fragment_navbar, container, false);

        BottomNavigationView navigation = v.findViewById(R.id.bottom_navigation);
        navigation.setOnItemSelectedListener(mOnNavigationItemSelectedListener);
        return v;
    }

    private final BottomNavigationView.OnItemSelectedListener mOnNavigationItemSelectedListener
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