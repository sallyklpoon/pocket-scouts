package com.example.termproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsFragment extends Fragment {

    private static final String FOCUS_LAT_PARAM = "latitude";
    private static final String FOCUS_LONG_PARAM = "longitude";
    private static final String EVENTS_PARAM = "events";
    private static final String SHOW_LOCATION_PARAM = "show_location";

    private double focusLatitude;
    private double focusLongitude;
    private ArrayList<Event> events = new ArrayList<>();
    private boolean showLocation;

    public MapsFragment() {}

    public static MapsFragment newInstance(double init_lat, double init_long, ArrayList<Event> events, boolean showLocation) {
        MapsFragment fragment = new MapsFragment();
        Bundle args = new Bundle();
        args.putDouble(FOCUS_LAT_PARAM, init_lat);
        args.putDouble(FOCUS_LONG_PARAM, init_long);
        args.putParcelableArrayList(EVENTS_PARAM, events);
        args.putBoolean(SHOW_LOCATION_PARAM, showLocation);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            focusLatitude = getArguments().getDouble(FOCUS_LAT_PARAM);
            focusLongitude = getArguments().getDouble(FOCUS_LONG_PARAM);
            events = getArguments().getParcelableArrayList(EVENTS_PARAM);
            showLocation = getArguments().getBoolean(SHOW_LOCATION_PARAM);
        }
    }

    private final OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng location = new LatLng(focusLatitude, focusLongitude);
            float zoomLevel = 12.0f;  // value goes up to 21
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel));
            googleMap.addMarker(new MarkerOptions().position(location).visible(showLocation));

            for (Event event: events) {
                LatLng eventLocation = new LatLng(event.getLatitude(), event.getLongitude());
                googleMap.addMarker(new MarkerOptions().position(eventLocation).title(event.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}
