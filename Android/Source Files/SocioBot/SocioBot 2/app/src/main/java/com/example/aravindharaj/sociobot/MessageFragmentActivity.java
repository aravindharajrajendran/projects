package com.example.aravindharaj.sociobot;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.wdullaer.swipeactionadapter.SwipeActionAdapter;
import com.wdullaer.swipeactionadapter.SwipeDirection;

import java.util.List;

public class MessageFragmentActivity extends Fragment {

    ProgressDialog pd = null;
    ProgressDialog pd1 = null;
    TextView noMessages;
    ListAdapter adapter;
    ParseObject object;
    ListView listView;
    SwipeActionAdapter mAdapter;
    public static List<ParseObject> parseObjects;
    public static int REQ_CODE = 1002;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_message_fragment, container, false);
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
            refresh();
        }
        return false;
    }

    public void refresh() {
        if (pd == null) {
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Refreshing your Messages..");
            pd.show();
        }
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Message");
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        setAdapterContents(objects);
                    } else {
                        noMessages.setVisibility(View.VISIBLE);
                    }
                } else {
                    Snackbar.make(getActivity().findViewById(R.id.custom_coordinator_layout), "Error refreshing your messages. Try again later!", Snackbar.LENGTH_LONG).show();
                }
            }
        });
        if (pd != null)
            pd.dismiss();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        pd1 = new ProgressDialog(getActivity());
        pd1.setMessage("Loading..");
        pd1.show();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Message");
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> objects, ParseException e) {
                noMessages = (TextView) getActivity().findViewById(R.id.textViewNoMessages);
                if (e == null) {
                    if (objects.size() > 0) {
                        setAdapterContents(objects);
                        pd1.dismiss();
                    } else {
                        pd1.dismiss();
                        noMessages.setVisibility(View.VISIBLE);
                    }
                } else {
                    pd1.dismiss();
                    Snackbar.make(getActivity().findViewById(R.id.custom_coordinator_layout), "There was a problem in retrieving your messages", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    public void setListener(final ListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ViewMessageActivity.class);
                intent.putExtra("position", position);
                startActivityForResult(intent, REQ_CODE);
            }
        });
    }

    public void setAdapterContents(final List<ParseObject> objects) {
        parseObjects = objects;
        noMessages.setVisibility(View.GONE);
        if (parseObjects.size() > 0) {
            listView = (ListView) getActivity().findViewById(R.id.listView2);
            adapter = new ListAdapter(getActivity(), parseObjects);
            adapter.setNotifyOnChange(true);
            mAdapter = new SwipeActionAdapter(adapter);
            mAdapter.setListView(listView);
            listView.setAdapter(mAdapter);
            mAdapter.addBackground(SwipeDirection.DIRECTION_FAR_LEFT, R.layout.layout_left).addBackground(SwipeDirection.DIRECTION_NORMAL_LEFT, R.layout.layout_left).addBackground(SwipeDirection.DIRECTION_FAR_RIGHT, R.layout.layout_right).addBackground(SwipeDirection.DIRECTION_NORMAL_RIGHT, R.layout.layout_right);
            mAdapter.setSwipeActionListener(new SwipeActionAdapter.SwipeActionListener() {
                @Override
                public boolean hasActions(int position, SwipeDirection direction) {
                    if (direction.isLeft())
                        return true;
                    if (direction.isRight())
                        return true;
                    return false;
                }

                @Override
                public boolean shouldDismiss(int position, SwipeDirection direction) {
                    return false;
                    //direction == SwipeDirection.DIRECTION_NORMAL_LEFT;
                }

                @Override
                public void onSwipe(int[] positionList, SwipeDirection[] directionList) {
                    for (int i = 0; i < positionList.length; i++) {
                        SwipeDirection direction = directionList[i];
                        final int position = positionList[i];
                        String dir = "";

                        switch (direction) {
                            case DIRECTION_NORMAL_LEFT:
                                dir = "Left";
                            case DIRECTION_FAR_LEFT:
                                dir = "Far left";
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("Delete Message");
                                builder.setMessage("Are you sure you want to delete this message?");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        pd = new ProgressDialog(getActivity());
                                        pd.setMessage("Deleting Message...");
                                        pd.show();
                                        object = parseObjects.get(position);
                                        ParseQuery<ParseObject> flagQuery = ParseQuery.getQuery("Flag");
                                        flagQuery.whereEqualTo("msg", object);
                                        flagQuery.whereEqualTo("user", ParseUser.getCurrentUser());
                                        flagQuery.findInBackground(new FindCallback<ParseObject>() {
                                            @Override
                                            public void done(List<ParseObject> objects, ParseException e) {
                                                if (e == null) {
                                                    for (int i = 0; i < objects.size(); i++) {
                                                        ParseObject f_object = objects.get(i);
                                                        f_object.deleteInBackground();
                                                    }
                                                } else {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        List<ParseUser> userList = object.getList("to");
                                        userList.remove(ParseUser.getCurrentUser());
                                        object.put("to", userList);
                                        ParseACL acl = object.getACL();
                                        acl.setReadAccess(ParseUser.getCurrentUser(), false);
                                        acl.setWriteAccess(ParseUser.getCurrentUser(), false);
                                        object.setACL(acl);
                                        object.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                    parseObjects.remove(position);
                                                    adapter.notifyDataSetChanged();
                                                    if (parseObjects.size() < 1) {
                                                        noMessages.setVisibility(View.VISIBLE);
                                                    }
                                                    pd.dismiss();
                                                    Snackbar.make(getActivity().findViewById(R.id.custom_coordinator_layout), "Message has been deleted successfully!", Snackbar.LENGTH_LONG).show();
                                                } else {
                                                    pd.dismiss();
                                                    Snackbar.make(getActivity().findViewById(R.id.custom_coordinator_layout), "There was a problem in deleting your message", Snackbar.LENGTH_LONG).show();
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
                                break;
                            case DIRECTION_FAR_RIGHT:
                                dir = "Far right";
                            case DIRECTION_NORMAL_RIGHT:
                                dir = "Right";
                                Intent intent = new Intent(getActivity(), ComposeMessage.class);
                                intent.putExtra("from", "reply");
                                intent.putExtra("position", position);
                                startActivity(intent);
                                break;
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }
            });
            setListener(listView);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE) {
            refresh();
        }
    }
}
