package com.example.aravindharaj.sociobot;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ViewUserAlbumActivity extends AppCompatActivity {

    ProgressDialog pd = null;
    ParseUser user;
    public static List<ParseObject> parseObjects = new ArrayList<>();
    public static ViewAlbumAdapter adapter;
    GridView gridView;
    TextView noAlbums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_album);
        String uname = getIntent().getStringExtra("uname");
        pd = new ProgressDialog(this);
        pd.setMessage("Loading..");
        pd.show();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", uname);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    user = objects.get(0);
                    try {
                        getSupportActionBar().setTitle(user.fetchIfNeeded().getString("firstname") + "'s Public Albums");
                        ParseQuery<ParseObject> album_query = ParseQuery.getQuery("Album");
                        album_query.whereEqualTo("owner", user);
                        album_query.whereEqualTo("privacy", "Public");
                        album_query.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                noAlbums = (TextView) findViewById(R.id.textViewNoAlbums);
                                if (e == null) {
                                    if (objects.size() > 0) {
                                        parseObjects.clear();
                                        setAdapterContents(objects);
                                        pd.dismiss();
                                    } else {
                                        parseObjects.clear();
                                        if (adapter != null) {
                                            adapter.notifyDataSetChanged();
                                        }
                                        noAlbums.setVisibility(View.VISIBLE);
                                        pd.dismiss();
                                    }
                                } else {
                                    pd.dismiss();
                                    e.printStackTrace();
                                    Toast.makeText(ViewUserAlbumActivity.this,"There was a problem in retrieving the user albums",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } catch (ParseException e1) {
                        getSupportActionBar().setTitle("User's Public Albums");
                        pd.dismiss();
                        e1.printStackTrace();
                    }
                } else {
                    Toast.makeText(ViewUserAlbumActivity.this,"There was a problem in retrieving the user",Toast.LENGTH_LONG).show();
                    pd.dismiss();
                    e.printStackTrace();
                }
            }
        });
    }

    public void setListener(GridView gridView) {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ViewUserAlbumActivity.this, ViewAlbumActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("from","view_user");
                startActivity(intent);
            }
        });
    }

    public void setAdapterContents(List<ParseObject> objects) {
        parseObjects = objects;
        noAlbums.setVisibility(View.GONE);
        if (parseObjects.size() > 0) {
            gridView = (GridView) findViewById(R.id.gridViewUserViewAlbum);
            adapter = new ViewAlbumAdapter(this, parseObjects);
            adapter.setNotifyOnChange(true);
            gridView.setAdapter(adapter);
            setListener(gridView);
        }
    }
}