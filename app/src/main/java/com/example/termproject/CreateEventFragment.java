package com.example.termproject;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class CreateEventFragment extends Fragment {
    MainActivity mainActivity;
    private LocationManager locationManager;
    EditText editText;
    View createEventFragment;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    Context context;
    private double eventLatitude = 1;
    private double eventLongitude = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();
        // Inflate the layout for this fragment
        createEventFragment = inflater.inflate(R.layout.fragment_create_event, container, false);

        return createEventFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // get Main Activity for accessing variables
        mainActivity = (MainActivity) getActivity();
        assert mainActivity != null;

        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        eventLatitude = location.getLatitude();
        eventLongitude = location.getLongitude();

        editText = view.findViewById(R.id.create_event_address_hint);

        renderMap();

        // Search Address listener
        Button searchAddressBtn = view.findViewById(R.id.findBtn);
        searchAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = editText.getText().toString();
                findAddress(address);
            }
        });
        Button saveChanges = createEventFragment.findViewById(R.id.eventsSaveChangesBtn);
        saveChanges.setOnClickListener(e -> {
            createEvent();
        });

    }

    public void createEvent() {
        String hostId = firebaseAuth.getCurrentUser().getUid();
        EditText title = createEventFragment.findViewById(R.id.eventTitleInput);
        EditText description = createEventFragment.findViewById(R.id.eventDescriptionTextArea);
        EditText attendeeLimit = createEventFragment.findViewById(R.id.attendeeLimit);
        DatePicker date = createEventFragment.findViewById(R.id.eventDatePicker);


        Map<String, Object> eventData = new HashMap<>();
        eventData.put("host_id", hostId);
        eventData.put("name", String.valueOf(Objects.requireNonNull(title.getText())));
        eventData.put("description", String.valueOf(Objects.requireNonNull(description.getText())));
        eventData.put("attendee_limit", Integer.parseInt(Objects.requireNonNull(attendeeLimit.getText().toString())));


        Calendar calendar = Calendar.getInstance();
        calendar.set(date.getYear(), date.getMonth(), date.getDayOfMonth());
        eventData.put("date", new Timestamp(calendar.getTime()));

        // Simulate event lat long, should be changed if Google API can be used
        eventLatitude = ThreadLocalRandom.current().nextDouble(-90, 90);
        eventLongitude = ThreadLocalRandom.current().nextDouble(-180, 180);

        eventData.put("latitude", eventLatitude);
        eventData.put("longitude", eventLongitude);
        db.collection("event").add(eventData)
                .addOnSuccessListener(success -> {
                    Toast.makeText(context, "Event successfully created.", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(failure -> {
                    Toast.makeText(context, "Error in creating event. Please try again later.", Toast.LENGTH_SHORT).show();
                });
        getParentFragmentManager().popBackStackImmediate();
    }

    public void findAddress(String query) {

        // Mocking search here. In actuality, the EditText
        // would make use of Google Place API and Autocomplete for the search
        // but broke student problems, so we will not be implementing the
        // tool here, but are mocking the search to Tokyo.

        LatLng tokyo = new LatLng(35.6812, 139.7671);
        LatLng vancouver = new LatLng(49.2820, -123.1171);

        LatLng queryLocation = (query.equals("Tokyo"))? tokyo : vancouver;

        eventLatitude = queryLocation.latitude;
        eventLongitude = queryLocation.longitude;

        renderMap();
    }

    private void renderMap() {
        Fragment mapFragment = MapsFragment.newInstance(this.eventLatitude, this.eventLongitude,
                new ArrayList<Event>(), true);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.map_frame_layout, mapFragment).commit();
    }
}