package com.example.aravindharaj.sociobot;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aravindharaj on 12/2/2015.
 */
public class AlbumViewAdapter extends ArrayAdapter<Bitmap> {

    Context context;
    List<Bitmap> result;

    public AlbumViewAdapter(Context context, List<Bitmap> result) {
        super(context, R.layout.layout_grid, result);
        this.context = context;
        this.result = result;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_grid, parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageViewAddPhoto);
        imageView.setImageBitmap(result.get(position));
        return convertView;
    }
}
