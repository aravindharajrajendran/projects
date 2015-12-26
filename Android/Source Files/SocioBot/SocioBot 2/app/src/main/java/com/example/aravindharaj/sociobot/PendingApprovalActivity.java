package com.example.aravindharaj.sociobot;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class PendingApprovalActivity extends AppCompatActivity {

    ProgressDialog pd = null;
    public static List<ParseObject> parseObjects;
    ListView listView;
    public static ApprovalAdapter adapter;
    ImageView zoomImage;
    public static boolean flag = false;
    LinearLayout linearLayout;
    public static TextView noapprovals;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_approval);
        getSupportActionBar().setTitle("Pending Approvals");
        pd = new ProgressDialog(this);
        pd.setMessage("Loading..");
        pd.show();
        noapprovals = (TextView) findViewById(R.id.textViewNoPendingApprovals);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Approval");
        query.whereEqualTo("owner", ParseUser.getCurrentUser());
        query.whereEqualTo("approved", "no");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        parseObjects = objects;
                        listView = (ListView) findViewById(R.id.listViewPendingApprovals);
                        adapter = new ApprovalAdapter(PendingApprovalActivity.this, parseObjects);
                        adapter.setNotifyOnChange(true);
                        listView.setAdapter(adapter);
                        setListener(listView);
                        pd.dismiss();
                    } else {
                        noapprovals.setVisibility(View.VISIBLE);
                        pd.dismiss();
                    }
                } else {
                    pd.dismiss();
                    Toast.makeText(PendingApprovalActivity.this, "There was a problem in retrieving your pending approvals", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }

    public void setListener(ListView listView) {
        zoomImage = (ImageView) findViewById(R.id.imageViewZoomImage);
        linearLayout = (LinearLayout) findViewById(R.id.linear_layout_approval);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (flag == false) {
                    ImageView image = (ImageView) view.findViewById(R.id.imageViewApprovalImage);
                    Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
                    zoomImage.setImageBitmap(bitmap);
                    Animation fadeIn = new AlphaAnimation(0, 1);
                    fadeIn.setInterpolator(new AccelerateInterpolator());
                    fadeIn.setDuration(500);
                    fadeIn.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            linearLayout.setVisibility(View.VISIBLE);
                            flag = true;
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    linearLayout.startAnimation(fadeIn);
                }
            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
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
                            linearLayout.setVisibility(View.INVISIBLE);
                            flag = false;
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    linearLayout.startAnimation(fadeOut);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (flag == true) {
            if (linearLayout.getVisibility() == View.VISIBLE) {
                Animation fadeOut = new AlphaAnimation(1, 0);
                fadeOut.setInterpolator(new AccelerateInterpolator());
                fadeOut.setDuration(500);
                fadeOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        linearLayout.setVisibility(View.INVISIBLE);
                        flag = false;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                linearLayout.startAnimation(fadeOut);
            }
        } else {
            super.onBackPressed();
        }
    }
}
