package com.mantey.newsapp.activity;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mantey.newsapp.R;
import com.mantey.newsapp.adapter.NewsAdapter;
import com.mantey.newsapp.model.Story;
import com.mantey.newsapp.utils.NewsLoader;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Story>> {
    /**
     * URL for news data from the Guardian API
     */
    private static final String GUARDIAN_API_REQUEST_URL = "http://content.guardianapis.com/search";
    /**
     * Adapter for the list of news
     */
    private NewsAdapter mAdapter;
    private static final int NEWS_LOADER_ID = 1;
    private TextView mEmptyStateTextView;
    private TextView mEmptyStateTopTextView;
    private ImageView mEmptyStateImageView;
    private RelativeLayout mEmptyStateLayout;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the {@link ListView} in the layout
        ListView newsListView = (ListView) findViewById(R.id.list);
        mEmptyStateLayout = (RelativeLayout) findViewById(R.id.empty_view);
        mEmptyStateTopTextView = (TextView) findViewById(R.id.empty_view_top_text);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view_text);
        mEmptyStateImageView = (ImageView) findViewById(R.id.empty_view_image);
        mProgressBar = (ProgressBar) findViewById(R.id.loading_indicator);

        // Create a new adapter that takes an empty list of story as input
        mAdapter = new NewsAdapter(this, new ArrayList<Story>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        newsListView.setAdapter(mAdapter);
        newsListView.setEmptyView(mEmptyStateLayout);

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Find the current story that was clicked on
                Story currentStory = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri storyUri = Uri.parse(currentStory.getUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, storyUri);
                startActivity(websiteIntent);

            }
        });


        //Check for Internet Connectivity
        NetworkInfo networkInfo = getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            mProgressBar.setVisibility(View.GONE);

            mEmptyStateImageView.setImageResource(R.drawable.no_internet);
            mEmptyStateTopTextView.setText(getString(R.string.oh_no));
            mEmptyStateTextView.setText(getString(R.string.no_internet));
        }

    }

    @Override
    public Loader<List<Story>> onCreateLoader(int id, Bundle args) {

        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(GUARDIAN_API_REQUEST_URL);

        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value. For example, the `q=debates`
        uriBuilder.appendQueryParameter("q", "debates");
        uriBuilder.appendQueryParameter("api-key", "test");
        uriBuilder.appendQueryParameter("show-fields", "byline");

        // Return the completed uri  "http://content.guardianapis.com/search?q=debates&api-key=test&show-fields=byline"
        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Story>> loader, List<Story> news) {

        mProgressBar.setVisibility(View.GONE);

        mEmptyStateImageView.setImageResource(R.drawable.no_news);
        mEmptyStateTopTextView.setText(getString(R.string.oh_no));
        mEmptyStateTextView.setText(getString(R.string.no_news));

        mAdapter.clear();

        // If there is a valid list of {@link Story}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (news != null && !news.isEmpty()) {
            mAdapter.addAll(news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Story>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

}
