package com.example.termproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EventItemDialogFragment extends DialogFragment {
    boolean rsvped;
    String eventId;
    Long iconType;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity(), R.style.AlertDialogTheme);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.event_list_item_dialog, null);
        builder.setView(view);
        setEventDialogFields(view);

        // we don't want to include the rsvp button if we've already rsvp'd
        if (!rsvped) {
            builder.setPositiveButton("RSVP", (dialog, id) -> {
                Map<String, Object> confirmation = new HashMap<>();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                confirmation.put("event_id", eventId);
                confirmation.put("user_id", FirebaseAuth.getInstance().getUid());
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
        }

        builder.setNeutralButton("Back", (dialog, id) -> {
            Objects.requireNonNull(EventItemDialogFragment.this.getDialog()).cancel();
        });
        return builder.create();
    }

    private void setEventDialogFields(View view) {
        ImageView imageView = view.findViewById(R.id.eventDialogImageView);
        TextView title = view.findViewById(R.id.eventDialogTitle);
        TextView date = view.findViewById(R.id.eventDialogDate);
        TextView limit = view.findViewById(R.id.eventDialogLimit);
        TextView description = view.findViewById(R.id.eventDialogDescription);

        Bundle bundle = getArguments();
        assert bundle != null;
        title.setText(bundle.getString("name"));
        date.setText(String.format("Date: %s", bundle.getString("date")));
        limit.setText(String.format("Attendee limit: %d", bundle.getLong("limit")));
        description.setText(bundle.getString("description"));
        rsvped = bundle.getBoolean("rsvped");
        eventId = bundle.getString("id");
        iconType = bundle.getLong("icon_type");
        imageView.setImageResource(IconAssignment.getIconMipMapID(iconType));
    }
}
