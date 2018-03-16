package com.vozoljubitelji.klubapp;

import android.content.Context;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;



/**
 * Created by macosx on 9/21/17.
 */
public class CategoryAdapter extends android.support.v13.app.FragmentStatePagerAdapter {

    public CategoryAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new MainFragment();
            case 1:
                return new GalleryFragment();
            case 2:
                return new PortfolioFragment();
            case 3:
                return new AboutUsFragment();


        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {

            case 0: return "Naslovna";
            case 1: return "Vesti";
            case 2: return "Portfolio";
            case 3: return "O nama";




        }
        return null;
    }



}
