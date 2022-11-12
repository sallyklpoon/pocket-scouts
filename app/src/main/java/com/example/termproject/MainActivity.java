package com.example.termproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    private FirebaseAuth mAuth;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(navigateToMenuItems);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            renderAuthentication();
        } else {
            System.out.println(currentUser.getEmail());
            renderUI();
        }
    }

    public void renderAuthentication() {
        bottomNav.setVisibility(View.INVISIBLE);
        navigateToFragment(new AuthFragment(), R.id.nav_auth, false);
    }

    public void renderUI() {
        bottomNav.setVisibility(View.VISIBLE);
        FragmentManager fm = getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
        navigateToFragment(new ExploreFragment(), R.id.nav_explore, false);
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
            navigateToFragment(selectedFragment, selectedIconId, true);
        }
        return false;
    };

    public void navigateToFragment(Fragment fragment, int navId, Boolean addToBackStack) {
        bottomNav.getMenu().findItem(navId).setChecked(true);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.page_fragment_container, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
        }
    }
}
