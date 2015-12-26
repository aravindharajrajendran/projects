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
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.PopupMenu;
import android.transition.Fade;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Aravindharaj on 12/2/2015.
 */
public class AlbumAdapter extends ArrayAdapter<ParseObject> {

    Context context;
    List<ParseObject> result;
    ProgressDialog pd = null;
    List<ParseObject> imageList;
    List<Bitmap> bitmapList = new ArrayList<>();
    ParseObject parseObject;
    int p = 0;

    public AlbumAdapter(Context context, List<ParseObject> result) {
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
                    ParseFile file = object.fetch().getParseFile("file");
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
        settingsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == ParseUser.getCurrentUser()) {
                    PopupMenu menu = new PopupMenu(context, settingsImage, Gravity.BOTTOM | Gravity.RIGHT);
                    menu.getMenuInflater().inflate(R.menu.menu_album, menu.getMenu());
                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int id = item.getItemId();
                            if (id == R.id.editAlbum) {
                                Intent intent = new Intent(context, EditAlbumActivity.class);
                                intent.putExtra("position", position);
                                context.startActivity(intent);
                            }
                            if (id == R.id.deleteAlbum) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("Delete Album");
                                builder.setMessage("Are you sure you want to delete the selected album?");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        pd = new ProgressDialog(context);
                                        pd.setMessage("Deleting Album..");
                                        pd.show();
                                        ParseObject parseObject = AlbumFragmentActivity.parseObjects.get(position);
                                        try {
                                            imageList = parseObject.fetch().getList("images");
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                            pd.dismiss();
                                        }
                                        for (int i = 0; i < imageList.size(); i++) {
                                            ParseObject individualImage = imageList.get(i);
                                            individualImage.deleteInBackground();
                                        }
                                        ParseQuery<ParseObject> approval_query = ParseQuery.getQuery("Approval");
                                        approval_query.whereEqualTo("album", parseObject);
                                        approval_query.findInBackground(new FindCallback<ParseObject>() {
                                            @Override
                                            public void done(List<ParseObject> objects, ParseException e) {
                                                if (e == null) {
                                                    if (objects.size() > 0) {
                                                        for (int i = 0; i < objects.size(); i++) {
                                                            ParseObject object = objects.get(i);
                                                            object.deleteInBackground();
                                                        }
                                                    }
                                                } else {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        parseObject.deleteInBackground(new DeleteCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                    AlbumFragmentActivity.parseObjects.remove(position);
                                                    AlbumFragmentActivity.adapter.notifyDataSetChanged();
                                                    pd.dismiss();
                                                } else {
                                                    pd.dismiss();
                                                    Snackbar.make(MainActivity.coordinatorLayout, "There was a problem in deleting the album", Snackbar.LENGTH_LONG).show();
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
                    menu.show();
                } else {
                    PopupMenu menu = new PopupMenu(context, settingsImage, Gravity.BOTTOM | Gravity.RIGHT);
                    menu.getMenuInflater().inflate(R.menu.menu_album_public, menu.getMenu());
                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int id = item.getItemId();
                            if (id == R.id.addPhotos) {
                                Intent intent = new Intent(context, AddPhotosActivity.class);
                                intent.putExtra("position", position);
                                context.startActivity(intent);
                            }
                            return true;
                        }
                    });
                    menu.show();
                }
            }
        });
        return convertView;
    }

    private void animate(final ImageView imageView, final List<Bitmap> images, final int imageIndex, final boolean forever) {

        final AnimationDrawable animationDrawable = new AnimationDrawable();
        for (int i = 0; i < images.size(); i++) {
            animationDrawable.addFrame(new BitmapDrawable(context.getResources(), images.get(i)), 4000);
        }
        animationDrawable.setEnterFadeDuration(500);
        animationDrawable.setExitFadeDuration(500);
        animationDrawable.setOneShot(false);
        imageView.setImageDrawable(animationDrawable);
        animationDrawable.start();
    }
}