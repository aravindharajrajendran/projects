package com.example.aravindharaj.sociobot;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class AlbumFragmentActivity extends android.support.v4.app.Fragment {

    ProgressDialog pd = null;
    TextView noAlbums;
    GridView gridView;
    public static AlbumAdapter adapter;
    public static List<ParseObject> parseObjects = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_album_fragment, container, false);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.refresh_menu) {
            if (pd == null) {
                pd = new ProgressDialog(getActivity());
                pd.setMessage("Refreshing your Albums..");
                pd.show();
            }
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Album");
            query.orderByDescending("createdAt");
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
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
                        Snackbar.make(getActivity().findViewById(R.id.custom_coordinator_layout), "Error refreshing your Albums. Try again later!", Snackbar.LENGTH_LONG).show();
                        pd.dismiss();
                    }
                }
            });
        }

        return false;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading..");
        pd.show();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Album");
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                noAlbums = (TextView) getActivity().findViewById(R.id.textViewNoAlbums);
                if (e == null) {
                    if (objects.size() > 0) {
                        parseObjects.clear();
                        setAdapterContents(objects);
                        pd.dismiss();
                    } else {
                        pd.dismiss();
                        noAlbums.setVisibility(View.VISIBLE);
                    }
                } else {
                    pd.dismiss();
                    Snackbar.make(getActivity().findViewById(R.id.custom_coordinator_layout), "There was a problem in retrieving your albums", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    public void setListener(GridView gridView) {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ViewAlbumActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("from", "album_fragment");
                startActivity(intent);
            }
        });
    }

    public void setAdapterContents(List<ParseObject> objects) {
        parseObjects = objects;
        noAlbums.setVisibility(View.GONE);
        if (parseObjects.size() > 0) {
            gridView = (GridView) getActivity().findViewById(R.id.gridViewAlbum);
            adapter = new AlbumAdapter(getActivity(), parseObjects);
            adapter.setNotifyOnChange(true);
            gridView.setAdapter(adapter);
            setListener(gridView);
        }
    }
}
