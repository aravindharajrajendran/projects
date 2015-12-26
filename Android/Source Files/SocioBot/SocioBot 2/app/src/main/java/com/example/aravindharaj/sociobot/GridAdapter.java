package com.example.aravindharaj.sociobot;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.tokenautocomplete.TokenCompleteTextView;

import java.util.List;

/**
 * Created by Aravindharaj on 11/27/2015.
 */
public class GridAdapter extends ArrayAdapter<Bitmap> {

    Context context;
    List<Bitmap> result;
    View view;
    public static final int REQ_CODE_IMAGE = 1001;
    String activity_name;

    public GridAdapter(Context context, List<Bitmap> result, String activity_name) {
        super(context, R.layout.layout_grid, result);
        this.context = context;
        this.result = result;
        this.activity_name = activity_name;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_grid, parent, false);
            view = convertView;
        }

        final ImageView imageView = (ImageView) convertView.findViewById(R.id.imageViewAddPhoto);
        imageView.setImageBitmap(result.get(position));
        if (activity_name.equals("create_album") || activity_name.equals("edit_album") || activity_name.equals("add_photos")) {
            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (position != 0) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Delete Picture");
                        builder.setMessage("Are you sure you want to remove this picture from the album?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ParseObject object;
                                if (activity_name.equals("create_album"))
                                    object = CreateAlbumActivity.imageIdList.get(position - 1);
                                else if (activity_name.equals("edit_album"))
                                    object = EditAlbumActivity.imageIdList.get(position - 1);
                                else
                                    object = AddPhotosActivity.finalImageIdList.get(position - 1);
                                object.deleteInBackground(new DeleteCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            if (activity_name.equals("create_album")) {
                                                CreateAlbumActivity.imageIdList.remove(position - 1);
                                                CreateAlbumActivity.bitmapArrayList.remove(position);
                                                CreateAlbumActivity.adapter.notifyDataSetChanged();
                                            } else if (activity_name.equals("edit_album")) {
                                                EditAlbumActivity.imageIdList.remove(position - 1);
                                                EditAlbumActivity.bitmapArrayList.remove(position);
                                                EditAlbumActivity.adapter.notifyDataSetChanged();
                                            } else {
                                                AddPhotosActivity.finalImageIdList.remove(position - 1);
                                                AddPhotosActivity.bitmapArrayList.remove(position);
                                                AddPhotosActivity.addPhotoAdapter.notifyDataSetChanged();
                                            }
                                        } else {
                                            Toast.makeText(context, "There was a problem in removing the picture", Toast.LENGTH_LONG).show();
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                    return true;
                }
            });
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position == 0) {
                        if (Build.VERSION.SDK_INT < 19) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            ((Activity) context).startActivityForResult(intent, REQ_CODE_IMAGE);
                        } else {
                            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            intent.setType("image/*");
                            ((Activity) context).startActivityForResult(intent, REQ_CODE_IMAGE);
                        }
                    }
                }
            });
        }
        return convertView;
    }
}