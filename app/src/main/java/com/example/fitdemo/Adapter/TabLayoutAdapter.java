package com.example.fitdemo.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by 最美人间四月天 on 2018/1/23.
 */

public class TabLayoutAdapter extends FragmentStatePagerAdapter {

    private String[] titles;
    private List<Fragment> mFragmentList;

    public TabLayoutAdapter(FragmentManager fm, String[] titles, List<Fragment> fragments) {
        super(fm);
        this.titles = titles;
        this.mFragmentList = fragments;
    }

    @Override
    public Fragment getItem(int arg0) {
        return mFragmentList.get(arg0);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}


