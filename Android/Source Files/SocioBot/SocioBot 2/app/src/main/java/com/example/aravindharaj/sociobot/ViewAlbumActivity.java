package com.example.aravindharaj.sociobot;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ViewAlbumActivity extends AppCompatActivity {

    ProgressDialog pd = null;
    public static boolean flag = false;
    ImageView zoomImage;
    LinearLayout viewLinearLayout;
    int p = 0;
    ArrayList<Bitmap> imageBitmapList = new ArrayList<>();
    List<ParseObject> imageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_album);
        pd = new ProgressDialog(ViewAlbumActivity.this);
        pd.setMessage("Opening the album..");
        pd.show();
        imageBitmapList.clear();
        imageList.clear();
        int position = getIntent().getIntExtra("position", 0);
        final GridView gridView = (GridView) findViewById(R.id.gridViewAlbumView);
        ParseObject parseObject;
        if (getIntent().getStringExtra("from").equals("album_fragment"))
            parseObject = AlbumFragmentActivity.parseObjects.get(position);
        else
            parseObject = ViewUserAlbumActivity.parseObjects.get(position);
        getSupportActionBar().setTitle(parseObject.getString("name"));
        TextView createdBy = (TextView) findViewById(R.id.textViewOwnerName);
        ParseUser owner = parseObject.getParseUser("owner");
        createdBy.setText(owner.get("name").toString());
        TextView description = (TextView) findViewById(R.id.textViewDescriptionValue);
        description.setText(parseObject.getString("description"));
        TextView privacy = (TextView) findViewById(R.id.textViewPrivacyValue);
        privacy.setText(parseObject.getString("privacy"));
        if (parseObject.getString("privacy").equals(getResources().getString(R.string.stringPrivate))) {
            List<ParseUser> userList = parseObject.getList("access_allowed");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < userList.size(); i++) {
                try {
                    sb.append(userList.get(i).fetchIfNeeded().get("name").toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (i != userList.size() - 1)
                    sb.append(", ");
            }
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_layout_album);
            TextView users = (TextView) findViewById(R.id.textViewUsersValue);
            users.setText(sb.toString());
            linearLayout.setVisibility(View.VISIBLE);
        }
        imageList = parseObject.getList("images");
        for (p = 0; p < imageList.size(); p++) {
            ParseObject image = imageList.get(p);
            ParseFile file = null;
            try {
                if (image.fetch().getString("approved").equals("yes")) {
                    file = image.fetchIfNeeded().getParseFile("file");
                    file.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            if (e == null) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                imageBitmapList.add(bitmap);
                                if (p == imageList.size()) {
                                    AlbumViewAdapter adapter = new AlbumViewAdapter(ViewAlbumActivity.this, imageBitmapList);
                                    adapter.setNotifyOnChange(true);
                                    gridView.setAdapter(adapter);
                                    pd.dismiss();
                                }
                            } else {
                                pd.dismiss();
                                e.printStackTrace();
                                Toast.makeText(ViewAlbumActivity.this, "There was a problem in retrieving the album images", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            } catch (ParseException e) {
                pd.dismiss();
                Toast.makeText(ViewAlbumActivity.this, "There was a problem in retrieving the album images", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
        viewLinearLayout = (LinearLayout) findViewById(R.id.linear_layout_view_album);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                zoomImage = (ImageView) findViewById(R.id.imageViewZoom);
                if (flag == false) {
                    zoomImage.setImageBitmap(imageBitmapList.get(position));
                    Animation fadeIn = new AlphaAnimation(0, 1);
                    fadeIn.setInterpolator(new AccelerateInterpolator());
                    fadeIn.setDuration(500);
                    fadeIn.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            viewLinearLayout.setVisibility(View.VISIBLE);
                            flag = true;
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    viewLinearLayout.startAnimation(fadeIn);
                }
            }
        });
        viewLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == true) {
                    Animation fadeOut = new AlphaAnimation(1, 0);
                    fadeOut.setInterpolator(new AccelerateInterpolator());
                    fadeOut.setDuration(500);
                    fadeOut.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            viewLinearLayout.setVisibility(View.INVISIBLE);
                            flag = false;
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    viewLinearLayout.startAnimation(fadeOut);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (flag == true) {
            if (viewLinearLayout.getVisibility() == View.VISIBLE) {
                Animation fadeOut = new AlphaAnimation(1, 0);
                fadeOut.setInterpolator(new AccelerateInterpolator());
                fadeOut.setDuration(500);
                fadeOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        viewLinearLayout.setVisibility(View.INVISIBLE);
                        flag = false;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                viewLinearLayout.startAnimation(fadeOut);
            }
        } else {
            super.onBackPressed();
        }
    }
}
