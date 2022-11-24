package com.example.termproject;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder> {
    private final ArrayList<Event> eventList;
    private final Fragment fragment;

    public recyclerAdapter(ArrayList<Event> events, Fragment fragment) {
        this.fragment = fragment;
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
        Fragment fragment;
        MaterialCardView eventCard;
        FirebaseFirestore db;

        public MyViewHolder(final View view) {
            super(view);
            titleText = view.findViewById(R.id.eventTitleText);
            descriptionText = view.findViewById(R.id.eventDescriptionText);
            imageView = view.findViewById(R.id.eventImageView);
            eventCard = view.findViewById(R.id.eventCardLayout);

            Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.event_fade_in);
            eventCard.setAnimation(animation);
            db = FirebaseFirestore.getInstance();
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            // check if the event has already been rsvp'd to before rendering modal
            db.collection("event_confirmation")
                    .whereEqualTo("event_id", event.getId())
                    .whereEqualTo("user_id", FirebaseAuth.getInstance().getUid())
                    .get()
                    .addOnCompleteListener(complete -> {
                        EventItemDialogFragment dialog = new EventItemDialogFragment();

                        // if results is empty, we haven't rsvp'd yet so render the rsvp button
                        Bundle bundle = buildEventBundle(event, !complete.getResult().isEmpty());
                        dialog.setArguments(bundle);
                        dialog.show(fragment.getChildFragmentManager(), "event_details");
                    });
        }

        private Bundle buildEventBundle(Event e, boolean rsvped) {
            Bundle bundle = new Bundle();
            bundle.putString("name", event.getName());
            bundle.putString("date", event.getDate().toString());
            bundle.putLong("limit", event.getAttendeeLimit());
            bundle.putString("description", event.getDescription());
            bundle.putBoolean("rsvped", rsvped);
            bundle.putString("id", event.getId());
            bundle.putString("hostId", event.getHostId());
            return bundle;
        }

        public void setEvent(Event e) {
            this.event = e;
        }

        public void setFragment(Fragment f) {
            this.fragment = f;
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
        String currentUser = FirebaseAuth.getInstance().getUid();
        Event event = eventList.get(position);

        holder.setEvent(event);
        holder.setFragment(fragment);
        if (event.getHostId().equals(currentUser)) {
            int color = ContextCompat.getColor(holder.eventCard.getContext(), R.color.purple_light);
            holder.eventCard.setBackgroundColor(color);
        }
        if (event.getDate().before(new Date(System.currentTimeMillis()))) {
            holder.eventCard.setChecked(true);
        }
        holder.titleText.setText(event.getName());
        holder.descriptionText.setText(event.getDescription());
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}
