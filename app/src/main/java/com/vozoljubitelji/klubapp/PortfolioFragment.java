package com.vozoljubitelji.klubapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class PortfolioFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<KlubPost>> {

    public static final String LOG_TAG = "TEST";

    private static final String URL = "http://klubljubiteljazeleznice-beograd.com/android_app_klub/getallforportfolio.php";

    private static final String GALLERY_URl = "http://klubljubiteljazeleznice-beograd.com/add/28102017191407/resized/";

    private KlubAdapterMain mAdapter;

    private TextView mEmptyStateTextView;

    private static final int KLUB_LOADER_ID=1;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public PortfolioFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_portfolio, container, false);

        ListView postListView = (ListView) rootView.findViewById(R.id.list);
        mEmptyStateTextView = (TextView) rootView.findViewById(R.id.empty_view);
        postListView.setEmptyView(mEmptyStateTextView);

        Typeface typefaceLight = Typeface.createFromAsset(getContext().getAssets(), "fonts/Dosis-Light.ttf");


        TextView portfolioText = (TextView) rootView.findViewById(R.id.portfolioText);
        String portfolio = getString(R.string.portofolio_text);
        portfolioText.setTypeface(typefaceLight);

        portfolioText.setText(portfolio);

        mAdapter = new KlubAdapterMain(getActivity(), new ArrayList<KlubPost>());
        postListView.setAdapter(mAdapter);

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

}
