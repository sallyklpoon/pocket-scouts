package com.example.termproject;

import java.util.ArrayList;
import java.util.Date;

public class Event {

    private final String id;
    private String name;
    private String description;
    private Date date;
    private ArrayList<Double> location = new ArrayList<>();
    private final String hostId;
    private Long attendeeLimit;


    public Event(String id, String name, String description,
                 Date date, ArrayList<Double> location, String hostId, Long limit) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
        this.location = location;
        this .hostId = hostId;
        this.attendeeLimit = limit;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public Date getDate() {
        return date;
    }

    public ArrayList<Double> getLocation() {
        return location;
    }

    public String getHostId() {
        return hostId;
    }

    public Long getAttendeeLimit() {
        return attendeeLimit;
    }

    public void setTitle(String title) {
        this.name = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
