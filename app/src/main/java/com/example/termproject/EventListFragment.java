package com.example.termproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;

public class EventListFragment extends Fragment {
    static final int DUMMY_LENGTH = 10;
    RecyclerView recyclerView;
    String[] titles, descriptions;
    int[] images;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        recyclerView = view.findViewById(R.id.eventListRecyclerView);

        titles = getDummyStrings("title");
        descriptions = getDummyStrings("description");
        images = getDummyImages();

        EventListRecyclerViewAdapter myRecyclerViewAdapter = new EventListRecyclerViewAdapter(getActivity(), titles, descriptions, images);
        recyclerView.setAdapter(myRecyclerViewAdapter);
        return view;
    }

    private static String[] getDummyStrings(String val) {
        String[] strings = new String[DUMMY_LENGTH];
        for (int i=0; i<DUMMY_LENGTH; i++) {
            strings[i] = val+i;
        }
        return strings;
    }

    private static int[] getDummyImages() {
        int[] images = new int[DUMMY_LENGTH];
        Arrays.fill(images, R.drawable.walbert_full);
        return images;
    }
}