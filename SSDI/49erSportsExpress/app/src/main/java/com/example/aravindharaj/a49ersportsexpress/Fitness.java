package com.example.aravindharaj.a49ersportsexpress;

import java.util.ArrayList;

/**
 * Created by Aravindharaj on 10/27/2016.
 */

public class Fitness {

    public String object_id;
    public String event_type;
    public String venue;
    public String date;
    public String time;
    public String max_capacity;
    public String availability;
    public String logo;
    public ArrayList<String> userList;

    public ArrayList<String> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList<String> userList) {
        this.userList = userList;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getMax_capacity() {
        return max_capacity;
    }

    public void setMax_capacity(String max_capacity) {
        this.max_capacity = max_capacity;
    }

    public String getObject_id() {
        return object_id;
    }

    public void setObject_id(String object_id) {
        this.object_id = object_id;
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
}
