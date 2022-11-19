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

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

public class ExploreFragment extends Fragment {
    double longitude = 1;
    double latitude = 1;
    MainActivity mainActivity;

    RequestQueue queue;
    private LocationManager locationManager;
    private LocationListener locationListener;

    TextView weatherText;

    public ExploreFragment() { super(R.layout.fragment_explore); }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        inflater.inflate(R.layout.fragment_explore, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // get Main Activity for accessing variables
        mainActivity = (MainActivity) getActivity();
        assert mainActivity != null;

        SearchView searchView = view.findViewById(R.id.search_location);
        searchView.setOnQueryTextListener(new LocationQueryListener());


        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        String fineLocationPermission = Manifest.permission.ACCESS_FINE_LOCATION;

        // Inflate mapFragment
        Fragment mapFragment = MapsFragment.newInstance(location.getLatitude(), location.getLongitude());
        getChildFragmentManager().beginTransaction()
                .replace(R.id.map_frame_layout, mapFragment).commit();

        // Setup Weather
        weatherText = view.findViewById(R.id.weatherText);
        queue = Volley.newRequestQueue(requireActivity());

        if (mainActivity.fineLocationPermission && mainActivity.coarseLocationPermission) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
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
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
            }
        }
    }

    private boolean permissionGranted(String permission) {
        return ActivityCompat.checkSelfPermission(requireActivity(), permission) == PackageManager.PERMISSION_GRANTED;
    }

    private class AsyncTaskRunner extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            Log.e("Weather Latitude", String.valueOf(latitude));
            Log.e("Weather Longitude", String.valueOf(longitude));

            String tempUrl = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude +
                    "&longitude=" + longitude + "&current_weather=true";
            JsonObjectRequest request = new JsonObjectRequest(tempUrl, null, response -> {
                try {
                    JSONObject jsonObjectSys = response.getJSONObject("current_weather");
                    double temp = jsonObjectSys.getDouble("temperature");
                    String output = "The temperature is: " + temp + " degrees Celsius.";
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

            // Inflate mapFragment with new searched location
            Fragment mapFragment = MapsFragment.newInstance(queryLocation.latitude, queryLocation.longitude);
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.map_frame_layout, mapFragment).commit();
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    }
}
