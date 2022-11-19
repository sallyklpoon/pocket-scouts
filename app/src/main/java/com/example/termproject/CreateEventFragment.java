package com.example.termproject;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;

import com.google.android.gms.maps.model.LatLng;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class CreateEventFragment extends Fragment {
    MainActivity mainActivity;
    private LocationManager locationManager;
    EditText editText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // get Main Activity for accessing variables
        mainActivity = (MainActivity) getActivity();
        assert mainActivity != null;

        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        editText = view.findViewById(R.id.create_event_address_hint);

        // Inflate Map fragment
        Fragment mapFragment = MapsFragment.newInstance(location.getLatitude(), location.getLongitude());
        getChildFragmentManager().beginTransaction()
                .replace(R.id.map_frame_layout, mapFragment).commit();

        // Search Address listener
        Button searchAddressBtn = view.findViewById(R.id.findBtn);
        searchAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = editText.getText().toString();
                findAddress(address);
            }
        });
    }

    public void findAddress(String query) {

        // Mocking search here. In actuality, the EditText
        // would make use of Google Place API and Autocomplete for the search
        // but broke student problems, so we will not be implementing the
        // tool here, but are mocking the search to Tokyo.

        LatLng tokyo = new LatLng(35.6812, 139.7671);
        LatLng vancouver = new LatLng(49.2820, -123.1171);

        LatLng queryLocation = (query.equals("Tokyo"))? tokyo : vancouver;

        // Inflate mapFragment with new searched location
        Fragment mapFragment = MapsFragment.newInstance(queryLocation.latitude, queryLocation.longitude);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.map_frame_layout, mapFragment).commit();

    }

}