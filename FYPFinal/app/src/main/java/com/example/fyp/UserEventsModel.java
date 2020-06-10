package com.example.fyp;

public class UserEventsModel {
    private String userId, eventId;
    private int rating;

    public UserEventsModel(String userId, String eventId, int rating) {
        this.userId = userId;
        this.eventId = eventId;
        this.rating = rating;
    }

    public UserEventsModel() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
