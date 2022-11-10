package com.example.termproject;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder>{
    private final ArrayList<Event> eventList;

    public recyclerAdapter(ArrayList<Event> events) {
        this.eventList = events;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titleText;
        TextView descriptionText;

        public MyViewHolder(final View view) {
            super(view);
            titleText = view.findViewById(R.id.eventTitleText);
            descriptionText = view.findViewById(R.id.eventDescriptionText);
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
        holder.titleText.setText(event.getTitle());
        holder.descriptionText.setText(event.getDescription());
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}
