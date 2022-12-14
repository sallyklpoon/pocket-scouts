package com.example.termproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.auth.FirebaseAuth;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

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

        View thisView = inflater.inflate(R.layout.fragment_profile, container, false);

        Button hostEventBtn = thisView.findViewById(R.id.hostEventsBtn);
        hostEventBtn.setOnClickListener((View view) -> getParentFragmentManager().beginTransaction()
                .replace(R.id.page_fragment_container, new CreateEventFragment())
                .addToBackStack(null).commit());
        MaterialRatingBar ratingBar = thisView.findViewById(R.id.rating_bar);

        db.collection("user").document(FirebaseAuth.getInstance().getUid()).get().addOnSuccessListener(documentSnapshot -> {
            Object rating = documentSnapshot.get("rating");
            if (rating instanceof Number) {
                ratingBar.setRating(((Number) rating).floatValue());
            }
        });

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

        Button imageUploadBtn = thisView.findViewById(R.id.dashboardIdVerificationBtn);
        imageUploadBtn.setOnClickListener(view -> selectAndUploadImage());

        Button signOutButton = thisView.findViewById(R.id.dashboardSignOutBtn);
        signOutButton.setOnClickListener((View view) -> {
            firebaseAuth.signOut();
            ((MainActivity) requireActivity()).renderAuthentication();
        });

        getUserDetails(thisView);

        Button saveChangesBtn = thisView.findViewById(R.id.dashboardSaveChangesBtn);
        saveChangesBtn.setOnClickListener(view -> updateUserData(thisView));

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
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentFirebaseUser != null) {
            String uid = currentFirebaseUser.getUid();
            if (filePath != null) {
                StorageReference ref = storage.child(uid);
                ref.putFile(filePath)
                        .addOnSuccessListener(
                                taskSnapshot -> Toast.makeText(requireActivity(), "Your ID has been" +
                                                " uploaded",
                                        Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(requireActivity(),
                                "Upload failed, please try again!" + e.getMessage(),
                                Toast.LENGTH_SHORT).show());
            }
        }
    }

    private void getUserDetails(View view) {

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentFirebaseUser != null) {
            String uid = currentFirebaseUser.getUid();
            DocumentReference docRef = db.collection("user").document(uid);
            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot userData = task.getResult();
                    if (userData.exists()) {

                        String name = (String) userData.get("first_name");
                        EditText nameText = view.findViewById(R.id.editNameInput);
                        nameText.setText(name);

                        String lname = (String) userData.get("last_name");
                        EditText lnameText = view.findViewById(R.id.editlNameInput);
                        lnameText.setText(lname);

                        String dateString = (String) userData.get("birthday");
                        if (dateString != null) {
                            SimpleDateFormat simpleDateFormat = new
                                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                            try {
                                DatePicker datePicker = view.findViewById(R.id.dobDatePicker);
                                Date birthday = simpleDateFormat.parse(dateString);
                                Calendar calendar = Calendar.getInstance();
                                assert birthday != null;
                                calendar.setTime(birthday);
                                int day = calendar.get(Calendar.DAY_OF_MONTH);
                                int month = calendar.get(Calendar.MONTH);
                                int year = calendar.get(Calendar.YEAR);
                                datePicker.updateDate(year, month, day);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        String gender = (String) userData.get("gender");
                        String[] genders = getResources().getStringArray(R.array.gender_list);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                                R.layout.list_item, genders);
                        Spinner editGender = view.findViewById(R.id.editGender);
                        editGender.setAdapter(adapter);

                        int spinnerPosition = adapter.getPosition(gender);
                        editGender.setSelection(spinnerPosition);

                        TextView verificationText = view.findViewById
                                (R.id.dashboardIdVerificationStatus);
                        Button uploadBtn = view.findViewById
                                (R.id.dashboardIdVerificationBtn);

                        boolean isVerified = (boolean) userData.get("verified");

                        if (isVerified) {
                            verificationText.setText(R.string.isVerifiedText);
                            uploadBtn.setEnabled(false);
                            uploadBtn.setBackgroundColor(Color.LTGRAY);
                        }

                        db.collection("event_confirmation").get().
                                addOnCompleteListener(getEventConf -> {
                                    if (getEventConf.isSuccessful()) {
                                        int eventsAttended = 0;
                                        for (QueryDocumentSnapshot document :
                                                getEventConf.getResult()) {
                                            String attendeeID = (String) document.get("user_id");
                                            boolean attended = (boolean) document.get("attended");
                                            if (attendeeID != null && attendeeID.equals(uid)
                                                    && attended) {
                                                eventsAttended++;
                                            }
                                        }

                                        TextView numEventsTotal = view.findViewById
                                                (R.id.numEventsTotal);

                                        numEventsTotal.setText(String.valueOf(eventsAttended));
                                    }
                                });

                    } else {
                        Toast.makeText(requireActivity(),
                                "There was a problem accessing user data. Please exit " +
                                        "the app and try again.",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(requireActivity(),
                            "There was a problem accessing user data. Please exit the " +
                                    "app and try again.",
                            Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(requireActivity(),
                    "There was a problem accessing user data. Please exit the app and try " +
                            "again.",
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void updateUserData(View view) {
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentFirebaseUser != null) {
            String uid = currentFirebaseUser.getUid();

            EditText nameText = view.findViewById(R.id.editNameInput);
            EditText lnameText = view.findViewById(R.id.editlNameInput);
            DatePicker datePicker = view.findViewById(R.id.dobDatePicker);
            Spinner editGender = view.findViewById(R.id.editGender);

            String fname = nameText.getText().toString();
            String lname = lnameText.getText().toString();

            String month = String.valueOf(datePicker.getMonth() + 1);
            String day = String.valueOf(datePicker.getDayOfMonth());
            String year = String.valueOf(datePicker.getYear());

            if (day.length() == 1) {
                day = "0" + day;
            }

            if (month.length() == 1) {
                month = "0" + month;
            }

            String updatedBDay = day + "/" + month + "/" + year;

            String gender = editGender.getSelectedItem().toString();

            Map<String, String> data = new HashMap<>();
            data.put("first_name", fname);
            data.put("last_name", lname);
            data.put("gender", gender);
            data.put("birthday", updatedBDay);

            db.collection("user").document(uid)
                    .set(data, SetOptions.merge())
                    .addOnSuccessListener(documentReference -> Toast.makeText(requireActivity(),
                            "Your data was updated", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(requireActivity(),
                            e.toString(), Toast.LENGTH_SHORT).show());


        }

    }

}