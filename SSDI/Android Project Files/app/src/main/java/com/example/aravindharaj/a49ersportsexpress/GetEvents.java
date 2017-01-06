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

public class GetEvents {

    public static ArrayList<ArrayList<Events>> arrayList = new ArrayList<>();

    public static ArrayList<ArrayList<Events>> getEvents(JSONArray jsonArray) throws JSONException {
        arrayList.clear();
        ArrayList<Events> list1 = new ArrayList<>();
        ArrayList<Events> list2 = new ArrayList<>();
        arrayList.add(list1);
        arrayList.add(list2);
        for (int i = 0; i < jsonArray.length(); i++) {
            Events events = new Events();
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            events.setObject_id(jsonObject.getString("_id"));
            events.setGame_type(jsonObject.getString("game_type"));
            events.setTeam1(jsonObject.getString("team_1"));
            events.setTeam2(jsonObject.getString("team_2").replaceAll("[^A-Za-z0-9 ]+", " ").toString());
            events.setVenue(jsonObject.getString("venue"));
            events.setDate(jsonObject.getString("date"));
            events.setTime(jsonObject.getString("time"));
            events.setMax_capacity(jsonObject.getString("max_capacity"));
            events.setTicket_price(Integer.parseInt(jsonObject.getString("ticket_price")));
            events.setLogo(jsonObject.getString("logo"));
            events.setAvailability(jsonObject.getString("availability"));
            events.setResult(jsonObject.getString("result"));
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
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            if (jsonObject.getString("date").equalsIgnoreCase(sdf.format(d)))
                arrayList.get(0).add(events);
            else
                arrayList.get(1).add(events);
        }
        return arrayList;
    }
}
