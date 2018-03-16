package com.vozoljubitelji.klubapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;


/**
 * Created by macosx on 9/26/17.
 */

class KlubAdapter extends ArrayAdapter<KlubPost> {
    //public static final String LOG_TAG = KlubAdapter.class.getName();

    public KlubAdapter (Activity context, ArrayList<KlubPost> klubPosts) {
        super(context, 0, klubPosts);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView (int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;


        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        KlubPost klubPost = getItem(position);

        String category = klubPost.getmCategory();
        String date = klubPost.getmDate();
        String imageUrl = klubPost.getmMainImageUrl();
        String title = klubPost.getmTitle();

        Typeface typefaceBold = Typeface.createFromAsset(getContext().getAssets(), "fonts/Dosis-Bold.ttf");
        Typeface typefaceLight = Typeface.createFromAsset(getContext().getAssets(), "fonts/Dosis-Light.ttf");


        TextView categoryText = (TextView) listItemView.findViewById(R.id.category);
        categoryText.setTypeface(typefaceLight);
        categoryText.setText(category);

        TextView dateText = (TextView) listItemView.findViewById(R.id.date);
        dateText.setTypeface(typefaceLight);
        dateText.setText(date);

        TextView titleText = (TextView) listItemView.findViewById(R.id.title);
        titleText.setTypeface(typefaceBold);
        titleText.setText(title);

        ImageView imageView = (ImageView) listItemView.findViewById(R.id.background);

        Picasso.with(getContext()).load("http://" + imageUrl).config(Bitmap.Config.RGB_565).fit().centerCrop().into(imageView);

        return listItemView;
    }
}
