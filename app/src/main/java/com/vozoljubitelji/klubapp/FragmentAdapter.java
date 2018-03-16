package com.vozoljubitelji.klubapp;


import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

/**
 * Created by macosx on 10/23/17.
 */

class FragmentAdapter extends FragmentPagerAdapter implements Adapter {
    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MainFragment mainFragment = new MainFragment();
                return mainFragment;

            case 1:
                GalleryFragment photosFragment = new GalleryFragment();
                return photosFragment;
            case 2:
                PortfolioFragment portfolioFragment = new PortfolioFragment();
                //return portfolioFragment;
            case 3:
                AboutUsFragment aboutUsFragment = new AboutUsFragment();
                //return aboutUsFragment;


            //default:
            //    return new MainFragment();

        }
        return null;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public int getCount() {
        return 4;
    }

}
