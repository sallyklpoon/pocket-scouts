package com.example.termproject;

import java.util.ArrayList;
import java.util.Date;

public class Event {

    private final String id;
    private String name;
    private String description;
    private Date date;
    private Double latitude;
    private Double longitude;
    private final String hostId;
    private Long attendeeLimit;


    public Event(String id, String name, String description,
                 Date date, Double latitude, Double longitude, String hostId, Long limit) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
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
