package com.vozoljubitelji.klubapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class GalleryRollingStockFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<KlubPost>>{


    public static final String LOG_TAG = "TEST";

    private static final String URL = "http://klubljubiteljazeleznice-beograd.com/android_app_klub/getallfromgalleryrollingstock.php";
    private KlubAdapter mAdapter;

    private TextView mEmptyStateTextView;

    private static final int KLUB_LOADER_ID=2;

    private ListView postListView;

    private static Parcelable mListViewScrollPos = null;

    Parcelable state;




    public GalleryRollingStockFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putBoolean("mIsBackbuttonisPressed", mIsBackbuttonisPressed);

        mListViewScrollPos = postListView.onSaveInstanceState();
        outState.putParcelable("STATE", postListView.onSaveInstanceState());


    }

    @Override
    public void onPause() {
        // Save ListView state @ onPause
        Log.d("NESTO", "saving listview state @ onPause");
        state = postListView.onSaveInstanceState();
        super.onPause();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null) {
            mListViewScrollPos = savedInstanceState.getParcelable("STATE");
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


        postListView = (ListView) rootView.findViewById(R.id.list);


        // Restore the ListView position
        if (mListViewScrollPos != null) {
            postListView.onRestoreInstanceState(mListViewScrollPos);
        }

        mEmptyStateTextView = (TextView) rootView.findViewById(R.id.empty_view);
        postListView.setEmptyView(mEmptyStateTextView);



        mAdapter = new KlubAdapter(getActivity(), new ArrayList<KlubPost>());

        //Parcelable state = postListView.onSaveInstanceState();


        postListView.setAdapter(mAdapter);


        if(state != null) {
            Log.d("NESTO", "trying to restore listview state..");
            postListView.onRestoreInstanceState(state);
        }






        if (postListView.getAdapter() == null) {

            Log.i("GETADAPTER", "NULL");

        } else {

            Log.i("GETADAPTER", "NOT NULL");}



//        postListView.setSelectionFromTop(index, top);
//        Log.i("POZICIJA2", String.valueOf(index));


        postListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                KlubPost currentKlubPost = mAdapter.getItem(position);
                Uri klubPostUri = Uri.parse(currentKlubPost.getmReferenceId());

                String author = currentKlubPost.getmAuthor();
                Log.i("AUTHOR", author);

                String mainImageUrl = currentKlubPost.getmMainImageUrl();
                Log.i("MAIN IMAGE", mainImageUrl);

                List imagesUrl = currentKlubPost.getmImagesUrl();
                for (int i=0; i<imagesUrl.size(); i++) {
                    Log.i("IMAGES", imagesUrl.toString());
                }

                Log.i("GALLERYF. KLUB POST URI",klubPostUri.toString());

                Uri postText = Uri.parse(currentKlubPost.getmText());
                Log.i("POSTTEXT", postText.toString());

                Intent intent = new Intent(getActivity(), GalleryActivity.class);
                intent.putExtra(GalleryActivity.REFERENCE_ID, klubPostUri.toString());
                startActivity(intent);
//                startActivityForResult(intent, 0);




//                Fragment someFragment = new PrivacyFragment();
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                //transaction.hide(getFragmentManager().findFragmentByTag("GalleryFragment"));
//                transaction.replace(R.id.frame, someFragment ); // give your fragment container id in first parameter
//
//                //transaction.replace(R.id.fragment, someFragment ); // give your fragment container id in first parameter
//                //transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
//                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
//                //transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//
//                transaction.commit();

//                FragmentTransaction ft =getFragmentManager().beginTransaction();
//                FragmentManager fm;
//                ft = fm.beginTransaction();
//                ft.hide(getFragmentManager().findFragmentByTag("searchFragment"));
//                ft.add(R.id.main_fragment, yourDetailfragment);
//                ft.addToBackStack(null);
//                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                ft.commit();


            }
        });




        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            Log.i(LOG_TAG, "TEST Calling initloader");
            loaderManager.initLoader(KLUB_LOADER_ID, null, this);

        } else {
            View loadingIndicator = rootView.findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText("Jebmgliga");
        }
        return rootView;

    }






    @Override
    public Loader<List<KlubPost>> onCreateLoader(int id, Bundle args) {
        Uri uri = Uri.parse(URL);
        Log.i(LOG_TAG, "ONCREATELOADER");
        return new PostLoader(getActivity(), uri.toString());
    }


    @Override
    public void onLoadFinished(Loader<List<KlubPost>> loader, List<KlubPost> data) {
        Log.i(LOG_TAG, "TEST load finished");
        View loadingIndicator = getView().findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        mEmptyStateTextView.setText("Nema postova za ucitavanje");

        mAdapter.clear();


        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<KlubPost>> loader) {
        mAdapter.clear();
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }




}
