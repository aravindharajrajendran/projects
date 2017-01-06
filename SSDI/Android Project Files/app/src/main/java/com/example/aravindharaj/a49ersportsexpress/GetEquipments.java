package com.example.aravindharaj.a49ersportsexpress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Aravindharaj on 10/27/2016.
 */

public class GetEquipments {

    public static ArrayList<Equipments> arrayList = new ArrayList<>();

    public static ArrayList<Equipments> getEquipments(JSONArray jsonArray) throws JSONException {
        arrayList.clear();
        for (int i = 0; i < jsonArray.length(); i++) {
            Equipments events = new Equipments();
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            events.set_id(jsonObject.getString("_id"));
            events.setEquipment_type(jsonObject.getString("equipment_type"));
            events.setLogo(jsonObject.getString("logo"));
            events.setAvailability(jsonObject.getString("availability"));
            JSONArray jsonArray1 = jsonObject.getJSONArray("checked_out_by");
            if (jsonArray1.length() < 1)
                events.setCheckedOutBy(null);
            else {
                HashMap<String, Integer> hashMap = new HashMap<>();
                for (int j = 0; j < jsonArray1.length(); j++) {
                    JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                    hashMap.put(jsonObject1.getString("user"), jsonObject1.getInt("count"));
                }
                events.setCheckedOutBy(hashMap);
            }
            arrayList.add(events);
        }
        return arrayList;
    }
}
