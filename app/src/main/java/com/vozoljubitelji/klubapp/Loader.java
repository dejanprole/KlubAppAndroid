package com.vozoljubitelji.klubapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by macosx on 9/26/17.
 */

class PostLoader extends AsyncTaskLoader<List<KlubPost>> {

    private static final String LOG_TAG = PostLoader.class.getSimpleName();

    private String mUrl;

    public PostLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading(){
        Log.i(LOG_TAG, "ONSTARTLOADIN");
        forceLoad();
    }

    @Override
    public List<KlubPost> loadInBackground() {
        Log.i(LOG_TAG, "LOADINBACKGROUND");
        if (mUrl == null) {
            return null;
        }
        List<KlubPost> klubPosts = QueryUtils.fetchNewData(mUrl);
        return klubPosts;
    }
}
