package com.example.aravindharaj.sociobot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Aravindharaj on 11/24/2015.
 */
public class ListAdapter extends ArrayAdapter<ParseObject> {

    Context context;
    List<ParseObject> result;

    public ListAdapter(Context context, List<ParseObject> result) {
        super(context, R.layout.layout_message, result);
        this.context = context;
        this.result = result;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_message, parent, false);
        }

        final ImageView userImage = (ImageView) convertView.findViewById(R.id.imageViewMessagePicture);
        final TextView messageSender = (TextView) convertView.findViewById(R.id.textViewMessageSender);
        final TextView sentDate = (TextView) convertView.findViewById(R.id.textViewMessageSentDate);
        final ImageView starImage = (ImageView) convertView.findViewById(R.id.imageViewStar);
        final ParseObject parseObject = result.get(position);
        final ParseUser user = parseObject.getParseUser("from");
        ParseFile file = null;
        try {
            file = user.fetchIfNeeded().getParseFile("profile_pic");
            file.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        userImage.setImageBitmap(bitmap);
                        try {
                            messageSender.setText(user.fetchIfNeeded().get("name").toString());
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                        Date date = parseObject.getCreatedAt();
                        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy, HH:mm");
                        String date_string = sdf.format(date);
                        sentDate.setText(date_string);
                        ParseQuery<ParseObject> flag_query = ParseQuery.getQuery("Flag");
                        flag_query.whereEqualTo("msg", parseObject);
                        flag_query.whereEqualTo("user", ParseUser.getCurrentUser());
                        flag_query.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                if (e == null) {
                                    String flag = null;
                                    try {
                                        flag = objects.get(0).fetchIfNeeded().get("access_flag").toString();
                                        if (flag.equals("false")) {
                                            starImage.setVisibility(View.VISIBLE);
                                        } else {
                                            starImage.setVisibility(View.INVISIBLE);
                                        }
                                    } catch (ParseException e1) {
                                        e1.printStackTrace();
                                    }
                                } else {
                                    e.printStackTrace();
                                    Snackbar.make(MainActivity.coordinatorLayout, "There was a problem in retrieving message flag", Snackbar.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else {
                        e.printStackTrace();
                        Snackbar.make(MainActivity.coordinatorLayout, "There was a problem in retrieving user picture", Snackbar.LENGTH_LONG).show();
                    }
                }
            });
        } catch (ParseException e) {
            Snackbar.make(MainActivity.coordinatorLayout, "There was a problem in retrieving user picture", Snackbar.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return convertView;
    }
}
