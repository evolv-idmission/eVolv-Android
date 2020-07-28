package com.idmission.libtestproject.adapter;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.idmission.libtestproject.fragments.SelfieFaceImage;
import com.idmission.libtestproject.fragments.SelfieOvalFace;
import com.idmission.libtestproject.fragments.SelfieProcessedFace;

public class SelfiePageAdapter extends FragmentPagerAdapter {
    private Context _context;
    public static int totalPage = 2;

    public SelfiePageAdapter(Context context, FragmentManager fm) {
        super(fm);
        _context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = new Fragment();
        switch (position) {
            case 0:
                f = new SelfieFaceImage();
                break;
            case 1:
                f = new SelfieProcessedFace();
                break;
//            case 2:
//                f = new SelfieOvalFace();
//                break;
        }
        return f;
    }

    @Override
    public int getCount() {
        return totalPage;
    }

}

