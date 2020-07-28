package com.idmission.libtestproject.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class PageAdapter extends FragmentPagerAdapter {

    private int numOfTabs;
    private final List<Fragment> mFragmentList = new ArrayList<>();

    public PageAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        return mFragmentList.get(position);
    }

    public void addFrag(Fragment fragment) {
        mFragmentList.add(fragment);

    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
