package com.example.aravindharaj.sociobot;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.parse.ParseInstallation;
import com.parse.ParseUser;

import java.util.jar.Manifest;

public class MainActivity extends AppCompatActivity {

    private static boolean exit_flag = false;
    public int[] drawable = {R.drawable.ic_message, R.drawable.ic_camera};
    ViewPager pager;
    FloatingActionButton fab;
    float positionOffsets;
    public static CoordinatorLayout coordinatorLayout;
    float multiplier = 1.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.custom_coordinator_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("SocioBot");
        pager = (ViewPager) findViewById(R.id.tabspager);
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment("", new MessageFragmentActivity());
        adapter.addFragment("", new AlbumFragmentActivity());
        pager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setIcon(drawable[i]);
        }
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ComposeMessage.class);
                intent.putExtra("from", "compose");
                startActivity(intent);
            }
        });
        int density = getResources().getDisplayMetrics().densityDpi;
        switch (density) {
            case DisplayMetrics.DENSITY_LOW:
                multiplier = 1.0f;
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                multiplier = 1.0f;
                break;
            case DisplayMetrics.DENSITY_HIGH:
                multiplier = 1.0f;
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                multiplier = 1.0f;
                break;
        }

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                positionOffsets = positionOffset;
                final int width = pager.getWidth();
                if (position == 0) {
                    int translationX = (int) ((-(width - fab.getWidth() - multiplier * fab.getWidth()) / 2f) * positionOffset);
                    fab.setTranslationX(translationX);
                    fab.setTranslationY(0);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    fab = (FloatingActionButton) findViewById(R.id.fab);
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, ComposeMessage.class);
                            intent.putExtra("from", "compose");
                            startActivity(intent);
                        }
                    });
                } else {
                    fab = (FloatingActionButton) findViewById(R.id.fab);
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, CreateAlbumActivity.class);
                            intent.putExtra("from", "main_activity");
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout) {
            ParseUser.getCurrentUser().logOut();
            ParseInstallation.getCurrentInstallation().remove("user");
            ParseInstallation.getCurrentInstallation().saveInBackground();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
        if (id == R.id.my_profile) {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        }

        if (id == R.id.pending_approvals) {
            Intent intent = new Intent(MainActivity.this, PendingApprovalActivity.class);
            startActivity(intent);
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (exit_flag) {
            super.onBackPressed();
        } else {
            Toast.makeText(MainActivity.this, "Press Back again to Exit", Toast.LENGTH_SHORT).show();
            exit_flag = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit_flag = false;
                }
            }, 3 * 1000);
        }
    }
}
