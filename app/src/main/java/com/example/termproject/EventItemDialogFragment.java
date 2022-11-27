package com.example.termproject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class EventItemDialogFragment extends DialogFragment {
    boolean rsvped;
    String eventId;
    Double hostRating;
    Context context;
    FirebaseFirestore db;
    boolean alreadyRated;
    String currentUser;
    String eventHost;
    Long iconType;
    ImageButton shareBtn;

    String title;
    String date;
    Long limit;
    String description;
    long signedUp;

    ImageView imageView;
    TextView titleView;
    TextView dateView ;
    TextView limitView;
    TextView detailsView;
    TextView eventStatusText;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        context = getContext();
        db = FirebaseFirestore.getInstance();
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity(), R.style.AlertDialogTheme);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.event_list_item_dialog, null);
      
        MaterialRatingBar ratingBar = view.findViewById(R.id.rating_bar);
        imageView = view.findViewById(R.id.eventDialogImageView);
        titleView = view.findViewById(R.id.eventDialogTitle);
        dateView = view.findViewById(R.id.eventDialogDate);
        limitView = view.findViewById(R.id.eventDialogLimit);
        detailsView = view.findViewById(R.id.eventDialogDescription);
        eventStatusText = view.findViewById(R.id.eventStatus);

        // Assign bundle variables
        Bundle bundle = getArguments();
        assert bundle != null;

        title = bundle.getString("name");
        date = bundle.getString("date");
        limit = bundle.getLong("limit");
        description = bundle.getString("description");
        rsvped = bundle.getBoolean("rsvped");
        eventId = bundle.getString("id");
        eventHost = bundle.getString("hostId");
        iconType = bundle.getLong("icon_type");
        alreadyRated = bundle.getBoolean("alreadyRated");

        currentUser = FirebaseAuth.getInstance().getUid();
        shareBtn = view.findViewById(R.id.shareBtn);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireActivity(), R.style.AlertDialogTheme);
        builder.setView(view);

        DocumentReference eventDoc = db.collection("event").document(eventId);

        try {
            setEventDialogFields(view);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // we don't want to include the rsvp button if we've already rsvp'd or if user is host
        if (!rsvped && !eventHost.equals(currentUser)) {
            builder.setPositiveButton("RSVP", (dialog, id) -> {
                Map<String, Object> confirmation = new HashMap<>();
                confirmation.put("event_id", eventId);
                confirmation.put("user_id", currentUser);
                confirmation.put("attended", false);
                db.collection("event_confirmation").add(confirmation)
                        .addOnSuccessListener(task -> {
                            Toast.makeText(view.getContext(), "Successfully RSVPed to the event!", Toast.LENGTH_SHORT).show();
                            Log.d("Event Creation", String.format("Event %s successfully created.", eventId));
                        }).addOnFailureListener(task -> {
                            Toast.makeText(view.getContext(), "Error in RSVPing to the event. Please try again later.", Toast.LENGTH_SHORT).show();
                            Log.d("Event Creation", String.format("Failed to create Event %s.", eventId));
                        });
            });
        } else if (!alreadyRated) {
            ratingBar.setVisibility(View.VISIBLE);
            builder.setPositiveButton("Rate", (dialog, id) -> {
                Map<String, Object> confirmation = new HashMap<>();
                eventDoc.update("ratings", FieldValue.arrayUnion(currentUser)).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentReference userDoc = db.collection("user").document(hostId);
                        userDoc.get().addOnSuccessListener(documentSnapshot -> {
                            Double rating = (Double) documentSnapshot.get("rating");
                            Double difference = ratingBar.getRating() - rating;
                            Double increment = 0.2 * difference;
                            userDoc.update("rating", FieldValue.increment(increment)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(context, "Rating added", Toast.LENGTH_SHORT).show();
                                }
                            });
                        });
                    } else {
                        Toast.makeText(context, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }

        // set share btn
        shareBtn.setOnClickListener(v -> {
            Log.e("testing button", "test");
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            String shareString = String.format("Check out this event I'm attending -- I'm going to %s on %s. Details are: %s. " +
                    "Learn more and download PocketScouts!",
                    title, date, description);
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareString);
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, "Event from PocketScouts");
            startActivity(shareIntent);
        });

        builder.setNeutralButton("Back", (dialog, id) -> Objects.requireNonNull(EventItemDialogFragment.this.getDialog()).cancel());
        return builder.create();
    }

    private void setHostName(String hostId, View view) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user")
                .document(hostId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String hostFirst = (String) task.getResult().get("first_name");
                        String hostLast = (String) task.getResult().get("last_name");
                        String hostName = String.format("%s %s", hostFirst, hostLast);
                        TextView eventHostName = view.findViewById(R.id.eventDialogHost);
                        eventHostName.setText(hostName);
                    }
                });
    }

    private void setConfirmedLimit() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        AggregateQuery countQuery = db.collection("event_confirmation").whereEqualTo("event_id", eventId).count();

        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                signedUp = task.getResult().getCount();
                Log.e("COUNT", String.valueOf(signedUp));
                limitView.setText(String.format(Locale.ENGLISH, "%d/%d people", signedUp, limit));

                if (signedUp > limit) {
                    eventStatusText.setText(R.string.event_limit_reached);
                    eventStatusText.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setEventDialogFields(View view) throws ParseException {
        titleView.setText(title);
        dateView.setText(String.format("%s", date));
        setConfirmedLimit();
        detailsView.setText(description);
        setHostName(eventHost, view);

        if (rsvped) {
            eventStatusText.setText(R.string.user_rsvp_event);
            eventStatusText.setVisibility(View.VISIBLE);
        }
        if (eventHost.equals(currentUser)) {
            eventStatusText.setText(R.string.user_host_of_event);
            eventStatusText.setVisibility(View.VISIBLE);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy", Locale.ENGLISH);
        Date eventDate = formatter.parse(date);
        assert eventDate != null;
        if (eventDate.before(new Date(System.currentTimeMillis()))) {
            eventStatusText.setText(R.string.old_event);
            eventStatusText.setVisibility(View.VISIBLE);
        }
        imageView.setImageResource(IconAssignment.getIconMipMapID(iconType));
    }
}
