package com.vozoljubitelji.klubapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.Context;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class AboutUsFragment extends Fragment implements LoaderCallbacks<List<KlubPost>> {

    public static final String LOG_TAG = "TEST";

    private static final String URL = "http://klubljubiteljazeleznice-beograd.com/android_app_klub/getallpostsaboutus.php";

    private KlubAdapterAboutus mAdapter;

    private TextView mEmptyStateTextView;

    private static final int KLUB_LOADER_ID=1;


    public AboutUsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_about_us, container, false);

        ListView postListView = (ListView) rootView.findViewById(R.id.list);
        mEmptyStateTextView = (TextView) rootView.findViewById(R.id.empty_view);
        postListView.setEmptyView(mEmptyStateTextView);

        mAdapter = new KlubAdapterAboutus(getActivity(), new ArrayList<KlubPost>());
        postListView.setAdapter(mAdapter);


        Typeface typefaceLight = Typeface.createFromAsset(getContext().getAssets(), "fonts/Dosis-Light.ttf");

        TextView team = (TextView) rootView.findViewById(R.id.teamText);

        team.setTypeface(typefaceLight);

//        postListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                KlubPost currentKlubPost = mAdapter.getItem(position);
//                Uri klubPostUri = Uri.parse(currentKlubPost.getmReferenceId());
//                Intent intent = new Intent(getActivity(), GalleryActivity.class);
//                intent.putExtra(GalleryActivity.REFERENCE_ID, klubPostUri.toString());
//                startActivity(intent);
//
//            }
//        });

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            Log.i(LOG_TAG, "TEST Calling initloader");
            loaderManager.initLoader(KLUB_LOADER_ID, null, this);

        } else {
            View loadingIndicator = rootView.findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText("Problem sa učitavanjem");
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

        mEmptyStateTextView.setText("Nema postova za učitavanje. Proverite internet konekciju");

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
