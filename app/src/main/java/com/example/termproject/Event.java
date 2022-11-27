package com.example.termproject;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.List;

public class Event implements Parcelable {

    private String id;
    private String name;
    private String description;
    private Date date;
    private Double latitude;
    private Double longitude;
    private String hostId;
    private Long attendeeLimit;
    private Double hostRating;
    private List<String> ratings;
    private Long iconType;

    public Event(String id, String name, String description, Long iconType,
                 Date date, Double latitude, Double longitude, String hostId, Long limit, Double hostRating, List<String> ratings) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.hostId = hostId;
        this.attendeeLimit = limit;
        this.hostRating = hostRating;
        this.ratings = ratings;
        this.iconType = iconType;
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

    public Double getHostRating() { return hostRating;}
  
    public Long getIconType() {return iconType;}

    public Long getAttendeeLimit() {
        return attendeeLimit;
    }

    public List getRatings() { return ratings;}

    public void setTitle(String title) {
        this.name = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
        dest.writeValue(this.latitude);
        dest.writeValue(this.longitude);
        dest.writeString(this.hostId);
        dest.writeValue(this.attendeeLimit);
        dest.writeValue(this.hostRating);
        dest.writeValue(this.ratings);
        dest.writeValue(this.iconType);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.name = source.readString();
        this.description = source.readString();
        long tmpDate = source.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.latitude = (Double) source.readValue(Double.class.getClassLoader());
        this.longitude = (Double) source.readValue(Double.class.getClassLoader());
        this.hostId = source.readString();
        this.attendeeLimit = (Long) source.readValue(Long.class.getClassLoader());
        this.hostRating = (Double) source.readValue(Double.class.getClassLoader());
        this.ratings = (List<String>) source.readValue(List.class.getClassLoader());
        this.iconType = (Long) source.readValue(Long.class.getClassLoader());
    }

    protected Event(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.description = in.readString();
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.latitude = (Double) in.readValue(Double.class.getClassLoader());
        this.longitude = (Double) in.readValue(Double.class.getClassLoader());
        this.hostId = in.readString();
        this.attendeeLimit = (Long) in.readValue(Long.class.getClassLoader());
        this.hostRating = (Double) in.readValue(Double.class.getClassLoader());
        this.ratings = (List<String>) in.readValue(List.class.getClassLoader());
        this.iconType = (Long) in.readValue(Long.class.getClassLoader());
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel source) {
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}
