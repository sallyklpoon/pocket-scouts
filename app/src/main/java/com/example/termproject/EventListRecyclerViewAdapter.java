package com.example.termproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EventListRecyclerViewAdapter extends RecyclerView.Adapter<EventListRecyclerViewAdapter.MyViewHolder> {

    Context c;
    String[] titles, descriptions;
    int[] images;

    public EventListRecyclerViewAdapter(Context c, String[] titles, String[] descriptions, int[] images) {
        this.c = c;
        this.titles = titles;
        this.descriptions = descriptions;
        this.images = images;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(c);
        View view = inflater.inflate(R.layout.event_list_item, parent, false);
        return new MyViewHolder((view));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.text1.setText(titles[position]);
        holder.text2.setText(descriptions[position]);
        holder.image.setImageResource(images[position]);
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text1, text2;
        ImageView image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            text1 = itemView.findViewById(R.id.eventTitleText);
            text2 = itemView.findViewById(R.id.eventDescriptionText);
            image = itemView.findViewById(R.id.eventImageView);
        }
    }
}
