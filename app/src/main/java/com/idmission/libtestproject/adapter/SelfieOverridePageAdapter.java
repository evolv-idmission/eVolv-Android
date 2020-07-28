package com.idmission.libtestproject.adapter;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.idmission.libtestproject.fragments.SelfieOverrideFaceImage;
import com.idmission.libtestproject.fragments.SelfieOverrideOvalFace;
import com.idmission.libtestproject.fragments.SelfieOverrideProcessedFace;

public class SelfieOverridePageAdapter extends FragmentPagerAdapter {
    private Context _context;
    public static int totalPage = 3;

    public SelfieOverridePageAdapter(Context context, FragmentManager fm) {
        super(fm);
        _context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = new Fragment();
        switch (position) {
            case 0:
                f = new SelfieOverrideFaceImage();
                break;
            case 1:
                f = new SelfieOverrideProcessedFace();
                break;
            case 2:
                f = new SelfieOverrideOvalFace();
                break;
        }
        return f;
    }

    @Override
    public int getCount() {
        return totalPage;
    }

}
