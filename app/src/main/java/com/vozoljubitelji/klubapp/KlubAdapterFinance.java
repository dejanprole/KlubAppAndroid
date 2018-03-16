package com.vozoljubitelji.klubapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;


/**
 * Created by macosx on 9/26/17.
 */

class KlubAdapterFinance extends ArrayAdapter<KlubPost> {
    public static final String LOG_TAG = KlubAdapterMain.class.getName();

    public KlubAdapterFinance (Activity context, ArrayList<KlubPost> klubPosts) {
        super(context, 0, klubPosts);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView (int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;


        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_finance, parent, false);
        }

        KlubPost klubPost = getItem(position);

        String incomeString = klubPost.getmTitle();
        String yearString = klubPost.getmTitle();

        Typeface typefaceBold = Typeface.createFromAsset(getContext().getAssets(), "fonts/Dosis-Bold.ttf");
        Typeface typefaceLight = Typeface.createFromAsset(getContext().getAssets(), "fonts/Dosis-Light.ttf");

        TextView incomeText = (TextView) listItemView.findViewById(R.id.income);
        incomeText.setTypeface(typefaceBold);
        incomeText.setText(incomeString);

        TextView yearText = (TextView) listItemView.findViewById(R.id.year);
        yearText.setTypeface(typefaceLight);
        yearText.setText(yearString);

        return listItemView;
    }


}
