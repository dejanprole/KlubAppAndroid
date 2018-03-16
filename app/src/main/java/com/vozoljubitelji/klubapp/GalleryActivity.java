package com.vozoljubitelji.klubapp;

import android.app.Activity;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Loader;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import junit.framework.Assert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * Created by macosx on 11/4/17.
 */

public class GalleryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<KlubPost>> {
    public static final String TAG = "GalleryActivity";
    public static final String EXTRA_NAME = "images";
    public static final String REFERENCE_ID = "referenceId";
    private static String URL1 = "http://klubljubiteljazeleznice-beograd.com/android_app_klub/getsinglepage.php?refid=";
    private static final int KLUB_LOADER_ID=2;
    private Toolbar toolbar;


    private KlubAdapterMain mAdapter;

    ProgressDialog pd;


    private ArrayList<String> images;
    private GalleryPagerAdapter _adapter;

    @BindView(R.id.pager)
    ViewPager _pager;
    @BindView(R.id.thumbnails)
    LinearLayout _thumbnails;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);


        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //getActionBar().setDisplayHomeAsUpEnabled(true); //BACK ARROW

        mAdapter = new KlubAdapterMain(this, new ArrayList<KlubPost>());

        images = new ArrayList<>();

        String referenceId = getIntent().getStringExtra(REFERENCE_ID);




        Log.i(TAG, referenceId.toString());

        Bundle argBundle = new Bundle();
        argBundle.putString(REFERENCE_ID, referenceId);

        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(KLUB_LOADER_ID, argBundle, this);

        } else {
            Log.i("GALLERY ACITIVTY", "Problem with loading data");

        }

        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);

        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);



//        for(int i=0 ; i<mAdapter.getCount() ; i++) {
//
//            KlubPost row = mAdapter.getItem(i);
//            String date = row.getmDate();
//            String title = row.getmTitle();
//            //images.add(images_row.toString());
//
//            //Log.i("GALLERY ACTIVITY DATE", date.toString());
//
//        }


//        Uri.Builder builder = new Uri.Builder();
//        builder.scheme("http").authority("klubljubiteljazeleznice-beograd.com").appendPath("android_app_klub").appendPath("getsinglepage.php");
//        String urlBuilder = builder.build().toString();


//        Assert.assertNotNull(images);
//
//        _adapter = new GalleryPagerAdapter(this);
//        _pager.setAdapter(_adapter);
//        _pager.setOffscreenPageLimit(6); // how many images to load into memory

//        _closeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "Close clicked");
//                finish();
//            }
//        });
    }



    @Override
    public Loader<List<KlubPost>> onCreateLoader(int id, Bundle args) {
        Uri uri = Uri.parse(URL1 + args.getString(REFERENCE_ID));
        return new PostLoader(this, uri.toString());    }



    @Override
    public void onLoadFinished(Loader<List<KlubPost>> loader, List<KlubPost> data) {

        mAdapter.clear();
        //mAdapter.addAll(data);

        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);

            if (mAdapter.getCount() == 0) {
                Log.i("GA ONLOADFINISHED", "JEDNAKO NULI");

            } else {
                Log.i("GA ONLOADFINISHED", "NIJE JEDNAKO NULI");
                for(int i=0 ; i<mAdapter.getCount() ; i++) {

                    KlubPost row = mAdapter.getItem(i);
                    String date = row.getmDate();
                    String title = row.getmTitle();
                    String text = row.getmText();
                    String author = row.getmAuthor();
                    String category = row.getmCategory();
                    Assert.assertNotNull(images);

                    List imagesList = row.getmImagesUrl();
                    for (int j=0; j<imagesList.size(); j++)
                    {

                        Log.i("G.ACTIVITYIMAGE", imagesList.get(j).toString());

                        images.add(imagesList.get(j).toString());
                    }
                    Typeface typefaceLight = Typeface.createFromAsset(this.getAssets(), "fonts/Dosis-Light.ttf");
                    Typeface typefaceBold = Typeface.createFromAsset(this.getAssets(), "fonts/Dosis-Bold.ttf");


                    //Log.i("GALLERYACTIVITYIMAGESS", images.toString());
                    TextView dateText = (TextView) findViewById(R.id.dateGallery);
                    dateText.setTypeface(typefaceLight);
                    dateText.setText(date);

                    TextView titleText = (TextView) findViewById(R.id.titleGallery);
                    titleText.setTypeface(typefaceBold);
                    titleText.setText(title);

                    TextView postText = (TextView) findViewById(R.id.textGallery);
                    postText.setTypeface(typefaceLight);
                    postText.setText(text);

                    TextView authorText = (TextView) findViewById(R.id.authorGallery);
                    authorText.setTypeface(typefaceLight);
                    authorText.setText("|  Objavio: "+author);

                    TextView categoryText = (TextView) findViewById(R.id.categoyGallery);
                    categoryText.setTypeface(typefaceLight);
                    categoryText.setText("|  " +category);




                    Log.i("GALLERY ACTIVITY DATE", date);



                    _adapter = new GalleryPagerAdapter(this);
                    _pager.setAdapter(_adapter);
                    _pager.setOffscreenPageLimit(6); // how many images to load into memory




                }



                Log.i("GALLERY ACTIVITY IMAGES", images.toString());


            }

        }

        Log.i("ON LOAD FINISHED", "ON LOAD FINISHED");
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onLoaderReset(Loader<List<KlubPost>> loader) {

        mAdapter.clear();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }


    class GalleryPagerAdapter extends PagerAdapter {

        Context _context;
        LayoutInflater _inflater;

        public GalleryPagerAdapter(Context context) {
            _context = context;
            _inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = _inflater.inflate(R.layout.pager_gallery_item, container, false);
            container.addView(itemView);

            // Get the border size to show around each image
            int borderSize = _thumbnails.getPaddingTop();

            // Get the size of the actual thumbnail image
            int thumbnailSize = ((FrameLayout.LayoutParams)
                    _pager.getLayoutParams()).bottomMargin - (borderSize*2);

            // Set the thumbnail layout parameters. Adjust as required
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(thumbnailSize, thumbnailSize);
            params.setMargins(1, 1, borderSize, 1);

            // You could also set like so to remove borders
            //ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
            //        ViewGroup.LayoutParams.WRAP_CONTENT,
            //        ViewGroup.LayoutParams.WRAP_CONTENT);

            final ImageView thumbView = new ImageView(_context);
            thumbView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            thumbView.setLayoutParams(params);
            thumbView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Thumbnail clicked");

                    // Set the pager position when thumbnail clicked
                    _pager.setCurrentItem(position);
                }
            });
            _thumbnails.addView(thumbView);

            final SubsamplingScaleImageView imageView =
                    (SubsamplingScaleImageView) itemView.findViewById(R.id.image);

            // Asynchronously load the image and set the thumbnail and pager view
            Glide.with(_context)
                    .load(images.get(position))
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                            imageView.setImage(ImageSource.bitmap(bitmap));
                            thumbView.setImageBitmap(bitmap);
                        }
                    });

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Log.i(TAG, "DESTROY ITEM");

            container.removeView((LinearLayout) object);
        }
    }

    //28102017191407
    //URL URL = "http://klubljubiteljazeleznice-beograd.com/android_app_klub/getsinglepage.php";


//    private class GetImages extends AsyncTask<String, Void, List<KlubPost>> {  //TREBA DA SE PROSLEDI ID POSTA DO ASYNCTASK?
//        @Override
//        protected List<KlubPost> doInBackground(String... strings) {
//
//            QueryUtils queryUtils = new QueryUtils();
//
//            String url = strings[0];
//
//
//
//            List<KlubPost> array = queryUtils.fetchNewData(url);
//
//
//
//
//            return array;
//        }
//
//        //@Override
//        protected void onPostExecute(KlubPost jsonStr) {
//
////            try {
////
////
////            } catch (JSONException e) {e.printStackTrace();}
//
//            String ajprobam = jsonStr.getmDate();
//            Log.i("AJPROBAM", ajprobam.toString());
//
//
//            //return jsonStr;
//
//        }


 //   }



}

