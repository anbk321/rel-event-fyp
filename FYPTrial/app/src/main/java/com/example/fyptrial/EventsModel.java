package com.example.fyptrial;

import android.net.Uri;

import java.io.Serializable;

public class EventsModel implements Serializable
{
    private String title;
    private String time;
    private String venue;
    private String description;
    private String source;
    private String picture;
    private String type;
    private String city;
    private String userEmail;

    public EventsModel(String title, String time, String venue, String description, String source, String picture, String type, String city, String userEmail) {
        this.title = title;
        this.time = time;
        this.venue = venue;
        this.description = description;
        this.source = source;
        this.picture = picture;
        this.type = type;
        this.city = city;
        this.userEmail = userEmail;
    }

    public EventsModel() {
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}

