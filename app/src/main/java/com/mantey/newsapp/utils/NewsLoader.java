package com.mantey.newsapp.utils;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.support.annotation.Nullable;


import com.mantey.newsapp.model.Story;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<Story>> {

    private String mURL;

    public NewsLoader(Context context, String url) {
        super(context);
        mURL = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<Story> loadInBackground() {
        if (mURL == null) {
            return null;
        }

        return QueryUtils.fetchNewsData(mURL);
    }
}
