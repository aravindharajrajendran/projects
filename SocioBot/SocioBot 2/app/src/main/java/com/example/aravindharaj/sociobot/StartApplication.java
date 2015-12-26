package com.example.aravindharaj.sociobot;

import android.app.Application;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SaveCallback;

/**
 * Created by Aravindharaj on 11/20/2015.
 */
public class StartApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(this);
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "wreYJCJ9bAcKN3bMjOzxlbAMUuKjqbWJ6LaTgEhj", "sFxQbyzykkCUWIV4ObJuPWlLuU6soFL4V5OhMfKh");
        ParseUser.enableRevocableSessionInBackground();
        ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("parse installation:","done");
                } else {
                    e.printStackTrace();
                }
            }
        });
        ParseFacebookUtils.initialize(this);
        ParseTwitterUtils.initialize("IdTYlVGXng8G8Opi6q93roiRh", "y9MmtxjKuj0YwEGXRsbnmlAhAlf3y2fkoLarOhYaqntFhfsdEU");
    }
}
