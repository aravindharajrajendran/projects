package com.example.aravindharaj.a49ersportsexpress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Aravindharaj on 10/27/2016.
 */

public class GetFitness {

    public static ArrayList<Fitness> arrayList = new ArrayList<>();

    public static ArrayList<Fitness> getFitness(JSONArray jsonArray) throws JSONException {
        arrayList.clear();
        for (int i = 0; i < jsonArray.length(); i++) {
            Fitness events = new Fitness();
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            events.setObject_id(jsonObject.getString("_id"));
            events.setEvent_type(jsonObject.getString("event_type"));
            events.setVenue(jsonObject.getString("venue"));
            events.setDate(jsonObject.getString("date"));
            events.setTime(jsonObject.getString("time"));
            events.setMax_capacity(jsonObject.getString("capacity"));
            events.setLogo(jsonObject.getString("logo"));
            events.setAvailability(jsonObject.getString("availability"));
            JSONArray jsonArray1 = jsonObject.getJSONArray("users");
            if (jsonArray1.length() < 1)
                events.setUserList(null);
            else {
                ArrayList<String> stringArrayList = new ArrayList<>();
                for (int j = 0; j < jsonArray1.length(); j++) {
                    stringArrayList.add(jsonArray1.getString(j));
                }
                events.setUserList(stringArrayList);
            }
            arrayList.add(events);
        }
        return arrayList;
    }
}
