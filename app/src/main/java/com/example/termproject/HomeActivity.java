package com.example.termproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        Fragment mapFragment = new MapsFragment();
        getSupportFragmentManager()
                .beginTransaction().replace(R.id.map_frame_layout, mapFragment).commit();
    }

}
