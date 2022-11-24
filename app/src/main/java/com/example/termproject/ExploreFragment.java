package com.example.termproject;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import androidx.fragment.app.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONException;
import org.json.JSONObject;

public class ExploreFragment extends Fragment {
    double longitude = 1;
    double latitude = 1;
    double searchLatitude;
    double searchLongitude;
    MainActivity mainActivity;
    RequestQueue queue;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private FirebaseFirestore db;
    Context context;
    View currentView;
    TextView weatherText;

    private RecyclerView eventsRecycler;
    private ArrayList<Event> events = new ArrayList<>();

    public ExploreFragment() { super(R.layout.fragment_explore); }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        eventsRecycler = view.findViewById(R.id.exploreRecyclerView);
        context = getContext();
        db = FirebaseFirestore.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // get Main Activity for accessing variables
        mainActivity = (MainActivity) getActivity();
        assert mainActivity != null;
        currentView = view;

        SearchView searchView = view.findViewById(R.id.search_location);
        searchView.setOnQueryTextListener(new LocationQueryListener());

        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        searchLatitude = location.getLatitude();
        searchLongitude = location.getLongitude();

        String fineLocationPermission = Manifest.permission.ACCESS_FINE_LOCATION;

        // Inflate mapFragment
        retrieveEventsAndMap();

        // Setup Weather
        weatherText = view.findViewById(R.id.weatherText);
        queue = Volley.newRequestQueue(requireActivity());

        if (mainActivity.fineLocationPermission && mainActivity.coarseLocationPermission) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100000, 0, locationListener);
        } else {
            Log.d("Weather", "No permissions :(");
            requestPermissions(new String[]{fineLocationPermission}, 1);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100000, 0, locationListener);
            }
        }
    }

    private class AsyncTaskRunner extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            Log.w("Weather Latitude", String.valueOf(latitude));
            Log.w("Weather Longitude", String.valueOf(longitude));

            String tempUrl = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude +
                    "&longitude=" + longitude + "&current_weather=true";
            JsonObjectRequest request = new JsonObjectRequest(tempUrl, null, response -> {
                try {
                    JSONObject jsonObjectSys = response.getJSONObject("current_weather");
                    double temp = jsonObjectSys.getDouble("temperature");
                    String output = "Current Temperature: " + temp + "°C";
                    weatherText.setText(output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> weatherText.setText(R.string.weather_error));
            queue.add(request);
            return null;
        }
    }

    private void updateWeatherData() {
        new AsyncTaskRunner().execute();
    }

    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            if(location != null){
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
            updateWeatherData();
        }
    }

    private class LocationQueryListener implements SearchView.OnQueryTextListener {

        @Override
        public boolean onQueryTextSubmit(String query) {

            // Mocking search here. In actuality, the SearchView
            // would make use of Google Place API and Autocomplete for the search
            // but broke student problems, so we will not be implementing the
            // tool here, but are mocking the search to Tokyo.

            LatLng tokyo = new LatLng(35.6812, 139.7671);
            LatLng vancouver = new LatLng(49.2820, -123.1171);

            LatLng queryLocation = (query.equals("Tokyo"))? tokyo : vancouver;
            searchLatitude = queryLocation.latitude;
            searchLongitude = queryLocation.longitude;

            retrieveEventsAndMap();
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    }

    private void renderMap() {
        if (!isAdded()) return;
        // Inflate mapFragment with searched location
        Fragment mapFragment = MapsFragment.newInstance(this.searchLatitude, this.searchLongitude, this.events, false);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.map_frame_layout, mapFragment).commit();
    }

    private void retrieveEventsAndMap() {
        events = new ArrayList<>();
        Date currentDate = new Date(System.currentTimeMillis());
        Query futureEventsInLocationQuery = db.collection("event")
                .whereGreaterThan("date", currentDate);

        futureEventsInLocationQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document: task.getResult()) {
                    String id = document.getId();
                    String name = (String) document.get("name");
                    String description = (String) document.get("description");
                    Date date = ((Timestamp) Objects.requireNonNull(document.get("date"))).toDate();
                    Double latitude = (Double) document.get("latitude");
                    Double longitude = (Double) document.get("longitude");
                    String hostId = (String) document.get("host_id");
                    Long attendeeLimit = (Long) document.get("attendee_limit");
                    Double hostRating = (Double) document.get("hostRating");
                    List<String> ratings = (List<String>)  document.get("ratings");
                    if (ratings == null) {
                        ratings = new ArrayList<String>();
                    }

                    Event event = new Event(id, name, description, date, latitude, longitude, hostId, attendeeLimit, hostRating, ratings);

                    if (eventInLocationRange(event)) {
                        events.add(event);
                    }
                }
                loadUserEventCardsRecycler();
                renderMap();
            } else {
                Log.e("EVENT ERRORED OUT.", task.getException().getMessage());
                Toast.makeText(context, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean eventInLocationRange(Event event) {
        Double eventLatitude = event.getLatitude();
        Double eventLongitude = event.getLongitude();

        // Search Region borders
        Double eastLatitude = searchLatitude + 1;
        double westLatitude = searchLatitude - 1;
        double northLongitude = searchLongitude + 1;
        double southLongitude = searchLongitude - 1;

        return eventLatitude <= eastLatitude && eventLatitude >= westLatitude
                && eventLongitude <= northLongitude && eventLongitude >= southLongitude;
    }

    private void loadUserEventCardsRecycler() {
        if (events.size() > 0) {
            Log.e("WOW EVENTS", this.events.toString());
            recyclerAdapter adapter = new recyclerAdapter(this.events, this);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
            eventsRecycler.setLayoutManager(layoutManager);
            eventsRecycler.setItemAnimator(new DefaultItemAnimator());
            eventsRecycler.setAdapter(adapter);
        } else {
            TextView noEventsText = currentView.findViewById(R.id.no_events_explore);
            noEventsText.setVisibility(View.VISIBLE);
        }
    }
}
