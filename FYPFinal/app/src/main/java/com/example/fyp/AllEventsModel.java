package com.example.fyp;

import java.io.Serializable;

public class AllEventsModel implements  Comparable,Serializable {

    private String id;
    private String title;
    private String time;
    private String venue;
    private String description;
    private String source;
    private String picture;
    private String type;
    private String city;
    private int pastFuture;
    private int rating;
    private String rated;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public AllEventsModel(String id, String title, String time, String venue, String description, String source, String picture, String type, String city, int pastFuture, int rating) {
        this.id = id;
        this.title = title;
        this.time = time;
        this.venue = venue;
        this.description = description;
        this.source = source;
        this.picture = picture;
        this.type = type;
        this.city = city;
        this.pastFuture = pastFuture;
        this.rating = rating;
        this.rated = "TRUE";
    }

    public String isRated() {
        return rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public AllEventsModel(String id, String title, String time, String venue, String description, String source, String picture, String type, String city, int pastFuture) {
        this.id = id;
        this.title = title;
        this.time = time;
        this.venue = venue;
        this.description = description;
        this.source = source;
        this.picture = picture;
        this.type = type;
        this.city = city;
        this.pastFuture = pastFuture;
        this.rating = -1;
        this.rated = "FALSE";
    }

    public AllEventsModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getPastFuture() {
        return pastFuture;
    }

    public void setPastFuture(int pastFuture) {
        this.pastFuture = pastFuture;
    }

    @Override
    public int compareTo(Object object ) {
        int ratingCompare = ((AllEventsModel)object).getRating();
        return ratingCompare-this.getRating();
    }
}
