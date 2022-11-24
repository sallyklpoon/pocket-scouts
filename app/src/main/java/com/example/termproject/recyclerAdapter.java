package com.example.termproject;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

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
        MaterialRatingBar ratingBar;

        public MyViewHolder(final View view) {
            super(view);
            titleText = view.findViewById(R.id.eventTitleText);
            descriptionText = view.findViewById(R.id.eventDescriptionText);
            imageView = view.findViewById(R.id.eventImageView);
            ratingBar = view.findViewById(R.id.rating_bar);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

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
            bundle.putDouble("hostRating", event.getHostRating());
            bundle.putBoolean("alreadyRated", event.getRatings().contains(FirebaseAuth.getInstance().getUid()));
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
        Event event = eventList.get(position);
        holder.setEvent(event);
        holder.setFragment(fragment);
        holder.titleText.setText(event.getName());
        holder.ratingBar.setRating(event.getHostRating().floatValue());
        holder.descriptionText.setText(event.getDescription());
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}
