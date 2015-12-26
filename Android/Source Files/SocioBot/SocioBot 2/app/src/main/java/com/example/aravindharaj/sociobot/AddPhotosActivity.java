package com.example.aravindharaj.sociobot;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.GetDataCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddPhotosActivity extends AppCompatActivity {

    public static List<Bitmap> bitmapList = new ArrayList<>();
    public static List<Bitmap> bitmapArrayList = new ArrayList<>();
    public static final int REQ_CODE_IMAGE = 1001;
    public static List<ParseObject> imageIdList = new ArrayList<>();
    public static List<ParseObject> finalImageIdList = new ArrayList<>();
    GridView gridViewExistingPhotos;
    GridView gridViewAddPhotos;
    GridAdapter adapter;
    public static GridAdapter addPhotoAdapter;
    ParseObject parseObject;
    ProgressDialog pd = null;
    int p = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photos);
        getSupportActionBar().setTitle("Add Photos");
        bitmapArrayList.clear();
        bitmapList.clear();
        imageIdList.clear();
        finalImageIdList.clear();
        int position = getIntent().getIntExtra("position", 0);
        parseObject = AlbumFragmentActivity.parseObjects.get(position);
        imageIdList = parseObject.getList("images");
        for (p = 0; p < imageIdList.size(); p++) {
            ParseObject parseObject1 = imageIdList.get(p);
            ParseFile file = null;
            try {
                if (parseObject1.fetchIfNeeded().getString("approved").equals("yes")) {
                    file = parseObject1.fetchIfNeeded().getParseFile("file");
                    file.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            if (e == null) {
                                Bitmap bitmap1 = BitmapFactory.decodeByteArray(data, 0, data.length);
                                bitmapList.add(bitmap1);
                                if (p == imageIdList.size()) {
                                    gridViewExistingPhotos = (GridView) findViewById(R.id.gridViewExistingPhotos);
                                    adapter = new GridAdapter(AddPhotosActivity.this, bitmapList, "view_photos");
                                    adapter.setNotifyOnChange(true);
                                    gridViewExistingPhotos.setAdapter(adapter);
                                }
                            } else {
                                Toast.makeText(AddPhotosActivity.this, "There was a problem in retrieving the album images", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    });
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.add_photo);
        bitmapArrayList.add(bitmap);
        gridViewAddPhotos = (GridView) findViewById(R.id.gridViewAddPhotos);
        addPhotoAdapter = new GridAdapter(this, bitmapArrayList, "add_photos");
        addPhotoAdapter.setNotifyOnChange(true);
        gridViewAddPhotos.setAdapter(addPhotoAdapter);
        Button saveButton = (Button) findViewById(R.id.buttonAddPhotosSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmapArrayList.size() < 2) {
                    Toast.makeText(AddPhotosActivity.this, "Choose pictures to add", Toast.LENGTH_LONG).show();
                } else {
                    pd = new ProgressDialog(AddPhotosActivity.this);
                    pd.setMessage("Adding your photos..");
                    pd.show();
                    for (p = 0; p < finalImageIdList.size(); p++) {
                        ParseObject approve_object = new ParseObject("Approval");
                        approve_object.put("album", parseObject);
                        approve_object.put("owner", parseObject.get("owner"));
                        approve_object.put("photo", finalImageIdList.get(p));
                        approve_object.put("added_by", ParseUser.getCurrentUser());
                        approve_object.put("approved", "no");
                        approve_object.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    if (p == finalImageIdList.size()) {
                                        finalImageIdList.addAll(imageIdList);
                                        parseObject.addAllUnique("images", finalImageIdList);
                                        parseObject.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                    try {
                                                        Map<String, Object> input = new HashMap<String, Object>();
                                                        input.put("title", "SocioBot - New Photos added to the album");
                                                        input.put("alert", ParseUser.getCurrentUser().fetchIfNeeded().get("name") + " has added new photos to your album: " + parseObject.fetchIfNeeded().getString("name"));
                                                        input.put("users", parseObject.getParseUser("owner").getObjectId());
                                                        ParseCloud.callFunctionInBackground("notifyUsers", input, new FunctionCallback<ParseUser>() {
                                                            @Override
                                                            public void done(ParseUser object, ParseException e) {
                                                                if (e == null) {
                                                                    if (pd != null)
                                                                        pd.dismiss();
                                                                    Log.d("notifyUsers", object.getUsername());
                                                                } else {
                                                                    if (pd != null)
                                                                        pd.dismiss();
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        });
                                                    } catch (ParseException e1) {
                                                        if (pd != null)
                                                            pd.dismiss();
                                                        e1.printStackTrace();
                                                    }
                                                    if (pd != null)
                                                        pd.dismiss();
                                                    AddPhotosActivity.this.finish();
                                                    Snackbar.make(MainActivity.coordinatorLayout, "Your photos have been sent for approval", Snackbar.LENGTH_LONG).show();
                                                } else {
                                                    if (pd != null)
                                                        pd.dismiss();
                                                    Toast.makeText(AddPhotosActivity.this, "There was a problem in adding your photos", Toast.LENGTH_LONG).show();
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    }
                                } else {
                                    if (pd != null)
                                        pd.dismiss();
                                    Toast.makeText(AddPhotosActivity.this, "There was a problem in adding your photos", Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    if (pd != null)
                        pd.dismiss();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_IMAGE) {
            if (resultCode == RESULT_OK) {
                Bitmap picture = null;
                try {
                    picture = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bitmapArrayList.add(picture);
                addPhotoAdapter.notifyDataSetChanged();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                picture.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                final ParseFile file = new ParseFile("image_file.png", byteArray);
                file.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            final ParseObject image = new ParseObject("Images");
                            image.put("file", file);
                            image.put("approved", "no");
                            image.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        finalImageIdList.add(image);
                                    } else {
                                        Toast.makeText(AddPhotosActivity.this, "There was a problem in adding the picture", Toast.LENGTH_LONG).show();
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(AddPhotosActivity.this, "There was a problem in adding the picture", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onBackPressed() {
        for (int i = 0; i < finalImageIdList.size(); i++) {
            ParseObject object = finalImageIdList.get(i);
            object.deleteInBackground();
        }
        super.onBackPressed();
    }
}
