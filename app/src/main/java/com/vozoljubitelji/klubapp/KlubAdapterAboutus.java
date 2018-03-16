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

class KlubAdapterAboutus extends ArrayAdapter<KlubPost> {
    public static final String LOG_TAG = KlubAdapterMain.class.getName();

    public KlubAdapterAboutus (Activity context, ArrayList<KlubPost> klubPosts) {
        super(context, 0, klubPosts);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView (int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;


        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_aboutus, parent, false);
        }

        KlubPost klubPost = getItem(position);

        String imageUrl = klubPost.getmMainImageUrl();
        String title = klubPost.getmTitle();
        String text = klubPost.getmText();

        Typeface typefaceBold = Typeface.createFromAsset(getContext().getAssets(), "fonts/Dosis-Bold.ttf");
        Typeface typefaceLight = Typeface.createFromAsset(getContext().getAssets(), "fonts/Dosis-Light.ttf");

        TextView titleText = (TextView) listItemView.findViewById(R.id.title);
        titleText.setTypeface(typefaceBold);
        titleText.setText(title);

        TextView about = (TextView) listItemView.findViewById(R.id.about);
        about.setTypeface(typefaceLight);
        about.setText(text);


        ImageView imageView = (ImageView) listItemView.findViewById(R.id.background);

//        ColorMatrix matrix = new ColorMatrix();
//        matrix.setSaturation(0);
//
//        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
//        imageView.setColorFilter(filter);

        Glide.with(getContext()).load("http://" + imageUrl)

                .centerCrop()
                .dontAnimate()
                .into(imageView);

        return listItemView;
    }


}
