package com.example.termproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class UserDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_dashboard);
        goToCreateEvent();

        // Add the navbar
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.nav_fragment, NavBarFragment.class, null);
        fragmentTransaction.commit();
    }

    private void goToCreateEvent() {
        Button eventsBtn = findViewById(R.id.hostEventsBtn);
        Intent goToCreateEvent = new Intent(this, CreateEventActivity.class);

        eventsBtn.setOnClickListener(view -> startActivity(goToCreateEvent));
    }

}
