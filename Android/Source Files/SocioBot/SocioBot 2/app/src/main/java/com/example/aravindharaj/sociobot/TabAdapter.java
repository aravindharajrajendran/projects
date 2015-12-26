package com.example.aravindharaj.sociobot;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aravindharaj on 11/22/2015.
 */
public class TabAdapter extends FragmentPagerAdapter {

    List<Fragment> FragmentList = new ArrayList<Fragment>();
    List<String> FragmentTitle = new ArrayList<String>();

    public TabAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public void addFragment(String title, Fragment fragment) {
        FragmentTitle.add(title);
        FragmentList.add(fragment);
    }

    @Override
    public int getCount() {
        return FragmentList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return FragmentList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }
}
