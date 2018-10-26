package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<News>>{

    private static final String LOG_TAG = NewsActivity.class.getName();

    /** URL for Guardian API */
    private static final String Guardian_URL =
            "https://content.guardianapis.com/search?api-key=";

     /** Constant value for loader ID */
     private static final int NEWS_LOADER_ID = 1;

     /** Adapter for the list */
     private NewsAdapter mAdapter;

     /** TextView that is displayed when the list is empty */
     private TextView mEmptyStateTextView;

     private ListView newsArticles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.news_activity );

        //Find ListView Layout
        newsArticles = findViewById( R.id.list);

        mEmptyStateTextView = findViewById( R.id.empty_view );
        newsArticles.setEmptyView( mEmptyStateTextView );

        //Create a new adapter for news input
        mAdapter = new NewsAdapter(this, new ArrayList<News>(  ));

        //Set adapter on ListView to populate
        newsArticles.setAdapter( mAdapter );

        //Set click listener to open news article in browser
        newsArticles.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                //Find the position of the article clicked
                News currentArticle = mAdapter.getItem( position );

                //Convert the string URL into a URI
                Uri newsUri = Uri.parse( currentArticle.getUrl() );

                //Create a new intent to view the URI
                Intent websiteIntent = new Intent( Intent.ACTION_VIEW, newsUri );

                //Send the intent to launch a new activity
                startActivity( websiteIntent );

            }
        });


        //Check network connection state
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService( Context.CONNECTIVITY_SERVICE);

        //Get network details
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        //If there is a network connection, fetch data
        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader( NEWS_LOADER_ID, null, this );
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }


    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        return new NewsLoader(this, Guardian_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newsList) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No News"
        mEmptyStateTextView.setText(R.string.no_news);

        // Clear the adapter of previous earthquake data
        //mAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (newsList != null && !newsList.isEmpty()) {
            mAdapter.addAll(newsList);

        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }


}

