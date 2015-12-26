package com.example.aravindharaj.sociobot;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.GetDataCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Aravindharaj on 12/8/2015.
 */
public class ApprovalAdapter extends ArrayAdapter<ParseObject> {

    Context context;
    List<ParseObject> result;

    public ApprovalAdapter(Context context, List<ParseObject> result) {
        super(context, R.layout.layout_pending_approvals, result);
        this.context = context;
        this.result = result;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_pending_approvals, parent, false);
        }

        final ImageView approvalImage = (ImageView) convertView.findViewById(R.id.imageViewApprovalImage);
        TextView albumName = (TextView) convertView.findViewById(R.id.textViewAlbumNameValue);
        TextView addedBy = (TextView) convertView.findViewById(R.id.textViewAddedByValue);
        ImageView approveImage = (ImageView) convertView.findViewById(R.id.imageViewApprove);
        ImageView disapproveImage = (ImageView) convertView.findViewById(R.id.imageViewDisapprove);
        final ParseObject parseObject = result.get(position);
        final ParseObject album = parseObject.getParseObject("album");
        final ParseObject image = parseObject.getParseObject("photo");
        final ParseUser added_by = parseObject.getParseUser("added_by");
        try {
            albumName.setText(album.fetchIfNeeded().getString("name"));
            albumName.setSelected(true);
            addedBy.setText(added_by.fetchIfNeeded().get("name").toString());
            addedBy.setSelected(true);
            ParseFile file = image.fetch().getParseFile("file");
            file.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        approvalImage.setImageBitmap(bitmap);
                    } else {
                        e.printStackTrace();
                        Toast.makeText(context, "There was a problem in fetching the photo", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }
        approveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image.put("approved", "yes");
                image.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            try {
                                Map<String, Object> input = new HashMap<String, Object>();
                                input.put("title", "SocioBot - Your photo has been Approved");
                                input.put("alert", album.getParseUser("owner").fetchIfNeeded().get("name") + " has approved your photo for the album - " + album.fetchIfNeeded().getString("name") + ". Your photo is now visible in the Album");
                                input.put("users", added_by.getObjectId());
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
                            parseObject.deleteInBackground(new DeleteCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        PendingApprovalActivity.parseObjects.remove(position);
                                        if (PendingApprovalActivity.parseObjects.size() < 1)
                                            PendingApprovalActivity.noapprovals.setVisibility(View.VISIBLE);
                                        PendingApprovalActivity.adapter.notifyDataSetChanged();
                                        Toast.makeText(context, "Image has been approved and is now available in the album", Toast.LENGTH_LONG).show();
                                    } else {
                                        e.printStackTrace();
                                        Toast.makeText(context, "There was a problem in approving the image", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            e.printStackTrace();
                            Toast.makeText(context, "There was a problem in approving the image", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        disapproveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image.put("approved", "no");
                image.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            try {
                                Map<String, Object> input = new HashMap<String, Object>();
                                input.put("title", "SocioBot - Your photo has been Rejected");
                                input.put("alert", album.getParseUser("owner").fetchIfNeeded().get("name") + " has rejected your photo for the album - " + album.fetchIfNeeded().getString("name") + ". Your photo is removed from the album");
                                input.put("users", added_by.getObjectId());
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
                            parseObject.deleteInBackground(new DeleteCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        PendingApprovalActivity.parseObjects.remove(position);
                                        if (PendingApprovalActivity.parseObjects.size() < 1)
                                            PendingApprovalActivity.noapprovals.setVisibility(View.VISIBLE);
                                        PendingApprovalActivity.adapter.notifyDataSetChanged();
                                        Toast.makeText(context, "Image has been rejected successfully", Toast.LENGTH_LONG).show();
                                    } else {
                                        e.printStackTrace();
                                        Toast.makeText(context, "There was a problem in rejecting the image", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            e.printStackTrace();
                            Toast.makeText(context, "There was a problem in rejecting the image", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        return convertView;
    }
}
