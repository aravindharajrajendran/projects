package com.example.aravindharaj.sociobot;

import android.content.Context;

import com.parse.ParseUser;

import java.util.ArrayList;

/**
 * Created by Aravindharaj on 11/22/2015.
 */
public class UserValues {

    public static ArrayList<Value> values = new ArrayList<>();
    public static String[] columns = {"email", "gender", "dob", "phone", "privacy", "push"};



    public static ArrayList<Value> userValues(Context context, ParseUser user) {
        values.clear();
        for (int i = 0; i < columns.length; i++) {
            Value value = new Value();
            if (!(columns[i].equals("push"))) {
                value.setValue(user.get(columns[i]).toString());
                value.setImage(context.getResources().getIdentifier(columns[i], "drawable", context.getPackageName()));
            } else {
                if (user.get(columns[i]).toString().equals("true"))
                    value.setValue("Push Enabled");
                else
                    value.setValue("Push Disabled");
                value.setImage(context.getResources().getIdentifier(columns[i], "drawable", context.getPackageName()));
            }
            values.add(value);
        }
        return values;
    }
}
