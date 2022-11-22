package com.example.termproject;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;

import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder> {
    private final ArrayList<Event> eventList;

    public recyclerAdapter(ArrayList<Event> events) {
        this.eventList = events;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Event event;
        TextView titleText;
        TextView descriptionText;
        CircleImageView imageView;

        public MyViewHolder(final View view) {
            super(view);
            titleText = view.findViewById(R.id.eventTitleText);
            descriptionText = view.findViewById(R.id.eventDescriptionText);
            imageView = view.findViewById(R.id.eventImageView);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            MaterialAlertDialogBuilder confirmEvent = new MaterialAlertDialogBuilder(v.getContext());
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            confirmEvent.setTitle(titleText.getText().toString())
                    .setMessage(descriptionText.getText().toString());

            db.collection("event_confirmation")
                    .whereEqualTo("event_id", event.getId())
                    .whereEqualTo("user_id", FirebaseAuth.getInstance().getUid())
                    .get()
                    .addOnCompleteListener(complete -> {
                        if (complete.getResult().isEmpty()) {
                            confirmEvent.setPositiveButton("Confirm RSVP", (dialogInterface, i) -> {
                                Map<String, Object> confirmation = new HashMap<>();
                                confirmation.put("event_id", event.getId());
                                confirmation.put("user_id", FirebaseAuth.getInstance().getUid());
                                confirmation.put("attended", false);
                                db.collection("event_confirmation").add(confirmation)
                                        .addOnSuccessListener(task -> {
                                            Toast.makeText(v.getContext(), "Successfully RSVPed to the event!", Toast.LENGTH_SHORT).show();
                                        }).addOnFailureListener(task -> {
                                            Toast.makeText(v.getContext(), "Error in RSVPing to the event. Please try again later.", Toast.LENGTH_SHORT).show();
                                        });

                            });
                        }
                        confirmEvent.setNegativeButton("Back", (dialogInterface, i) -> {

                        });
                        confirmEvent.show();
                    });
        }

        public void setEvent(Event e) {
            this.event = e;
        }
    }


    @NonNull
    @Override
    public recyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.setEvent(event);
        holder.titleText.setText(event.getName());
        holder.descriptionText.setText(event.getDescription());
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}
