package com.example.aravindharaj.sociobot;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ViewMessageActivity extends AppCompatActivity {

    ParseObject parseObject;
    ParseUser user;
    ParseFile file;
    ImageView userImage;
    TextView userName;
    TextView date;
    TextView message;
    ProgressDialog pd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_message);
        pd = new ProgressDialog(this);
        pd.setMessage("Opening Message..");
        pd.show();
        int position = getIntent().getIntExtra("position", 0);
        Log.d("position", position + "");
        userImage = (ImageView) findViewById(R.id.imageViewMessagePicture1);
        userName = (TextView) findViewById(R.id.textViewMessageSender1);
        date = (TextView) findViewById(R.id.textViewMessageSentDate1);
        message = (TextView) findViewById(R.id.textViewMessage1);
        parseObject = MessageFragmentActivity.parseObjects.get(position);
        user = parseObject.getParseUser("from");
        try {
            file = user.fetchIfNeeded().getParseFile("profile_pic");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        file.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
                if (e == null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    userImage.setImageBitmap(bitmap);
                    try {
                        userName.setText(user.fetchIfNeeded().get("name").toString());
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                    Date date1 = parseObject.getCreatedAt();
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy, HH:mm");
                    String date_string = sdf.format(date1);
                    date.setText(date_string);
                    message.setText(parseObject.get("msg").toString());
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Flag");
                    query.whereEqualTo("user", ParseUser.getCurrentUser());
                    query.whereEqualTo("msg", parseObject);
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (e == null) {
                                ParseObject user_flag = objects.get(0);
                                user_flag.put("access_flag", "true");
                                user_flag.saveEventually();
                                pd.dismiss();
                            } else {
                                pd.dismiss();
                                Snackbar.make(MainActivity.coordinatorLayout, "There was a problem in marking the read flag", Snackbar.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    pd.dismiss();
                    Snackbar.make(MainActivity.coordinatorLayout, "There was a problem in retrieving user picture", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
