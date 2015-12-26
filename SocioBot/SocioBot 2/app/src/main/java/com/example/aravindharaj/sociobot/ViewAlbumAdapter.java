package com.example.aravindharaj.sociobot;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Aravindharaj on 12/12/2015.
 */
public class ViewAlbumAdapter extends ArrayAdapter<ParseObject> {

    Context context;
    List<ParseObject> result;
    ProgressDialog pd = null;
    List<ParseObject> imageList;
    List<Bitmap> bitmapList = new ArrayList<>();
    ParseObject parseObject;
    int p = 0;

    public ViewAlbumAdapter(Context context, List<ParseObject> result) {
        super(context, R.layout.layout_album, result);
        this.context = context;
        this.result = result;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_album, parent, false);
        }
        final ImageView settingsImage = (ImageView) convertView.findViewById(R.id.imageViewAlbumSettings);
        settingsImage.setVisibility(View.GONE);
        TextView albumName = (TextView) convertView.findViewById(R.id.textViewAlbumName);
        albumName.setSelected(true);
        TextView ownerName = (TextView) convertView.findViewById(R.id.textViewOwnerName);
        TextView creationDate = (TextView) convertView.findViewById(R.id.textViewDateCreated);
        final ImageView albumImage = (ImageView) convertView.findViewById(R.id.imageViewAlbumImage);
        parseObject = result.get(position);
        final ParseUser user = parseObject.getParseUser("owner");
        try {
            ownerName.setText(user.fetchIfNeeded().get("name").toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date = parseObject.getCreatedAt();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy, HH:mm");
        String date_string = sdf.format(date);
        creationDate.setText(date_string);
        albumName.setText(parseObject.get("name").toString());
        List<ParseObject> imagesList = parseObject.getList("images");
        for (int i = 0; i < imagesList.size(); i++) {
            ParseObject object = imagesList.get(i);
            try {
                if (object.fetch().getString("approved").equals("yes")) {
                    ParseFile file = object.getParseFile("file");
                    file.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            if (e == null) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                albumImage.setImageBitmap(bitmap);
                            } else {
                                e.printStackTrace();
                            }
                        }
                    });
                    break;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return convertView;
    }
}