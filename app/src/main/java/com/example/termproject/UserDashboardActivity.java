// TODO: migrate Firebase/Firestore set up to ProfileFragment

//package com.example.termproject;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentTransaction;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.util.HashMap;
//import java.util.Map;
//
//
//public class UserDashboardActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.user_dashboard);
//        goToCreateEvent();
//        firestoreUpload();
//    }
//
//    private void goToCreateEvent() {
//        Button eventsBtn = findViewById(R.id.hostEventsBtn);
//        Intent goToCreateEvent = new Intent(this, CreateEventActivity.class);
//        eventsBtn.setOnClickListener(view -> startActivity(goToCreateEvent));
//    }
//
//    private void firestoreUpload() {
//        Button test = findViewById(R.id.myEventsBtn);
//        test.setOnClickListener(view -> testUpload());
//    }
//
//    private void testUpload() {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        Map<String, Object> user = new HashMap<>();
//        user.put("first", "Ada");
//        user.put("last", "Lovelace");
//        user.put("born", 1815);
//        db.collection("test3717")
//                .add(user)
//                .addOnSuccessListener(documentReference -> Toast.makeText(UserDashboardActivity.this,
//                        "Count updated", Toast.LENGTH_SHORT).show())
//                .addOnFailureListener(e -> Toast.makeText(UserDashboardActivity.this,
//                        e.toString(), Toast.LENGTH_SHORT).show());
//
//    }
//
//}
