package com.example.termproject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class ProfileFragment extends Fragment {

    private Uri filePath;
    private StorageReference storage;
    private FirebaseFirestore db;
    private final int PICK_IMAGE_REQUEST = 22;
    private FirebaseAuth firebaseAuth;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storage = FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();
        // Inflate the layout for this fragment
        View thisView = inflater.inflate(R.layout.fragment_profile, container, false);
        // Set onClick Listener for Create Event
        Button hostEventBtn = thisView.findViewById(R.id.hostEventsBtn);
        hostEventBtn.setOnClickListener((View view) -> getParentFragmentManager().beginTransaction()
                .replace(R.id.page_fragment_container, new CreateEventFragment()).commit());

        // Set onClick Listener for My Events
        Button myEventsBtn = thisView.findViewById(R.id.myEventsBtn);
        myEventsBtn.setOnClickListener((View view) -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.page_fragment_container, new EventFragment()).commit();
            if (getActivity() != null) {
                BottomNavigationView bottomNavigationView =
                        getActivity().findViewById(R.id.bottom_navigation);
                bottomNavigationView.getMenu().findItem(R.id.nav_events).setChecked(true);
            }
        });
        Button signOutButton = thisView.findViewById(R.id.dashboardSignOutBtn);
        signOutButton.setOnClickListener((View view) -> {
            firebaseAuth.signOut();
            ((MainActivity) getActivity()).renderAuthentication();
        });

        // Listener for image upload
        Button imageUploadBtn = thisView.findViewById(R.id.dashboardIdVerificationBtn);
        imageUploadBtn.setOnClickListener(view -> selectAndUploadImage());

        return thisView;
    }

    private void selectAndUploadImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,
                resultCode,
                data);
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == Activity.RESULT_OK
                && data != null
                && data.getData() != null) {
            filePath = data.getData();
            uploadImage();
        }
    }

    private void uploadImage() {
        if (filePath != null) {
            StorageReference ref = storage.child(UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot -> Toast.makeText(
                            requireActivity(), "Your ID has been uploaded",
                            Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> {
                        Toast.makeText(requireActivity(),
                                "Upload failed, please try again!" + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void uploadToDB(String imageURL) {
        // When verification is implemented, upload user_id (passed via argument) and image url
        // Will be called after image is uploaded to storage to get url
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);
        db.collection("test3717")
                .add(user)
                .addOnSuccessListener(documentReference -> Toast.makeText(requireActivity(),
                        "Count updated", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(requireActivity(),
                        e.toString(), Toast.LENGTH_SHORT).show());

    }
}