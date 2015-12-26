package com.example.aravindharaj.sociobot;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.AbstractCursor;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.BaseColumns;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    public static ArrayList<Value> user_details = new ArrayList<>();
    ProgressDialog pd = null;
    User user;
    Bitmap bitmap = null;
    private SimpleCursorAdapter mAdapter;
    String[] users;
    FloatingActionButton fab;
    FloatingActionButton fab1;
    String uname = new String();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
            menu.findItem(R.id.action_search).setVisible(false);
        } else {
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            final SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                    .getActionView();
            searchView.setQueryHint(getResources().getString(R.string.stringSearchHint));
            searchView.setSuggestionsAdapter(mAdapter);
            searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
                @Override
                public boolean onSuggestionSelect(int position) {
                    return false;
                }

                @Override
                public boolean onSuggestionClick(int position) {
                    String suggestion = users[position];
                    searchView.setQuery(suggestion, true);
                    return true;
                }
            });
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    populateAdapter(newText);
                    return false;
                }
            });
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void populateAdapter(final String query) {
        ParseQuery<ParseUser> user_query = ParseUser.getQuery();
        user_query.whereNotEqualTo("username", ParseUser.getCurrentUser().getEmail());
        user_query.whereEqualTo("privacy", "Public");
        user_query.whereContains("name", query);
        user_query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    users = new String[objects.size()];
                    for (int i = 0; i < objects.size(); i++) {
                        users[i] = objects.get(i).get("name").toString() + " <" + objects.get(i).getEmail() + ">";
                    }
                    final MatrixCursor c = new MatrixCursor(new String[]{BaseColumns._ID, "cityName"});
                    for (int i = 0; i < users.length; i++) {
                        if (users[i].toLowerCase().startsWith(query.toLowerCase()))
                            c.addRow(new Object[]{i, users[i]});
                    }
                    mAdapter.changeCursor(c);
                } else
                    e.printStackTrace();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            getSupportActionBar().setTitle("User Profile");
            pd = new ProgressDialog(ProfileActivity.this);
            pd.setMessage("Loading...");
            pd.show();
            String query = intent.getStringExtra(SearchManager.QUERY);
            String username = query.substring(query.indexOf("<") + 1, query.indexOf(">"));
            uname = username;
            ParseQuery<ParseUser> query1 = ParseUser.getQuery();
            query1.whereEqualTo("username", username);
            query1.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if (e == null) {
                        ParseUser user = objects.get(0);
                        new GetProfileValues().execute(user);
                    } else
                        e.printStackTrace();
                }
            });
        } else {
            getSupportActionBar().setTitle("My Profile");
            final String[] from = new String[]{"cityName"};
            final int[] to = new int[]{android.R.id.text1};
            mAdapter = new SimpleCursorAdapter(this, R.layout.layout_search, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            new GetProfileValues().execute(ParseUser.getCurrentUser());
        }
    }

    public class GetProfileValues extends AsyncTask<ParseUser, Void, User> {
        @Override
        protected User doInBackground(ParseUser... params) {
            user = new User();
            ParseUser p_user = params[0];
            user.setFirstname(p_user.get("firstname").toString());
            user.setLastname(p_user.get("lastname").toString());
            user.setUsername(p_user.get("name").toString());
            user.setGender(p_user.get("gender").toString());
            user.setDob(p_user.get("dob").toString());
            user.setEmail(p_user.getEmail());
            user.setPhone(p_user.get("phone").toString());
            user.setPrivacy(p_user.get("privacy").toString());
            if (p_user.getString("push").equals("true"))
                user.setPush("Push Enabled");
            else
                user.setPush("Push Disabled");
            ParseFile file = p_user.getParseFile("profile_pic");
            user.setProfile_pic(file);
            user_details = UserValues.userValues(ProfileActivity.this, p_user);
            return user;
        }

        @Override
        protected void onPostExecute(User user) {
            final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
            collapsingToolbarLayout.setTitle(user.getUsername());
            fab = (FloatingActionButton) findViewById(R.id.fab);
            fab1 = (FloatingActionButton) findViewById(R.id.fab1);
            if (!ParseUser.getCurrentUser().getEmail().equals("")) {
                if (!(user.getEmail().equals(ParseUser.getCurrentUser().getUsername()))) {
                    CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
                    p.setAnchorId(View.NO_ID);
                    fab.setLayoutParams(p);
                    fab.setVisibility(View.GONE);
                    CoordinatorLayout.LayoutParams p1 = (CoordinatorLayout.LayoutParams) fab1.getLayoutParams();
                    p1.setAnchorId(R.id.app_bar_layout);
                    fab1.setLayoutParams(p1);
                    fab1.setVisibility(View.VISIBLE);
                }
            }
            final ImageView imageView = (ImageView) findViewById(R.id.image);
            final ParseFile image = user.getProfile_pic();
            image.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        imageView.setImageBitmap(bitmap);
                    } else {
                        Toast.makeText(ProfileActivity.this, "Error retrieving profile picture", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            });
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                    startActivity(intent);
                }
            });

            fab1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProfileActivity.this, ViewUserAlbumActivity.class);
                    intent.putExtra("uname", uname);
                    startActivity(intent);
                }
            });

            RecyclerView rv = (RecyclerView) findViewById(R.id.recycler_view);
            rv.setHasFixedSize(true);
            LinearLayoutManager lm = new LinearLayoutManager(ProfileActivity.this);
            rv.setLayoutManager(lm);
            AppAdapter adapter = new AppAdapter(ProfileActivity.this, user_details);
            rv.setAdapter(adapter);
            pd.dismiss();
        }

        @Override
        protected void onPreExecute() {
            if (pd == null) {
                pd = new ProgressDialog(ProfileActivity.this);
                pd.setMessage("Loading up your profile...");
                pd.show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
            super.onBackPressed();
        } else {
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            ProfileActivity.this.finish();
        }
    }
}
