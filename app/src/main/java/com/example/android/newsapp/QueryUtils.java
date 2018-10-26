package com.example.android.newsapp;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/** Helper method for requesting and recieving data */

public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
        //Private Empty Constructor
    }

    /**
     * Query the dataset
     */
    public static List<News> fetchNewsData(String requestURL) {
        //create URL object
        URL url = createUrl( requestURL );

        //Send request for JSON
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest( url );
        } catch (IOException e) {
            Log.e( LOG_TAG, "Problem making the HTTP Request.", e );
        }

        //Extract fields
        List<News> newsArticles = extractResultsFromJson( jsonResponse );

        //Return the list
        return newsArticles;
    }


    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL( stringUrl );
        } catch (MalformedURLException e) {
            Log.e( LOG_TAG, "Problem building the URL ", e );
        }
        return url;

    }

    /** Make HTTP Request */
    private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse = "";

        //If URL is Null, Return Early
        if (url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout( 1000 /* milliseconds */ );
            urlConnection.setConnectTimeout( 15000 /* milliseconds */ );
            urlConnection.setRequestMethod( "GET" );
            urlConnection.connect();

            //Successful connection == code 200, parse response
            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else {
                Log.e(LOG_TAG,"Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG,"Problem retrieving the News JSON response", e);
        }finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /** Convert the InputStream into a string */

    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder(  );
        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader( inputStream, Charset.forName( "UTF-8" ) );
            BufferedReader reader = new BufferedReader( inputStreamReader );
            String line = reader.readLine();
            while (line !=null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /** Return a list of objects built from parsing JSON */
    private static List<News> extractResultsFromJson(String newsJSON){
        String webSectionName;
        String webPublicationDate;
        String webTitle;
        String webUrl;
        String byLine;

        if (TextUtils.isEmpty( newsJSON )){
            Log.v(LOG_TAG, "The JSON string is empty or Null");
            return null;
        }

        List<News> newsArticles = new ArrayList<>(  );

        try{
            JSONObject jsonObjectRoot = new JSONObject( newsJSON );
            JSONObject jsonObjectResponse = jsonObjectRoot.getJSONObject( "response" );
            JSONArray jsonArrayResults = jsonObjectResponse.getJSONArray( "results" );

            for (int i = 0; i < jsonArrayResults.length(); i++){

                JSONObject currentArticle = jsonArrayResults.getJSONObject(i);
                JSONObject jsonObjectFields = currentArticle.getJSONObject( "fields" );

                webSectionName = currentArticle.optString( "sectionName" );
                webPublicationDate = currentArticle.optString( "webPublicationDate" );
                webTitle = jsonObjectFields.getString( "headline" );
                webUrl = jsonObjectFields.getString( "shortUrl" );
                byLine = jsonObjectFields.optString( "byline" );

                newsArticles.add( new News(
                        webSectionName,
                        webPublicationDate,
                        webTitle,
                        webUrl,
                        byLine
                ) );
            }
        } catch (JSONException e){
            Log.e(LOG_TAG,"QueryUtilis: Problem Parsing Article",e);
        }
        return newsArticles;


    }

}