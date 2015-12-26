package com.example.aravindharaj.sociobot;

import java.io.Serializable;

/**
 * Created by Aravindharaj on 11/28/2015.
 */
public class UserCompletionView implements Serializable {

    private String name;

    public UserCompletionView (String n)
    {
        this.name = n;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
