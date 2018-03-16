package com.vozoljubitelji.klubapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by macosx on 9/26/17.
 */

class KlubAdapterMain extends ArrayAdapter<KlubPost> {
    public static final String LOG_TAG = KlubAdapterMain.class.getName();

    public KlubAdapterMain (Activity context, ArrayList<KlubPost> klubPosts) {
        super(context, 0, klubPosts);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView (int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;


        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_main, parent, false);
        }

        KlubPost klubPost = getItem(position);

        String imageUrl = klubPost.getmMainImageUrl();
        String title = klubPost.getmTitle();

        Typeface typefaceBold = Typeface.createFromAsset(getContext().getAssets(), "fonts/Dosis-Bold.ttf");
        Typeface typefaceLight = Typeface.createFromAsset(getContext().getAssets(), "fonts/Dosis-Light.ttf");

        TextView titleText = (TextView) listItemView.findViewById(R.id.title);
        titleText.setTypeface(typefaceBold);
        titleText.setText(title);

        ImageView imageView = (ImageView) listItemView.findViewById(R.id.background);

        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);

        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        imageView.setColorFilter(filter);

        Picasso.with(getContext()).load("http://" + imageUrl).config(Bitmap.Config.RGB_565).fit().centerCrop().into(imageView);

        return listItemView;
    }


}
