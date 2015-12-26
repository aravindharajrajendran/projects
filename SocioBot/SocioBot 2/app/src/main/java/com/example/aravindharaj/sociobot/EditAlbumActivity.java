package com.example.aravindharaj.sociobot;

import android.animation.Animator;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Switch;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.tokenautocomplete.TokenCompleteTextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditAlbumActivity extends AppCompatActivity implements TokenCompleteTextView.TokenListener {

    public static boolean flag = false;
    public static ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();
    public static ArrayList<ParseObject> backupFileList = new ArrayList<>();
    public static final int REQ_CODE_IMAGE = 1001;
    GridView gridView;
    public static GridAdapter adapter;
    private int duration;
    ContactsCompletionView users;
    Switch album_switch;
    public EditText albumName;
    EditText albumdesc;
    String switchvalue = new String();
    HashMap<String, String> usermap = new HashMap<>();
    UserCompletionView[] user;
    ArrayList<ParseUser> tokenlist = new ArrayList<>();
    ProgressDialog pd = null;
    ParseObject album;
    public static List<ParseObject> imageIdList = new ArrayList<>();
    ParseObject parseObject;
    public static int position;
    int p = 0;
    List<ParseUser> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_album);
        getSupportActionBar().setTitle("Edit Album");
        position = getIntent().getIntExtra("position", 0);
        parseObject = AlbumFragmentActivity.parseObjects.get(position);
        duration = getResources().getInteger(android.R.integer.config_mediumAnimTime);
        bitmapArrayList.clear();
        backupFileList.clear();
        imageIdList.clear();
        tokenlist.clear();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.add_photo);
        bitmapArrayList.add(bitmap);
        imageIdList = parseObject.getList("images");
        for (p = 0; p < imageIdList.size(); p++) {
            final ParseObject parseObject1 = imageIdList.get(p);
            ParseFile file = null;
            try {
                file = parseObject1.fetch().getParseFile("file");
                file.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] data, ParseException e) {
                        if (e == null) {
                            final ParseFile backupFile = new ParseFile("image_file.png", data);
                            backupFile.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        final ParseObject backupObject = new ParseObject("Images");
                                        backupObject.put("file", backupFile);
                                        backupObject.put("approved", parseObject1.getString("approved"));
                                        backupObject.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null)
                                                    backupFileList.add(backupObject);
                                                else {
                                                    e.printStackTrace();
                                                    Toast.makeText(EditAlbumActivity.this, "There was a problem in retrieving the album images", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(EditAlbumActivity.this, "There was a problem in retrieving the album images", Toast.LENGTH_LONG).show();
                                        e.printStackTrace();
                                    }
                                }
                            });
                            try {
                                if (parseObject1.fetch().getString("approved").equals("yes")) {
                                    Bitmap bitmap1 = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    bitmapArrayList.add(bitmap1);
                                }
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                            if (p == imageIdList.size()) {
                                gridView = (GridView) findViewById(R.id.gridView1);
                                adapter = new GridAdapter(EditAlbumActivity.this, bitmapArrayList, "edit_album");
                                adapter.setNotifyOnChange(true);
                                gridView.setAdapter(adapter);
                            }
                        } else {
                            e.printStackTrace();
                            Toast.makeText(EditAlbumActivity.this, "There was a problem in retrieving the album images", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        albumName = (EditText) findViewById(R.id.editTextAlbumName1);
        albumName.setText(parseObject.getString("name"));
        albumdesc = (EditText) findViewById(R.id.editTextDescription1);
        albumdesc.setText(parseObject.getString("description"));
        users = (ContactsCompletionView) findViewById(R.id.textViewChooseUsers1);
        users.setTokenListener(this);
        users.allowDuplicates(false);
        album_switch = (Switch) findViewById(R.id.switchAlbumPrivacy1);
        if (parseObject.getString("privacy").equals(getResources().getString(R.string.stringPrivate))) {
            album_switch.setChecked(false);
            switchvalue = album_switch.getTextOff().toString();
            userList = parseObject.getList("access_allowed");
            for (int i = 0; i < userList.size(); i++) {
                try {
                    usermap.put(userList.get(i).fetchIfNeeded().get("name").toString(), userList.get(i).getEmail());
                    users.addObject(new UserCompletionView(userList.get(i).fetchIfNeeded().get("name").toString()));
                    users.setVisibility(View.VISIBLE);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } else {
            album_switch.setChecked(true);
            tokenlist.clear();
            switchvalue = album_switch.getTextOn().toString();
            users.setVisibility(View.GONE);
        }
        final Button saveButton = (Button) findViewById(R.id.buttonAlbumSave1);
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("privacy", "Public");
        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.whereContains("username", "@");
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    user = new UserCompletionView[objects.size()];
                    for (int i = 0; i < objects.size(); i++) {
                        try {
                            user[i] = new UserCompletionView(objects.get(i).fetchIfNeeded().get("name").toString());
                            usermap.put(user[i].getName(), objects.get(i).getEmail());
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                    }
                    ArrayAdapter<UserCompletionView> adapter = new ArrayAdapter<UserCompletionView>(EditAlbumActivity.this, android.R.layout.simple_list_item_1, user);
                    users.setAdapter(adapter);
                } else {
                    e.printStackTrace();
                }
            }
        });
        album_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    users.animate().alpha(0f).setDuration(duration).setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            users.setVisibility(View.GONE);
                            switchvalue = album_switch.getTextOn().toString();
                            tokenlist.clear();
                            users.clear();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                } else {
                    users.animate().alpha(1f).setDuration(duration).setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            users.setVisibility(View.VISIBLE);
                            switchvalue = album_switch.getTextOff().toString();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                }
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (albumName.getText().toString().length() < 1)
                    albumName.setError("Enter the album name");
                else if (albumdesc.getText().toString().length() < 1)
                    albumdesc.setError("Enter the album description");
                else if (!(album_switch.isChecked()) && (users.getText().toString().length() < 1)) {
                    users.setError("Select the users before continuing");
                } else if (bitmapArrayList.size() < 2) {
                    Toast.makeText(EditAlbumActivity.this, "Choose pictures for your album", Toast.LENGTH_LONG).show();
                } else {
                    pd = new ProgressDialog(EditAlbumActivity.this);
                    pd.setMessage("Editing your album..");
                    pd.show();
                    parseObject.deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                album = new ParseObject("Album");
                                album.put("name", albumName.getText().toString());
                                album.put("description", albumdesc.getText().toString());
                                album.put("privacy", switchvalue);
                                album.addAllUnique("access_allowed", tokenlist);
                                if (tokenlist.size() > 0) {
                                    ParseACL acl = new ParseACL();
                                    acl.setPublicReadAccess(false);
                                    acl.setPublicWriteAccess(false);
                                    acl.setReadAccess(ParseUser.getCurrentUser(), true);
                                    acl.setWriteAccess(ParseUser.getCurrentUser(), true);
                                    for (int i = 0; i < tokenlist.size(); i++) {
                                        acl.setReadAccess(tokenlist.get(i), true);
                                        acl.setWriteAccess(tokenlist.get(i), true);
                                    }
                                    album.setACL(acl);
                                }
                                album.addAllUnique("images", imageIdList);
                                album.put("owner", ParseUser.getCurrentUser());
                                album.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            if (userList != null)
                                                tokenlist.removeAll(userList);
                                            if (tokenlist.size() > 0) {
                                                for (int i = 0; i < tokenlist.size(); i++) {
                                                    try {
                                                        Map<String, Object> input = new HashMap<String, Object>();
                                                        input.put("title", "SocioBot - Album Shared");
                                                        input.put("alert", ParseUser.getCurrentUser().fetchIfNeeded().get("name") + " has shared a new photo album with you!");
                                                        input.put("users", tokenlist.get(i).getObjectId());
                                                        ParseCloud.callFunctionInBackground("notifyUsers", input, new FunctionCallback<ParseUser>() {
                                                            @Override
                                                            public void done(ParseUser object, ParseException e) {
                                                                if (e == null) {
                                                                    Log.d("notifyUsers", object.getUsername());
                                                                } else
                                                                    e.printStackTrace();
                                                            }
                                                        });
                                                    } catch (ParseException e1) {
                                                        e1.printStackTrace();
                                                    }
                                                }
                                            }
                                            deleteBackupFiles();
                                            pd.dismiss();
                                            EditAlbumActivity.this.finish();
                                            Snackbar.make(MainActivity.coordinatorLayout, "Your Album has been edited successfully", Snackbar.LENGTH_LONG).show();
                                        } else {
                                            pd.dismiss();
                                            Toast.makeText(EditAlbumActivity.this, "There was a problem in editing your album", Toast.LENGTH_LONG).show();
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            } else {
                                pd.dismiss();
                                Toast.makeText(EditAlbumActivity.this, "There was a problem in creating your album", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    });
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
                adapter.notifyDataSetChanged();
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
                            image.put("approved", "yes");
                            image.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        imageIdList.add(image);
                                    } else {
                                        Toast.makeText(EditAlbumActivity.this, "There was a problem in adding the picture", Toast.LENGTH_LONG).show();
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(EditAlbumActivity.this, "There was a problem in adding the picture", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onTokenAdded(Object token) {
        ParseQuery<ParseUser> user = ParseUser.getQuery();
        user.whereEqualTo("username", usermap.get(token.toString()));
        user.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    tokenlist.add(objects.get(0));
                } else {
                    Toast.makeText(EditAlbumActivity.this, "Please select a valid user to continue", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onTokenRemoved(Object token) {
        for (int i = 0; i < tokenlist.size(); i++) {
            if (tokenlist.get(i).getUsername().equals(usermap.get(token.toString()))) {
                tokenlist.remove(i);
            }
        }
    }

    public void deleteBackupFiles() {
        for (int i = 0; i < backupFileList.size(); i++) {
            ParseObject backupObject = backupFileList.get(i);
            backupObject.deleteInBackground();
        }
    }

    @Override
    public void onBackPressed() {
        for (int i = 0; i < imageIdList.size(); i++) {
            ParseObject obj = imageIdList.get(i);
            obj.deleteInBackground();
        }
        parseObject.remove("images");
        parseObject.addAllUnique("images", backupFileList);
        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    EditAlbumActivity.this.finish();
                } else {
                    Toast.makeText(EditAlbumActivity.this, "There was a problem in returning to the previous activity", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }
}
