package com.example.termproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Add the navbar
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.add(R.id.nav_fragment, NavBarFragment.class, null);
//        fragmentTransaction.commit();
//        Intent intent = new Intent(this, AuthSplash.class);
//        startActivity(intent);

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(navigateToMenuItems);

        navigateToFragment(new ExploreFragment(), R.id.nav_explore);

    }

    // this is the custom method we want to implement to interface NavigationBarView.OnItemSelectedListener
    private final BottomNavigationView.OnItemSelectedListener navigateToMenuItems
            = item -> {
        Fragment selectedFragment = null;
        int selectedIconId = R.id.nav_explore;
        int selected = item.getItemId();
        if (selected == R.id.nav_events) {
            selectedFragment = new EventFragment();
            selectedIconId = R.id.nav_events;
        } else if (selected == R.id.nav_explore) {
            selectedFragment = new ExploreFragment();
        } else if (selected == R.id.nav_profile) {
            selectedFragment = new ProfileFragment();
            selectedIconId = R.id.nav_profile;
        }

        if (selectedFragment != null) {
            navigateToFragment(selectedFragment, selectedIconId);
        }
        return false;
    };

    private final void navigateToFragment(Fragment fragment, int navId) {
        bottomNav.getMenu().findItem(navId).setChecked(true);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.page_fragment_container, fragment).commit();
    }
}
